package com.luka.mackovic.eus.repository.network;

import com.luka.mackovic.eus.domain.enumeration.RSVPAction;
import com.luka.mackovic.eus.domain.exception.EventNotFoundException;
import com.luka.mackovic.eus.domain.model.Attendee;
import com.luka.mackovic.eus.domain.model.Event;

import java.util.List;

import io.reactivex.Single;

public interface EventRepository {

    Single<List<Event>> getAll();

    Single<Event> save(Event event);

    void delete(String eventId);

    Single<Event> findById(String eventId);

    void respondToEvent(String eventId,
                        Attendee attendee,
                        RSVPAction attendanceAction) throws EventNotFoundException;

    Single<List<Event>> findAllByAttendingUser();
}