package com.example.application.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.example.application.model.Cor;
import com.example.application.model.Produto;
import com.example.application.model.UnidMedida;
import com.example.application.model.Material;

public class DaoProduto {

    public boolean inserir(Produto produto){
        try{
            Connection connection = DBConnection.getInstance().getConnection();
            String insert = "INSERT INTO produto (nome, modelo, quantidadeAtual, quantidadeMinima, custoUnitario, cor_id, material_id, unid_medida_id) VALUE (?,?,?,?,?,?,?,?)";
            PreparedStatement prepareStatement = connection.prepareStatement(insert);
            prepareStatement.setString(1, produto.getNome());
            prepareStatement.setString(2, produto.getModelo());
            prepareStatement.setInt(3, produto.getQuantidadeAtual());
            prepareStatement.setInt(4, produto.getQuantidadeMinima());
            prepareStatement.setBigDecimal(5, produto.getCustoUnitario());
            prepareStatement.setObject(6, produto.getCor() != null ? produto.getCor().getId() : null, java.sql.Types.INTEGER);
            prepareStatement.setObject(7, produto.getMaterial() != null ? produto.getMaterial().getId() : null, java.sql.Types.INTEGER);
            prepareStatement.setObject(8, produto.getUnidMedida() != null ? produto.getUnidMedida().getId() : null, java.sql.Types.INTEGER);
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
            String update = "UPDATE produto SET nome = ?, modelo = ?, quantidadeAtual = ?, quantidadeMinima = ?, custoUnitario = ?, cor_id = ?, material_id = ?, unid_medida_id = ? WHERE id = ?";
            
            PreparedStatement prepareStatement = connection.prepareStatement(update);
            prepareStatement.setString(1, produto.getNome());
            prepareStatement.setString(2, produto.getModelo());
            prepareStatement.setInt(3, produto.getQuantidadeAtual());
            prepareStatement.setInt(4, produto.getQuantidadeMinima());
            prepareStatement.setBigDecimal(5, produto.getCustoUnitario());
            prepareStatement.setInt(6, produto.getCor().getId());
            prepareStatement.setInt(7, produto.getMaterial().getId());
            prepareStatement.setInt(8, produto.getUnidMedida().getId());
            prepareStatement.setLong(9, produto.getId());
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
                SELECT p.*, c.nome AS cor_nome, m.nome AS material_nome, um.nome AS unid_medida_nome
                FROM produto p
                LEFT JOIN cor c ON p.cor_id = c.id
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
                Cor cor = new Cor();
                cor.setId(resultSet.getInt("cor_id"));
                cor.setNome(resultSet.getString("cor_nome"));
                produto.setCor(cor.getId() != 0 ? cor : null);
    
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
        String consulta = "SELECT * FROM produto WHERE nome LIKE ?";
    
        try (Connection connection = DBConnection.getInstance().getConnection();
             PreparedStatement prepareStatement = connection.prepareStatement(consulta)) {
    
            String busca = "%" + pesquisa + "%";
            prepareStatement.setString(1, busca);
    
            ResultSet resultSet = prepareStatement.executeQuery();
    
            while (resultSet.next()) {
                Produto produto = new Produto();
                produto.setId(resultSet.getLong("id"));
                produto.setNome(resultSet.getString("nome"));
                produto.setModelo(resultSet.getString("modelo"));
                produto.setQuantidadeAtual(resultSet.getInt("quantidadeAtual"));
                produto.setQuantidadeMinima(resultSet.getInt("quantidadeMinima"));
                produto.setCustoUnitario(resultSet.getBigDecimal("custoUnitario"));
                Cor cor = new Cor();
                cor.setId(resultSet.getInt("cor_id"));
                cor.setNome(resultSet.getString("cor_nome"));
                produto.setCor(cor);
    
                Material material = new Material();
                material.setId(resultSet.getInt("material_id"));
                produto.setMaterial(material);
    
                UnidMedida unidMedida = new UnidMedida();
                unidMedida.setId(resultSet.getInt("unid_medida_id"));
                produto.setUnidMedida(unidMedida);
    
                lista.add(produto);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return lista;
    }
}
