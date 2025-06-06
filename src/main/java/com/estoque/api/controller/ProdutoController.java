package com.estoque.api.controller;

import com.estoque.api.domain.Produto;
import com.estoque.api.service.ProdutoService;
import com.estoque.api.shared.DTO.ProdutoFiltroDTO;
import com.estoque.api.shared.DTO.request.AtualizarProdutoRequest;
import com.estoque.api.shared.DTO.response.AtualizarProdutoResponse;
import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/produtos")
public class ProdutoController {

  @Autowired private ProdutoService service;

  @PostMapping("/criar")
  public ResponseEntity<Produto> criarAtividade(@RequestBody @Valid Produto produto) {
    return new ResponseEntity<>(service.cadastrar(produto), HttpStatus.CREATED);
  }

  @PutMapping("/{id}")
  public ResponseEntity<AtualizarProdutoResponse> atualizar(
      @PathVariable UUID id, @Valid @RequestBody AtualizarProdutoRequest dto) {

    Produto atualizado = service.atualizar(id, dto);
    return ResponseEntity.ok(new AtualizarProdutoResponse(atualizado));
  }

  @GetMapping("/listar")
  public ResponseEntity<List<Produto>> listar() {
    List<Produto> produtos = service.listar();
    return ResponseEntity.ok(produtos);
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Produto> excluirAtividade(@PathVariable UUID id) {
    Produto produtoRemovido = service.marcarComoRemovido(id);
    return ResponseEntity.ok(produtoRemovido);
  }

  @GetMapping("/pesquisar")
  public ResponseEntity<List<Produto>> pesquisarProdutos(
      @Valid @ModelAttribute ProdutoFiltroDTO filtro) {
    List<Produto> produtos = service.pesquisar(filtro);
    return ResponseEntity.ok(produtos);
  }

  @GetMapping("/{id}")
  public ResponseEntity<Produto> pesquisarPorId(@PathVariable UUID id) {
    Produto produto = service.pesquisarPorId(id);
    return ResponseEntity.ok(produto);
  }
}
