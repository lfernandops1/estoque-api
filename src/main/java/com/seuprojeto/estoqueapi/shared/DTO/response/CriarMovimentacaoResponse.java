package com.seuprojeto.estoqueapi.shared.DTO.response;

import com.seuprojeto.estoqueapi.shared.enums.TipoMovimentacao;
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

  private UUID produtoId;

  private TipoMovimentacao tipo;

  private Integer quantidade;

  private LocalDateTime dataMovimentacao;
}
