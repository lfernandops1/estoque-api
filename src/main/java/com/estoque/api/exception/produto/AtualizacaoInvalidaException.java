package com.estoque.api.exception.produto;

public class AtualizacaoInvalidaException extends RuntimeException {
  public AtualizacaoInvalidaException(String message) {
    super(message);
  }
}
