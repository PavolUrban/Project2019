/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DataPreparation;

import NetworkComponents.Edge;
import NetworkComponents.Vertex;
import NetworkCreatingAlgorithms.EpsilonKNNCombinated;
import NetworkCreatingAlgorithms.EpsilonNeighbourhoodGraph;
import NetworkCreatingAlgorithms.EpsilonNew;
import NetworkCreatingAlgorithms.KNearestNeighbor;
import NetworkCreatingAlgorithms.NetworkByTopEdges;
import UserSettings.ToMove;
import UserSettings.UserSettings;
import edu.uci.ics.jung.graph.Graph;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author pavol
 */
public class DataPreparationToNetwork {
    
    
    //normalization related
    public static List<Double> listMaxValuesForNormalization = new ArrayList();
    public static List<Double> listMinValuesForNormalization = new ArrayList();
 
    public static Graph<Vertex, Edge> readSpecificLines(Graph<Vertex, Edge> network,Boolean normalized,int methodOfNetworkCreation, List<Headers> headers, Boolean filterYear, int year, Boolean filterSex,int sex, Boolean filterGrade, int grade, Boolean filterRegion, int region, Boolean filterSchool, int school, double Epsilon, String distanceMethod, String kValue, String topEdgesValue) throws IOException
    {   
        File file = new File(UserSettings.pathToDataset); 
        List<String> lines = Files.readAllLines(file.toPath(), StandardCharsets.UTF_8); 
        lines.remove(0); //remove header
        
        //TODO
        if(filterYear == true || filterSex == true || filterRegion == true || filterGrade == true ||filterSchool == true)
        {
            network = getOnlyRelevantRows(methodOfNetworkCreation,network,normalized, headers, lines, filterYear, year, filterSex, sex, filterGrade,grade, filterRegion, region, filterSchool, school, Epsilon, distanceMethod);
        }
        else
        {
            if(methodOfNetworkCreation == 0 )
            {
                //Todo vyriesit normalizaciu
                EpsilonNew en = new EpsilonNew();
                network = en.createEpsilonNetwork(getOnlyRelevantColumns(headers, lines, distanceMethod, normalized), distanceMethod, Epsilon, true);
//                System.out.println("Creating epsilon");
//                EpsilonNeighbourhoodGraph eng = new EpsilonNeighbourhoodGraph();
//                network = eng.createNetwork(getOnlyRelevantColumns(headers, lines, distanceMethod),Epsilon, distanceMethod, normalized); 
            }
            else if(methodOfNetworkCreation == 1)
            {
                KNearestNeighbor knn = new KNearestNeighbor();
                network = knn.createKNNNetwork(getOnlyRelevantColumns(headers, lines, distanceMethod, normalized), Integer.parseInt(kValue), distanceMethod);
                
                //System.out.println("Kcko mame na "+ kValue);
            }
            
            else if(methodOfNetworkCreation == 2)
            {
                NetworkByTopEdges nbt = new NetworkByTopEdges();
                network = nbt.createNetworkByTopEdges(getOnlyRelevantColumns(headers, lines, distanceMethod, normalized), distanceMethod, Double.parseDouble(topEdgesValue));
                
                //System.out.println("Top edges mame na "+ topEdgesValue);
            }
            
            else if(methodOfNetworkCreation == 3)
            {
                EpsilonKNNCombinated eknn = new EpsilonKNNCombinated();
                network = eknn.createNetwork(getOnlyRelevantColumns(headers, lines, distanceMethod, normalized),distanceMethod, Epsilon, Integer.parseInt(kValue));
                
                //System.out.println("combinated mame na epsilon " +Epsilon+" ,KNN: "+ kValue);
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
            if(a>12)
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
        List<ChosenRecords> chosenRecords = getOnlyRelevantColumns(headers, finalLines, distanceMethod, normalized);
        
        
        
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
            
            
        }
        
      
        
        return network;
    }
    
    
    
    public static List<ChosenRecords> getOnlyRelevantColumns(List<Headers> headers, List<String> lines, String distanceMethod, boolean normalization)
    {
        ToMove.headers = headers;
        List<ChosenRecords> chosenRecords = new ArrayList();
        int id = 0;
        
        for(String line : lines)
        {
            //todo remove this cycle
            if(id>151)
            {
                break;
            }
           
            boolean hasEmptyProperty = false;

            String [] array = line.split(UserSettings.separator); 
           
            ChosenRecords cr = null;
             
            
            if(UserSettings.pathToDataset.equalsIgnoreCase("C:\\A11.csv")) //TODO tu tie cesty osetrit nejak rozumnejsie, umoznit mat id stlpec
                cr = new ChosenRecords(Integer.parseInt(array[0])); //array[0] -> cislo dotaznika sluzi ako id TODO opravit ked su ine data
           
            else
                cr = new ChosenRecords(id);
                
            for(Headers h : headers)
            {
                if(array[h.getId()].equalsIgnoreCase("") || array[h.getId()]==null || array[h.getId()].isEmpty()) 
                {
                    hasEmptyProperty = true;
                    break;
                }           
                
                else
                {
                  String localeValue = array[h.getId()].replaceAll(",",".");
                  cr.attributesValues.add(Double.parseDouble(localeValue));
                }
            }
           
            //add only records with complete set of list - todo add median or something like that
            if(!hasEmptyProperty)
            {
                chosenRecords.add(cr);
            }
            
            id++;
        }
        
        
        if(normalization)
        {
            prepareMinAndMaxForNormalization(chosenRecords);
            doNormalization(chosenRecords);
            System.out.println("Normalization done");
        }
        
        return chosenRecords;
    }
    
    
    public static void doNormalization(List<ChosenRecords> chosenRecords)
    {    
        for(ChosenRecords singleRecord : chosenRecords)
        {       
            ArrayList<Double> currentValues = singleRecord.getAttributesValuesAsList();
    
            for(int i = 0; i < currentValues.size(); i++)
            {
                
                double min = listMinValuesForNormalization.get(i);
                double max = listMaxValuesForNormalization.get(i);
                
                double normalizedValue = ( currentValues.get(i) - min ) / ( max - min); 
 
                currentValues.set(i, normalizedValue);              
            }        
            
            singleRecord.attributesValues = currentValues;
        }
        
        
    }
    
    
    public static void prepareMinAndMaxForNormalization(List<ChosenRecords> chosenRecords)
    {
        //default initalization
        List<Double> minValues = new ArrayList<>(chosenRecords.get(0).getAttributesValuesAsList());
        List<Double> maxValues = new ArrayList<>(chosenRecords.get(0).getAttributesValuesAsList());
       
        for(ChosenRecords singleRecord : chosenRecords)
        {           
            List<Double> currentValues = singleRecord.getAttributesValuesAsList();
  
            for(int i = 0; i < currentValues.size(); i++)
            {
                double currentValue = currentValues.get(i);
                
                if( currentValue < minValues.get(i) )
                    minValues.set(i, currentValue);
              
                else if( currentValue > maxValues.get(i))
                    maxValues.set(i, currentValue);
            }
        }
        
        listMaxValuesForNormalization = maxValues;
        listMinValuesForNormalization = minValues;
    }
    
   
    
}
