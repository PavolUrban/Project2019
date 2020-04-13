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
import java.util.List;

/**
 *
 * @author pavol
 */
public class EpsilonNew {
    
    
    public Graph<Vertex, Edge> createEpsilonNetwork(List<ChosenRecords> lines, String distanceMethod, double epsilon)
    {
        List<Vertex> vertices = new ArrayList<>(); 
        SparseGraph <Vertex, Edge> graph = new SparseGraph<>();

        //add vertices and get values from proper columns (attributes)
        for(ChosenRecords cr : lines)
        {
            ArrayList<Double> values = cr.getAttributesValuesAsList();   
            Vertex v = new Vertex(cr.getRecordId());
            v.setValuesOfProps(values);
            v.setClusterId(UserSettings.mapAliasingForColorization.get(cr.getClassName()));
//            System.out.println(cr.getClassName() +" vs "+ v.clusterId);
//            System.out.println(values);
            vertices.add(v);
            graph.addVertex(v);
        }
       
        
        int edgeId = 0;
        double maxDistance = 0.0;
        
        //add edges
        for(Vertex v1 : vertices)
        {
            for(Vertex v2 : vertices)
            {
                if(v1.getId() != v2.getId()) //self-loops dissalowed
                {
                    double distance = Distances.countDistance(distanceMethod, v1.getValuesOfProps(), v2.getValuesOfProps());
                    
                    if(distance < epsilon)
                    {
                        graph.addEdge(new Edge(edgeId, distance), v1, v2);
                        edgeId++;
                    }
                    
                    //for slider max value
                    if(distance > maxDistance)
                    {
                        maxDistance = distance;
                    }
                }
            }
        }
        
        UserSettings.maxSliderValue = maxDistance;
        
        return graph;
        
    }

}