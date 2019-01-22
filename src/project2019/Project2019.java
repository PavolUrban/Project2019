/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package project2019;

import DataPreparation.DataPreparationToNetwork;
import DataPreparation.Headers;
import DataPreparation.HeadersIndexes;
import DataPreparation.SeparateDataIntoRelatedSections;
import GUI.AlertsWindows;
import GUI.Design;
import GUI.NetworkDrawer;
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
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.CustomMenuItem;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.GridPane;
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
    
    ArrayList<Headers> allHeaders;
    List<Headers> selectedHeaders = new ArrayList();
    
    private final TableView<SelectedAtributes> table = new TableView<>();
    ObservableList<SelectedAtributes> data = FXCollections.observableArrayList();
    List<File> files = new ArrayList();

    //filters active-inactive
    Boolean filterByGrade = false;
    Boolean filterByRegion = false;
    Boolean filterBySex = false;
    Boolean filterBySchool = false;
    Boolean filterByYear = false;
    
    //filters values
    int grade = 0;
    int region = 0;
    int sex = 0;
    int school = 0;
    int year = 0;
     
    private HBox somethin(String label, int readFromIndex, int readToIndex) throws FileNotFoundException
    {
        final HBox HBOXChoices = new HBox();
        HBOXChoices.setMinWidth(Design.minTableWidth);
        HBOXChoices.setSpacing(25);
        
        allHeaders = SeparateDataIntoRelatedSections.readFile(readFromIndex,readToIndex);
        
        MenuButton menuButton = new MenuButton(label);   
        List<CustomMenuItem> items = new ArrayList();
        List<CheckBox> checkboxes = new ArrayList();

        CheckBox selectAll = new CheckBox("Select all");  
        selectAll.selectedProperty().addListener(new ChangeListener<Boolean>() {
            public void changed(ObservableValue ov,Boolean old_val, Boolean new_val) 
            {       
                for(CheckBox cbo : checkboxes)
                {
                    cbo.setSelected(new_val);
                }

                if(new_val)
                {
                    selectAll.setText("Unselect all");
                }
                else if(!new_val)
                {
                     selectAll.setText("Select all");
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
                    if(new_val)
                    {
                        SelectedAtributes myFile = new SelectedAtributes(header.getHeaderName());
                        data.add(myFile);
                        System.out.println("Changed "+header.getHeaderName()+" to "+new_val);
                        
                        for(Headers actualHeader : allHeaders)
                        {
                            //System.out.println("Porovnavam "+actualHeader.getHeaderName() +" s "+locationchoiceBox.getSelectionModel().getSelectedItem().toString());
                            if(header.getHeaderName() == actualHeader.getHeaderName())
                            {
                                System.out.println("Pridal som hlavicku s menom "+actualHeader.getHeaderName()+" a id je "+actualHeader.getId());
                                selectedHeaders.add(actualHeader);
                            }
                        }
                    }
                    
                    else
                    {
                        data.clear();
                        selectedHeaders.remove(header);
                        for(Headers s : selectedHeaders)
                        {
                            SelectedAtributes sa  = new SelectedAtributes((s.getHeaderName()));
                            data.add(sa);
                        }
                    }                   
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
  
    //TODO upravit tabulku, odstranit nezmysly
    private TableView createTable(TableView table, int id) {

      table.setMaxHeight(Design.maxTableHeight);
      table.setMinWidth(Design.minTableWidth);

      TableColumn lastNameCol = new TableColumn("Selected attributes");
      lastNameCol.setCellValueFactory(new PropertyValueFactory<>("firstName"));


      TableColumn<SelectedAtributes, SelectedAtributes> unfriendCol = new TableColumn<>("Remove attribute");
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
     
     private ChoiceBox createChoiceBox(String label, List<String> choices)
     {
        ChoiceBox choiceBox = new ChoiceBox(); 
        choiceBox.getItems().addAll(choices);
        choiceBox.getSelectionModel().select(0);
        choiceBox.setDisable(true);
        
        choiceBox.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() 
        {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number number, Number number2) 
            {
                if(label=="Filter by sex")
                {
                    sex = Integer.parseInt(choiceBox.getItems().get((Integer) number2).toString());
                    System.out.println("Sex changed to "+sex);
                }
                else if(label=="Filter by year")
                {
                    year = Integer.parseInt(choiceBox.getItems().get((Integer) number2).toString());
                    System.out.println("Year changed to "+ year);
                }
                else if(label == "Filter by region")
                {
                    region = Integer.parseInt(choiceBox.getItems().get((Integer) number2).toString());
                    System.out.println("Region changed to "+ region);
                }

                else if(label == "Filter by school")
                {
                    school = Integer.parseInt(choiceBox.getItems().get((Integer) number2).toString());
                    System.out.println("School changed to "+ school);
                }

                else if(label == "Filter by grade")
                {
                    grade =Integer.parseInt(choiceBox.getItems().get((Integer) number2).toString());
                    System.out.println("Grade changed to "+ grade);
                }
             
           }
        });
     
        return choiceBox;
     }
     
     public HBox createHbox(String label, ChoiceBox locationchoiceBox)
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

        HBOXfilterBySex.getChildren().addAll(labelFilterBySex,buttonFilterYes, buttonFilterNo, locationchoiceBox); //todo add group?
          
        group.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
            public void changed(ObservableValue<? extends Toggle> ov,
                    Toggle old_toggle, Toggle new_toggle) {
                if (group.getSelectedToggle() != null) {
                    if (group.getSelectedToggle() == buttonFilterYes) {
                        
                        locationchoiceBox.setDisable(false);
               
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
                    locationchoiceBox.setDisable(true);
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

    @Override
    public void start(Stage primaryStage) throws FileNotFoundException {
        copyOfPrimaryStage = primaryStage;
        Group root = new Group();
        Scene scene = new Scene(root, Design.sceneWidth, Design.sceneHeight/*Color.CADETBLUE*/);
       
        primaryStage.setTitle("Tool for network creation");
        primaryStage.setWidth(Design.sceneWidth);
        primaryStage.setHeight(Design.sceneHeight);
  
        //filters
        List<String> filterValues = SeparateDataIntoRelatedSections.getAllPosibleValuesForFilter(HeadersIndexes.indexOfYearColumn);   
        final ChoiceBox choicesYears = createChoiceBox("Filter by year", filterValues);
        year = Integer.parseInt(filterValues.get(0));
        final HBox HBOXfilterByYear = createHbox("Filter by year", choicesYears);
        
        filterValues = SeparateDataIntoRelatedSections.getAllPosibleValuesForFilter(HeadersIndexes.indexOfSexColumn);
        final ChoiceBox choicessexes = createChoiceBox("Filter by sex", filterValues);
        sex = Integer.parseInt(filterValues.get(0));
        final HBox HBOXfilterBySex = createHbox("Filter by sex",choicessexes);
        
        filterValues = SeparateDataIntoRelatedSections.getAllPosibleValuesForFilter(HeadersIndexes.indexOfRegionColumn);
        final ChoiceBox choicesRegions = createChoiceBox("Filter by region", filterValues);
        region = Integer.parseInt(filterValues.get(0));
        final HBox HBoxfilterByRegion = createHbox("Filter by region", choicesRegions);
        
        filterValues = SeparateDataIntoRelatedSections.getAllPosibleValuesForFilter(HeadersIndexes.indexOfSchoolColumn);
        final ChoiceBox choicesSchools = createChoiceBox("Filter by school", filterValues);
        school = Integer.parseInt(filterValues.get(0));
        final HBox HBoxfilterBySchool = createHbox("Filter by school", choicesSchools);
       
        filterValues = SeparateDataIntoRelatedSections.getAllPosibleValuesForFilter(HeadersIndexes.indexOfGradeColumn);
        final ChoiceBox choicesGrades = createChoiceBox("Filter by grade", filterValues);
        grade = Integer.parseInt(filterValues.get(0));
        final HBox HBoxfilterByGrade = createHbox("Filter by grade", choicesGrades);
        
        TitledPane gridTitlePane = new TitledPane();
        GridPane grid = new GridPane();
        grid.setVgap(4);
        grid.setPadding(new Insets(5, 5, 5, 5));
        grid.add(HBOXfilterBySex, 0, 0);
        grid.add(HBOXfilterByYear, 0, 1);
        grid.add(HBoxfilterByGrade, 0, 2);
        grid.add(HBoxfilterByRegion, 0, 3);
        grid.add(HBoxfilterBySchool, 0, 4);        
        gridTitlePane.setText("Filters");
        gridTitlePane.setContent(grid);
        gridTitlePane.setExpanded(false);
        
        //related sections
        final HBox mb =  somethin("Eating habits", 6,20);
   
        Button buttonCreateNetwork =  new Button("Create network!");
        buttonCreateNetwork.setOnAction((event) -> {
            try {
                if(data.isEmpty())
                {
                    AlertsWindows.displayAlert("No data were selected to create a network.");
                    System.out.println("ALEERT");
                }
                else
                {
                    network = DataPreparationToNetwork.readSpecificLines(network,selectedHeaders, filterByYear, filterBySex,sex, filterByGrade, filterByRegion, filterBySchool);
                    System.out.println("Network hotova "+network.getEdgeCount()+" tolko hran, a tolko uzlov "+network.getVertexCount());
                    NetworkDrawer.drawNetwork(network, canvas1);
                }
            } catch (IOException ex) {
                Logger.getLogger(Project2019.class.getName()).log(Level.SEVERE, null, ex);
            }
        });

        //right side of screen
        final VBox vbox = new VBox();
        vbox.setSpacing(5);
        vbox.setPadding(new Insets(30, 0, 0, Design.canvasWidth + 50)); //TODO dynamicky posledne bolo 930
        vbox.getChildren().addAll(gridTitlePane, mb, createTable(table, 1), buttonCreateNetwork);//HBOxPhysical
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
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
    public static class SelectedAtributes 
    {
        private final SimpleStringProperty firstName;

        private SelectedAtributes(String fileName) 
        {
            this.firstName = new SimpleStringProperty(fileName);
        }

        public String getFirstName() 
        {
            return firstName.get();
        }

        public void setFirstName(String fName) 
        {
            firstName.set(fName);
        }
    }
    
}
    /*
     probably useless?
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
    */
