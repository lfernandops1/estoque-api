package com.estoque.api.exception.generico;

public class ErroDeIntegridadeException extends RuntimeException {
  public ErroDeIntegridadeException(String mensagem) {
    super(mensagem);
  }
}
