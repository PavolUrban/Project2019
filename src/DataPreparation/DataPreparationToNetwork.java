/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DataPreparation;

import NetworkComponents.Edge;
import NetworkComponents.Vertex;
import NetworkCreatingAlgorithms.EpsilonNeighbourhoodGraph;
import edu.uci.ics.jung.graph.Graph;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author pavol
 */
public class DataPreparationToNetwork {
 
    public static Graph<Vertex, Edge> readSpecificLines(Graph<Vertex, Edge> network, List<Headers> headers, Boolean filterYear, Boolean filterSex,int sex, Boolean filterGrade, Boolean filterRegion, Boolean filterSchool) throws IOException
    {   
        File file = new File("C:\\A11.csv"); 
        List<String> lines = Files.readAllLines(file.toPath(), StandardCharsets.UTF_8); 
        lines.remove(0); //remove header
        
        //TODO
        if(filterYear == true || filterSex == true || filterRegion == true || filterGrade == true ||filterSchool == true)
        {
            //TODO remove records with no filter value in column to prevent NULLPointerException
            network = getOnlyRelevantRows(network, headers, lines, filterYear, filterSex,sex, filterGrade, filterRegion, filterSchool);
        }
        else
        {
             EpsilonNeighbourhoodGraph eng = new EpsilonNeighbourhoodGraph();
             network = eng.createNetwork(getOnlyRelevantColumns(headers, lines));
        }
        
        return network;
    }
    
    public static Graph<Vertex, Edge> getOnlyRelevantRows(Graph<Vertex, Edge> network, List<Headers> headers, List<String> lines, Boolean filterYear, Boolean filterSex,int sex, Boolean filterGrade, Boolean filterRegion, Boolean filterSchool) throws FileNotFoundException
    {
        List<String> finalLines = new ArrayList<>();
   
        int a = 0;
        for(String line : lines)
        {
            if(a>9)
            {
                break;
            }
            
            String [] array = line.split(",");
           
            //filtering rows
            if(filterSex == true)
            {
                //System.out.println(array[3]);
                if(Integer.parseInt(array[3])== sex)
                {
                    finalLines.add(line);
                    //System.out.println("Chlapci su nasledujuci "+array[3]);
                }
            }
            a++;
        }
        
        //filtering columns
        List<ChosenRecords> chosenRecords = getOnlyRelevantColumns(headers, finalLines);
        
        EpsilonNeighbourhoodGraph eng = new EpsilonNeighbourhoodGraph();
        network = eng.createNetwork(chosenRecords);
        
        return network;
    }
    
    public static List<ChosenRecords> getOnlyRelevantColumns(List<Headers> headers, List<String> lines)
    {
        int id = 0;
        List<ChosenRecords> chosenRecords = new ArrayList();
        
        for(String line : lines)
        {
            if(id>9)
            {
                break;
            }
            ChosenRecords cr = new ChosenRecords(id);
            Boolean hasEmptyProperty = false;
            String [] array = line.split(",");
            int counter = 0;
            for(Headers h : headers)
            {
                System.out.println("Hlavicka "+h.getHeaderName()+" ma id "+h.getId()+" "+array[h.getId()]);
                
                if(counter ==0)
                {
                    if(array[h.getId()].equalsIgnoreCase("") || array[h.getId()]==null || array[h.getId()].isEmpty()) 
                    {
                        System.out.println("Empty property");
                        hasEmptyProperty = true;   
                    }
                       
                    
                    cr.attributesValues = array[h.getId()];
                }
                else
                {
                    if(array[h.getId()].equalsIgnoreCase("") || array[h.getId()]==null || array[h.getId()].isEmpty()) 
                    {
                        System.out.println("Empty property");
                        hasEmptyProperty = true;
                    }
                    cr.attributesValues = cr.attributesValues+ ","+array[h.getId()];
                }
                counter++;
            }
           
            if(!hasEmptyProperty)
            {
                System.out.println("Pridavam uzol!");
                chosenRecords.add(cr);
            }
            
            id++;
        }
        
        return chosenRecords;
    }
    
    
    
}
