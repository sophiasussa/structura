package com.example.application.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.example.application.model.Material;
import com.example.application.model.OrdemServico;
import com.example.application.model.Produto;
import com.example.application.model.StatusOS;

public class DaoOrdemServico {

    private Connection connection;
    
    public DaoOrdemServico() throws SQLException {
        this.connection = DBConnection.getInstance().getConnection();
    }

    public boolean saveOrdemServico(OrdemServico os, List<Produto> produtos) {
        String sqlOS = "INSERT INTO os (status_os, endereco, imagens, dataa, observacoes, cliente_id, funcionario_id) VALUES (?, ?, ?, ?, ?, ?, ?)";
        String sqlOsProdutos = "INSERT INTO produto_os (os_id, produto_id) VALUES (?, ?)";

        try (PreparedStatement stmtOS = connection.prepareStatement(sqlOS, PreparedStatement.RETURN_GENERATED_KEYS)) {
            stmtOS.setString(1, os.getStatusOS().name());
            stmtOS.setString(2, os.getEndereco());
            stmtOS.setString(3, String.join(",", os.getImagens()));
            stmtOS.setDate(4, java.sql.Date.valueOf(os.getData()));
            stmtOS.setString(5, os.getObservacao());
            stmtOS.setLong(6, os.getCliente().getId());
            stmtOS.setLong(7, os.getFuncionario().getId());

            int rowsInserted = stmtOS.executeUpdate();

            if (rowsInserted > 0) {
                try (ResultSet generatedKeys = stmtOS.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        int idOs = generatedKeys.getInt(1);

                        try (PreparedStatement stmtOsProdutos = connection.prepareStatement(sqlOsProdutos)) {
                            for (Produto produto : produtos) {
                                stmtOsProdutos.setInt(1, idOs);
                                stmtOsProdutos.setLong(2, produto.getId());
                                stmtOsProdutos.addBatch();
                            }
                            stmtOsProdutos.executeBatch();
                        }
                    }
                }
            }
            return false;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean updateOrdemServico(OrdemServico os, List<Produto> produtos) {
        String sqlOS = "UPDATE os SET status_os = ?, endereco = ?, imagens = ?, dataa = ?, observacoes = ?, cliente_id = ?, funcionario_id = ? WHERE id = ?";
        String deleteOsProdutos = "DELETE FROM os_produto WHERE id_os = ?";
        String insertOsProdutos = "INSERT INTO os_produto (id_os, id_produto) VALUES (?, ?)";

        try (PreparedStatement stmtOS = connection.prepareStatement(sqlOS)) {
            stmtOS.setString(1, os.getStatusOS().name());
            stmtOS.setString(2, os.getEndereco());
            stmtOS.setString(3, String.join(",", os.getImagens()));
            stmtOS.setDate(4, java.sql.Date.valueOf(os.getData()));
            stmtOS.setString(5, os.getObservacao());
            stmtOS.setLong(6, os.getCliente().getId());
            stmtOS.setLong(7, os.getFuncionario().getId());
            stmtOS.setLong(8, os.getId());

            int rowsUpdated = stmtOS.executeUpdate();

            if (rowsUpdated > 0) {
                try (PreparedStatement stmtDeleteProduto = connection.prepareStatement(deleteOsProdutos)) {
                    stmtDeleteProduto.setLong(1, os.getId());
                    stmtDeleteProduto.executeUpdate();
                }

                try (PreparedStatement stmtInsertProduto = connection.prepareStatement(insertOsProdutos)) {
                    for (Produto produto : produtos) {
                        stmtInsertProduto.setLong(1, os.getId());
                        stmtInsertProduto.setLong(2, produto.getId());
                        stmtInsertProduto.addBatch();
                    }
                    stmtInsertProduto.executeBatch();
                }
                return true;
            }

            return false;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteOrdemServico(Long idOs) {
        String sqlDeleteProdutos = "DELETE FROM os_produto WHERE id_os = ?";
        String sqlDeleteOS = "DELETE FROM os WHERE id = ?";

        try (PreparedStatement stmtDeleteProdutos = connection.prepareStatement(sqlDeleteProdutos);
                PreparedStatement stmtDeleteOS = connection.prepareStatement(sqlDeleteOS)) {

            stmtDeleteProdutos.setLong(1, idOs);
            stmtDeleteProdutos.executeUpdate();

            stmtDeleteOS.setLong(1, idOs);
            int rowsDeleted = stmtDeleteOS.executeUpdate();

            return rowsDeleted > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<OrdemServico> getAllOrdensServico() {
        List<OrdemServico> ordens = new ArrayList<>();
        String sqlOS = "SELECT * FROM os";
        String sqlProdutos = "SELECT p.* FROM produto p " +
                            "JOIN produto_os po ON p.id = po.produto_id " +
                            "WHERE po.os_id = ?";

        try (PreparedStatement stmtOS = connection.prepareStatement(sqlOS);
            ResultSet rsOS = stmtOS.executeQuery()) {

            while (rsOS.next()) {
                OrdemServico os = new OrdemServico();
                os.setId(rsOS.getLong("id"));
                os.setStatusOS(StatusOS.valueOf(rsOS.getString("status_os")));
                os.setEndereco(rsOS.getString("endereco"));
                String imagens = rsOS.getString("imagens");
                os.setImagens(imagens != null ? Arrays.asList(imagens.split(",")) : new ArrayList<>());
                os.setData(rsOS.getDate("dataa").toLocalDate());
                os.setObservacao(rsOS.getString("observacoes"));
                os.setCliente(new DaoCliente().getClienteById(rsOS.getInt("cliente_id")));
                os.setFuncionario(new DaoFuncionario().getFuncionarioById(rsOS.getInt("funcionario_id")));

                List<Produto> produtos = new ArrayList<>();
                try (PreparedStatement stmtProdutos = connection.prepareStatement(sqlProdutos)) {
                    stmtProdutos.setLong(1, os.getId());
                    try (ResultSet rsProdutos = stmtProdutos.executeQuery()) {
                        while (rsProdutos.next()) {
                            Produto produto = new Produto();
                            produto.setId(rsProdutos.getLong("id"));
                            produto.setNome(rsProdutos.getString("nome"));
                            Material material = new Material();
                            material.setId(rsProdutos.getInt("material_id"));
                            material.setNome(rsProdutos.getString("material_nome"));
                            produto.setMaterial(material.getId() != 0 ? material : null);
                            produtos.add(produto);
                        }
                    }
                }
                ordens.add(os);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ordens;
    }

    public List<OrdemServico> searchOS(String searchTerm) {
        List<OrdemServico> ordens = new ArrayList<>();
        String sqlOS = "SELECT * FROM os WHERE status_os LIKE ?";
        String sqlOSCliente = "SELECT * FROM os WHERE cliente_id LIKE ?";
        String sqlProdutos = "SELECT p.* FROM produto p " +
                "JOIN os_produto op ON p.id = op.id_produto " +
                "WHERE op.id_os = ?";

        try (PreparedStatement stmtOS = connection.prepareStatement(sqlOS)) {
            stmtOS.setString(1, "%" + searchTerm + "%");
            ResultSet rsOS = stmtOS.executeQuery();

            while (rsOS.next()) {
                OrdemServico os = new OrdemServico();
                os.setStatusOS(StatusOS.valueOf(rsOS.getString("status_os")));
                os.setEndereco(rsOS.getString("endereco"));
                os.setImagens(Arrays.asList(rsOS.getString("imagens").split(",")));
                java.sql.Date dataSql = rsOS.getDate("dataa");
                os.setData(dataSql != null ? dataSql.toLocalDate() : null);
                os.setObservacao(rsOS.getString("observacao"));
                os.setCliente(new DaoCliente().getClienteById(rsOS.getInt("id_cliente")));
                os.setFuncionario(new DaoFuncionario().getFuncionarioById(rsOS.getInt("id_mecanico")));

                ordens.add(os);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return ordens;
    }

    public OrdemServico OrdemServicoById(Long id) {
        String sql = "SELECT os.*, c.*, m.*, v.* " +
                "FROM os " +
                "LEFT JOIN cliente c ON os.cliente_id = c.id " +
                "LEFT JOIN funcionario m ON os.funcionario_id = m.id " +
                "WHERE os.id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, id);
            ResultSet result = stmt.executeQuery();

            if (result.next()) {
                OrdemServico os = new OrdemServico();
                os.setStatusOS(StatusOS.valueOf(result.getString("status_os")));
                os.setEndereco(result.getString("endereco"));
                os.setImagens(Arrays.asList(result.getString("imagens").split(",")));
                java.sql.Date dataSql = result.getDate("dataa");
                os.setData(dataSql != null ? dataSql.toLocalDate() : null);
                os.setObservacao(result.getString("observacao"));
                os.setCliente(new DaoCliente().getClienteById(result.getInt("id_cliente")));
                os.setFuncionario(new DaoFuncionario().getFuncionarioById(result.getInt("id_mecanico")));

                return os;
            } else {
                return null;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
}
