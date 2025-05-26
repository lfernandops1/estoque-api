package com.seuprojeto.estoqueapi.repository.specs;

import com.seuprojeto.estoqueapi.domain.Produto;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;

public class ProdutoSpecs {

    public static Specification<Produto> descricaoContem(String descricao) {
        return (root, query, builder) ->
                descricao == null || descricao.isBlank() ?
                        builder.conjunction() :
                        builder.like(builder.lower(root.get("descricao")), "%" + descricao.toLowerCase() + "%");
    }

    public static Specification<Produto> precoExato(BigDecimal preco) {
        return (root, query, builder) ->
                preco == null ?
                        builder.conjunction() :
                        builder.equal(root.get("preco"), preco.stripTrailingZeros());
    }

    public static Specification<Produto> precoEntre(BigDecimal precoMin, BigDecimal precoMax) {
        return (root, query, builder) -> {
            if (precoMin != null && precoMax != null) {
                return builder.between(root.get("preco"), precoMin, precoMax);
            } else if (precoMin != null) {
                return builder.greaterThanOrEqualTo(root.get("preco"), precoMin);
            } else if (precoMax != null) {
                return builder.lessThanOrEqualTo(root.get("preco"), precoMax);
            } else {
                return builder.conjunction();
            }
        };
    }

    public static Specification<Produto> ativo(Boolean ativo) {
        return (root, query, builder) ->
                ativo == null ?
                        builder.conjunction() :
                        builder.equal(root.get("ativo"), ativo);
    }

    public static Specification<Produto> naoExcluido() {
        return (root, query, builder) ->
                builder.isNull(root.get("dataHoraRemocao"));
    }

    public static Specification<Produto> quantidadeExata(Integer quantidade) {
        return (root, query, builder) ->
                quantidade == null ?
                        builder.conjunction() :
                        builder.equal(root.get("quantidade"), quantidade);
    }

    public static Specification<Produto> quantidadeEntre(Integer quantidadeMin, Integer quantidadeMax) {
        return (root, query, builder) -> {
            if (quantidadeMin != null && quantidadeMax != null) {
                return builder.between(root.get("quantidade"), quantidadeMin, quantidadeMax);
            } else if (quantidadeMin != null) {
                return builder.greaterThanOrEqualTo(root.get("quantidade"), quantidadeMin);
            } else if (quantidadeMax != null) {
                return builder.lessThanOrEqualTo(root.get("quantidade"), quantidadeMax);
            } else {
                return builder.conjunction();
            }
        };
    }

    public static Specification<Produto> quantidadeFiltrada(Integer quantidade, Integer quantidadeMin, Integer quantidadeMax) {
        Specification<Produto> spec = Specification.where(naoExcluido());

        if (quantidade != null) {
            spec = spec.and(quantidadeExata(quantidade));
        } else if (quantidadeMin != null || quantidadeMax != null) {
            spec = spec.and(quantidadeEntre(quantidadeMin, quantidadeMax));
        }

        return spec;
    }

    public static Specification<Produto> precoFiltrado(BigDecimal preco, BigDecimal precoMin, BigDecimal precoMax) {
        Specification<Produto> spec = Specification.where(naoExcluido());

        if (preco != null) {
            spec = spec.and(precoExato(preco));
        } else if (precoMin != null || precoMax != null) {
            spec = spec.and(precoEntre(precoMin, precoMax));
        }

        return spec;
    }
}
