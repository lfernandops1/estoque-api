package com.seuprojeto.estoqueapi.exception.generico;

public class ValidacaoException extends RuntimeException {
    public ValidacaoException(String mensagem) {
        super(mensagem);
    }
}
