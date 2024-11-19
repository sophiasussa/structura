package com.example.application.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.example.application.model.Cor;
import com.example.application.model.Pedra;
import com.example.application.model.UnidMedida;
import com.example.application.model.Material;

public class DaoPedra {

    public boolean inserir(Pedra pedra){
        try{
            Connection connection = DBConnection.getInstance().getConnection();
            String insert = "INSERT INTO pedra (nome, modelo, quantidadeAtual, quantidadeMinima, custoUnitario, cor, material, unidMedida) VALUE (?,?,?,?,?,?,?,?)";
            PreparedStatement prepareStatement = connection.prepareStatement(insert);
            prepareStatement.setString(1, pedra.getNome());
            prepareStatement.setString(2, pedra.getModelo());
            prepareStatement.setInt(3, pedra.getQuantidadeAtual());
            prepareStatement.setInt(4, pedra.getQuantidadeMinima());
            prepareStatement.setBigDecimal(5, pedra.getCustoUnitario());
            prepareStatement.setInt(6, pedra.getCor().getId());
            prepareStatement.setInt(7, pedra.getMaterial().getId());
            prepareStatement.setInt(8, pedra.getUnidMedida().getId());
            int resultado = prepareStatement.executeUpdate();
            return resultado > 0;
        }catch(Exception e){
            System.out.println(e.getMessage());
            return false;
        }
    }

    public boolean alterar(Pedra pedra){
        try{

            Connection connection = DBConnection.getInstance().getConnection();
            String update = "UPDATE pedra SET nome = ?, modelo = ?, quantidadeAtual = ?, quantidadeMinima = ?, custoUnitario = ?, cor = ?, material = ?, unidMedida = ? WHERE id = ?";
            
            PreparedStatement prepareStatement = connection.prepareStatement(update);
            prepareStatement.setString(1, pedra.getNome());
            prepareStatement.setString(2, pedra.getModelo());
            prepareStatement.setInt(3, pedra.getQuantidadeAtual());
            prepareStatement.setInt(4, pedra.getQuantidadeMinima());
            prepareStatement.setBigDecimal(5, pedra.getCustoUnitario());
            prepareStatement.setInt(6, pedra.getCor().getId());
            prepareStatement.setInt(7, pedra.getMaterial().getId());
            prepareStatement.setInt(8, pedra.getUnidMedida().getId());
            prepareStatement.setInt(9, pedra.getId());
            int resultado = prepareStatement.executeUpdate();
            return resultado > 0;
        } catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    public boolean excluir(Pedra pedra) {
        try {
            Connection connection = DBConnection.getInstance().getConnection();
            String delete = "DELETE FROM pedra WHERE id = ?";
            PreparedStatement prepareStatement = connection.prepareStatement(delete);
            prepareStatement.setLong(1, pedra.getId());
            int resultado = prepareStatement.executeUpdate();
            return resultado > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public List<Pedra> pesquisarTodos() {
        try {
            Connection connection = DBConnection.getInstance().getConnection();
            String consulta = "SELECT * from pedra";
            List<Pedra> lista = new ArrayList<>();
            PreparedStatement prepareStatement = connection.prepareStatement(consulta);
            ResultSet resultSet = prepareStatement.executeQuery();
            while (resultSet.next()) {
                Pedra pedra = new Pedra();
                pedra.setId(resultSet.getInt("id"));
                pedra.setNome(resultSet.getString("nome"));
                pedra.setModelo(resultSet.getString("modelo"));
                pedra.setQuantidadeAtual(resultSet.getInt("quantidadeAtual"));
                pedra.setQuantidadeMinima(resultSet.getInt("quantidadeMinima"));
                pedra.setCustoUnitario(resultSet.getBigDecimal("custoUnitario"));
                Cor cor = new Cor();
                cor.setId(resultSet.getInt("cor_id"));
                pedra.setCor(cor);

                Material material = new Material();
                material.setId(resultSet.getInt("material_id"));
                pedra.setMaterial(material);

                UnidMedida unidMedida = new UnidMedida();
                unidMedida.setId(resultSet.getInt("unid_medida_id"));
                pedra.setUnidMedida(unidMedida);

                lista.add(pedra);
            }
            return lista;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<Pedra> pesquisarPedra(String pesquisa) {
        List<Pedra> lista = new ArrayList<>();
        String consulta = "SELECT * FROM pedra WHERE nome LIKE ?";
    
        try (Connection connection = DBConnection.getInstance().getConnection();
             PreparedStatement prepareStatement = connection.prepareStatement(consulta)) {
    
            String busca = "%" + pesquisa + "%";
            prepareStatement.setString(1, busca);
    
            ResultSet resultSet = prepareStatement.executeQuery();
    
            while (resultSet.next()) {
                Pedra pedra = new Pedra();
                pedra.setId(resultSet.getInt("id"));
                pedra.setNome(resultSet.getString("nome"));
                pedra.setModelo(resultSet.getString("modelo"));
                pedra.setQuantidadeAtual(resultSet.getInt("quantidadeAtual"));
                pedra.setQuantidadeMinima(resultSet.getInt("quantidadeMinima"));
                pedra.setCustoUnitario(resultSet.getBigDecimal("custoUnitario"));
                Cor cor = new Cor();
                cor.setId(resultSet.getInt("cor_id"));
                pedra.setCor(cor);
    
                Material material = new Material();
                material.setId(resultSet.getInt("material_id"));
                pedra.setMaterial(material);
    
                UnidMedida unidMedida = new UnidMedida();
                unidMedida.setId(resultSet.getInt("unid_medida_id"));
                pedra.setUnidMedida(unidMedida);
    
                lista.add(pedra);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return lista;
    }
}
