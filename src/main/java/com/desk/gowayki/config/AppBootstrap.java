package com.desk.gowayki.config;

import com.desk.gowayki.model.repository.*;
import com.desk.gowayki.repository.*;
import com.desk.gowayki.service.DatabaseService;
import com.desk.gowayki.service.ReportService;

/**
 * Archivo único que orquesta la inicialización de la aplicación.
 * Crea servicios de infraestructura, repositorios y arma el contexto.
 */
public class AppBootstrap {

    private static AppContext appContext;

    private AppBootstrap() {
        // evitar instanciación
    }

    /**
     * Inicializa el contexto de la aplicación si aún no está creado.
     */
    public static synchronized AppContext init() {
        if (appContext == null) {
            // TODO: en el futuro leer desde .env, properties, etc.
            String url = "jdbc:mariadb://localhost:3306/gowayki_db";
            String username = "gowayki_user";
            String password = "gowayki_pass";

            DatabaseService databaseService = new DatabaseService(url, username, password);

            // Repositorios
            EmpresaRepository empresaRepository = new MariaDbEmpresaRepository(databaseService);
            RutaRepository rutaRepository = new MariaDbRutaRepository(databaseService);
            ParaderoRepository paraderoRepository = new MariaDbParaderoRepository(databaseService);
            HorarioRepository horarioRepository = new MariaDbHorarioRepository(databaseService);
            TarifaRepository tarifaRepository = new MariaDbTarifaRepository(databaseService);
            RutaBusquedaRepository rutaBusquedaRepository = new MariaDbRutaBusquedaRepository(databaseService);
            ReportService reportService = new ReportService();

            appContext = new AppContext(
                    databaseService,
                    empresaRepository,
                    rutaRepository,
                    paraderoRepository,
                    horarioRepository,
                    tarifaRepository,
                    rutaBusquedaRepository,
                    reportService
            );

            // Test simple de conexión al iniciar (puedes quitarlo luego)
            boolean ok = databaseService.testConnection();
            System.out.println("MariaDB connection test: " + (ok ? "OK" : "FAILED"));
        }
        return appContext;
    }

    /**
     * Obtiene el contexto ya inicializado.
     */
    public static AppContext getContext() {
        if (appContext == null) {
            throw new IllegalStateException("AppBootstrap no ha sido inicializado. Llama a AppBootstrap.init() primero.");
        }
        return appContext;
    }
}

