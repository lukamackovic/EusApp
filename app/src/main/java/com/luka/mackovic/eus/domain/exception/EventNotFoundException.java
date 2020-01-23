package com.luka.mackovic.eus.domain.exception;

public class EventNotFoundException extends RuntimeException {

    public EventNotFoundException(String eventId) {
        super(String.format("Event with %s Id is not found", eventId));
    }
}