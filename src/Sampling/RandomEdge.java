/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Sampling;

import NetworkComponents.Edge;
import NetworkComponents.Vertex;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.util.Pair;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

/**
 *
 * @author pavol
 */
public class RandomEdge {
    private Graph<Vertex, Edge> network;
    
    private static final Random RANDOM = new Random();    
   
    
    public RandomEdge(Graph<Vertex, Edge> network) {
        this.network = network;
    }
    
    
    public Graph<Vertex, Edge> run(double percentage) {
        
        Collection<Vertex> allVertices = network.getVertices();
        ArrayList<Vertex> vertices = new ArrayList<>(allVertices);

        
        Collection<Edge> allEdges = network.getEdges();
        ArrayList<Edge> edges = new ArrayList<>(allEdges);
        
        int numberOfVertices = network.getVertexCount();
        double numberOfVerticesInSample = numberOfVertices * percentage;
        int roundedNumber = (int) numberOfVerticesInSample;
        
        final Set<Integer> pickedRandomNumbers = new HashSet<>();
        final Set<Integer> verticesIds = new HashSet<>();
        
        while(verticesIds.size() < roundedNumber)
        {
            int newRandomNumber = RANDOM.nextInt(network.getVertexCount()-1);
            pickedRandomNumbers.add(newRandomNumber);
            Edge edgeToSample = edges.get(newRandomNumber);
            edgeToSample.setIsInSample(true);
            Pair<Vertex> p = network.getEndpoints(edgeToSample);
            Vertex v1 = p.getFirst();
            Vertex v2 = p.getSecond();
            v1.setIsInSample(true);
            v2.setIsInSample(true);
            verticesIds.add(v1.getId());
            verticesIds.add(v2.getId());
        }
        
        for(Vertex v : vertices)
        {
            if(!v.getIsInSample())
                network.removeVertex(v);
        }
        
        return network;
    }
}
