/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DataPreparation;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 *
 * @author pavol
 */
public class SeparateDataIntoRelatedSections {
    
    public static ArrayList<String> readFile(int begin, int end) throws FileNotFoundException
    {
        ArrayList<String> namesForSelectHeaders = new ArrayList<>();
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
                   namesForSelectHeaders.add(tempArray[i]);
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
