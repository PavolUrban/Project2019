/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DataPreparation;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

/**
 *
 * @author pavol
 */
public class SeparateDataIntoRelatedSections {
    
    
    public static List<String> getAllPosibleValuesForFilter(int indexOfFilter) throws FileNotFoundException
    {
        List<String> valuesForFilter = new ArrayList();
        String[] tempArray;
        Scanner scanner = new Scanner(new File("C:\\A11.csv"));
        scanner.useDelimiter("\n");
        
        while(scanner.hasNext())
        {
            String record = scanner.next();
            tempArray = record.split(",");
            if(!valuesForFilter.contains(tempArray[indexOfFilter]))
            {
                if(tempArray[indexOfFilter].equalsIgnoreCase("") || tempArray[indexOfFilter]==null || tempArray[indexOfFilter].isEmpty())
                {
                 //empty
                }
                else
                {
                     valuesForFilter.add(tempArray[indexOfFilter]);
                }
            }                 
        }
 
        scanner.close();
        
        
        valuesForFilter.remove(0);
       /* for(String value : valuesForFilter)
        {
            System.out.println("Najdena hodnota "+value);
        }*/
        Collections.sort(valuesForFilter);
        
        return valuesForFilter;
    }
    
    
    public static ArrayList<Headers> readFile(int begin, int end) throws FileNotFoundException
    {
        ArrayList<Headers> namesForSelectHeaders = new ArrayList<>();
        String[] tempArray;
        Scanner scanner = new Scanner(new File("C:\\A11.csv"));
        scanner.useDelimiter("\n");
        int counter=0;
        while(scanner.hasNext())
        {
           if(counter>0)
           {
               break;
           }
          
          
           String record = scanner.next();
           tempArray = record.split(",");
           for(int i=0;i<tempArray.length; i++)
           {
               if(i>begin && i<end)
               {
                   Headers h = new Headers(tempArray[i], i);
                   namesForSelectHeaders.add(h);
               } 
           }
           // namesForSelectHeaders.add(removeSignsFromName(scanner.next()));
           
           //System.out.print(scanner.next()+"|");
           counter++;
        }
 
        scanner.close();
        
        return namesForSelectHeaders;
    }
    
    
    private static String removeSignsFromName(String name) 
    {
        for (char ch: name.toCharArray()) 
        {
        }
        return name.replaceAll("\\d","");
    }
}
