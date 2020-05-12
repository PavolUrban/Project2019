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
import DataPreparation.NEWPreprocessing;
import DataPreparation.PrepareDifferentDataSource;
import DataPreparation.SeparateDataIntoRelatedSections;
import GUI.AlertsWindows;
import GUI.BoxesRelatedSections;
import GUI.ChartMaker;
import GUI.CustomDataSourceSettingsView;
import static GUI.CustomDataSourceSettingsView.chooseFile;
import static GUI.CustomDataSourceSettingsView.modifyMainPage;
import GUI.Design;
import GUI.MyAlerts;
import GUI.MyChartMaker;
import GUI.NetworkDrawer;
import GUI.SimpleNetworkProperties;
import GUI.UserSettingsWindow;
import Layouts.Layout;
import Layouts.LayoutCircular;
import Layouts.LayoutFruchtermann;
import Layouts.LayoutISOM;
import Layouts.LayoutKK;
import NetworkAnalysis.*;
import NetworkComponents.Edge;
import NetworkComponents.Vertex;
import Sampling.RandomDegreeNode;
import Sampling.RandomEdge;
import Sampling.RandomNode;
import UserSettings.UserSettings;
import edu.uci.ics.jung.algorithms.shortestpath.DistanceStatistics;
import edu.uci.ics.jung.graph.Graph;
import static edu.uci.ics.jung.graph.util.EdgeType.DIRECTED;
import edu.uci.ics.jung.graph.util.Pair;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.UnaryOperator;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;
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
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
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
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Separator;
import javafx.scene.control.Slider;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.control.TextFormatter.Change;
import javafx.scene.control.TitledPane;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 *
 * @author pavol
 */
public class Project2019 extends Application {
   
    
    String pathToFile = "C:\\A11.csv";
    HBox eatingHabbits;
    
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
    
    
    //to be changed when file is changed
    TitledPane filtersGridTitlePane;
    VBox vbox;
    
    
    Stage loadingStage;
    Label labelWaitPlease;

    //filters active-inactive
    Boolean filterByGrade = false;
    Boolean filterByRegion = false;
    Boolean filterBySex = false;
    Boolean filterBySchool = false;
    Boolean filterByYear = false;
    
    String filterByGradeText = "Ročník";
    String filterByRegionText = "Kraj";
    String filterBySexText = "Pohlavie";
    String filterBySchoolText = "Škola";
    String filterByYearText = "Rok narodenia";
    
    Boolean attributesWasChanged = true;
    
    //filters values
    int grade = 0;
    int region = 0;
    int sex = 0;
    int school = 0;
    int year = 0;
    
    //Top edges related
    TextField textFieldTopEdges = new TextField("20");
    Label labelTopEdges = new Label("Percento najvýznamnejších hrán: ");
    
    
    //KNN related
    TextField textFieldKNN = new TextField("2");
    Label labelKNNRelated = new Label("Hodnota k: ");
    
    Label labelForSlider = new Label("Hodnota ε:");
    ChoiceBox choicesNetworkMethodCreation = new ChoiceBox(FXCollections.observableArrayList(
    "ε-okolie", "k-NN", "Najvýznamnejšie hrany", "Kombinácia ε-okolie a k-NN", "LRNet")
    );
    
     ChoiceBox choicesMetrics = new ChoiceBox(FXCollections.observableArrayList(
    "Euklidovská", "Čebyševova","Manhattan", "Pearson", "RBF")
    );
     
     ChoiceBox choicesEmptyRecords = new ChoiceBox(FXCollections.observableArrayList(
        "Doplniť medián","Doplniť priemer","Zmazať záznam")
    );
     
     ChoiceBox choicesColorizeByAttribute = new ChoiceBox(FXCollections.observableArrayList(
        "Žiadne","Podľa príslušnosti ku komponente")
    );
     
    CheckBox checkboxNormalization = new CheckBox(); 
    CheckBox checkboxSaveLayout = new CheckBox();
    
    CheckBox checkboxDrawNetwork = new CheckBox();
    
    
    Boolean saveLayout = true;
    final Separator separator = new Separator();
    final Separator separatorTest = new Separator();
    Label numberOfVertices = new Label("Uzly: ");
    Label numberOfEdges = new Label("Hrany: ");
    Label numberOfComponents = new Label("Komponenty: ");
    Label chosenLayout = new Label ("Layout: KK Layout");
    Slider slider = new Slider(1, 10, 2);
   
    String currentLayout = "KK";
    double defaultSliderValue = 2.0;
    Label sliderValue = new Label(""+defaultSliderValue);
    
    Boolean layoutWasChosen=false;
   
    Label labelNetworkCreationMethod = new Label("Metóda vytvorenia siete");
    Label labelDistanceMethod = new Label("Metrika");
    Label labelEmptyRecordsAction = new Label("Prázdne záznamy");
    Boolean networkWasCreated = false;
    
    
    Boolean onlyEpsilonWasChanged = false;
    Boolean dataWasChanged = false;
    
    
    
    public void samplingDialog(Stage stage, int samplingMethod)
    {
        StackPane secondaryLayout = new StackPane();
        Scene secondScene = new Scene(secondaryLayout, 400, 400);
 
         
        TextField textFieldSampleSize = new TextField();
        
        Label label = new Label("Nastavte prosím veľkosť vzorky od 0 do 1, kde 1 = 100%");
       
        Button buttonRun = new Button ("Spustiť sampling");
        Stage newWindow;
        
        // New window (Stage)
        newWindow = new Stage();
        newWindow.setTitle("Vzorkovanie");
        newWindow.setScene(secondScene);

        // Set position of second window, related to primary window.
        newWindow.setX(stage.getX() + 200);
        newWindow.setY(stage.getY() + 100);

        newWindow.show();   
        
        
        
        buttonRun.setOnAction((event) -> {
    
            if(Double.parseDouble(textFieldSampleSize.getText().toString()) > 1.0)
            {
                AlertsWindows.displayAlert("Maximálna hodnota je 1.0");
            }
            
            else
            {
                double sampleSize = Double.parseDouble(textFieldSampleSize.getText().toString());
                //random node
                if(samplingMethod == 0)
                {
                    RandomNode rn = new RandomNode(network);
                    rn.run(sampleSize);
                }
                    
                else if( samplingMethod == 1)
                {
                    RandomEdge re = new RandomEdge(network);
                    re.run(sampleSize);
                }
                
                //random degree node 
                else if(samplingMethod == 2)
                {
                    RandomDegreeNode rdn = new RandomDegreeNode(network);
                    rdn.run(sampleSize);
                }
                
                setBasicNetworkCharacteristics();
                runLayoutOrDisplayWarning(currentLayout);
            
            }
        });          
        
        


        Pattern pattern = Pattern.compile("\\d*|\\d+\\.\\d*");
        TextFormatter formatter = new TextFormatter((UnaryOperator<TextFormatter.Change>) change -> {
            return pattern.matcher(change.getControlNewText()).matches() ? change : null;
        });

        textFieldSampleSize.setTextFormatter(formatter);
        
        GridPane grid = new GridPane();
        grid.setVgap(4);
        grid.setHgap(10);
        grid.setPadding(new Insets(5, 5, 5, 5));


        //element, column, row
        grid.add(label, 0, 0);
        grid.add(textFieldSampleSize, 0, 1);
        
             
        grid.setAlignment(Pos.CENTER);
         final VBox vboxCustomSource = new VBox();
        vboxCustomSource.setSpacing(5);
        //vbox.setPadding(new Insets(30, 0, 0, Design.canvasWidth + 50)); //TODO dynamicky posledne bolo 930
        vboxCustomSource.getChildren().addAll(buttonRun, grid);
        vboxCustomSource.setAlignment(Pos.CENTER);
        
        secondaryLayout.getChildren().addAll(vboxCustomSource);  
    
    }
    
    
    public void openNewDataSourceWindow(Stage stage)
    {
        StackPane secondaryLayout = new StackPane();
        Scene secondScene = new Scene(secondaryLayout, 400, 400);
 
        String pathToFile;
        Label labelFilePath = new Label("Vybraný súbor");
        Label labelFilePathValue = new Label("Nevybrané!");
        Label labelSeparator = new Label("Separátor");

        Button buttonAddFile = new Button ("Vybrať dátový súbor");
        Stage newWindow;
        
        // New window (Stage)
        newWindow = new Stage();
        newWindow.setTitle("Import vlastného dátového súboru");
        newWindow.setScene(secondScene);

        // Set position of second window, related to primary window.
        newWindow.setX(stage.getX() + 200);
        newWindow.setY(stage.getY() + 100);

        newWindow.show();   
       
        buttonAddFile.setOnAction((event) -> {
    
            //read file process
            FileChooser fileChooser = new FileChooser();
            File selectedFile = fileChooser.showOpenDialog(newWindow);
        
            labelFilePathValue.setText(selectedFile.getPath());
            buttonAddFile.setText("Zmeniť dátový súbor");
        });          
        
        Label labelHeader = new Label("Hlavička");
        ChoiceBox choiceBoxHeader = new ChoiceBox(FXCollections.observableArrayList("Áno", "Nie"));
        choiceBoxHeader.setValue("Áno");
        choiceBoxHeader.setMaxWidth(50);
        
        ChoiceBox choiceBoxSeparator = new ChoiceBox(FXCollections.observableArrayList(";", ","));
        choiceBoxSeparator.setValue(";");
        choiceBoxSeparator.setMaxWidth(50);
        
        GridPane grid = new GridPane();
        grid.setVgap(4);
        grid.setHgap(10);
        grid.setPadding(new Insets(5, 5, 5, 5));


        //element, column, row
        grid.add(labelSeparator, 0, 0);
        grid.add(choiceBoxSeparator, 1, 0 );
        
        grid.add(labelFilePath, 0, 2);
        grid.add(labelFilePathValue,1 , 2);
        
        grid.add(labelHeader, 0, 3);
        grid.add(choiceBoxHeader, 1, 3);
      
        grid.setAlignment(Pos.CENTER);
        
        Button buttonSaveOptions = new Button ("Uložiť");
        buttonSaveOptions.setOnAction((event) -> {
     
            //file path is not set
            if(labelFilePathValue.getText().equalsIgnoreCase("Nevybrané!"))
                AlertsWindows.displayAlert("Pre pokračovánie je potrebné vybrať súbor");
            
            else
            {
                newWindow.close();
                
                UserSettings.pathToDataset = labelFilePathValue.getText();
                UserSettings.separator = choiceBoxSeparator.getValue().toString();
                
                if(choiceBoxHeader.getValue().toString().equalsIgnoreCase("Áno"))
                    UserSettings.hasHeader = true;
                
                else
                    UserSettings.hasHeader = false;
                
                
                modifyMainPage(vbox, selectedHeaders, data);
                
                ArrayList<Headers> headers = new ArrayList();
                try {
                    
                    if(UserSettings.hasHeader)
                    {
                        headers = PrepareDifferentDataSource.getHeaders(UserSettings.pathToDataset);

                        choicesColorizeByAttribute.getItems().clear(); //reset

                        choicesColorizeByAttribute.getItems().add("Žiadne");
                        choicesColorizeByAttribute.getItems().add("Podľa príslušnosti ku komponente");

                        for(Headers h : headers)
                        {
                            choicesColorizeByAttribute.getItems().add(h.getHeaderName());
                        }

                        choicesColorizeByAttribute.getSelectionModel().selectFirst();
                    }
                    
                    else
                    {
                        int numberOfAtributes = PrepareDifferentDataSource.getNumberOfAttributes(UserSettings.pathToDataset);
                        
                        choicesColorizeByAttribute.getItems().clear(); //reset

                        choicesColorizeByAttribute.getItems().add("Žiadne");
                        choicesColorizeByAttribute.getItems().add("Podľa príslušnosti ku komponente");
                        
                        for(int i = 0; i < numberOfAtributes; i++)
                        {
                            choicesColorizeByAttribute.getItems().add("Atribút "+i);
                            Headers newHeader = new Headers("Atribút "+i, i);
                            headers.add(newHeader);
                        }
                        
                        choicesColorizeByAttribute.getSelectionModel().selectFirst();
                    }
                    
                } catch (FileNotFoundException ex) {
                    Logger.getLogger(Project2019.class.getName()).log(Level.SEVERE, null, ex);
                }
                HBox a = new HBox();
                try {
                    a = somethin("Atribúty", 0 , 0, headers);
                    
                    
                } catch (FileNotFoundException ex) {
                    Logger.getLogger(Project2019.class.getName()).log(Level.SEVERE, null, ex);
                }
                
                
                selectedHeaders.clear();
                
                if(UserSettings.attributesBoxWasInitialized)
                    vbox.getChildren().remove(0);
                
                else
                    UserSettings.attributesBoxWasInitialized = true;
                
                vbox.getChildren().add(0,a);
          
                
                
                AlertsWindows.optionsSavedDialog();
            }
 
        });
        
        Button buttonCloseOptions = new Button ("Zrušiť");
        buttonCloseOptions.setOnAction((event) -> {
            //todo add are you sure dialog
            newWindow.close();
            //AlertsWindows.areYouSureDialog();
        });
        
        final HBox HBoxButtons = new HBox();
        HBoxButtons.setMinWidth(Design.minTableWidth);
        HBoxButtons.setSpacing(25);
        HBoxButtons.getChildren().addAll(buttonCloseOptions, buttonSaveOptions);
        HBoxButtons.setAlignment(Pos.CENTER);
        
        final VBox vboxCustomSource = new VBox();
        vboxCustomSource.setSpacing(5);
        //vbox.setPadding(new Insets(30, 0, 0, Design.canvasWidth + 50)); //TODO dynamicky posledne bolo 930
        vboxCustomSource.getChildren().addAll(buttonAddFile, grid, HBoxButtons);
        vboxCustomSource.setAlignment(Pos.CENTER);
        
        secondaryLayout.getChildren().addAll(vboxCustomSource);   
    }
    
    
    
    
    
    //TODO finish
    private HBox unfinished(String label, List<Headers> myHeaders)
    {
        final HBox HBOXChoices = new HBox();
        HBOXChoices.setMinWidth(Design.minTableWidth);
        HBOXChoices.setSpacing(25);
        
     
        MenuButton menuButton = new MenuButton(label);   
        List<CustomMenuItem> items = new ArrayList();
        List<CheckBox> checkboxes = new ArrayList();

        CheckBox selectAll = new CheckBox("Označiť všetko");  
        
        // select/unselect all
        selectAll.selectedProperty().addListener(new ChangeListener<Boolean>() {
            public void changed(ObservableValue ov,Boolean old_val, Boolean new_val) 
            {       
                for(CheckBox cbo : checkboxes)
                {
                    cbo.setSelected(new_val);
                }

                if(new_val)
                    selectAll.setText("Odznačiť všetko");
                
                else if(!new_val)
                     selectAll.setText("Označiť všetko");
            }
        });
        
        CustomMenuItem itemSelectAll = new CustomMenuItem(selectAll);
        itemSelectAll.setHideOnClick(false); 

        items.add(itemSelectAll);
        
        for(Headers header : myHeaders)
        {
            //for each header prepare Checkbox
            CheckBox cb0 = new CheckBox(header.getHeaderName()); 
            
            cb0.selectedProperty().addListener(new ChangeListener<Boolean>() 
            {
                public void changed(ObservableValue ov,Boolean old_val, Boolean new_val) 
                {
                    dataWasChanged = true;
                    
                    if(new_val)
                    {
                        SelectedAtributes myFile = new SelectedAtributes(header.getHeaderName());
                        data.add(myFile);
                        System.out.println("Changed "+header.getHeaderName()+" to "+new_val);
                        selectedHeaders.add(header);
                    }
                    
                    else
                    {
                        // check this - data.clear();
                        selectedHeaders.remove(header);
                        data.remove(header.getHeaderName());
                    } 
                    
//                     System.out.println("Bude sa nieco diat?? data maju size" + data.size()+" "+choicesNetworkMethodCreation.getSelectionModel().getSelectedIndex());
                     if(choicesNetworkMethodCreation.getSelectionModel().getSelectedIndex()==0)
                     {
//                         System.out.println("teraz by sa mal nastavovat slider podla velkosti dat");
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
    
    
    
    
    private HBox somethin(String label, int readFromIndex, int readToIndex, ArrayList<Headers> headerNames) throws FileNotFoundException
    {
        final HBox HBOXChoices = new HBox();
        HBOXChoices.setMinWidth(Design.minTableWidth);
        HBOXChoices.setSpacing(25);
        
        
        if(headerNames == null)
            allHeaders = SeparateDataIntoRelatedSections.readFile(pathToFile, readFromIndex,readToIndex);
        
        else
            allHeaders = headerNames;
        
        MenuButton menuButton = new MenuButton(label);   
        List<CustomMenuItem> items = new ArrayList();
        List<CheckBox> checkboxes = new ArrayList();

        CheckBox selectAll = new CheckBox("Označiť všetko");  
        selectAll.selectedProperty().addListener(new ChangeListener<Boolean>() {
            public void changed(ObservableValue ov,Boolean old_val, Boolean new_val) 
            {       
                for(CheckBox cbo : checkboxes)
                {
                    cbo.setSelected(new_val);
                }

                if(new_val)
                {
                    selectAll.setText("Odznačiť všetko");
                }
                else if(!new_val)
                {
                     selectAll.setText("Označiť všetko");
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
  
  
    private TableView createTable(TableView table, int id) {

      table.setMaxHeight(Design.maxTableHeight);
      table.setMinWidth(Design.minTableWidth);
      
      TableColumn lastNameCol = new TableColumn("Vybrané atribúty");
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
            AlertsWindows.displayAlert("Pre aplikovanie layoutu je nutné najskôr vytvoriť sieť.");
        }
    }
     
     private MenuBar createMainMenu() {
        MenuBar mainMenu = new MenuBar();
        mainMenu.setMinSize(Design.sceneWidth, 10);

   
        Menu menu2 = new Menu("Súbor");
        MenuItem exitItem = new MenuItem("Exit", null);
        exitItem.setMnemonicParsing(true);
        exitItem.setAccelerator(new KeyCodeCombination(KeyCode.X, KeyCombination.CONTROL_DOWN));
        exitItem.setOnAction((event) -> {
            Platform.exit();
        });

        MenuItem saveAsNetwork = new MenuItem("Uložiť sieť ako GDF", null);
        saveAsNetwork.setMnemonicParsing(true);
        saveAsNetwork.setOnAction((event) -> {
            Exports.saveNetworkAsConnectedVerticesList(copyOfPrimaryStage, network);
        });
        
        MenuItem saveAsChosenRecords = new MenuItem("Uložiť vybrané vektorové dáta", null);
        saveAsChosenRecords.setMnemonicParsing(true);
        saveAsChosenRecords.setOnAction((event) -> {
            Exports.saveNetworkAsConnectedVerticesList(copyOfPrimaryStage);
        });
        
        
        
        MenuItem saveAsAdjacencyList = new MenuItem("Uložiť sieť ako zoznam hrán", null);
        saveAsAdjacencyList.setMnemonicParsing(true);
        saveAsAdjacencyList.setOnAction((event) -> {
            Exports.saveAsAdjacencyList(copyOfPrimaryStage, network);
        });
        
        MenuItem chooseDataFile = new MenuItem("Načítať dátový súbor");
        chooseDataFile.setOnAction((event) -> {
            openNewDataSourceWindow(copyOfPrimaryStage);          
        });
        
        menu2.getItems().addAll(exitItem, saveAsNetwork, saveAsChosenRecords,saveAsAdjacencyList, chooseDataFile);
        
        Menu menuLayouts = new Menu("Layouty");
        MenuItem itemISOMLayout = new MenuItem("ISOM Layout");
        itemISOMLayout.setOnAction((event) -> {
            checkboxSaveLayout.setSelected(false);
            chosenLayout.setText("Layout: ISOM Layout");
            currentLayout = "ISOM";
            runLayoutOrDisplayWarning("ISOM");
        });

        MenuItem itemCircularLayout = new MenuItem("Circular Layout");
        itemCircularLayout.setOnAction((event) -> {
            checkboxSaveLayout.setSelected(false);
            chosenLayout.setText("Layout: Circular Layout");
            currentLayout = "Circular";
            runLayoutOrDisplayWarning("Circular");
        });

        MenuItem itemKKLayout = new MenuItem("KK Layout");
        itemKKLayout.setOnAction((event) -> {
            checkboxSaveLayout.setSelected(false);
            chosenLayout.setText("Layout: KK Layout");
            currentLayout = "KK";
            runLayoutOrDisplayWarning("KK");
        });
        
        MenuItem itemFRLayout = new MenuItem("FR Layout");
        itemFRLayout.setOnAction((event) -> {
            checkboxSaveLayout.setSelected(false);
            chosenLayout.setText("Layout: FR Layout");
            currentLayout = "FR";
            runLayoutOrDisplayWarning("FR");
        });

        menuLayouts.getItems().addAll(itemCircularLayout, itemISOMLayout, itemKKLayout, itemFRLayout);

        // Analysis based on charts        
        Menu chartAnalysis =  new Menu("Centrality");
        
        MenuItem betweeness = new MenuItem("Betweeness");
        betweeness.setOnAction((event) -> {
          networkAndTableState(1);
        });
        
         MenuItem closeness = new MenuItem("Closeness");
        closeness.setOnAction((event) -> {
          networkAndTableState(2);
        });
        
        MenuItem degree = new MenuItem("Stupeň uzlov");
        degree.setOnAction((event) -> {
          networkAndTableState(3);
        });
        
        MenuItem degreeCentrality = new MenuItem("Degree centralita");
        degreeCentrality.setOnAction((event) -> {
          networkAndTableState(5);
        });
        
        chartAnalysis.getItems().addAll(betweeness, closeness, degree, degreeCentrality);
     
        Menu sampling = new Menu("Vzorkovanie");
        
        MenuItem randomNode = new MenuItem("Random node");
        randomNode.setOnAction((event) -> {
            
            if(network != null)
            {
                samplingDialog(copyOfPrimaryStage, 0);
            }
            
            else 
                AlertsWindows.displayAlert("Najskôr vytvorte sieť, prosím.");
        });
        
        MenuItem randomEdge = new MenuItem("Random edge");
        randomEdge.setOnAction((event) -> {
            if(network != null)
            {
                samplingDialog(copyOfPrimaryStage, 1);
            }
            
            else 
                AlertsWindows.displayAlert("Najskôr vytvorte sieť, prosím.");
            
        });
        
        MenuItem randomDegreeNode = new MenuItem("Random degree node");
        randomDegreeNode.setOnAction((event) -> {
            if(network != null)
            {
                  samplingDialog(copyOfPrimaryStage, 2);
            }
            
            else 
                AlertsWindows.displayAlert("Najskôr vytvorte sieť, prosím.");
        });
        
        
        sampling.getItems().addAll(randomNode,randomDegreeNode, randomEdge);
        
        Menu statistics = new Menu("Analýza siete");
        
        MenuItem avgDegree = new MenuItem("Priemerný stupeň uzlu");
        avgDegree.setOnAction((event) -> {
           SimpleNetworkProperties snp = new SimpleNetworkProperties();
           snp.getAverageDegree(network);
        });
        
        MenuItem diamter = new MenuItem("Priemer siete");
        diamter.setOnAction((event) -> {
          SimpleNetworkProperties snp = new SimpleNetworkProperties();
          snp.getDiameter(network);
        });
        
        MenuItem globalClustCoeff = new MenuItem("Globálny zhlukovací koeficient");
        globalClustCoeff.setOnAction((event) -> {
//          SimpleNetworkProperties snp = new SimpleNetworkProperties();
//          snp.getClusteringCoefficient(network);
   
            if(network != null)
            {
                MyChartMaker mcm = new MyChartMaker();
                mcm.createChart(network);
            }
            
            else
                AlertsWindows.displayAlert("Najskôr, prosím, vytvorte sieť.");
            
        });
        
        MenuItem density = new MenuItem("Hustota siete");
        density.setOnAction((event) -> {
          SimpleNetworkProperties snp = new SimpleNetworkProperties();
          snp.getNetworkDensity(network);
        });
           
        
    
        
        statistics.getItems().addAll(avgDegree,diamter, globalClustCoeff,density);
       
        mainMenu.getMenus().addAll( menu2, menuLayouts, statistics, chartAnalysis, sampling); //menuCentralities

        return mainMenu;
    }
     
     
    private void setBasicNetworkCharacteristics()
    {
        double maxNumberOfEdges = (network.getVertexCount()*(network.getVertexCount()-1.0))/2.0;
        double percentilOfEdges = (network.getEdgeCount()/maxNumberOfEdges)*100;
        numberOfVertices.setText("Uzly: "+network.getVertexCount());
        NumberOfComponents noc = new NumberOfComponents(network);
        numberOfComponents.setText("Komponenty "+ noc.getNumberOfComponents());
        double roundedPercentil = (double) Math.round(percentilOfEdges * 100) / 100;
        numberOfEdges.setText("Hrany: "+network.getEdgeCount()+ " ("+ roundedPercentil + "%)");
        sliderValue.setText(slider.getValue()+"");
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
     
     
     
    public void cleanCanvas()
    {
        GraphicsContext gc = canvas1.getGraphicsContext2D();
        gc.setFill(Color.WHITE);
        gc.setStroke(Color.BLACK);
        gc.fillRect(0, 0, Design.canvasWidth, Design.canvasHeight);

        gc.strokeLine(0, 0, Design.canvasWidth, 0); // UP -> in general start x,y, end x, y
        gc.strokeLine(0, 0, 0, Design.canvasHeight); // LEFT
        gc.strokeLine(Design.canvasWidth,Design.canvasHeight, 0,Design.canvasHeight); // DOWN
        gc.strokeLine(Design.canvasWidth, 0, Design.canvasWidth, Design.canvasHeight); 
        gc.stroke();
    }
     
     public void runLayout(final Layout layout) {
        if (network == null) {
            return;
        }
        
        else if(checkboxDrawNetwork.isSelected() == false)
        {
            System.out.println("Checkbox selected je " + checkboxDrawNetwork.isSelected());
            cleanCanvas();
            return;
        }

        Task task = new Task() {
            @Override
            protected Object call() throws Exception {
                
                cleanCanvas();
                
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
    
    
    @Override
    public void start(Stage primaryStage) throws FileNotFoundException {
        Rectangle2D screenBounds = Screen.getPrimary().getBounds();
        //System.out.println(screenBounds.getWidth()+ "x "+screenBounds.getHeight());
        copyOfPrimaryStage = primaryStage;
        Group root = new Group();
        Scene scene = new Scene(root, Design.sceneWidth, Design.sceneHeight/*Color.CADETBLUE*/);
        
        primaryStage.setTitle("Nástroj na tvorbu sietí");
        primaryStage.setWidth(Design.sceneWidth);
        primaryStage.setHeight(Design.sceneHeight);
  
        choicesColorizeByAttribute.getSelectionModel().selectFirst();
     
        choicesNetworkMethodCreation.getSelectionModel().selectFirst();
        choicesEmptyRecords.getSelectionModel().selectLast();
        choicesMetrics.getSelectionModel().selectFirst();
        
        
        
        choicesMetrics.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
 
        @Override
        public void changed(ObservableValue<? extends Number> observable,
            Number oldVal, Number newVal) {

       
            
            //0- euclidean, 1- cebyshev, 2 - manhattan
            if((int)newVal == 0 || (int)newVal == 1 || (int)newVal == 2)
            {
                slider.setMin(0.0);
                slider.setMax(UserSettings.maxSliderValue);
            }   
            
            else
            {
                slider.setMin(0.0);
                slider.setMax(1.0);
            }

            //TODO disable LRNET knn props and so ons
        }
    });
        
        
        
        choicesNetworkMethodCreation.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
 
        @Override
        public void changed(ObservableValue<? extends Number> observable,
            Number oldVal, Number newVal) {

       
            
            //Epsilon neighbourhood
            if((int)newVal == 0)
            {
                textFieldKNN.setDisable(true);
                labelKNNRelated.setDisable(true);
                labelTopEdges.setDisable(true);
                textFieldTopEdges.setDisable(true);
              
                labelForSlider.setDisable(false);
                slider.setDisable(false);
                sliderValue.setDisable(false);

                
                setSliderDefault(slider, checkboxNormalization.isSelected());
            }
            
            //KNN
            else if((int)newVal == 1)
            {
                labelTopEdges.setDisable(true);
                textFieldTopEdges.setDisable(true);
                labelForSlider.setDisable(true);
                slider.setDisable(true);
                sliderValue.setDisable(true);
                
                textFieldKNN.setDisable(false);
                labelKNNRelated.setDisable(false);
            }
            
            //Top edges
            else if((int)newVal == 2)
            {
                textFieldKNN.setDisable(true);
                labelKNNRelated.setDisable(true);
                labelForSlider.setDisable(true);
                slider.setDisable(true);
                sliderValue.setDisable(true);
           
                labelTopEdges.setDisable(false);
                textFieldTopEdges.setDisable(false);
            }
            
            //Combinated Knn and Epsilon
            else if((int)newVal == 3)
            {
                labelTopEdges.setDisable(true);
                textFieldTopEdges.setDisable(true);
                
                textFieldKNN.setDisable(false);
                labelKNNRelated.setDisable(false);
                labelForSlider.setDisable(false);
                slider.setDisable(false);
                sliderValue.setDisable(false);
            }

            //TODO disable LRNET knn props and so ons
        }
    });
       
        
        
        
        //colorization of vertices choices
        choicesColorizeByAttribute.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
        @Override
        public void changed(ObservableValue<? extends Number> observable,
            Number oldVal, Number newVal) {
            UserSettings.colorizeByIndex = -1;
            try
            {
                int position = observable.getValue().intValue();
                UserSettings.colorizeByAttribute = choicesColorizeByAttribute.getItems().get(position).toString();
            
                if((UserSettings.colorizeByAttribute.equalsIgnoreCase("Žiadne") == false ) && ( UserSettings.colorizeByAttribute.equalsIgnoreCase("Podľa príslušnosti ku komponente") == false))
                {
                    for(Headers h: allHeaders)
                    {
                        if(h.getHeaderName().equalsIgnoreCase(choicesColorizeByAttribute.getItems().get(position).toString()))
                        {
                            UserSettings.colorizeByIndex = h.getId();
                            break;
                        }
                    }
                }
            }
            
            catch(Exception e)
            {
                System.out.println(e.getMessage());
            }
           
         }
           
        });
        
        
        
        
        
        
        
        
        
         checkboxNormalization.selectedProperty().addListener(new ChangeListener<Boolean>() {
                public void changed(ObservableValue ov,Boolean old_val, Boolean new_val) {
                        if(new_val)
                        {
                            if(choicesNetworkMethodCreation.getSelectionModel().getSelectedIndex() == 0 || choicesNetworkMethodCreation.getSelectionModel().getSelectedIndex() == 1)
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
            sliderValue.setText(newVal.toString());
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
        
        Button buttonCreateNetwork =  new Button("Vytvorit sieť!");
        buttonCreateNetwork.setOnAction((event) -> {
            if(data.isEmpty())
            {
                AlertsWindows.displayAlert("Pre vytvorenie siete je potrebné zvoliť dáta.");
            }
            
            //if Kvalue is not set
            else if(choicesNetworkMethodCreation.getSelectionModel().getSelectedIndex() == 1 && textFieldKNN.getText().equalsIgnoreCase(""))
            {
                AlertsWindows.displayAlert("Prosím, nastavte hodnotu parametru k.");
            }
            
            else if(choicesNetworkMethodCreation.getSelectionModel().getSelectedIndex() == 2 && textFieldTopEdges.getText().equalsIgnoreCase(""))
            {
                AlertsWindows.displayAlert("Prosím. nastavte percento najvýznamnejších hrán.");
            }
            
            else if(choicesNetworkMethodCreation.getSelectionModel().getSelectedIndex() == 3 && textFieldKNN.getText().equalsIgnoreCase(""))
            {
                
            }
            
            else
            {
                
                //TODO toto upravit na nieco rozumnejsie
                
                
                displayLoading(null);
                
                
               
              
                
                
                //todo add button to table or something like that to decide what should be done with non-numeric property
                if(UserSettings.hasNonNumericProperty)
                    AlertsWindows.displayNetworkHasNonNumericProperties(UserSettings.nonNumericPropertiesNames);
                
                
            }
        });
        
        final HBox HBOXNetworkCreation = new HBox(); 
        HBOXNetworkCreation.setSpacing(5);
        HBOXNetworkCreation.getChildren().addAll(labelForSlider, sliderValue);
        
        
        final HBox HBoxNetworkCreationParams = new HBox();
        HBoxNetworkCreationParams.setSpacing(5);
        
        
        HBoxNetworkCreationParams.getChildren().addAll(labelNetworkCreationMethod, choicesNetworkMethodCreation, labelDistanceMethod,choicesMetrics, labelEmptyRecordsAction, choicesEmptyRecords);
        separatorTest.setMinHeight(20);
        
        
        //KNN params
        
        UnaryOperator<Change> filter = change -> {
            String text = change.getText();
            if (text.matches("[0-9]*")) {
                return change;
            }

                return null;
        };
        
        TextFormatter<String> textFormatter = new TextFormatter<>(filter);
        labelKNNRelated.setDisable(true);
        textFieldKNN.setDisable(true);
        textFieldKNN.setMaxWidth(50);
        textFieldKNN.setTextFormatter(textFormatter);
        
       
        
        final HBox HBOXNetworkCreationParams = new HBox();
        HBOXNetworkCreationParams.setSpacing(5);
 
        
          //Top edges related
        TextFormatter<String> textFormatter2 = new TextFormatter<>(filter);
        labelTopEdges.setDisable(true);
        textFieldTopEdges.setDisable(true);
        textFieldTopEdges.setMaxWidth(50);
        textFieldTopEdges.setTextFormatter(textFormatter2);
        
        HBOXNetworkCreationParams.getChildren().addAll(labelKNNRelated, textFieldKNN, labelTopEdges , textFieldTopEdges);
        
        
        final HBox HBOXNetworkParamsColorize = new HBox();
        HBOXNetworkParamsColorize.setSpacing(5);
        Label labelColorize = new Label("Zafarbiť podľa");
   
      
       HBOXNetworkParamsColorize.getChildren().addAll(labelColorize, choicesColorizeByAttribute);
        
         final HBox box = new HBox( 5.0, new Label("Normalizovať data"), checkboxNormalization,
                              new Label("Zachovať layout"), checkboxSaveLayout,
                              new Label("Vykresliť sieť"), checkboxDrawNetwork);
   
    
        
        checkboxDrawNetwork.setSelected(true);
       
       
        vbox = new VBox();
        vbox.setSpacing(5);
        
        vbox.setPadding(new Insets(30, 0, 0, Design.canvasWidth + 50)); //TODO dynamicky posledne bolo 930
        
        
        vbox.getChildren().addAll(createTable(table, 1),HBOXNetworkCreation,slider, HBOXNetworkCreationParams, HBoxNetworkCreationParams,box,separatorTest,HBOXNetworkParamsColorize, buttonCreateNetwork, numberOfVertices, numberOfEdges, numberOfComponents, chosenLayout);//HBOxPhysical
   
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
    
    
    
        public void displayLoading(File f) {
        final ProgressIndicator pin = new ProgressIndicator();
        pin.setProgress(-1.0f);
        pin.setVisible(false);

        final VBox vb = new VBox();
        labelWaitPlease = new Label("Vytváram sieť. Počkajte, prosím...");
        labelWaitPlease.setVisible(false);

        Button start = new Button("Štart");

        start.setOnAction((event) -> {
            labelWaitPlease.setVisible(true);
            pin.setVisible(true);
            start.setDisable(true);
            startTaskWithNetwork();
        });

        Button cancel = new Button("Zrušiť");
        cancel.setOnAction((event) -> {
            network = null;
            loadingStage.close();
            GraphicsContext gc = canvas1.getGraphicsContext2D();
            gc.setFill(Color.WHITE);
            gc.fillRect(0, 0, Design.canvasWidth, Design.canvasHeight);
        });

        Group root = new Group();
        Scene scene = new Scene(root, 350, 350);
        loadingStage = new Stage();
        loadingStage.setScene(scene);
        loadingStage.setTitle("Progress Controls");
        start.setMinWidth(150);
        start.setMaxWidth(150); //dynamicky
        cancel.setMaxWidth(150);
        cancel.setMinWidth(150);

        HBox optionButtons = new HBox();
        optionButtons.setSpacing(5);
        optionButtons.getChildren().addAll(start, cancel);
        optionButtons.setAlignment(Pos.CENTER);
        vb.setSpacing(5);
        vb.setAlignment(Pos.CENTER);
        vb.getChildren().addAll(pin, labelWaitPlease, optionButtons);
        scene.setRoot(vb);
        loadingStage.initStyle(StageStyle.UNDECORATED);
        loadingStage.show();
    }
        
         public void startTaskWithNetwork() {
        // Create a Runnable
        Runnable task = new Runnable() {
            public void run() {

                try {
                    runTask();
                } catch (IOException ex) {
                    Logger.getLogger(Project2019.class.getName()).log(Level.SEVERE, null, ex);
                }

            }
        };

        // Run the task in a background thread
        Thread backgroundThread = new Thread(task);
        // Terminate the running thread if the application exits
        backgroundThread.setDaemon(true);
        // Start the thread
        backgroundThread.start();

    }
         
         
          public void runTask() throws IOException {
//TODO toto treba niekde schovat
              
//              
//                //if Epsilon network - set slider max value
//                if(choicesNetworkMethodCreation.getSelectionModel().getSelectedIndex()== 0)
//                    slider.setMax(UserSettings.maxSliderValue);
//                else
//                {
//                    int savedCurrentK = currentK;
//                    System.out.println("Vytvoril som siet a nastavujem slider na knn");
//                    setSliderForKnn(slider, maxK, minK, savedCurrentK);
//                }
              
              
              
              
        try {
            
            System.out.println("Starting creation");
            network = DataPreparationToNetwork.readSpecificLines(network,checkboxNormalization.isSelected(),choicesNetworkMethodCreation.getSelectionModel().getSelectedIndex(),selectedHeaders, filterByYear,year, filterBySex,sex, filterByGrade,grade, filterByRegion,region, filterBySchool, school, slider.getValue(),choicesMetrics.getSelectionModel().getSelectedItem().toString(), textFieldKNN.getText(), textFieldTopEdges.getText());
  



            // Update the Label on the JavaFx Application Thread
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    loadingStage.hide();
                        double maxNumberOfEdges = (network.getVertexCount()*(network.getVertexCount()-1.0))/2.0;
                double percentilOfEdges = (network.getEdgeCount()/maxNumberOfEdges)*100;
                numberOfVertices.setText("Uzly: "+network.getVertexCount());
                
                NumberOfComponents noc = new NumberOfComponents(network);
                numberOfComponents.setText("Komponenty "+ noc.getNumberOfComponents());
                double roundedPercentil = (double) Math.round(percentilOfEdges * 100) / 100;
                
                numberOfEdges.setText("Hrany: "+network.getEdgeCount()+ " ("+ roundedPercentil + "%)");
                
                //euclid, chebyshev and manhattan - assign max possible epsilon value
                if(choicesMetrics.getSelectionModel().getSelectedIndex() == 0 || choicesMetrics.getSelectionModel().getSelectedIndex() == 1 || choicesMetrics.getSelectionModel().getSelectedIndex() == 2 )
                {
                    slider.setMin(0.0);
                    slider.setMax(UserSettings.maxSliderValue);
                }    
                   
                
                sliderValue.setText(slider.getValue()+"");
                System.out.println("******* Idem vykreslit");
              
                runLayoutOrDisplayWarning(currentLayout);
                
                dataWasChanged = false;
                
                    loadingStage.close();

                }
            });

            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

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
    
    
    private void networkAndTableState(int chosenCeentrality) {
        if (network == null) {
                MyAlerts a = new MyAlerts();
                a.displayAlert("There is no data chosen.\n" + "Add some files in table, please.");
          
        } 
        else {
            whichCentralityIsChosen(chosenCeentrality);
        }

    }
    
    
     private void whichCentralityIsChosen(int chosenCentrality) {
        if (chosenCentrality == 1) {
            ChartMaker ch = new ChartMaker();
            ch.createChart("Betweenness centrality", network, 500000);
        } else if (chosenCentrality == 2) {
            ChartMaker ch = new ChartMaker();
            ch.createChart("Closeness centrality", network, 0.1);

        } else if (chosenCentrality == 3) {
            ChartMaker ch = new ChartMaker();
            ch.createChart("Degree", network, 2);

        } else if (chosenCentrality == 4) {
            ChartMaker ch = new ChartMaker();
            ch.createChart("Eigenvector centrality", network, 5);
        } else if (chosenCentrality == 5) {
            ChartMaker ch = new ChartMaker();
            ch.createChart("Degree centrality", network, 0.1);
        }
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

        public SelectedAtributes(String fileName) 
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