package robot.robot;

import robot.start.StartTask;
import robot.start.StartTask.TSync;

/**
 * @author 0.2
 *
 */
public class Robot implements Runnable {
	private final double R_EARTH = 6371;

	private double lastDistanceReport = 0;
	public double distance = 0;

	private Place _plzBPalace = null;
	private Place _plzTEstation = null;

	private int minute = 0;
	private Point _init;
	private Point _offset;
	private TSync _pc2;

	public Robot(Point init, TSync pc) {
		_pc2 = pc;
		init(init);
	}

	/**
	 * @return current position of the robot
	 */
	private Point getOffset() {
		return _offset;
	}

	/**
	 * 1st position and assign stations
	 * 
	 * @param init -- 1st position
	 */
	public void init(Point init) {
		_init = init;
		_offset = _init;// 1st point offset is 0
		_plzBPalace = new Place("Buckingham Palace", new Point(51.50129, -0.14193));
		_plzTEstation = new Place("Temple Station", new Point(51.51085, -0.11416));
	}

	/**
	 * Main logic
	 */
	public void run() {
		while (true) {
			try {
				Thread.sleep(1000);
				minute++;
				distance += updateOffset();
				isNearToPlace();
				isAppropiateTakeMeasure();
				isTimeReport();
			} catch (InterruptedException ex) {
				System.err.println("Error! Shutdown...");
				System.exit(0);
			}
			isStop();
		}
	}

	/**
	 * Check if stop of the robot has been requested. The robot will wait until next
	 * order
	 */
	private void isStop() {
		try {
			while (StartTask.stop) {
				System.out.println("Robot Waiting");
				_pc2.pause();
				System.out.println("Robot Restarted");
			}

		} catch (InterruptedException e) {
			System.err.println("Error! Robot Thread sundely stopped" + e.getMessage());
		}

	}

	/**
	 * Check if the robot is near to certain places
	 */
	private void isNearToPlace() {
		if (currentDistanceToKnownPoint(_plzBPalace.getLocation())) {
			StartTask.report(new Measure(getOffset(), Math.random() * 200, _plzBPalace.getName()));
		} else if (currentDistanceToKnownPoint(_plzTEstation.getLocation())) {
			StartTask.report(new Measure(getOffset(), Math.random() * 200, _plzTEstation.getName()));
		}
	}

	/**
	 * Check if the robot has advanced the defined distance between measures
	 */
	private void isAppropiateTakeMeasure() {
		if (distance - lastDistanceReport > StartTask.distanceBetweenMeasures) {
			lastDistanceReport = distance;
			StartTask.measures.add(new Measure(getOffset(), Math.random() * 200, minute));
		}
	}

	/**
	 * Check if is time for new report
	 */
	private void isTimeReport() {
		if (minute % 15 == 0) {
			StartTask.reportLog();
		}
	}

	/**
	 * @param newInit -- reset current status of robot parameter to the new origin
	 */
	public void reRoute(Point newInit) {
		minute = 0;
		distance = 0;
		init(newInit);
		System.out.println("Robot New route selected");
	}

	/**
	 * number of km per degree = ~111km (111.32 in google maps, but range varies
	 * between 110.567km at the equator and 111.699km at the poles) 1km in degree =
	 * 1 / 111.32km = 0.0089 1m in degree = 0.0089 / 1000 = 0.0000089
	 * 
	 * @return distance traveled by the robot this last minute
	 */
	private double updateOffset() {
		Triangle triangle = new Coordinates(getOffset()).calculateNextCord();
		double offsetX = triangle.get_catA() * 0.0000089;
		double offsetY = triangle.get_catO() * 0.0000089;
		double new_lat = getOffset().getX() + offsetX;
		double new_long = getOffset().getY() + offsetY / Math.cos(getOffset().getX() * 0.018);
		_offset = new Point(new_lat, new_long);

		return triangle.get_Hypotenuse();
	}

	/**
	 * Check if position is the same of measure the distance
	 * 
	 * @param station
	 * @return
	 */
	public boolean currentDistanceToKnownPoint(Point station) {
		Point aux = new Point();

		if (station.getX() == getOffset().getX() && station.getY() == getOffset().getY()) {
			return true;
		}
		aux.setX(Math.toRadians(getOffset().getX() - station.getX()));
		aux.setY(Math.toRadians(getOffset().getY() - station.getY()));

		if (getSegment(aux, Math.cos(Math.toRadians(station.getX()))) < 100) {
			return true;
		}
		return false;
	}

	/**
	 * @param point      -- point to be checked agais the robot position
	 * @param cosXTarget -- cos angle in radians of target to be compared
	 * @return
	 */
	private double getSegment(Point point, double cosXTarget) {
		double a = Math.sin(point.getX() / 2) * Math.sin(point.getX() / 2)
				+ Math.cos(Math.toRadians(getOffset().getX())) * cosXTarget * Math.sin(point.getY() / 2)
						* Math.sin(point.getY() / 2);
		double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
		double distance = (R_EARTH * c) * 1000;
		distance = Math.pow(distance, 2) + 1;

		return Math.sqrt(distance);
	}

}
