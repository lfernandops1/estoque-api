package com.seuprojeto.estoqueapi.shared.DTO.response;

import com.seuprojeto.estoqueapi.domain.Produto;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter

public class AtualizarProdutoResponse {

    private UUID id;
    private String descricao;
    private BigDecimal preco;
    private Boolean ativo;
    private Integer quantidade;

    public AtualizarProdutoResponse(Produto produto) {
        this.id = produto.getId();
        this.descricao = produto.getDescricao();
        this.preco = produto.getPreco();
        this.ativo = produto.getAtivo();
        this.quantidade = produto.getQuantidade();
    }
}
