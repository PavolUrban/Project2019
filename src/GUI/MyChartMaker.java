package GUI;

import NetworkAnalysis.Closennes;
import NetworkAnalysis.ClusteringCoefficients;
import NetworkComponents.Edge;
import NetworkComponents.Vertex;
import edu.uci.ics.jung.graph.Graph;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Pair;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author pavol
 */
public class MyChartMaker {
     ArrayList<Pair<Vertex, Double>> dataToPrint;
     int[] groups = new int[10];
     double[] borders = new double[10];
     
  
    Label labelGlobalCC = new Label();
    public MyChartMaker()
    {
        
    }
    
    public void createChart(Graph<Vertex, Edge> network)
    {
         Group root = new Group();
        Scene scene = new Scene(root, Design.sceneWidth, Design.sceneHeight);

        // ScrollPane scrollPane = new ScrollPane();
       

        ClusteringCoefficients c = new ClusteringCoefficients(network);
        labelGlobalCC.setText("Priemerný zhlukovací koeficient: "+c.count());;
        dataToPrint =c.getScores();
        Stage chartStage = new Stage();
        chartStage.setTitle("Zhlukovací koeficient");
        CategoryAxis xAxis = new CategoryAxis();
        final NumberAxis yAxis = new NumberAxis();
        BarChart<String, Number> bc = new BarChart<String, Number>(xAxis, yAxis);
        bc.setTitle("Zhlukovací koeficient");

        bc.setMinWidth(Design.sceneWidth);
        bc.setMinHeight(Design.sceneHeight / 2);
        bc.setAnimated(false);

        xAxis.setLabel("Hodnota koeficientu");
        yAxis.setLabel("Počet uzlov");

        
      
        final VBox vbox = new VBox();
        vbox.setSpacing(5);
        vbox.setPadding(new Insets(30, 0, 0, 0)); //TODO dynamicky

        final VBox vbox1 = new VBox();
        final VBox leftCornerBox = new VBox();

        
        labelGlobalCC.setMinWidth(Region.USE_PREF_SIZE);
        labelGlobalCC.setMaxWidth(Region.USE_PREF_SIZE);
      
       
        vbox1.setPadding(new Insets(50, 0, 0, 560));
        leftCornerBox.setPadding(new Insets(120, 0, 0, 210));
   
       vbox1.getChildren().addAll(labelGlobalCC,leftCornerBox);

        vbox1.setMaxWidth(500);
 
        vbox1.setMinWidth(500);
      
        vbox1.setSpacing(5);
        //vbox1.setAlignment(Pos.CENTER);

        vbox.getChildren().addAll(bc, vbox1);
        resetGroupsCounters(0.1);
        orderData();
        
        
        
        XYChart.Series series1 = new XYChart.Series();

 
            double lowBorder=0.0;
            /* XYChart.Series series2 = new XYChart.Series();
             series2.setName("Group 2");*/
            double upBorder = lowBorder + 0.1;
            for (int i = 0; i < 10; i++) {
                NumberFormat formatter = new DecimalFormat("#0.00");
                
                
                //series1.setName("Group " + lowBorder + "-" + borders[i]); //opravit ..ked nevykreslujem vsetko tak je to napicu
                series1.getData().add(new XYChart.Data(formatter.format(lowBorder) + "-" + formatter.format(upBorder), groups[i]));
                // series2.getData().add(new XYChart.Data(lowBorder + "-" + (upBorder - 1), groups[i] + 3));
                lowBorder += 0.1;
                upBorder = lowBorder+0.1;
            }
        
         bc.getData().addAll(series1);
        
        

        root.getChildren().addAll(vbox);
        chartStage.setScene(scene);
        chartStage.show();
    }
    
     private void resetGroupsCounters(double range) {
        int times = 1;
        for (int i = 0; i < 10; i++) {
            groups[i] = 0;
            borders[i]=range * times;
            times++;
        }
        
        
    }
     
     public void orderData()
     {
         for (Pair<Vertex, Double> m : dataToPrint) {

                for (int i = 0; i < groups.length; i++) {
                   
                    if (m.getValue() < borders[i]) {
                       
                        groups[i]++;
                        break;
                    }
                }
                //System.out.println("kluc "+ m.getKey()+" hodnota"+ m.getValue());

            }
                
         
     }
     
     
     public void testClosenes(Graph<Vertex, Edge>network)
     {
         startTask(network);
     }
     
     public void startTask(Graph<Vertex, Edge>network) {
        // Create a Runnable
        Runnable task = new Runnable() {
            public void run() {

                runTask(network);

            }
        };

        // Run the task in a background thread
        Thread backgroundThread = new Thread(task);
        // Terminate the running thread if the application exits
        backgroundThread.setDaemon(true);
        // Start the thread
        backgroundThread.start();

    }

    public void runTask(Graph<Vertex, Edge>network) {

        try {
            System.out.println("Start of task");
             ArrayList<Pair<Vertex, Double>> data;
            Closennes c = new Closennes(network);
            c.count();
            data = c.getScores();
            System.out.println("End of task");
            // Update the Label on the JavaFx Application Thread
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    
                    MyAlerts m = new MyAlerts();
                    m.closenesTestDialog();
                }
            });

            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
    
    
    public void degreeDistributionChart(ArrayList<Pair<Integer, Double>> results2)
    {
        Group root = new Group();
        Scene scene = new Scene(root, Design.sceneWidth, Design.sceneHeight);

        // ScrollPane scrollPane = new ScrollPane();
       

        Stage chartStage = new Stage();
        chartStage.setTitle("Degree distribution");
        CategoryAxis xAxis = new CategoryAxis();
        final NumberAxis yAxis = new NumberAxis();
        BarChart<String, Number> bc = new BarChart<String, Number>(xAxis, yAxis);
        bc.setTitle("Degree distribution");
bc.setStyle("-fx-font-size: " + 30 + "px;");
        bc.setMinWidth(Design.sceneWidth);
        bc.setMinHeight(600); //Warning line was bc.setMinHeight(Design.sceneHeight / 2)
        bc.setAnimated(false);

        xAxis.setLabel("Degree");
        yAxis.setLabel("Value");

        
      
        final VBox vbox = new VBox();
        vbox.setSpacing(5);
        vbox.setPadding(new Insets(30, 0, 0, 0)); //TODO dynamicky

        final VBox vbox1 = new VBox();
        final VBox leftCornerBox = new VBox();

        
        
        
        vbox1.setPadding(new Insets(50, 0, 0, 560));
        leftCornerBox.setPadding(new Insets(120, 0, 0, 810));
        leftCornerBox.setSpacing(5);
       // vbox1.getChildren().addAll(labellowestCentrality, labelbiggestCentrality, labelchooseRange, slider, recount, leftCornerBox);
        vbox1.getChildren().add(leftCornerBox);
        vbox1.setMaxWidth(500);
 
        vbox1.setMinWidth(500);
      
        vbox1.setSpacing(5);
        //vbox1.setAlignment(Pos.CENTER);

        vbox.getChildren().addAll(bc, vbox1);
      
        
        
        
        XYChart.Series series1 = new XYChart.Series();

            series1.setName("Group 1");
      
            for (int i = 0; i < results2.size(); i++) {

               
                series1.getData().add(new XYChart.Data(results2.get(i).getKey().toString(), results2.get(i).getValue()));
                
            }
        
         bc.getData().addAll(series1);
        
        

        root.getChildren().addAll(vbox);
        chartStage.setScene(scene);
        chartStage.show();
    }
}
