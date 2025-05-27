package com.seuprojeto.estoqueapi.handler;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.seuprojeto.estoqueapi.exception.generico.ErroDeIntegridadeException;
import com.seuprojeto.estoqueapi.exception.generico.OperacaoNaoPermitidaException;
import com.seuprojeto.estoqueapi.exception.generico.ValidacaoException;
import com.seuprojeto.estoqueapi.exception.movimentacao.MovimentacaoInvalidaException;
import com.seuprojeto.estoqueapi.exception.produto.ProdutoNaoEncontradoException;
import com.seuprojeto.estoqueapi.exception.produto.QuantidadeInsuficienteException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<String> handleHttpMessageNotReadable(HttpMessageNotReadableException ex) {
        if (ex.getCause() instanceof InvalidFormatException invalidFormat && invalidFormat.getTargetType().isEnum()) {
            String field = invalidFormat.getPath().get(0).getFieldName();
            String enumValues = Arrays.stream(invalidFormat.getTargetType().getEnumConstants())
                    .map(Object::toString)
                    .collect(Collectors.joining(", "));

            String msg = String.format("Valor inválido para o campo '%s'. Valores válidos: [%s]", field, enumValues);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(msg);
        }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Erro de leitura do corpo da requisição.");
    }

    @ExceptionHandler(MovimentacaoInvalidaException.class)
    public ResponseEntity<String> handleMovimentacaoInvalida(MovimentacaoInvalidaException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }

    @ExceptionHandler(OperacaoNaoPermitidaException.class)
    public ResponseEntity<String> handleOperacaoNaoPermitida(OperacaoNaoPermitidaException ex) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ex.getMessage());
    }

    @ExceptionHandler(ErroDeIntegridadeException.class)
    public ResponseEntity<String> handleErroDeIntegridade(ErroDeIntegridadeException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(ex.getMessage());
    }

    @ExceptionHandler(ValidacaoException.class)
    public ResponseEntity<String> handleValidacao(ValidacaoException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }

    @ExceptionHandler(ProdutoNaoEncontradoException.class)
    public ResponseEntity<Map<String, String>> handleProdutoNaoEncontrado(ProdutoNaoEncontradoException ex) {
        Map<String, String> response = new HashMap<>();
        response.put("erro", ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    @ExceptionHandler(QuantidadeInsuficienteException.class)
    public ResponseEntity<Map<String, String>> handleQuantidadeInsuficienteException(QuantidadeInsuficienteException ex) {
        Map<String, String> erro = Map.of("erro", ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(erro);
    }

    // Erros de validação com @Valid
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();

        ex.getBindingResult().getFieldErrors().forEach(error -> {
            errors.put(error.getField(), error.getDefaultMessage());
        });

        return ResponseEntity.badRequest().body(errors);
    }

    // Erros de argumentos inválidos no path ou query
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Map<String, String>> handleConstraintViolation(ConstraintViolationException ex) {
        Map<String, String> errors = new HashMap<>();

        ex.getConstraintViolations().forEach(violation -> {
            String field = violation.getPropertyPath().toString();
            errors.put(field, violation.getMessage());
        });

        return ResponseEntity.badRequest().body(errors);
    }

    // Recurso não encontrado
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleEntityNotFound(EntityNotFoundException ex) {
        Map<String, String> response = Map.of("erro", ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    // Fallback para qualquer outro erro
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, String>> handleGenericException(Exception ex) {
        Map<String, String> response = Map.of("erro", "Erro interno no servidor");
        ex.printStackTrace(); // para log
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }
}
