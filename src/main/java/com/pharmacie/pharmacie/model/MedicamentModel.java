package com.pharmacie.pharmacie.model;

import com.pharmacie.pharmacie.components.Medicament;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class MedicamentModel {
    private static MedicamentModel instance;
    private ObservableList<Medicament> medicamentList;

    private MedicamentModel() {
        medicamentList = FXCollections.observableArrayList();
    }

    public static MedicamentModel getInstance() {
        if (instance == null) {
            instance = new MedicamentModel();
        }
        return instance;
    }

    public ObservableList<Medicament> getMedicamentList() {
        return medicamentList;
    }

    public void addMedicament(Medicament medicament) {
        medicamentList.add(medicament);
    }

}

