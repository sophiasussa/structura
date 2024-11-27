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
            String insert = "INSERT INTO fornecedor (empresa, vendedor, cnpj, ie, telefone, email, descriProdutos) VALUES (?,?,?,?,?,?,?)";
            PreparedStatement prepareStatement = connection.prepareStatement(insert);
            prepareStatement.setString(1, fornecedor.getEmpresa());
            prepareStatement.setString(2, fornecedor.getVendedor());
            prepareStatement.setString(3, fornecedor.getCnpj());
            prepareStatement.setString(4, fornecedor.getIe());
            prepareStatement.setString(5, fornecedor.getTelefone());
            prepareStatement.setString(6, fornecedor.getEmail());
            prepareStatement.setString(7, fornecedor.getDescriProdutos());
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
            String update = "UPDATE fornecedor SET empresa = ?, vendedor = ?, cnpj = ?, ie = ?, telefone = ?, email = ?, descriProdutos = ? WHERE id = ?";
            PreparedStatement prepareStatement = connection.prepareStatement(update);
            prepareStatement.setString(1, fornecedor.getEmpresa());
            prepareStatement.setString(2, fornecedor.getVendedor());
            prepareStatement.setString(3, fornecedor.getCnpj());
            prepareStatement.setString(4, fornecedor.getIe());
            prepareStatement.setString(5, fornecedor.getTelefone());
            prepareStatement.setString(6, fornecedor.getEmail());
            prepareStatement.setString(7, fornecedor.getDescriProdutos());
            prepareStatement.setLong(8, fornecedor.getId());
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
                fornecedor.setEmpresa(resultSet.getString("empresa"));
                fornecedor.setVendedor(resultSet.getString("vendedor"));
                fornecedor.setCnpj(resultSet.getString("cnpj"));
                fornecedor.setIe(resultSet.getString("ie"));
                fornecedor.setTelefone(resultSet.getString("telefone"));
                fornecedor.setEmail(resultSet.getString("email"));
                fornecedor.setDescriProdutos(resultSet.getString("descriProdutos"));
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
        String consulta = "SELECT * FROM fornecedor WHERE empresa LIKE ? OR vendedor LIKE ? OR CAST(cnpj AS CHAR) LIKE ? OR CAST(ie AS CHAR) LIKE ?";
        
        try (Connection connection = DBConnection.getInstance().getConnection();
             PreparedStatement prepareStatement = connection.prepareStatement(consulta)) {
            
            String busca = "%" + pesquisa + "%";
            prepareStatement.setString(1, busca);
            prepareStatement.setString(2, busca);
            prepareStatement.setString(3, busca);
            prepareStatement.setString(4, busca);
    
            ResultSet resultSet = prepareStatement.executeQuery();
            
            while (resultSet.next()) {
                Fornecedor fornecedor = new Fornecedor();
                fornecedor.setId(resultSet.getLong("id"));
                fornecedor.setEmpresa(resultSet.getString("empresa"));
                fornecedor.setVendedor(resultSet.getString("vendedor"));
                fornecedor.setCnpj(resultSet.getString("cnpj"));
                fornecedor.setIe(resultSet.getString("ie"));
                fornecedor.setTelefone(resultSet.getString("telefone"));
                fornecedor.setEmail(resultSet.getString("email"));
                fornecedor.setDescriProdutos(resultSet.getString("descriProdutos"));
                lista.add(fornecedor);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return lista;
    }
}
