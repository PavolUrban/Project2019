/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package NetworkCreatingAlgorithms;

import DataPreparation.ChosenRecords;
import DistancesMethods.Distances;
import NetworkComponents.Edge;
import NetworkComponents.Vertex;
import edu.uci.ics.jung.graph.SparseGraph;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 *
 * @author pavol
 */
public class NetworkByTopEdges {
 
    
    public NetworkByTopEdges(List<ChosenRecords> lines, String distanceMethod, double percentil)
    {
        List<Vertex> vertices = new ArrayList<>(); 
        SparseGraph <Vertex, Edge> graph = new SparseGraph<>();
        
        for(ChosenRecords cr : lines)
        {
            ArrayList<Double> values = cr.getAttributesValuesAsList();
            
            Vertex v = new Vertex(cr.getRecordId());
            v.setValuesOfProps(values);
            vertices.add(v);
            graph.addVertex(v);
        }
        
        
        int edgeId = 0;
        
        for(Vertex v1 : vertices)
        {
            for(Vertex v2 : vertices)
            {
                if(v1.getId() != v2.getId())
                {
                    double distance = Distances.countDistance(distanceMethod, v1.getValuesOfProps(), v2.getValuesOfProps());
                    Edge e = new Edge(edgeId, distance); 
                    graph.addEdge(e, v1, v2);
                }
                
                edgeId++;
            }
        }
        
        Collection<Edge> allPossibleEdges = graph.getEdges();
        ArrayList<Edge> sortedEdges = new ArrayList<>(allPossibleEdges);
       
        Collections.sort(sortedEdges);
   
        
        
        
        double edgesToAdd = (percentil / 100) * sortedEdges.size();
        System.out.println("We should add "+edgesToAdd +" current number of edges is "+sortedEdges.size());
        int numberOfAddedEdges =0;
        for(Edge e : allPossibleEdges)
        {
            if(numberOfAddedEdges > edgesToAdd)
                graph.removeEdge(e);
            
            numberOfAddedEdges++;
        }
           
        System.out.println("Now there are "+ graph.getEdgeCount());
        
    }
    
}
