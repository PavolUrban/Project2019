/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package NetworkCreatingAlgorithms;

import DataPreparation.ChosenRecords;
import DistancesMethods.Distances;
import NetworkComponents.*;
import UserSettings.ToMove;
import UserSettings.UserSettings;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.SparseGraph;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

/**
 *
 * @author pavol
 */
public class EpsilonNeighbourhoodGraph {
    
    private Vertex[] vertices;

    private Graph<Vertex, Edge> initVertices(int size, List<ChosenRecords> records) {
        vertices = new Vertex[size];
        SparseGraph <Vertex, Edge> graph = new SparseGraph<>();
        for(int i = 0; i < size; i++){
            vertices[i] = new Vertex(records.get(i).getRecordId());
            //System.out.println("adding "+records.get(i).getRecordId()+" and values "+records.get(i).getAttributesValues());
            graph.addVertex(vertices[i]);
            
        }
        return graph;
    }
    
    public Graph<Vertex, Edge> createNetwork(List<ChosenRecords> lines, double Epsilon, String distanceMethod, Boolean normalized) throws FileNotFoundException
    {
        double maxValue = 0.0;
//        Distances.countKernellTrick(2);
//        Distances.countKernellTrick(-2);
//        Distances.countKernellTrick(1);
//        Distances.countKernellTrick(-1);
        System.out.println();
        System.out.println("Som v Epsilon NG "+" .. stav normalized je "+normalized);
           
        String[] tempArray;
        ToMove.chosenRecords =lines;
        Map<Integer, String[]> map = new HashMap<Integer, String[]>();
        int numberOfVertices = 0;
        List<Integer> maxValues = new ArrayList<Integer>();
           List<Integer> minValues = new ArrayList<Integer>();
        
        for(ChosenRecords cr : lines)
        {    
            String record = cr.getAttributesValues();
            //System.out.println("jaaja "+ record);
            tempArray = record.split(",");
            map.put(numberOfVertices, tempArray);
            numberOfVertices++;
        }
       
       if(normalized)
       {
           
           Boolean firstTime = true;
           for (Map.Entry<Integer, String[]> firstObject : map.entrySet()) {
               
               int position = 0;
               for(String singleValue : firstObject.getValue())
                {
                    int value = Integer.parseInt(singleValue);
                       if(firstTime)
                       {
                           maxValues.add(value);
                           minValues.add(value);
                       }
                       
                       else
                       {
                           if(value > maxValues.get(position))
                           {
                               maxValues.set(position, value);
                           }
                           
                           if(value< minValues.get(position))
                           {
                               minValues.set(position, value);
                           }
                       }
                       position++;
                }
               
               firstTime = false;
         }
           System.out.println("*********Min values");
           for(Integer min : minValues)
           {
               System.out.println("\t"+min);
           }
           System.out.println("********Max values");
           for(Integer max : maxValues)
           {
           System.out.println("\t"+max);
           }
       }
        
       
        Graph<Vertex,Edge> graph = initVertices(numberOfVertices, lines);
     
        int edgeID=0;
        for (Map.Entry<Integer, String[]> firstObject : map.entrySet()) {
            for (Map.Entry<Integer, String[]> secondObject : map.entrySet()){
                
                  
                 ArrayList<Double> values1 = new ArrayList<>();
                 ArrayList<Double> values2 = new ArrayList<>();
                
                if(firstObject.getKey() != secondObject.getKey() && secondObject.getKey()>firstObject.getKey())
                {
                    int position1 = 0;
                    int position2= 0;
                    for(String singleValue : firstObject.getValue())
                    {
                        if(normalized)
                        {
                            double normalizedValue = (Double.parseDouble(singleValue)-minValues.get(position1))/(maxValues.get(position1)-minValues.get(position1));
                            values1.add(normalizedValue);
                        }
                        else
                            values1.add(Double.parseDouble(singleValue));
                        
                        position1++;
                    }
                    
                    for(String singleValue : secondObject.getValue())
                    {
                        if(normalized)
                        {
                            double normalizedValue = (Double.parseDouble(singleValue)-minValues.get(position2))/(maxValues.get(position2)-minValues.get(position2));
                            values2.add(normalizedValue);
                        }
                        else
                            values2.add(Double.parseDouble(singleValue));
                        
                        position2++;
                    }   
                    
                   
                    double distance;
                    
                    if(distanceMethod == "Pearsonuv korelační koeficient")
                    {
                            distance = Distances.countPearsonCorrelationCoefficient(values1,values2, values1.stream().mapToDouble(val -> val).average().orElse(0.0), values2.stream().mapToDouble(val -> val).average().orElse(0.0));
                            
                            distance = 1 - Math.abs(distance);
                            System.out.println("Pearson hodnota "+distance);
                    }
                     else
                    {
                           distance = countEuclideanDistance(values1, values2);
                            //System.out.println("euklid hodnota "+distance);
                    }
         
                    if(distance>maxValue)
                    {
                        maxValue = distance;
                    }
                    
                    if(distance< Epsilon)
                    {
                        graph.addEdge(new Edge(edgeID), vertices[firstObject.getKey()], vertices[secondObject.getKey()]);
                        edgeID++;
                    }
                    
                   // Distances.countJaccard(values1, values2);
                    //System.out.println("Vzdialenost medzi "+firstObject.getKey()+" "+secondObject.getKey()+" je "+countEuclideanDistance(values1, values2));
                }
            }
        }
        
        UserSettings.maxSliderValue = maxValue;
        
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
