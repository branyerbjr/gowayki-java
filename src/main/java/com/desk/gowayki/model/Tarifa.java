package com.desk.gowayki.model;

import java.math.BigDecimal;
import java.time.LocalTime;

/**
 * Entidad que representa la tabla "tarifa".
 */
public class Tarifa {

    private int idTarifa;
    private int idRuta;
    private BigDecimal tarifaDia;
    private BigDecimal tarifaNoche;
    private LocalTime horaInicioNoche;
    private LocalTime horaFinNoche;

    public Tarifa() {
    }

    public Tarifa(int idTarifa, int idRuta, BigDecimal tarifaDia, BigDecimal tarifaNoche,
                  LocalTime horaInicioNoche, LocalTime horaFinNoche) {
        this.idTarifa = idTarifa;
        this.idRuta = idRuta;
        this.tarifaDia = tarifaDia;
        this.tarifaNoche = tarifaNoche;
        this.horaInicioNoche = horaInicioNoche;
        this.horaFinNoche = horaFinNoche;
    }

    public int getIdTarifa() {
        return idTarifa;
    }

    public void setIdTarifa(int idTarifa) {
        this.idTarifa = idTarifa;
    }

    public int getIdRuta() {
        return idRuta;
    }

    public void setIdRuta(int idRuta) {
        this.idRuta = idRuta;
    }

    public BigDecimal getTarifaDia() {
        return tarifaDia;
    }

    public void setTarifaDia(BigDecimal tarifaDia) {
        this.tarifaDia = tarifaDia;
    }

    public BigDecimal getTarifaNoche() {
        return tarifaNoche;
    }

    public void setTarifaNoche(BigDecimal tarifaNoche) {
        this.tarifaNoche = tarifaNoche;
    }

    public LocalTime getHoraInicioNoche() {
        return horaInicioNoche;
    }

    public void setHoraInicioNoche(LocalTime horaInicioNoche) {
        this.horaInicioNoche = horaInicioNoche;
    }

    public LocalTime getHoraFinNoche() {
        return horaFinNoche;
    }

    public void setHoraFinNoche(LocalTime horaFinNoche) {
        this.horaFinNoche = horaFinNoche;
    }
}

