package com.desk.gowayki.service;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Servicio genérico para gestionar la conexión a MariaDB.
 * Centraliza la URL, credenciales y la obtención de conexiones JDBC.
 */
public class DatabaseService {

    private final String url;
    private final String username;
    private final String password;

    public DatabaseService(String url, String username, String password) {
        this.url = url;
        this.username = username;
        this.password = password;
    }

    /**
     * Obtiene una nueva conexión JDBC a MariaDB.
     */
    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url, username, password);
    }

    /**
     * Verifica que la conexión funcione correctamente.
     */
    public boolean testConnection() {
        try (Connection ignored = getConnection()) {
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}

