package com.seuprojeto.estoqueapi.exception.generico;

public class ErroDeIntegridadeException extends RuntimeException {
  public ErroDeIntegridadeException(String mensagem) {
    super(mensagem);
  }
}
