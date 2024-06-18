package com.pharmacie.pharmacie;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;


public class ChildWindowController {

    @FXML
    private Button quit;

    @FXML
    private Button annulation;

    public void initialize() {
        quit.setOnAction(event -> {
            Stage stage = (Stage) quit.getScene().getWindow();
            stage.close();
            System.exit(0);
        });

        annulation.setOnAction(event -> {
            Stage stage = (Stage) annulation.getScene().getWindow();
            stage.close();
        });
    }

}
