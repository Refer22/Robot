package robot.robot;

/**
 * @author 0.2
 *
 */
public class Place {
	private Point location = null;
	private String name = "";

	/**
	 * @param placeName
	 * @param placeLocation
	 */
	public Place(String placeName, Point placeLocation) {
		name = placeName;
		location = placeLocation;
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
	public String getName() {
		return name;
	}

}
