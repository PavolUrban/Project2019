/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GUI;

import UserSettings.UserSettings;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 *
 * @author pavol
 */
public class UserSettingsWindow {
 
    //todo overovat ci sa stali nejake zmeny a na zaklade toho vyhadzovat dialog
    
    Stage newWindow;
    
    String labelDistanceMethod = "Choose distance method";
    String [] distanceMethodsStrings = { "Chebyshev distance", "Euclidean distance", "Manhattan distance" };
    String defaultDistanceMethod = "Euclidean distance";
    
    String LabelEmptyRecord = "Choose empty records action";
    String [] emptyRecordsAction = {"Remove records","Use mean","Use median"};
    String defaultEmptyRecordAction = "Remove records";
    
    public HBox createHBoxOfOptions(String label, String [] optionItems)
    {
        final HBox HBoxOptions = new HBox();
        Text textDistanceMethod = new Text(label);
        HBoxOptions.setMinWidth(Design.minTableWidth);
        HBoxOptions.setSpacing(25);
        ChoiceBox<String> choiceBoxOptions = new ChoiceBox<String>(); 
        choiceBoxOptions.getItems().addAll(optionItems);
        
        if(label == LabelEmptyRecord)
        {
            choiceBoxOptions.getSelectionModel().select(UserSettings.emptyColumnsAction);
        }
        
        else if(label == labelDistanceMethod)
        {
            choiceBoxOptions.getSelectionModel().select(UserSettings.distanceMethod);
        }
        
        choiceBoxOptions.getSelectionModel().selectedItemProperty()
            .addListener(new ChangeListener<String>() {
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if(label == "Choose distance method")
                {
                    defaultDistanceMethod = newValue;
                    System.out.println("Zmenena metoda vzdialenosti na "+defaultDistanceMethod);
                }
                else if(label == "Choose empty records action")
                {
                    defaultEmptyRecordAction = newValue;
                     System.out.println("Zmenene spravanie pri prazdnych poliach na "+newValue);
                }
            }
        });
        
        HBoxOptions.getChildren().addAll(textDistanceMethod, choiceBoxOptions);
        HBoxOptions.setAlignment(Pos.CENTER);
        
        return HBoxOptions;
    }
    
    public void openUserSettingsWindow(Stage stage)
    {
        StackPane secondaryLayout = new StackPane();
        System.out.println("Defaulty mame "+ defaultDistanceMethod+ " "+defaultEmptyRecordAction);
        
        final HBox HBOXDistances = createHBoxOfOptions(labelDistanceMethod, distanceMethodsStrings);    
        final HBox HBOXEmptyRecordsActions = createHBoxOfOptions(LabelEmptyRecord, emptyRecordsAction);
      
        Button buttonSaveOptions = new Button ("Save");
        buttonSaveOptions.setOnAction((event) -> {
            newWindow.close();
            UserSettings.distanceMethod = defaultDistanceMethod;
            UserSettings.emptyColumnsAction = defaultEmptyRecordAction;
            AlertsWindows.optionsSavedDialog();
        });
        
        Button buttonCloseOptions = new Button ("Close");
        buttonCloseOptions.setOnAction((event) -> {
            //todo - nie je doriesene odpoved z dialogu
            AlertsWindows.areYouSureDialog();
        });
        
        final HBox HBoxButtons = new HBox();
        HBoxButtons.setMinWidth(Design.minTableWidth);
        HBoxButtons.setSpacing(25);
        HBoxButtons.getChildren().addAll(buttonCloseOptions, buttonSaveOptions);
        HBoxButtons.setAlignment(Pos.CENTER);
        
        
        final VBox vbox = new VBox();
        vbox.setSpacing(5);
        //vbox.setPadding(new Insets(30, 0, 0, Design.canvasWidth + 50)); //TODO dynamicky posledne bolo 930
        vbox.getChildren().addAll(HBOXDistances, HBOXEmptyRecordsActions, HBoxButtons);
        vbox.setAlignment(Pos.CENTER);
        
        secondaryLayout.getChildren().addAll(vbox);
        Scene secondScene = new Scene(secondaryLayout, 400, 400);

        // New window (Stage)
        newWindow = new Stage();
        newWindow.setTitle("Second Stage");
        newWindow.setScene(secondScene);

        // Set position of second window, related to primary window.
        newWindow.setX(stage.getX() + 200);
        newWindow.setY(stage.getY() + 100);

        newWindow.show();   
    }
}
