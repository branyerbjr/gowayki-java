package com.desk.gowayki.model.repository;

import com.desk.gowayki.model.Ruta;

import java.util.List;
import java.util.Optional;

public interface RutaRepository {

    List<Ruta> findAll();

    List<Ruta> findByEmpresa(int idEmpresa);

    Optional<Ruta> findById(int idRuta);

    Ruta save(Ruta ruta);

    void delete(int idRuta);
}

