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
import com.example.application.model.StatusAgenda;
import com.example.application.model.StatusOS;

public class OrdemServicoRepository {

    private Connection connection;
    
    public OrdemServicoRepository() throws SQLException {
        this.connection = DBConnection.getInstance().getConnection();
    }

    public long saveOrdemServico(OrdemServico os) throws SQLException {
        String sql = "INSERT INTO os (status_os, endereco, imagens, dataa, datap, observacoes, cliente_id, funcionario_id) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
    
        try (PreparedStatement stmtOS = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
            stmtOS.setString(1, os.getStatusOS() != null ? os.getStatusOS().name() : null);
            stmtOS.setString(2, os.getEndereco());
            stmtOS.setString(3, os.getImagens() != null ? String.join(",", os.getImagens()) : null);
            stmtOS.setDate(4, os.getDataAbertura() != null ? java.sql.Date.valueOf(os.getDataAbertura()) : null);
            stmtOS.setDate(5, os.getDataPrevFinaliza() != null ? java.sql.Date.valueOf(os.getDataPrevFinaliza()) : null);
            stmtOS.setString(6, os.getObservacao());
            stmtOS.setLong(7, os.getCliente().getId());
            stmtOS.setLong(8, os.getFuncionario() != null ? os.getFuncionario().getId() : null);
    
            int rowsInserted = stmtOS.executeUpdate();
    
            if (rowsInserted > 0) {
                try (ResultSet generatedKeys = stmtOS.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        return generatedKeys.getLong(1);
                    }
                }
            }
        }
        return -1;
    }
    
    public boolean updateOrdemServico(OrdemServico os, List<Produto> produtos) {
        String sqlOS = "UPDATE os SET status_os = ?, endereco = ?, imagens = ?, dataa = ?, datap = ?, observacoes = ?, cliente_id = ?, funcionario_id = ? WHERE id = ?";
        String deleteOsProdutos = "DELETE FROM os_produto WHERE id_os = ?";
        String insertOsProdutos = "INSERT INTO os_produto (id_os, id_produto) VALUES (?, ?)";

        try (PreparedStatement stmtOS = connection.prepareStatement(sqlOS)) {
            stmtOS.setString(1, os.getStatusOS().name());
            stmtOS.setString(2, os.getEndereco());
            stmtOS.setString(3, String.join(",", os.getImagens()));
            stmtOS.setDate(4, java.sql.Date.valueOf(os.getDataAbertura()));
            stmtOS.setDate(5, java.sql.Date.valueOf(os.getDataPrevFinaliza()));
            stmtOS.setString(6, os.getObservacao());
            stmtOS.setLong(7, os.getCliente().getId());
            stmtOS.setLong(8, os.getFuncionario().getId());
            stmtOS.setLong(9, os.getId());

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
                String status = rsOS.getString("status_os");
                os.setStatusOS(status != null ? StatusOS.valueOf(status) : null);
                os.setEndereco(rsOS.getString("endereco"));
                String imagens = rsOS.getString("imagens");
                os.setImagens(imagens != null ? Arrays.asList(imagens.split(",")) : new ArrayList<>());
                java.sql.Date dataSql = rsOS.getDate("dataa");
                os.setDataAbertura(dataSql != null ? dataSql.toLocalDate() : null);
                os.setDataPrevFinaliza(dataSql != null ? dataSql.toLocalDate() : null);
                os.setObservacao(rsOS.getString("observacoes"));
                os.setCliente(new ClienteRepository().getClienteById(rsOS.getInt("cliente_id")));
                os.setFuncionario(new FuncionarioRepository().getFuncionarioById(rsOS.getInt("funcionario_id")));

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
                String status = rsOS.getString("status_os");
                os.setStatusOS(status != null ? StatusOS.valueOf(status) : null);
                os.setEndereco(rsOS.getString("endereco"));
                os.setImagens(Arrays.asList(rsOS.getString("imagens").split(",")));
                java.sql.Date dataSql = rsOS.getDate("dataa");
                os.setDataAbertura(dataSql != null ? dataSql.toLocalDate() : null);
                os.setDataPrevFinaliza(dataSql != null ? dataSql.toLocalDate() : null);
                os.setObservacao(rsOS.getString("observacao"));
                os.setCliente(new ClienteRepository().getClienteById(rsOS.getInt("id_cliente")));
                os.setFuncionario(new FuncionarioRepository().getFuncionarioById(rsOS.getInt("id_mecanico")));

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
                os.setDataAbertura(dataSql != null ? dataSql.toLocalDate() : null);
                os.setDataPrevFinaliza(dataSql != null ? dataSql.toLocalDate() : null);
                os.setObservacao(result.getString("observacao"));
                os.setCliente(new ClienteRepository().getClienteById(result.getInt("id_cliente")));
                os.setFuncionario(new FuncionarioRepository().getFuncionarioById(result.getInt("id_mecanico")));

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
