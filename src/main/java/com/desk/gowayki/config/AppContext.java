package com.desk.gowayki.config;

import com.desk.gowayki.model.repository.*;
import com.desk.gowayki.service.DatabaseService;
import com.desk.gowayki.service.ReportService;

/**
 * Contenedor simple de dependencias de la aplicación.
 * Aquí se exponen servicios, repositorios y casos de uso ya inicializados.
 */
public class AppContext {

    private final DatabaseService databaseService;
    private final EmpresaRepository empresaRepository;
    private final RutaRepository rutaRepository;
    private final ParaderoRepository paraderoRepository;
    private final HorarioRepository horarioRepository;
    private final TarifaRepository tarifaRepository;
    private final RutaBusquedaRepository rutaBusquedaRepository;
    private final ReportService reportService;

    public AppContext(DatabaseService databaseService,
                      EmpresaRepository empresaRepository,
                      RutaRepository rutaRepository,
                      ParaderoRepository paraderoRepository,
                      HorarioRepository horarioRepository,
                      TarifaRepository tarifaRepository,
                      RutaBusquedaRepository rutaBusquedaRepository,
                      ReportService reportService) {
        this.databaseService = databaseService;
        this.empresaRepository = empresaRepository;
        this.rutaRepository = rutaRepository;
        this.paraderoRepository = paraderoRepository;
        this.horarioRepository = horarioRepository;
        this.tarifaRepository = tarifaRepository;
        this.rutaBusquedaRepository = rutaBusquedaRepository;
        this.reportService = reportService;
    }

    public DatabaseService getDatabaseService() {
        return databaseService;
    }

    public EmpresaRepository getEmpresaRepository() {
        return empresaRepository;
    }

    public RutaRepository getRutaRepository() {
        return rutaRepository;
    }

    public ParaderoRepository getParaderoRepository() {
        return paraderoRepository;
    }

    public HorarioRepository getHorarioRepository() {
        return horarioRepository;
    }

    public TarifaRepository getTarifaRepository() {
        return tarifaRepository;
    }

    public RutaBusquedaRepository getRutaBusquedaRepository() {
        return rutaBusquedaRepository;
    }

    public ReportService getReportService() {
        return reportService;
    }
}

