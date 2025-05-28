package com.seuprojeto.estoqueapi.shared.DTO.request;

import com.seuprojeto.estoqueapi.shared.enums.TipoMovimentacao;
import com.seuprojeto.estoqueapi.validation.ValidTipoMovimentacao;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CriarMovimentacaoRequest {

  @NotNull(message = "ProdutoId é obrigatório")
  private UUID produtoId;

  @ValidTipoMovimentacao private TipoMovimentacao tipo;

  @NotNull(message = "Quantidade é obrigatória")
  @Positive(message = "Quantidade deve ser maior que zero")
  private Integer quantidade;
}
