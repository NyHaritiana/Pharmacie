package com.pharmacie.pharmacie.components;

import com.pharmacie.pharmacie.DbFunctions;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;

import java.sql.*;
import java.time.LocalDate;

public class Achat {
    private String numAchat;
    private String numMedoc;
    private String nomClient;
    private int nbr;
    private LocalDate dateAchat;

    // Constructeurs
    public Achat() {}

    public Achat(String numAchat, String numMedoc, String nomClient, int nbr) {
        this.numAchat = numAchat;
        this.numMedoc = numMedoc;
        this.nomClient = nomClient;
        this.nbr = nbr;
        this.dateAchat = Date.valueOf(LocalDate.now()).toLocalDate();
    }

    public Achat(String numAchat, String numMedoc, String nomClient, int nbr, LocalDate dateAchat) {
        this.numAchat = numAchat;
        this.numMedoc = numMedoc;
        this.nomClient = nomClient;
        this.nbr = nbr;
        this.dateAchat = dateAchat;
    }

    // Getters et Setters
    public String getNumAchat() { return numAchat; }
    public void setNumAchat(String numAchat) { this.numAchat = numAchat; }

    public String getNumMedoc() { return numMedoc; }
    public void setNumMedoc(String numMedoc) { this.numMedoc = numMedoc; }

    public String getNomClient() { return nomClient; }
    public void setNomClient(String nomClient) { this.nomClient = nomClient; }

    public int getNbr() { return nbr; }
    public void setNbr(int nbr) { this.nbr = nbr; }

    public LocalDate getDateAchat() { return dateAchat; }
    public void setDateAchat(LocalDate dateAchat) { this.dateAchat = dateAchat; }

    @Override
    public String toString() {
        return STR."Achat{numAchat='\{numAchat}\{'\''}, numMedoc='\{numMedoc}\{'\''}, nomCient=\{nomClient}, nbr=\{nbr}, dateAchat=\{dateAchat}\{'}'}\n";
    }



    DbFunctions db = new DbFunctions();

    Medicament medoc = new Medicament();

    // Méthode pour ajouter un achat
    public void ajouterAchat(Achat achat) {

        String query= "SELECT \"stock\" FROM \"MEDICAMENT\" WHERE \"numMedoc\"='"+achat.getNumMedoc()+"'";
        try {Connection conn = db.connect_to_db();
            Statement statm = conn.createStatement();
            ResultSet rs = statm.executeQuery(query);

            if (rs.next()) {
                int stockMedoc = rs.getInt("stock");
                if((stockMedoc!=0) && (stockMedoc>=achat.getNbr())){
                    String sql = "INSERT INTO \"ACHAT\" (\"numAchat\", \"numMedoc\", \"nomClient\", \"nbr\", \"dateAchat\") VALUES (?, ?, ?, ?, ?)";
                    try (
                         PreparedStatement pstmt = conn.prepareStatement(sql)) {
                        pstmt.setString(1, achat.getNumAchat());
                        pstmt.setString(2, achat.getNumMedoc());
                        pstmt.setString(3, achat.getNomClient());
                        pstmt.setInt(4, achat.getNbr());
                        pstmt.setDate(5, Date.valueOf(achat.getDateAchat()));
                        pstmt.executeUpdate();

                        medoc.stockMaj();

                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
                else {
                    System.out.println("Stock insuffisant.");
                    // Show error message for other exceptions
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setHeaderText(null);
                    alert.setContentText(STR."Stock insuffisant!  (\{achat.getNumMedoc()})");
                    alert.showAndWait();
                }
            } else {
                System.out.println("No record found.");
                // Show error message for other exceptions
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText(null);
                alert.setContentText("Stock inexistant!");
                alert.showAndWait();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    // Méthode pour mettre à jour un achat
    public void miseAJourAchat(Achat achat) {
        String sql = "UPDATE \"ACHAT\" SET \"numMedoc\" = ?, \"nomClient\" = ?, \"nbr\" = ?, \"dateAchat\" = ? WHERE \"numAchat\" = ?";
        try (Connection conn = db.connect_to_db();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, achat.getNumMedoc());
            pstmt.setString(2, achat.getNomClient());
            pstmt.setInt(3, achat.getNbr());
            pstmt.setDate(4, Date.valueOf(achat.getDateAchat()));
            pstmt.setString(5, achat.getNumAchat());
            pstmt.executeUpdate();

            medoc.stockMaj();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Méthode pour supprimer un achat
    public void supprimerAchat(String numAchat) {
        String sql = "DELETE FROM \"ACHAT\" WHERE \"numAchat\" = ?";
        try (Connection conn = db.connect_to_db();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, numAchat);
            pstmt.executeUpdate();

            medoc.stockMaj();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Méthode pour afficher tous les achats
    public ObservableList<Achat> afficherTousLesAchats() {
        ObservableList<Achat> achats = FXCollections.observableArrayList();
        String sql = "SELECT \"numAchat\",\"numMedoc\",\"nomClient\",\"nbr\",\"dateAchat\" FROM \"ACHAT\"";
        try (Connection conn = db.connect_to_db();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                String numAchat=rs.getString("numAchat");
                String numMedoc=rs.getString("numMedoc");
                String nomClient=rs.getString("nomClient");
                int nbr=rs.getInt("nbr");
                LocalDate dateAchat=rs.getDate("dateAchat").toLocalDate();

                Achat achat = new Achat(numAchat, numMedoc, nomClient, nbr, dateAchat);
                achats.add(achat);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return achats;
    }


}
