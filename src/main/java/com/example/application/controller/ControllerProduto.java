package com.example.application.controller;

import java.util.List;

import com.example.application.model.Cliente;
import com.example.application.model.Produto;
import com.example.application.repository.DaoProduto;

public class ControllerProduto {
    DaoProduto dao = new DaoProduto();

    public boolean inserir(Produto produto){
        return dao.inserir(produto);
    }

    public boolean alterar(Produto produto){
        return dao.alterar(produto);
    }

    public boolean excluir(Produto produto){
        return dao.excluir(produto);
    }

    public List<Produto> pesquisarTodos(){
        return dao.pesquisarTodos();
    }

    public List<Produto> pesquisarProduto(String pesquisa){
        return dao.pesquisarProduto(pesquisa);
    }

    public Produto getProdutoById(int id) {
        return dao.getProdutoById(id);
    }

    public List<Produto> pesquisarProdutoComQuantidadeMinimaIgual() {
        return dao.pesquisarProdutoComQuantidadeMinimaIgual();
    }
}
