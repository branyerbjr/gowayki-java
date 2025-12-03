package com.desk.gowayki.controller;

import com.desk.gowayki.GoWaykiApplication;
import com.desk.gowayki.config.AppContext;
import com.desk.gowayki.model.Paradero;
import com.desk.gowayki.model.Ruta;
import com.desk.gowayki.model.repository.ParaderoRepository;
import com.desk.gowayki.service.ReportDTO;
import com.desk.gowayki.service.ReportService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public class ParaderoController {

    @FXML
    private Label rutaLabel;
    @FXML
    private TableView<Paradero> paraderoTable;
    @FXML
    private TableColumn<Paradero, Integer> colId;
    @FXML
    private TableColumn<Paradero, Integer> colOrden;
    @FXML
    private TableColumn<Paradero, String> colNombre;

    @FXML
    private Spinner<Integer> ordenSpinner;
    @FXML
    private TextField nombreField;
    @FXML
    private Label statusLabel;

    private ParaderoRepository paraderoRepository;
    private ReportService reportService;
    private Ruta rutaActual;
    private final ObservableList<Paradero> paraderosObservable = FXCollections.observableArrayList();

    public void setRuta(Ruta ruta) {
        this.rutaActual = ruta;
        if (ruta != null) {
            rutaLabel.setText("Paraderos - " + ruta.getNombreRuta());
            cargarParaderos();
        }
    }

    @FXML
    public void initialize() {
        AppContext context = GoWaykiApplication.getAppContext();
        paraderoRepository = context.getParaderoRepository();
        reportService = context.getReportService();

        ordenSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 100, 1));

        colId.setCellValueFactory(new PropertyValueFactory<>("idParadero"));
        colOrden.setCellValueFactory(new PropertyValueFactory<>("orden"));
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombreParadero"));

        paraderoTable.setItems(paraderosObservable);

        paraderoTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSel, newSel) -> {
            if (newSel != null) {
                nombreField.setText(newSel.getNombreParadero());
                ordenSpinner.getValueFactory().setValue(newSel.getOrden());
            }
        });
    }

    @FXML
    private void onNuevo() {
        paraderoTable.getSelectionModel().clearSelection();
        nombreField.clear();
        ordenSpinner.getValueFactory().setValue(1);
        statusLabel.setText("");
    }

    @FXML
    private void onGuardar() {
        if (rutaActual == null) {
            statusLabel.setText("No hay ruta seleccionada.");
            return;
        }

        try {
            Paradero seleccionado = paraderoTable.getSelectionModel().getSelectedItem();
            Paradero paradero = (seleccionado == null) ? new Paradero() : seleccionado;

            paradero.setIdRuta(rutaActual.getIdRuta());
            paradero.setNombreParadero(nombreField.getText());
            paradero.setOrden(ordenSpinner.getValue());

            Paradero guardado = paraderoRepository.save(paradero);

            if (!paraderosObservable.contains(guardado)) {
                paraderosObservable.add(guardado);
            }
            paraderoTable.refresh();
            cargarParaderos(); // Recargar para mantener orden
            statusLabel.setText("Paradero guardado correctamente.");
        } catch (Exception e) {
            e.printStackTrace();
            statusLabel.setText("Error al guardar el paradero.");
        }
    }

    @FXML
    private void onEliminar() {
        Paradero seleccionado = paraderoTable.getSelectionModel().getSelectedItem();
        if (seleccionado == null) {
            statusLabel.setText("Seleccione un paradero para eliminar.");
            return;
        }

        try {
            paraderoRepository.delete(seleccionado.getIdParadero());
            paraderosObservable.remove(seleccionado);
            paraderoTable.getSelectionModel().clearSelection();
            statusLabel.setText("Paradero eliminado correctamente.");
        } catch (Exception e) {
            e.printStackTrace();
            statusLabel.setText("Error al eliminar el paradero.");
        }
    }

    @FXML
    private void onGenerarReporte() {
        if (rutaActual == null) {
            statusLabel.setText("No hay ruta seleccionada.");
            return;
        }

        try {
            List<Paradero> paraderos = paraderoRepository.findByRuta(rutaActual.getIdRuta());
            List<Map<String, Object>> datos = ReportDTO.paraderosToMap(paraderos);
            reportService.generarReporteParaderos(datos, rutaActual.getNombreRuta());
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
            Stage stage = (Stage) paraderoTable.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Gestión de Rutas");
        } catch (IOException e) {
            e.printStackTrace();
            statusLabel.setText("Error al volver.");
        }
    }

    private void cargarParaderos() {
        if (rutaActual != null) {
            List<Paradero> lista = paraderoRepository.findByRuta(rutaActual.getIdRuta());
            paraderosObservable.setAll(lista);
        }
    }
}

