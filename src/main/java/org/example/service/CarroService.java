package org.example.service;

import org.example.dao.CarroDAO;
import org.example.dao.VeiculoDAO;
import org.example.domain.Carro;
import org.example.web.dto.CarroCadastroForm;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.List;


@Service
public class CarroService {

    private final CarroDAO carroDAO;



    public CarroService(CarroDAO carroDAO){
        this.carroDAO = carroDAO;
    }

    public Carro cadastraCarro(CarroCadastroForm carroCadastroForm){
        if (carroCadastroForm.modelo() == null || carroCadastroForm.fabricante() == null || carroCadastroForm.ano() == null ||
                carroCadastroForm.preco() == null || carroCadastroForm.quantidadePortas() == null || carroCadastroForm.tipoCombustivel() == null) {
            throw new IllegalArgumentException("Todos os campos são obrigatórios para cadastro de um carro.");
        }
        Carro carro = Carro.fromDto(carroCadastroForm);
        try {
            carroDAO.cadastrarCarro(carro);
            return carro;
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao salvar o carro no banco de dados.", e);
        }
    }

    public List<Carro> listaCarroFiltrados(String modelo, String cor, Integer ano) throws SQLException {
        List<Carro> list = carroDAO.consultarCarros(modelo, cor, ano);
        return list;
    }

}
