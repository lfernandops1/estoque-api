package com.estoque.api.service;

import com.estoque.api.domain.Produto;
import com.estoque.api.shared.DTO.ProdutoFiltroDTO;
import com.estoque.api.shared.DTO.request.AtualizarProdutoRequest;
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
