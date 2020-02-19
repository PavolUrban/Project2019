/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GUI;

import DataPreparation.Headers;
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
public class BoxesRelatedSections {
    
    
    
    public static Map<Integer, List<Headers>> something(String path, String splitCharacter) throws FileNotFoundException
    {
        Map<Integer, List<Headers>> sectionsWithHeaders = new HashMap<>();
        
        //eating habbits - todo indexes
        sectionsWithHeaders.put(1, readFile(path, splitCharacter, 6, 20));
        
        //others
        sectionsWithHeaders.put(2, readFile(path, splitCharacter, 21, 30));
        
        for(Map.Entry<Integer, List<Headers>> singleSection : sectionsWithHeaders.entrySet())
        {
            System.out.println("Key "+ singleSection.getKey());
            for(int i =0 ; i<singleSection.getValue().size(); i++)
            {
                System.out.println("\t"+singleSection.getValue().get(i).getId() + " - "+ singleSection.getValue().get(i).getHeaderName());
            }
        }
        
        return sectionsWithHeaders;
    }
    
    
    
    public static List<Headers> readFile(String path, String splitCharacter, int begin, int end) throws FileNotFoundException
    {
        List<Headers> namesForSelectHeaders = new ArrayList<>();
        
        String[] headerNames;
        Scanner scanner = new Scanner(new File(path));
        scanner.useDelimiter("\n");
  
        int counter=0;
  
        while(scanner.hasNext())
        {
           if(counter>0) //read only first line - headers
               break;
          
          
           String headerLine = scanner.next();
           headerNames = headerLine.split(splitCharacter);
           
           for(int i=0;i<headerNames.length; i++)
           {
               if(i>begin && i<end)
               {
                   Headers h = new Headers(headerNames[i], i);
                   namesForSelectHeaders.add(h);
               } 
           }
      
           counter++;
        }
 
        scanner.close();
        
        return namesForSelectHeaders;
    }
    
}