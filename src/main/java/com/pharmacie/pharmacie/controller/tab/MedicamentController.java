package com.pharmacie.pharmacie.controller.tab;

import com.pharmacie.pharmacie.components.Entree;
import com.pharmacie.pharmacie.components.Medicament;
import com.pharmacie.pharmacie.model.AchatModel;
import com.pharmacie.pharmacie.model.MedicamentModel;
import com.pharmacie.pharmacie.controller.modif.AjoutModif;
import com.pharmacie.pharmacie.model.StockModel;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;


public class MedicamentController {

    @FXML
    private TextField recherche;


    @FXML
    private TableView<Medicament> medocTableView;

    @FXML
    private TableColumn<Medicament, String> numMedocColumn;

    @FXML
    private TableColumn<Medicament, String> designColumn;

    @FXML
    private TableColumn<Medicament, Integer> prixColumn;

    @FXML
    private TableColumn<Medicament, Integer> stockColumn;

    @FXML
    private ImageView rupture;

    @FXML
    private ToggleButton ruptureToggle;

    @FXML
    public void initialize() {
        // Initialize the columns
        try {
            numMedocColumn.setCellValueFactory(new PropertyValueFactory<>("numMedoc"));
            designColumn.setCellValueFactory(new PropertyValueFactory<>("design"));
            prixColumn.setCellValueFactory(new PropertyValueFactory<>("prix"));
            stockColumn.setCellValueFactory(new PropertyValueFactory<>("stock"));

            medocTableView.setRowFactory(_ -> {
                TableRow<Medicament> row = new TableRow<>() {
                    @Override
                    protected void updateItem(Medicament item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty || item == null) {
                            setPrefHeight(40);
                            setOpacity(0);
                            setStyle("-fx-border-color: grey; -fx-border-width: 0.2;");
                            setVisible(false);
                            setManaged(false);
                        } else {
                            setPrefHeight(40);
                            setOpacity(0.8);
                            setStyle("-fx-border-color: grey; -fx-border-width: 0.2;");
                            setVisible(true);
                            setManaged(true);
                        }
                    }
                };

                row.setOnMouseClicked(event -> {
                    if (event.getClickCount() == 1 && (!row.isEmpty())) {
                        Medicament rowData = row.getItem();
                        AjoutModif.setMedicamentData(rowData);

                    }
                });

                return row;
            });


            // Load data from the shared model
            loadMedicamentData();

            // Listen for changes in the shared model
            StockModel.getInstance().getEntreeList().addListener((ListChangeListener<Entree>) change -> {
                while (change.next()) {
                    if (change.wasAdded() || change.wasRemoved() || change.wasUpdated() || change.wasReplaced()) {
                        medocTableView.setItems(MedicamentModel.getInstance().getMedicamentList());
                        Medicament med = new Medicament();
                        med.stockMaj();
                        loadMedicamentData();
                    }
                }
            });

            // Listen for changes in the shared model
            AchatModel.getInstance().getCombinedAchatListe().addListener((ListChangeListener<CombinedAchat>) change -> {
                while (change.next()) {
                    if (change.wasAdded() || change.wasRemoved() || change.wasUpdated() || change.wasReplaced()) {
                        medocTableView.setItems(MedicamentModel.getInstance().getMedicamentList());
                        Medicament med = new Medicament();
                        med.stockMaj();
                        loadMedicamentData();
                    }
                }
            });


            // Listen for changes in the shared model
            MedicamentModel.getInstance().getMedicamentList().addListener((ListChangeListener<Medicament>) change -> {
                while (change.next()) {
                    if (change.wasAdded() || change.wasRemoved() || change.wasUpdated() || change.wasReplaced()) {
                        medocTableView.setItems(MedicamentModel.getInstance().getMedicamentList());
                        loadMedicamentData();
                    }
                }
            });

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        recherche.textProperty().addListener((observable, oldValue, newValue) -> {
            filterMedicamentList(newValue.trim());
        });

        ruptureToggle.setOnAction(event -> {
            filterRuptureMedicamentList();
        });

    }


    private void loadMedicamentData() {
        ObservableList<Medicament> medicamentList = null;
        try {
            Medicament med = new Medicament();
            medicamentList = med.getAllMedicaments();
        }  catch (Exception e) {
            e.printStackTrace();
        } finally {
            medocTableView.setItems(medicamentList);
        }
    }

    public void filterMedicamentList(String newValue) {
        ObservableList<Medicament> filteredList = FXCollections.observableArrayList();
        try {
            Medicament med = new Medicament();
            ObservableList<Medicament> allMedicaments = med.getAllMedicaments();

            for (Medicament medicament : allMedicaments) {
                if (medicament.getDesign().toLowerCase().contains(newValue.toLowerCase())) {
                    filteredList.add(medicament);
                }
            }
        } catch (Exception e){
            e.printStackTrace();
        } finally {
            medocTableView.setItems(filteredList);
        }
    }

    public void filterRuptureMedicamentList() {
        ObservableList<Medicament> filteredList = FXCollections.observableArrayList();
        try {
            Medicament med = new Medicament();
            ObservableList<Medicament> allMedicaments = med.getAllMedicaments();

            if (ruptureToggle.isSelected()) {
                for (Medicament medicament : allMedicaments) {
                    if (medicament.getStock() < 5) {
                        filteredList.add(medicament);
                        displayImage(medicament);
                    }
                }
            } else {
                filteredList = allMedicaments;
            }

        } catch (Exception e){
            e.printStackTrace();
        } finally {
            medocTableView.setItems(filteredList);
            rupture.setVisible(ruptureToggle.isSelected());
        }
    }

    private void displayImage(Medicament medicament) {

        if (medicament.getStock() > 5) {
            rupture.setVisible(true);
        } else {
            rupture.setVisible(false);
        }
    }


}
