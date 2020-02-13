package DataPreparation;

import DataPreparation.ChosenRecords;
import DataPreparation.Headers;
import NetworkComponents.Edge;
import NetworkComponents.Vertex;
import NetworkCreatingAlgorithms.EpsilonNeighbourhoodGraph;
import NetworkCreatingAlgorithms.KNearestNeighbor;
import UserSettings.ToMove;
import edu.uci.ics.jung.graph.Graph;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author pavol
 */
public class NEWPreprocessing {
    
    public static List<ChosenRecords> getRelevantRecords(String path, List<Headers> headers, Boolean hasFilters) throws IOException
    {   
        File file = new File(path);  //"C:\\A11.csv"
        List<String> lines = Files.readAllLines(file.toPath(), StandardCharsets.UTF_8); 
        
        lines.remove(0); //TODO ask about header to remove header - by default it should be removed
        
        
//        if(!hasFilters) TODO
            getOnlyRelevantColumns(headers, lines);
   

        
        
        return null;
    }
    
    
    
    public static List<ChosenRecords> getOnlyRelevantColumns(List<Headers> headers, List<String> lines)
    {
       
        int id = 0;
        
        List<ChosenRecords> chosenRecords = new ArrayList();
        
        for(String line : lines)
        {
            Boolean hasEmptyProperty = false;
            String [] propertiesValues = line.split(";"); //TODO oddelovac ,/;
        
            ChosenRecords cr = new ChosenRecords(id); //TODO bud id stlpec alebo generovane id
           
            int counter = 0;
            for(Headers h : headers)
            {
                if(counter ==0)
                {
                    hasEmptyProperty = checkIfSomeOfPropertiesIsEmpty(propertiesValues, h);
                    cr.attributesValues = propertiesValues[h.getId()];
                }
                else
                {
                    hasEmptyProperty = checkIfSomeOfPropertiesIsEmpty(propertiesValues, h);
                    cr.attributesValues = cr.attributesValues+ ","+propertiesValues[h.getId()];
                }
                counter++;
            }
           
            if(!hasEmptyProperty)
            {
                chosenRecords.add(cr); //todo check other possibilities when hasEmptyProperty is True
                System.out.println(cr.getAttributesValues());
            }
            
            id++;
        }
        
        return chosenRecords;
    }

    
    private static Boolean checkIfSomeOfPropertiesIsEmpty(String [] propertiesValues, Headers h)
    {
        Boolean hasEmptyProperty = false;
        
        if(propertiesValues[h.getId()].equalsIgnoreCase("") || propertiesValues[h.getId()]==null || propertiesValues[h.getId()].isEmpty()) 
            hasEmptyProperty = true;   
          
        return hasEmptyProperty;
    }

    
}
