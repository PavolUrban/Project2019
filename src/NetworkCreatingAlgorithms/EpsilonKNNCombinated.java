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
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.SparseGraph;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 *
 * @author pavol
 */
public class EpsilonKNNCombinated {
    
    public Graph<Vertex, Edge> createNetwork(List<ChosenRecords> lines, String distanceMethod, double epsilon, int kValue)
    {
        List<Vertex> vertices = new ArrayList<>(); 
        SparseGraph <Vertex, Edge> graph = new SparseGraph<>();
        
        
        //initialize vertices
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
            
            int v2Index = 0;
            
            for(Vertex v2 : vertices)
            {
                if(v1.getId() != v2.getId()) //self-loops dissalowed
                {
                    double distance = Distances.countDistance(distanceMethod, v1.getValuesOfProps(), v2.getValuesOfProps());
                    
                    
                    //epsilon neighbourhood part
                    if(distance < epsilon)
                    {
                        v1.neighoursIdsAndDistances.put(v2Index, distance);
                        Edge e = new Edge(edgeId, distance);
                        graph.addEdge(e, v1, v2);
                    }
                  
                }
                
                v2Index++;
                edgeId++;
            }
            
            
            
            
            //KNN part
            if(v1.neighoursIdsAndDistances.size() < kValue)
            {    
                int anotherVertexId = 0;
            
                for(Vertex anotherVertex : vertices)
                {
                    if(v1.getId() != anotherVertex.getId())
                    {        
                        double distance = Distances.countDistance(distanceMethod, v1.getValuesOfProps(), anotherVertex.getValuesOfProps());

                        //number of neigbours is less then k - add neighbour automatically
                        if(v1.neighoursIdsAndDistances.size() < kValue)
                        {
                            v1.neighoursIdsAndDistances.put(anotherVertexId, distance);
                        }

                        //check if distance is lower than maximal distance in current neighbours list
                        else
                        {
                            double max = v1.neighoursIdsAndDistances.values().stream().max(Double::compare).get();
   
                            if(distance < max)
                            {
                                v1.neighoursIdsAndDistances.values().remove(max);
                                v1.neighoursIdsAndDistances.put(anotherVertexId, distance);
                            }
                        }
                    }
                    
                    anotherVertexId++;
                }
                
                //add edges from k-nn process
                for(Map.Entry<Integer,Double> neighbour : v1.neighoursIdsAndDistances.entrySet()) 
                {
                    //System.out.println("\t\tpozor toto je rozdiel "+ neighbour.getKey()+ " vs "+ vertices.get(neighbour.getKey()).getId());
                    graph.addEdge(new Edge(edgeId, neighbour.getValue()), v1, vertices.get(neighbour.getKey()));
                    edgeId++;
                 }
                
                
                System.out.println("TOTOTOT");
                System.out.println(v1.neighoursIdsAndDistances);
            }        
        }
        
        return graph;
    }
    
}
