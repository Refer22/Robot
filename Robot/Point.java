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
public class Point {
    private double _x;
    private double _y;
    
    public Point(double x, double y) {
        _x = x;
        _y = y;
    }
    
    public void desplazar(double dx, double dy){
        _x += dx;
        _y += dy;
    }
    
    public double getX()    {
        return _x;
    }
    
    public double getY()    {
        return _y;
    }
    
    public void setX(double x)    {
        _x = x;
    }
    
    public void setY(double y)    {
        _y = y;
    }
    public void updatePoint(Point newPoint)    {
        _x = newPoint.getX();
        _y = newPoint.getY();
    }
    
} 
