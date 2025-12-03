package com.desk.gowayki.model.repository;

import com.desk.gowayki.model.RutaBusqueda;

import java.time.LocalTime;
import java.util.List;

public interface RutaBusquedaRepository {

    List<RutaBusqueda> buscarRutas(String origen, String destino, LocalTime hora);
}

