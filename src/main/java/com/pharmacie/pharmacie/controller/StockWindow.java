package com.pharmacie.pharmacie.controller;

import com.pharmacie.pharmacie.components.Entree;
import com.pharmacie.pharmacie.model.StockModel;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.time.LocalDate;

public class StockWindow {
    @FXML
    private TextField numEntreeField;
    @FXML
    private TextField numMedocEntreeField;
    @FXML
    private TextField nombreEntreeField;
    @FXML
    private TextField dateEntreeField;

    @FXML
    private Button ajstock;

    @FXML
    private Button cancelstock;

    // Method to handle adding a new entree de stock
    @FXML
    void AddStock(ActionEvent event) {
        try {
            String numEntree = numEntreeField.getText();
            String numMedoc = numMedocEntreeField.getText();
            int stockEntree = Integer.parseInt(nombreEntreeField.getText());
            LocalDate dateEntree = LocalDate.parse(dateEntreeField.getText());

            Entree entree = new Entree(numEntree, numMedoc, stockEntree, dateEntree);
            entree.addEntree(entree);

            // Add the new medicament to the shared model
            StockModel.getInstance().addStock(entree);

            // Clear the form fields
            clearFields();

            // Close the window
            Stage stage = (Stage) ajstock.getScene().getWindow();
            stage.close();
            // Show success message
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Success");
            alert.setHeaderText(null);
            alert.setContentText("Entree de Stock added!");
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

    // Method to clear all form fields
    private void clearFields() {
        numEntreeField.clear();
        numMedocEntreeField.clear();
        numEntreeField.clear();
        dateEntreeField.clear();
    }

    // Method to handle canceling the operation
    @FXML
    void Cancel(ActionEvent event) {
        clearFields();

        // Close the window
        Stage stage = (Stage) cancelstock.getScene().getWindow();
        stage.close();
    }
}
