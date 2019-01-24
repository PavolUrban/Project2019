/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DistancesMethods;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 *
 * @author pavol
 */
public class Distances {
  
    public double countEuclideanDistance( ArrayList<Double> values1,  ArrayList<Double> values2)
    {
        double sum = 0.0;
        for (int x=0; x<values1.size(); x++)//nezacinam 0 aby sa ignoroval prvy riadok - premysliet
        {
            
            sum += Math.pow(values1.get(x)-values2.get(x),2);
        }

        double result = Math.sqrt(sum);
        
        return result;
    }
    
    public double countManhattanDistance(ArrayList<Double> values1,  ArrayList<Double> values2)
    {
        double sum = 0.0;
        for (int x=0; x<values1.size(); x++)//nezacinam 0 aby sa ignoroval prvy riadok - premysliet
        {
            
            sum += Math.abs(values1.get(x)- values2.get(x));
        }
        
        return sum;
    }
    
    public double countChebyshevDistance(ArrayList<Double> values1,  ArrayList<Double> values2)
    {
        List<Double> distancesBetweenCoordinates = new ArrayList<>();
        
        for (int x=0; x<values1.size(); x++)//nezacinam 0 aby sa ignoroval prvy riadok - premysliet
        {
            double distance = Math.abs(values1.get(x)- values2.get(x));
            distancesBetweenCoordinates.add(distance);
        }
        
        return Collections.max(distancesBetweenCoordinates);
    }
}
