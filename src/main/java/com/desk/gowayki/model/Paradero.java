package com.desk.gowayki.model;

/**
 * Entidad que representa la tabla "paradero".
 */
public class Paradero {

    private int idParadero;
    private int idRuta;
    private String nombreParadero;
    private int orden;

    public Paradero() {
    }

    public Paradero(int idParadero, int idRuta, String nombreParadero, int orden) {
        this.idParadero = idParadero;
        this.idRuta = idRuta;
        this.nombreParadero = nombreParadero;
        this.orden = orden;
    }

    public int getIdParadero() {
        return idParadero;
    }

    public void setIdParadero(int idParadero) {
        this.idParadero = idParadero;
    }

    public int getIdRuta() {
        return idRuta;
    }

    public void setIdRuta(int idRuta) {
        this.idRuta = idRuta;
    }

    public String getNombreParadero() {
        return nombreParadero;
    }

    public void setNombreParadero(String nombreParadero) {
        this.nombreParadero = nombreParadero;
    }

    public int getOrden() {
        return orden;
    }

    public void setOrden(int orden) {
        this.orden = orden;
    }
}

