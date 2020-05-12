/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package UserSettings;

import NetworkComponents.Edge;
import NetworkComponents.Vertex;
import edu.uci.ics.jung.graph.Graph;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author pavol
 */
public class UserSettings {
    
    //default settings 
    public static String distanceMethod = "Euclidean distance";
    public static String emptyColumnsAction = "Remove records";
   
    //attributes for vertices colorization
    public static String colorizeByAttribute = "Žiadne";
    public static int colorizeByIndex = -1;
    public static Map<String, Integer> mapAliasingForColorization = new HashMap<>();
    
    
    public static Collection<Vertex> listOfSavedVertices;
    public static Graph<Vertex, Edge> savedNetwork = null;
    
    public static double maxSliderValue = 5.0;
    
    //property used if user wants use non-numeric attribute for network creation
    public static boolean hasNonNumericProperty = false;
    public static List<String> nonNumericPropertiesNames = new ArrayList();
    
    //properties for datasoruce reading
    public static String separator = ",";
    public static String pathToDataset = "C:\\A11.csv";
    public static boolean hasHeader = true;
    
    
    public static boolean attributesBoxWasInitialized = false;
    
    //max-similarities for LRNET
    public static List<Double> maxSimilarities = new ArrayList();
    
    public static int minDegree = 1000;
    public static int maxDegree = -1;
    
    
    public static double sampleSize = 0.15;
}
