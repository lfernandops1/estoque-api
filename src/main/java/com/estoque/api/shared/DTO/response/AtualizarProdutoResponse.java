package com.estoque.api.shared.DTO.response;

import com.estoque.api.domain.Produto;
import java.math.BigDecimal;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;

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
