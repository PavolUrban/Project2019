/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Sampling;

import NetworkAnalysis.Degreee;
import NetworkComponents.Edge;
import NetworkComponents.Vertex;
import UserSettings.UserSettings;
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
public class RandomDegreeNode {
        
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
            picked.add(RANDOM.nextInt(k));
        }
        return picked;
    }
    
    public RandomDegreeNode(Graph<Vertex, Edge> network) {
        this.network = network;
    }
    
    
    
    public Graph<Vertex, Edge> run(double percentage) {
        
        Degreee degree = new Degreee(network);
        degree.count();
        
        final Set<Integer> pickedRandomNumbers = new HashSet<>();

        
        System.out.println("Max deg "+ UserSettings.maxDegree);
        System.out.println("Min deg "+UserSettings.minDegree);
        
        Collection<Vertex> allVertices = network.getVertices();
        ArrayList<Vertex> vertices = new ArrayList<>(allVertices);
        
        double numberOfVerticesInSample = vertices.size() * percentage;
        int roundedNumber = (int) numberOfVerticesInSample;
        Set<Integer> verticesToAdd = new HashSet<>();
        
        Random r = new Random();
        
        while(verticesToAdd.size() < roundedNumber)
        {
            int randomVertexId = RANDOM.nextInt(vertices.size()-1);
            Vertex v = vertices.get(randomVertexId);
            double normalizedDegree = ( (double) network.getOutEdges(v).size() - (double) UserSettings.minDegree) / ( (double)UserSettings.maxDegree - (double)UserSettings.minDegree);
            
            double randomValue = 0 + (1 - 0) * r.nextDouble();
            System.out.println("Porovnavam "+normalizedDegree+" vs "+randomValue+ " povodne bol degree "+v.getDegree());
            if(normalizedDegree > randomValue)
            {
                pickedRandomNumbers.add(randomVertexId);
                verticesToAdd.add(v.getId());
                v.setIsInSample(true);
            }
        }
  
        
        for(Vertex v : vertices)
        {
            if(!v.getIsInSample())
                network.removeVertex(v);
        }
        
        return network;
    }
}
