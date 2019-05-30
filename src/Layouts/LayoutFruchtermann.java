/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Layouts;

import GUI.Design;
import NetworkComponents.Edge;
import NetworkComponents.Vertex;
import edu.uci.ics.jung.algorithms.layout.FRLayout;
import edu.uci.ics.jung.algorithms.layout.KKLayout;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.visualization.DefaultVisualizationModel;
import java.awt.Dimension;

/**
 *
 * @author pavol
 */
public class LayoutFruchtermann extends Layout{
 
    @Override
    public void runLayout(Graph<Vertex, Edge> network) {
       
        int requiredOperations =60;
        FRLayout layout = new FRLayout(network);
        layout.initialize();
        DefaultVisualizationModel<Integer,Integer> model = new DefaultVisualizationModel<>(layout, new Dimension(Design.canvasWidth,Design.canvasHeight));
        layout.reset();
        for(int doneOperations = 0; doneOperations < requiredOperations; doneOperations++){
            layout.step();
        }
        
        for(Vertex v : network.getVertices()){
            v.setPositionX((float)layout.getX(v));
            v.setPositionY((float)layout.getY(v));
        }
        
        
    }
}
