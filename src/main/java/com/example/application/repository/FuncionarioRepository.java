package com.example.application.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.example.application.model.Cliente;
import com.example.application.model.Funcionario;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FuncionarioRepository {

    private static final Logger logger = LoggerFactory.getLogger(FuncionarioRepository.class);
    private Connection connection;

    public FuncionarioRepository() throws SQLException {
        this.connection = DBConnection.getInstance().getConnection();
    }

    public boolean inserir(Funcionario funcionario) {
        String sql = "INSERT INTO funcionario (nome, cpf, rg, telefone, dataAdmissao, salario) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, funcionario.getNome());
            stmt.setString(2, funcionario.getCpf());
            stmt.setString(3, funcionario.getRg());
            stmt.setString(4, funcionario.getTelefone());
            stmt.setObject(5, funcionario.getDataAdmissao() != null ? java.sql.Date.valueOf(funcionario.getDataAdmissao()) : null, java.sql.Types.DATE);
            stmt.setDouble(6, funcionario.getSalario());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            logger.error("Erro ao inserir funcionário: " + funcionario.getNome(), e);
            throw new RuntimeException("Erro ao processar a solicitação. Tente novamente.", e);
        }
    }

    public boolean alterar(Funcionario funcionario) {
        String sql = "UPDATE funcionario SET nome = ?, cpf = ?, rg = ?, telefone = ?, dataAdmissao = ?, salario = ? WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, funcionario.getNome());
            stmt.setString(2, funcionario.getCpf());
            stmt.setString(3, funcionario.getRg());
            stmt.setString(4, funcionario.getTelefone());
            stmt.setObject(5, funcionario.getDataAdmissao() != null ? java.sql.Date.valueOf(funcionario.getDataAdmissao()) : null, java.sql.Types.DATE);
            stmt.setDouble(6, funcionario.getSalario());
            stmt.setLong(7, funcionario.getId());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            logger.error("Erro ao alterar funcionário com ID: " + funcionario.getId(), e);
            throw new RuntimeException("Erro ao processar a solicitação. Tente novamente.", e);
        }
    }

    public boolean excluir(Funcionario funcionario) {
        String sql = "DELETE FROM funcionario WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, funcionario.getId());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            logger.error("Erro ao excluir funcionário com ID: " + funcionario.getId(), e);
            throw new RuntimeException("Erro ao processar a solicitação. Tente novamente.", e);
        }
    }

    public List<Funcionario> pesquisarTodos() {
        List<Funcionario> lista = new ArrayList<>();
        String sql = "SELECT * FROM funcionario";
        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet resultSet = stmt.executeQuery()) {

            while (resultSet.next()) {
                Funcionario funcionario = new Funcionario();
                funcionario.setId(resultSet.getLong("id"));
                funcionario.setNome(resultSet.getString("nome"));
                funcionario.setCpf(resultSet.getString("cpf"));
                funcionario.setRg(resultSet.getString("rg"));
                funcionario.setTelefone(resultSet.getString("telefone"));
                java.sql.Date dataSql = resultSet.getDate("dataAdmissao");
                funcionario.setDataAdmissao(dataSql != null ? dataSql.toLocalDate() : null);
                funcionario.setSalario(resultSet.getDouble("salario"));
                lista.add(funcionario);
            }
        } catch (SQLException e) {
            logger.error("Erro ao pesquisar todos os funcionários.", e);
            throw new RuntimeException("Erro ao processar a solicitação. Tente novamente.", e);
        }
        return lista;
    }

    public List<Funcionario> pesquisarFuncionario(String pesquisa) {
        List<Funcionario> lista = new ArrayList<>();
        String sql = "SELECT * FROM funcionario WHERE nome LIKE ? OR CAST(cpf AS CHAR) LIKE ? OR CAST(rg AS CHAR) LIKE ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            String busca = "%" + pesquisa + "%";
            stmt.setString(1, busca);
            stmt.setString(2, busca);
            stmt.setString(3, busca);
            ResultSet resultSet = stmt.executeQuery();
            while (resultSet.next()) {
                Funcionario funcionario = new Funcionario();
                funcionario.setId(resultSet.getLong("id"));
                funcionario.setNome(resultSet.getString("nome"));
                funcionario.setCpf(resultSet.getString("cpf"));
                funcionario.setRg(resultSet.getString("rg"));
                funcionario.setTelefone(resultSet.getString("telefone"));
                java.sql.Date dataSql = resultSet.getDate("dataAdmissao");
                funcionario.setDataAdmissao(dataSql != null ? dataSql.toLocalDate() : null);
                funcionario.setSalario(resultSet.getDouble("salario"));
                lista.add(funcionario);
            }
        } catch (SQLException e) {
            logger.error("Erro ao pesquisar funcionário com a pesquisa: " + pesquisa, e);
            throw new RuntimeException("Erro ao processar a solicitação. Tente novamente.", e);
        }
        return lista;
    }

    public Funcionario getFuncionarioById(int id) {
        String sql = "SELECT * FROM funcionario WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet resultSet = stmt.executeQuery();
            if (resultSet.next()) {
                Funcionario funcionario = new Funcionario();
                funcionario.setId(resultSet.getLong("id"));
                funcionario.setNome(resultSet.getString("nome"));
                funcionario.setCpf(resultSet.getString("cpf"));
                funcionario.setRg(resultSet.getString("rg"));
                funcionario.setTelefone(resultSet.getString("telefone"));
                java.sql.Date dataSql = resultSet.getDate("dataAdmissao");
                funcionario.setDataAdmissao(dataSql != null ? dataSql.toLocalDate() : null);
                funcionario.setSalario(resultSet.getDouble("salario"));
                return funcionario;
            }
        } catch (SQLException e) {
            logger.error("Erro ao buscar funcionário pelo ID: " + id, e);
            throw new RuntimeException("Erro ao processar a solicitação. Tente novamente.", e);
        }
        return null;
    }

    public boolean isFuncionarioInUse(Funcionario funcionario) {
        String sql = """
            SELECT COUNT(*) AS total
            FROM (
                SELECT funcionario_id FROM os WHERE funcionario_id = ?
                UNION ALL
                SELECT funcionario_id FROM projeto WHERE funcionario_id = ?
            ) AS combined
        """;
    
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, funcionario.getId());
            stmt.setLong(2, funcionario.getId());
            ResultSet result = stmt.executeQuery();
    
            if (result.next()) {
                int count = result.getInt("total");
                return count > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
}