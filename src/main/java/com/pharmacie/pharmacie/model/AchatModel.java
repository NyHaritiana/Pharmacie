package com.pharmacie.pharmacie.model;

import com.pharmacie.pharmacie.controller.tab.CombinedAchat;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class AchatModel {
    private static AchatModel instance;
    private final ObservableList<CombinedAchat> combinedAchatListe;

    private AchatModel() {
        combinedAchatListe = FXCollections.observableArrayList();
    }

    public static AchatModel getInstance() {
        if (instance == null) {
            instance = new AchatModel();
        }
        return instance;
    }

    public ObservableList<CombinedAchat> getCombinedAchatListe() {
        return combinedAchatListe;
    }

    public void addAchat(CombinedAchat combinedAchat) {
        combinedAchatListe.add(combinedAchat);
    }

}

