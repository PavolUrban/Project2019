/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GUI;

import NetworkComponents.Edge;
import NetworkComponents.Vertex;
import UserSettings.UserSettings;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.util.Pair;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

/**
 *
 * @author pavol
 */
public class NetworkDrawer {
    
    static List<Color> colors = new ArrayList(Arrays.asList(Color.CORAL, Color.CHARTREUSE, Color.BROWN, Color.BLUE, Color.BURLYWOOD,
                                                     Color.DARKSEAGREEN, Color.SILVER, Color.AQUA, Color.YELLOW, Color.BLUEVIOLET,
                                                     Color.CADETBLUE, Color.FUCHSIA, Color.DARKORANGE, Color.KHAKI,Color.PINK,
                                                     Color.ROSYBROWN, Color.SPRINGGREEN, Color.BLACK));
    
    
     private static void drawEdge(GraphicsContext gc, double fromX, double fromY, double toX, double toY, double edgeWidth, Color color) {
        gc.setLineWidth(edgeWidth);
        gc.setStroke(color);
        gc.strokeLine(fromX, fromY, toX, toY);
    }

    private static void drawVertex(GraphicsContext gc, double positionX, double positionY, double size, Color color) {
        double leftX = positionX - size / 2;
        double leftY = positionY - size / 2;
        double sizeLine = size * 0.05;
        gc.setFill(color);
        gc.fillOval(leftX, leftY, size, size);
        gc.setStroke(Color.BLACK);
        gc.setLineWidth(sizeLine);
        gc.strokeOval(leftX, leftY, size, size);
    }

    public static void drawVertex(GraphicsContext gc, Vertex v, Color color, Boolean useExistingLayout) {
        if(!useExistingLayout)
            drawVertex(gc, v.getPositionX(), v.getPositionY(), v.getSize(), color);
        else
        {
            Collection<Vertex> oldVertices = UserSettings.savedNetwork.getVertices();
            for(Vertex old : oldVertices)
            {
                if(v.getId()== old.getId())
                {
                    drawVertex(gc, old.getPositionX(), old.getPositionY(), v.getSize(), color);
                    break;
                }
            }
        }
    }
    
    public static void drawNetwork(Graph<Vertex, Edge> network, Canvas canvas1)
    {
        GraphicsContext gc = canvas1.getGraphicsContext2D();
        gc.setFill(Color.WHITE);
        gc.setStroke(Color.BLACK);
        gc.fillRect(0, 0, Design.canvasWidth, Design.canvasHeight);
        
        gc.strokeLine(0, 0, Design.canvasWidth, 0); // UP -> in general start x,y, end x, y
        gc.strokeLine(0, 0, 0, Design.canvasHeight); // LEFT
        gc.strokeLine(Design.canvasWidth,Design.canvasHeight, 0,Design.canvasHeight); // DOWN
        gc.strokeLine(Design.canvasWidth, 0, Design.canvasWidth, Design.canvasHeight); 
        gc.stroke();

        for (Edge e : network.getEdges()) 
        {
            Pair<Vertex> p = network.getEndpoints(e);
            Vertex v = p.getFirst();
            Vertex v2 = p.getSecond();
//            System.out.println(UserSettings.maxSliderValue + " vs "+ e.getWeight()/UserSettings.maxSliderValue);
            double normalizedEdgeWeight = 1 - ( e.getWeight()/UserSettings.maxSliderValue) + 0.2; // + 0,2 to visible maximally weighted edge which would have value 0
           
            drawEdge(gc, v.getPositionX(), v.getPositionY(), v2.getPositionX(), v2.getPositionY(), Math.pow(normalizedEdgeWeight, 3), Color.BLACK);
        }             
         
        
        for (Vertex v : network.getVertices()) 
        {
            if(v.clusterId > colors.size() - 1) // - 1 because of indexing
                drawVertex(gc, v, Color.CORAL, false); //skontrolovat ci je spravne
            
            else
                drawVertex(gc, v, colors.get(v.clusterId), false);
        }
    }
    
    public static void redrawNetwork(Graph<Vertex, Edge> network, Canvas canvas1)
    {
        System.out.println("Teraz sa vola redraw");
        GraphicsContext gc = canvas1.getGraphicsContext2D();
        gc.setFill(Color.WHITE);
        gc.setStroke(Color.BLACK);
        gc.fillRect(0, 0, Design.canvasWidth, Design.canvasHeight);
        
        gc.strokeLine(0, 0, Design.canvasWidth, 0); // UP -> in general start x,y, end x, y
        gc.strokeLine(0, 0, 0, Design.canvasHeight); // LEFT
        gc.strokeLine(Design.canvasWidth,Design.canvasHeight, 0,Design.canvasHeight); // DOWN
        gc.strokeLine(Design.canvasWidth, 0, Design.canvasWidth, Design.canvasHeight); 
        gc.stroke();

        Collection<Vertex> oldVertices = UserSettings.savedNetwork.getVertices();
        
        System.out.println("V starej sieti bolo tolkoto hran "+UserSettings.savedNetwork.getEdgeCount()+" vs v novej "+network.getEdgeCount());
        for (Edge e : network.getEdges()) 
        {
            Pair<Vertex> p = network.getEndpoints(e);
            Vertex v = p.getFirst();
            Vertex v2 = p.getSecond();
            
            //TODO - prechadzat rozumnejsie
            for(Vertex old : oldVertices)
            {
                if(old.getId() == v.getId())
                {
                   // System.out.println("nasiel som uzol "+v.getId()+ "v starej sieti menim poziciu "+v.getPositionX()+" na poziciu "+old.getPositionX());
                    v.setPositionX(old.getPositionX());
                    v.setPositionY(old.getPositionY());
                    break;
                }
            }
            
            for(Vertex old : oldVertices)
            {
                if(old.getId() == v2.getId())
                {
                    v2.setPositionX(old.getPositionX());
                    v2.setPositionY(old.getPositionY());
                    break;
                }
            }
            
            
            double normalizedEdgeWeight = 1 - ( e.getWeight()/UserSettings.maxSliderValue) + 0.2; // + 0,2 to visible maximally weighted edge which would have value 0
    
            drawEdge(gc, v.getPositionX(), v.getPositionY(), v2.getPositionX(), v2.getPositionY(), Math.pow(normalizedEdgeWeight, 3), Color.BLACK);
        }             
                   
        for (Vertex v : network.getVertices()) 
        {
             if(v.clusterId > colors.size() - 1) // - 1 because of indexing
                drawVertex(gc, v, Color.CORAL, true); //skontrolovat ci je spravne
            
            else
                drawVertex(gc, v, colors.get(v.clusterId), true); //skontrolovat ci je spravne
        }  
    }
    
}
