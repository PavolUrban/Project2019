/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package NetworkAnalysis;

import NetworkComponents.Edge;
import NetworkComponents.Vertex;
import edu.uci.ics.jung.algorithms.scoring.BetweennessCentrality;
import edu.uci.ics.jung.graph.Graph;
import java.util.ArrayList;
import javafx.util.Pair;

/**
 *
 * @author pavol
 */
public class Betweenness {
    private Graph<Vertex, Edge> network;
    private ArrayList<Pair<Vertex, Double>> scores;

    public Betweenness(Graph<Vertex, Edge> network) {
        this.network = network;
    }
    
    
    public void count() {
        BetweennessCentrality centrality = new BetweennessCentrality(network);
        scores = new ArrayList<>();
        int a=0;
         double alltogether= 0.0;
        for(Vertex v : network.getVertices()){
            System.out.println(a+ ". som v betweennes na uzle"+ v.toString()+ " jeho hodnota je "+centrality.getVertexScore(v));
            scores.add(new Pair(v, centrality.getVertexScore(v)));
              alltogether += centrality.getVertexScore(v);
            a++;
        }
        
          System.out.println("Vsetko dokopy je "+alltogether+" betweennes clustering je"+(alltogether/network.getVertexCount()));
    }
    
   
    public ArrayList<Pair<Vertex, Double>> getScores()
    {
        return scores;
    }
}
