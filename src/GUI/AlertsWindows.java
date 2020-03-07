/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GUI;

import java.util.List;
import java.util.Optional;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;

/**
 *
 * @author pavol
 */
public class AlertsWindows {
    
    public static void optionsSavedDialog()
    {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Informační dialog");
        alert.setHeaderText("Uložení dokončeno");
        alert.setContentText("Změny byli úspěšně uloženy.");

        alert.showAndWait();
    }
    
    public static void areYouSureDialog()
    {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Cancel action");
        alert.setHeaderText("Changes must be saved");
        alert.setContentText("Do you want to save the changes?");

        ButtonType buttonTypeOne = new ButtonType("Save");
        ButtonType buttonTypeTwo = new ButtonType("Storno");
        ButtonType buttonTypeCancel = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);

        alert.getButtonTypes().setAll(buttonTypeOne, buttonTypeTwo, buttonTypeCancel);

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == buttonTypeOne) {
            
        } else if (result.get() == buttonTypeTwo) {
            System.out.println("Tu bude konverzia sieti v tabulkach + vypocet centralit");
        } else {
    // ... user chose CANCEL or closed the dialog
        }

    }
    
    public static void displayNetworkHasNonNumericProperties(List<String> nonNumericAttributes)
    {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Upozornění");
       
        alert.setHeaderText("Při tvorbě sítě nebyly použity všechny vámi zvolené atributy");
        
        String alertText = "Seznam nenumerických atributů:\n";
        
        int index = 1;
        for(String attribute : nonNumericAttributes)
        {
            alertText += index + ". " +  attribute+ "\n";
            index++;
        }
        
        alert.setContentText(alertText);
        alert.showAndWait();
    }
    
    public static void displayAlert(String alertText) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Chybový dialog");
        alert.setHeaderText("Chyba");
        alert.setContentText(alertText);

        alert.showAndWait();
    }
}
