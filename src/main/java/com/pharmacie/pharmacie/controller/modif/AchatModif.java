package com.pharmacie.pharmacie.controller.modif;

import com.pharmacie.pharmacie.components.Achat;
import com.pharmacie.pharmacie.components.Pdf;
import com.pharmacie.pharmacie.controller.AchatWindow;
import com.pharmacie.pharmacie.controller.tab.CombinedAchat;
import com.pharmacie.pharmacie.model.AchatModel;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class AchatModif {
    @FXML
    private TextField numAchatField;

    @FXML
    private TextField nomClientField;

    @FXML
    private TextField dateAchatField;

    @FXML
    private TextField medicamentField;

    @FXML
    private TextField nombreField;

    @FXML
    private VBox medicamentVBox;
    @FXML
    private HBox initialHBox;
    @FXML
    private Button modifyachat;

    private static AchatModif instance;

    public AchatModif() {
        instance = this;
    }

    public static void setAchatData(CombinedAchat achat) {
        if (instance != null && achat != null) {
            instance.numAchatField.setText(achat.getNumAchat());
            instance.nomClientField.setText(achat.getNomClient());
            instance.dateAchatField.setText(String.valueOf(achat.getDateAchat()));
            instance.medicamentVBox.getChildren().clear();
            instance.medicamentVBox.getChildren().add(instance.initialHBox);

            List<CombinedAchat.MedicamentInfo> medicaments = achat.getMedicaments();

            if (!medicaments.isEmpty()) {
                // Placer le premier élément dans initialHBox
                CombinedAchat.MedicamentInfo firstMedInfo = medicaments.get(0);
                ((TextField) instance.initialHBox.getChildren().get(0)).setText(firstMedInfo.getNumMedoc());
                ((TextField) instance.initialHBox.getChildren().get(1)).setText(String.valueOf(firstMedInfo.getNbr()));

                // Ajouter les autres éléments dans de nouveaux HBox
                for (int i = 1; i < medicaments.size(); i++) {
                    CombinedAchat.MedicamentInfo medInfo = medicaments.get(i);

                    HBox hbox = new HBox(0);
                    TextField medField = new TextField(medInfo.getNumMedoc());
                    medField.setPrefHeight(25.0);
                    medField.setPrefWidth(200);
                    medField.setEditable(true);

                    TextField nbrField = new TextField(String.valueOf(medInfo.getNbr()));
                    nbrField.setPrefHeight(25.0);
                    nbrField.setPrefWidth(40);
                    nbrField.setEditable(true);

                    Button deleteButton = new Button("-");
                    deleteButton.setTranslateX(5);
                    deleteButton.setPrefWidth(25);
                    deleteButton.setOnAction(e -> instance.medicamentVBox.getChildren().remove(hbox));

                    hbox.getChildren().addAll(medField, nbrField, deleteButton);
                    instance.medicamentVBox.getChildren().add(hbox);
                }
            }
        } else {
            System.err.println("One or more fields are null");
        }
    }


    @FXML
    private void modifyAchat(ActionEvent event) {
        try {
            String numAchat = numAchatField.getText();
            String nomClient = nomClientField.getText();
            LocalDate dateAchat = LocalDate.parse(dateAchatField.getText());

            Achat achatsup = new Achat();
            achatsup.supprimerAchat(numAchat);

            if(medicamentField.getText()!=null && nombreField.getText()!=null){
                Achat achat = new Achat(numAchat, medicamentField.getText(), nomClient, Integer.parseInt(nombreField.getText()), dateAchat);
                achat.ajouterAchat(achat);
            }

            List<AchatWindow.MedicamentQuantity> medicaments = new ArrayList<>();
            for (int i = 1; i < medicamentVBox.getChildren().size(); i++) { // Skip the initial HBox
                HBox hBox = (HBox) medicamentVBox.getChildren().get(i);
                TextField medField = (TextField) hBox.getChildren().get(0);
                TextField nbrField = (TextField) hBox.getChildren().get(1);
                medicaments.add(new AchatWindow.MedicamentQuantity(medField.getText(), Integer.parseInt(nbrField.getText())));
            }

            for (AchatWindow.MedicamentQuantity medicament : medicaments) {
                String numMedoc = medicament.getNumMedoc();
                int nbr = medicament.getNbr();
                try {
                    Achat achat = new Achat(numAchat, numMedoc, nomClient, nbr, dateAchat);
                    achat.ajouterAchat(achat);

                } catch (Exception e) {
                    throw new RuntimeException(e);
                }

            }

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Success");
            alert.setHeaderText(null);
            alert.setContentText("Achat modifié avec succès!");
            alert.showAndWait();

            CombinedAchat newAchat = new CombinedAchat(numAchat, nomClient, dateAchat);
            AchatModel.getInstance().addAchat(newAchat);

            clearFields();

        } catch (NumberFormatException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Veuiller entrer des donnees corectes");
            alert.showAndWait();
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Erreur lors de l'ajout de l'Achat");
            alert.showAndWait();
            e.printStackTrace();
        }
    }

    @FXML
    void supprimerAchat(ActionEvent event) {
        try {
            String numAchat = numAchatField.getText();
            // Assuming you have a delete method in your Medicament class
            Achat achat = new Achat();
            achat.supprimerAchat(numAchat);

            CombinedAchat newAchat = new CombinedAchat(null,null,null);
            AchatModel.getInstance().addAchat(newAchat);

            clearFields();

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Success");
            alert.setHeaderText(null);
            alert.setContentText("Achat supprimé avec succès!");
            alert.showAndWait();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void clearFields() {
        numAchatField.clear();
        nomClientField.clear();
        dateAchatField.clear();
        medicamentVBox.getChildren().clear();
    }

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
    private ImageView pdf;

    @FXML
    private void genererPdf(MouseEvent event){
        Pdf pdf = new Pdf();
        pdf.creerFacture(numAchatField.getText());
    }
}
