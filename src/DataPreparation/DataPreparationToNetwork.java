/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DataPreparation;

import NetworkComponents.Edge;
import NetworkComponents.Vertex;
import NetworkCreatingAlgorithms.EpsilonNeighbourhoodGraph;
import NetworkCreatingAlgorithms.KNearestNeighbor;
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
 
    public static Graph<Vertex, Edge> readSpecificLines(Graph<Vertex, Edge> network, List<Headers> headers, Boolean filterYear, int year, Boolean filterSex,int sex, Boolean filterGrade, int grade, Boolean filterRegion, int region, Boolean filterSchool, int school) throws IOException
    {   
        File file = new File("C:\\A11.csv"); 
        List<String> lines = Files.readAllLines(file.toPath(), StandardCharsets.UTF_8); 
        lines.remove(0); //remove header
        
        //TODO
        if(filterYear == true || filterSex == true || filterRegion == true || filterGrade == true ||filterSchool == true)
        {
            //TODO remove records with no filter value in column to prevent NULLPointerException
            network = getOnlyRelevantRows(network, headers, lines, filterYear, year, filterSex, sex, filterGrade,grade, filterRegion, region, filterSchool, school);
        }
        else
        {
            EpsilonNeighbourhoodGraph eng = new EpsilonNeighbourhoodGraph();
            network = eng.createNetwork(getOnlyRelevantColumns(headers, lines));
            /*KNearestNeighbor knn = new KNearestNeighbor();
            network = knn.createKNNNetwork(getOnlyRelevantColumns(headers, lines));*/
        }
        
        return network;
    }
    
    public static boolean checkMatchingFilter(String attribute, int filterValue)
    {
        boolean recordMatchAllFilters = true;
        if(attribute.equalsIgnoreCase("") || attribute==null || attribute.isEmpty()) 
        {
            System.out.println(" Warning! Empty property. Tento musi byt zmazany");
            recordMatchAllFilters = false;
        }
        else
        {
            if(Integer.parseInt(attribute)!= filterValue)
            {
                recordMatchAllFilters = false;
                // TOto bolo odkomentovane !! finalLines.add(line);
                System.out.println("Tento zaznam musim odstranit hodnota mala byt "+filterValue +" a je "+attribute);
            }
        }
        
        
        return recordMatchAllFilters;
    }
    
    public static Graph<Vertex, Edge> getOnlyRelevantRows(Graph<Vertex, Edge> network, List<Headers> headers, List<String> lines, Boolean filterYear, int year, Boolean filterSex,int sex, Boolean filterGrade,int grade, Boolean filterRegion,int region, Boolean filterSchool, int school) throws FileNotFoundException
    {
        List<String> finalLines = new ArrayList<>();
   
        System.out.println();
        System.out.println("Values of filters");
        System.out.println("Year "+year +", school "+ school +", grade "+ grade +", sex "+sex+", region"+region);
        
        int a = 0;
        for(String line : lines)
        {
            if(a>201)
            {
                break;
            }
            
            String [] array = line.split(",");
           
            System.out.println();
//            System.out.println("Record "+a+ " values: "+array[HeadersIndexes.indexOfRegionColumn]+", "+array[HeadersIndexes.indexOfSchoolColumn]+", "+array[HeadersIndexes.indexOfSexColumn]+", "+array[HeadersIndexes.indexOfGradeColumn]+", "+array[HeadersIndexes.indexOfYearColumn]);
            //filtering rows
            
            int numberOfDismatchingFilters = 0;
            if(filterSex == true)
            {
                if(!checkMatchingFilter(array[HeadersIndexes.indexOfSexColumn], sex))
                {
                    numberOfDismatchingFilters++;
                }
            }
            if(filterYear == true)
            {
                if(!checkMatchingFilter(array[HeadersIndexes.indexOfYearColumn], year))
                {
                    numberOfDismatchingFilters++;
                }
            }
            
            if(filterSchool == true)
            {
                if(!checkMatchingFilter(array[HeadersIndexes.indexOfSchoolColumn], school))
                {
                    numberOfDismatchingFilters++;
                }
            }
            
            if(filterGrade == true)
            {
                if(!checkMatchingFilter(array[HeadersIndexes.indexOfGradeColumn], grade))
                {
                    numberOfDismatchingFilters++;
                }
            }
            
            if(filterRegion == true)
            {
                if(!checkMatchingFilter(array[HeadersIndexes.indexOfRegionColumn], region))
                {
                    numberOfDismatchingFilters++;
                }
            }
            
            if(numberOfDismatchingFilters == 0)
            {
                 System.out.println("Zaver. Splna filtre. Vsetko OK");
                 finalLines.add(line);
            }
            
            else
            {
                System.out.println("Zaver. Nezhoduje sa s niektorym filtrom musi byt zmazany");
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
            if(id>200)
            {
                break;
            }
            ChosenRecords cr = new ChosenRecords(id);
            Boolean hasEmptyProperty = false;
            String [] array = line.split(",");
            int counter = 0;
            for(Headers h : headers)
            {
                //System.out.println("Hlavicka "+h.getHeaderName()+" ma id "+h.getId()+" "+array[h.getId()]);
                
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
                        //System.out.println("Empty property");
                        hasEmptyProperty = true;
                    }
                    cr.attributesValues = cr.attributesValues+ ","+array[h.getId()];
                }
                counter++;
            }
           
            if(!hasEmptyProperty)
            {
               // System.out.println("Pridavam uzol s id !"+id);
                chosenRecords.add(cr);
            }
            
            id++;
        }
        
        return chosenRecords;
    }
    
    
    
}
