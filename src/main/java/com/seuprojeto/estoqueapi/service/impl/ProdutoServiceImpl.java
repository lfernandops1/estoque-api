package com.seuprojeto.estoqueapi.service.impl;

import com.seuprojeto.estoqueapi.domain.Produto;
import com.seuprojeto.estoqueapi.exception.produto.AtualizacaoInvalidaException;
import com.seuprojeto.estoqueapi.exception.produto.ProdutoNaoEncontradoException;
import com.seuprojeto.estoqueapi.repository.ProdutoRepository;
import com.seuprojeto.estoqueapi.repository.specs.ProdutoSpecs;
import com.seuprojeto.estoqueapi.service.ProdutoService;
import com.seuprojeto.estoqueapi.shared.DTO.ProdutoFiltroDTO;
import com.seuprojeto.estoqueapi.shared.DTO.request.AtualizarProdutoRequest;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
public class ProdutoServiceImpl implements ProdutoService {

  @Autowired private ProdutoRepository repository;

  @Override
  public Produto cadastrar(Produto produto) {
    produto.setDataHoraCadastro(LocalDateTime.now());
    produto.setAtivo(true);
    return repository.save(produto);
  }

  @Override
  public Produto atualizar(UUID id, AtualizarProdutoRequest request) {
    Produto produto =
        repository.findById(id).orElseThrow(() -> new ProdutoNaoEncontradoException(id));

    boolean houveAtualizacao = atualizarCamposSeDiferente(produto, request);

    if (!houveAtualizacao) {
      throw new AtualizacaoInvalidaException("Nenhum campo foi alterado");
    }

    produto.setDataHoraAlteracao(LocalDateTime.now());
    return repository.save(produto);
  }

  private boolean atualizarCamposSeDiferente(Produto produto, AtualizarProdutoRequest request) {
    boolean mudou = false;

    if (campoDiferente(produto.getDescricao(), request.getDescricao())) {
      produto.setDescricao(request.getDescricao());
      mudou = true;
    }

    if (campoDiferente(produto.getPreco(), request.getPreco())) {
      produto.setPreco(request.getPreco());
      mudou = true;
    }

    if (campoDiferente(produto.getAtivo(), request.getAtivo())) {
      produto.setAtivo(request.getAtivo());
      mudou = true;
    }

    return mudou;
  }

  private <T> boolean campoDiferente(T atual, T novoValor) {
    if (atual == null && novoValor == null) return false;
    if (atual == null || novoValor == null) return true;
    if (atual instanceof BigDecimal && novoValor instanceof BigDecimal) {
      return ((BigDecimal) atual).compareTo((BigDecimal) novoValor) != 0;
    }
    return !atual.equals(novoValor);
  }

  @Override
  public List<Produto> listar() {
    return repository.findAll();
  }

  public Produto remover(UUID id) {
    Produto produto =
        repository.findById(id).orElseThrow(() -> new ProdutoNaoEncontradoException(id));

    produto.setDataHoraRemocao(LocalDateTime.now());

    return repository.save(produto);
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
