module com.pharmacie.pharmacie {
    requires javafx.controlsEmpty;
    requires javafx.fxmlEmpty;
    requires javafx.swing;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires com.almasb.fxgl.all;
    requires java.sql;
    requires annotations;
    requires org.postgresql.jdbc;
    requires itextpdf;
    requires java.desktop;

    opens com.pharmacie.pharmacie.components to javafx.base;
    opens com.pharmacie.pharmacie to javafx.fxml;
    opens com.pharmacie.pharmacie.controller to javafx.fxml;


    exports com.pharmacie.pharmacie;
    exports com.pharmacie.pharmacie.controller;
    exports com.pharmacie.pharmacie.controller.tab;
    exports com.pharmacie.pharmacie.controller.modif;

    opens com.pharmacie.pharmacie.controller.modif to javafx.fxml;
    opens com.pharmacie.pharmacie.controller.tab to javafx.fxml;

    exports com.pharmacie.pharmacie.model;
    opens com.pharmacie.pharmacie.model to javafx.fxml;

}