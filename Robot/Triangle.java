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
public class Triangle {
    private double _catA = 0;
    private double _catO = 0;
    private double _hypotenuse = 0;
    
    Triangle(double catA, double catO, double hip)    {
        _catA = catA;
        _catO = catO;
        _hypotenuse = hip;
    }
    
    public double get_catA()    {
        return _catA;
    }

    public double get_catO()    {
        return _catO;
    }
    
    public double get_Hypotenuse()    {
        return _hypotenuse;
    }
}
