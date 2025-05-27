package com.seuprojeto.estoqueapi.controller;

import com.seuprojeto.estoqueapi.domain.MovimentacaoEstoque;
import com.seuprojeto.estoqueapi.service.MovimentacaoService;
import com.seuprojeto.estoqueapi.shared.DTO.request.CriarMovimentacaoRequest;
import com.seuprojeto.estoqueapi.shared.DTO.response.CriarMovimentacaoResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/movimentacoes")
public class MovimentacaoEstoqueController {

    @Autowired
    private MovimentacaoService service;


    @PostMapping("/criar")
    public ResponseEntity<CriarMovimentacaoResponse> criarMovimentacao(
            @RequestBody @Valid CriarMovimentacaoRequest movimentacao) {
        CriarMovimentacaoResponse response = service.criar(movimentacao);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }


    @GetMapping("/listar")
    public ResponseEntity<List<MovimentacaoEstoque>> listar() {
        List<MovimentacaoEstoque> movimentacoes = service.listar();
        return ResponseEntity.ok(movimentacoes);
    }

    @GetMapping("/{id}")
    public ResponseEntity<MovimentacaoEstoque> buscarPorId(@PathVariable UUID id) {
        MovimentacaoEstoque movimentacao = service.buscarPorId(id);
        return ResponseEntity.ok(movimentacao);
    }

}




