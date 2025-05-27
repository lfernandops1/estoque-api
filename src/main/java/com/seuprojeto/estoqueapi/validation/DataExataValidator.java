package com.seuprojeto.estoqueapi.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.LocalDateTime;

public class DataExataValidator implements ConstraintValidator<DataExata, LocalDateTime> {

    @Override
    public boolean isValid(LocalDateTime value, ConstraintValidatorContext context) {
        if (value == null) return false;
        return value.isEqual(LocalDateTime.now());
    }
}
