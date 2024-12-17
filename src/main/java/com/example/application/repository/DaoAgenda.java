package com.example.application.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.example.application.model.Agenda;
import com.example.application.model.Funcionario;
import com.example.application.model.Modelo;
import com.example.application.model.Produto;
import com.example.application.model.StatusAgenda;
import com.example.application.model.UnidMedida;

public class DaoAgenda {

    public boolean inserir(Agenda agenda){
        try {
            Connection connection = DBConnection.getInstance().getConnection();
            String insert = "INSERT INTO agenda (titulo, descricao, endereco, dataa, statuss, funcionario_id) VALUES (?,?,?,?,?,?)";
            PreparedStatement prepareStatement = connection.prepareStatement(insert);
            prepareStatement.setString(1, agenda.getTitulo());
            prepareStatement.setString(2, agenda.getDescricao());
            prepareStatement.setString(3, agenda.getEndereco());
            prepareStatement.setObject(4, agenda.getDataHora() != null ? java.sql.Timestamp.valueOf(agenda.getDataHora()) : null, java.sql.Types.TIMESTAMP);
            prepareStatement.setObject(5, agenda.getStatus() != null ? agenda.getStatus().name() : null, java.sql.Types.VARCHAR);
            prepareStatement.setObject(6, agenda.getFuncionario() != null ? agenda.getFuncionario().getId() : null, java.sql.Types.INTEGER);
            int resultado = prepareStatement.executeUpdate();
            return resultado > 0;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    public boolean alterar(Agenda agenda) {
        try {
            Connection connection = DBConnection.getInstance().getConnection();
            String update = "UPDATE agenda SET titulo = ?, descricao = ?, endereco = ?, dataa = ?, statuss = ?, funcionario_id = ? WHERE id = ?";
            
            PreparedStatement prepareStatement = connection.prepareStatement(update);
            prepareStatement.setString(1, agenda.getTitulo());
            prepareStatement.setString(2, agenda.getDescricao());
            prepareStatement.setString(3, agenda.getEndereco());
            prepareStatement.setObject(4, agenda.getDataHora() != null ? java.sql.Timestamp.valueOf(agenda.getDataHora()) : null, java.sql.Types.TIMESTAMP);
            prepareStatement.setObject(5, agenda.getStatus() != null ? agenda.getStatus().name() : null, java.sql.Types.VARCHAR);
            prepareStatement.setObject(6, agenda.getFuncionario() != null ? agenda.getFuncionario().getId() : null, java.sql.Types.INTEGER);
            prepareStatement.setLong(7, agenda.getId());
            
            int resultado = prepareStatement.executeUpdate();
            return resultado > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


    public boolean excluir(Agenda agenda) {
        try {
            Connection connection = DBConnection.getInstance().getConnection();
            String delete = "DELETE FROM agenda WHERE id = ?";
            
            PreparedStatement prepareStatement = connection.prepareStatement(delete);
            prepareStatement.setLong(1, agenda.getId());
            
            int resultado = prepareStatement.executeUpdate();
            return resultado > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
        
    public List<Agenda> pesquisarTodos() {
        try {
            Connection connection = DBConnection.getInstance().getConnection();
            String consulta = """
                SELECT a.*, f.nome AS funcionario_nome
                FROM agenda a
                LEFT JOIN funcionario f ON a.funcionario_id = f.id
            """;
            List<Agenda> lista = new ArrayList<>();
            PreparedStatement prepareStatement = connection.prepareStatement(consulta);
            ResultSet resultSet = prepareStatement.executeQuery();

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
            return lista;
        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }
    
    public List<Agenda> pesquisarAgenda(String pesquisa) {
        List<Agenda> lista = new ArrayList<>();
        String consulta = """
            SELECT a.*, f.nome AS funcionario_nome
            FROM agenda a
            LEFT JOIN funcionario f ON a.funcionario_id = f.id
            WHERE f.nome LIKE ? OR a.titulo LIKE ? OR DATE(a.dataa) = ? OR a.statuss LIKE ?
        """;
    
        try (Connection connection = DBConnection.getInstance().getConnection();
             PreparedStatement prepareStatement = connection.prepareStatement(consulta)) {
    
            String buscaTexto = "%" + pesquisa + "%";
    
            prepareStatement.setString(1, buscaTexto);
            prepareStatement.setString(2, buscaTexto);
            prepareStatement.setString(4, buscaTexto);
    
            try {
                DateTimeFormatter entradaFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                LocalDate dataFormatada = LocalDate.parse(pesquisa, entradaFormatter);
                prepareStatement.setDate(3, java.sql.Date.valueOf(dataFormatada));
            } catch (DateTimeParseException e) {
                prepareStatement.setNull(3, java.sql.Types.DATE);
            }
    
            ResultSet resultSet = prepareStatement.executeQuery();
    
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
        } catch (Exception e) {
            e.printStackTrace();
        }
        return lista;
    }
    
    
}
