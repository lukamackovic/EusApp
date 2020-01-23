package com.luka.mackovic.eus.domain.exception;

import com.luka.mackovic.eus.ui.view.CreateNewsFormValueType;

import java.util.Collection;

public class CreateNewsValidationException extends RuntimeException{

    private Collection<CreateNewsFormValueType> validationViolationValueType;

    public CreateNewsValidationException(Collection<CreateNewsFormValueType> validationViolationValueType) {
        this.validationViolationValueType = validationViolationValueType;
    }

    public Collection<CreateNewsFormValueType> getValidationViolationValueType() {
        return validationViolationValueType;
    }
}