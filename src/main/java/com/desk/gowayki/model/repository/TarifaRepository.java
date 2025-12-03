package com.desk.gowayki.model.repository;

import com.desk.gowayki.model.Tarifa;

import java.util.Optional;

public interface TarifaRepository {

    Optional<Tarifa> findByRuta(int idRuta);

    Tarifa save(Tarifa tarifa);

    void delete(int idTarifa);
}

