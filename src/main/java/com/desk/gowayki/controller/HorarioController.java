package com.desk.gowayki.controller;

import com.desk.gowayki.GoWaykiApplication;
import com.desk.gowayki.config.AppContext;
import com.desk.gowayki.model.Horario;
import com.desk.gowayki.model.Ruta;
import com.desk.gowayki.model.repository.HorarioRepository;
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

import java.io.IOException;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Map;

public class HorarioController {

    @FXML
    private Label rutaLabel;
    @FXML
    private TableView<Horario> horarioTable;
    @FXML
    private TableColumn<Horario, Integer> colId;
    @FXML
    private TableColumn<Horario, String> colHoraSalida;
    @FXML
    private TableColumn<Horario, String> colHoraLlegada;

    @FXML
    private TextField horaSalidaField;
    @FXML
    private TextField horaLlegadaField;
    @FXML
    private Label statusLabel;

    private HorarioRepository horarioRepository;
    private ReportService reportService;
    private Ruta rutaActual;
    private final ObservableList<Horario> horariosObservable = FXCollections.observableArrayList();
    private final DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");

    public void setRuta(Ruta ruta) {
        this.rutaActual = ruta;
        if (ruta != null) {
            rutaLabel.setText("Horarios - " + ruta.getNombreRuta());
            cargarHorarios();
        }
    }

    @FXML
    public void initialize() {
        AppContext context = GoWaykiApplication.getAppContext();
        horarioRepository = context.getHorarioRepository();
        reportService = context.getReportService();

        colId.setCellValueFactory(new PropertyValueFactory<>("idHorario"));
        colHoraSalida.setCellValueFactory(cellData -> {
            LocalTime hora = cellData.getValue().getHoraSalida();
            String texto = hora != null ? hora.format(timeFormatter) : "-";
            return new javafx.beans.property.SimpleStringProperty(texto);
        });
        colHoraLlegada.setCellValueFactory(cellData -> {
            LocalTime hora = cellData.getValue().getHoraLlegada();
            String texto = hora != null ? hora.format(timeFormatter) : "-";
            return new javafx.beans.property.SimpleStringProperty(texto);
        });

        horarioTable.setItems(horariosObservable);

        horarioTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSel, newSel) -> {
            if (newSel != null) {
                horaSalidaField.setText(newSel.getHoraSalida() != null ? newSel.getHoraSalida().format(timeFormatter) : "");
                horaLlegadaField.setText(newSel.getHoraLlegada() != null ? newSel.getHoraLlegada().format(timeFormatter) : "");
            }
        });
    }

    @FXML
    private void onNuevo() {
        horarioTable.getSelectionModel().clearSelection();
        horaSalidaField.clear();
        horaLlegadaField.clear();
        statusLabel.setText("");
    }

    @FXML
    private void onGuardar() {
        if (rutaActual == null) {
            statusLabel.setText("No hay ruta seleccionada.");
            return;
        }

        try {
            LocalTime horaSalida = parseTime(horaSalidaField.getText());
            LocalTime horaLlegada = parseTime(horaLlegadaField.getText());

            if (horaSalida == null || horaLlegada == null) {
                statusLabel.setText("Ingrese horas válidas en formato HH:mm");
                return;
            }

            Horario seleccionado = horarioTable.getSelectionModel().getSelectedItem();
            Horario horario = (seleccionado == null) ? new Horario() : seleccionado;

            horario.setIdRuta(rutaActual.getIdRuta());
            horario.setHoraSalida(horaSalida);
            horario.setHoraLlegada(horaLlegada);

            Horario guardado = horarioRepository.save(horario);

            if (!horariosObservable.contains(guardado)) {
                horariosObservable.add(guardado);
            }
            horarioTable.refresh();
            cargarHorarios(); // Recargar para mantener orden
            statusLabel.setText("Horario guardado correctamente.");
        } catch (Exception e) {
            e.printStackTrace();
            statusLabel.setText("Error al guardar el horario.");
        }
    }

    @FXML
    private void onEliminar() {
        Horario seleccionado = horarioTable.getSelectionModel().getSelectedItem();
        if (seleccionado == null) {
            statusLabel.setText("Seleccione un horario para eliminar.");
            return;
        }

        try {
            horarioRepository.delete(seleccionado.getIdHorario());
            horariosObservable.remove(seleccionado);
            horarioTable.getSelectionModel().clearSelection();
            statusLabel.setText("Horario eliminado correctamente.");
        } catch (Exception e) {
            e.printStackTrace();
            statusLabel.setText("Error al eliminar el horario.");
        }
    }

    @FXML
    private void onGenerarReporte() {
        if (rutaActual == null) {
            statusLabel.setText("No hay ruta seleccionada.");
            return;
        }

        try {
            List<Horario> horarios = horarioRepository.findByRuta(rutaActual.getIdRuta());
            List<Map<String, Object>> datos = ReportDTO.horariosToMap(horarios);
            reportService.generarReporteHorarios(datos, rutaActual.getNombreRuta());
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
            Stage stage = (Stage) horarioTable.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Gestión de Rutas");
        } catch (IOException e) {
            e.printStackTrace();
            statusLabel.setText("Error al volver.");
        }
    }

    private void cargarHorarios() {
        if (rutaActual != null) {
            List<Horario> lista = horarioRepository.findByRuta(rutaActual.getIdRuta());
            horariosObservable.setAll(lista);
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

