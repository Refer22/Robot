package robot.robot;

public class Triangle {
    private double _catA = 0;
    private double _catO = 0;
    private double _hypotenuse = 0;
    
    public Triangle(double catA, double catO, double hip)    {
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
