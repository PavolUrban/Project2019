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
import UserSettings.UserSettings;
import edu.uci.ics.jung.graph.Graph;
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
 
    
    public Graph<Vertex, Edge> createNetworkByTopEdges(List<ChosenRecords> lines, String distanceMethod, double percentil)
    {
        List<Vertex> vertices = new ArrayList<>(); 
        SparseGraph <Vertex, Edge> graph = new SparseGraph<>();
        
        for(ChosenRecords cr : lines)
        {
            ArrayList<Double> values = cr.getAttributesValuesAsList();
            
            Vertex v = new Vertex(cr.getRecordId());
            v.setClusterId(UserSettings.mapAliasingForColorization.get(cr.getClassName()));
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
        
        for(Edge e: sortedEdges)
        {
            System.out.println(e.getId() + " "+ e.getWeight());
        }
   
        
        
        
        double edgesToAdd = (percentil / 100) * sortedEdges.size();
        System.out.println("We should add "+edgesToAdd +" current number of edges is "+sortedEdges.size());
        int numberOfAddedEdges = 0;
        
        for(Edge e : sortedEdges)
        {
            if(numberOfAddedEdges > edgesToAdd)
                graph.removeEdge(e);
            
            numberOfAddedEdges++;
        }
           
        Collection<Edge> realEdgesInGraph = graph.getEdges();
        
        
        System.out.println("TOTO TU MAME NAOZAJ");
        for(Edge e: realEdgesInGraph)
        {
            System.out.println(e.getId()+ ", weight: "+e.getWeight());
        }
        
        System.out.println("Now there are "+ graph.getEdgeCount());
       
        return graph;
    }
    
}
