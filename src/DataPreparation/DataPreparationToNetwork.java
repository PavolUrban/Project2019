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
 /*
    public static Graph<Vertex, Edge> readSpecificLines() throws IOException
    {
        File file = new File("C:\\A11.csv"); 
        List<String> lines = Files.readAllLines(file.toPath(), StandardCharsets.UTF_8); 
        int a =0;
        for (String line : lines) 
        { 
            if(a>10)
            {
                break;
            }

            String[] array = line.split(","); 
            System.out.println(array[0]+" "+array[array.length-1]); 
            a++;
        }
        //private Graph<Vertex, Edge> network;
    }*/
    
    public static Graph<Vertex, Edge> readSpecificLines(Graph<Vertex, Edge> network, List<Headers> headers, Boolean filterYear, Boolean filterSex, Boolean filterGrade, Boolean filterRegion, Boolean filterSchool) throws IOException
    {
        File file = new File("C:\\A11.csv"); 
        List<String> lines = Files.readAllLines(file.toPath(), StandardCharsets.UTF_8); 
        
        if(filterYear == true || filterSex == true || filterRegion == true || filterGrade == true ||filterSchool == true)
        {
            network = getOnlyRelevantRecords(network, headers, lines, filterYear, filterSex,filterGrade, filterRegion, filterSchool);
        }
        int a =0;
        /*for (String line : lines) 
        { 
            if(a>10)
            {
                break;
            }

            String[] array = line.split(","); 
            System.out.println(array[0]+" "+array[array.length-1]); 
            a++;
        }*/
        return network;
    }
    
    public static Graph<Vertex, Edge> getOnlyRelevantRecords(Graph<Vertex, Edge> network, List<Headers> headers, List<String> lines, Boolean filterYear, Boolean filterSex, Boolean filterGrade, Boolean filterRegion, Boolean filterSchool) throws FileNotFoundException
    {
        List<String> finalLines = new ArrayList<>();
        lines.remove(0); //remove header
        int a = 0;
        for(String line : lines)
        {
            if(a>10)
            {
                break;
            }
            
            String [] array = line.split(",");
           
            //filtering rows
            if(filterYear == true)
            {
                //System.out.println(array[3]);
                if(Integer.parseInt(array[3])==2)
                {
                    finalLines.add(line);
                    //System.out.println("Chlapci su nasledujuci "+array[3]);
                }
            }
            a++;
        }
        
        //filtering columns
        int id = 0;
        List<ChosenRecords> chosenRecords = new ArrayList();
        for(String line : finalLines)
        {
            ChosenRecords cr = new ChosenRecords(id);
            
            String [] array = line.split(",");
            int counter = 0;
            for(Headers h : headers)
            {
                System.out.println("Hlavicka "+h.getHeaderName()+" ma id "+h.getId()+" "+array[h.getId()]);
                if(counter ==0)
                {
                    cr.attributesValues = array[h.getId()];
                }
                else
                {
                    cr.attributesValues = cr.attributesValues+ ","+array[h.getId()];
                }
                counter++;
            }
           
            chosenRecords.add(cr);
            id++;
        }
        
        EpsilonNeighbourhoodGraph eng = new EpsilonNeighbourhoodGraph();
        network = eng.readCSV(chosenRecords);
        
        return network;
    }
    
    
    
}
