package com.pharmacie.pharmacie.model;

import com.pharmacie.pharmacie.components.Entree;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class StockModel {
    private static StockModel instance;
    private final ObservableList<Entree> entreeList;

    private StockModel() {
        entreeList = FXCollections.observableArrayList();
    }

    public static StockModel getInstance() {
        if (instance == null) {
            instance = new StockModel();
        }
        return instance;
    }

    public ObservableList<Entree> getEntreeList() {
        return entreeList;
    }

    public void addStock(Entree entree) {
        entreeList.add(entree);
    }

}

