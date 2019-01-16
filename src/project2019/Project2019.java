/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package project2019;

import DataPreparation.DataPreparationToNetwork;
import DataPreparation.Headers;
import DataPreparation.SeparateDataIntoRelatedSections;
import GUI.Design;
import GUI.UserSettingsWindow;
import NetworkComponents.Edge;
import NetworkComponents.Vertex;
import UserSettings.UserSettings;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.util.Pair;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 *
 * @author pavol
 */
public class Project2019 extends Application {
    
     private Graph<Vertex, Edge> network;
     Canvas canvas1 = new Canvas(Design.canvasWidth, Design.canvasHeight);
     Stage copyOfPrimaryStage;
     ObservableList<SelectedAtributes> data = FXCollections.observableArrayList();
     List<File> files = new ArrayList();
     
     ArrayList<Headers> allHeaders;
     List<Headers> selectedHeaders = new ArrayList();
     
     
     List<String> selectedAttributes = new ArrayList<>();
     private final TableView<SelectedAtributes> table = new TableView<>();

     //filters
     Boolean filterByYear = false;
     Boolean filterBySex = false;
     Boolean filterByGrade = false;
     Boolean filterByRegion = false;
     Boolean filterBySchool = false;
     
     
     
     private void drawEdge(GraphicsContext gc, double fromX, double fromY, double toX, double toY, double edgeWidth, Color color) {
        gc.setLineWidth(edgeWidth);
        gc.setStroke(color);
        gc.strokeLine(fromX, fromY, toX, toY);
    }

    private void drawVertex(GraphicsContext gc, double positionX, double positionY, double size, Color color) {
        double leftX = positionX - size / 2;
        double leftY = positionY - size / 2;
        double sizeLine = size * 0.05;
        gc.setFill(color);
        gc.fillOval(leftX, leftY, size, size);
        gc.setStroke(Color.BLACK);
        gc.setLineWidth(sizeLine);
        gc.strokeOval(leftX, leftY, size, size);
    }

    public void drawVertex(GraphicsContext gc, Vertex v, Color color) {
        drawVertex(gc, v.getPositionX(), v.getPositionY(), v.getSize(), color);
    }int variableToDelete =0;
     
     
      private TableView createTable(TableView table, int id) {

        table.setMaxHeight(Design.maxTableHeight);
        table.setMinWidth(Design.minTableWidth);

        TableColumn lastNameCol = new TableColumn("Selected attributes");
        lastNameCol.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        
        
        TableColumn<SelectedAtributes, SelectedAtributes> unfriendCol = new TableColumn<>("Delete file");
        unfriendCol.setCellValueFactory(
                param -> new ReadOnlyObjectWrapper<>(param.getValue())
        );
        unfriendCol.setCellFactory(param -> new TableCell<SelectedAtributes, SelectedAtributes>() {
            private final Button deleteButton = new Button("Remove");
            

            @Override
            protected void updateItem(SelectedAtributes person, boolean empty) {
                super.updateItem(person, empty);

                if (person == null) {
                    setGraphic(null);
                    return;
                }

                setGraphic(deleteButton);

                deleteButton.setOnAction((event) -> {

                    String fileToDelete = person.firstName.getValue();

                    getTableView().getItems().remove(person);
                    if (id == 1) {
                        //TODO Delete from data and files

                        data.remove(person);

                        Iterator<File> iter = files.iterator();

                        while (iter.hasNext()) {
                            File str = iter.next();

                            if (str.getName().equals(person.firstName.getValue())) {
                                iter.remove();
                            }
                        }

                    } else {

                    }
                });
            }
        });

        if (id == 1) {
            table.setItems(data);
        } else {
            // table.setItems(data2);
        }

        table.getColumns().addAll(lastNameCol, unfriendCol);

        lastNameCol.setMinWidth(Design.minTableColumnWidth);
        unfriendCol.setMinWidth(Design.minTableColumnWidth);

        return table;
    }
     
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
        
        allHeaders = SeparateDataIntoRelatedSections.readFile(readFromIndex,readToIndex); //todo nefunguje ked mam viac selectboxov
        for(Headers s : allHeaders)
        {
            System.out.println(s.getHeaderName());
            locationchoiceBox.getItems().add(s.getHeaderName());
        } 
        
        Button b = new Button("Add!");
        b.setOnAction((event) -> {
             SelectedAtributes myFile = new SelectedAtributes(locationchoiceBox.getSelectionModel().getSelectedItem().toString());
                    data.add(myFile);
            System.out.println();
            //System.out.println("Added "+locationchoiceBox.getSelectionModel().getSelectedItem().toString());
            
            selectedAttributes.add(locationchoiceBox.getSelectionModel().getSelectedItem().toString());
            System.out.println();
            for(Headers actualHeader : allHeaders)
            {
                System.out.println("Porovnavam "+actualHeader.getHeaderName() +" s "+locationchoiceBox.getSelectionModel().getSelectedItem().toString());
                if(locationchoiceBox.getSelectionModel().getSelectedItem().toString() == actualHeader.getHeaderName())
                {
                    System.out.println("Pridal som hlavicku s menom "+actualHeader.getHeaderName()+" a id je "+actualHeader.getId());
                    selectedHeaders.add(actualHeader);
                }
            }
                });

        HBOXChoices.getChildren().addAll(selectProperty, locationchoiceBox, b);
        
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
        //final HBox HBOXPhysicalActivities = createInterestsFieldsHBox("Physical activities", 20, 30);
      
      
        Button buttonCreateNetwork =  new Button("Create network!");
        buttonCreateNetwork.setOnAction((event) -> {
            try {
                network = DataPreparationToNetwork.readSpecificLines(network,selectedHeaders, filterByYear, filterBySex, filterByGrade, filterByRegion, filterBySchool);
                System.out.println("Network hotova "+network.getEdgeCount()+" tolko hran, a tolko uzlov "+network.getVertexCount());
                 GraphicsContext gc = canvas1.getGraphicsContext2D();
                     gc.setFill(Color.WHITE);
                     gc.fillRect(0, 0, Design.canvasWidth, Design.canvasHeight);
                     
                        for (Edge e : network.getEdges()) {
                   
                            Pair<Vertex> p = network.getEndpoints(e);
                            Vertex v = p.getFirst();
                            Vertex v2 = p.getSecond();
                            drawEdge(gc, v.getPositionX(), v.getPositionY(), v2.getPositionX(), v2.getPositionY(), 0.1, Color.BLACK);
                }
                     
                     
                    for (Vertex v : network.getVertices()) {
                    drawVertex(gc, v, Color.CORAL); //skontrolovat ci je spravne

                }
                
            } catch (IOException ex) {
                Logger.getLogger(Project2019.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        //right side of screen
        final VBox vbox = new VBox();
        vbox.setSpacing(5);
        vbox.setPadding(new Insets(30, 0, 0, Design.canvasWidth + 50)); //TODO dynamicky posledne bolo 930
        vbox.getChildren().addAll(HBOXfilterByYear, HBOXfilterBySex,HBoxfilterByGrade, HBoxfilterByRegion, HBoxfilterBySchool, HBOXEatingHabits, createTable(table, 1), buttonCreateNetwork);//HBOxPhysical
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
    
       public static class SelectedAtributes {

        private final SimpleStringProperty firstName;

        private SelectedAtributes(String fileName) {
            this.firstName = new SimpleStringProperty(fileName);

        }

        public String getFirstName() {
            return firstName.get();
        }

        public void setFirstName(String fName) {
            firstName.set(fName);
        }

    }
    
}
