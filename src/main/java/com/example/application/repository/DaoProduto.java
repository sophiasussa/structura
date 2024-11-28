package com.example.application.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.example.application.model.Produto;
import com.example.application.model.UnidMedida;
import com.example.application.model.Material;

public class DaoProduto {

    public boolean inserir(Produto produto){
        try{
            Connection connection = DBConnection.getInstance().getConnection();
            String insert = "INSERT INTO produto (nome, modelo, quantidadeAtual, quantidadeMinima, custoUnitario, material_id, unid_medida_id) VALUE (?,?,?,?,?,?,?)";
            PreparedStatement prepareStatement = connection.prepareStatement(insert);
            prepareStatement.setString(1, produto.getNome());
            prepareStatement.setString(2, produto.getModelo());
            prepareStatement.setInt(3, produto.getQuantidadeAtual());
            prepareStatement.setInt(4, produto.getQuantidadeMinima());
            prepareStatement.setBigDecimal(5, produto.getCustoUnitario());
            prepareStatement.setObject(6, produto.getMaterial() != null ? produto.getMaterial().getId() : null, java.sql.Types.INTEGER);
            prepareStatement.setObject(7, produto.getUnidMedida() != null ? produto.getUnidMedida().getId() : null, java.sql.Types.INTEGER);
            int resultado = prepareStatement.executeUpdate();
            return resultado > 0;
        }catch(Exception e){
            System.out.println(e.getMessage());
            return false;
        }
    }

    public boolean alterar(Produto produto){
        try{

            Connection connection = DBConnection.getInstance().getConnection();
            String update = "UPDATE produto SET nome = ?, modelo = ?, quantidadeAtual = ?, quantidadeMinima = ?, custoUnitario = ?, material_id = ?, unid_medida_id = ? WHERE id = ?";
            
            PreparedStatement prepareStatement = connection.prepareStatement(update);
            prepareStatement.setString(1, produto.getNome());
            prepareStatement.setString(2, produto.getModelo());
            prepareStatement.setInt(3, produto.getQuantidadeAtual());
            prepareStatement.setInt(4, produto.getQuantidadeMinima());
            prepareStatement.setBigDecimal(5, produto.getCustoUnitario());
            prepareStatement.setObject(6, produto.getMaterial() != null ? produto.getMaterial().getId() : null, java.sql.Types.INTEGER);
            prepareStatement.setObject(7, produto.getUnidMedida() != null ? produto.getUnidMedida().getId() : null, java.sql.Types.INTEGER);
            prepareStatement.setLong(8, produto.getId());
            int resultado = prepareStatement.executeUpdate();
            return resultado > 0;
        } catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    public boolean excluir(Produto produto) {
        try {
            Connection connection = DBConnection.getInstance().getConnection();
            String delete = "DELETE FROM produto WHERE id = ?";
            PreparedStatement prepareStatement = connection.prepareStatement(delete);
            prepareStatement.setLong(1, produto.getId());
            int resultado = prepareStatement.executeUpdate();
            return resultado > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public List<Produto> pesquisarTodos() {
        try {
            Connection connection = DBConnection.getInstance().getConnection();
            String consulta = """
                SELECT p.*, m.nome AS material_nome, um.nome AS unid_medida_nome
                FROM produto p
                LEFT JOIN material m ON p.material_id = m.id
                LEFT JOIN unidMedida um ON p.unid_medida_id = um.id
            """;
            List<Produto> lista = new ArrayList<>();
            PreparedStatement prepareStatement = connection.prepareStatement(consulta);
            ResultSet resultSet = prepareStatement.executeQuery();
    
            while (resultSet.next()) {
                Produto produto = new Produto();
                produto.setId(resultSet.getLong("id"));
                produto.setNome(resultSet.getString("nome"));
                produto.setModelo(resultSet.getString("modelo"));
                produto.setQuantidadeAtual(resultSet.getInt("quantidadeAtual"));
                produto.setQuantidadeMinima(resultSet.getInt("quantidadeMinima"));
                produto.setCustoUnitario(resultSet.getBigDecimal("custoUnitario"));
                Material material = new Material();
                material.setId(resultSet.getInt("material_id"));
                material.setNome(resultSet.getString("material_nome"));
                produto.setMaterial(material.getId() != 0 ? material : null);

                UnidMedida unidMedida = new UnidMedida();
                unidMedida.setId(resultSet.getInt("unid_medida_id"));
                unidMedida.setNome(resultSet.getString("unid_medida_nome"));
                produto.setUnidMedida(unidMedida.getId() != 0 ? unidMedida : null);
    
                lista.add(produto);
            }
            return lista;
        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }
    

    public List<Produto> pesquisarProduto(String pesquisa) {
        List<Produto> lista = new ArrayList<>();
        String consulta = """
            SELECT p.*, m.nome AS material_nome, um.nome AS unid_medida_nome
            FROM produto p
            LEFT JOIN material m ON p.material_id = m.id
            LEFT JOIN unidMedida um ON p.unid_medida_id = um.id
            WHERE p.nome LIKE ? OR m.nome LIKE ?
        """;
    
        try (Connection connection = DBConnection.getInstance().getConnection();
             PreparedStatement prepareStatement = connection.prepareStatement(consulta)) {

            String busca = "%" + pesquisa + "%";
            prepareStatement.setString(1, busca);
            prepareStatement.setString(2, busca);

            ResultSet resultSet = prepareStatement.executeQuery();
    
            while (resultSet.next()) {
                Produto produto = new Produto();
                produto.setId(resultSet.getLong("id"));
                produto.setNome(resultSet.getString("nome"));
                produto.setModelo(resultSet.getString("modelo"));
                produto.setQuantidadeAtual(resultSet.getInt("quantidadeAtual"));
                produto.setQuantidadeMinima(resultSet.getInt("quantidadeMinima"));
                produto.setCustoUnitario(resultSet.getBigDecimal("custoUnitario"));
                Material material = new Material();
                material.setId(resultSet.getInt("material_id"));
                material.setNome(resultSet.getString("material_nome"));
                produto.setMaterial(material.getId() != 0 ? material : null);
    
                UnidMedida unidMedida = new UnidMedida();
                unidMedida.setId(resultSet.getInt("unid_medida_id"));
                unidMedida.setNome(resultSet.getString("unid_medida_nome"));
                produto.setUnidMedida(unidMedida.getId() != 0 ? unidMedida : null);
    
                lista.add(produto);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return lista;
    }
}
