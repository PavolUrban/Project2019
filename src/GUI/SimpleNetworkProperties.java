/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GUI;

import NetworkAnalysis.*;
import NetworkComponents.Edge;
import NetworkComponents.Vertex;
import edu.uci.ics.jung.graph.Graph;
import java.text.DecimalFormat;
import javafx.application.Platform;
import javafx.scene.control.Alert;

/**
 *
 * @author pavol
 */
public class SimpleNetworkProperties {
     private double diameterValuee;
    Alert myAlert;
     public SimpleNetworkProperties() {
    }
     
     public void getNetworkDensity(Graph<Vertex, Edge> network)
     {
         Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Network density");
        alert.setHeaderText("Network density value is: ");
        
         NetworkDensity n = new NetworkDensity(network);
         n.count();
        
         
           DecimalFormat df = new DecimalFormat("#.######");
        String valueDensity = df.format(n.getScores());
        alert.setContentText(valueDensity);

        alert.showAndWait();
     }
     
    
     
     public void getAverageDegree(Graph<Vertex, Edge> network)
     {
          Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Average degree");
        alert.setHeaderText("Average degree value is: ");
        
         AverageDegree ad= new AverageDegree(network);
         ad.count();
         String valueAverage = String.valueOf(ad.getAverageDegree());
        alert.setContentText(valueAverage);

        alert.showAndWait();
     }
     
     
      public void getDiameter(Graph<Vertex, Edge> network)
     {
          myAlert = new Alert(Alert.AlertType.INFORMATION);
        myAlert.setTitle("Diameter");
        myAlert.setHeaderText("Diameter value is counting right now, wait please.");
         startTask(network, "Diameter");
        

            myAlert.showAndWait();
     }
     
      
      public void startTask(Graph<Vertex, Edge> network, String property) {
        // Create a Runnable
        Runnable task = new Runnable() {
            public void run() {

                runTask(network, property);

            }
        };

        // Run the task in a background thread
        Thread backgroundThread = new Thread(task);
        // Terminate the running thread if the application exits
        backgroundThread.setDaemon(true);
        // Start the thread
        backgroundThread.start();

    }

    public void runTask(Graph<Vertex, Edge> network, String property) {

        try {
            if(property.equals("Diameter"))
            {
              
         DiameterClass dc = new DiameterClass(network);
         dc.count();
         diameterValuee = dc.getDiameter();
         System.out.println("Finalne vlakno "+diameterValuee);
            }

            // Update the Label on the JavaFx Application Thread
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
 String valueDiameter = String.valueOf(diameterValuee);
 myAlert.setHeaderText("Diameter value is: ");
        myAlert.setContentText(valueDiameter);
                }
            });

            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
}
