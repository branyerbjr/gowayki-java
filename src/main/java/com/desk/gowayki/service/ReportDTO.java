package com.desk.gowayki.service;

import com.desk.gowayki.model.*;

import java.math.BigDecimal;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Utilidad para convertir entidades a Map para JasperReports.
 */
public class ReportDTO {

    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm");

    public static List<Map<String, Object>> empresasToMap(List<Empresa> empresas) {
        return empresas.stream().map(empresa -> {
            Map<String, Object> map = new HashMap<>();
            map.put("idEmpresa", empresa.getIdEmpresa());
            map.put("nombre", empresa.getNombre());
            map.put("ruc", empresa.getRuc() != null ? empresa.getRuc() : "");
            map.put("telefono", empresa.getTelefono() != null ? empresa.getTelefono() : "");
            return map;
        }).collect(Collectors.toList());
    }

    public static List<Map<String, Object>> rutasToMap(List<Ruta> rutas) {
        return rutas.stream().map(ruta -> {
            Map<String, Object> map = new HashMap<>();
            map.put("idRuta", ruta.getIdRuta());
            map.put("nombreRuta", ruta.getNombreRuta());
            map.put("origen", ruta.getOrigen());
            map.put("destino", ruta.getDestino());
            return map;
        }).collect(Collectors.toList());
    }

    public static List<Map<String, Object>> paraderosToMap(List<Paradero> paraderos) {
        return paraderos.stream().map(paradero -> {
            Map<String, Object> map = new HashMap<>();
            map.put("idParadero", paradero.getIdParadero());
            map.put("orden", paradero.getOrden());
            map.put("nombreParadero", paradero.getNombreParadero());
            return map;
        }).collect(Collectors.toList());
    }

    public static List<Map<String, Object>> horariosToMap(List<Horario> horarios) {
        return horarios.stream().map(horario -> {
            Map<String, Object> map = new HashMap<>();
            map.put("idHorario", horario.getIdHorario());
            map.put("horaSalida", horario.getHoraSalida() != null ? horario.getHoraSalida().format(TIME_FORMATTER) : "-");
            map.put("horaLlegada", horario.getHoraLlegada() != null ? horario.getHoraLlegada().format(TIME_FORMATTER) : "-");
            return map;
        }).collect(Collectors.toList());
    }

    public static List<Map<String, Object>> tarifasToMap(List<Tarifa> tarifas) {
        return tarifas.stream().map(tarifa -> {
            Map<String, Object> map = new HashMap<>();
            map.put("idTarifa", tarifa.getIdTarifa());
            map.put("tarifaDia", tarifa.getTarifaDia() != null ? tarifa.getTarifaDia() : BigDecimal.ZERO);
            map.put("tarifaNoche", tarifa.getTarifaNoche() != null ? tarifa.getTarifaNoche() : BigDecimal.ZERO);
            map.put("horaInicioNoche", tarifa.getHoraInicioNoche() != null ? tarifa.getHoraInicioNoche().format(TIME_FORMATTER) : "-");
            map.put("horaFinNoche", tarifa.getHoraFinNoche() != null ? tarifa.getHoraFinNoche().format(TIME_FORMATTER) : "-");
            return map;
        }).collect(Collectors.toList());
    }

    public static List<Map<String, Object>> consultasToMap(List<RutaBusqueda> consultas) {
        return consultas.stream().map(consulta -> {
            Map<String, Object> map = new HashMap<>();
            map.put("nombreRuta", consulta.getNombreRuta());
            map.put("empresa", consulta.getEmpresa() != null ? consulta.getEmpresa() : "");
            map.put("horaSalida", consulta.getHoraSalida() != null ? consulta.getHoraSalida().format(TIME_FORMATTER) : "-");
            map.put("horaLlegada", consulta.getHoraLlegada() != null ? consulta.getHoraLlegada().format(TIME_FORMATTER) : "-");
            map.put("tarifaAplicable", consulta.getTarifaAplicable() != null ? consulta.getTarifaAplicable() : BigDecimal.ZERO);
            return map;
        }).collect(Collectors.toList());
    }
}

