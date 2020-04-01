/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package NetworkComponents;

import GUI.Design;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.TreeMap;

/**
 *
 * @author pavol
 */
public class Vertex {
    private final int id;
    private final String label;
    private double positionX;
    private double positionY;
    private double size = 10;
    private Boolean isVisited;
    
    private ArrayList<Double> valuesOfProps;
    public TreeMap<Integer, Double> neighoursIdsAndDistances;
    
    //use for colorize vertices in clusters
    public int clusterId = 0;
    
    //props for LRNet
    private int localSignificance = 0;
    private int localDegree = 0;
    private double localRepresentativeness;
    private double KValue;
    private Map<Integer, Double> LRneighbours = new HashMap();
    private List<Double> allSimilarities = new ArrayList();
    
    private double revValue = -1;
    private String revChange = "";

    public Vertex(int id) {
        this(id, "");
        
    }
    public Vertex(int id, String label){
        this.id = id;
        this.label = label;
        this.neighoursIdsAndDistances = new TreeMap<>();
        Random r = new Random();
        this.positionX = r.nextInt(Design.canvasWidth-30); //este upravit
        this.positionY = r.nextInt(Design.canvasHeight-30);
        this.isVisited =false;
    }
    
    public Vertex(int id, String label, double positionX, double positionY) {
        this.id = id;
        this.label = label;
        this.positionX = positionX;
        this.positionY = positionY;
    }

    public int getId() {
        return id;
    }
    
    public List<Double> getAllSimilarities()
    {
        return this.allSimilarities;
    }
    
    
    public int getLocalDegree()
    {
        return this.localDegree;
    }
    
    public Map<Integer, Double> getLRNeighbours()
    {
        return this.LRneighbours;
    }
    
    public double getLocalRepresentativenes()
    {
        return this.localRepresentativeness;
    }
    
    public int getLocalSignificance()
    {
        return this.localSignificance;
    }

    public String getLabel() {
        return label;
    }

    public double getPositionX() {
        return positionX;
    }

    public double getPositionY() {
        return positionY;
    }

    public double getSize() {
        return size;
    }

    public double getRevValue() {
        return revValue;
    }

    public String getRevChange() {
        return revChange;
    }

    public void setPositionX(double positionX) {
        this.positionX = positionX;
    }

    public void setPositionY(double positionY) {
        this.positionY = positionY;
    }

    public void setSize(double size) {
        this.size = size;
    }
    
    public void setKValue(double kValue)
    {
        this.KValue = kValue;
    }
    
    public void setLocalDegree(int localDegree)
    {
        this.localDegree = localDegree;
    }
    
    public void setLocalSignificance(int localSignificance)
    {
        this.localSignificance = localSignificance;
    }
    
    
    public void setAllSimilarities(List<Double> similarities)
    {
        this.allSimilarities = similarities;
    }
    
    
    public void setValuesOfProps(ArrayList<Double>values)
    {
        this.valuesOfProps = values;
    }

    @Override
    public String toString() {
        if("".equals(label))
            return "Vertex " + id;
        return "Vertex " + id + ": " + label;
    }

    public void setRevValue(double revValue) {
        this.revValue = revValue;
    }

    public void setRevChange(String revChange) {
        this.revChange = revChange;
    }
    
    public void setisVisited(Boolean visited)
    {
        this.isVisited = visited;
    }
    
    public void setLocalRepresentativenes(double localRepresentativenes)
    {
        this.localRepresentativeness = localRepresentativenes;
    }
    
    public void setLRNeighbours(Map<Integer, Double> neighbours)
    {
        this.LRneighbours = neighbours;
    }
    
    public Boolean getisVisited()
    {
        
        return this.isVisited;
    }
    
    
    public double getKValue()
    {
        return this.KValue;
    }
    
    public ArrayList<Double> getValuesOfProps()
    {
        return this.valuesOfProps;
    }
    
}
