package com.example.application.repository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.sql.Connection;
import com.example.application.model.Cliente;

public class DaoCliente {

    public boolean inserir(Cliente cliente){
        try{
            Connection connection = DBConnection.getInstance().getConnection();
            String insert = "INSERT INTO cliente (nome, cpf, rg, telefone) VALUE (?,?,?,?)";
            PreparedStatement prepareStatement = connection.prepareStatement(insert);
            prepareStatement.setString(1, cliente.getNome());
            prepareStatement.setLong(2, cliente.getCpf());
            prepareStatement.setLong(3, cliente.getRg());
            prepareStatement.setLong(4, cliente.getTelefone());
            int resultado = prepareStatement.executeUpdate();
            return resultado > 0;
        }catch(Exception e){
            System.out.println(e.getMessage());
            return false;
        }
    }

    public boolean alterar(Cliente cliente){
        try{
            Connection connection = DBConnection.getInstance().getConnection();
            String update = "UPDATE cliente set nome = ? cpf = ? rg = ? telefone = ? where id = ?";
            PreparedStatement prepareStatement = connection.prepareStatement(update);
            prepareStatement.setString(1, cliente.getNome());
            prepareStatement.setLong(2, cliente.getCpf());
            prepareStatement.setLong(3, cliente.getRg());
            prepareStatement.setLong(4, cliente.getTelefone());
            int resultado = prepareStatement.executeUpdate();
            return resultado > 0;
        } catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    public boolean excluir(Cliente cliente) {
        try {
            Connection connection = DBConnection.getInstance().getConnection();
            String delete = "DELETE FROM cliente WHERE id = ?";
            PreparedStatement prepareStatement = connection.prepareStatement(delete);
            prepareStatement.setLong(1, cliente.getId());
            int resultado = prepareStatement.executeUpdate();
            return resultado > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public List<Cliente> pesquisarTodos(){
        try{
            Connection connection = DBConnection.getInstance().getConnection();
            String consulta = "SELECT * from cliente";
            List<Cliente> lista = new ArrayList<Cliente>();
            Cliente cliente;
            PreparedStatement prepareStatement = connection.prepareStatement(consulta);
            ResultSet resultSet = prepareStatement.executeQuery();
            while (resultSet.next()){
                cliente = new Cliente();
                cliente.setId(resultSet.getLong("id"));
                cliente.setNome(resultSet.getString("nome"));
                cliente.setCpf(resultSet.getLong("cpf"));
                cliente.setRg(resultSet.getLong("rg"));
                cliente.setTelefone(resultSet.getLong("telefone"));
                lista.add(cliente);
            }
            return lista;
        }catch(Exception e){
            e.printStackTrace();
            return null;
        }
    }

    public List<Cliente> pesquisarCliente(String pesquisa){
        List<Cliente> lista = new ArrayList<>();
        String consulta = "SELECT * FROM cliente WHERE nome LIKE ? OR CAST(cpf AS CHAR) LIKE ? OR CAST(rg AS CHAR) LIKE ?";

        try(Connection connection = DBConnection.getInstance().getConnection();
            PreparedStatement prepareStatement = connection.prepareStatement(consulta)){
            
            String busca = "%" + pesquisa + "%";
            prepareStatement.setString(1, busca);
            prepareStatement.setString(2, busca);
            prepareStatement.setString(3, busca);

            ResultSet resultSet = prepareStatement.executeQuery();

            while(resultSet.next()){
                Cliente cliente = new Cliente();
                cliente.setId(resultSet.getLong("id"));
                cliente.setNome(resultSet.getString("nome"));
                cliente.setCpf(resultSet.getLong("cpf"));
                cliente.setRg(resultSet.getLong("rg"));
                cliente.setTelefone(resultSet.getLong("telefone"));
                lista.add(cliente);
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return lista;
    }
}
