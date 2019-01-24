/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GUI;

import NetworkComponents.Edge;
import NetworkComponents.Vertex;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.util.Pair;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

/**
 *
 * @author pavol
 */
public class NetworkDrawer {
    
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

    public static void drawVertex(GraphicsContext gc, Vertex v, Color color) {
        drawVertex(gc, v.getPositionX(), v.getPositionY(), v.getSize(), color);
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
            drawEdge(gc, v.getPositionX(), v.getPositionY(), v2.getPositionX(), v2.getPositionY(), 0.1, Color.BLACK);
        }             
                   
        for (Vertex v : network.getVertices()) 
        {
            drawVertex(gc, v, Color.CORAL); //skontrolovat ci je spravne
        }
    }
    
}
