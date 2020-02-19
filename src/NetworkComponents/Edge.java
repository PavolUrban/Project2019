/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package NetworkComponents;

/**
 *
 * @author pavol
 */
public class Edge implements Comparable<Edge>{
    private int id;
    private double weight;
    
    public Edge(int id){
        this.id = id;
    }
    
    public Edge(int id, double weight){
        this.id = id;
        this.weight = weight;
    }
    
    public int getId()
    {
        return this.id;
    }
    
    public double getWeight()
    {
        return this.weight;
    }

    @Override
    public int compareTo(Edge e) {
        return new Double(weight).compareTo( e.weight);
    }

}
