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
    String attributesValues;
    
    public ChosenRecords(int id)
    {
        this.id = id;
        this.attributesValues ="";
    }
    
    public String getAttributesValues()
    {
        return this.attributesValues;
    }
    
    public void setAttributesValues(String attribute)
    {
        this.attributesValues += attribute;
    }
    
    public int getRecordId()
    {
        return this.id;
    }
}
