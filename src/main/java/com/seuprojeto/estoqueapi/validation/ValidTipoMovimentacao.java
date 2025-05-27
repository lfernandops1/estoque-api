package com.seuprojeto.estoqueapi.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = TipoMovimentacaoValidator.class)
@Target({ ElementType.FIELD, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidTipoMovimentacao {
    String message() default "Tipo de movimentação inválido. Deve ser ENTRADA ou SAÍDA";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
