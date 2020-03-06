/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package NetworkAnalysis;

import NetworkComponents.Edge;
import NetworkComponents.Vertex;
import edu.uci.ics.jung.algorithms.cluster.WeakComponentClusterer;
import edu.uci.ics.jung.algorithms.scoring.ClosenessCentrality;
import edu.uci.ics.jung.graph.Graph;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import javafx.scene.paint.Color;

/**
 *
 * @author pavol
 */
public class NumberOfComponents {
    
    private Graph<Vertex, Edge> network;
  
    public NumberOfComponents(Graph<Vertex, Edge> network) {
        this.network = network;
    }
    
    
    public int getNumberOfComponents()
    {
   
        WeakComponentClusterer wcc = new WeakComponentClusterer();
        Set<Set<Vertex>> ll = wcc.transform(network);
        
        int counter = 0;
        for(Set<Vertex> test : ll)
        {
            for(Vertex v : test)
            {
                v.clusterId = counter;
            }
            
            counter++;
        }
        
        
        return ll.size();
    }
}
