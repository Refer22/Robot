package robot.robot;

/**
 * @author 0.2
 * No AWT
 */
public class Point {
    private double _x;
    private double _y;
    
    /**
     * @param x
     * @param y
     */
    public Point(double x, double y) {
        _x = x;
        _y = y;
    }
    
    /**
     * 
     */
    public Point() {
		_x = 0.0;
		_y = 0.0;
	}
    
    /**
     * @return
     */
    public double getX()    {
        return _x;
    }
    
    /**
     * @return
     */
    public double getY()    {
        return _y;
    }
    
    /**
     * @param x
     */
    public void setX(double x)    {
        _x = x;
    }
    
    /**
     * @param y
     */
    public void setY(double y)    {
        _y = y;
    }    
} 
