package com.seuprojeto.estoqueapi.service.impl;

import com.seuprojeto.estoqueapi.domain.MovimentacaoEstoque;
import com.seuprojeto.estoqueapi.domain.Produto;
import com.seuprojeto.estoqueapi.exception.generico.ValidacaoException;
import com.seuprojeto.estoqueapi.exception.movimentacao.MovimentacaoInvalidaException;
import com.seuprojeto.estoqueapi.exception.movimentacao.MovimentacaoNaoEncontradaException;
import com.seuprojeto.estoqueapi.exception.produto.ProdutoNaoEncontradoException;
import com.seuprojeto.estoqueapi.exception.produto.QuantidadeInsuficienteException;
import com.seuprojeto.estoqueapi.repository.MovimentacaoRepository;
import com.seuprojeto.estoqueapi.repository.ProdutoRepository;
import com.seuprojeto.estoqueapi.service.MovimentacaoService;
import com.seuprojeto.estoqueapi.shared.DTO.mapper.MovimentacaoMapper;
import com.seuprojeto.estoqueapi.shared.DTO.request.CriarMovimentacaoRequest;
import com.seuprojeto.estoqueapi.shared.DTO.response.CriarMovimentacaoResponse;
import com.seuprojeto.estoqueapi.shared.enums.TipoMovimentacao;
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
