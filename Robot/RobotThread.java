package Robot;

public class RobotThread implements Runnable {
    final double R_EARTH = 6378137;
    
    private Point _init;
    private Point _offset;//robot current position
    private double lastMesure = 0;
    private int minute = 0;
    public Place plzBPalace = null;
    public Place plzTEstation = null;
    
    public RobotThread(Point init) {
        _init = init;
        _offset = _init;//1st point offset is 0
        //plzBPalace = new Place("Buckingham Palace", new Point(51.50129, -0.14193));
        //plzTEstation = new Place("Temple Station", new Point(51.51085, -0.11416));
    }
    
    private Point getInit()	{
    	return _init;
    }
    
    private Point getOffset()	{
    	return _offset;
    }
    
    @Override
    public void run() {
    	while (true) {
            try {
                Robot.distance += preparation();
                calculation();
                distance();
                /*
                if (distance(plzBPalace.location) 
                		|| distance(plzTEstation.location))	{
                	Robot.report(new Measure(getOffset(), Math.random() * 200, minute));
                }
                */
                System.out.println("asdasdasdasd");
                if (Robot.distance - lastMesure % 100 <= 3)	{
                	lastMesure = Robot.distance;
                	Robot.measures.add(new Measure(getOffset(), Math.random() * 200 , minute));
                }
                if (minute % 15 == 0)   {
                    Robot.report();
                }
                
		    Thread.sleep(60000);
		    minute++;
		} catch (InterruptedException ex) {}
            System.err.print("Error!, shutdown!");
            System.exit(1);
    	}
    }
    
    private double preparation() {
        Triangle triangle = new Coordinates(getOffset()).calculateNextCord();

        _offset = new Point(
        		getInit().getX() + getOffset().getX() * 180 / Math.PI,
        		getInit().getY() + getOffset().getY() * 180 / Math.PI
        );
        
        return triangle.get_Hypotenuse();
    }
    
    private void calculation()  {
		getOffset().setX(getInit().getX() + getOffset().getX() * 180 / Math.PI);
        getOffset().setY(
        		getInit().getY() + getOffset().getY() * 180 
        		/ Math.PI / Math.cos(getInit().getX() * Math.PI / 180));
    }
    
    public double distance() {
        double latDistance = Math.toRadians(getOffset().getX() - getInit().getX());
        double lonDistance = Math.toRadians(getOffset().getY() - getInit().getY());
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(getInit().getX())) * Math.cos(Math.toRadians(getOffset().getX()))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double distance = R_EARTH * c / 1000; // convert to meters
        distance = Math.pow(distance, 2) + 1;
        return Math.sqrt(distance);
    }
    
    public boolean distance(Point station) {
        double latDistance = Math.toRadians(getOffset().getX() - station.getX());
        double lonDistance = Math.toRadians(getOffset().getY() - station.getY());
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(getOffset().getX())) * Math.cos(Math.toRadians(station.getX()))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double distance = R_EARTH * c / 1000; // convert to meters

        distance = Math.pow(distance, 2) + 1;
        if (Math.sqrt(distance) < 100)	{
        	return true;
        }
        return false;
    }
}
