package com.pharmacie.pharmacie;


import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

public class App extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("pharmacie.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 720, 480);
        stage.setMinWidth(1280);
        stage.setMinHeight(720);
        stage.initStyle(StageStyle.UNDECORATED);
        stage.setTitle("Pharmacie");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
