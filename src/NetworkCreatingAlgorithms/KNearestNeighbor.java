/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package NetworkCreatingAlgorithms;

import DataPreparation.ChosenRecords;
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
    
    
   
    
    public Graph<Vertex, Edge> createKNNNetwork(List<ChosenRecords> lines, int k) throws FileNotFoundException
    {
        
        String[] tempArray;
        
        Map<Integer, String[]> map = new HashMap<Integer, String[]>();
        Map<Integer, Integer> topNeighbor = new HashMap<Integer, Integer>();
        
       
        int numberOfVertices = 0;
        
        for(ChosenRecords cr : lines)
        {
            String record = cr.getAttributesValues();
            tempArray = record.split(",");
            map.put(numberOfVertices, tempArray);
            numberOfVertices++;
        }
        
        Graph<Vertex,Edge> graph = initVertices(numberOfVertices, lines);
      //  System.out.println("Pocet uzlov bude "+numberOfVertices);
        int edgeID=0;
        for (Map.Entry<Integer, String[]> firstObject : map.entrySet()) {
            List<Double> topsDistances = new ArrayList<Double>();
            List<Integer> topNeighbours = new ArrayList<Integer>();
            
            for (Map.Entry<Integer, String[]> secondObject : map.entrySet()){
                 ArrayList<Double> values1 = new ArrayList<>();
                 ArrayList<Double> values2 = new ArrayList<>();
                
                if(firstObject.getKey() != secondObject.getKey())
                {
                    //System.out.println("Pracujeme na uzle "+firstObject.getKey());
                    for(String singleValue : firstObject.getValue())
                    {
                        values1.add(Double.parseDouble(singleValue));
                    }
                    
                    for(String singleValue : secondObject.getValue())
                    {
                        values2.add(Double.parseDouble(singleValue));
                    }
                   
                    
                    double distance = countEuclideanDistance(values1, values2);
                    
                    int secondID = secondObject.getKey();
                    
                    if(topsDistances.size()<k)
                    {    
                        topsDistances.add(distance);
                        topNeighbours.add(secondID);
                    }
                    
                    else
                    {
                        if(distance < Collections.min(topsDistances))
                        {
                            int index = topsDistances.indexOf(Collections.min(topsDistances));
                            topsDistances.remove(index);
                            topNeighbours.remove(index);
                            topsDistances.add(distance);
                            topNeighbours.add(secondID);
                        }
                    }
                }
                 
                 
            }
           
            for(Integer neighboursIDs : topNeighbours)
            {
                graph.addEdge(new Edge(edgeID), vertices[firstObject.getKey()], vertices[neighboursIDs]);
                edgeID++;
            }
            
            
        }
        
        
        return graph;
    }


    public double countEuclideanDistance( ArrayList<Double> values1,  ArrayList<Double> values2)
    {
        double sum = 0.0;
        for (int x=0; x<values1.size(); x++)//nezacinam 0 aby sa ignoroval prvy riadok - premysliet
        {
            
            sum += Math.pow(values1.get(x)-values2.get(x),2);
        }

        double result = Math.sqrt(sum);
        
        return result;
    }
}
