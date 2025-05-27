package com.seuprojeto.estoqueapi.shared.DTO.request;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class AtualizarProdutoRequest {
    @NotBlank(message = "Descrição não pode estar em branco")
    private String descricao;

    @NotNull(message = "Preço é obrigatório")
    private BigDecimal preco;

    @NotNull(message = "Status é obrigatório")
    private Boolean ativo;
}
