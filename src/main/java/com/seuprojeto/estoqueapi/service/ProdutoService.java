package com.seuprojeto.estoqueapi.service;

import com.seuprojeto.estoqueapi.domain.Produto;
import com.seuprojeto.estoqueapi.shared.DTO.request.AtualizarProdutoRequest;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ProdutoService {
    Produto cadastrar(Produto produto);

    Produto atualizar(UUID id, AtualizarProdutoRequest dto);

    List<Produto> listar();

    Produto remover(UUID id);

    Optional<Produto> pesquisarPorId(UUID id);

    List<Produto> pesquisar(String descricao, Boolean completado,
                            BigDecimal preco, BigDecimal precoMax, BigDecimal precoMin,
                            Integer quantidade, Integer quantidadeMin, Integer quantidadeMax);
}
