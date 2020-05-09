/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GUI;

import DataPreparation.Headers;
import DataPreparation.NEWPreprocessing;
import DataPreparation.PrepareDifferentDataSource;
import UserSettings.UserSettings;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;
import project2019.Project2019.SelectedAtributes;

/**
 *
 * @author pavol
 */
public class CustomDataSourceSettingsView {
    
    static Stage newWindow;
    
    static String pathToFile;
    Label labelFilePath = new Label("Vybraný soubor");
    static Label labelFilePathValue = new Label("Nevybráno!");
    Label labelSeparator = new Label("Separátor");

    static Button buttonAddFile = new Button ("Vybrat datový soubor");

    
    
    public static void chooseFile() throws FileNotFoundException, IOException 
    {        
        FileChooser fileChooser = new FileChooser();
        File selectedFile = fileChooser.showOpenDialog(newWindow);
        
        labelFilePathValue.setText(selectedFile.getPath());
        buttonAddFile.setText("Změnit datový soubor");   
    }
    
    public static void modifyMainPage(VBox vbox, List<Headers> selectedHeaders,  ObservableList<SelectedAtributes> data)
    {
        selectedHeaders.clear();
        data.clear();
        ArrayList<Headers> headers = new ArrayList();
        
        try {
            headers = PrepareDifferentDataSource.getHeaders(UserSettings.pathToDataset);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(CustomDataSourceSettingsView.class.getName()).log(Level.SEVERE, null, ex);
        }  
    }
    
   
    
}