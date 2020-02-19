/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DistancesMethods;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 *
 * @author pavol
 */
public class Distances {
  
    //Note: Default method is euclidean 
    
  //Pearson  
//     distance = Distances.countPearsonCorrelationCoefficient(values1,values2, values1.stream().mapToDouble(val -> val).average().orElse(0.0), values2.stream().mapToDouble(val -> val).average().orElse(0.0));
//                            
//                            distance = 1 - Math.abs(distance);
//                            System.out.println("Pearson hodnota "+distance);
// 
//   
    public static double countDistance(String distanceMethod,ArrayList<Double> values1, ArrayList<Double> values2)
    {
        double distance;
        
      
        if(distanceMethod.equalsIgnoreCase("Euklidovská") )
           distance =  countEuclideanDistance(values1, values2);
        
        else if(distanceMethod.equalsIgnoreCase("Čebyševova"))
             distance = countChebyshevDistance(values1, values2);
        
        else if(distanceMethod.equalsIgnoreCase("Manhattan"))
            distance = countManhattanDistance(values1, values2);
        
        
//        else if(distanceMethod.equalsIgnoreCase("Pearsonuv korelační koeficient"))
//        {
//            values1.stream().mapToDouble(val -> val).average().orElse(0.0), values2.stream().mapToDouble(val -> val).average().orElse(0.0)
//        }
//            distance = countPearsonCorrelationCoefficient(values1, values2, distance, distance)
//        
        else //default
            distance = countEuclideanDistance(values1, values2);
        
        return distance;
    }
    
    
//    public static double countKernellTrick(double value)
//    {
//        System.out.println("Kernell trick e value is "+Math.exp(-value));
//        return Math.exp(-value);
//    }
//    
    public static double countEuclideanDistance( ArrayList<Double> values1,  ArrayList<Double> values2)
    {
        
       // System.out.println("Euklid");
        double sum = 0.0;
        for (int x=0; x<values1.size(); x++)
        {
            
            sum += Math.pow(values1.get(x)-values2.get(x),2);
        }

        double result = Math.sqrt(sum);
        
        return result;
    }
    
    public static double countManhattanDistance(ArrayList<Double> values1,  ArrayList<Double> values2)
    {
        //System.out.println("Manhattan");
        double sum = 0.0;
        for (int x=0; x<values1.size(); x++)//nezacinam 0 aby sa ignoroval prvy riadok - premysliet
        {
            
            sum += Math.abs(values1.get(x)- values2.get(x));
        }
        
        return sum;
    }
    
    public static double countChebyshevDistance(ArrayList<Double> values1,  ArrayList<Double> values2)
    {
        //System.out.println("Chebyshev");
        List<Double> distancesBetweenCoordinates = new ArrayList<>();
        
        for (int x=0; x<values1.size(); x++)//nezacinam 0 aby sa ignoroval prvy riadok - premysliet
        {
            double distance = Math.abs(values1.get(x)- values2.get(x));
            distancesBetweenCoordinates.add(distance);
        }
        
        return Collections.max(distancesBetweenCoordinates);
    }
    
    public static double countPearsonCorrelationCoefficient(ArrayList<Double> values1,  ArrayList<Double> values2, double meanValues1, double meanValues2)
    {
        // Double average = values1.stream().mapToInt(val -> val).average().orElse(0.0);
        
        double sumUpside = 0.0;
        double sumDownsideValues1 = 0.0;
        double sumDownsideValues2 = 0.0;
        
        for(int i=0; i< values1.size(); i++)
        {
            sumUpside += ( (values1.get(i)-meanValues1) * (values2.get(i)-meanValues2));
            
            sumDownsideValues1 += Math.pow(values1.get(i)-meanValues1, 2);
            sumDownsideValues2 += Math.pow(values2.get(i)-meanValues2, 2);
        }
        
        double finalResult = (sumUpside)/(Math.sqrt(sumDownsideValues1*sumDownsideValues2));
        
        return finalResult;
    }
    
    public static double countJaccard(ArrayList<Double> values1, ArrayList<Double> values2)
    {
        double numberOfEqualAttributes = 0.0;
        for(int i=0; i<values1.size();i++)
        {
            //System.out.println("Porovnavame "+values1.get(i)+ " s "+values2.get(i));
            if(Objects.equals(values1.get(i), values2.get(i)))
                numberOfEqualAttributes++;
                
        }
        
      
        return numberOfEqualAttributes/values1.size();
    }
}
