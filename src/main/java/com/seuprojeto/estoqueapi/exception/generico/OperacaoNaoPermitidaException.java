package com.seuprojeto.estoqueapi.exception.generico;

public class OperacaoNaoPermitidaException extends RuntimeException {
  public OperacaoNaoPermitidaException(String mensagem) {
    super(mensagem);
  }
}
