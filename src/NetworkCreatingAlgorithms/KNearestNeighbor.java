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
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

/**
 *
 * @author pavol
 */
public class KNearestNeighbor {
    private Vertex[] vertices;

    private Graph<Vertex, Edge> initVertices(int size, List<ChosenRecords> records) {
        vertices = new Vertex[size];
        SparseGraph <Vertex, Edge> graph = new SparseGraph<>();
        for(int i = 0; i < size; i++){
            vertices[i] = new Vertex(records.get(i).getRecordId());
            graph.addVertex(vertices[i]);
            
        }
        return graph;
    }
    
    
   
    
//    public Graph<Vertex, Edge> createKNNNetwork(List<ChosenRecords> lines, int k) throws FileNotFoundException
//    {
//        
//        System.out.println("Pocitam KNN");
//        String[] tempArray;
//        
//        Map<Integer, String[]> map = new HashMap<Integer, String[]>();
//        Map<Integer, Integer> topNeighbor = new HashMap<Integer, Integer>();
//        
//       
//        int numberOfVertices = 0;
//        
//        for(ChosenRecords cr : lines)
//        {
//            String record = cr.getAttributesValues();
//            tempArray = record.split(",");
//            map.put(numberOfVertices, tempArray);
//            numberOfVertices++;
//        }
//        
//        Graph<Vertex,Edge> graph = initVertices(numberOfVertices, lines);
//      //  System.out.println("Pocet uzlov bude "+numberOfVertices);
//        int edgeID=0;
//        
//        for (Map.Entry<Integer, String[]> firstObject : map.entrySet()) {
//            List<Double> topsDistances = new ArrayList<Double>();
//            List<Integer> topNeighbours = new ArrayList<Integer>();
//            
//            for (Map.Entry<Integer, String[]> secondObject : map.entrySet()){
//                 ArrayList<Double> values1 = new ArrayList<>();
//                 ArrayList<Double> values2 = new ArrayList<>();
//                
//                if(firstObject.getKey() != secondObject.getKey())
//                {
//                    //System.out.println("Pracujeme na uzle "+firstObject.getKey());
//                    for(String singleValue : firstObject.getValue())
//                    {
//                        values1.add(Double.parseDouble(singleValue));
//                    }
//                    
//                    for(String singleValue : secondObject.getValue())
//                    {
//                        values2.add(Double.parseDouble(singleValue));
//                    }
//                   
//                    
//                    double distance = countEuclideanDistance(values1, values2);
//                    
//                    int secondID = secondObject.getKey();
//                    
//                    if(topsDistances.size()<k)
//                    {    
//                        topsDistances.add(distance);
//                        topNeighbours.add(secondID);
//                    }
//                    
//                    else
//                    {
//                        if(distance < Collections.min(topsDistances))
//                        {
//                            int index = topsDistances.indexOf(Collections.min(topsDistances));
//                            topsDistances.remove(index);
//                            topNeighbours.remove(index);
//                            topsDistances.add(distance);
//                            topNeighbours.add(secondID);
//                        }
//                    }
//                }
//                 
//                 
//            }
//           
//            for(Integer neighboursIDs : topNeighbours)
//            {
//                graph.addEdge(new Edge(edgeID), vertices[firstObject.getKey()], vertices[neighboursIDs]);
//                edgeID++;
//            }
//            
//            
//        }
//        
//        
//        return graph;
//    }
    
    
    public Graph<Vertex, Edge> createKNNNetwork(List<ChosenRecords> lines, int k, String distanceMethod)
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
        
       // System.out.println("K je nastavene na "+k);
        
        int edgeID = 0;
        
        
        int v1Index = 0;
        
        
        for(Vertex v1 : vertices)
        {
         //   System.out.println("Pracujem na uzle "+v1.getId());
            int numberOfNeighbours = 0;
            
            int v2Index = 0;
            
            for(Vertex v2 : vertices)
            {
                
                if(v1.getId() != v2.getId())
                {        
                    double distance = Distances.countDistance(distanceMethod, v1.getValuesOfProps(), v2.getValuesOfProps());
               
                    //number of neigbours is less then k - add neighbour automatically
                    if(numberOfNeighbours < k)
                    {
           //             System.out.println("\tEste nema dost susedov, pridavam "+ v2.getId());
                        numberOfNeighbours++;
                        v1.neighoursIdsAndDistances.put(v2Index, distance);
                    }

                    else
                    {


                        double max = v1.neighoursIdsAndDistances.values().stream().max(Double::compare).get();
//                        System.out.println("\t  Uz ma dost susedov musim porovnavat");
//
//                        System.out.println("\t  Mapa vyzera takto: " +  v1.neighoursIdsAndDistances);
//                        System.out.println("\t Max je "+max +" a distance "+ distance);

                        if(distance < max)
                        {
                         //   System.out.println("\t\tvzdialenost je mensia musim mazat");
                            v1.neighoursIdsAndDistances.values().remove(max);
                            v1.neighoursIdsAndDistances.put(v2Index, distance);

                           // System.out.println("\t\tPo zmazani je taketo mapa "+ v1.neighoursIdsAndDistances);
                        }



                    }
                }
                
                
                v2Index++;
            }
            
            for(Map.Entry<Integer,Double> neighbour : v1.neighoursIdsAndDistances.entrySet()) 
            {
//                String key = entry.getKey();
//                Integer value = entry.getValue();
                graph.addEdge(new Edge(edgeID), vertices.get(v1Index), vertices.get(neighbour.getKey()));
                edgeID++;
             }
            
            
            //System.out.println("-----------------------------------------");
            
            v1Index++;
        }
    
        
        return graph;
    }


  
}
