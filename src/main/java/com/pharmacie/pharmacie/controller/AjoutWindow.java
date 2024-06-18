package com.pharmacie.pharmacie.controller;

import com.pharmacie.pharmacie.components.Medicament;
import com.pharmacie.pharmacie.model.MedicamentModel;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class AjoutWindow {

    @FXML
    private TextField numMedocField;

    @FXML
    private TextField designationField;

    @FXML
    private TextField prixUnitaireField;

    @FXML
    private TextField nombreField;

    @FXML
    private Button ajmedoc;

    @FXML
    private Button cancelmedoc;

    // Method to handle adding a new medicament
    @FXML
    void AddMedicament(ActionEvent event) {
        try {
            String numMedoc = numMedocField.getText();
            String designation = designationField.getText();
            int prixUnitaire = Integer.parseInt(prixUnitaireField.getText());

            Medicament medicament = new Medicament(numMedoc, designation, prixUnitaire);
            medicament.addMedicament(medicament);

            // Add the new medicament to the shared model
            MedicamentModel.getInstance().addMedicament(medicament);

            // Clear the form fields
            clearFields();

            // Close the window
            Stage stage = (Stage) ajmedoc.getScene().getWindow();
            stage.close();

            // Show success message
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Success");
            alert.setHeaderText(null);
            alert.setContentText("Medicament added successfully!");
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
        numMedocField.clear();
        designationField.clear();
        prixUnitaireField.clear();
        nombreField.clear();
    }

    // Method to handle canceling the operation
    @FXML
    void Cancel(ActionEvent event) {
        clearFields();

        // Close the window
        Stage stage = (Stage) cancelmedoc.getScene().getWindow();
        stage.close();
    }
}
