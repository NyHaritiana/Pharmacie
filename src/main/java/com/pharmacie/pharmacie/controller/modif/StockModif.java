package com.pharmacie.pharmacie.controller.modif;

import com.pharmacie.pharmacie.components.Entree;
import com.pharmacie.pharmacie.components.Medicament;
import com.pharmacie.pharmacie.model.MedicamentModel;
import com.pharmacie.pharmacie.model.StockModel;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

import java.time.LocalDate;

public class StockModif {
    @FXML
    private TextField numEntreeField;

    @FXML
    private TextField numMedocEntreeField;

    @FXML
    private TextField nombreEntreeField;

    @FXML
    private TextField dateEntreeField;

    @FXML
    private Button modifstock;

    @FXML
    private Button supprimerstock;


    private static StockModif instance;

    public StockModif() {
        instance = this;
    }



    // Method to set the medicament data in the form
    public static void setEntreeData(Entree entree) {
        if (instance != null && entree != null) {
            instance.numEntreeField.setText(entree.getNumEntree());
            instance.numMedocEntreeField.setText(entree.getNumMedoc());
            instance.nombreEntreeField.setText(String.valueOf(entree.getStockEntree()));
            instance.dateEntreeField.setText(String.valueOf(entree.getDateEntree()));
        } else {
            System.err.println("One or more fields are null");
        }
    }


    // Method to handle adding a new medicament
    @FXML
    void ModifyStock(ActionEvent event) {
        try {
            String numEntree = numEntreeField.getText();
            String numMedoc = numMedocEntreeField.getText();
            int nombreEntree = Integer.parseInt(nombreEntreeField.getText());
            LocalDate dateEntree = LocalDate.parse(dateEntreeField.getText());

            Entree entree = new Entree(numEntree, numMedoc, nombreEntree,dateEntree);
            entree.updateEntree(entree);

            // Add the new medicament to the shared model
            StockModel.getInstance().addStock(entree);

            // Clear the form fields
            clearFields();


            // Show success message
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Success");
            alert.setHeaderText(null);
            alert.setContentText("Medicament modified successfully!");
            alert.showAndWait();

        } catch (NumberFormatException e) {
            // Show error message if number format is incorrect
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Please enter valid numbers for price and quantity.");
            alert.showAndWait();
        } catch (Exception e) {
            // Show error message for other exceptions
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("An error occurred while adding the medicament.");
            alert.showAndWait();
            e.printStackTrace();
        }
    }

    @FXML
    void SupprimerStock(ActionEvent event) {
        try {
            String numEntree = numEntreeField.getText();
            Entree med = new Entree();
            med.deleteEntree(numEntree);

            // Add the new medicament to the shared model
            Entree entree = new Entree();
            StockModel.getInstance().addStock(entree);

            clearFields();

            // Show success message
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Success");
            alert.setHeaderText(null);
            alert.setContentText("Medicament deleted successfully!");
            alert.showAndWait();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    // Method to clear all form fields
    private void clearFields() {
        numEntreeField.clear();
        numMedocEntreeField.clear();
        nombreEntreeField.clear();
        dateEntreeField.clear();
    }

}
