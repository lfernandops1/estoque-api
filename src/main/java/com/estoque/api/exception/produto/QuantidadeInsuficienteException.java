package com.estoque.api.exception.produto;

import java.util.UUID;

public class QuantidadeInsuficienteException extends RuntimeException {
  public QuantidadeInsuficienteException(Integer quantidade, UUID produtoId) {
    super(
        "Quantidade:"
            + quantidade
            + " insuficiente no estoque para o produto com ID: "
            + produtoId);
  }
}
