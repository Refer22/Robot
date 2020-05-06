package Robot;


import java.util.ArrayList;

public class Robot {
    public static double distance = 0;
    static ArrayList<Point> init = new ArrayList<Point>();
    static Point end;
    static Thread thread;
    
    public static ArrayList<Measure> measures = new ArrayList<Measure>();
     
    public static void main(String args[]) throws Exception {
		for (String s: args) {
		     for (String split: s.split(",“"))  {
		         if (split.indexOf("location") != -1)    {
		        	 addPoint(split);
		         }
		     }
		}
		if (init.size() == 0) {
		    throw new Exception("Are needed at least is needed one point to guide");
		}   else    {
			thread = new Thread(new RobotThread(init.get(0)));
			thread.setDaemon(true);
			thread.start();
			thread.join();
		}
    } 

    private static void addPoint(String info)	{
		Point aux = new Point(
		        Double.parseDouble(info.substring(info.indexOf("lat") + 6, info.indexOf(","))),
		        Double.parseDouble(info.substring(info.indexOf("lng") + 6, info.indexOf("}")))
		    );
		init.add(aux);
    }

    public static void report() {
    	String report = "";
    	
    	if (measures.size() == 0)   {
    		report = "No measure realiced... the robot has meters traveled";
    	}	else	{
    		for (Measure element: measures)   {
    			report += "Minute: " + String.valueOf(element.minute) + "   "; 
    			report += "Location: lat:" + element.location.getX() + " lng: " + element.location.getY();
    			report += "Measure (~" + Math.round(element.pM25) + ") ";
    			report += ": " + element.getQualityRange() + "\n";
    		}            
    	}
        System.out.println(report);
        measures.clear();
    }
    
    /*
    public static void report(Measure status) {
    	String report = "";
    	
    	report += "{\"timestamp\": " +System.currentTimeMillis() + ",";
    	report += "\"location\": {";
    	report += "\"lat\": " + status.location.getX() + "\"lng\": " + status.location.getY() + "}";
    	report += "\"level\": " + status.getQualityRange() + ",";
    	report += "\"source\": station_name";
    
    	 System.out.println(report);
    }
    */
}
