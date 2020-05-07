package robot.start;

import java.util.ArrayList;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.glassfish.jersey.servlet.ServletContainer;

import robot.api.EntryPoint;
import robot.robot.Measure;
import robot.robot.Point;
import robot.robot.Robot;



public class StartTask 
{
	public static final TSync sync = new TSync(); 
	public static boolean stop = false;
	
	private static final int PORT_NR = 7070;
	private static final String ENDPOINT_1 = "/*";
	
	static Point init = null;
	
	public static Robot robot = new Robot(init, sync);
	public static String lastReport = ""; 
	public static int distanceBetweenMeasures = 100; 
	
	public static ArrayList<Measure> measures = new ArrayList<Measure>();
	
    public static void main( String[] args ) throws Exception
    {
    	init = getPointFromInput(args);
		if (init == null) {
		    throw new Exception("Are needed at least is needed one point to guide");
		}   else    {
			robot = new Robot(init, sync);
			startApp();
		}
    }
    
    public static Point getPointFromInput(String[] value)	{
    	for (String s: value) {
		     for (String split: s.split(",“"))  {
		         if (split.indexOf("location") != -1)    {
		        	 return addPoint(split);
		         }
		     }
		}
    	return null;
    }
    
    private static Point addPoint(String info)	{
    	try	{
    		String x = info.substring(info.indexOf("lat") + 5, info.indexOf(",", info.indexOf("lat")));
        	String y = info.substring(info.indexOf("lng") + 5, info.indexOf("}", info.indexOf("lng")));
        	
    		return new Point(Double.parseDouble(x),Double.parseDouble(y));
    	} catch (Exception e) {
			System.err.println("Error in value transformation" + e.getMessage());
		}	
    	return null;
		 
    }
    
    public static void startApp() throws Exception	{
		ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
		context.setContextPath("/");
		Server jettyServer = new Server(PORT_NR);
		jettyServer.setHandler(context);
		ServletHolder jerseyServlet = context.addServlet(ServletContainer.class, ENDPOINT_1);
		jerseyServlet.setInitOrder(0);
		jerseyServlet.setInitParameter(
				"jersey.config.server.provider.classnames", getAvailableApli());
		
		Thread robotThread = new Thread(robot);
		
		try {
			jettyServer.start();
			robotThread.start();
			jettyServer.join();
			robotThread.join();
		} finally {
			jettyServer.destroy();
		}
    }
	
    private static String getAvailableApli()	{
    	return EntryPoint.class.getCanonicalName();
    }

    public static void reportLog() {
    	System.out.println(generateReport());
	}
    
    public static String generateReport()	{
    	String report = "";
    	
    	if (measures.size() == 0)   {
    		report = "No measure realiced... the robot has meters traveled";
    	}	else	{
    		for (Measure element: measures)   {
    			report += report(element);
    		}    
    	}
    	measures.clear();
    	return report;
    }
    
    public static String report(Measure status) {
    	String report = "";
    	
    	report += "{\"timestamp\": " +System.currentTimeMillis() + ", ";
    	report += "\"location\": {";
    	report += "\"lat\": " + status.location.getX() + ", \"lng\": " + status.location.getY() + "}, ";
    	report += "\"level\": " + status.getQualityRange() + ", ";
    	report += "\"source\": station_name}";
    
    	 System.out.println(report);
    	 lastReport += "\n" + report + "\n";
    	 return report;
    }
    
    public static class TSync 
    { 
    	public void pause()throws InterruptedException	{
    		synchronized(this)	{ 
    			wait();
    		} 
    	} 

    	public void restart()throws InterruptedException	{ 
    		synchronized(this)	{ 
    			StartTask.stop = false;
    			notify(); 
    		} 
    	} 
    }

    public static void callRestart()	{
    	try {
    		sync.restart();
    	} catch (InterruptedException e) {
    		e.printStackTrace();
    	} 
    }
}
