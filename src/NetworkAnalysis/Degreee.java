/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package NetworkAnalysis;

import NetworkComponents.Edge;
import NetworkComponents.Vertex;
import UserSettings.UserSettings;
import edu.uci.ics.jung.graph.Graph;
import java.util.ArrayList;
import javafx.util.Pair;

/**
 *
 * @author pavol
 */
public class Degreee {
    private Graph<Vertex, Edge> network;
    private ArrayList<Pair<Vertex, Double>> scores;
    
    
    public Degreee(Graph<Vertex, Edge> network)
    {
        this.network=network;
    }
    
    public void count() {
        scores = new ArrayList<>();
        for(Vertex v : network.getVertices()){
            
            int degree = network.getOutEdges(v).size() / 2;
            
            if(degree < UserSettings.minDegree)
                UserSettings.minDegree = degree;
            
            if(degree > UserSettings.maxDegree)
                UserSettings.maxDegree = degree;
            
            
            v.setDegree(degree);
            scores.add(new Pair(v, (double)degree));
        }
    }
     
    public ArrayList<Pair<Vertex, Double>> getScores()
    {
        return scores;
    }
}



   
    
 