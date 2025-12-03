package com.desk.gowayki.model.repository;

import com.desk.gowayki.model.Horario;

import java.util.List;

public interface HorarioRepository {

    List<Horario> findByRuta(int idRuta);

    Horario save(Horario horario);

    void delete(int idHorario);
}

