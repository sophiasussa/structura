package com.example.application.controller;

import java.util.ArrayList;
import java.util.List;

import com.example.application.model.Funcionario;
import com.example.application.repository.FuncionarioRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.sql.SQLException;

public class FuncionarioController {

    private static final Logger logger = LoggerFactory.getLogger(FuncionarioController.class);
    private FuncionarioRepository daoFuncionario;

    public FuncionarioController() {
        try {
            this.daoFuncionario = new FuncionarioRepository();
        } catch (SQLException e) {
            logger.error("Erro ao conectar ao banco de dados", e);
            throw new RuntimeException("Erro ao conectar ao banco de dados", e);
        }
    }

    private boolean validarFuncionario(Funcionario funcionario) {
        if (funcionario == null) {
            logger.warn("Tentativa de inserir funcionário com valor nulo");
            return false;
        }
        if (funcionario.getNome() == null || funcionario.getNome().isEmpty()) {
            logger.warn("Nome do funcionário é obrigatório");
            return false;
        }
        if (funcionario.getNome().length() > 100) {
            logger.warn("Nome do funcionário não pode ter mais de 100 caracteres");
            return false;
        }
        if (funcionario.getCpf().length() > 20) {
            logger.warn("CPF do funcionário não pode ter mais de 20 caracteres");
            return false;
        }
        if (funcionario.getRg().length() > 20) {
            logger.warn("RG do funcionário não pode ter mais de 20 caracteres");
            return false;
        }
        if (funcionario.getTelefone() == null || funcionario.getTelefone().isEmpty()) {
            logger.warn("Telefone do funcionário é obrigatório");
            return false;
        }
        if (funcionario.getTelefone().length() > 20) {
            logger.warn("Telefone do funcionário não pode ter mais de 20 caracteres");
            return false;
        }
        return true;
    }

    public boolean inserir(Funcionario funcionario) {
        if (!validarFuncionario(funcionario)) {
            return false;
        }
        try {
            boolean sucesso = daoFuncionario.inserir(funcionario);
            if (sucesso) {
                logger.info("Funcionário inserido com sucesso: " + funcionario.getNome());
            } else {
                logger.warn("Nenhuma linha foi inserida para o funcionário: " + funcionario.getNome());
            }
            return sucesso;
        } catch (Exception e) {
            logger.error("Erro ao inserir funcionário: " + funcionario.getNome(), e);
            return false;
        }
    }

    public boolean alterar(Funcionario funcionario) {
        if (!validarFuncionario(funcionario)) {
            return false;
        }
        try {
            boolean sucesso = daoFuncionario.alterar(funcionario);
            if (sucesso) {
                logger.info("Funcionário atualizado com sucesso: " + funcionario.getNome());
            } else {
                logger.warn("Nenhuma linha atualizada para o funcionário com ID: " + funcionario.getId());
            }
            return sucesso;
        } catch (Exception e) {
            logger.error("Erro ao alterar funcionário: " + funcionario.getNome(), e);
            return false;
        }
    }

    public String excluir(Funcionario funcionario) {
        if (funcionario == null || funcionario.getId() == null) {
            logger.warn("Tentativa de excluir funcionário com valor nulo ou ID inválido");
            return "Funcionário inválido ou não encontrado.";
        } else if (isFuncionarioInUse(funcionario)) {
            logger.warn("Não é possível excluir o funcionário. Ele está associado a um agendamento ou OS.");
            return "Não é possível excluir o funcionário, pois ele está associado a um agendamento ou ordem de serviço.";
        }
        try {
            boolean sucesso = daoFuncionario.excluir(funcionario);
            if (sucesso) {
                logger.info("Funcionário excluído com sucesso: " + funcionario.getId());
                return null;
            } else {
                logger.warn("Nenhuma linha excluída para o funcionário com ID: " + funcionario.getId());
                return "Nenhuma exclusão realizada. Funcionário não encontrado.";
            }
        } catch (Exception e) {
            logger.error("Erro ao excluir funcionário com ID: " + funcionario.getId(), e);
            return "Erro interno ao tentar excluir o funcionário. Por favor, entre em contato com o suporte.";
        }
    }

    public List<Funcionario> pesquisarTodos() {
        try {
            List<Funcionario> lista = daoFuncionario.pesquisarTodos();
            logger.info("Pesquisados " + (lista != null ? lista.size() : 0) + " funcionários.");
            return lista;
        } catch (Exception e) {
            logger.error("Erro ao buscar todos os funcionários", e);
            return new ArrayList<>();
        }
    }

    public Funcionario getFuncionarioById(int id) {
        try {
            Funcionario funcionario = daoFuncionario.getFuncionarioById(id);
            if (funcionario != null) {
                logger.info("Funcionário encontrado pelo ID: " + id);
            } else {
                logger.warn("Nenhum funcionário encontrado com o ID: " + id);
            }
            return funcionario;
        } catch (Exception e) {
            logger.error("Erro ao buscar funcionário com ID: " + id, e);
            return null;
        }
    }

    public List<Funcionario> pesquisarFuncionario(String pesquisa) {
        if (pesquisa == null || pesquisa.isEmpty()) {
            logger.warn("Busca de funcionário com parâmetro de pesquisa inválido");
            return new ArrayList<>();
        }
        try {
            List<Funcionario> lista = daoFuncionario.pesquisarFuncionario(pesquisa);
            logger.info("Pesquisados " + (lista != null ? lista.size() : 0) + " funcionários para a pesquisa: " + pesquisa);
            return lista;
        } catch (Exception e) {
            logger.error("Erro ao buscar funcionário com pesquisa: " + pesquisa, e);
            return new ArrayList<>();
        }
    }

    public boolean isFuncionarioInUse(Funcionario funcionario) {
        try {
            return daoFuncionario.isFuncionarioInUse(funcionario);
        } catch (Exception e) {
            logger.error("Erro ao buscar esse funcionário ", e);
            return false;
        }
    }
}