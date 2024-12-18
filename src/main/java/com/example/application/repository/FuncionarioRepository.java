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
            int rowsInserted = stmt.executeUpdate();
            if (rowsInserted > 0) {
                logger.info("Funcionário inserido com sucesso: " + funcionario.getNome());
                return true;
            } else {
                logger.warn("Nenhuma linha inserida para o funcionário: " + funcionario.getNome());
                return false;
            }
        } catch (SQLException e) {
            logger.error("Erro ao inserir funcionário: " + funcionario.getNome(), e);
            throw new RuntimeException("Erro ao processar a solicitação. Tente novamente.", e);
        } catch (Exception e) {
            logger.error("Erro inesperado ao inserir funcionário: " + funcionario.getNome(), e);
            throw new RuntimeException("Erro inesperado ao processar a solicitação. Tente novamente.", e);
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
            int rowsUpdated = stmt.executeUpdate();
            if (rowsUpdated > 0) {
                logger.info("Funcionário atualizado com sucesso: " + funcionario.getNome());
                return true;
            } else {
                logger.warn("Nenhuma linha atualizada para o funcionário com ID: " + funcionario.getId());
                return false;
            }
        } catch (SQLException e) {
            logger.error("Erro ao alterar funcionário com ID: " + funcionario.getId(), e);
            throw new RuntimeException("Erro ao processar a solicitação. Tente novamente.", e);
        } catch (Exception e) {
            logger.error("Erro inesperado ao alterar funcionário com ID: " + funcionario.getId(), e);
            throw new RuntimeException("Erro inesperado ao processar a solicitação. Tente novamente.", e);
        }
    }

    public boolean excluir(Funcionario funcionario) {
        String sql = "DELETE FROM funcionario WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, funcionario.getId());
            int rowsDeleted = stmt.executeUpdate();
            if (rowsDeleted > 0) {
                logger.info("Funcionário excluído com sucesso: " + funcionario.getId());
                return true;
            } else {
                logger.warn("Nenhuma linha excluída para o funcionário com ID: " + funcionario.getId());
                return false;
            }
        } catch (SQLException e) {
            logger.error("Erro ao excluir funcionário com ID: " + funcionario.getId(), e);
            throw new RuntimeException("Erro ao processar a solicitação. Tente novamente.", e);
        } catch (Exception e) {
            logger.error("Erro inesperado ao excluir funcionário com ID: " + funcionario.getId(), e);
            throw new RuntimeException("Erro inesperado ao processar a solicitação. Tente novamente.", e);
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
            logger.info("Pesquisados " + lista.size() + " funcionários.");
        } catch (SQLException e) {
            logger.error("Erro ao pesquisar todos os funcionários.", e);
            throw new RuntimeException("Erro ao processar a solicitação. Tente novamente.", e);
        } catch (Exception e) {
            logger.error("Erro inesperado ao pesquisar todos os funcionários.", e);
            throw new RuntimeException("Erro inesperado ao processar a solicitação. Tente novamente.", e);
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
            logger.info("Pesquisados " + lista.size() + " funcionários para a pesquisa: " + pesquisa);
        } catch (SQLException e) {
            logger.error("Erro ao pesquisar funcionário com a pesquisa: " + pesquisa, e);
            throw new RuntimeException("Erro ao processar a solicitação. Tente novamente.", e);
        } catch (Exception e) {
            logger.error("Erro inesperado ao pesquisar funcionário com a pesquisa: " + pesquisa, e);
            throw new RuntimeException("Erro inesperado ao processar a solicitação. Tente novamente.", e);
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
                logger.info("Funcionário encontrado pelo ID: " + id);
                return funcionario;
            }
        } catch (SQLException e) {
            logger.error("Erro ao buscar funcionário pelo ID: " + id, e);
            throw new RuntimeException("Erro ao processar a solicitação. Tente novamente.", e);
        } catch (Exception e) {
            logger.error("Erro inesperado ao buscar funcionário pelo ID: " + id, e);
            throw new RuntimeException("Erro inesperado ao processar a solicitação. Tente novamente.", e);
        }
        return null;
    }
}
