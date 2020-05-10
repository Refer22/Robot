package robot.robot;

/**
 * @author 0.2
 *
 */
public class Measure {
	public Point location = null;
	public double pM25 = 0.0;
	public long time = 0;
	public String station_name = "";

	/**
	 * 
	 * Default constructor to take a measure
	 * 
	 * @param location
	 * @param pM25
	 * @param minute
	 */
	public Measure(Point location, double pM25, int minute) {
		this.location = location;
		this.pM25 = pM25;
		this.time = System.currentTimeMillis();
		this.station_name = "robot";

	}

	/**
	 * Contructor to take measure of a centain known Place
	 * 
	 * @param locationA
	 * @param pM25A
	 * @param station_name
	 */
	public Measure(Point locationA, double pM25A, String station_name) {
		this.location = locationA;
		this.pM25 = pM25A;
		this.time = System.currentTimeMillis();
		this.station_name = station_name;

	}

	/**
	 * @return Air quality of certain position
	 */
	public String getQualityRange() {
		if (Math.round(pM25) < 50) {
			return "Good";
		} else if (Math.round(pM25) < 100) {
			return "Moderate";
		} else if (Math.round(pM25) < 150) {
			return "USG (Unhealthy to Sensitive Groups)";
		} else {
			return "Unhealthy";
		}
	}

	/**
	 * @return
	 */
	public Point getLocation() {
		return location;
	}

	/**
	 * @return
	 */
	public double getpM25() {
		return pM25;
	}

	/**
	 * @return
	 */
	public long getTime() {
		return time;
	}

	/**
	 * @return
	 */
	public String getStation_name() {
		return station_name;
	}
}
