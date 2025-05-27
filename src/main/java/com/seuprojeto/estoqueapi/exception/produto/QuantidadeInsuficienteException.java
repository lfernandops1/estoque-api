package com.seuprojeto.estoqueapi.exception.produto;

import java.util.UUID;

public class QuantidadeInsuficienteException extends RuntimeException {
    public QuantidadeInsuficienteException(UUID produtoId) {
        super("Quantidade insuficiente no estoque para o produto com ID: " + produtoId);
    }
}
