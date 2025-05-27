package org.example.dao;

import org.example.domain.Carro;
import org.example.domain.Moto;

import java.sql.SQLException;

public interface VeiculoDAO {

    Carro cadastrarCarro(Carro carro) throws SQLException;
    Moto cadastrarMoto(Moto moto) throws SQLException;
}
