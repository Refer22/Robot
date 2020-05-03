/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Robot;

import java.io.Console;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Polyline;
import javafx.stage.Stage;
import javafx.concurrent.Task;
import javafx.scene.text.Text;
/**
 *
 * @author 0.2
 */
public class Robot extends Application {
    static double R_EARTH = 6378137;
    private int count = 0;
    private final Text text = new Text("");
    private int minute = 0;
    private double distance = 0;
    private double lastMesure = 0;
    
    ArrayList<Double> measures = new ArrayList();
    
    Point offset;
    static Point init;
    static Point end;
    Point dPoint;
     
    public static void main(String args[]) throws Exception{
        String one = "";
        String two = "";
        for (String s: args) {
             for (String split: s.split(",“"))  {
                 if (split.indexOf("location") != -1 && one == "")    {
                     one = split;
                 }  else if (split.indexOf("location") != -1 && one != "")   {
                     two = split;
                 }
             }
        }
        if (one == "" || two == "") {
            throw new Exception("Are needed at least two points to guide the robot.");
        }   else    {
            init = new Point(
                Double.parseDouble(one.substring(one.indexOf("lat") + 6, one.indexOf(","))),
                Double.parseDouble(one.substring(one.indexOf("lng") + 6, one.indexOf("}")))
        );
        end = new Point(33.38790, 2.16990);
        System.out.println(two.substring(two.indexOf("lat") + 6, two.indexOf(",")));
        System.out.println(two.substring(two.indexOf("lng") + 6, two.indexOf("}")));
        launch(args);  
        }
    } 

    private void calculation()  {
            offset.setX(init.getX() + dPoint.getX() * 180 / Math.PI);
            offset.setY(init.getY() + dPoint.getY() * 180 / Math.PI / Math.cos(init.getX() * Math.PI / 180));
//            System.out.println("Minute: " + minute + " Distance: " + distance(offset.getX(), end.getX(), offset.getY(), end.getY())); 
            init.updatePoint(offset);
    }
    
    private double preparation() {
        Coordinates myCord = new Coordinates(init, end);
        Triangle triangle = myCord.calculateNextCord();
        dPoint = new Point(
                triangle.get_catO() / R_EARTH, 
                triangle.get_catA() / (R_EARTH * Math.cos(Math.PI * init.getX() / 180))
        ); 
        offset = new Point(
                init.getX() + dPoint.getX() * 180 / Math.PI,
                init.getY() + dPoint.getY() * 180 / Math.PI
        );
//        System.out.println("Lat Lon: " + offset.getX() + "   " + offset.getY());
//        System.out.println("Distance: " + distance(init.getX(), end.getX(), init.getY(), end.getY())); 
        return triangle.get_Hypotenuse();
    }
    
  @Override 
   public void start(Stage stage) {    
        
        StackPane root = new StackPane();
        root.getChildren().add(text);
        Scene scene = new Scene(root, 600, 600);
        preparation();
        
        //Thread thread = robot();
        //thread.setDaemon(true);
        //thread.start();

        stage.setScene(scene);
        stage.show(); 
   }    
   
    private void report(Point newPoint) {
        String report = "";
        
        if (measures.size() == 0)   {
            report = "No measure realiced... the robot has " + distance + " meters traveled";
        }   else    {
            for (int i = 0; i < measures.size(); i++)   {
                report += String.valueOf(measures.get(i)) + "\n";
            }            
        }
        text.setText(report);
    }   
  
   public static double distance(double lat1, double lat2, double lon1,
        double lon2) {
        double latDistance = Math.toRadians(lat2 - lat1);
        double lonDistance = Math.toRadians(lon2 - lon1);
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double distance = R_EARTH * c * 1000; // convert to meters

        distance = Math.pow(distance, 2) + 1;

        return Math.sqrt(distance);
    }

    private Thread robot() {
        return new Thread(new Runnable() {

            @Override
            public void run() {
                Runnable updater = new Runnable() {
                    @Override
                    public void run() {
                        distance += preparation();
                        System.out.println("Distance: " + distance);
                        calculation();
                        if (distance - lastMesure > 100)    {
                            lastMesure = distance;
                            measures.add(Math.random() * 200);//PM 2.5;
                        }
                        if (minute % 15 == 0)   {
                            report(offset);
                        }
                    }
                };

                while (true) {
                    try {
                        Thread.sleep(60000);
                        minute++;
                    } catch (InterruptedException ex) {}
                    Platform.runLater(updater);
                }
            }
        });
    }

}
