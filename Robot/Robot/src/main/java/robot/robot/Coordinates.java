package robot.robot;

public class Coordinates {
    private Point _init = null;
    private Point _seed = new Point(0.5 , 0.5);
    
    public Coordinates(Point init)   {
        _init = init;
    }
    
  //Know angle and 2 points
    public Triangle calculateNextCord()   {
        double step = (Math.random() % 2) + 1;
        Angle myAngle = new Angle( getInit(), getSeed());
        double catA = step * Math.sin( myAngle.getAngleDeg());
        double catO = step * Math.cos( myAngle.getAngleDeg());
        
        return new Triangle( catA, catO, step); 
    }
    
    public Point getInit()    {
        return _init;
    }
    
    private Point getSeed()	{
    	return _seed;
    }
}
