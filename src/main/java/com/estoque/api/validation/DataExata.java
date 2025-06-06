package com.estoque.api.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = DataExataValidator.class)
public @interface DataExata {
  String message() default "Data da movimentação deve ser exatamente o momento atual";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};
}
