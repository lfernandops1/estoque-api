package com.seuprojeto.estoqueapi.exception.movimentacao;

import java.util.UUID;

public class MovimentacaoNaoEncontradaException extends RuntimeException {
  public MovimentacaoNaoEncontradaException(UUID id) {
    super("Movimentação não encontrada com id: " + id);
  }
}
