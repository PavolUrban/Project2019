/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package NetworkAnalysis;

import NetworkComponents.Edge;
import NetworkComponents.Vertex;
import edu.uci.ics.jung.graph.Graph;

/**
 *
 * @author pavol
 */
public class NetworkDensity {
     private Graph<Vertex, Edge> network;
    private double networkDeensity;

    public NetworkDensity(Graph<Vertex, Edge> network) {
        this.network = network;
    }
    
    
    public void count() {
       double numberOfVertices = network.getVertexCount();
        double numberOfPotentialConnections =(numberOfVertices *(numberOfVertices-1)/2);
       double numberOfConnections =network.getEdgeCount();
    
            networkDeensity = (numberOfConnections/numberOfPotentialConnections);
            
           double total = 0;
        total += 5.6;
        total += 5.8;
        System.out.println(total);
        
         double total2 = 0.0;
        total2 += 5.6;
        total2 += 5.8;
        System.out.println(total2);
        
    }
    
   
    public double getScores()
    {
        return networkDeensity;
    }
    
   
}
