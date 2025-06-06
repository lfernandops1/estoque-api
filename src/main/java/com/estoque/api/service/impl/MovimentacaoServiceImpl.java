package com.estoque.api.service.impl;

import com.estoque.api.domain.MovimentacaoEstoque;
import com.estoque.api.domain.Produto;
import com.estoque.api.exception.movimentacao.MovimentacaoInvalidaException;
import com.estoque.api.exception.movimentacao.MovimentacaoNaoEncontradaException;
import com.estoque.api.exception.produto.ProdutoNaoEncontradoException;
import com.estoque.api.exception.produto.QuantidadeInsuficienteException;
import com.estoque.api.repository.MovimentacaoRepository;
import com.estoque.api.repository.ProdutoRepository;
import com.estoque.api.service.MovimentacaoService;
import com.estoque.api.shared.DTO.mapper.MovimentacaoMapper;
import com.estoque.api.shared.DTO.request.CriarMovimentacaoRequest;
import com.estoque.api.shared.DTO.response.CriarMovimentacaoResponse;
import com.estoque.api.shared.enums.TipoMovimentacao;
import java.time.LocalDateTime;
import java.util.EnumSet;
import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MovimentacaoServiceImpl implements MovimentacaoService {

  private final MovimentacaoRepository movimentacaoRepository;

  private final ProdutoRepository produtoRepository;

  private final MovimentacaoMapper movimentacaoMapper;

  public MovimentacaoServiceImpl(
      MovimentacaoRepository movimentacaoRepository,
      ProdutoRepository produtoRepository,
      MovimentacaoMapper movimentacaoMapper,
      ProdutoServiceImpl produtoServiceImpl) {
    this.movimentacaoRepository = movimentacaoRepository;
    this.produtoRepository = produtoRepository;
    this.movimentacaoMapper = movimentacaoMapper;
  }

  @Override
  @Transactional
  public CriarMovimentacaoResponse criar(CriarMovimentacaoRequest request) {
    validarDadosMovimentacao(request);

    Produto produto =
        produtoRepository
            .findById(request.getProdutoId())
            .orElseThrow(() -> new ProdutoNaoEncontradoException(request.getProdutoId()));

    atualizarEstoqueProduto(request, produto);
    MovimentacaoEstoque movimentacaoSalva = criarMovimentacao(request, produto);

    return movimentacaoMapper.toResponse(movimentacaoSalva);
  }

  private void validarDadosMovimentacao(CriarMovimentacaoRequest request) {
    if (request.getQuantidade() == null || request.getQuantidade() <= 0) {
      throw new QuantidadeInsuficienteException(request.getQuantidade(), request.getProdutoId());
    }
    if (request.getTipo() == null
        || !EnumSet.allOf(TipoMovimentacao.class).contains(request.getTipo())) {
      throw new MovimentacaoInvalidaException();
    }
  }

  private void atualizarEstoqueProduto(CriarMovimentacaoRequest request, Produto produto) {
    if (request.getTipo() == TipoMovimentacao.SAIDA
        && produto.getQuantidade() < request.getQuantidade()) {
      throw new QuantidadeInsuficienteException(produto.getQuantidade(), produto.getId());
    }

    int novaQuantidade =
        request.getTipo() == TipoMovimentacao.ENTRADA
            ? produto.getQuantidade() + request.getQuantidade()
            : produto.getQuantidade() - request.getQuantidade();

    produto.setQuantidade(novaQuantidade);
    produtoRepository.save(produto);
  }

  private MovimentacaoEstoque criarMovimentacao(CriarMovimentacaoRequest request, Produto produto) {
    MovimentacaoEstoque movimentacao = new MovimentacaoEstoque();
    movimentacao.setProduto(produto);
    movimentacao.setTipo(request.getTipo());
    movimentacao.setQuantidade(request.getQuantidade());
    movimentacao.setDataMovimentacao(LocalDateTime.now());

    return movimentacaoRepository.save(movimentacao);
  }

  @Override
  public List<MovimentacaoEstoque> listar() {
    return movimentacaoRepository.findAll();
  }

  @Override
  public MovimentacaoEstoque buscarPorId(UUID id) {
    return movimentacaoRepository
        .findById(id)
        .orElseThrow(() -> new MovimentacaoNaoEncontradaException(id));
  }
}
