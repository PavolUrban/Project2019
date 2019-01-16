/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DataPreparation;

/**
 *
 * @author pavol
 */
public class Headers {
 
    String headerName;
    int id;
    
    public Headers(String headerName, int id)
    {
        this.headerName = headerName;
        this.id = id;
    }
    
    public String getHeaderName()
    {
        return this.headerName;
    }
    
    public int getId()
    {
        return this.id;
    }
}
