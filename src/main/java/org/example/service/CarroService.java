package org.example.service;

import org.example.dao.VeiculoDAO;
import org.example.domain.Carro;
import org.example.web.dto.CarroCadastroForm;
import org.springframework.stereotype.Service;

import java.sql.SQLException;


@Service
public class CarroService {

    private final VeiculoDAO veiculoDAO;



    public CarroService(VeiculoDAO veiculoDAO){
        this.veiculoDAO = veiculoDAO;
    }

    public Carro cadastraCarro(CarroCadastroForm carroCadastroForm){
        if (carroCadastroForm.modelo() == null || carroCadastroForm.fabricante() == null || carroCadastroForm.ano() == null ||
                carroCadastroForm.preco() == null || carroCadastroForm.quantidadePortas() == null || carroCadastroForm.tipoCombustivel() == null) {
            throw new IllegalArgumentException("Todos os campos são obrigatórios para cadastro de um carro.");
        }
        Carro carro = Carro.fromDto(carroCadastroForm);
        try {
            veiculoDAO.cadastrarCarro(carro);
            return carro;
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao salvar o carro no banco de dados.", e);
        }
    }

}
