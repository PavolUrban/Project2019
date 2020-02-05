/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package NetworkAnalysis;

import NetworkComponents.Edge;
import NetworkComponents.Vertex;
import edu.uci.ics.jung.algorithms.shortestpath.DistanceStatistics;
import edu.uci.ics.jung.graph.Graph;

/**
 *
 * @author pavol
 */
public class DiameterClass {
     private Graph<Vertex, Edge> network;
    private double diamaterValue;

    public DiameterClass(Graph<Vertex, Edge> network) {
        this.network = network;
    }
    
    
    public void count() {
       
         diamaterValue  = DistanceStatistics.diameter(network);
       
    }
    
   
    public double getDiameter()
    {
          System.out.println("Toto je hodnota ktoru posielam"+diamaterValue);
        return diamaterValue;
    }
}
