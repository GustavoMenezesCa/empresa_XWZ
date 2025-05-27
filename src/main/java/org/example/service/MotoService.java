package org.example.service;

import org.example.dao.VeiculoDAO;
import org.example.domain.Moto;
import org.example.web.dto.MotoCadastroForm;
import org.springframework.stereotype.Service;

import java.sql.SQLException;

@Service
public class MotoService {

    private final VeiculoDAO veiculoDAO;

    public MotoService(VeiculoDAO veiculoDAO){
        this.veiculoDAO = veiculoDAO;
    }

    public Moto cadastraMoto(MotoCadastroForm motoCadastroForm){
        System.out.println(motoCadastroForm);
        if (motoCadastroForm.modelo() == null || motoCadastroForm.fabricante() == null || motoCadastroForm.ano() == null ||
                motoCadastroForm.preco() == null || motoCadastroForm.cilindradas() == null) {
            throw new IllegalArgumentException("Todos os campos são obrigatórios para cadastro de uma moto.");
        }
        Moto moto = Moto.fromDto(motoCadastroForm);
        try {
            veiculoDAO.cadastrarMoto(moto);
            return moto;
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao salvar a moto no banco de dados.", e);
        }
    }


}
