package com.seuprojeto.estoqueapi.shared.DTO.response;

import com.seuprojeto.estoqueapi.domain.Produto;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter

public class AtualizarProdutoResponse {

    private UUID id;

    @NotBlank(message = "Descrição é obrigatória")
    private String descricao;

    @NotNull(message = "Preço é obrigatório")
    @DecimalMin(value = "0.0", inclusive = false, message = "Preço deve ser maior que zero")
    private BigDecimal preco;

    @NotNull(message = "Ativo é obrigatório")
    private Boolean ativo;

    @NotNull(message = "Quantidade é obrigatória")
    @Min(value = 0, message = "Quantidade deve ser zero ou maior")
    private Integer quantidade;

    public AtualizarProdutoResponse(Produto produto) {
        this.id = produto.getId();
        this.descricao = produto.getDescricao();
        this.preco = produto.getPreco();
        this.ativo = produto.getAtivo();
        this.quantidade = produto.getQuantidade();
    }
}
