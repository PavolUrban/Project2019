/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DataPreparation;

import GUI.Design;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.CheckBox;
import javafx.scene.control.CustomMenuItem;
import javafx.scene.control.MenuButton;
import javafx.scene.layout.HBox;
import project2019.Project2019;

/**
 *
 * @author pavol
 */
public class PrepareDifferentDataSource {
 
    
    public static HBox start(String path) throws FileNotFoundException
    {
        ArrayList<Headers> headers = getHeaders(path);
        
        return createHBox(headers);
    }
    
    public static ArrayList<Headers> getHeaders(String path) throws FileNotFoundException
    {
        ArrayList<Headers> namesForSelectHeaders = new ArrayList<>();
        String[] tempArray;
        Scanner scanner = new Scanner(new File(path));
        
        scanner.useDelimiter("\n");
        int counter=0;
        while(scanner.hasNext())
        {
           if(counter>0) //to read only header
               break;
          
          
           String record = scanner.next();
           
          
           tempArray = record.split(";"); //TODO
         
           for(int i=0;i<tempArray.length; i++)
           {
                Headers h = new Headers(tempArray[i], i);
                namesForSelectHeaders.add(h); 
           }
         
           counter++;
        }
 
        scanner.close();
        
        return namesForSelectHeaders;
    }
    
    
    
    
    
    private static HBox createHBox(List<Headers> allHeaders) throws FileNotFoundException
    {
        final HBox HBOXChoices = new HBox();
        HBOXChoices.setMinWidth(Design.minTableWidth);
        HBOXChoices.setSpacing(25);
 
        
        MenuButton menuButton = new MenuButton("Atributy");   
        List<CustomMenuItem> items = new ArrayList();
        List<CheckBox> checkboxes = new ArrayList();

        CheckBox selectAll = new CheckBox("Označit vše");  
        selectAll.selectedProperty().addListener(new ChangeListener<Boolean>() {
            public void changed(ObservableValue ov,Boolean old_val, Boolean new_val) 
            {       
                for(CheckBox cbo : checkboxes)
                {
                    cbo.setSelected(new_val);
                }

                if(new_val)
                {
                    selectAll.setText("Odznačit vše");
                }
                else if(!new_val)
                {
                     selectAll.setText("Označit vše");
                }               
            }
        });
        
        CustomMenuItem itemSelectAll = new CustomMenuItem(selectAll);
        itemSelectAll.setHideOnClick(false); 

        items.add(itemSelectAll);
        
        for(Headers header : allHeaders)
        {
            CheckBox cb0 = new CheckBox(header.getHeaderName());  
            cb0.selectedProperty().addListener(new ChangeListener<Boolean>() {
                public void changed(ObservableValue ov,Boolean old_val, Boolean new_val) 
                {
                   
                }
            });
        
            CustomMenuItem item0 = new CustomMenuItem(cb0);
            item0.setHideOnClick(false); 
            items.add(item0);
            checkboxes.add(cb0);
 
        }
          
        menuButton.getItems().setAll(items);
        
        HBOXChoices.getChildren().addAll(menuButton);
        
        return HBOXChoices;

    }
    
    
}
