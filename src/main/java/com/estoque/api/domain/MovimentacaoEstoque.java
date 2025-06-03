package com.estoque.api.domain;

import com.estoque.api.shared.enums.TipoMovimentacao;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
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
