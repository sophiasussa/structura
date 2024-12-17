package com.example.application.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.example.application.model.Produto;
import com.example.application.model.UnidMedida;
import com.example.application.model.Funcionario;
import com.example.application.model.Material;
import com.example.application.model.Modelo;

public class DaoProduto {

    public boolean inserir(Produto produto){
        try{
            Connection connection = DBConnection.getInstance().getConnection();
            String insert = "INSERT INTO produto (nome, quantidadeAtual, quantidadeMinima, custoUnitario, material_id, unid_medida_id, modelo_id) VALUE (?,?,?,?,?,?,?)";
            PreparedStatement prepareStatement = connection.prepareStatement(insert);
            prepareStatement.setString(1, produto.getNome());
            prepareStatement.setObject(2, produto.getQuantidadeAtual());
            prepareStatement.setObject(3, produto.getQuantidadeMinima());
            prepareStatement.setDouble(4, produto.getCustoUnitario());
            prepareStatement.setObject(5, produto.getMaterial() != null ? produto.getMaterial().getId() : null, java.sql.Types.INTEGER);
            prepareStatement.setObject(6, produto.getUnidMedida() != null ? produto.getUnidMedida().getId() : null, java.sql.Types.INTEGER);
            prepareStatement.setObject(7, produto.getModelo() != null ? produto.getModelo().getId() : null, java.sql.Types.INTEGER);
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
            String update = "UPDATE produto SET nome = ?, quantidadeAtual = ?, quantidadeMinima = ?, custoUnitario = ?, material_id = ?, unid_medida_id = ?, modelo_id = ? WHERE id = ?";
            
            PreparedStatement prepareStatement = connection.prepareStatement(update);
            prepareStatement.setString(1, produto.getNome());
            prepareStatement.setObject(2, produto.getQuantidadeAtual());
            prepareStatement.setObject(3, produto.getQuantidadeMinima());
            prepareStatement.setDouble(4, produto.getCustoUnitario());
            prepareStatement.setObject(5, produto.getMaterial() != null ? produto.getMaterial().getId() : null, java.sql.Types.INTEGER);
            prepareStatement.setObject(6, produto.getUnidMedida() != null ? produto.getUnidMedida().getId() : null, java.sql.Types.INTEGER);
            prepareStatement.setObject(7, produto.getModelo() != null ? produto.getModelo().getId() : null, java.sql.Types.INTEGER);
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
                SELECT p.*, m.nome AS material_nome, um.nome AS unid_medida_nome, mo.nome AS modelo_nome
                FROM produto p
                LEFT JOIN material m ON p.material_id = m.id
                LEFT JOIN unidMedida um ON p.unid_medida_id = um.id
                LEFT JOIN modelo mo ON p.modelo_id = mo.id
            """;
            List<Produto> lista = new ArrayList<>();
            PreparedStatement prepareStatement = connection.prepareStatement(consulta);
            ResultSet resultSet = prepareStatement.executeQuery();
    
            while (resultSet.next()) {
                Produto produto = new Produto();
                produto.setId(resultSet.getLong("id"));
                produto.setNome(resultSet.getString("nome"));
                produto.setQuantidadeAtual(resultSet.getInt("quantidadeAtual"));
                produto.setQuantidadeMinima(resultSet.getInt("quantidadeMinima"));
                produto.setCustoUnitario(resultSet.getDouble("custoUnitario"));
                Material material = new Material();
                material.setId(resultSet.getInt("material_id"));
                material.setNome(resultSet.getString("material_nome"));
                produto.setMaterial(material.getId() != 0 ? material : null);

                UnidMedida unidMedida = new UnidMedida();
                unidMedida.setId(resultSet.getInt("unid_medida_id"));
                unidMedida.setNome(resultSet.getString("unid_medida_nome"));
                produto.setUnidMedida(unidMedida.getId() != 0 ? unidMedida : null);

                Modelo modelo = new Modelo();
                modelo.setId(resultSet.getInt("modelo_id"));
                modelo.setNome(resultSet.getString("modelo_nome"));
                produto.setModelo(modelo.getId() != 0 ? modelo : null);
    
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
            SELECT p.*, m.nome AS material_nome, um.nome AS unid_medida_nome, mo.nome AS modelo_nome
            FROM produto p
            LEFT JOIN material m ON p.material_id = m.id
            LEFT JOIN unidMedida um ON p.unid_medida_id = um.id
            LEFT JOIN modelo mo ON p.modelo_id = mo.id
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
                produto.setQuantidadeAtual(resultSet.getInt("quantidadeAtual"));
                produto.setQuantidadeMinima(resultSet.getInt("quantidadeMinima"));
                produto.setCustoUnitario(resultSet.getDouble("custoUnitario"));
                Material material = new Material();
                material.setId(resultSet.getInt("material_id"));
                material.setNome(resultSet.getString("material_nome"));
                produto.setMaterial(material.getId() != 0 ? material : null);
    
                UnidMedida unidMedida = new UnidMedida();
                unidMedida.setId(resultSet.getInt("unid_medida_id"));
                unidMedida.setNome(resultSet.getString("unid_medida_nome"));
                produto.setUnidMedida(unidMedida.getId() != 0 ? unidMedida : null);

                Modelo modelo = new Modelo();
                modelo.setId(resultSet.getInt("modelo_id"));
                modelo.setNome(resultSet.getString("modelo_nome"));
                produto.setModelo(modelo.getId() != 0 ? modelo : null);
    
                lista.add(produto);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return lista;
    }

    public Produto getProdutoById(int id) {
        String sql = "SELECT * FROM produto WHERE id = ?";
        try (Connection connection = DBConnection.getInstance().getConnection();
             PreparedStatement prepareStatement = connection.prepareStatement(sql)) {
            prepareStatement.setInt(1, id);
            ResultSet resultSet = prepareStatement.executeQuery();
            if (resultSet.next()) {
                Produto produto = new Produto();
                produto.setId(resultSet.getLong("id"));
                produto.setNome(resultSet.getString("nome"));
                produto.setQuantidadeAtual(resultSet.getInt("quantidadeAtual"));
                produto.setQuantidadeMinima(resultSet.getInt("quantidadeMinima"));
                produto.setCustoUnitario(resultSet.getDouble("custoUnitario"));
                Material material = new Material();
                material.setId(resultSet.getInt("material_id"));
                material.setNome(resultSet.getString("material_nome"));
                produto.setMaterial(material.getId() != 0 ? material : null);
    
                UnidMedida unidMedida = new UnidMedida();
                unidMedida.setId(resultSet.getInt("unid_medida_id"));
                unidMedida.setNome(resultSet.getString("unid_medida_nome"));
                produto.setUnidMedida(unidMedida.getId() != 0 ? unidMedida : null);

                Modelo modelo = new Modelo();
                modelo.setId(resultSet.getInt("modelo_id"));
                modelo.setNome(resultSet.getString("modelo_nome"));
                produto.setModelo(modelo.getId() != 0 ? modelo : null);
                return produto;
            } else {
                return null;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<Produto> pesquisarProdutoComQuantidadeMinimaIgual() {
        List<Produto> lista = new ArrayList<>();
        String consulta = """
            SELECT p.*, m.nome AS material_nome, um.nome AS unid_medida_nome, mo.nome AS modelo_nome
            FROM produto p
            LEFT JOIN material m ON p.material_id = m.id
            LEFT JOIN unidMedida um ON p.unid_medida_id = um.id
            LEFT JOIN modelo mo ON p.modelo_id = mo.id
            WHERE p.quantidadeAtual = p.quantidadeMinima
        """;
    
        try (Connection connection = DBConnection.getInstance().getConnection();
             PreparedStatement prepareStatement = connection.prepareStatement(consulta)) {
    
            ResultSet resultSet = prepareStatement.executeQuery();
    
            while (resultSet.next()) {
                Produto produto = new Produto();
                produto.setId(resultSet.getLong("id"));
                produto.setNome(resultSet.getString("nome"));
                produto.setQuantidadeAtual(resultSet.getInt("quantidadeAtual"));
                produto.setQuantidadeMinima(resultSet.getInt("quantidadeMinima"));
                produto.setCustoUnitario(resultSet.getDouble("custoUnitario"));
    
                Material material = new Material();
                material.setId(resultSet.getInt("material_id"));
                material.setNome(resultSet.getString("material_nome"));
                produto.setMaterial(material.getId() != 0 ? material : null);
    
                UnidMedida unidMedida = new UnidMedida();
                unidMedida.setId(resultSet.getInt("unid_medida_id"));
                unidMedida.setNome(resultSet.getString("unid_medida_nome"));
                produto.setUnidMedida(unidMedida.getId() != 0 ? unidMedida : null);
    
                Modelo modelo = new Modelo();
                modelo.setId(resultSet.getInt("modelo_id"));
                modelo.setNome(resultSet.getString("modelo_nome"));
                produto.setModelo(modelo.getId() != 0 ? modelo : null);
    
                lista.add(produto);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return lista;
    }
    
}
