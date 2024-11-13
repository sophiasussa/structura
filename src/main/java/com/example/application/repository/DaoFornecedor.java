package com.example.application.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import com.example.application.model.Fornecedor;

public class DaoFornecedor {

    public boolean inserir(Fornecedor fornecedor){
        try {
            Connection connection = DBConnection.getInstance().getConnection();
            String insert = "INSERT INTO fornecedor (nome, cnpj, ie, telefone, email, descriProdutos) VALUE (?,?,?,?,?,?)";
            PreparedStatement prepareStatement = connection.prepareStatement(insert);
            prepareStatement.setString(1, fornecedor.getNome());
            prepareStatement.setString(2, fornecedor.getCpf());
            prepareStatement.setString(3, fornecedor.getRg());
            prepareStatement.setString(4, fornecedor.getTelefone());
            prepareStatement.setString(5, fornecedor.getEmail());
            prepareStatement.setString(6, fornecedor.getDescriProdu());
            int resultado = prepareStatement.executeUpdate();
            return resultado > 0;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    public boolean alterar(Fornecedor fornecedor){
        try{
            Connection connection = DBConnection.getInstance().getConnection();
            String update = "UPDATE fornecedor SET nome = ?, cnpj = ?, ie = ?, telefone = ?, email = ?, descriProdutos = ? WHERE id = ?";
            PreparedStatement prepareStatement = connection.prepareStatement(update);
            prepareStatement.setString(1, fornecedor.getNome());
            prepareStatement.setString(2, fornecedor.getCpf());
            prepareStatement.setString(3, fornecedor.getRg());
            prepareStatement.setString(4, fornecedor.getTelefone());
            prepareStatement.setString(5, fornecedor.getEmail());
            prepareStatement.setString(6, fornecedor.getDescriProdu());
            prepareStatement.setLong(7, fornecedor.getId());
            int resultado = prepareStatement.executeUpdate();
            return resultado > 0;
        }catch(Exception e){
            e.printStackTrace();
            return false;
        }
    }

    public boolean excluir(Fornecedor fornecedor){
        try {
            Connection connection = DBConnection.getInstance().getConnection();
            String delete = "DELETE FROM fornecedor WHERE id = ?";
            PreparedStatement prepareStatement = connection.prepareStatement(delete);
            prepareStatement.setLong(1, fornecedor.getId());
            int resultado = prepareStatement.executeUpdate();
            return resultado > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<Fornecedor> pesquisarTodos(){
        try {
            Connection connection = DBConnection.getInstance().getConnection();
            String consulta = "SELECT * from fornecedor";
            List<Fornecedor> lista = new ArrayList<Fornecedor>();
            Fornecedor fornecedor;
            PreparedStatement prepareStatement = connection.prepareStatement(consulta);
            ResultSet resultSet = prepareStatement.executeQuery();
            while (resultSet.next()){
                fornecedor = new Fornecedor();
                fornecedor.setId(resultSet.getLong("id"));
                fornecedor.setNome(resultSet.getString("nome"));
                fornecedor.setCpf(resultSet.getString("cnpj"));
                fornecedor.setRg(resultSet.getString("ie"));
                fornecedor.setTelefone(resultSet.getString("telefone"));
                fornecedor.setEmail(resultSet.getString("email"));
                fornecedor.setDescriProdu(resultSet.getString("descriProdutos"));
                lista.add(fornecedor);
            }
            return lista;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    public List<Fornecedor> pesquisarFornecedor(String pesquisa) {
        List<Fornecedor> lista = new ArrayList<>();
        String consulta = "SELECT * FROM fornecedor WHERE nome LIKE ? OR CAST(cnpj AS CHAR) LIKE ? OR CAST(ie AS CHAR) LIKE ?";
        
        try (Connection connection = DBConnection.getInstance().getConnection();
             PreparedStatement prepareStatement = connection.prepareStatement(consulta)) {
            
            String busca = "%" + pesquisa + "%";
            prepareStatement.setString(1, busca);
            prepareStatement.setString(2, busca);
            prepareStatement.setString(3, busca);
    
            ResultSet resultSet = prepareStatement.executeQuery();
            
            while (resultSet.next()) {
                Fornecedor fornecedor = new Fornecedor();
                fornecedor.setId(resultSet.getLong("id"));
                fornecedor.setNome(resultSet.getString("nome"));
                fornecedor.setCpf(resultSet.getString("cnpj"));
                fornecedor.setRg(resultSet.getString("ie"));
                fornecedor.setTelefone(resultSet.getString("telefone"));
                fornecedor.setEmail(resultSet.getString("email"));
                fornecedor.setDescriProdu(resultSet.getString("descriProdutos"));
                lista.add(fornecedor);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return lista;
    }
}
