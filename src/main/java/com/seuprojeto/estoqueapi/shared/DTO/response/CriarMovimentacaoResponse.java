package com.seuprojeto.estoqueapi.shared.DTO.response;

import com.seuprojeto.estoqueapi.shared.enums.TipoMovimentacao;
import com.seuprojeto.estoqueapi.validation.ValidTipoMovimentacao;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
public class CriarMovimentacaoResponse {

    @NotNull
    private UUID produtoId;

    @NotNull(message = "Tipo de movimentação é obrigatório")
    @ValidTipoMovimentacao
    private TipoMovimentacao tipo;

    @NotNull(message = "Quantidade é obrigatória")
    @Positive(message = "Quantidade deve ser maior que zero")
    private Integer quantidade;

    @NotNull(message = "Data da movimentação é obrigatória")
    private LocalDateTime dataMovimentacao;

}
