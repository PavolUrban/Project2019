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
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;
import java.util.stream.Collectors;
import java.util.stream.DoubleStream;

/**
 *
 * @author pavol
 */
public class LRNet 
{
    static double tolerance = 1e-6;
    
    //Comparator to sort Map by valu
    public static <K, V extends Comparable<? super V>> Map<K, V> sortByValue(Map<K, V> map) {
        List<Entry<K, V>> list = new ArrayList<>(map.entrySet());
        list.sort(Entry.comparingByValue());
        
        Collections.reverse(list);
        
        Map<K, V> result = new LinkedHashMap<>();
        for (Entry<K, V> entry : list) {
            result.put(entry.getKey(), entry.getValue());
        }

        return result;
    }   
    
   
    private int getLocalDegree(List<Double> objectSimilarities, int currentVertexIndex, Vertex v, List<Vertex> vertices)
    {
        int numberOfNeighbours = 0;
 
        List<Double> relevantSimilarities = new ArrayList();
        Map<Integer, Double> neighbours = new HashMap<>();
             
        for(int i=0; i < objectSimilarities.size() ;i++)
        {    
            if( ( i != currentVertexIndex ) && ( objectSimilarities.get(i) > tolerance ))
            {
                relevantSimilarities.add(objectSimilarities.get(i));
                neighbours.put(vertices.get(i).getIndexInVerticesArray(), objectSimilarities.get(i));
                numberOfNeighbours++;
            }   
        }
        
        Map<Integer, Double> allNeighbours = sortByValue(neighbours);
        v.setLRNeighbours(allNeighbours);
        
        UserSettings.maxSimilarities.add(Collections.max(relevantSimilarities));
        
        return numberOfNeighbours;
    }
    
    
    private void assignLocalDegree(List<Vertex> vertices, double[][] similarityMatrix)
    {
        int vertexIndex=0;
        
        for(Vertex v : vertices)
        {
            List<Double> similarityValues = DoubleStream.of(similarityMatrix[vertexIndex]).boxed().collect(Collectors.toList());
            v.setAllSimilarities(similarityValues);
            v.setLocalDegree(getLocalDegree(similarityValues, vertexIndex, v, vertices));
            vertexIndex++;
        }
    
    }
    
    
    private static boolean objectsAreClose(double actual, double expected)
    {
        double diff = Math.abs(actual - expected);
        boolean areClose = false;
        
        if (diff < tolerance) 
        {
            areClose = true;
        }
        
        return areClose;
    }
    
    
   
    private void setRepresentativeness(List<Vertex> vertices, int minNeighbours)
    {
        for(Vertex v : vertices)
        {   
             //x-representativeness base
            double b = 0;
           
            //local representativeness
            double lr = 0.0;
            
            if(v.getLocalSignificance() > 0)
            {
                double exponent = 1.0 / v.getLocalSignificance();
                b = Math.pow( ( 1 + v.getLocalDegree() ), exponent);
                
                lr = 1.0 / b;
            }
            
            v.setLocalRepresentativenes(lr);
            
            double kValue = lr * v.getLocalDegree();
            kValue = Math.max(minNeighbours, (int) Math.round(kValue));    
            v.setKValue(kValue);
            
    
            Map<Integer, Double> finalNeighbours = new HashMap();
            int maxNeighbours = 0;
            double last=-75;// TODO doriesit last
            for (Map.Entry<Integer, Double> neighbour : v.getLRNeighbours().entrySet())
            {
                //if(objectsAreClose(neighbour.getValue(), kValue))
                if(maxNeighbours < kValue)
                {
                    last = neighbour.getValue();
                    finalNeighbours.put(neighbour.getKey(), neighbour.getValue());
                }
                else
                    break;
                maxNeighbours++;
            }
            
            
            v.setLRNeighbours(finalNeighbours);
        }
        
    }
    
    
    private void assignLocalSignificance(List<Vertex>vertices)
    {
        
        for(int i=0; i < vertices.size(); i++)
        {
            List<Double> allVertexSimilarities = vertices.get(i).getAllSimilarities();
            int localSignigicance = 0;
            for(int j=0; j< UserSettings.maxSimilarities.size(); j++)
            {              
                if(objectsAreClose(UserSettings.maxSimilarities.get(j), allVertexSimilarities.get(j)) && (i!=j))
                    localSignigicance++;
            }
            
            vertices.get(i).setLocalSignificance(localSignigicance);
        }
    }
    
    
   
    private double[][] countSimilarityMatrix(List<Vertex> vertices)
    {
        double similarityMatrix[][] = new double[vertices.size()][vertices.size()];
        
        int indexV1 = 0;
        for(Vertex v1 : vertices)
        {
            int indexV2 = 0;
            for(Vertex v2 : vertices)
            {
                similarityMatrix[indexV1][indexV2] = Distances.countRBF(v1.getValuesOfProps(), v2.getValuesOfProps());
                indexV2++;
            }
           
            indexV1++;            
        }
        
        return similarityMatrix;
    }
    
    
    //add parameter min neighbours
    public Graph<Vertex, Edge> createLRNetwork(List<ChosenRecords> lines, double epsilon) throws FileNotFoundException
    {         
        //to avoid out of bounds exception when network is created repeatedly by LRNet method
        UserSettings.maxSimilarities.clear();
        
        System.out.println("Counting LRNET");
        List<Vertex> vertices = new ArrayList<>(); 
        SparseGraph <Vertex, Edge> graph = new SparseGraph<>();

        //add vertices and get values from proper columns (attributes)
        int index = 0;
        for(ChosenRecords cr : lines)
        {
            ArrayList<Double> values = cr.getAttributesValuesAsList();
            Vertex v = new Vertex(cr.getRecordId());
            v.setValuesOfProps(values);
            v.setIndexInVerticesArray(index);
            vertices.add(v);
            graph.addVertex(v);
            index++;
        }
        
        double[][] similarityMatrix = countSimilarityMatrix(vertices);
        assignLocalDegree(vertices, similarityMatrix);
        assignLocalSignificance(vertices);  
        setRepresentativeness(vertices, 1);
        
        int edgeId = 0;
        for(Vertex v :vertices)
        {
            for (Map.Entry<Integer, Double> neighbour : v.getLRNeighbours().entrySet())
            {
                Edge e = new Edge(edgeId, neighbour.getValue());           
                graph.addEdge(e, v, vertices.get(neighbour.getKey()));
                edgeId++;
            }
        }
        
        return graph;
    }
}