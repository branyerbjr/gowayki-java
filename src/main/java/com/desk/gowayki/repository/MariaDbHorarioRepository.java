package com.desk.gowayki.repository;

import com.desk.gowayki.model.Horario;
import com.desk.gowayki.model.repository.HorarioRepository;
import com.desk.gowayki.service.DatabaseService;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Time;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class MariaDbHorarioRepository implements HorarioRepository {

    private final DatabaseService databaseService;

    public MariaDbHorarioRepository(DatabaseService databaseService) {
        this.databaseService = databaseService;
    }

    @Override
    public List<Horario> findByRuta(int idRuta) {
        List<Horario> horarios = new ArrayList<>();
        String sql = "SELECT id_horario, id_ruta, hora_salida, hora_llegada FROM horario WHERE id_ruta = ? ORDER BY hora_salida";

        try (Connection connection = databaseService.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, idRuta);
            try (ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    horarios.add(mapRowToHorario(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return horarios;
    }

    @Override
    public Horario save(Horario horario) {
        if (horario.getIdHorario() == 0) {
            return insert(horario);
        } else {
            return update(horario);
        }
    }

    private Horario insert(Horario horario) {
        String sql = "INSERT INTO horario (id_ruta, hora_salida, hora_llegada) VALUES (?, ?, ?)";

        try (Connection connection = databaseService.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            statement.setInt(1, horario.getIdRuta());
            statement.setTime(2, Time.valueOf(horario.getHoraSalida()));
            statement.setTime(3, Time.valueOf(horario.getHoraLlegada()));
            statement.executeUpdate();

            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    horario.setIdHorario(generatedKeys.getInt(1));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return horario;
    }

    private Horario update(Horario horario) {
        String sql = "UPDATE horario SET id_ruta = ?, hora_salida = ?, hora_llegada = ? WHERE id_horario = ?";

        try (Connection connection = databaseService.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, horario.getIdRuta());
            statement.setTime(2, Time.valueOf(horario.getHoraSalida()));
            statement.setTime(3, Time.valueOf(horario.getHoraLlegada()));
            statement.setInt(4, horario.getIdHorario());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return horario;
    }

    @Override
    public void delete(int idHorario) {
        String sql = "DELETE FROM horario WHERE id_horario = ?";

        try (Connection connection = databaseService.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, idHorario);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private Horario mapRowToHorario(ResultSet rs) throws SQLException {
        Horario horario = new Horario();
        horario.setIdHorario(rs.getInt("id_horario"));
        horario.setIdRuta(rs.getInt("id_ruta"));
        
        Time horaSalida = rs.getTime("hora_salida");
        if (horaSalida != null) {
            horario.setHoraSalida(horaSalida.toLocalTime());
        }
        
        Time horaLlegada = rs.getTime("hora_llegada");
        if (horaLlegada != null) {
            horario.setHoraLlegada(horaLlegada.toLocalTime());
        }
        
        return horario;
    }
}

