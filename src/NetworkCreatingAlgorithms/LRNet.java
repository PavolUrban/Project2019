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
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author pavol
 */
public class LRNet 
{
    
    public Graph<Vertex, Edge> createLRNetwork(List<ChosenRecords> lines, double epsilon) throws FileNotFoundException
    {         
        Map<Integer, ArrayList<Double>> map = new HashMap<Integer, ArrayList<Double>>();       
       
        int numberOfVertices = 0;
        List<Vertex> vertices = new ArrayList<>();
        
        
        //get all values 
        for(ChosenRecords cr : lines)
        {
            ArrayList<Double> values = cr.getAttributesValuesAsList();
            map.put(cr.getRecordId(), values);
            numberOfVertices++;
        }
        
        //init vertices - in constructor vertices are created with significance = 0
        SparseGraph <Vertex, Edge> graph = new SparseGraph<>();
        
        
        for(int i = 0; i < numberOfVertices; i++)
        {
            Vertex v = new Vertex(lines.get(i).getRecordId());
            vertices.add(v);
        }
     
        
        int currentVertexIndex = 0;
        for (Map.Entry<Integer, ArrayList<Double>> firstObject : map.entrySet()) 
        {         
            int numberOfNeighbours = 0;
            double distanceOfNearestNeighbour = 500;
            List<Integer> idsOfNearestNeighbours = new ArrayList<Integer>();
            
            
            int neighbourVertexIndex = 0;
            
            for (Map.Entry<Integer, ArrayList<Double>> secondObject : map.entrySet())
            {
                boolean verticesAreNeighbours = false;                
               
                if(firstObject.getKey() !=  secondObject.getKey())
                {
                    double distance = countEuclideanDistance(firstObject.getValue(), secondObject.getValue());
                    
                    if(distance <= epsilon)
                    {
                         verticesAreNeighbours = true;
                         numberOfNeighbours++;
                         
                         if(distance < distanceOfNearestNeighbour)
                         {
                             idsOfNearestNeighbours.clear();
                             idsOfNearestNeighbours.add(neighbourVertexIndex);
                         }
                         
                         else if(distance == distanceOfNearestNeighbour)
                         {
                             idsOfNearestNeighbours.add(neighbourVertexIndex);
                         }
                    }
                       
                    
                }
                
                
                neighbourVertexIndex++;
            }   
            
            
            //update local significance
            for(int id : idsOfNearestNeighbours)
            {
               Vertex v = vertices.get(id);
               v.setSignificanceLRNet(v.getSignificanceLRNet() + 1);
            }
            
            //update number of neighbours
            Vertex currentVertex = vertices.get(currentVertexIndex);
            currentVertex.setNumberOfNeighbours(numberOfNeighbours);
         
            
            currentVertexIndex++;
            
            
        }
        
        
//        for(Vertex v : vertices)
//        {
//            System.out.println("Vertex "+ v.getId()+ " number of neighbours: "+ v.getNumberOfNeighbours() + ", localSignificance "+v.getSignificanceLRNet());
//            graph.addVertex(v);
//        }
        
        
        for(Vertex v : vertices)
        {
            if(v.getSignificanceLRNet()>0)
            {
            }
        }
        
        return null;
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
