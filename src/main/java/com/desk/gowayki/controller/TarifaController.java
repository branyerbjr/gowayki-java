package com.desk.gowayki.controller;

import com.desk.gowayki.GoWaykiApplication;
import com.desk.gowayki.config.AppContext;
import com.desk.gowayki.model.Ruta;
import com.desk.gowayki.model.Tarifa;
import com.desk.gowayki.model.repository.TarifaRepository;
import com.desk.gowayki.service.ReportDTO;
import com.desk.gowayki.service.ReportService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class TarifaController {

    @FXML
    private Label rutaLabel;
    @FXML
    private TextField tarifaDiaField;
    @FXML
    private TextField tarifaNocheField;
    @FXML
    private TextField horaInicioField;
    @FXML
    private TextField horaFinField;
    @FXML
    private Label statusLabel;

    private TarifaRepository tarifaRepository;
    private ReportService reportService;
    private Ruta rutaActual;
    private Tarifa tarifaActual;
    private final DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");

    public void setRuta(Ruta ruta) {
        this.rutaActual = ruta;
        if (ruta != null) {
            rutaLabel.setText("Tarifa - " + ruta.getNombreRuta());
            cargarTarifa();
        }
    }

    @FXML
    public void initialize() {
        AppContext context = GoWaykiApplication.getAppContext();
        tarifaRepository = context.getTarifaRepository();
        reportService = context.getReportService();
    }

    @FXML
    private void onGuardar() {
        if (rutaActual == null) {
            statusLabel.setText("No hay ruta seleccionada.");
            return;
        }

        try {
            BigDecimal tarifaDia = parseDecimal(tarifaDiaField.getText());
            BigDecimal tarifaNoche = parseDecimal(tarifaNocheField.getText());
            LocalTime horaInicio = parseTime(horaInicioField.getText());
            LocalTime horaFin = parseTime(horaFinField.getText());

            if (tarifaDia == null || tarifaNoche == null || horaInicio == null || horaFin == null) {
                statusLabel.setText("Complete todos los campos correctamente.");
                return;
            }

            Tarifa tarifa = (tarifaActual == null) ? new Tarifa() : tarifaActual;

            tarifa.setIdRuta(rutaActual.getIdRuta());
            tarifa.setTarifaDia(tarifaDia);
            tarifa.setTarifaNoche(tarifaNoche);
            tarifa.setHoraInicioNoche(horaInicio);
            tarifa.setHoraFinNoche(horaFin);

            tarifaRepository.save(tarifa);
            tarifaActual = tarifa;
            statusLabel.setText("Tarifa guardada correctamente.");
        } catch (Exception e) {
            e.printStackTrace();
            statusLabel.setText("Error al guardar la tarifa.");
        }
    }

    @FXML
    private void onEliminar() {
        if (tarifaActual == null) {
            statusLabel.setText("No hay tarifa para eliminar.");
            return;
        }

        try {
            tarifaRepository.delete(tarifaActual.getIdTarifa());
            tarifaActual = null;
            limpiarCampos();
            statusLabel.setText("Tarifa eliminada correctamente.");
        } catch (Exception e) {
            e.printStackTrace();
            statusLabel.setText("Error al eliminar la tarifa.");
        }
    }

    @FXML
    private void onGenerarReporte() {
        if (tarifaActual == null) {
            statusLabel.setText("No hay tarifa para generar reporte.");
            return;
        }

        try {
            List<Tarifa> tarifas = new ArrayList<>();
            tarifas.add(tarifaActual);
            List<Map<String, Object>> datos = ReportDTO.tarifasToMap(tarifas);
            reportService.generarReportePDF("tarifas", "Reporte de Tarifa - " + rutaActual.getNombreRuta(), 
                    datos, null, "reporte_tarifa_" + rutaActual.getIdRuta());
            statusLabel.setText("Reporte generado correctamente.");
        } catch (Exception e) {
            e.printStackTrace();
            statusLabel.setText("Error al generar reporte: " + e.getMessage());
        }
    }

    @FXML
    private void onVolver() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/desk/gowayki/view/ruta-view.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) tarifaDiaField.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Gestión de Rutas");
        } catch (IOException e) {
            e.printStackTrace();
            statusLabel.setText("Error al volver.");
        }
    }

    private void cargarTarifa() {
        if (rutaActual != null) {
            Optional<Tarifa> tarifaOpt = tarifaRepository.findByRuta(rutaActual.getIdRuta());
            if (tarifaOpt.isPresent()) {
                tarifaActual = tarifaOpt.get();
                tarifaDiaField.setText(tarifaActual.getTarifaDia().toString());
                tarifaNocheField.setText(tarifaActual.getTarifaNoche().toString());
                horaInicioField.setText(tarifaActual.getHoraInicioNoche().format(timeFormatter));
                horaFinField.setText(tarifaActual.getHoraFinNoche().format(timeFormatter));
            } else {
                tarifaActual = null;
                limpiarCampos();
            }
        }
    }

    private void limpiarCampos() {
        tarifaDiaField.clear();
        tarifaNocheField.clear();
        horaInicioField.clear();
        horaFinField.clear();
    }

    private BigDecimal parseDecimal(String value) {
        if (value == null || value.trim().isEmpty()) {
            return null;
        }
        try {
            return new BigDecimal(value.trim());
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private LocalTime parseTime(String timeStr) {
        if (timeStr == null || timeStr.trim().isEmpty()) {
            return null;
        }
        try {
            return LocalTime.parse(timeStr.trim(), timeFormatter);
        } catch (DateTimeParseException e) {
            return null;
        }
    }
}

