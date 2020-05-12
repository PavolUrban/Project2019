/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DataPreparation;

import GUI.AlertsWindows;
import NetworkComponents.Edge;
import NetworkComponents.Vertex;
import UserSettings.ToMove;
import UserSettings.UserSettings;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.util.Pair;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

/**
 *
 * @author pavol
 */
public class Exports 
{
    
     static List<Color> colors = new ArrayList(Arrays.asList( Color.AQUA,Color.CORAL, Color.SPRINGGREEN,Color.YELLOW , Color.BURLYWOOD,
                                                     Color.DARKSEAGREEN, Color.SILVER,Color.CHARTREUSE,Color.BLUE , Color.BLUEVIOLET,
                                                     Color.CADETBLUE, Color.FUCHSIA, Color.DARKORANGE, Color.KHAKI,Color.PINK,
                                                     Color.ROSYBROWN,Color.BROWN,  Color.BLACK, Color.AZURE, Color.BLANCHEDALMOND));
    
    
     public static void colorizePointsProperly(Graph<Vertex, Edge> network)
    {
        System.out.println("Colorize called!");
        for (Vertex v : network.getVertices()) 
        {
            if(UserSettings.colorizeByAttribute.equalsIgnoreCase("Žiadne"))
            {
                v.setColor(Color.CORAL);
            }
            
            else //point will be colorized by one of the attributes
            {
                if(v.clusterId > colors.size() - 1) // - 1 because of indexing
                {
                    v.setColor(Color.CORAL); 
                }
                   
                else
                {
                    v.setColor(colors.get(v.clusterId));
                }
                
            }
            
        }
    }
    
    
    //GDF format
     public static void saveNetworkAsConnectedVerticesList(Stage stage, Graph<Vertex, Edge> network) 
     {
         
        if(network == null)
        {
            AlertsWindows.displayAlert("Žiadna sieť na uloženie");
        }
        
        else
        {
            
            colorizePointsProperly(network);
            BufferedWriter bw = null;
            FileWriter fw = null;
            FileChooser fileChooser = new FileChooser();

            //Set extension filter
            FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("CSV", ".csv");;
            fileChooser.getExtensionFilters().add(extFilter);

            //Show save file dialog
            File file = fileChooser.showSaveDialog(stage);

            if (file != null) {
                try {
                    fw = new FileWriter(file, true);                  
    
                    fw.write("nodedef>name VARCHAR,color VARCHAR");
                    fw.write("\n");
                    
                    for(Vertex v : network.getVertices())
                    {   
                        Color color;
                        
                        if(v.getColor() != null)
                        {
                            color = v.getColor();
                        }
                        
                        else
                        {
                            color = Color.BLACK;
                        }
                        
                        
                        String r = ""+(int)( color.getRed() * 255 );
                        String g = ""+(int)( color.getGreen() * 255 );
                        String b = ""+(int)( color.getBlue() * 255 );
                        fw.write(v.getId()+ "," + "'" + r + "," + g + "," + b + "'");
                        fw.write("\n");
                    }
                    
                    fw.write("edgedef>node1 VARCHAR,node2 VARCHAR");
                    fw.write("\n");
                    
                    for(Edge e: network.getEdges())
                    {
                        Pair<Vertex> p = network.getEndpoints(e);
                        Vertex v = p.getFirst();
                        Vertex v2 = p.getSecond();
                        String firstVertex = String.valueOf(v.getId());
                        String secondVertex = String.valueOf(v2.getId());

                        fw.write(firstVertex+","+secondVertex);
                        fw.write("\n");
                    }
  
                } catch (IOException ex) {
                    //Logger.getLogger(JavaFX_DrawOnCanvas.class.getName()).log(Level.SEVERE, null, ex);
                } finally {
                    try {
                        if (bw != null) {
                            bw.close();
                        }

                        if (fw != null) {
                            fw.close();
                        }
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        }   
    }
     
     
     public static void saveAsAdjacencyList(Stage stage, Graph<Vertex, Edge> network)
     {
        if(network == null)
        {
            AlertsWindows.displayAlert("Žiadna sieť na uloženie");
        }
        
        else
        {
            BufferedWriter bw = null;
            FileWriter fw = null;
            FileChooser fileChooser = new FileChooser();

            //Set extension filter
            FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("CSV", ".csv");;
            fileChooser.getExtensionFilters().add(extFilter);

            //Show save file dialog
            File file = fileChooser.showSaveDialog(stage);

            if (file != null) {
                try {
                    fw = new FileWriter(file, true);                  
               
                    for(Edge e: network.getEdges())
                    {
                        Pair<Vertex> p = network.getEndpoints(e);
                        Vertex v = p.getFirst();
                        Vertex v2 = p.getSecond();
                        String firstVertex = String.valueOf(v.getId());
                        String secondVertex = String.valueOf(v2.getId());

                        fw.write(firstVertex+","+secondVertex);
                        fw.write("\n");
                    }
  
                } catch (IOException ex) {
                    //Logger.getLogger(JavaFX_DrawOnCanvas.class.getName()).log(Level.SEVERE, null, ex);
                } finally {
                    try {
                        if (bw != null) {
                            bw.close();
                        }

                        if (fw != null) {
                            fw.close();
                        }
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        }
     }
     
     
     public static void saveNetworkAsConnectedVerticesList(Stage stage) 
    {
        if(ToMove.headers.size() == 0)
        {
            AlertsWindows.displayAlert("Žiadna dáta na uloženie. Najskôr, prosím, vytvorte sieť.");
        }
        else
        {
            BufferedWriter bw = null;
            FileWriter fw = null;
            FileChooser fileChooser = new FileChooser();

            //Set extension filter
            FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("CSV", ".csv");;
            fileChooser.getExtensionFilters().add(extFilter);

            //Show save file dialog
            File file = fileChooser.showSaveDialog(stage);

            if (file != null) {
                try {
                    fw = new FileWriter(file, true); 
                    
                    List<Headers> headers = ToMove.headers;
                    
                    int numberOfAddedHeaders = 0;
                    for(Headers h : headers)
                    {
                        if(numberOfAddedHeaders == headers.size() - 1)
                            fw.write(h.getHeaderName()+"\n");
                        else
                            fw.write(h.getHeaderName()+",");
                        
                       numberOfAddedHeaders++;
                    }
                    
                   
                    List<ChosenRecords> cr = ToMove.chosenRecords;
                   
                    for(ChosenRecords c : cr)
                    {
                        fw.write(c.attributesValues+"\n");    
                    }
                    
  
                } catch (IOException ex) {
                    //Logger.getLogger(JavaFX_DrawOnCanvas.class.getName()).log(Level.SEVERE, null, ex);
                } finally {
                    try {
                        if (bw != null) {
                            bw.close();
                        }

                        if (fw != null) {
                            fw.close();
                        }
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        }
            
    }
     
}
