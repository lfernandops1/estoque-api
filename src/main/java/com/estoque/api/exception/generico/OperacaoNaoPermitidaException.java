package com.estoque.api.exception.generico;

public class OperacaoNaoPermitidaException extends RuntimeException {
  public OperacaoNaoPermitidaException(String mensagem) {
    super(mensagem);
  }
}
