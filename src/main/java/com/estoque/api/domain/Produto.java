package com.estoque.api.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;
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
@Table(name = "produtos")
public class Produto {

  @Id
  @GeneratedValue(generator = "UUID")
  @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
  @Column(updatable = false, nullable = false)
  private UUID id;

  @NotNull(message = "Descrição não pode ser nula")
  @NotBlank(message = "Descrição não pode estar em branco")
  private String descricao;

  @NotNull(message = "Quantidade é obrigatória")
  @Positive(message = "Quantidade deve ser maior que zero")
  private Integer quantidade;

  @NotNull(message = "Preço é obrigatório")
  @DecimalMin(value = "0.0", inclusive = false, message = "Preço deve ser maior que zero")
  private BigDecimal preco;

  private LocalDateTime dataHoraCadastro;
  private LocalDateTime dataHoraAlteracao;
  private LocalDateTime dataHoraRemocao;

  private Boolean ativo;
}
