package com.seuprojeto.estoqueapi.controller;

import com.seuprojeto.estoqueapi.domain.Produto;
import com.seuprojeto.estoqueapi.service.ProdutoService;
import com.seuprojeto.estoqueapi.shared.DTO.ProdutoFiltroDTO;
import com.seuprojeto.estoqueapi.shared.DTO.request.AtualizarProdutoRequest;
import com.seuprojeto.estoqueapi.shared.DTO.response.AtualizarProdutoResponse;
import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/produto")
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
