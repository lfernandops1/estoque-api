package com.seuprojeto.estoqueapi.shared.DTO.response;

import com.seuprojeto.estoqueapi.shared.enums.TipoMovimentacao;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
public class CriarMovimentacaoResponse {
    private UUID id;
    private UUID produtoId;
    private TipoMovimentacao tipo;
    private Integer quantidade;
    private LocalDateTime dataMovimentacao;
}
