package com.desk.gowayki.repository;

import com.desk.gowayki.model.Ruta;
import com.desk.gowayki.model.repository.RutaRepository;
import com.desk.gowayki.service.DatabaseService;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class MariaDbRutaRepository implements RutaRepository {

    private final DatabaseService databaseService;

    public MariaDbRutaRepository(DatabaseService databaseService) {
        this.databaseService = databaseService;
    }

    @Override
    public List<Ruta> findAll() {
        List<Ruta> rutas = new ArrayList<>();
        String sql = "SELECT id_ruta, id_empresa, nombre_ruta, origen, destino FROM ruta";

        try (Connection connection = databaseService.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet rs = statement.executeQuery()) {

            while (rs.next()) {
                rutas.add(mapRowToRuta(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return rutas;
    }

    @Override
    public List<Ruta> findByEmpresa(int idEmpresa) {
        List<Ruta> rutas = new ArrayList<>();
        String sql = "SELECT id_ruta, id_empresa, nombre_ruta, origen, destino FROM ruta WHERE id_empresa = ?";

        try (Connection connection = databaseService.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, idEmpresa);
            try (ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    rutas.add(mapRowToRuta(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return rutas;
    }

    @Override
    public Optional<Ruta> findById(int idRuta) {
        String sql = "SELECT id_ruta, id_empresa, nombre_ruta, origen, destino FROM ruta WHERE id_ruta = ?";

        try (Connection connection = databaseService.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, idRuta);
            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapRowToRuta(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return Optional.empty();
    }

    @Override
    public Ruta save(Ruta ruta) {
        if (ruta.getIdRuta() == 0) {
            return insert(ruta);
        } else {
            return update(ruta);
        }
    }

    private Ruta insert(Ruta ruta) {
        String sql = "INSERT INTO ruta (id_empresa, nombre_ruta, origen, destino) VALUES (?, ?, ?, ?)";

        try (Connection connection = databaseService.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            statement.setInt(1, ruta.getIdEmpresa());
            statement.setString(2, ruta.getNombreRuta());
            statement.setString(3, ruta.getOrigen());
            statement.setString(4, ruta.getDestino());
            statement.executeUpdate();

            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    ruta.setIdRuta(generatedKeys.getInt(1));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return ruta;
    }

    private Ruta update(Ruta ruta) {
        String sql = "UPDATE ruta SET id_empresa = ?, nombre_ruta = ?, origen = ?, destino = ? WHERE id_ruta = ?";

        try (Connection connection = databaseService.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, ruta.getIdEmpresa());
            statement.setString(2, ruta.getNombreRuta());
            statement.setString(3, ruta.getOrigen());
            statement.setString(4, ruta.getDestino());
            statement.setInt(5, ruta.getIdRuta());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return ruta;
    }

    @Override
    public void delete(int idRuta) {
        String sql = "DELETE FROM ruta WHERE id_ruta = ?";

        try (Connection connection = databaseService.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, idRuta);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private Ruta mapRowToRuta(ResultSet rs) throws SQLException {
        Ruta ruta = new Ruta();
        ruta.setIdRuta(rs.getInt("id_ruta"));
        ruta.setIdEmpresa(rs.getInt("id_empresa"));
        ruta.setNombreRuta(rs.getString("nombre_ruta"));
        ruta.setOrigen(rs.getString("origen"));
        ruta.setDestino(rs.getString("destino"));
        return ruta;
    }
}

