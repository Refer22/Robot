/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Robot;

/**
 *
 * @author 0.2
 */
public class Coordinates {
    private Point _init = null;
    private Point _end = null;
    
    public Coordinates(Point init, Point end)   {
        _init = init;
        _end = end;
    }
    
    public Triangle calculateNextCord()   {
        double step = (Math.random() % 2) + 1;
        Angle myAngle = new Angle( getInit(), getEnd());
        double catA = step * Math.sin( myAngle.getAngleDeg());
        double catO = step * Math.cos( myAngle.getAngleDeg());
        
        return new Triangle( catA, catO, step); 
    }
    
    public Point getInit()    {
        return _init;
    }
    
    public Point getEnd()    {
        return _end;
    }
}
