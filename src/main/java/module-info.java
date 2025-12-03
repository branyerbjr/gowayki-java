module com.desk.gowayki {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires java.sql;
    requires jasperreports;
    requires org.slf4j.simple;
    requires org.slf4j;

    opens com.desk.gowayki to javafx.fxml;
    opens com.desk.gowayki.controller to javafx.fxml;
    opens com.desk.gowayki.model to javafx.base;
    exports com.desk.gowayki;
    exports com.desk.gowayki.model;
}