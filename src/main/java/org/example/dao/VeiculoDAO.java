package org.example.dao;

import org.example.domain.Carro;

import java.sql.SQLException;

public interface VeiculoDAO {

    Carro cadastrarCarro(Carro carro) throws SQLException;
}
