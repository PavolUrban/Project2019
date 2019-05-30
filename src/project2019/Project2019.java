/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package project2019;

import DataPreparation.DataPreparationToNetwork;
import DataPreparation.Exports;
import DataPreparation.Headers;
import DataPreparation.HeadersIndexes;
import DataPreparation.SeparateDataIntoRelatedSections;
import GUI.AlertsWindows;
import GUI.Design;
import GUI.NetworkDrawer;
import GUI.UserSettingsWindow;
import Layouts.Layout;
import Layouts.LayoutCircular;
import Layouts.LayoutFruchtermann;
import Layouts.LayoutISOM;
import Layouts.LayoutKK;
import NetworkComponents.Edge;
import NetworkComponents.Vertex;
import UserSettings.UserSettings;
import edu.uci.ics.jung.graph.Graph;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
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
import javafx.scene.control.Separator;
import javafx.scene.control.Slider;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
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
    
    
    int maxK = 7;
    int minK = 1;
    int currentK =2;
    

    //filters active-inactive
    Boolean filterByGrade = false;
    Boolean filterByRegion = false;
    Boolean filterBySex = false;
    Boolean filterBySchool = false;
    Boolean filterByYear = false;
    
    String filterByGradeText = "Ročník";
    String filterByRegionText = "Kraj";
    String filterBySexText = "Pohlaví";
    String filterBySchoolText = "Škola";
    String filterByYearText = "Rok narození";
    
    Boolean attributesWasChanged = true;
    
    //filters values
    int grade = 0;
    int region = 0;
    int sex = 0;
    int school = 0;
    int year = 0;
    
    Label labelForSlider = new Label("Hodnota ε:");
    ChoiceBox choicesNetworkMethodCreation = new ChoiceBox(FXCollections.observableArrayList(
    "ε-okolí ", "KNN")
    );
    
     ChoiceBox choicesMetrics = new ChoiceBox(FXCollections.observableArrayList(
    "Euklidovská", "Čebyševova","Manhattan","Pearsonuv korelační koeficient")
    );
     
     ChoiceBox choicesEmptyRecords = new ChoiceBox(FXCollections.observableArrayList(
        "Doplnit medián","Doplnit průměr","Smazat záznam")
    );
     
    CheckBox checkboxNormalization = new CheckBox(); 
    CheckBox checkboxSaveLayout = new CheckBox();
    
    HBox box = new HBox( 5.0, new Label("Normalizovat data"), checkboxNormalization, new Label("Zachovat rozmístnění uzlů"), checkboxSaveLayout);
   
    
    Boolean saveLayout = true;
    final Separator separator = new Separator();
    final Separator separatorTest = new Separator();
    Label numberOfVertices = new Label("Uzly: ");
    Label numberOfEdges = new Label("Hrany: ");
    Label chosenLayout = new Label ("Layout: KK Layout");
    Slider slider = new Slider(1, 10, 2);
   
    String currentLayout = "KK";
    double defaultSliderValue = 2.0;
    Label sliderValue = new Label(""+defaultSliderValue);
    
    Boolean layoutWasChosen=false;
   
    Label labelNetworkCreationMethod = new Label("Metoda vytvoření sítě");
    Label labelDistanceMethod = new Label("Metrika");
    Label labelEmptyRecordsAction = new Label("Prázdné záznamy");
    Boolean networkWasCreated = false;
    
    
    Boolean onlyEpsilonWasChanged = false;
    Boolean dataWasChanged = false;
    
    private HBox somethin(String label, int readFromIndex, int readToIndex) throws FileNotFoundException
    {
        final HBox HBOXChoices = new HBox();
        HBOXChoices.setMinWidth(Design.minTableWidth);
        HBOXChoices.setSpacing(25);
        
        allHeaders = SeparateDataIntoRelatedSections.readFile(readFromIndex,readToIndex);
        
        MenuButton menuButton = new MenuButton(label);   
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
                    dataWasChanged = true;
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
                    
                     System.out.println("Bude sa nieco diat?? data maju size" + data.size()+" "+choicesNetworkMethodCreation.getSelectionModel().getSelectedIndex());
                     if(choicesNetworkMethodCreation.getSelectionModel().getSelectedIndex()==0)
                     {
                         System.out.println("teraz by sa mal nastavovat slider podla velkosti dat");
                         setSliderDefault(slider, checkboxNormalization.isSelected());
                     }
                }
            });
        
            CustomMenuItem item0 = new CustomMenuItem(cb0);
            item0.setHideOnClick(false); 
            items.add(item0);
            checkboxes.add(cb0);
            dataWasChanged = true;
        }
          
        menuButton.getItems().setAll(items);
        
        HBOXChoices.getChildren().addAll(menuButton);
        
        return HBOXChoices;
    }
  
    //TODO upravit tabulku, odstranit nezmysly
    private TableView createTable(TableView table, int id) {

      table.setMaxHeight(Design.maxTableHeight);
      table.setMinWidth(Design.minTableWidth);
      
      TableColumn lastNameCol = new TableColumn("Vybrané atributy");
      lastNameCol.setCellValueFactory(new PropertyValueFactory<>("firstName"));



      if (id == 1) {
          table.setItems(data);
      } else {
          // table.setItems(data2);
      }

      table.getColumns().addAll(lastNameCol);

      lastNameCol.setMinWidth(654);

      return table;
  }
    
    private void runLayoutOrDisplayWarning(String layoutType)
    {
      
        if(network !=null)
        {
            if(layoutType=="KK")
            {
                runLayout(new LayoutKK());
            }
            
            else if(layoutType == "ISOM")
            {
                runLayout(new LayoutISOM());
            }
            
            else if(layoutType == "Circular")
            {
                runLayout(new LayoutCircular());
            }
            
            else if(layoutType == "FR")
            {
                runLayout(new LayoutFruchtermann());
            }
        }
        else 
        {
            AlertsWindows.displayAlert("Pro aplikování layoutu je nutné nejdříve vytvořit síť.");
        }
    }
     
     private MenuBar createMainMenu() {
        MenuBar mainMenu = new MenuBar();
        mainMenu.setMinSize(Design.sceneWidth, 10);
        
       /* Menu menuCentralities = new Menu("Možnosti");
        MenuItem userSettings = new MenuItem("Nastavení");
        userSettings.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent t) {
              UserSettingsWindow u = new UserSettingsWindow();
              u.openUserSettingsWindow(copyOfPrimaryStage);
            }
        });

        menuCentralities.getItems().add(userSettings);*/
   
        Menu menu2 = new Menu("Soubor");
        MenuItem exitItem = new MenuItem("Exit", null);
        exitItem.setMnemonicParsing(true);
        exitItem.setAccelerator(new KeyCodeCombination(KeyCode.X, KeyCombination.CONTROL_DOWN));
        exitItem.setOnAction((event) -> {
            Platform.exit();
        });

        MenuItem saveAsNetwork = new MenuItem("Save network", null);
        saveAsNetwork.setMnemonicParsing(true);
        saveAsNetwork.setOnAction((event) -> {
            Exports.saveNetworkAsConnectedVerticesList(copyOfPrimaryStage, network);
        });
        
        MenuItem saveAsChosenRecords = new MenuItem("Save selected...", null);
        saveAsChosenRecords.setMnemonicParsing(true);
        saveAsChosenRecords.setOnAction((event) -> {
            Exports.saveNetworkAsConnectedVerticesList(copyOfPrimaryStage);
        });
        
        menu2.getItems().addAll(exitItem, saveAsNetwork, saveAsChosenRecords);
        
        Menu menuLayouts = new Menu("Layouty");
        MenuItem itemISOMLayout = new MenuItem("ISOM Layout");
        itemISOMLayout.setOnAction((event) -> {
            chosenLayout.setText("Layout: ISOM Layout");
            currentLayout = "ISOM";
            runLayoutOrDisplayWarning("ISOM");
        });

        MenuItem itemCircularLayout = new MenuItem("Circular Layout");
        itemCircularLayout.setOnAction((event) -> {
            chosenLayout.setText("Layout: Circular Layout");
            currentLayout = "Circular";
            runLayoutOrDisplayWarning("Circular");
        });

        MenuItem itemKKLayout = new MenuItem("KK Layout");
        itemKKLayout.setOnAction((event) -> {
            chosenLayout.setText("Layout: KK Layout");
            currentLayout = "KK";
            runLayoutOrDisplayWarning("KK");
        });
        
        MenuItem itemFRLayout = new MenuItem("FR Layout");
        itemFRLayout.setOnAction((event) -> {
            chosenLayout.setText("Layout: FR Layout");
            currentLayout = "FR";
            runLayoutOrDisplayWarning("FR");
        });

        menuLayouts.getItems().addAll(itemCircularLayout, itemISOMLayout, itemKKLayout, itemFRLayout);

        
        mainMenu.getMenus().addAll( menu2, menuLayouts); //menuCentralities

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
                if(label==filterBySexText)
                {
                    sex = Integer.parseInt(choiceBox.getItems().get((Integer) number2).toString());
                    System.out.println("Sex changed to "+sex);
                }
                else if(label==filterByYearText)
                {
                    year = Integer.parseInt(choiceBox.getItems().get((Integer) number2).toString());
                    System.out.println("Year changed to "+ year);
                }
                else if(label == filterByRegionText)
                {
                    region = Integer.parseInt(choiceBox.getItems().get((Integer) number2).toString());
                    System.out.println("Region changed to "+ region);
                }

                else if(label == filterBySchoolText)
                {
                    school = Integer.parseInt(choiceBox.getItems().get((Integer) number2).toString());
                    System.out.println("School changed to "+ school);
                }

                else if(label == filterByGradeText)
                {
                    grade =Integer.parseInt(choiceBox.getItems().get((Integer) number2).toString());
                    System.out.println("Grade changed to "+ grade);
                }
             
           }
        });
     
        return choiceBox;
     }
     
     
     
     public void runLayout(final Layout layout) {
        if (network == null) {
            return;
        }

        Task task = new Task() {
            @Override
            protected Object call() throws Exception {
                
                GraphicsContext gc = canvas1.getGraphicsContext2D();
                gc.setFill(Color.WHITE);
                gc.setStroke(Color.BLACK);
                gc.fillRect(0, 0, Design.canvasWidth, Design.canvasHeight);

                gc.strokeLine(0, 0, Design.canvasWidth, 0); // UP -> in general start x,y, end x, y
                gc.strokeLine(0, 0, 0, Design.canvasHeight); // LEFT
                gc.strokeLine(Design.canvasWidth,Design.canvasHeight, 0,Design.canvasHeight); // DOWN
                gc.strokeLine(Design.canvasWidth, 0, Design.canvasWidth, Design.canvasHeight); 
                gc.stroke();

                
                System.out.println("Stav networkWasCreated je "+networkWasCreated);
                if(networkWasCreated)
                {
                    if(checkboxSaveLayout.isSelected() == true)
                    {
                           Platform.runLater(new Runnable() {
                        @Override public void run() {
                        System.out.println("Run later is called and save layout is true");
                         NetworkDrawer.redrawNetwork(network, canvas1);
                        } 
                        });
                    }
                    
                    else
                    {
                           Platform.runLater(new Runnable() {
                        @Override public void run() {
                        System.out.println("Run later is called but save layout is false");
                            layout.runLayout(network);
                            layout.fitToScrean(network);
                            NetworkDrawer.drawNetwork(network, canvas1);
                            networkWasCreated = true;
                            UserSettings.savedNetwork = network;
                        } 
                        });
                    }
                  
               }
                else
                {
                    layout.runLayout(network);
                    layout.fitToScrean(network);
                    NetworkDrawer.drawNetwork(network, canvas1);
                    networkWasCreated = true;
                    UserSettings.savedNetwork = network;
                }
                    
                   
                
                
                return null;
            }
        };
        beginTask(task);

    }

    public void beginTask(Task task) {
        Thread tr = new Thread(task);
        tr.setDaemon(true);
        tr.start();

    }
     
     public HBox createHbox(String label, ChoiceBox locationchoiceBox)
     { 
        final ToggleGroup group = new ToggleGroup();
        RadioButton buttonFilterYes = new RadioButton("Ano");
        buttonFilterYes.setToggleGroup(group);
        buttonFilterYes.setUserData("Ano");

        RadioButton buttonFilterNo = new RadioButton("Ne");
        buttonFilterNo.setToggleGroup(group);
        buttonFilterNo.setUserData("Ne"); 
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
               
                        if(label==filterBySexText)
                        {
                            filterBySex = true;
                            System.out.println("Filter by Sex changed to "+filterBySex);
                        }
                        else if(label==filterByYearText)
                        {
                            filterByYear = true;
                            System.out.println("Filter by Year changed to "+filterByYear);
                        }
                        else if(label == filterByRegionText)
                        {
                            filterByRegion = true;
                            System.out.println("Filter by Region changed to "+filterByRegion);
                        }
                        
                        else if(label == filterBySchoolText)
                        {
                            filterBySchool = true;
                            System.out.println("Filter by School changed to "+filterBySchool);
                        }
                        
                        else if(label == filterByGradeText)
                        {
                            filterByGrade = true;
                            System.out.println("Filter by Grade changed to "+filterByGrade);
                        }
                    }
                    
                else if (group.getSelectedToggle() == buttonFilterNo) {
                    locationchoiceBox.setDisable(true);
                        if(label==filterBySexText)
                        {
                            filterBySex = false;
                            System.out.println("Filter by Sex changed to "+filterBySex);
                        }
                        else if(label==filterByYearText)
                        {
                            filterByYear = false;
                            System.out.println("Filter by Year changed to "+filterByYear);
                        }
                        
                        else if(label == filterByRegionText)
                        {
                            filterByRegion = false;
                            System.out.println("Filter by Region changed to "+filterByRegion);
                        }
                        
                        else if(label == filterBySchoolText)
                        {
                            filterBySchool = false;
                            System.out.println("Filter by School changed to "+filterBySchool);
                        }
                        
                        else if(label == filterByGradeText)
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
       
        primaryStage.setTitle("Nástroj na tvorbu sítí");
        primaryStage.setWidth(Design.sceneWidth);
        primaryStage.setHeight(Design.sceneHeight);
  
        //filters
        List<String> filterValues = SeparateDataIntoRelatedSections.getAllPosibleValuesForFilter(HeadersIndexes.indexOfYearColumn);   
        final ChoiceBox choicesYears = createChoiceBox(filterByYearText, filterValues);
        year = Integer.parseInt(filterValues.get(0));
        final HBox HBOXfilterByYear = createHbox(filterByYearText, choicesYears);
        
        filterValues = SeparateDataIntoRelatedSections.getAllPosibleValuesForFilter(HeadersIndexes.indexOfSexColumn);
        final ChoiceBox choicessexes = createChoiceBox(filterBySexText, filterValues);
        sex = Integer.parseInt(filterValues.get(0));
        final HBox HBOXfilterBySex = createHbox(filterBySexText,choicessexes);
        
        filterValues = SeparateDataIntoRelatedSections.getAllPosibleValuesForFilter(HeadersIndexes.indexOfRegionColumn);
        final ChoiceBox choicesRegions = createChoiceBox(filterByRegionText, filterValues);
        region = Integer.parseInt(filterValues.get(0));
        final HBox HBoxfilterByRegion = createHbox(filterByRegionText, choicesRegions);
        
        filterValues = SeparateDataIntoRelatedSections.getAllPosibleValuesForFilter(HeadersIndexes.indexOfSchoolColumn);
        final ChoiceBox choicesSchools = createChoiceBox(filterBySchoolText, filterValues);
        school = Integer.parseInt(filterValues.get(0));
        final HBox HBoxfilterBySchool = createHbox(filterBySchoolText, choicesSchools);
       
        filterValues = SeparateDataIntoRelatedSections.getAllPosibleValuesForFilter(HeadersIndexes.indexOfGradeColumn);
        final ChoiceBox choicesGrades = createChoiceBox(filterByGradeText, filterValues);
        grade = Integer.parseInt(filterValues.get(0));
        final HBox HBoxfilterByGrade = createHbox(filterByGradeText, choicesGrades);
        
        TitledPane gridTitlePane = new TitledPane();
        GridPane grid = new GridPane();
        grid.setVgap(4);
        grid.setPadding(new Insets(5, 5, 5, 5));
        grid.add(HBOXfilterBySex, 0, 0);
        grid.add(HBOXfilterByYear, 0, 1);
        grid.add(HBoxfilterByGrade, 0, 2);
        grid.add(HBoxfilterByRegion, 0, 3);
        grid.add(HBoxfilterBySchool, 0, 4);        
        gridTitlePane.setText("Filtry");
        gridTitlePane.setContent(grid);
        gridTitlePane.setExpanded(false);
        
        //related sections
        final HBox mb =  somethin("Stravovací návyky", 6,12);
       // final HBox toDelete =  somethin("Iné veci", 21,30);
        
        choicesNetworkMethodCreation.getSelectionModel().selectFirst();
        choicesEmptyRecords.getSelectionModel().selectLast();
        choicesMetrics.getSelectionModel().selectFirst();
        choicesNetworkMethodCreation.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
        @Override
        public void changed(ObservableValue<? extends Number> observable,
            Number oldVal, Number newVal) {

            System.out.println(observable.getValue());    
            if((int)newVal == 0)
            {
                 labelForSlider.setText("Hodnota ε:");
                 setSliderDefault(slider, checkboxNormalization.isSelected());
            }
            else
            {
                labelForSlider.setText("Hodnota k");
                setSliderForKnn(slider, maxK, minK, currentK);
                
            }

        }
    });
        
        
         checkboxNormalization.selectedProperty().addListener(new ChangeListener<Boolean>() {
                public void changed(ObservableValue ov,Boolean old_val, Boolean new_val) {
                        if(new_val)
                        {
                            if(choicesNetworkMethodCreation.getSelectionModel().getSelectedIndex() == 0)
                            {
                                System.out.println("Teraz je " +choicesNetworkMethodCreation.getSelectionModel().getSelectedItem() +"a nastavujem na odmocninu z dimenzie.");
                                setSliderDefault(slider, true);
                            }
                                
                        }
                            
                        else
                            setSliderDefault(slider, false);
                }
            });
         
         
         
         
        
      
        setSliderDefault(slider, false);
        slider.valueProperty().addListener((obs, oldval, newVal) -> {
            sliderValue.setText(newVal.toString()+" -> ?% hran");
            slider.setValue((newVal.doubleValue()));
            currentK = (int)(newVal.doubleValue());
            System.out.println("Menim current K na "+currentK);
            
            if(choicesNetworkMethodCreation.getSelectionModel().getSelectedIndex()== 1)
            {   
                if((int)slider.getMax()== currentK)
                {
                    setSliderForKnn(slider, maxK+5, minK+5, currentK);
                    maxK = maxK+5;
                    minK = minK+5;
                }
                
                if((int)slider.getMin() == currentK)
                {
                    if((minK-5)>0)
                    {
                        setSliderForKnn(slider, maxK-5, minK-5, currentK);
                        maxK = maxK-5;
                        minK = minK-5;
                    }
                    
                }
                    
            }

        });
        
        Button buttonCreateNetwork =  new Button("Vytvořit síť!");
        buttonCreateNetwork.setOnAction((event) -> {
            try {
                if(data.isEmpty())
                {
                    AlertsWindows.displayAlert("Pro vytvoření sítě je potřeba zvolit data.");
                }
                else
                {
                    System.out.println("boli zmenene data "+dataWasChanged);
                    
                    network = DataPreparationToNetwork.readSpecificLines(network,checkboxNormalization.isSelected(),choicesNetworkMethodCreation.getSelectionModel().getSelectedIndex(),selectedHeaders, filterByYear,year, filterBySex,sex, filterByGrade,grade, filterByRegion,region, filterBySchool, school, slider.getValue(),choicesMetrics.getSelectionModel().getSelectedItem().toString());
                    numberOfVertices.setText("Uzly: "+network.getVertexCount());
                    numberOfEdges.setText("Hrany: "+network.getEdgeCount());
                    double maxNumberOfEdges = (network.getVertexCount()*(network.getVertexCount()-1.0))/2.0;
                    double percentilOfEdges = (network.getEdgeCount()/maxNumberOfEdges)*100;
                    
                   /* if(percentilOfEdges != Double.NaN)
                    {
                        DecimalFormat df = new DecimalFormat("#,##");      
                        percentilOfEdges = Double.valueOf(df.format(percentilOfEdges));
                    }*/
                    
                    
                    sliderValue.setText(slider.getValue()+" -> "+percentilOfEdges+"% hran");
                    System.out.println("Network hotova "+network.getEdgeCount()+" tolko hran, a tolko uzlov "+network.getVertexCount());
                    //NetworkDrawer.drawNetwork(network, canvas1);
                    System.out.println("Vytvaram siet s layoutom "+currentLayout);
                    runLayoutOrDisplayWarning(currentLayout);
                    dataWasChanged = false;
                    //if Epsilon network - set slider max value
                    if(choicesNetworkMethodCreation.getSelectionModel().getSelectedIndex()== 0)
                        slider.setMax(UserSettings.maxSliderValue);
                    else
                    {
                        int savedCurrentK = currentK;
                        System.out.println("Vytvoril som siet a nastavujem slider na knn");
                        setSliderForKnn(slider, maxK, minK, savedCurrentK);
                    }
                        
                    
                   
                }
            } catch (IOException ex) {
                Logger.getLogger(Project2019.class.getName()).log(Level.SEVERE, null, ex);
            }
        });

        
        
     
        final HBox HBOXNetworkCreation = new HBox(); 
        
        HBOXNetworkCreation.setSpacing(5);
        HBOXNetworkCreation.getChildren().addAll(labelForSlider, sliderValue);
        
        
        final HBox HBoxNetworkCreationParams = new HBox();
        HBoxNetworkCreationParams.setSpacing(5);
        
        
        HBoxNetworkCreationParams.getChildren().addAll(labelNetworkCreationMethod, choicesNetworkMethodCreation, labelDistanceMethod,choicesMetrics, labelEmptyRecordsAction, choicesEmptyRecords);
        
        
   separatorTest.setMinHeight(20);
   
        final VBox vbox = new VBox();
        vbox.setSpacing(5);
        
        vbox.setPadding(new Insets(30, 0, 0, Design.canvasWidth + 50)); //TODO dynamicky posledne bolo 930
        vbox.getChildren().addAll(gridTitlePane, mb, createTable(table, 1),HBOXNetworkCreation,slider,HBoxNetworkCreationParams,box,separatorTest, buttonCreateNetwork, numberOfVertices, numberOfEdges, chosenLayout);//HBOxPhysical
   
        root.getChildren().addAll(vbox);
        
        //canvas
        GraphicsContext gc = canvas1.getGraphicsContext2D();
        gc.setFill(Color.WHITE);
        gc.setStroke(Color.BLACK);
        gc.fillRect(0, 0, Design.canvasWidth, Design.canvasHeight);
        
        gc.strokeLine(0, 0, Design.canvasWidth, 0); // UP -> in general start x,y, end x, y
        gc.strokeLine(0, 0, 0, Design.canvasHeight); // LEFT
        gc.strokeLine(Design.canvasWidth,Design.canvasHeight, 0,Design.canvasHeight); // DOWN
        gc.strokeLine(Design.canvasWidth, 0, Design.canvasWidth, Design.canvasHeight); 
        gc.stroke();

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

    
    private void setSliderDefault(Slider slider, Boolean isNormalizationSetToTrue)
    {
        if(isNormalizationSetToTrue)
        {
            if(data.size()==0)
                setSlider(slider, 3, 0);
            else
            setSlider(slider, Math.sqrt(data.size()),0);
        }
        else
        {
            if(UserSettings.maxSliderValue == 0.0)
                setSlider(slider, 5, 0);
            else
                setSlider(slider, UserSettings.maxSliderValue,0);
        }
    }
    
    private void setSlider(Slider slider, double max, double min)
    {
        //slider.setSnapToTicks(true);
             //right side of screen
        slider.setVisible(true);
        slider.setMax(max);
        slider.setMin(min);
        slider.setValue(defaultSliderValue);
        if(choicesNetworkMethodCreation.getSelectionModel().getSelectedIndex()== 1)
        {
            System.out.println("KNN a nastavujem k na "+currentK );
            slider.setValue(currentK);
        }
        slider.setSnapToTicks(false);
        slider.setMajorTickUnit(1);
        slider.setMinorTickCount(0);
        slider.setShowTickMarks(true);
        slider.setShowTickLabels(true);
    }
    
    private void setSliderForKnn(Slider slider, double max, double min, int currentK)
    {
        slider.setValue(currentK);
        slider.setSnapToTicks(true);
             //right side of screen
        slider.setVisible(true);
        slider.setMax(max);
        slider.setMin(min);
      
        slider.setMajorTickUnit(1);
        slider.setMinorTickCount(0);
        slider.setShowTickMarks(true);
        slider.setShowTickLabels(true);
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
class MyChangeListener implements ChangeListener<Number>{
    final ChoiceBox<Number> cb;

    MyChangeListener(ChoiceBox<Number> cb) {
        this.cb = cb;
     }

    @Override
    public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
        System.out.println("cb: "+cb.getId());
    }

}