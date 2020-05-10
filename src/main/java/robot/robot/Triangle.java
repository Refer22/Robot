package robot.robot;

/**
 * @author 0.2
 *
 */
public class Triangle {
	private double _catA = 0;
	private double _catO = 0;
	private double _hypotenuse = 0;

	/**
	 * @param catA
	 * @param catO
	 * @param hip
	 */
	public Triangle(double catA, double catO, double hip) {
		_catA = catA;
		_catO = catO;
		_hypotenuse = hip;
	}

	/**
	 * @return
	 */
	public double get_catA() {
		return _catA;
	}

	/**
	 * @return
	 */
	public double get_catO() {
		return _catO;
	}

	/**
	 * @return
	 */
	public double get_Hypotenuse() {
		return _hypotenuse;
	}
}
