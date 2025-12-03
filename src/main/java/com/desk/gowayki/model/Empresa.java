package com.desk.gowayki.model;

/**
 * Entidad de dominio que representa la tabla "empresa".
 */
public class Empresa {

    private int idEmpresa;
    private String nombre;
    private String ruc;
    private String telefono;

    public Empresa() {
    }

    public Empresa(int idEmpresa, String nombre, String ruc, String telefono) {
        this.idEmpresa = idEmpresa;
        this.nombre = nombre;
        this.ruc = ruc;
        this.telefono = telefono;
    }

    public int getIdEmpresa() {
        return idEmpresa;
    }

    public void setIdEmpresa(int idEmpresa) {
        this.idEmpresa = idEmpresa;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getRuc() {
        return ruc;
    }

    public void setRuc(String ruc) {
        this.ruc = ruc;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }
}

