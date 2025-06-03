package com.estoque.api.exception.produto;

import java.util.UUID;

public class ProdutoNaoEncontradoException extends RuntimeException {
  public ProdutoNaoEncontradoException(UUID id) {
    super("Produto com ID " + id + " não encontrado.");
  }
}
