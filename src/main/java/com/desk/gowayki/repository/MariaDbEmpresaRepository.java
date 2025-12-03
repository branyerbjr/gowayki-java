package com.desk.gowayki.repository;

import com.desk.gowayki.model.Empresa;
import com.desk.gowayki.model.repository.EmpresaRepository;
import com.desk.gowayki.service.DatabaseService;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Implementación JDBC de EmpresaRepository usando MariaDB.
 */
public class MariaDbEmpresaRepository implements EmpresaRepository {

    private final DatabaseService databaseService;

    public MariaDbEmpresaRepository(DatabaseService databaseService) {
        this.databaseService = databaseService;
    }

    @Override
    public List<Empresa> findAll() {
        List<Empresa> empresas = new ArrayList<>();
        String sql = "SELECT id_empresa, nombre, ruc, telefono FROM empresa";

        try (Connection connection = databaseService.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet rs = statement.executeQuery()) {

            while (rs.next()) {
                Empresa empresa = mapRowToEmpresa(rs);
                empresas.add(empresa);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return empresas;
    }

    @Override
    public Optional<Empresa> findById(int idEmpresa) {
        String sql = "SELECT id_empresa, nombre, ruc, telefono FROM empresa WHERE id_empresa = ?";

        try (Connection connection = databaseService.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, idEmpresa);
            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    Empresa empresa = mapRowToEmpresa(rs);
                    return Optional.of(empresa);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return Optional.empty();
    }

    @Override
    public Empresa save(Empresa empresa) {
        if (empresa.getIdEmpresa() == 0) {
            return insert(empresa);
        } else {
            return update(empresa);
        }
    }

    private Empresa insert(Empresa empresa) {
        String sql = "INSERT INTO empresa (nombre, ruc, telefono) VALUES (?, ?, ?)";

        try (Connection connection = databaseService.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            statement.setString(1, empresa.getNombre());
            statement.setString(2, empresa.getRuc());
            statement.setString(3, empresa.getTelefono());
            statement.executeUpdate();

            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    empresa.setIdEmpresa(generatedKeys.getInt(1));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return empresa;
    }

    private Empresa update(Empresa empresa) {
        String sql = "UPDATE empresa SET nombre = ?, ruc = ?, telefono = ? WHERE id_empresa = ?";

        try (Connection connection = databaseService.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, empresa.getNombre());
            statement.setString(2, empresa.getRuc());
            statement.setString(3, empresa.getTelefono());
            statement.setInt(4, empresa.getIdEmpresa());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return empresa;
    }

    private Empresa mapRowToEmpresa(ResultSet rs) throws SQLException {
        Empresa empresa = new Empresa();
        empresa.setIdEmpresa(rs.getInt("id_empresa"));
        empresa.setNombre(rs.getString("nombre"));
        empresa.setRuc(rs.getString("ruc"));
        empresa.setTelefono(rs.getString("telefono"));
        return empresa;
    }
}

