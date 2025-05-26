package com.seuprojeto.estoqueapi.service.impl;

import com.seuprojeto.estoqueapi.domain.Produto;
import com.seuprojeto.estoqueapi.repository.ProdutoRepository;
import com.seuprojeto.estoqueapi.repository.specs.ProdutoSpecs;
import com.seuprojeto.estoqueapi.service.ProdutoService;
import com.seuprojeto.estoqueapi.shared.DTO.request.AtualizarProdutoRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ProdutoServiceImpl implements ProdutoService {

    @Autowired
    private ProdutoRepository repository;


    @Override
    public Produto cadastrar(Produto produto) {
        produto.setDataHoraCadastro(LocalDateTime.now());
        produto.setAtivo(true);
        this.repository.save(produto);
        return produto;
    }

    @Override
    public Produto atualizar(UUID id, AtualizarProdutoRequest produto) {
        Produto produtoEncontrado = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Produto não encontrado"));

        if (produto.getDescricao() == null &&
                produto.getAtivo() == null &&
                produto.getPreco() == null) {
            throw new RuntimeException("Nenhum campo válido fornecido para atualização");
        }

        Optional.ofNullable(produto.getDescricao()).ifPresent(produtoEncontrado::setDescricao);
        Optional.ofNullable(produto.getPreco()).ifPresent(produtoEncontrado::setPreco);
        Optional.ofNullable(produto.getAtivo()).ifPresent(produtoEncontrado::setAtivo);

        produtoEncontrado.setDataHoraAlteracao(LocalDateTime.now());

        return repository.save(produtoEncontrado);
    }

    @Override
    public List<Produto> listar() {
        return repository.findAll();
    }

    public Produto remover(UUID id) {
        Produto produto = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Produto não encontrado"));

        produto.setDataHoraRemocao(LocalDateTime.now());

        return repository.save(produto);
    }

    public List<Produto> pesquisar(String descricao, Boolean ativo,
                                   BigDecimal preco, BigDecimal precoMin, BigDecimal precoMax,
                                   Integer quantidade, Integer quantidadeMin, Integer quantidadeMax) {
        return repository.findAll(
                Specification.where(ProdutoSpecs.naoExcluido())
                        .and(ProdutoSpecs.descricaoContem(descricao))
                        .and(ProdutoSpecs.ativo(ativo))
                        .and(ProdutoSpecs.precoFiltrado(preco, precoMin, precoMax))
                        .and(ProdutoSpecs.quantidadeFiltrada(quantidade, quantidadeMin, quantidadeMax))
        );
    }



    public Optional<Produto> pesquisarPorId(UUID id) {
        return repository.findByIdAndDataHoraRemocaoIsNull(id);
    }
}
