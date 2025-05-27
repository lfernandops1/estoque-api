package com.seuprojeto.estoqueapi.service;

import com.seuprojeto.estoqueapi.domain.Produto;
import com.seuprojeto.estoqueapi.shared.DTO.ProdutoFiltroDTO;
import com.seuprojeto.estoqueapi.shared.DTO.request.AtualizarProdutoRequest;

import java.util.List;
import java.util.UUID;

public interface ProdutoService {
    Produto cadastrar(Produto produto);

    Produto atualizar(UUID id, AtualizarProdutoRequest dto);

    List<Produto> listar();

    Produto remover(UUID id);

    Produto pesquisarPorId(UUID id);

    List<Produto> pesquisar(ProdutoFiltroDTO produtoFiltroDTO);
}
