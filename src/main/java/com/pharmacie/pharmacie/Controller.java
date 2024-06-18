package com.pharmacie.pharmacie;

import com.pharmacie.pharmacie.components.Finance;
import com.pharmacie.pharmacie.controller.HistogramController;
import com.pharmacie.pharmacie.controller.modif.AchatModif;
import com.pharmacie.pharmacie.controller.tab.AchatController;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.XYChart;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.input.MouseButton;
import javafx.fxml.Initializable;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.net.URL;
import java.util.*;

public class Controller implements Initializable {

    @FXML
    public Pane panestock;
    @FXML
    public Pane panehome;
    @FXML
    public Pane paneachat;
    @FXML
    public Pane panehisto;
    @FXML
    public Pane paneinfo;

    @FXML
    private Label xtext;

    @FXML
    private Pane xline;

    @FXML
    private StackPane StackpaneTableau;

    @FXML
    private StackPane stackAjout;

    @FXML
    private StackPane parentstack;

    @FXML
    private VBox navig;

    @FXML
    private HBox navtete;

    @FXML
    private ImageView btnajoutmedoc;

    @FXML
    private ImageView btnajoutstock;

    @FXML
    private ImageView btnajoutachat;

    @FXML
    private ImageView home;

    @FXML
    private ImageView stockage;

    @FXML
    private ImageView quitter;

    @FXML
    private AnchorPane medoctableau;

    @FXML
    private AnchorPane stocktableau;

    @FXML
    private AnchorPane achattableau;

    @FXML
    private AnchorPane histotableau;

    @FXML
    private AnchorPane modifMedoc;

    @FXML
    private AnchorPane modifStock;

    @FXML
    private AnchorPane modifAchat;


    @Override
    public void initialize(URL url, ResourceBundle rb) {

        StackpaneTableau.getChildren().clear();
        StackpaneTableau.getChildren().add(medoctableau);
        stackAjout.getChildren().clear();
        stackAjout.getChildren().add(modifMedoc);
        panehome.setStyle("-fx-background-color: #9fffc4d3;");
        xtext.setText("MEDOCS");
        btnajoutmedoc.setVisible(true);
        btnajoutstock.setVisible(false);
        btnajoutachat.setVisible(false);
    }

    @FXML
    protected void homeClicked(MouseEvent event) {
        if (event.getButton().equals(MouseButton.PRIMARY)) {
            StackpaneTableau.getChildren().clear();
            StackpaneTableau.getChildren().add(medoctableau);
            stackAjout.getChildren().clear();
            stackAjout.getChildren().add(modifMedoc);
            panehome.setStyle("-fx-background-color: #9fffc4d3;");
            panestock.setStyle("");
            paneachat.setStyle("");
            panehisto.setStyle("");
            xtext.setText("MEDOCS");
            btnajoutmedoc.setVisible(true);
            btnajoutstock.setVisible(false);
            btnajoutachat.setVisible(false);
        }
    }

    @FXML
    protected void stockageClicked(MouseEvent event) {
        if (event.getButton().equals(MouseButton.PRIMARY)) {
            StackpaneTableau.getChildren().clear();
            StackpaneTableau.getChildren().add(stocktableau);
            stackAjout.getChildren().clear();
            stackAjout.getChildren().add(modifStock);
            panestock.setStyle("-fx-background-color: #9fffc4d3;");
            panehome.setStyle("");
            paneachat.setStyle("");
            panehisto.setStyle("");
            xtext.setText("STOCK");
            btnajoutmedoc.setVisible(false);
            btnajoutstock.setVisible(true);
            btnajoutachat.setVisible(false);
        }
    }

    @FXML
    protected void achatClicked(MouseEvent event) {
        if (event.getButton().equals(MouseButton.PRIMARY)) {
            try {
                // Charger la vue principale (achat tableau)
                FXMLLoader loader = new FXMLLoader(getClass().getResource("achat.fxml"));
                Parent achattableau = loader.load();
                AchatController achatController = loader.getController();

                // Charger la vue de modification (achat window)
                FXMLLoader otherLoader = new FXMLLoader(getClass().getResource("AchatModif.fxml"));
                Parent achatwindow = otherLoader.load();
                AchatModif achatModifController = otherLoader.getController();

                // Passer le contrôleur de modification au contrôleur principal
                achatController.setAchatModifController(achatModifController);

                // Ajouter les vues aux StackPanes
                StackpaneTableau.getChildren().clear();
                StackpaneTableau.getChildren().add(achattableau);
                stackAjout.getChildren().clear();
                stackAjout.getChildren().add(achatwindow);

                // Mettre à jour le style des panneaux
                paneachat.setStyle("-fx-background-color: #9fffc4d3;");
                panehome.setStyle("");
                panestock.setStyle("");
                panehisto.setStyle("");
                xtext.setText("ACHAT");

                // Gérer la visibilité des boutons
                btnajoutmedoc.setVisible(false);
                btnajoutstock.setVisible(false);
                btnajoutachat.setVisible(true);
            } catch (IOException e) {
                e.printStackTrace();
                // Vous pouvez également afficher une alerte ou un message d'erreur à l'utilisateur ici
            }
        }
    }


    @FXML
    protected void histoClicked(MouseEvent event) throws IOException {
        if (event.getButton().equals(MouseButton.PRIMARY)) {
            stackAjout.getChildren().clear();
            StackpaneTableau.getChildren().clear();

            FXMLLoader loader = new FXMLLoader(getClass().getResource("histogram.fxml"));
            Parent root = loader.load();
            HistogramController controller = loader.getController();

            XYChart.Series<String, Number> data = new XYChart.Series<>();
            Map<String, Integer> monthlyRevenue = Finance.recetteParMois();

            // Create a list from the map entries and reverse it
            List<Map.Entry<String, Integer>> reversedEntries = new ArrayList<>(monthlyRevenue.entrySet());
            Collections.reverse(reversedEntries);

            // Iterate over the reversed monthly revenue data and add to the histogram data
            for (Map.Entry<String, Integer> entry : reversedEntries) {
                XYChart.Data<String, Number> dataPoint = new XYChart.Data<>(entry.getKey(), entry.getValue());
                data.getData().add(dataPoint);
            }

            controller.setHistogramData(data);

            // Set custom colors for bars
            Platform.runLater(() -> {
                int colorIndex = 0;
                String[] colors = {"#174601", "#00ff00", "#36be86", "#039e10", "#00ff88"};
                for (XYChart.Data<String, Number> dataPoint : data.getData()) {
                    dataPoint.getNode().setStyle("-fx-bar-fill:" + colors[colorIndex % colors.length] + ";");
                    colorIndex++;
                }
            });

            StackpaneTableau.getChildren().add(root);
            panehisto.setStyle("-fx-background-color: #9fffc4d3;");
            panehome.setStyle("");
            panestock.setStyle("");
            paneachat.setStyle("");
            xtext.setText("HISTOGRAMME");
            btnajoutmedoc.setVisible(false);
            btnajoutstock.setVisible(false);
            btnajoutachat.setVisible(false);
        }
    }

    @FXML
    protected void quitterClicked(MouseEvent event) {

        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("ChildWindow.fxml"));
            Parent root = fxmlLoader.load();

            Stage stage = new Stage();
            stage.setTitle("Child Window");
            stage.setScene(new Scene(root));

            stage.initStyle(StageStyle.UNDECORATED);

            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    protected void medocajoutClicked(MouseEvent event) {

        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("AjoutWindow.fxml"));
            Parent root = fxmlLoader.load();

            Stage stage = new Stage();
            stage.setTitle("Child Window");
            stage.setScene(new Scene(root));

            stage.initStyle(StageStyle.UNDECORATED);

            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    protected void stockajoutClicked(MouseEvent event) {

        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("StockWindow.fxml"));
            Parent root = fxmlLoader.load();

            Stage stage = new Stage();
            stage.setTitle("Child Window");
            stage.setScene(new Scene(root));

            stage.initStyle(StageStyle.UNDECORATED);

            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    protected void achatajoutClicked(MouseEvent event) {

        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("AchatWindow.fxml"));
            Parent root = fxmlLoader.load();

            Stage stage = new Stage();
            stage.setTitle("Child Window");
            stage.setScene(new Scene(root));

            stage.initStyle(StageStyle.UNDECORATED);

            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    protected void quitterHover(MouseEvent event) {
        quitter.setOpacity(0.7);
    }

    @FXML
    protected void quitterSortieHover(MouseEvent event) {
        quitter.setOpacity(1);
    }

    @FXML
    private ImageView btnMinimize;

    @FXML
    private ImageView btnMaximize;

    @FXML
    private ImageView btnClose;

    @FXML
    private void minimizeWindow(MouseEvent event) {
        Stage stage = (Stage) btnMinimize.getScene().getWindow();
        stage.setIconified(true);
    }
    @FXML
    private void maximizeRestoreWindow(MouseEvent event) {
        Stage stage = (Stage) btnMaximize.getScene().getWindow();
        if (stage.isMaximized()) {
            stage.setMaximized(false);
        } else {
            stage.setMaximized(true);
        }
    }
    @FXML
    private void closeWindow(MouseEvent event) {
        Stage stage = (Stage) btnClose.getScene().getWindow();
        stage.close();
    }


}
