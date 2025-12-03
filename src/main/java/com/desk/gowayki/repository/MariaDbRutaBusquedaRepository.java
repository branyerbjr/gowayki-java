package com.desk.gowayki.repository;

import com.desk.gowayki.model.RutaBusqueda;
import com.desk.gowayki.model.repository.RutaBusquedaRepository;
import com.desk.gowayki.service.DatabaseService;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class MariaDbRutaBusquedaRepository implements RutaBusquedaRepository {

    private final DatabaseService databaseService;

    public MariaDbRutaBusquedaRepository(DatabaseService databaseService) {
        this.databaseService = databaseService;
    }

    @Override
    public List<RutaBusqueda> buscarRutas(String origen, String destino, LocalTime hora) {
        List<RutaBusqueda> resultados = new ArrayList<>();
        
        // Si no se proporciona hora, usar hora actual
        if (hora == null) {
            hora = LocalTime.now();
        }

        String sql = "{CALL sp_buscar_rutas(?, ?, ?)}";

        try (Connection connection = databaseService.getConnection();
             PreparedStatement statement = connection.prepareCall(sql)) {

            statement.setString(1, origen);
            statement.setString(2, destino);
            statement.setTime(3, Time.valueOf(hora));

            try (ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    resultados.add(mapRowToRutaBusqueda(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return resultados;
    }

    private RutaBusqueda mapRowToRutaBusqueda(ResultSet rs) throws SQLException {
        RutaBusqueda ruta = new RutaBusqueda();
        ruta.setIdRuta(rs.getInt("id_ruta"));
        ruta.setNombreRuta(rs.getString("nombre_ruta"));
        ruta.setOrigen(rs.getString("origen"));
        ruta.setDestino(rs.getString("destino"));
        
        Time horaSalida = rs.getTime("hora_salida");
        if (horaSalida != null) {
            ruta.setHoraSalida(horaSalida.toLocalTime());
        }
        
        Time horaLlegada = rs.getTime("hora_llegada");
        if (horaLlegada != null) {
            ruta.setHoraLlegada(horaLlegada.toLocalTime());
        }
        
        BigDecimal tarifa = rs.getBigDecimal("tarifa_aplicable");
        if (tarifa != null) {
            ruta.setTarifaAplicable(tarifa);
        }
        
        ruta.setEmpresa(rs.getString("empresa"));
        return ruta;
    }
}

