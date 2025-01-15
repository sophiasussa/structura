package com.example.application.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.example.application.model.Produto;
import com.example.application.model.UnidMedida;
import com.example.application.model.Material;
import com.example.application.model.Modelo;

public class ProdutoRepository {

    private static final Logger logger = LoggerFactory.getLogger(ProdutoRepository.class);
    private Connection connection;

    public ProdutoRepository() throws SQLException {
        this.connection = DBConnection.getInstance().getConnection();
    }

    public boolean inserir(Produto produto) {
        String sql = "INSERT INTO produto (nome, quantidadeAtual, quantidadeMinima, custoUnitario, material_id, unid_medida_id, modelo_id) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, produto.getNome());
            stmt.setObject(2, produto.getQuantidadeAtual());
            stmt.setObject(3, produto.getQuantidadeMinima());
            stmt.setDouble(4, produto.getCustoUnitario());
            stmt.setObject(5, produto.getMaterial() != null ? produto.getMaterial().getId() : null, java.sql.Types.INTEGER);
            stmt.setObject(6, produto.getUnidMedida() != null ? produto.getUnidMedida().getId() : null, java.sql.Types.INTEGER);
            stmt.setObject(7, produto.getModelo() != null ? produto.getModelo().getId() : null, java.sql.Types.INTEGER);

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            logger.error("Erro ao inserir produto: " + produto.getNome(), e);
            throw new RuntimeException("Erro ao processar a solicitação. Tente novamente.", e);
        }
    }

    public boolean alterar(Produto produto) {
        String sql = "UPDATE produto SET nome = ?, quantidadeAtual = ?, quantidadeMinima = ?, custoUnitario = ?, material_id = ?, unid_medida_id = ?, modelo_id = ? WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, produto.getNome());
            stmt.setObject(2, produto.getQuantidadeAtual());
            stmt.setObject(3, produto.getQuantidadeMinima());
            stmt.setDouble(4, produto.getCustoUnitario());
            stmt.setObject(5, produto.getMaterial() != null ? produto.getMaterial().getId() : null, java.sql.Types.INTEGER);
            stmt.setObject(6, produto.getUnidMedida() != null ? produto.getUnidMedida().getId() : null, java.sql.Types.INTEGER);
            stmt.setObject(7, produto.getModelo() != null ? produto.getModelo().getId() : null, java.sql.Types.INTEGER);
            stmt.setLong(8, produto.getId());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            logger.error("Erro ao alterar produto com ID: " + produto.getId(), e);
            throw new RuntimeException("Erro ao processar a solicitação. Tente novamente.", e);
        }
    }

    public boolean excluir(Produto produto) {
        String sql = "DELETE FROM produto WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, produto.getId());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            logger.error("Erro ao excluir produto com ID: " + produto.getId(), e);
            throw new RuntimeException("Erro ao processar a solicitação. Tente novamente.", e);
        }
    }

    public List<Produto> pesquisarTodos() {
        List<Produto> lista = new ArrayList<>();
        String sql = """
            SELECT p.*, m.nome AS material_nome, um.nome AS unid_medida_nome, mo.nome AS modelo_nome
            FROM produto p
            LEFT JOIN material m ON p.material_id = m.id
            LEFT JOIN unidMedida um ON p.unid_medida_id = um.id
            LEFT JOIN modelo mo ON p.modelo_id = mo.id
        """;
        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet resultSet = stmt.executeQuery()) {

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
        } catch (SQLException e) {
            logger.error("Erro ao pesquisar todos os produtos.", e);
            throw new RuntimeException("Erro ao processar a solicitação. Tente novamente.", e);
        }
        return lista;
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
    
        try (PreparedStatement prepareStatement = connection.prepareStatement(consulta)) {
            String busca = "%" + pesquisa + "%";
            prepareStatement.setString(1, busca);
            prepareStatement.setString(2, busca);
    
            try (ResultSet resultSet = prepareStatement.executeQuery()) {
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
            }
        } catch (SQLException e) {
            logger.error("Erro ao pesquisar produtos com a pesquisa: " + pesquisa, e);
            throw new RuntimeException("Erro ao processar a solicitação. Tente novamente.", e);
        }
        return lista;
    }
    

    public Produto getProdutoById(int id) {
        String sql = "SELECT p.*, m.nome AS material_nome, um.nome AS unid_medida_nome, mo.nome AS modelo_nome " +
                     "FROM produto p " +
                     "LEFT JOIN material m ON p.material_id = m.id " +
                     "LEFT JOIN unidMedida um ON p.unid_medida_id = um.id " +
                     "LEFT JOIN modelo mo ON p.modelo_id = mo.id " +
                     "WHERE p.id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet resultSet = stmt.executeQuery();
            if (resultSet.next()) {
                Produto produto = new Produto();
                produto.setId(resultSet.getLong("id"));
                produto.setNome(resultSet.getString("nome") != null ? resultSet.getString("nome") : "Nome não disponível");
                produto.setQuantidadeAtual(resultSet.getInt("quantidadeAtual"));
                produto.setQuantidadeMinima(resultSet.getInt("quantidadeMinima"));
                produto.setCustoUnitario(resultSet.getDouble("custoUnitario"));
    
                Material material = new Material();
                material.setId(resultSet.getInt("material_id"));
                String materialNome = resultSet.getString("material_nome");
                if (materialNome != null) {
                    material.setNome(materialNome);
                } else {
                    material.setNome("N/A");
                }
                produto.setMaterial(material.getId() != 0 ? material : null);
    
                UnidMedida unidMedida = new UnidMedida();
                unidMedida.setId(resultSet.getInt("unid_medida_id"));
                String unidMedidaNome = resultSet.getString("unid_medida_nome");
                if (unidMedidaNome != null) {
                    unidMedida.setNome(unidMedidaNome);
                } else {
                    unidMedida.setNome("N/A");
                }
                produto.setUnidMedida(unidMedida.getId() != 0 ? unidMedida : null);
                
                Modelo modelo = new Modelo();
                modelo.setId(resultSet.getInt("modelo_id"));
                String modeloNome = resultSet.getString("modelo_nome");
                if (modeloNome != null) {
                    modelo.setNome(modeloNome);
                } else {
                    modelo.setNome("N/A");
                }
                produto.setModelo(modelo.getId() != 0 ? modelo : null);
    
                return produto;
            }
        } catch (SQLException e) {
            logger.error("Erro ao buscar produto pelo ID: " + id, e);
            throw new RuntimeException("Erro ao processar a solicitação. Tente novamente.", e);
        }
        return null;
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
    
        try (PreparedStatement prepareStatement = connection.prepareStatement(consulta);
             ResultSet resultSet = prepareStatement.executeQuery()) {
    
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

        } catch (SQLException e) {
            logger.error("Erro ao pesquisar produtos com quantidade mínima igual à atual.", e);
            throw new RuntimeException("Erro ao processar a solicitação. Tente novamente.", e);
        }
        return lista;
    }

    public boolean isProdutoInUse(Produto produto) {
        String sql = "SELECT COUNT(*) FROM produto_os WHERE produto_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, produto.getId());
            ResultSet result = stmt.executeQuery();
            if (result.next()) {
                int count = result.getInt(1);
                return count > 0;
            }
        } catch (SQLException e) {
            logger.error("Erro ao verificar se o produto está em uso.", e);
            throw new RuntimeException("Erro ao processar a solicitação. Tente novamente.", e);
        }
        return false;
    }
    
}