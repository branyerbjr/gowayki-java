package com.desk.gowayki;

import com.desk.gowayki.config.AppBootstrap;
import com.desk.gowayki.config.AppContext;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class GoWaykiApplication extends Application {

    private static AppContext appContext;

    @Override
    public void start(Stage stage) throws IOException {
        // Inicializa el contexto de la aplicación (DB, repositorios, etc.)
        appContext = AppBootstrap.init();

        FXMLLoader fxmlLoader = new FXMLLoader(GoWaykiApplication.class.getResource("view/busqueda-rutas-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 900, 600);
        stage.setTitle("GoWayki - Sistema de Transporte");
        stage.setScene(scene);
        stage.show();
    }

    public static AppContext getAppContext() {
        return appContext;
    }
}

