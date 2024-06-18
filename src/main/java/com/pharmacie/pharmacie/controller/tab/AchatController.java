package com.pharmacie.pharmacie.controller.tab;

import com.pharmacie.pharmacie.components.Achat;
import com.pharmacie.pharmacie.components.Medicament;
import com.pharmacie.pharmacie.controller.modif.AchatModif;
import com.pharmacie.pharmacie.model.AchatModel;
import com.pharmacie.pharmacie.model.MedicamentModel;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

public class AchatController {

    @FXML
    private TableView<CombinedAchat> achatTableView;

    @FXML
    private TableColumn<CombinedAchat, String> numAchatColumn;

    @FXML
    private TableColumn<CombinedAchat, String> nomClientColumn;

    @FXML
    private TableColumn<CombinedAchat, LocalDate> dateAchatColumn;

    @FXML
    private TableColumn<CombinedAchat, String> numMedocColumn;

    @FXML
    private TableColumn<CombinedAchat, Integer> nbrColumn;

    // Ajoutez une référence au contrôleur AchatModif
    private AchatModif achatModifController;

    @FXML
    public void initialize() {
        // Initialize the columns
        numAchatColumn.setCellValueFactory(new PropertyValueFactory<>("numAchat"));
        nomClientColumn.setCellValueFactory(new PropertyValueFactory<>("nomClient"));
        dateAchatColumn.setCellValueFactory(new PropertyValueFactory<>("dateAchat"));

        numMedocColumn.setCellFactory(column -> new TableCell<CombinedAchat, String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setText(null);
                    setGraphic(null);
                } else {
                    CombinedAchat combinedAchat = getTableRow().getItem();
                    if (combinedAchat != null) {
                        VBox vbox = new VBox(20); // 5 is the spacing between elements
                        for (CombinedAchat.MedicamentInfo medInfo : combinedAchat.getMedicaments()) {
                            vbox.getChildren().add(new Label(medInfo.getNumMedoc()));
                        }
                        setGraphic(vbox);
                    }
                }
            }
        });

        nbrColumn.setCellFactory(column -> new TableCell<CombinedAchat, Integer>() {
            @Override
            protected void updateItem(Integer item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setText(null);
                    setGraphic(null);
                } else {
                    CombinedAchat combinedAchat = getTableRow().getItem();
                    if (combinedAchat != null) {
                        VBox vbox = new VBox(20); // 5 is the spacing between elements
                        for (CombinedAchat.MedicamentInfo medInfo : combinedAchat.getMedicaments()) {
                            vbox.getChildren().add(new Label(String.valueOf(medInfo.getNbr())));
                        }
                        setGraphic(vbox);
                    }
                }
            }
        });


        // Customize rows
        achatTableView.setRowFactory(tv -> {
            TableRow<CombinedAchat> rowAchat = new TableRow<>() {
                @Override
                protected void updateItem(CombinedAchat item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty || item == null) {
                        setPrefHeight(40);
                        setOpacity(0);
                        setStyle("-fx-border-color: grey;-fx-border-width: 0.2;");
                        setVisible(true);
                        setManaged(true);
                    } else {
                        int rowHeight = 40; // Base height for the row
                        if (item.getMedicaments().size() > 1) {
                            rowHeight += item.getMedicaments().size() * 20; // Adjust the multiplier as needed
                        }
                        setPrefHeight(rowHeight);
                        setStyle("-fx-border-color: grey;-fx-border-width: 0.2;");
                        setOpacity(0.8);
                        setVisible(true);
                        setManaged(true);
                    }
                }
            };

            rowAchat.setOnMouseClicked(event -> {
                if (event.getClickCount() == 1 && (!rowAchat.isEmpty())) {
                    CombinedAchat row = rowAchat.getItem();
                    // Envoyer les données au formulaire
                    AchatModif.setAchatData(row);
                }
            });

            return rowAchat;
        });


        // Load data from the database
        loadAchatData();

        // Listen for changes in the shared model
        AchatModel.getInstance().getCombinedAchatListe().addListener((ListChangeListener<CombinedAchat>) change -> {
            while (change.next()) {
                if (change.wasAdded() || change.wasRemoved() || change.wasUpdated() || change.wasReplaced()) {
                    achatTableView.setItems(AchatModel.getInstance().getCombinedAchatListe());
                    loadAchatData();
                }
            }
        });

    }

    public void setAchatModifController(AchatModif achatModifController) {
        this.achatModifController = achatModifController;
    }


    private void loadAchatData() {
        ObservableList<Achat> achatList = FXCollections.observableArrayList();
        ObservableList<CombinedAchat> combinedAchatList = FXCollections.observableArrayList();

        try {
            Achat achat = new Achat();
            achatList = achat.afficherTousLesAchats();

            //Transformation des données
            Map<String, CombinedAchat> combinedMap = new HashMap<>();

            for (Achat a : achatList) {
                if (combinedMap.containsKey(a.getNumAchat())) {
                    CombinedAchat existingCombined = combinedMap.get(a.getNumAchat());
                    existingCombined.addMedicament(a.getNumMedoc(), a.getNbr());
                } else {
                    CombinedAchat combinedAchat = new CombinedAchat(a.getNumAchat(), a.getNomClient(), a.getDateAchat());
                    combinedAchat.addMedicament(a.getNumMedoc(), a.getNbr());
                    combinedMap.put(a.getNumAchat(), combinedAchat);
                }
            }

            combinedAchatList.addAll(combinedMap.values());

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            achatTableView.setItems(combinedAchatList);
        }
    }



}
