package com.pharmacie.pharmacie.controller;

import com.pharmacie.pharmacie.DbFunctions;
import com.pharmacie.pharmacie.components.Achat;
import com.pharmacie.pharmacie.controller.tab.CombinedAchat;
import com.pharmacie.pharmacie.model.AchatModel;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class AchatWindow {

    @FXML
    public TextField nomClientField;
    @FXML
    public TextField dateAchatField;

    @FXML
    private TextField numAchatField;

    @FXML
    private Button ajachat;

    @FXML
    private Button cancelachat;

    @FXML
    private VBox medicamentVBox;
    @FXML
    private TextField medicamentField;
    @FXML
    private TextField nombreField;

    @FXML
    private void handleAddMedicament() {
        String medicament = medicamentField.getText();
        String nombre = nombreField.getText();

        if (!medicament.isEmpty() && !nombre.isEmpty()) {
            HBox newHBox = new HBox();
            newHBox.setTranslateY(2);

            TextField newMedicamentField = new TextField(medicament);
            newMedicamentField.setPrefHeight(25.0);
            newMedicamentField.setPrefWidth(200);
            newMedicamentField.setEditable(true);

            TextField newNombreField = new TextField(nombre);
            newNombreField.setTranslateX(2);
            newNombreField.setStyle("-fx-max-width: 40px;");
            newNombreField.setEditable(true);

            Button deleteButton = new Button("-");
            deleteButton.setTranslateX(5);
            deleteButton.setPrefWidth(25);
            deleteButton.setOnAction(e -> medicamentVBox.getChildren().remove(newHBox));

            newHBox.getChildren().addAll(newMedicamentField, newNombreField, deleteButton);
            medicamentVBox.getChildren().add(newHBox);

            // Clear the initial text fields for new input
            medicamentField.clear();
            nombreField.clear();
        }
    }

    @FXML
    private void handleAddAchat(ActionEvent event) {
        String numAchat = numAchatField.getText();
        String nomClient = nomClientField.getText();
        LocalDate dateAchat = LocalDate.parse(dateAchatField.getText());

        if(medicamentField.getText()!=null && nombreField.getText()!=null){
            Achat achat = new Achat(numAchat, medicamentField.getText(), nomClient, Integer.parseInt(nombreField.getText()), dateAchat);
            achat.ajouterAchat(achat);
        }

        List<MedicamentQuantity> medicaments = new ArrayList<>();
        for (int i = 1; i < medicamentVBox.getChildren().size(); i++) { // Skip the initial HBox
            HBox hBox = (HBox) medicamentVBox.getChildren().get(i);
            TextField medField = (TextField) hBox.getChildren().get(0);
            TextField nbrField = (TextField) hBox.getChildren().get(1);
            medicaments.add(new MedicamentQuantity(medField.getText(), Integer.parseInt(nbrField.getText())));
        }

        for (MedicamentQuantity medicament : medicaments) {
            String numMedoc = medicament.getNumMedoc();
            int nbr = medicament.getNbr();
            try {

                Achat achat = new Achat(numAchat, numMedoc, nomClient, nbr, dateAchat);
                achat.ajouterAchat(achat);

            } catch (Exception e) {
                throw new RuntimeException(e);
            }

        }

        String query= STR."SELECT \"numMedoc\" FROM \"ACHAT\" WHERE \"numAchat\"='\{numAchat}'";
        try {
            DbFunctions db= new DbFunctions();
            Connection conn = db.connect_to_db();
            Statement statm = conn.createStatement();
            ResultSet rs = statm.executeQuery(query);
            if (rs.next()){
                // Show success message
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Success");
                alert.setHeaderText(null);
                alert.setContentText("Achat ajouté avec succès!");
                alert.showAndWait();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        CombinedAchat newAchat = new CombinedAchat(numAchat, nomClient, dateAchat);
        AchatModel.getInstance().addAchat(newAchat);




        // Clear the form fields
        clearFields();

        // Close the window
        Stage stage = (Stage) ajachat.getScene().getWindow();
        stage.close();

    }

    public static class MedicamentQuantity {
        private final String numMedoc;
        private final int nbr;

        public MedicamentQuantity(String numMedoc, int nbr) {
            this.numMedoc = numMedoc;
            this.nbr = nbr;
        }

        public String getNumMedoc() {
            return numMedoc;
        }

        public int getNbr() {
            return nbr;
        }
    }

    // Method to clear all form fields
    private void clearFields() {
        nomClientField.clear();
        numAchatField.clear();
        dateAchatField.clear();
        nombreField.clear();
        medicamentField.clear();
    }

    // Method to handle canceling the operation
    @FXML
    void Cancel(ActionEvent event) {
        clearFields();

        // Close the window
        Stage stage = (Stage) cancelachat.getScene().getWindow();
        stage.close();
    }

}
