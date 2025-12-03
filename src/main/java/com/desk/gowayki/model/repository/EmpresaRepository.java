package com.desk.gowayki.model.repository;

import com.desk.gowayki.model.Empresa;

import java.util.List;
import java.util.Optional;

/**
 * Contrato de acceso a datos para la entidad Empresa.
 * No sabe nada de JDBC ni de MariaDB, solo del dominio.
 */
public interface EmpresaRepository {

    List<Empresa> findAll();

    Optional<Empresa> findById(int idEmpresa);

    Empresa save(Empresa empresa);
}

