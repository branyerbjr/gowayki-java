package com.desk.gowayki.repository;

import com.desk.gowayki.model.Tarifa;
import com.desk.gowayki.model.repository.TarifaRepository;
import com.desk.gowayki.service.DatabaseService;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Time;
import java.time.LocalTime;
import java.util.Optional;

public class MariaDbTarifaRepository implements TarifaRepository {

    private final DatabaseService databaseService;

    public MariaDbTarifaRepository(DatabaseService databaseService) {
        this.databaseService = databaseService;
    }

    @Override
    public Optional<Tarifa> findByRuta(int idRuta) {
        String sql = "SELECT id_tarifa, id_ruta, tarifa_dia, tarifa_noche, hora_inicio_noche, hora_fin_noche FROM tarifa WHERE id_ruta = ?";

        try (Connection connection = databaseService.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, idRuta);
            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapRowToTarifa(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return Optional.empty();
    }

    @Override
    public Tarifa save(Tarifa tarifa) {
        if (tarifa.getIdTarifa() == 0) {
            return insert(tarifa);
        } else {
            return update(tarifa);
        }
    }

    private Tarifa insert(Tarifa tarifa) {
        String sql = "INSERT INTO tarifa (id_ruta, tarifa_dia, tarifa_noche, hora_inicio_noche, hora_fin_noche) VALUES (?, ?, ?, ?, ?)";

        try (Connection connection = databaseService.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            statement.setInt(1, tarifa.getIdRuta());
            statement.setBigDecimal(2, tarifa.getTarifaDia());
            statement.setBigDecimal(3, tarifa.getTarifaNoche());
            statement.setTime(4, Time.valueOf(tarifa.getHoraInicioNoche()));
            statement.setTime(5, Time.valueOf(tarifa.getHoraFinNoche()));
            statement.executeUpdate();

            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    tarifa.setIdTarifa(generatedKeys.getInt(1));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return tarifa;
    }

    private Tarifa update(Tarifa tarifa) {
        String sql = "UPDATE tarifa SET id_ruta = ?, tarifa_dia = ?, tarifa_noche = ?, hora_inicio_noche = ?, hora_fin_noche = ? WHERE id_tarifa = ?";

        try (Connection connection = databaseService.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, tarifa.getIdRuta());
            statement.setBigDecimal(2, tarifa.getTarifaDia());
            statement.setBigDecimal(3, tarifa.getTarifaNoche());
            statement.setTime(4, Time.valueOf(tarifa.getHoraInicioNoche()));
            statement.setTime(5, Time.valueOf(tarifa.getHoraFinNoche()));
            statement.setInt(6, tarifa.getIdTarifa());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return tarifa;
    }

    @Override
    public void delete(int idTarifa) {
        String sql = "DELETE FROM tarifa WHERE id_tarifa = ?";

        try (Connection connection = databaseService.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, idTarifa);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private Tarifa mapRowToTarifa(ResultSet rs) throws SQLException {
        Tarifa tarifa = new Tarifa();
        tarifa.setIdTarifa(rs.getInt("id_tarifa"));
        tarifa.setIdRuta(rs.getInt("id_ruta"));
        tarifa.setTarifaDia(rs.getBigDecimal("tarifa_dia"));
        tarifa.setTarifaNoche(rs.getBigDecimal("tarifa_noche"));
        
        Time horaInicio = rs.getTime("hora_inicio_noche");
        if (horaInicio != null) {
            tarifa.setHoraInicioNoche(horaInicio.toLocalTime());
        }
        
        Time horaFin = rs.getTime("hora_fin_noche");
        if (horaFin != null) {
            tarifa.setHoraFinNoche(horaFin.toLocalTime());
        }
        
        return tarifa;
    }
}

