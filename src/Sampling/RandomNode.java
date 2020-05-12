/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Sampling;

import NetworkComponents.Edge;
import NetworkComponents.Vertex;
import edu.uci.ics.jung.graph.Graph;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

/**
 *
 * @author pavol
 */
public class RandomNode {
    
    private Graph<Vertex, Edge> network;
    
    private static final Random RANDOM = new Random();    
   /**
     * Pick n numbers between 0 (inclusive) and k (inclusive)
     * While there are very deterministic ways to do this,
     * for large k and small n, this could be easier than creating
     * an large array and sorting, i.e. k = 10,000
     */
    public Set<Integer> pickRandom(int n, int k) {
        final Set<Integer> picked = new HashSet<>();
        while (picked.size() < n) {
            picked.add(RANDOM.nextInt(k-1));
        }
        return picked;
    }
    
    public RandomNode(Graph<Vertex, Edge> network) {
        this.network = network;
    }
    
    
    
    public Graph<Vertex, Edge> run(double percentage) {
        
        
        double currentlyAdded = 0.0;
        
        Collection<Vertex> allVertices = network.getVertices();
        ArrayList<Vertex> vertices = new ArrayList<>(allVertices);
        
        double numberOfVerticesInSample = vertices.size() * percentage;
        int roundedNumber = (int) numberOfVerticesInSample;
        Set<Integer> verticesToAdd = pickRandom(roundedNumber, vertices.size());
        
        for(Integer v : verticesToAdd)
        {
            Vertex vToSample = vertices.get(v);
            vToSample.setIsInSample(true);
        }
        
        for(Vertex v : vertices)
        {
            if(!v.getIsInSample())
                network.removeVertex(v);
        }
        
        return network;
    }
}
