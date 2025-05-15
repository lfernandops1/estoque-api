package com.seuprojeto.estoqueapi.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@RequiredArgsConstructor
@Entity
@Table(name = "produtos")
public class Produto {

    @Id
    @GeneratedValue
    private UUID id;

    private String descricao;
    private Integer quantidade;
    private BigDecimal preco;

    private LocalDateTime dataHoraCadastro;
    private LocalDateTime dataHoraAlteracao;
    private LocalDateTime dataHoraRemocao;

    private Boolean ativo;

}
