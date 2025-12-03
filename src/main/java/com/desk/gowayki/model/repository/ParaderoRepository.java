package com.desk.gowayki.model.repository;

import com.desk.gowayki.model.Paradero;

import java.util.List;

public interface ParaderoRepository {

    List<Paradero> findByRuta(int idRuta);

    Paradero save(Paradero paradero);

    void delete(int idParadero);
}

