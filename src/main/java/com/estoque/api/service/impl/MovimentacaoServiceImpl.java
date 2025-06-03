package com.estoque.api.service.impl;

import com.estoque.api.domain.MovimentacaoEstoque;
import com.estoque.api.domain.Produto;
import com.estoque.api.exception.generico.ValidacaoException;
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
import java.util.List;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MovimentacaoServiceImpl implements MovimentacaoService {

  @Autowired private MovimentacaoRepository movimentacaoRepository;

  @Autowired private ProdutoRepository produtoRepository;

  @Autowired private MovimentacaoMapper movimentacaoMapper;

  @Override
  @Transactional
  public CriarMovimentacaoResponse criar(CriarMovimentacaoRequest criarMovimentacaoRequest) {
    Produto produto =
        produtoRepository
            .findById(criarMovimentacaoRequest.getProdutoId())
            .orElseThrow(
                () -> new ProdutoNaoEncontradoException(criarMovimentacaoRequest.getProdutoId()));

    Integer quantidade = criarMovimentacaoRequest.getQuantidade();
    if (quantidade == null || quantidade <= 0) {
      throw new ValidacaoException("Quantidade deve ser maior que zero");
    }

    TipoMovimentacao tipo = criarMovimentacaoRequest.getTipo();
    if (tipo == null || !(tipo == TipoMovimentacao.SAIDA || tipo == TipoMovimentacao.ENTRADA)) {
      throw new MovimentacaoInvalidaException("Tipo de movimentação inválido");
    }

    if (tipo == TipoMovimentacao.SAIDA) {
      if (produto.getQuantidade() < quantidade) {
        throw new QuantidadeInsuficienteException(produto.getId());
      }
      produto.setQuantidade(produto.getQuantidade() - quantidade);
    } else {
      produto.setQuantidade(produto.getQuantidade() + quantidade);
    }

    produtoRepository.save(produto);

    MovimentacaoEstoque movimentacao = new MovimentacaoEstoque();
    movimentacao.setProduto(produto);
    movimentacao.setTipo(tipo);
    movimentacao.setQuantidade(quantidade);
    movimentacao.setDataMovimentacao(LocalDateTime.now());

    MovimentacaoEstoque salvo = movimentacaoRepository.save(movimentacao);
    return movimentacaoMapper.toResponse(salvo);
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
