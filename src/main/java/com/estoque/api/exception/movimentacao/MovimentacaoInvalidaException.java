package com.estoque.api.exception.movimentacao;

public class MovimentacaoInvalidaException extends RuntimeException {
  public MovimentacaoInvalidaException() {
    super("Tipo de Movimentação inválida");
  }
}
