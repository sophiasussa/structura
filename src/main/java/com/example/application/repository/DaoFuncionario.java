package com.example.application.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;
import java.sql.ResultSet;

import com.example.application.model.Funcionario;

public class DaoFuncionario {

    public boolean inserir(Funcionario funcionario){
        try {
            Connection connection = DBConnection.getInstance().getConnection();
            String insert = "INSERT INTO funcionario (nome, cpf, rg, telefone, dataAdmissao, salario) VALUES (?,?,?,?,?,?)";
            PreparedStatement prepareStatement = connection.prepareStatement(insert);
            prepareStatement.setString(1, funcionario.getNome());
            prepareStatement.setString(2, funcionario.getCpf());
            prepareStatement.setString(3, funcionario.getRg());
            prepareStatement.setString(4, funcionario.getTelefone());
            prepareStatement.setDate(5, java.sql.Date.valueOf(funcionario.getDataAdmissao()));
            prepareStatement.setBigDecimal(6, funcionario.getSalario());
            int resultado = prepareStatement.executeUpdate();
            return resultado > 0;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    public boolean alterar(Funcionario funcionario){
        try {
            Connection connection = DBConnection.getInstance().getConnection();
            String update = "UPDATE funcionario SET nome = ?, cpf = ?, rg = ?, telefone = ?, dataAdmissao = ?, salario = ? WHERE id = ?";
            PreparedStatement prepareStatement = connection.prepareStatement(update);
            prepareStatement.setString(1, funcionario.getNome());
            prepareStatement.setString(2, funcionario.getCpf());
            prepareStatement.setString(3, funcionario.getRg());
            prepareStatement.setString(4, funcionario.getTelefone());
            prepareStatement.setDate(5, java.sql.Date.valueOf(funcionario.getDataAdmissao()));
            prepareStatement.setBigDecimal(6, funcionario.getSalario());
            int resultado = prepareStatement.executeUpdate();
            return resultado > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean excluir(Funcionario funcionario){
        try {
            Connection connection = DBConnection.getInstance().getConnection();
            String delete = "DELETE FROM funcionario WHERE id = ?";
            PreparedStatement prepareStatement = connection.prepareStatement(delete);
            prepareStatement.setLong(1, funcionario.getId());
            int resultado = prepareStatement.executeUpdate();
            return resultado > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<Funcionario> pesquisarTodos(){
        try {
            Connection connection = DBConnection.getInstance().getConnection();
            String consulta = "SELECT * from funcionario";
            List<Funcionario> lista = new ArrayList<Funcionario>();
            Funcionario funcionario;
            PreparedStatement prepareStatement = connection.prepareStatement(consulta);
            ResultSet resultSet = prepareStatement.executeQuery();
            while(resultSet.next()){
                funcionario = new Funcionario();
                funcionario.setId(resultSet.getLong("id"));
                funcionario.setNome(resultSet.getString("nome"));
                funcionario.setCpf(resultSet.getString("cpf"));
                funcionario.setRg(resultSet.getString("rg"));
                funcionario.setTelefone(resultSet.getString("telefone"));
                funcionario.setDataAdmissao(resultSet.getDate("dataAdmissao").toLocalDate());
                funcionario.setSalario(resultSet.getBigDecimal("salario"));
                lista.add(funcionario);
            }
            return lista;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<Funcionario> pesquisarFuncionario(String pesquisa){
        List<Funcionario> lista = new ArrayList<>();
        String consulta = "SELECT * FROM funcionario WHERE nome LIKE ? OR CAST(cpf AS CHAR) LIKE ? OR CAST(rg AS CHAR) LIKE ?";

        try (Connection connection = DBConnection.getInstance().getConnection();
            PreparedStatement prepareStatement = connection.prepareStatement(consulta)){
            
            String busca = "%" + pesquisa + "%";
            prepareStatement.setString(1, busca);
            prepareStatement.setString(2, busca);
            prepareStatement.setString(3, busca);

            ResultSet resultSet = prepareStatement.executeQuery();

            while (resultSet.next()){
                Funcionario funcionario = new Funcionario();
                funcionario.setId(resultSet.getLong("id"));
                funcionario.setNome(resultSet.getString("nome"));
                funcionario.setCpf(resultSet.getString("cpf"));
                funcionario.setRg(resultSet.getString("rg"));
                funcionario.setTelefone(resultSet.getString("telefone"));
                funcionario.setDataAdmissao(resultSet.getDate("dataAdmissao").toLocalDate());
                funcionario.setSalario(resultSet.getBigDecimal("salario"));
                lista.add(funcionario);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return lista;
    }

}
