package com.seuprojeto.estoqueapi.controller;

import com.seuprojeto.estoqueapi.domain.Produto;
import com.seuprojeto.estoqueapi.service.ProdutoService;
import com.seuprojeto.estoqueapi.shared.DTO.request.AtualizarProdutoRequest;
import com.seuprojeto.estoqueapi.shared.DTO.response.AtualizarProdutoResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/produto")
public class ProdutoController {

    @Autowired
    private ProdutoService service;

    @PostMapping("/criar")
    public ResponseEntity<Produto> criarAtividade(
            @RequestBody Produto produto) {
        return new ResponseEntity<>(
                service.cadastrar(produto), HttpStatus.CREATED
        );

    }

    @PutMapping("/{id}")
    public ResponseEntity<AtualizarProdutoResponse> atualizar(@PathVariable UUID id,
                                                              @Valid @RequestBody AtualizarProdutoRequest dto) {

        Produto atualizado = service.atualizar(id, dto);
        return ResponseEntity.ok(new AtualizarProdutoResponse(atualizado));
    }

    @GetMapping
    public ResponseEntity<List<Produto>> listar() {
        List<Produto> produtos = service.listar();
        return ResponseEntity.ok(produtos);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Produto> excluirAtividade(@PathVariable UUID id) {
        Produto produtoRemovido = service.remover(id);
        return ResponseEntity.ok(produtoRemovido);
    }

    @GetMapping("/pesquisar")
    public ResponseEntity<List<Produto>> pesquisarProdutos(
            @RequestParam(required = false) String descricao,
            @RequestParam(required = false) Boolean completado,
            @RequestParam(required = false) BigDecimal preco,
            @RequestParam(required = false) BigDecimal precoMin,
            @RequestParam(required = false) BigDecimal precoMax,
            @RequestParam(required = false) Integer quantidade,
            @RequestParam(required = false) Integer quantidadeMin,
            @RequestParam(required = false) Integer quantidadeMax
    ) {
        List<Produto> produtos = service.pesquisar(
                descricao,
                completado,
                preco,
                precoMax,
                precoMin,
                quantidade,
                quantidadeMin,
                quantidadeMax
        );
        return ResponseEntity.ok(produtos);
    }


    @GetMapping("/{id}")
    public ResponseEntity<Produto> pesquisarPorId(@PathVariable UUID id) {
        return service.pesquisarPorId(id)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new RuntimeException("Produto não encontrado"));
    }
}
