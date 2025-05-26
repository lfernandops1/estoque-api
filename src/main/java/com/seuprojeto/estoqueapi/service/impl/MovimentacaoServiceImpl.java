package com.seuprojeto.estoqueapi.service.impl;

import com.seuprojeto.estoqueapi.domain.MovimentacaoEstoque;
import com.seuprojeto.estoqueapi.domain.Produto;
import com.seuprojeto.estoqueapi.repository.MovimentacaoRepository;
import com.seuprojeto.estoqueapi.repository.ProdutoRepository;
import com.seuprojeto.estoqueapi.service.MovimentacaoService;
import com.seuprojeto.estoqueapi.shared.DTO.mapper.MovimentacaoMapper;
import com.seuprojeto.estoqueapi.shared.DTO.request.CriarMovimentacaoRequest;
import com.seuprojeto.estoqueapi.shared.DTO.response.CriarMovimentacaoResponse;
import com.seuprojeto.estoqueapi.shared.enums.TipoMovimentacao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class MovimentacaoServiceImpl implements MovimentacaoService {

    @Autowired
    private MovimentacaoRepository movimentacaoRepository;

    @Autowired
    private ProdutoRepository produtoRepository;

    @Autowired
    private MovimentacaoMapper movimentacaoMapper;


    @Override
    @Transactional
    public CriarMovimentacaoResponse criar(CriarMovimentacaoRequest criarMovimentacaoRequest) {
        Produto produto = produtoRepository.findById(criarMovimentacaoRequest.getProdutoId())
                .orElseThrow(() -> new RuntimeException("Produto não encontrado"));

        Integer quantidade = criarMovimentacaoRequest.getQuantidade();
        if (quantidade == null || quantidade <= 0) {
            throw new RuntimeException("Quantidade deve ser maior que zero");
        }

        TipoMovimentacao tipo = criarMovimentacaoRequest.getTipo();
        if (tipo == TipoMovimentacao.SAIDA) {
            if (produto.getQuantidade() < quantidade) {
                throw new RuntimeException("Quantidade insuficiente no estoque");
            }
            produto.setQuantidade(produto.getQuantidade() - quantidade);
        } else if (tipo == TipoMovimentacao.ENTRADA) {
            produto.setQuantidade(produto.getQuantidade() + quantidade);
        } else {
            throw new RuntimeException("Tipo de movimentação inválido");
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
    public Optional<MovimentacaoEstoque> buscarPorId(UUID id) {
        return movimentacaoRepository.findById(id);
    }


}
