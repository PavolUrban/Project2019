package NetworkCreatingAlgorithms;

import DataPreparation.ChosenRecords;
import DistancesMethods.Distances;
import NetworkComponents.Edge;
import NetworkComponents.Vertex;
import UserSettings.UserSettings;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.SparseGraph;
import java.io.FileNotFoundException;
import java.time.Duration;
import java.time.ZonedDateTime;
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
    
   
    private int getLocalDegree(List<Double> objectSimilarities, int currentVertexIndex, Vertex v, List<Vertex> vertices, double tolerance)
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

        System.out.println("Toto je dlzka kolekcie "+relevantSimilarities.size());
        
        if(relevantSimilarities.size() < 1)
            UserSettings.maxSimilarities.add(null);
        else
            UserSettings.maxSimilarities.add(Collections.max(relevantSimilarities));
        
        return numberOfNeighbours;
    }
    
    
    private void assignLocalDegree(List<Vertex> vertices, double[][] similarityMatrix, double tolerance)
    {
        int vertexIndex=0;
        
        for(Vertex v : vertices)
        {
            List<Double> similarityValues = DoubleStream.of(similarityMatrix[vertexIndex]).boxed().collect(Collectors.toList());
            v.setAllSimilarities(similarityValues);
            v.setLocalDegree(getLocalDegree(similarityValues, vertexIndex, v, vertices, tolerance));
            vertexIndex++;
        }
    
    }
    
    
    private static boolean objectsAreClose(double actual, double expected, double tolerance)
    {
        double diff = Math.abs(actual - expected);
        boolean areClose = false;
        
        if (diff < tolerance) 
        {
            areClose = true;
        }
        
        return areClose;
    }
    
    
   
    
    private void assignLocalSignificance(List<Vertex>vertices, int minNeighbours, Graph<Vertex, Edge> graph, double tolerance)
    {
        
        for(int i=0; i < vertices.size(); i++)
        {
            Vertex v = vertices.get(i);
            
            //local significance part
            List<Double> allVertexSimilarities = v.getAllSimilarities();
            int localSignigicance = 0;
            for(int j=0; j< UserSettings.maxSimilarities.size(); j++)
            {              
                if(UserSettings.maxSimilarities.get(j) != null)
                {
                    if(objectsAreClose(UserSettings.maxSimilarities.get(j), allVertexSimilarities.get(j), tolerance) && (i!=j))
                        localSignigicance++;
                }
                
            }

            v.setLocalSignificance(localSignigicance);
            
            
            //caluculating representativeness from here
            double b = 0; //x-representativeness base
            double lr = 0.0;//local representativeness
            
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

            for (Map.Entry<Integer, Double> neighbour : v.getLRNeighbours().entrySet())
            {
                //if(objectsAreClose(neighbour.getValue(), kValue))
                if(maxNeighbours < kValue)
                {
                    finalNeighbours.put(neighbour.getKey(), neighbour.getValue());
                }
                else
                    break;
                maxNeighbours++;
            }
            
            
            v.setLRNeighbours(finalNeighbours);
      
            int edgeId = 0;
            for (Map.Entry<Integer, Double> neighbour : v.getLRNeighbours().entrySet())
            {
                Edge e = new Edge(edgeId, neighbour.getValue());           
                graph.addEdge(e, v, vertices.get(neighbour.getKey()));
                edgeId++;
            }
            
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
                similarityMatrix[indexV1][indexV2] = countRBF(v1.getValuesOfProps(), v2.getValuesOfProps());
                indexV2++;
            }
           
            indexV1++;            
        }
        
        return similarityMatrix;
    }
    
    
    //add parameter min neighbours
    public Graph<Vertex, Edge> createLRNetwork(List<ChosenRecords> lines, double tolerance) throws FileNotFoundException
    {         
        //to avoid out of bounds exception when network is created repeatedly by LRNet method
        UserSettings.maxSimilarities.clear();
        
        System.out.println("Counting LRNET");
        
        List<Vertex> vertices = new ArrayList<>(); 
        SparseGraph <Vertex, Edge> graph = new SparseGraph<>();
        ZonedDateTime startTime = ZonedDateTime.now();
        
        //add vertices and get values from proper columns (attributes)
        int index = 0;
        for(ChosenRecords cr : lines)
        {
            ArrayList<Double> values = cr.getAttributesValuesAsList();
            Vertex v = new Vertex(cr.getRecordId());
            v.setValuesOfProps(values);
            v.setClusterId(UserSettings.mapAliasingForColorization.get(cr.getClassName()));
            v.setIndexInVerticesArray(index);
            vertices.add(v);
            graph.addVertex(v);
            index++;
        }
        
        System.out.println("Counting similarityMatrix");
        double[][] similarityMatrix = countSimilarityMatrix(vertices);
        System.out.println("**Asinging local degree");
        
        assignLocalDegree(vertices, similarityMatrix, tolerance);
        
        System.out.println("now local significance with representativeness  + add edges");
        assignLocalSignificance(vertices, 1, graph, tolerance); 
       
        
      
        
        ZonedDateTime endTime = ZonedDateTime.now();
        Duration duration = Duration.between(startTime, endTime);
        System.out.println("cas "+ duration.toMillis()/1000+" s");
       
        
        return graph;
    }
    
    
    public static double countRBF(ArrayList<Double> values1, ArrayList<Double> values2)
    {
        double sum = 0.0;
        
        for (int x=0; x<values1.size(); x++)
        {
            
            sum += Math.pow(values1.get(x)-values2.get(x),2);
        }

       double finalResult = Math.exp(-1 * sum);     
       
        return finalResult;
    }
}
