package com.estoque.api.validation;

import com.estoque.api.shared.enums.TipoMovimentacao;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class TipoMovimentacaoValidator
    implements ConstraintValidator<ValidTipoMovimentacao, TipoMovimentacao> {

  @Override
  public boolean isValid(TipoMovimentacao value, ConstraintValidatorContext context) {
    if (value == null) {
      return false;
    }
    return value == TipoMovimentacao.ENTRADA || value == TipoMovimentacao.SAIDA;
  }
}
