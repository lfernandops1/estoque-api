package com.seuprojeto.estoqueapi.shared.DTO.request;

import com.seuprojeto.estoqueapi.shared.enums.TipoMovimentacao;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class CriarMovimentacaoRequest {
    private UUID produtoId;
    private TipoMovimentacao tipo;
    private Integer quantidade;


}
