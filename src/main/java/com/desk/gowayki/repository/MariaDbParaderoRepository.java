package com.desk.gowayki.repository;

import com.desk.gowayki.model.Paradero;
import com.desk.gowayki.model.repository.ParaderoRepository;
import com.desk.gowayki.service.DatabaseService;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class MariaDbParaderoRepository implements ParaderoRepository {

    private final DatabaseService databaseService;

    public MariaDbParaderoRepository(DatabaseService databaseService) {
        this.databaseService = databaseService;
    }

    @Override
    public List<Paradero> findByRuta(int idRuta) {
        List<Paradero> paraderos = new ArrayList<>();
        String sql = "SELECT id_paradero, id_ruta, nombre_paradero, orden FROM paradero WHERE id_ruta = ? ORDER BY orden";

        try (Connection connection = databaseService.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, idRuta);
            try (ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    paraderos.add(mapRowToParadero(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return paraderos;
    }

    @Override
    public Paradero save(Paradero paradero) {
        if (paradero.getIdParadero() == 0) {
            return insert(paradero);
        } else {
            return update(paradero);
        }
    }

    private Paradero insert(Paradero paradero) {
        String sql = "INSERT INTO paradero (id_ruta, nombre_paradero, orden) VALUES (?, ?, ?)";

        try (Connection connection = databaseService.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            statement.setInt(1, paradero.getIdRuta());
            statement.setString(2, paradero.getNombreParadero());
            statement.setInt(3, paradero.getOrden());
            statement.executeUpdate();

            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    paradero.setIdParadero(generatedKeys.getInt(1));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return paradero;
    }

    private Paradero update(Paradero paradero) {
        String sql = "UPDATE paradero SET id_ruta = ?, nombre_paradero = ?, orden = ? WHERE id_paradero = ?";

        try (Connection connection = databaseService.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, paradero.getIdRuta());
            statement.setString(2, paradero.getNombreParadero());
            statement.setInt(3, paradero.getOrden());
            statement.setInt(4, paradero.getIdParadero());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return paradero;
    }

    @Override
    public void delete(int idParadero) {
        String sql = "DELETE FROM paradero WHERE id_paradero = ?";

        try (Connection connection = databaseService.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, idParadero);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private Paradero mapRowToParadero(ResultSet rs) throws SQLException {
        Paradero paradero = new Paradero();
        paradero.setIdParadero(rs.getInt("id_paradero"));
        paradero.setIdRuta(rs.getInt("id_ruta"));
        paradero.setNombreParadero(rs.getString("nombre_paradero"));
        paradero.setOrden(rs.getInt("orden"));
        return paradero;
    }
}

