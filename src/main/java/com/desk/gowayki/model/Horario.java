package com.desk.gowayki.model;

import java.time.LocalTime;

/**
 * Entidad que representa la tabla "horario".
 */
public class Horario {

    private int idHorario;
    private int idRuta;
    private LocalTime horaSalida;
    private LocalTime horaLlegada;

    public Horario() {
    }

    public Horario(int idHorario, int idRuta, LocalTime horaSalida, LocalTime horaLlegada) {
        this.idHorario = idHorario;
        this.idRuta = idRuta;
        this.horaSalida = horaSalida;
        this.horaLlegada = horaLlegada;
    }

    public int getIdHorario() {
        return idHorario;
    }

    public void setIdHorario(int idHorario) {
        this.idHorario = idHorario;
    }

    public int getIdRuta() {
        return idRuta;
    }

    public void setIdRuta(int idRuta) {
        this.idRuta = idRuta;
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
}

