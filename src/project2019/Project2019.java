/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package project2019;

import DataPreparation.SeparateDataIntoRelatedSections;
import GUI.Design;
import GUI.UserSettingsWindow;
import UserSettings.UserSettings;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.chart.NumberAxis;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Slider;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Popup;
import javafx.stage.Stage;

/**
 *
 * @author pavol
 */
public class Project2019 extends Application {
    
     Canvas canvas1 = new Canvas(Design.canvasWidth, Design.canvasHeight);
     Stage copyOfPrimaryStage;
     //filters
     Boolean filterByYear = false;
     Boolean filterBySex = false;
     Boolean filterByGrade = false;
     Boolean filterByRegion = false;
     Boolean filterBySchool = false;
     
     private MenuBar createMainMenu() {
        MenuBar mainMenu = new MenuBar();
        mainMenu.setMinSize(Design.sceneWidth, 10);
        
        Menu menuCentralities = new Menu("Options");
        MenuItem userSettings = new MenuItem("Settings");
        userSettings.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent t) {
              UserSettingsWindow u = new UserSettingsWindow();
              u.openUserSettingsWindow(copyOfPrimaryStage);
            }
        });

        menuCentralities.getItems().add(userSettings);
   
        Menu menu2 = new Menu("File");
        MenuItem exitItem = new MenuItem("Exit", null);
        exitItem.setMnemonicParsing(true);
        exitItem.setAccelerator(new KeyCodeCombination(KeyCode.X, KeyCombination.CONTROL_DOWN));
        exitItem.setOnAction((event) -> {
            Platform.exit();
        });

        menu2.getItems().add(exitItem);

        mainMenu.getMenus().addAll( menu2,menuCentralities);

        return mainMenu;
    }
     
     public HBox createHbox(String label)
     { 
        final ToggleGroup group = new ToggleGroup();
        RadioButton buttonFilterYes = new RadioButton("Yes");
        buttonFilterYes.setToggleGroup(group);
        buttonFilterYes.setUserData("Yes");

        RadioButton buttonFilterNo = new RadioButton("No");
        buttonFilterNo.setToggleGroup(group);
        buttonFilterNo.setUserData("No"); 
        buttonFilterNo.setSelected(true);
        
        final HBox HBOXfilterBySex = new HBox();
        Text labelFilterBySex = new Text(label); 
        HBOXfilterBySex.setMinWidth(Design.minTableWidth);
        HBOXfilterBySex.setSpacing(25);

        HBOXfilterBySex.getChildren().addAll(labelFilterBySex,buttonFilterYes, buttonFilterNo);
          
        group.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
            public void changed(ObservableValue<? extends Toggle> ov,
                    Toggle old_toggle, Toggle new_toggle) {
                if (group.getSelectedToggle() != null) {
                    if (group.getSelectedToggle() == buttonFilterYes) {
                        
                        System.out.println("Distance: "+ UserSettings.distanceMethod+", empty: "+UserSettings.emptyColumnsAction);
                        if(label=="Filter by sex")
                        {
                            filterBySex = true;
                            System.out.println("Filter by Sex changed to "+filterBySex);
                        }
                        else if(label=="Filter by year")
                        {
                            filterByYear = true;
                            System.out.println("Filter by Year changed to "+filterByYear);
                        }
                        else if(label == "Filter by region")
                        {
                            filterByRegion = true;
                            System.out.println("Filter by Region changed to "+filterByRegion);
                        }
                        
                        else if(label == "Filter by school")
                        {
                            filterBySchool = true;
                            System.out.println("Filter by School changed to "+filterBySchool);
                        }
                        
                        else if(label == "Filter by grade")
                        {
                            filterByGrade = true;
                            System.out.println("Filter by Grade changed to "+filterByGrade);
                        }
                    }
                    
                else if (group.getSelectedToggle() == buttonFilterNo) {
                        if(label=="Filter by sex")
                        {
                            filterBySex = false;
                             System.out.println("Filter by Sex changed to "+filterBySex);
                        }
                        else if(label=="Filter by year")
                        {
                            filterByYear = false;
                            System.out.println("Filter by Year changed to "+filterByYear);
                        }
                        
                        else if(label == "Filter by region")
                        {
                            filterByRegion = false;
                            System.out.println("Filter by Region changed to "+filterByRegion);
                        }
                        
                        else if(label == "Filter by school")
                        {
                            filterBySchool = false;
                            System.out.println("Filter by School changed to "+filterBySchool);
                        }
                        
                        else if(label == "Filter by grade")
                        {
                            filterByGrade = false;
                            System.out.println("Filter by Grade changed to "+filterByGrade);
                        }
                    }
                }

            }
        });
        
        return HBOXfilterBySex;
     }
    
     public HBox createInterestsFieldsHBox(String label, int readFromIndex, int readToIndex) throws FileNotFoundException
     {
        final HBox HBOXChoices = new HBox();
        Text selectProperty = new Text(label); 
        HBOXChoices.setMinWidth(Design.minTableWidth);
        HBOXChoices.setSpacing(25);
         //Choice box for location 
        ChoiceBox locationchoiceBox = new ChoiceBox(); 
        
        ArrayList<String> ar = SeparateDataIntoRelatedSections.readFile(readFromIndex,readToIndex);
        for(String s : ar)
        {
            System.out.println(s);
            locationchoiceBox.getItems().add(s);
        } 

        HBOXChoices.getChildren().addAll(selectProperty, locationchoiceBox);
        
        return HBOXChoices;
     }
    
    @Override
    public void start(Stage primaryStage) throws FileNotFoundException {
        copyOfPrimaryStage = primaryStage;
        Group root = new Group();
        Scene scene = new Scene(root, Design.sceneWidth, Design.sceneHeight/*Color.CADETBLUE*/);
       
        primaryStage.setTitle("Tool for network creation");
        primaryStage.setWidth(Design.sceneWidth);
        primaryStage.setHeight(Design.sceneHeight);
  
        //filters
        final HBox HBOXfilterByYear = createHbox("Filter by year");
        final HBox HBOXfilterBySex = createHbox("Filter by sex ");
        final HBox HBoxfilterByRegion = createHbox("Filter by region");
        final HBox HBoxfilterBySchool = createHbox("Filter by school");
        final HBox HBoxfilterByGrade = createHbox("Filter by grade");
        
        
        //fieldsOfInterests
        final HBox HBOXEatingHabits = createInterestsFieldsHBox("Eating habits", 6, 20);
        final HBox HBOXPhysicalActivities = createInterestsFieldsHBox("Physical activities", 20, 30);
      
      
       
       
        //right side of screen
        final VBox vbox = new VBox();
        vbox.setSpacing(5);
        vbox.setPadding(new Insets(30, 0, 0, Design.canvasWidth + 50)); //TODO dynamicky posledne bolo 930
        vbox.getChildren().addAll(HBOXfilterByYear, HBOXfilterBySex,HBoxfilterByGrade, HBoxfilterByRegion, HBoxfilterBySchool, HBOXEatingHabits, HBOXPhysicalActivities);
        root.getChildren().addAll(vbox);
        
        //canvas
        GraphicsContext gc = canvas1.getGraphicsContext2D();
        gc.setFill(Color.CORNSILK);
        gc.setStroke(Color.BLUE);
        gc.fillRect(0, 0, Design.canvasWidth, Design.canvasHeight);

        //left side of screen
        final VBox vboxCanvas = new VBox();
        vboxCanvas.setSpacing(5);
        vboxCanvas.setPadding(new Insets(75, 0, 0, 10)); //TODO dynamicky
        vboxCanvas.getChildren().addAll(canvas1);
       
        
        root.getChildren().add(vboxCanvas);
        root.getChildren().add(createMainMenu());
        //root.getChildren().add(createMainMenu());
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}
