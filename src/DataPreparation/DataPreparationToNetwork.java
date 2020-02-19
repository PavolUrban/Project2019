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
import NetworkCreatingAlgorithms.NetworkByTopEdges;
import UserSettings.ToMove;
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
 
    public static Graph<Vertex, Edge> readSpecificLines(String path, Graph<Vertex, Edge> network,Boolean normalized,int methodOfNetworkCreation, List<Headers> headers, Boolean filterYear, int year, Boolean filterSex,int sex, Boolean filterGrade, int grade, Boolean filterRegion, int region, Boolean filterSchool, int school, double Epsilon, String distanceMethod) throws IOException
    {   
        File file = new File(path);  //"C:\\A11.csv"
        List<String> lines = Files.readAllLines(file.toPath(), StandardCharsets.UTF_8); 
        lines.remove(0); //remove header
        
        //TODO
        if(filterYear == true || filterSex == true || filterRegion == true || filterGrade == true ||filterSchool == true)
        {
            //TODO remove records with no filter value in column to prevent NULLPointerException
            network = getOnlyRelevantRows(methodOfNetworkCreation,network,normalized, headers, lines, filterYear, year, filterSex, sex, filterGrade,grade, filterRegion, region, filterSchool, school, Epsilon, distanceMethod);
        }
        else
        {
            if(methodOfNetworkCreation == 0 )
            {
                EpsilonNeighbourhoodGraph eng = new EpsilonNeighbourhoodGraph();
                network = eng.createNetwork(getOnlyRelevantColumns(headers, lines, distanceMethod),Epsilon, distanceMethod, normalized); 
            }
            else
            {
                KNearestNeighbor knn = new KNearestNeighbor();
                network = knn.createKNNNetwork(getOnlyRelevantColumns(headers, lines, distanceMethod), (int)Epsilon, distanceMethod);
               
            }
            
        }
        
        return network;
    }
    
    public static boolean checkMatchingFilter(String attribute, int filterValue)
    {
        boolean recordMatchAllFilters = true;
        if(attribute.equalsIgnoreCase("") || attribute==null || attribute.isEmpty()) 
        {
            //System.out.println(" Warning! Empty property. Tento musi byt zmazany");
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
    
    public static Graph<Vertex, Edge> getOnlyRelevantRows(int methodOfNetworkCreation, Graph<Vertex, Edge> network,Boolean normalized, List<Headers> headers, List<String> lines, Boolean filterYear, int year, Boolean filterSex,int sex, Boolean filterGrade,int grade, Boolean filterRegion,int region, Boolean filterSchool, int school, double Epsilon, String distanceMethod) throws FileNotFoundException
    {
        System.out.println("Method creation je "+methodOfNetworkCreation);
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
        List<ChosenRecords> chosenRecords = getOnlyRelevantColumns(headers, finalLines, distanceMethod);
        
        
        
         if(methodOfNetworkCreation == 0 )
         {
                EpsilonNeighbourhoodGraph eng = new EpsilonNeighbourhoodGraph();
                System.out.println("Creating network with epsilon = "+distanceMethod);
                network = eng.createNetwork(chosenRecords, Epsilon, distanceMethod, normalized);
         }
        else
        {
            System.out.println("Budem vytvarat siet s KNNN " +Epsilon);
            KNearestNeighbor knn = new KNearestNeighbor();
            network = knn.createKNNNetwork(chosenRecords, (int)Epsilon, distanceMethod);
            
             NetworkByTopEdges nbt = new NetworkByTopEdges(chosenRecords, distanceMethod, 10.0);
        }
        
      
        
        return network;
    }
    
    public static List<ChosenRecords> getOnlyRelevantColumns(List<Headers> headers, List<String> lines, String distanceMethod)
    {
        ToMove.headers = headers;
        int id = 0;
        List<ChosenRecords> chosenRecords = new ArrayList();
        
        for(String line : lines)
        {
            if(id>200)
            {
                break;
  }
           
            Boolean hasEmptyProperty = false;
            String [] array = line.split(","); //TODO split
            
            //
            ChosenRecords cr = new ChosenRecords(Integer.parseInt(array[0])); //array[0] -> cislo dotaznika sluzi ako id TODO opravit ked su ine data
           
            int counter = 0;
            for(Headers h : headers)
            {
                    
                System.out.println(id+".Hlavicka "+h.getHeaderName()+" ma id "+h.getId()+" "+array[h.getId()]);
                
                if(counter ==0)
                {
                    if(array[h.getId()].equalsIgnoreCase("") || array[h.getId()]==null || array[h.getId()].isEmpty()) 
                    {
                        //System.out.println("Empty property");
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
               
                chosenRecords.add(cr);
            }
            
            id++;
        }
        
        return chosenRecords;
    }
    
    
    
}
