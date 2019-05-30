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
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.util.Pair;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

/**
 *
 * @author pavol
 */
public class Exports 
{
    
     public static void saveNetworkAsConnectedVerticesList(Stage stage, Graph<Vertex, Edge> network) 
     {
         
        if(network == null)
        {
            AlertsWindows.displayAlert("No network to save.");
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

                        fw.write(firstVertex+";"+secondVertex);
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
         System.out.println("Saving children and their attributes ");
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
                    for(Headers h : headers)
                    {
                       fw.write(h.getHeaderName()+",");
                    }
                    fw.write("\n");
                    
                    
                    
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
