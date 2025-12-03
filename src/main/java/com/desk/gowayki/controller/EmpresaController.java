package com.desk.gowayki.controller;

import com.desk.gowayki.GoWaykiApplication;
import com.desk.gowayki.config.AppContext;
import com.desk.gowayki.model.Empresa;
import com.desk.gowayki.model.repository.EmpresaRepository;
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
import java.util.List;
import java.util.Map;

/**
 * Controlador principal con CRUD sencillo para la entidad Empresa.
 */
public class EmpresaController {

    @FXML
    private TableView<Empresa> empresaTable;
    @FXML
    private TableColumn<Empresa, Integer> colId;
    @FXML
    private TableColumn<Empresa, String> colNombre;
    @FXML
    private TableColumn<Empresa, String> colRuc;
    @FXML
    private TableColumn<Empresa, String> colTelefono;

    @FXML
    private TextField nombreField;
    @FXML
    private TextField rucField;
    @FXML
    private TextField telefonoField;

    @FXML
    private Label statusLabel;

    private EmpresaRepository empresaRepository;
    private ReportService reportService;
    private final ObservableList<Empresa> empresasObservable = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        AppContext context = GoWaykiApplication.getAppContext();
        empresaRepository = context.getEmpresaRepository();
        reportService = context.getReportService();

        // Configurar columnas de la tabla
        colId.setCellValueFactory(new PropertyValueFactory<>("idEmpresa"));
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colRuc.setCellValueFactory(new PropertyValueFactory<>("ruc"));
        colTelefono.setCellValueFactory(new PropertyValueFactory<>("telefono"));

        empresaTable.setItems(empresasObservable);

        // Cargar datos iniciales
        cargarEmpresas();

        // Al seleccionar una fila, pasar datos al formulario
        empresaTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSel, newSel) -> {
            if (newSel != null) {
                nombreField.setText(newSel.getNombre());
                rucField.setText(newSel.getRuc());
                telefonoField.setText(newSel.getTelefono());
            }
        });
    }

    @FXML
    private void onNuevo() {
        empresaTable.getSelectionModel().clearSelection();
        nombreField.clear();
        rucField.clear();
        telefonoField.clear();
        statusLabel.setText("");
    }

    @FXML
    private void onGuardar() {
        try {
            Empresa seleccionada = empresaTable.getSelectionModel().getSelectedItem();
            Empresa empresa;

            if (seleccionada == null) {
                empresa = new Empresa();
            } else {
                empresa = seleccionada;
            }

            empresa.setNombre(nombreField.getText());
            empresa.setRuc(rucField.getText());
            empresa.setTelefono(telefonoField.getText());

            Empresa guardada = empresaRepository.save(empresa);

            if (!empresasObservable.contains(guardada)) {
                empresasObservable.add(guardada);
            }
            empresaTable.refresh();
            statusLabel.setText("Empresa guardada correctamente.");
        } catch (Exception e) {
            e.printStackTrace();
            statusLabel.setText("Error al guardar la empresa.");
        }
    }

    @FXML
    private void onEliminar() {
        Empresa seleccionada = empresaTable.getSelectionModel().getSelectedItem();
        if (seleccionada == null) {
            statusLabel.setText("Seleccione una empresa para eliminar.");
            return;
        }

        // Implementación rápida: eliminar solo de la tabla (sin borrar en BD)
        // Para borrar en BD, crear método delete en EmpresaRepository.
        empresasObservable.remove(seleccionada);
        empresaTable.getSelectionModel().clearSelection();
        statusLabel.setText("Empresa eliminada de la vista (no de la BD).");
    }

    private void cargarEmpresas() {
        List<Empresa> lista = empresaRepository.findAll();
        empresasObservable.setAll(lista);
    }

    @FXML
    private void onIrRutas() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/desk/gowayki/view/ruta-view.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) empresaTable.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("GoWayki - Gestión de Rutas");
        } catch (IOException e) {
            e.printStackTrace();
            statusLabel.setText("Error al cargar pantalla de rutas.");
        }
    }

    @FXML
    private void onGenerarReporte() {
        try {
            List<Empresa> empresas = empresaRepository.findAll();
            List<Map<String, Object>> datos = ReportDTO.empresasToMap(empresas);
            reportService.generarReportePDF("empresas", "Reporte de Empresas", datos, null, "reporte_empresas");
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
            Stage stage = (Stage) empresaTable.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("GoWayki - Sistema de Transporte");
        } catch (IOException e) {
            e.printStackTrace();
            statusLabel.setText("Error al volver a la búsqueda.");
        }
    }
}

