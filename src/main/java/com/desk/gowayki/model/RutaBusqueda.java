package com.desk.gowayki.model;

import java.math.BigDecimal;
import java.time.LocalTime;

/**
 * DTO para resultados de búsqueda de rutas (resultado del stored procedure).
 */
public class RutaBusqueda {

    private int idRuta;
    private String nombreRuta;
    private String origen;
    private String destino;
    private LocalTime horaSalida;
    private LocalTime horaLlegada;
    private BigDecimal tarifaAplicable;
    private String empresa;

    public RutaBusqueda() {
    }

    public int getIdRuta() {
        return idRuta;
    }

    public void setIdRuta(int idRuta) {
        this.idRuta = idRuta;
    }

    public String getNombreRuta() {
        return nombreRuta;
    }

    public void setNombreRuta(String nombreRuta) {
        this.nombreRuta = nombreRuta;
    }

    public String getOrigen() {
        return origen;
    }

    public void setOrigen(String origen) {
        this.origen = origen;
    }

    public String getDestino() {
        return destino;
    }

    public void setDestino(String destino) {
        this.destino = destino;
    }

    public LocalTime getHoraSalida() {
        return horaSalida;
    }

    public void setHoraSalida(LocalTime horaSalida) {
        this.horaSalida = horaSalida;
    }

    public LocalTime getHoraLlegada() {
        return horaLlegada;
    }

    public void setHoraLlegada(LocalTime horaLlegada) {
        this.horaLlegada = horaLlegada;
    }

    public BigDecimal getTarifaAplicable() {
        return tarifaAplicable;
    }

    public void setTarifaAplicable(BigDecimal tarifaAplicable) {
        this.tarifaAplicable = tarifaAplicable;
    }

    public String getEmpresa() {
        return empresa;
    }

    public void setEmpresa(String empresa) {
        this.empresa = empresa;
    }
}

