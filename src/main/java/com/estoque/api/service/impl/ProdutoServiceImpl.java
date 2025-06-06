package com.estoque.api.service.impl;

import com.estoque.api.domain.Produto;
import com.estoque.api.exception.generico.NenhumCampoModificadoException;
import com.estoque.api.exception.produto.ProdutoNaoEncontradoException;
import com.estoque.api.repository.ProdutoRepository;
import com.estoque.api.repository.specs.ProdutoSpecs;
import com.estoque.api.service.ProdutoService;
import com.estoque.api.shared.DTO.ProdutoFiltroDTO;
import com.estoque.api.shared.DTO.request.AtualizarProdutoRequest;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
public class ProdutoServiceImpl implements ProdutoService {

  @Autowired private ProdutoRepository repository;

  @Override
  public Produto cadastrar(Produto produto) {
    Produto produtoParaSalvar =
        produto.toBuilder().dataHoraCadastro(LocalDateTime.now()).ativo(true).build();

    return repository.save(produtoParaSalvar);
  }

  @Override
  public Produto atualizar(UUID id, AtualizarProdutoRequest request) {
    Produto produto = pesquisarPorId(id);
    validarAtualizacao(request, produto);

    Produto produtoAtualizado = atualizarProduto(produto, request);
    return repository.save(produtoAtualizado);
  }

  private Produto atualizarProduto(Produto produto, AtualizarProdutoRequest request) {
    return produto.toBuilder()
        .descricao(request.getDescricao())
        .preco(request.getPreco())
        .ativo(request.getAtivo())
        .dataHoraAlteracao(LocalDateTime.now())
        .build();
  }

  private void validarAtualizacao(AtualizarProdutoRequest request, Produto produto) {
    if (!isProdutoModificado(produto, request)) {
      throw new NenhumCampoModificadoException("Nenhum campo foi alterado");
    }
  }

  private boolean isProdutoModificado(Produto produto, AtualizarProdutoRequest request) {
    return !Objects.equals(produto.getDescricao(), request.getDescricao())
        || !Objects.equals(produto.getPreco(), request.getPreco())
        || !Objects.equals(produto.getAtivo(), request.getAtivo());
  }

  @Override
  public List<Produto> listar() {
    return repository.findAll();
  }

  public Produto marcarComoRemovido(UUID id) {
    Produto produto = pesquisarPorId(id);
    Produto produtoRemovido = produto.toBuilder().dataHoraRemocao(LocalDateTime.now()).build();

    return repository.save(produtoRemovido);
  }

  public List<Produto> pesquisar(ProdutoFiltroDTO filtro) {
    return repository.findAll(
        Specification.where(ProdutoSpecs.naoExcluido())
            .and(ProdutoSpecs.descricaoContem(filtro.getDescricao()))
            .and(ProdutoSpecs.ativo(filtro.getAtivo()))
            .and(
                ProdutoSpecs.precoFiltrado(
                    filtro.getPreco(), filtro.getPrecoMin(), filtro.getPrecoMax()))
            .and(
                ProdutoSpecs.quantidadeFiltrada(
                    filtro.getQuantidade(), filtro.getQuantidadeMin(), filtro.getQuantidadeMax())));
  }

  public Produto pesquisarPorId(UUID id) {
    return repository
        .findByIdAndDataHoraRemocaoIsNull(id)
        .orElseThrow(() -> new ProdutoNaoEncontradoException(id));
  }
}
