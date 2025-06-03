package com.estoque.api.shared.DTO.response;

import com.estoque.api.shared.enums.TipoMovimentacao;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CriarMovimentacaoResponse {

  private UUID id;
  private UUID produtoId;

  private TipoMovimentacao tipo;

  private Integer quantidade;

  private LocalDateTime dataMovimentacao;
}
