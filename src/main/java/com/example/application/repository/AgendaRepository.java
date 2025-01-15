package com.example.application.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

import com.example.application.model.Agenda;
import com.example.application.model.Funcionario;
import com.example.application.model.StatusAgenda;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AgendaRepository {

    private static final Logger logger = LoggerFactory.getLogger(AgendaRepository.class);
    private Connection connection;

    public AgendaRepository() throws SQLException {
        this.connection = DBConnection.getInstance().getConnection();
    }

    public boolean inserir(Agenda agenda) {
        String sql = "INSERT INTO agenda (titulo, descricao, endereco, dataa, statuss, funcionario_id) VALUES (?,?,?,?,?,?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, agenda.getTitulo());
            stmt.setString(2, agenda.getDescricao());
            stmt.setString(3, agenda.getEndereco());
            stmt.setObject(4, agenda.getDataHora() != null ? java.sql.Timestamp.valueOf(agenda.getDataHora()) : null, java.sql.Types.TIMESTAMP);
            stmt.setObject(5, agenda.getStatus() != null ? agenda.getStatus().name() : null, java.sql.Types.VARCHAR);
            stmt.setObject(6, agenda.getFuncionario() != null ? agenda.getFuncionario().getId() : null, java.sql.Types.INTEGER);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            logger.error("Erro ao inserir agenda: " + agenda.getTitulo(), e);
            throw new RuntimeException("Erro ao processar a solicitação. Tente novamente.", e);
        }
    }

    public boolean alterar(Agenda agenda) {
        String sql = "UPDATE agenda SET titulo = ?, descricao = ?, endereco = ?, dataa = ?, statuss = ?, funcionario_id = ? WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, agenda.getTitulo());
            stmt.setString(2, agenda.getDescricao());
            stmt.setString(3, agenda.getEndereco());
            stmt.setObject(4, agenda.getDataHora() != null ? java.sql.Timestamp.valueOf(agenda.getDataHora()) : null, java.sql.Types.TIMESTAMP);
            stmt.setObject(5, agenda.getStatus() != null ? agenda.getStatus().name() : null, java.sql.Types.VARCHAR);
            stmt.setObject(6, agenda.getFuncionario() != null ? agenda.getFuncionario().getId() : null, java.sql.Types.INTEGER);
            stmt.setLong(7, agenda.getId());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            logger.error("Erro ao alterar agenda com ID: " + agenda.getId(), e);
            throw new RuntimeException("Erro ao processar a solicitação. Tente novamente.", e);
        }
    }

    public boolean excluir(Agenda agenda) {
        String sql = "DELETE FROM agenda WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, agenda.getId());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            logger.error("Erro ao excluir agenda com ID: " + agenda.getId(), e);
            throw new RuntimeException("Erro ao processar a solicitação. Tente novamente.", e);
        }
    }

    public List<Agenda> pesquisarTodos() {
        List<Agenda> lista = new ArrayList<>();
        String sql = "SELECT a.*, f.nome AS funcionario_nome FROM agenda a LEFT JOIN funcionario f ON a.funcionario_id = f.id";
        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet resultSet = stmt.executeQuery()) {

            while (resultSet.next()) {
                Agenda agenda = new Agenda();
                agenda.setId(resultSet.getLong("id"));
                agenda.setTitulo(resultSet.getString("titulo"));
                agenda.setDescricao(resultSet.getString("descricao"));
                agenda.setEndereco(resultSet.getString("endereco"));
                agenda.setDataHora(resultSet.getDate("dataa") != null
                    ? resultSet.getTimestamp("dataa").toLocalDateTime()
                    : null);
                String status = resultSet.getString("statuss");
                agenda.setStatus(status != null ? StatusAgenda.valueOf(status) : null);

                Funcionario funcionario = new Funcionario();
                funcionario.setId(resultSet.getLong("funcionario_id"));
                funcionario.setNome(resultSet.getString("funcionario_nome"));
                agenda.setFuncionario(funcionario.getId() != 0 ? funcionario : null);

                lista.add(agenda);
            }
        } catch (SQLException e) {
            logger.error("Erro ao pesquisar todas as agendas.", e);
            throw new RuntimeException("Erro ao processar a solicitação. Tente novamente.", e);
        }
        return lista;
    }

    public List<Agenda> pesquisarAgenda(String pesquisa) {
        List<Agenda> lista = new ArrayList<>();
        String sql = "SELECT a.*, f.nome AS funcionario_nome FROM agenda a LEFT JOIN funcionario f ON a.funcionario_id = f.id WHERE f.nome LIKE ? OR a.titulo LIKE ? OR DATE(a.dataa) = ? OR a.statuss LIKE ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            String buscaTexto = "%" + pesquisa + "%";
            stmt.setString(1, buscaTexto);
            stmt.setString(2, buscaTexto);
            stmt.setString(4, buscaTexto);

            try {
                DateTimeFormatter entradaFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                LocalDate dataFormatada = LocalDate.parse(pesquisa, entradaFormatter);
                stmt.setDate(3, java.sql.Date.valueOf(dataFormatada));
            } catch (DateTimeParseException e) {
                stmt.setNull(3, java.sql.Types.DATE);
            }

            try (ResultSet resultSet = stmt.executeQuery()) {
                while (resultSet.next()) {
                    Agenda agenda = new Agenda();
                    agenda.setId(resultSet.getLong("id"));
                    agenda.setTitulo(resultSet.getString("titulo"));
                    agenda.setDescricao(resultSet.getString("descricao"));
                    agenda.setEndereco(resultSet.getString("endereco"));
                    agenda.setDataHora(resultSet.getTimestamp("dataa") != null
                        ? resultSet.getTimestamp("dataa").toLocalDateTime()
                        : null);
                    String status = resultSet.getString("statuss");
                    agenda.setStatus(status != null ? StatusAgenda.valueOf(status) : null);

                    Funcionario funcionario = new Funcionario();
                    funcionario.setId(resultSet.getLong("funcionario_id"));
                    funcionario.setNome(resultSet.getString("funcionario_nome"));
                    agenda.setFuncionario(funcionario.getId() != 0 ? funcionario : null);

                    lista.add(agenda);
                }
            }
        } catch (SQLException e) {
            logger.error("Erro ao pesquisar agenda com a pesquisa: " + pesquisa, e);
            throw new RuntimeException("Erro ao processar a solicitação. Tente novamente.", e);
        }
        return lista;
    }

    public List<Agenda> pesquisarTarefasDeHoje() {
        List<Agenda> lista = new ArrayList<>();
        String sql = "SELECT a.*, f.nome AS funcionario_nome FROM agenda a LEFT JOIN funcionario f ON a.funcionario_id = f.id WHERE DATE(a.dataa) = CURRENT_DATE";
        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet resultSet = stmt.executeQuery()) {

            while (resultSet.next()) {
                Agenda agenda = new Agenda();
                agenda.setId(resultSet.getLong("id"));
                agenda.setTitulo(resultSet.getString("titulo"));
                agenda.setDescricao(resultSet.getString("descricao"));
                agenda.setEndereco(resultSet.getString("endereco"));
                agenda.setDataHora(resultSet.getTimestamp("dataa") != null
                    ? resultSet.getTimestamp("dataa").toLocalDateTime()
                    : null);
                String status = resultSet.getString("statuss");
                agenda.setStatus(status != null ? StatusAgenda.valueOf(status) : null);

                Funcionario funcionario = new Funcionario();
                funcionario.setId(resultSet.getLong("funcionario_id"));
                funcionario.setNome(resultSet.getString("funcionario_nome"));
                agenda.setFuncionario(funcionario.getId() != 0 ? funcionario : null);

                lista.add(agenda);
            }
        } catch (SQLException e) {
            logger.error("Erro ao pesquisar tarefas de hoje.", e);
            throw new RuntimeException("Erro ao processar a solicitação. Tente novamente.", e);
        }
        return lista;
    }
}