package com.desk.gowayki.controller;

import com.desk.gowayki.GoWaykiApplication;
import com.desk.gowayki.config.AppContext;
import com.desk.gowayki.model.Empresa;
import com.desk.gowayki.model.Ruta;
import com.desk.gowayki.model.repository.EmpresaRepository;
import com.desk.gowayki.model.repository.RutaRepository;
import com.desk.gowayki.service.ReportDTO;
import com.desk.gowayki.service.ReportService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public class RutaController {

    @FXML
    private TableView<Ruta> rutaTable;
    @FXML
    private TableColumn<Ruta, Integer> colId;
    @FXML
    private TableColumn<Ruta, String> colNombre;
    @FXML
    private TableColumn<Ruta, String> colEmpresa;
    @FXML
    private TableColumn<Ruta, String> colOrigen;
    @FXML
    private TableColumn<Ruta, String> colDestino;

    @FXML
    private ComboBox<Empresa> empresaCombo;
    @FXML
    private TextField nombreField;
    @FXML
    private TextField origenField;
    @FXML
    private TextField destinoField;
    @FXML
    private Label statusLabel;

    private RutaRepository rutaRepository;
    private EmpresaRepository empresaRepository;
    private ReportService reportService;
    private final ObservableList<Ruta> rutasObservable = FXCollections.observableArrayList();
    private final ObservableList<Empresa> empresasObservable = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        AppContext context = GoWaykiApplication.getAppContext();
        rutaRepository = context.getRutaRepository();
        empresaRepository = context.getEmpresaRepository();
        reportService = context.getReportService();

        // Configurar columnas
        colId.setCellValueFactory(new PropertyValueFactory<>("idRuta"));
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombreRuta"));
        colOrigen.setCellValueFactory(new PropertyValueFactory<>("origen"));
        colDestino.setCellValueFactory(new PropertyValueFactory<>("destino"));
        colEmpresa.setCellValueFactory(cellData -> {
            int idEmpresa = cellData.getValue().getIdEmpresa();
            Empresa empresa = empresasObservable.stream()
                    .filter(e -> e.getIdEmpresa() == idEmpresa)
                    .findFirst()
                    .orElse(null);
            return new javafx.beans.property.SimpleStringProperty(
                    empresa != null ? empresa.getNombre() : "N/A"
            );
        });

        rutaTable.setItems(rutasObservable);
        empresaCombo.setItems(empresasObservable);

        cargarEmpresas();
        cargarRutas();

        rutaTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSel, newSel) -> {
            if (newSel != null) {
                nombreField.setText(newSel.getNombreRuta());
                origenField.setText(newSel.getOrigen());
                destinoField.setText(newSel.getDestino());
                Empresa empresa = empresasObservable.stream()
                        .filter(e -> e.getIdEmpresa() == newSel.getIdEmpresa())
                        .findFirst()
                        .orElse(null);
                empresaCombo.setValue(empresa);
            }
        });
    }

    @FXML
    private void onNuevo() {
        rutaTable.getSelectionModel().clearSelection();
        nombreField.clear();
        origenField.clear();
        destinoField.clear();
        empresaCombo.setValue(null);
        statusLabel.setText("");
    }

    @FXML
    private void onGuardar() {
        try {
            Empresa empresaSeleccionada = empresaCombo.getValue();
            if (empresaSeleccionada == null) {
                statusLabel.setText("Seleccione una empresa.");
                return;
            }

            Ruta seleccionada = rutaTable.getSelectionModel().getSelectedItem();
            Ruta ruta = (seleccionada == null) ? new Ruta() : seleccionada;

            ruta.setIdEmpresa(empresaSeleccionada.getIdEmpresa());
            ruta.setNombreRuta(nombreField.getText());
            ruta.setOrigen(origenField.getText());
            ruta.setDestino(destinoField.getText());

            Ruta guardada = rutaRepository.save(ruta);

            if (!rutasObservable.contains(guardada)) {
                rutasObservable.add(guardada);
            }
            rutaTable.refresh();
            statusLabel.setText("Ruta guardada correctamente.");
        } catch (Exception e) {
            e.printStackTrace();
            statusLabel.setText("Error al guardar la ruta.");
        }
    }

    @FXML
    private void onEliminar() {
        Ruta seleccionada = rutaTable.getSelectionModel().getSelectedItem();
        if (seleccionada == null) {
            statusLabel.setText("Seleccione una ruta para eliminar.");
            return;
        }

        try {
            rutaRepository.delete(seleccionada.getIdRuta());
            rutasObservable.remove(seleccionada);
            rutaTable.getSelectionModel().clearSelection();
            statusLabel.setText("Ruta eliminada correctamente.");
        } catch (Exception e) {
            e.printStackTrace();
            statusLabel.setText("Error al eliminar la ruta.");
        }
    }

    @FXML
    private void onVerParaderos() {
        Ruta seleccionada = rutaTable.getSelectionModel().getSelectedItem();
        if (seleccionada == null) {
            statusLabel.setText("Seleccione una ruta para ver sus paraderos.");
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/desk/gowayki/view/paradero-view.fxml"));
            Parent root = loader.load();
            ParaderoController controller = loader.getController();
            controller.setRuta(seleccionada);

            Stage stage = (Stage) rutaTable.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Paraderos - " + seleccionada.getNombreRuta());
        } catch (IOException e) {
            e.printStackTrace();
            statusLabel.setText("Error al abrir paraderos.");
        }
    }

    @FXML
    private void onVerHorarios() {
        Ruta seleccionada = rutaTable.getSelectionModel().getSelectedItem();
        if (seleccionada == null) {
            statusLabel.setText("Seleccione una ruta para ver sus horarios.");
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/desk/gowayki/view/horario-view.fxml"));
            Parent root = loader.load();
            HorarioController controller = loader.getController();
            controller.setRuta(seleccionada);

            Stage stage = (Stage) rutaTable.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Horarios - " + seleccionada.getNombreRuta());
        } catch (IOException e) {
            e.printStackTrace();
            statusLabel.setText("Error al abrir horarios.");
        }
    }

    @FXML
    private void onVerTarifa() {
        Ruta seleccionada = rutaTable.getSelectionModel().getSelectedItem();
        if (seleccionada == null) {
            statusLabel.setText("Seleccione una ruta para ver su tarifa.");
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/desk/gowayki/view/tarifa-view.fxml"));
            Parent root = loader.load();
            TarifaController controller = loader.getController();
            controller.setRuta(seleccionada);

            Stage stage = (Stage) rutaTable.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Tarifa - " + seleccionada.getNombreRuta());
        } catch (IOException e) {
            e.printStackTrace();
            statusLabel.setText("Error al abrir tarifa.");
        }
    }

    @FXML
    private void onGenerarReporte() {
        try {
            List<Ruta> rutas = rutaRepository.findAll();
            List<Map<String, Object>> datos = ReportDTO.rutasToMap(rutas);
            reportService.generarReportePDF("rutas", "Reporte de Rutas", datos, null, "reporte_rutas");
            statusLabel.setText("Reporte generado correctamente.");
        } catch (Exception e) {
            e.printStackTrace();
            statusLabel.setText("Error al generar reporte: " + e.getMessage());
        }
    }

    @FXML
    private void onVolver() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/desk/gowayki/view/busqueda-rutas-view.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) rutaTable.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("GoWayki - Sistema de Transporte");
        } catch (IOException e) {
            e.printStackTrace();
            statusLabel.setText("Error al volver.");
        }
    }

    private void cargarRutas() {
        List<Ruta> lista = rutaRepository.findAll();
        rutasObservable.setAll(lista);
    }

    private void cargarEmpresas() {
        List<Empresa> lista = empresaRepository.findAll();
        empresasObservable.setAll(lista);
    }
}

