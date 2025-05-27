package com.seuprojeto.estoqueapi.service.impl;

import com.seuprojeto.estoqueapi.domain.Produto;
import com.seuprojeto.estoqueapi.exception.produto.AtualizacaoInvalidaException;
import com.seuprojeto.estoqueapi.exception.produto.ProdutoNaoEncontradoException;
import com.seuprojeto.estoqueapi.repository.ProdutoRepository;
import com.seuprojeto.estoqueapi.repository.specs.ProdutoSpecs;
import com.seuprojeto.estoqueapi.service.ProdutoService;
import com.seuprojeto.estoqueapi.shared.DTO.ProdutoFiltroDTO;
import com.seuprojeto.estoqueapi.shared.DTO.request.AtualizarProdutoRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

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
        return repository.save(produto);
    }

    @Override
    public Produto atualizar(UUID id, AtualizarProdutoRequest produto) {
        Produto produtoEncontrado = repository.findById(id)
                .orElseThrow(() -> new ProdutoNaoEncontradoException(id));

        if (produto.getDescricao() == null &&
                produto.getAtivo() == null &&
                produto.getPreco() == null) {
            throw new AtualizacaoInvalidaException("Nenhum campo válido fornecido para atualização");
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
                .orElseThrow(() -> new ProdutoNaoEncontradoException(id));

        produto.setDataHoraRemocao(LocalDateTime.now());

        return repository.save(produto);
    }

    public List<Produto> pesquisar(ProdutoFiltroDTO filtro) {
        return repository.findAll(
                Specification.where(ProdutoSpecs.naoExcluido())
                        .and(ProdutoSpecs.descricaoContem(filtro.getDescricao()))
                        .and(ProdutoSpecs.ativo(filtro.getAtivo()))
                        .and(ProdutoSpecs.precoFiltrado(filtro.getPreco(), filtro.getPrecoMin(), filtro.getPrecoMax()))
                        .and(ProdutoSpecs.quantidadeFiltrada(filtro.getQuantidade(), filtro.getQuantidadeMin(), filtro.getQuantidadeMax()))
        );
    }

    public Produto pesquisarPorId(UUID id) {
        return repository.findByIdAndDataHoraRemocaoIsNull(id)
                .orElseThrow(() -> new ProdutoNaoEncontradoException(id));
    }

}
