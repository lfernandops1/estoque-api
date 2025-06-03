package com.estoque.api.shared.DTO.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AtualizarProdutoRequest {
  @NotBlank(message = "Descrição não pode estar em branco")
  private String descricao;

  @NotNull(message = "Preço é obrigatório")
  @DecimalMin(value = "0.0", inclusive = false, message = "Preço deve ser maior que zero")
  private BigDecimal preco;

  @NotNull(message = "Status é obrigatório")
  private Boolean ativo;
}
