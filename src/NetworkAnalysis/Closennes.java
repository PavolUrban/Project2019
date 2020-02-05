/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package NetworkAnalysis;

import NetworkComponents.Edge;
import NetworkComponents.Vertex;
import edu.uci.ics.jung.algorithms.scoring.ClosenessCentrality;
import edu.uci.ics.jung.graph.Graph;
import java.util.ArrayList;
import javafx.util.Pair;

/**
 *
 * @author pavol
 */
public class Closennes {
      private Graph<Vertex, Edge> network;
    private ArrayList<Pair<Vertex, Double>> scores;

    public Closennes(Graph<Vertex, Edge> network) {
        this.network = network;
    }
    
 
    public void count() {
        ClosenessCentrality centrality = new ClosenessCentrality(network);
        scores = new ArrayList<>();
        double alltogether= 0.0;
        for(Vertex v : network.getVertices()){
            scores.add(new Pair(v, centrality.getVertexScore(v)));
             System.out.println( ". som v closeness na uzle"+ v.toString()+ " jeho hodnota je "+centrality.getVertexScore(v)+" pocet uzlov pripojenych je ");
            alltogether += centrality.getVertexScore(v);
        }
        
        
        
        System.out.println("Vsetko dokopy je "+alltogether+" closeness je"+(alltogether/network.getVertexCount()));
    }
    
    public ArrayList<Pair<Vertex, Double>> getScores()
    {
        return scores;
    }
}
