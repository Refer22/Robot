package robot.robot;

import robot.start.StartTask;
import robot.start.StartTask.TSync;

public class Robot  implements Runnable {
    final double R_EARTH = 6371;
	public static boolean stopRobot = false;
    
    private double lastDistanceReport = 0;
    public double distance = 0;

    public Place plzBPalace = null;
    public Place plzTEstation = null;

    
    private int minute = 0;
    private Point _init;
    private Point _offset;//robot current position
    TSync pc2;
	
    public Robot(Point init, TSync pc) {
    	pc2 = pc;
    	init(init);
    }
    
	private Point getInit()	{
    	return _init;
    }
    
    private Point getOffset()	{
    	return _offset;
    }
    
    public void init(Point init)	{
    	_init = init;
        _offset = _init;//1st point offset is 0
        plzBPalace = new Place("Buckingham Palace", new Point(51.50129, -0.14193));
        plzTEstation = new Place("Temple Station", new Point(51.51085, -0.11416));
    }
    
    public void run() {
    	while (true) {
            try {
            	Thread.sleep(60000);
            	minute++;
            	distance += newPointPreparation();
                distanceToPrevius();
                isOnPlace();
                isTakeMeasure();
                isTimeReport();                
            } catch (InterruptedException ex) {
            	System.err.println("Error! Shutdown...");
            	System.exit(1);                
            }
            try {
            	while(StartTask.stop)	{
            		System.out.println("Robot Waiting");
    				pc2.pause();    
    				System.out.println("Robot Restarted");
            	}

			} catch (InterruptedException e) {
				System.err.println("Error! Robot Thread sundely stopped" + e.getMessage());
			}
        }
    }
    
    private void isOnPlace()	{
    	if (currentDistanceToKnownPoint(plzBPalace.location) 
        		|| currentDistanceToKnownPoint(plzTEstation.location))	{
        	StartTask.report(new Measure(getOffset(), Math.random() * 200, minute));
        }
    }
    
    private void isTakeMeasure()	{
    	if (distance - lastDistanceReport > StartTask.distanceBetweenMeasures)	{
        	lastDistanceReport = distance;
        	StartTask.measures.add(new Measure(getOffset(), Math.random() * 200, minute));
        }
    }
    
    private void isTimeReport()	{
    	if (minute % 15 == 0)   {
        	StartTask.reportLog();
        }
    }
    
    public void reRoute(Point newInit)	{
    	minute = 0;
        distance = 0;
    	init(newInit);
    	System.out.println("Robot New route selected");
    }

    // number of km per degree = ~111km (111.32 in google maps, but range varies
    // between 110.567km at the equator and 111.699km at the poles)
    // 1km in degree = 1 / 111.32km = 0.0089
    // 1m in degree = 0.0089 / 1000 = 0.0000089
	private double newPointPreparation() {
        Triangle triangle = new Coordinates(getOffset()).calculateNextCord();
		double coef = triangle.get_Hypotenuse() * 0.0000089;	
		double new_lat = getInit().getX() + coef;
		double new_long = getInit().getY() + coef / Math.cos(getInit().getX() * 0.018);
		_offset = new Point(new_lat, new_long);

		return triangle.get_Hypotenuse();     
	}
    
    public double distanceToPrevius() {
        Point aux = new Point();
    	aux.setX(Math.toRadians(getOffset().getX() - getInit().getX()));
        aux.setY(Math.toRadians(getOffset().getY() - getInit().getY()));
        
        return getSegment(aux, Math.cos(Math.toRadians(getOffset().getX())));
    }
    
    public boolean currentDistanceToKnownPoint(Point station) {   	
    	Point aux = new Point();
    	aux.setX(Math.toRadians(getOffset().getX() - station.getX()));
        aux.setY(Math.toRadians(getOffset().getY() - station.getY()));

        if (getSegment(aux, Math.cos(Math.toRadians(station.getX()))) < 100)	{
        	return true;
        }
        return false;
    }
    
    private double getSegment(Point aux, double cosXTarget)	{
    	double a = Math.sin(aux.getX() / 2) * Math.sin(aux.getX() / 2)
                + Math.cos(Math.toRadians(getOffset().getX())) * cosXTarget
                * Math.sin(aux.getY() / 2) * Math.sin(aux.getY() / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double distance = (R_EARTH * c) * 1000;
        distance = Math.pow(distance, 2) + 1;
        
        return Math.sqrt(distance);
    }
}
