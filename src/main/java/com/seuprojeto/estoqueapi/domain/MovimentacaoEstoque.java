package com.seuprojeto.estoqueapi.domain;

import com.seuprojeto.estoqueapi.shared.enums.TipoMovimentacao;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

@Getter
@Setter
@RequiredArgsConstructor
@Entity
@Table(name = "movimentacoes")
public class MovimentacaoEstoque {
  @Id
  @GeneratedValue(generator = "UUID")
  @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
  @Column(updatable = false, nullable = false)
  private UUID id;

  @ManyToOne
  @JoinColumn(name = "produto_id", nullable = false)
  private Produto produto;

  @Enumerated(EnumType.STRING)
  private TipoMovimentacao tipo;

  private Integer quantidade;
  private LocalDateTime dataMovimentacao;
}
