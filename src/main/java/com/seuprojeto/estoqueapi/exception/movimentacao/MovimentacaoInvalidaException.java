package com.seuprojeto.estoqueapi.exception.movimentacao;

public class MovimentacaoInvalidaException extends RuntimeException {
  public MovimentacaoInvalidaException(String mensagem) {
    super(mensagem);
  }
}
