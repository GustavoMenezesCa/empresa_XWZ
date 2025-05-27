package org.example.dao.impl;


import org.example.dao.VeiculoDAO;
import org.example.domain.Carro;
import org.springframework.context.annotation.Configuration;


import java.sql.*;

@Configuration
public class VeiculoDAOImpl implements VeiculoDAO {
    private static Connection conn = null;

    private Connection getConnection() throws SQLException {

        String conexao = "jdbc:postgresql://localhost:5432/postgres";
        String usuario = "postgres";
        String senha = "12345";

        return DriverManager.getConnection(conexao, usuario, senha);
    }
    @Override
    public Carro cadastrarCarro(Carro carro) throws SQLException {
        String sqlVeiculo = "INSERT INTO VEICULO(modelo, fabricante, ano, preco, tipo_veiculo) VALUES (?,?,?,?, 'CARRO');";
        String sqlCarro = "INSERT INTO CARRO (id_veiculo, quantidade_portas, tipo_combustivel) VALUES (?,?,?);";

        try (Connection conn = getConnection()) {
            conn.setAutoCommit(false);

            try (
                    PreparedStatement pstmtVeiculo = conn.prepareStatement(sqlVeiculo, Statement.RETURN_GENERATED_KEYS);
            ) {
                pstmtVeiculo.setString(1, carro.getModelo());
                pstmtVeiculo.setString(2, carro.getFabricante());
                pstmtVeiculo.setInt(3, carro.getAno());
                pstmtVeiculo.setDouble(4, carro.getPreco());
                pstmtVeiculo.executeUpdate();

                try (ResultSet generatedKeys = pstmtVeiculo.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        carro.setId(generatedKeys.getLong(1));
                    } else {
                        conn.rollback();
                        throw new SQLException("Falha ao obter o ID do veículo gerado.");
                    }
                }
            }

            try (PreparedStatement pstmtCarro = conn.prepareStatement(sqlCarro)) {
                pstmtCarro.setLong(1, carro.getId());
                pstmtCarro.setInt(2, carro.getQuantPortas());
                pstmtCarro.setString(3, carro.getTipCombustivel().toString());

                int affectedRowsCarro = pstmtCarro.executeUpdate();
                if (affectedRowsCarro == 0) {
                    conn.rollback();
                    throw new SQLException("Falha ao inserir na tabela Carro, nenhuma linha inserida");
                }
            }

            conn.commit();
            return carro;

        } catch (SQLException e) {
            throw new SQLException("Erro ao cadastrar carro: " + e.getMessage(), e);
        }
    }

}