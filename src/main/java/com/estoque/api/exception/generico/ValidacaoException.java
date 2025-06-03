package com.estoque.api.exception.generico;

public class ValidacaoException extends RuntimeException {
  public ValidacaoException(String mensagem) {
    super(mensagem);
  }
}
