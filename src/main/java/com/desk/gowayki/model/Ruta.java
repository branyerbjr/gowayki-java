package com.desk.gowayki.model;

/**
 * Entidad que representa la tabla "ruta".
 */
public class Ruta {

    private int idRuta;
    private int idEmpresa;
    private String nombreRuta;
    private String origen;
    private String destino;

    public Ruta() {
    }

    public Ruta(int idRuta, int idEmpresa, String nombreRuta, String origen, String destino) {
        this.idRuta = idRuta;
        this.idEmpresa = idEmpresa;
        this.nombreRuta = nombreRuta;
        this.origen = origen;
        this.destino = destino;
    }

    public int getIdRuta() {
        return idRuta;
    }

    public void setIdRuta(int idRuta) {
        this.idRuta = idRuta;
    }

    public int getIdEmpresa() {
        return idEmpresa;
    }

    public void setIdEmpresa(int idEmpresa) {
        this.idEmpresa = idEmpresa;
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
}

