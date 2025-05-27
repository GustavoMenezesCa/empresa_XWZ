package org.example.dao;

import org.example.domain.Carro;

import java.sql.SQLException;
import java.util.List;

public interface CarroDAO {

    Carro cadastrarCarro(Carro carro) throws SQLException;
    List<Carro> consultarCarros(String modeloFiltro, String corFiltro, Integer anoFiltro) throws SQLException;
}
