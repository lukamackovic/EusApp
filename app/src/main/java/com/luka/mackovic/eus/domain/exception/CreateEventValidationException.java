package com.luka.mackovic.eus.domain.exception;

import com.luka.mackovic.eus.ui.view.CreateEventFormValueType;

import java.util.Collection;

public class CreateEventValidationException extends RuntimeException{

    private Collection<CreateEventFormValueType> validationViolationValueType;

    public CreateEventValidationException(Collection<CreateEventFormValueType> validationViolationValueType) {
        this.validationViolationValueType = validationViolationValueType;
    }

    public Collection<CreateEventFormValueType> getValidationViolationValueType() {
        return validationViolationValueType;
    }
}