package com.seuprojeto.estoqueapi.shared.DTO;

import jakarta.validation.constraints.PositiveOrZero;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class ProdutoFiltroDTO {

    private String descricao;

    private Boolean ativo;

    @PositiveOrZero(message = "Preço não pode ser negativo")
    private BigDecimal preco;

    @PositiveOrZero(message = "Preço mínimo não pode ser negativo")
    private BigDecimal precoMin;

    @PositiveOrZero(message = "Preço máximo não pode ser negativo")
    private BigDecimal precoMax;

    private Integer quantidade;

    @PositiveOrZero(message = "Quantidade mínima não pode ser negativa")
    private Integer quantidadeMin;

    @PositiveOrZero(message = "Quantidade máxima não pode ser negativa")
    private Integer quantidadeMax;
}
