package com.pharmacie.pharmacie.components;

import com.pharmacie.pharmacie.DbFunctions;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;

// Classe Medicament
public class Medicament {
    private String numMedoc;
    private String Design;
    private int prix_unitaire;
    private int stock;

    // Constructeur avec stock initialisé à 0 par défaut
    public Medicament(){}

    public Medicament(String numMedoc, String Design, int prix_unitaire) {
        this.numMedoc = numMedoc;
        this.Design = Design;
        this.prix_unitaire = prix_unitaire;
        this.stock = 0; // stock initialisé à 0
    }

    // Constructeur
    public Medicament(String numMedoc, String Design, int prix_unitaire, int stock) {
        this.numMedoc = numMedoc;
        this.Design = Design;
        this.prix_unitaire = prix_unitaire;
        this.stock = stock;
    }

    // Getters et setters
    public String getNumMedoc() {
        return numMedoc;
    }

    public void setNumMedoc(String numMedoc) {
        this.numMedoc = numMedoc;
    }

    public String getDesign() {
        return Design;
    }

    ///public void setDesign(String design) {
        //this.Design = design;
    //}

    public int getPrix() {
        return prix_unitaire;
    }

    ///public void setPrix(int prix) {
      //  this.prix_unitaire = prix;
    //}

    public int getStock() {
        return stock;
    }

    ///public void setStock(int stock) {
      //  this.stock = stock;
    //}

    @Override
    public String toString() {
        return STR."Medicament{numMedoc='\{numMedoc}\{'\''}, Design='\{Design}\{'\''}, prix_unitaire=\{prix_unitaire}, stock=\{stock}\{'}'}";
    }

    DbFunctions db = new DbFunctions();

    // Ajouter un medicament
    public void addMedicament(Medicament medicament) {
        String sql = "INSERT INTO \"MEDICAMENT\" (\"numMedoc\", \"Design\", \"prix_unitaire\", \"stock\") VALUES (?, ?, ?, ?)";
        try (Connection conn = db.connect_to_db();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, medicament.getNumMedoc());
            pstmt.setString(2, medicament.getDesign());
            pstmt.setInt(3, medicament.getPrix());
            pstmt.setInt(4, medicament.getStock());
            pstmt.executeUpdate();
            System.out.println("Medicament added successfully");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Mettre à jour un medicament
    public void updateMedicament(Medicament updatedMedicament) {
        String sql = "UPDATE \"MEDICAMENT\" SET \"Design\" = ?, \"prix_unitaire\" = ? WHERE \"numMedoc\" = ?";
        try (Connection conn = db.connect_to_db();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, updatedMedicament.getDesign());
            pstmt.setInt(2, updatedMedicament.getPrix());
            pstmt.setString(3, updatedMedicament.getNumMedoc());
            pstmt.executeUpdate();
            System.out.println("Medicament updated successfully");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Supprimer un medicament
    public void deleteMedicament(String numMedoc) {
        String sql = "DELETE FROM \"MEDICAMENT\" WHERE \"numMedoc\" = ?";
        try (Connection conn = db.connect_to_db();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, numMedoc);
            pstmt.executeUpdate();
            System.out.println("Medicament deleted successfully");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Afficher tous les medicaments
    public ObservableList<Medicament> getAllMedicaments() {
        ObservableList<Medicament> medicamentList = FXCollections.observableArrayList();
        String sql = "SELECT * FROM \"MEDICAMENT\"";
        try (Connection conn = db.connect_to_db();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                String numMedoc = rs.getString("numMedoc");
                String design = rs.getString("Design");
                int prix = rs.getInt("prix_unitaire");
                int stock = rs.getInt("stock");
                Medicament medicament = new Medicament(numMedoc, design, prix, stock);
                medicamentList.add(medicament);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    return medicamentList;
    }



    public void stockMaj() {
        String query = "SELECT \"numMedoc\", SUM(\"stockEntree\") AS \"stockMedoc\" FROM \"ENTREE\" GROUP BY \"numMedoc\"";
        try (Connection conn = db.connect_to_db();
             PreparedStatement pstmt = conn.prepareStatement(query);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                String numMedoc = rs.getString("numMedoc");
                int stock = rs.getInt("stockMedoc");
                String sql = "UPDATE \"MEDICAMENT\" SET stock = ? WHERE \"numMedoc\" = ?";
                try (PreparedStatement updateStmt = conn.prepareStatement(sql)) {
                    updateStmt.setInt(1, stock);
                    updateStmt.setString(2, numMedoc);
                    updateStmt.executeUpdate();
                }

                String sql1 = "UPDATE \"MEDICAMENT\" SET stock = 0 WHERE \"numMedoc\" NOT IN (SELECT \"numMedoc\" FROM \"ENTREE\")";
                try (Statement updateStmt1 = conn.createStatement()) {
                    updateStmt1.executeUpdate(sql1);
                }

                String querym = "SELECT SUM(\"nbr\") AS \"achatMedoc\" FROM \"ACHAT\" WHERE \"numMedoc\" = ?";
                try (PreparedStatement pstmtm = conn.prepareStatement(querym)) {
                    pstmtm.setString(1, numMedoc);
                    try (ResultSet rsm = pstmtm.executeQuery()) {
                        if (rsm.next()) {
                            int stockm = rsm.getInt("achatMedoc");

                            String sqlm = "UPDATE \"MEDICAMENT\" SET stock = ? WHERE \"numMedoc\" = ?";
                             try (PreparedStatement updateStmt2 = conn.prepareStatement(sqlm)) {
                                 updateStmt2.setInt(1, stock - stockm);
                                 updateStmt2.setString(2, numMedoc);
                                 updateStmt2.executeUpdate();
                             }
                        }
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


}
