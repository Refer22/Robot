package robot.robot;

/**
 * @author 0.2
 *
 */
public class Angle {
	private Point first;
	private Point second;

	/**
	 * @param firstPoint
	 * @param secondPoint
	 */
	public Angle(Point firstPoint, Point secondPoint) {
		first = firstPoint;
		second = secondPoint;
	}

	/**
	 * @return
	 */
	public double getAngleRa() {
		return angleCalculation();
	}

	/**
	 * @return
	 */
	public double getAngleDeg() {
		return Math.toDegrees(angleCalculation());
	}

	/**
	 * @return
	 */
	private double angleCalculation() {
		Point base = new Point(1, 0);
		Point p1 = new Point(second.getX() - first.getX(), second.getY() - first.getY());
		Point p2 = new Point(base.getX() - first.getX(), base.getY() - first.getY());
		double ang_final = getAtanAngle(p1) - getAtanAngle(p2);

		if (ang_final < 0.0) {
			return ang_final + (2.0 * Math.PI);
		} else {
			return ang_final;
		}
	}

	/**
	 * @param evaluateAngle
	 * @return
	 */
	private static double getAtanAngle(Point evaluateAngle) {
		return Math.atan2(evaluateAngle.getX(), evaluateAngle.getY());
	}
}
