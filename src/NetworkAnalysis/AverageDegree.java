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
public class AverageDegree {
    private Graph<Vertex, Edge> network;
    private double averageDegree;
    
    

    public AverageDegree(Graph<Vertex, Edge> network) {
        this.network = network;
    }
    
    
    public void count() {
        double upside=0;
        System.out.println("Horna strana zlomku");
        
        int maxDegree = 10;
        int minDegree = 500;
        for(Vertex v : network.getVertices()){
          
            upside += (double)network.getOutEdges(v).size();
            if(network.getOutEdges(v).size()< minDegree)
            {
                minDegree = network.getOutEdges(v).size();
            }
            
            if(network.getOutEdges(v).size()> maxDegree)
            {
                maxDegree = network.getOutEdges(v).size();
            }
        }
        
        System.out.println("Max degree "+maxDegree+" min degree "+minDegree);
        double numberOfVertices = network.getVertexCount();
        
        averageDegree = upside/numberOfVertices;
        
    
    }
    
   
    public double getAverageDegree()
    {
        return averageDegree;
    }
}
