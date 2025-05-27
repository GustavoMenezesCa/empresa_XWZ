package org.example.dao.impl;


import org.example.dao.CarroDAO;
import org.example.domain.Carro;
import org.springframework.context.annotation.Configuration;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Configuration
public class CarroDAOImpl implements CarroDAO {

    private static Connection conn = null;

    private Connection getConnection() throws SQLException {

        String conexao = "jdbc:postgresql://localhost:5432/postgres";
        String usuario = "postgres";
        String senha = "12345";

        return DriverManager.getConnection(conexao, usuario, senha);
    }


    public Carro cadastrarCarro(Carro carro) throws SQLException{

        String sqlVeiculo = "INSERT INTO VEICULO(modelo, fabricante, ano, preco, tipo_veiculo) VALUES (?,?,?,?, 'CARRO');";
        String sqlCarro = "INSERT INTO CARRO (id_veiculo, quantidade_portas, tipo_combustivel) VALUES (?,?,?);";

        Connection conn = null;
        PreparedStatement pstmtVeiculo = null;
        PreparedStatement pstmtCarro = null;
        ResultSet generatedKeys = null;

        try {
            conn = getConnection();
            conn.setAutoCommit(false);
            pstmtVeiculo = conn.prepareStatement(sqlVeiculo, Statement.RETURN_GENERATED_KEYS);
            pstmtVeiculo.setString(1, carro.getModelo());
            pstmtVeiculo.setString(2, carro.getFabricante());
            pstmtVeiculo.setInt(3, carro.getAno());
            pstmtVeiculo.setDouble(4, carro.getPreco());

            int affectedRowsVeiculo = pstmtVeiculo.executeUpdate();

            if (affectedRowsVeiculo == 0) {
                throw new SQLException("Falha ao inserir em VEICULO, nenhuma linha afetada.");
            }

            generatedKeys = pstmtVeiculo.getGeneratedKeys();
            if (generatedKeys.next()) {
                carro.setId(generatedKeys.getLong(1));
            } else {
                conn.rollback();
                throw new SQLException("Falha ao obter o ID do veículo gerado.");
            }
            pstmtCarro = conn.prepareStatement(sqlCarro);
            pstmtCarro.setLong(1, carro.getId());
            pstmtCarro.setInt(2, carro.getQuantPortas());
            pstmtCarro.setString(3, carro.getTipCombustivel().toString());

            int affectedRowsCarro = pstmtCarro.executeUpdate();

            if (affectedRowsCarro == 0) {
                conn.rollback();
                throw new SQLException("Falha ao inserir em CARRO, nenhuma linha afetada.");
            }

            conn.commit();
            return carro;
        }
        catch (SQLException e){
            if (conn != null) {
                try {
                    System.err.println("Transação está sendo revertida para o cadastro do carro.");
                    conn.rollback();
                } catch (SQLException excep) {
                    System.err.println("Erro ao tentar reverter a transação: " + excep.getMessage());
                }
            }
            throw new SQLException("Erro ao cadastrar carro: " + e.getMessage(), e);
        } finally {
            if (generatedKeys != null) try { generatedKeys.close(); } catch (SQLException ignored) { }
            if (pstmtVeiculo != null) try { pstmtVeiculo.close(); } catch (SQLException ignored) {  }
            if (pstmtCarro != null) try { pstmtCarro.close(); } catch (SQLException ignored) {  }
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                    conn.close();
                } catch (SQLException ignored) { }
            }
        }
    }



    public List<Carro> consultarCarros(String modeloFiltro, String corFiltro, Integer anoFiltro) throws SQLException {
        StringBuilder sqlBuilder = new StringBuilder(
                "SELECT v.id, v.modelo, v.fabricante, v.ano, v.preco, v.cor, " +
                        "c.quantidade_portas, c.tipo_combustivel " +
                        "FROM VEICULO v " +
                        "INNER JOIN CARRO c ON v.id = c.id_veiculo "
        );

        List<Object> params = new ArrayList<>();
        List<String> conditions = new ArrayList<>();

        if (modeloFiltro != null && !modeloFiltro.isEmpty()) {
            conditions.add("v.modelo ILIKE ?");
            params.add("%" + modeloFiltro + "%");
        }
        if (corFiltro != null && !corFiltro.isEmpty()) {
            conditions.add("v.cor ILIKE ?");
            params.add("%" + corFiltro + "%");
        }
        if (anoFiltro != null && anoFiltro > 0) {
            conditions.add("v.ano = ?");
            params.add(anoFiltro);
        }

        if (!conditions.isEmpty()) {
            sqlBuilder.append(" WHERE ");
            sqlBuilder.append(String.join(" AND ", conditions));
        }
        sqlBuilder.append(" ORDER BY v.id;");

        List<Carro> carros = new ArrayList<>();
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sqlBuilder.toString())) {

            for (int i = 0; i < params.size(); i++) {
                pstmt.setObject(i + 1, params.get(i));
            }

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Carro carro = new Carro();
                    carro.setId(rs.getLong("id"));
                    carro.setModelo(rs.getString("modelo"));
                    carro.setFabricante(rs.getString("fabricante"));
                    carro.setAno(rs.getInt("ano"));
                    carro.setPreco(rs.getDouble("preco"));

                    carro.setCor(rs.getString("cor"));

                    carro.setQuantPortas(rs.getInt("quantidade_portas"));
                    if (carro.getTipCombustivel() != null) {
                        pstmt.setString(3, carro.getTipCombustivel().name());
                    } else {
                        pstmt.setNull(3, Types.VARCHAR);
                    };
                    carros.add(carro);
                }
            }
        } catch (SQLException e) {
            throw new SQLException("Erro ao consultar carros: " + e.getMessage(), e);
        }
        return carros;
    }


}
