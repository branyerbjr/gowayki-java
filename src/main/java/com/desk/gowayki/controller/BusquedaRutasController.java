package com.desk.gowayki.controller;

import com.desk.gowayki.GoWaykiApplication;
import com.desk.gowayki.config.AppContext;
import com.desk.gowayki.model.RutaBusqueda;
import com.desk.gowayki.model.repository.RutaBusquedaRepository;
import com.desk.gowayki.service.ReportDTO;
import com.desk.gowayki.service.ReportService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import javafx.beans.property.SimpleStringProperty;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Map;

/**
 * Controlador para la pantalla principal de búsqueda de rutas.
 */
public class BusquedaRutasController {

    @FXML
    private TextField origenField;
    @FXML
    private TextField destinoField;
    @FXML
    private TextField horaField;
    @FXML
    private TableView<RutaBusqueda> resultadosTable;
    @FXML
    private TableColumn<RutaBusqueda, String> colRuta;
    @FXML
    private TableColumn<RutaBusqueda, String> colEmpresa;
    @FXML
    private TableColumn<RutaBusqueda, String> colOrigen;
    @FXML
    private TableColumn<RutaBusqueda, String> colDestino;
    @FXML
    private TableColumn<RutaBusqueda, String> colHoraSalida;
    @FXML
    private TableColumn<RutaBusqueda, String> colHoraLlegada;
    @FXML
    private TableColumn<RutaBusqueda, String> colTarifa;
    @FXML
    private Label statusLabel;

    private RutaBusquedaRepository rutaBusquedaRepository;
    private ReportService reportService;
    private final ObservableList<RutaBusqueda> resultadosObservable = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        AppContext context = GoWaykiApplication.getAppContext();
        rutaBusquedaRepository = context.getRutaBusquedaRepository();
        reportService = context.getReportService();

        // Configurar columnas
        colRuta.setCellValueFactory(new PropertyValueFactory<>("nombreRuta"));
        colEmpresa.setCellValueFactory(new PropertyValueFactory<>("empresa"));
        colOrigen.setCellValueFactory(new PropertyValueFactory<>("origen"));
        colDestino.setCellValueFactory(new PropertyValueFactory<>("destino"));
        colHoraSalida.setCellValueFactory(cellData -> {
            LocalTime hora = cellData.getValue().getHoraSalida();
            String texto = hora != null ? hora.format(DateTimeFormatter.ofPattern("HH:mm")) : "-";
            return new SimpleStringProperty(texto);
        });
        colHoraLlegada.setCellValueFactory(cellData -> {
            LocalTime hora = cellData.getValue().getHoraLlegada();
            String texto = hora != null ? hora.format(DateTimeFormatter.ofPattern("HH:mm")) : "-";
            return new SimpleStringProperty(texto);
        });
        colTarifa.setCellValueFactory(cellData -> {
            BigDecimal tarifa = cellData.getValue().getTarifaAplicable();
            String texto = tarifa != null ? "S/ " + tarifa.toString() : "-";
            return new SimpleStringProperty(texto);
        });

        resultadosTable.setItems(resultadosObservable);
    }

    @FXML
    private void onBuscar() {
        String origen = origenField.getText().trim();
        String destino = destinoField.getText().trim();
        String horaStr = horaField.getText().trim();

        if (origen.isEmpty() || destino.isEmpty()) {
            statusLabel.setText("Por favor ingrese origen y destino.");
            return;
        }

        LocalTime hora = null;
        if (!horaStr.isEmpty()) {
            try {
                hora = LocalTime.parse(horaStr, DateTimeFormatter.ofPattern("HH:mm"));
            } catch (DateTimeParseException e) {
                statusLabel.setText("Formato de hora inválido. Use HH:mm (ej: 14:30)");
                return;
            }
        }

        try {
            List<RutaBusqueda> resultados = rutaBusquedaRepository.buscarRutas(origen, destino, hora);
            resultadosObservable.setAll(resultados);

            if (resultados.isEmpty()) {
                statusLabel.setText("No se encontraron rutas para la búsqueda especificada.");
            } else {
                statusLabel.setText("Se encontraron " + resultados.size() + " ruta(s).");
            }
        } catch (Exception e) {
            e.printStackTrace();
            statusLabel.setText("Error al buscar rutas: " + e.getMessage());
        }
    }

    @FXML
    private void onGenerarReporte() {
        if (resultadosObservable.isEmpty()) {
            statusLabel.setText("Realice una búsqueda primero para generar el reporte.");
            return;
        }

        try {
            String origen = origenField.getText().trim();
            String destino = destinoField.getText().trim();
            List<Map<String, Object>> datos = ReportDTO.consultasToMap(resultadosObservable);
            reportService.generarReporteConsultas(datos, origen, destino);
            statusLabel.setText("Reporte generado correctamente.");
        } catch (Exception e) {
            e.printStackTrace();
            statusLabel.setText("Error al generar reporte: " + e.getMessage());
        }
    }

    @FXML
    private void onIrEmpresas() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/desk/gowayki/view/empresa-view.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) origenField.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("GoWayki - Gestión de Empresas");
        } catch (IOException e) {
            e.printStackTrace();
            statusLabel.setText("Error al cargar pantalla de empresas.");
        }
    }

    @FXML
    private void onIrRutas() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/desk/gowayki/view/ruta-view.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) origenField.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("GoWayki - Gestión de Rutas");
        } catch (IOException e) {
            e.printStackTrace();
            statusLabel.setText("Error al cargar pantalla de rutas.");
        }
    }
}

