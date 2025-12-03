package com.desk.gowayki.service;

import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.view.JasperViewer;

import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Servicio para generar reportes PDF usando JasperReports.
 */
public class ReportService {

    private static final String REPORTS_DIR = "reports";

    /**
     * Genera un reporte PDF desde una lista de datos.
     */
    public void generarReportePDF(String nombreReporte, String titulo, List<?> datos, 
                                   Map<String, Object> parametros, String nombreArchivo) {
        try {
            // Crear directorio de reportes si no existe
            Path reportsPath = Paths.get(REPORTS_DIR);
            if (!Files.exists(reportsPath)) {
                Files.createDirectories(reportsPath);
            }

            // Cargar template JRXML
            InputStream templateStream = getClass().getResourceAsStream("/reports/" + nombreReporte + ".jrxml");
            if (templateStream == null) {
                throw new RuntimeException("Template de reporte no encontrado: " + nombreReporte + ".jrxml");
            }

            JasperDesign jasperDesign = JRXmlLoader.load(templateStream);
            JasperReport jasperReport = JasperCompileManager.compileReport(jasperDesign);

            // Preparar parámetros
            if (parametros == null) {
                parametros = new HashMap<>();
            }
            parametros.put("TITULO", titulo);

            // Crear datasource
            JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(datos);

            // Llenar reporte
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parametros, dataSource);

            // Exportar a PDF
            Path pdfPath = Paths.get(REPORTS_DIR, nombreArchivo + ".pdf");
            JasperExportManager.exportReportToPdfFile(jasperPrint, pdfPath.toString());

            System.out.println("Reporte generado: " + pdfPath.toAbsolutePath());

            // Abrir el PDF automáticamente (opcional)
            abrirPDF(pdfPath.toString());

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error al generar reporte: " + e.getMessage(), e);
        }
    }

    /**
     * Genera reporte de empresas.
     */
    public void generarReporteEmpresas(List<?> empresas) {
        Map<String, Object> params = new HashMap<>();
        generarReportePDF("empresas", "Reporte de Empresas", empresas, params, "reporte_empresas");
    }

    /**
     * Genera reporte de rutas.
     */
    public void generarReporteRutas(List<?> rutas) {
        Map<String, Object> params = new HashMap<>();
        generarReportePDF("rutas", "Reporte de Rutas", rutas, params, "reporte_rutas");
    }

    /**
     * Genera reporte de paraderos.
     */
    public void generarReporteParaderos(List<?> paraderos, String nombreRuta) {
        Map<String, Object> params = new HashMap<>();
        params.put("NOMBRE_RUTA", nombreRuta);
        generarReportePDF("paraderos", "Reporte de Paraderos - " + nombreRuta, paraderos, params, "reporte_paraderos");
    }

    /**
     * Genera reporte de horarios.
     */
    public void generarReporteHorarios(List<?> horarios, String nombreRuta) {
        Map<String, Object> params = new HashMap<>();
        params.put("NOMBRE_RUTA", nombreRuta);
        generarReportePDF("horarios", "Reporte de Horarios - " + nombreRuta, horarios, params, "reporte_horarios");
    }

    /**
     * Genera reporte de tarifas.
     */
    public void generarReporteTarifas(List<?> tarifas) {
        Map<String, Object> params = new HashMap<>();
        generarReportePDF("tarifas", "Reporte de Tarifas", tarifas, params, "reporte_tarifas");
    }

    /**
     * Genera reporte de consultas de usuario (búsqueda de rutas).
     */
    public void generarReporteConsultas(List<?> consultas, String origen, String destino) {
        Map<String, Object> params = new HashMap<>();
        params.put("ORIGEN", origen);
        params.put("DESTINO", destino);
        String titulo = "Consulta de Rutas: " + origen + " → " + destino;
        generarReportePDF("consultas", titulo, consultas, params, "reporte_consulta_" + origen + "_" + destino);
    }


    /**
     * Abre el PDF generado (Windows).
     */
    private void abrirPDF(String rutaPDF) {
        try {
            if (System.getProperty("os.name").toLowerCase().contains("win")) {
                Runtime.getRuntime().exec("cmd /c start \"\" \"" + rutaPDF + "\"");
            } else {
                // Linux/Mac
                Runtime.getRuntime().exec("xdg-open " + rutaPDF);
            }
        } catch (Exception e) {
            System.out.println("No se pudo abrir el PDF automáticamente: " + e.getMessage());
        }
    }
}

