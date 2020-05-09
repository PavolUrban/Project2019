/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DataPreparation;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author pavol
 */
public class ChosenRecords {
    
    int id;
    ArrayList<Double> attributesValues;
    String className;
    List<Integer> emptyPropertiesIndexes;
    
    
    String testClassName;
    
    public ChosenRecords(int id)
    {
        this.id = id;
        this.attributesValues = new ArrayList();
        this.emptyPropertiesIndexes = new ArrayList<>();
    }
 
    
    public String getClassName()
    {
        return this.className;
    }
    
    public ArrayList<Double> getAttributesValuesAsList()
    {
        return this.attributesValues;
    }
    
//    public String getAttributesValues()
//    {
//        return this.attributesValues;
//    }

    public int getRecordId()
    {
        return this.id;
    }
}
