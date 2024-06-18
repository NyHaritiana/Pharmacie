package com.pharmacie.pharmacie.components;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfWriter;
import com.pharmacie.pharmacie.DbFunctions;
import com.pharmacie.pharmacie.controller.tab.CombinedAchat;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.*;
import java.time.LocalDate;

import static java.awt.Desktop.getDesktop;


public class Pdf {

    public void creerFacture(String numAchat) {

        String sanitizedNumAchat = numAchat.replaceAll("\\s+", "_");
        String pdfPath = String.format("C:\\Users\\Jheim\\Documents\\PDF\\facture_%s.pdf", sanitizedNumAchat);
        Document document = new Document();

        try {
            // Crée un écrivain PDF
            PdfWriter.getInstance(document, new FileOutputStream(pdfPath));

            // Ouvre le document
            document.open();

            // Ajouter un titre

            Chunk facture = new Chunk("FACTURE");
            facture.setUnderline(0.1f, -2f);
            Paragraph title = new Paragraph(facture);
            title.setAlignment(Element.ALIGN_CENTER);
            document.add(title);
            document.add(new Paragraph(" "));

            //recuperation achat dans la base de donnees
            DbFunctions db = new DbFunctions();
            Connection conn = db.connect_to_db();
            String query= STR."SELECT * FROM \"ACHAT\" WHERE \"numAchat\"='\{numAchat}'";
            String query2= STR."SELECT *FROM \"ACHAT\" WHERE \"numAchat\"='\{numAchat}'";
            Statement statm = null;
            Statement statm2 = null;
            try {
                statm = conn.createStatement();
                ResultSet rs = statm.executeQuery(query);
                statm2 = conn.createStatement();
                ResultSet rs2 = statm2.executeQuery(query2);
                if(rs2.next()) {
                    String nomClient = rs2.getString("nomClient");
                    LocalDate dateAchat = LocalDate.parse(rs2.getString("dateAchat"));

                    // Ajouter des détails de la facture
                    String date = STR."Date :       \{dateAchat}";
                    String nom = STR."Facturé à :   \{nomClient}";
                    document.add(new Paragraph(date));
                    document.add(new Paragraph(nom));
                    document.add(new Paragraph(" "));
                }
                // Ajouter un tableau avec des détails de produits/services
                PdfPTable table = new PdfPTable(4); // 4 colonnes

                // En-tête du tableau
                table.addCell(new PdfPCell(new Paragraph("Désignation")));
                table.addCell(new PdfPCell(new Paragraph("Prix Unitaire")));
                table.addCell(new PdfPCell(new Paragraph("Nombre")));
                table.addCell(new PdfPCell(new Paragraph("Total")));
                int Total = 0;

                while (rs.next()) {
                    int nbr = rs.getInt("nbr");
                    String numMedoc = rs.getString("numMedoc");
                    String queryMed= STR."SELECT \"Design\", \"prix_unitaire\" FROM \"MEDICAMENT\" WHERE \"numMedoc\"='\{numMedoc}'";
                    Statement statmMed = conn.createStatement();
                    ResultSet medoc= statmMed.executeQuery(queryMed);

                    if(medoc.next()) {
                        // Lignes de la facture
                        table.addCell(new PdfPCell(new Paragraph(medoc.getString("Design"))));
                        int prix = medoc.getInt("prix_unitaire");
                        table.addCell(new PdfPCell(new Paragraph(STR."\{String.valueOf(prix)}  Ar")));
                        table.addCell(new PdfPCell(new Paragraph(String.valueOf(nbr))));
                        int totalm = nbr*prix;
                        Total = Total + totalm;
                        table.addCell(new PdfPCell(new Paragraph(STR."\{String.valueOf(totalm)}  Ar")));
                    }
                }

                // Ajouter des cellules vides pour aligner le total à droite
                PdfPCell emptyCell = new PdfPCell(new Paragraph(""));
                emptyCell.setBorder(PdfPCell.NO_BORDER);
                table.addCell(emptyCell);
                table.addCell(emptyCell);

                Chunk underline = new Chunk("TOTAL :");
                underline.setUnderline(0.1f, -2f);
                PdfPCell total = new PdfPCell(new Paragraph(underline));
                total.setHorizontalAlignment(Element.ALIGN_RIGHT);
                total.setBorder(PdfPCell.NO_BORDER);
                table.addCell(total);

                // Cellule pour le total
                PdfPCell totalCell = new PdfPCell(new Paragraph(STR."\{String.valueOf(Total)}  Ar"));
                table.addCell(totalCell);

                document.add(table);

                // Ferme le document
                document.close();
                System.out.println("Facture PDF générée avec succès !");

                // Show success message and ask if user wants to open the PDF
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Succès");
                alert.setHeaderText(null);
                alert.setContentText("\t\t\tFacture PDF générée avec succès !\n\t Voulez-vous ouvrir le fichier PDF ? (pdf dans Documents)");

                if (alert.showAndWait().get() == ButtonType.OK) {
                    openPDF(pdfPath);
                }

            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

        } catch (FileNotFoundException | DocumentException e) {
            e.printStackTrace();
        }
    }

    private void openPDF(String pdfPath) {
        if (true) {
            try {
                File pdfFile = new File(pdfPath);
                if (pdfFile.exists()) {
                    getDesktop().open(pdfFile);
                } else {
                    System.out.println("Le fichier PDF n'existe pas.");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("L'ouverture de fichiers PDF n'est pas supportée sur ce système.");
        }
    }

}

