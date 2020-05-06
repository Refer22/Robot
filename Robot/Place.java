package Robot;

public class Place {
	public Point location = null;
	public String name = "";
			
	public Place(String placeName, Point placeLocation)	{
		name = placeName;
		location = placeLocation; 
	}
}
