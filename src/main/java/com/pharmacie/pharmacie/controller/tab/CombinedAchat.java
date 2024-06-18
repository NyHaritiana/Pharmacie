package com.pharmacie.pharmacie.controller.tab;

import com.pharmacie.pharmacie.components.Medicament;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.time.LocalDate;

public class CombinedAchat extends Medicament {
    private String numAchat;
    private String nomClient;
    private final ObservableList<MedicamentInfo> medicaments;
    private LocalDate dateAchat;

    @Override
    public String toString() {
        return STR."Achat{numAchat='\{numAchat}\{'\''}, numMedoc='\{medicaments.getFirst()}\{'\''}, nomCient=\{nomClient}, nbr=\{medicaments.getLast()}, dateAchat=\{dateAchat}\{'}'}\n";
    }

    public CombinedAchat(String numAchat, String nomClient, LocalDate dateAchat) {
        this.numAchat = numAchat;
        this.nomClient = nomClient;
        this.medicaments = FXCollections.observableArrayList();
        this.dateAchat = dateAchat;
    }

    public String getNumAchat() {
        return numAchat;
    }

    public void setNumAchat(String numAchat) {
        this.numAchat = numAchat;
    }

    public String getNomClient() {
        return nomClient;
    }

    public void setNomClient(String nomClient) {
        this.nomClient = nomClient;
    }

    public ObservableList<MedicamentInfo> getMedicaments() {
        return medicaments;
    }

    public void addMedicament(String numMedoc, int nbr) {
        this.medicaments.add(new MedicamentInfo(numMedoc, nbr));
    }

    public LocalDate getDateAchat() {
        return dateAchat;
    }

    public void setDateAchat(LocalDate dateAchat) {
        this.dateAchat = dateAchat;
    }

    public static class MedicamentInfo {
        private final String numMedoc;
        private final int nbr;

        public MedicamentInfo(String numMedoc, int nbr) {
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
}
