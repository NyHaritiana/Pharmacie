package com.pharmacie.pharmacie.controller.modif;

import com.pharmacie.pharmacie.components.Medicament;
import com.pharmacie.pharmacie.model.MedicamentModel;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

public class AjoutModif {
    @FXML
    private TextField numMedocField;

    @FXML
    private TextField designationField;

    @FXML
    private TextField prixUnitaireField;

    @FXML
    private TextField nombreField;

    @FXML
    private Button modifmedoc;

    @FXML
    private Button supprimermedoc;


    private static AjoutModif instance;

    public AjoutModif() {
        instance = this;
    }



    // Method to set the medicament data in the form
    public static void setMedicamentData(Medicament medicament) {
        if (instance != null && medicament != null) {
            instance.numMedocField.setText(medicament.getNumMedoc());
            instance.designationField.setText(medicament.getDesign());
            instance.prixUnitaireField.setText(String.valueOf(medicament.getPrix()));
            instance.nombreField.setText(String.valueOf(medicament.getStock()));
        } else {
            System.err.println("One or more fields are null");
        }
    }


    // Method to handle adding a new medicament
    @FXML
    void ModifyMedicament(ActionEvent event) {
        try {
            String numMedoc = numMedocField.getText();
            String designation = designationField.getText();
            int prixUnitaire = Integer.parseInt(prixUnitaireField.getText());

            Medicament medicament = new Medicament(numMedoc, designation, prixUnitaire);
            medicament.updateMedicament(medicament);

            // Add the new medicament to the shared model
            MedicamentModel.getInstance().addMedicament(medicament);

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
    void SupprimerMedicament(ActionEvent event) {
        try {
            String numMedoc = numMedocField.getText();
            Medicament med = new Medicament();
            med.deleteMedicament(numMedoc);
            // Add the new medicament to the shared model
            Medicament medicament = new Medicament();
            MedicamentModel.getInstance().addMedicament(medicament);

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
        numMedocField.clear();
        designationField.clear();
        prixUnitaireField.clear();
        nombreField.clear();
    }

}
