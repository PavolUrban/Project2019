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
import java.util.List;

/**
 *
 * @author pavol
 */
public class UserSettings {
    
    //default settings 
    public static String distanceMethod = "Euclidean distance";
    public static String emptyColumnsAction = "Remove records";
    
    public static Collection<Vertex> listOfSavedVertices;
    public static Graph<Vertex, Edge> savedNetwork = null;
    
    public static double maxSliderValue = 0.0;
    
    //property used if user wants use non-numeric attribute for network creation
    public static boolean hasNonNumericProperty = false;
    public static List<String> nonNumericPropertiesNames = new ArrayList();
    
    //properties for datasoruce reading
    public static String separator = ",";
    public static String pathToDataset = "C:\\A11.csv";
    public static boolean hasHeader = true;
    
    
    //max-similarities for LRNET
    public static List<Double> maxSimilarities = new ArrayList();
    
}
