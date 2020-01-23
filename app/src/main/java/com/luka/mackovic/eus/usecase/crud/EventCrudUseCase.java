package com.luka.mackovic.eus.usecase.crud;

import android.annotation.SuppressLint;
import android.net.Uri;

import com.luka.mackovic.eus.domain.enumeration.RSVPAction;
import com.luka.mackovic.eus.domain.exception.CreateEventValidationException;
import com.luka.mackovic.eus.domain.exception.EventNotFoundException;
import com.luka.mackovic.eus.domain.model.Attendee;
import com.luka.mackovic.eus.domain.model.Event;
import com.luka.mackovic.eus.repository.network.EventRepository;
import com.luka.mackovic.eus.repository.network.ImageRepository;
import com.luka.mackovic.eus.ui.view.CreateEventFormValueType;

import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.inject.Inject;

import io.reactivex.Single;
import io.reactivex.SingleObserver;

public class EventCrudUseCase {

    private final EventRepository eventRepository;
    private final ImageRepository imageRepository;

    @Inject
    EventCrudUseCase(EventRepository firebaseEventRepository,
                     ImageRepository imageRepository) {

        this.eventRepository = firebaseEventRepository;
        this.imageRepository = imageRepository;
    }

    public Single<List<Event>> getEvents() {
        return eventRepository.getAll();
    }

    public Single<Event> getEvent(String eventId) throws EventNotFoundException {
        return eventRepository.findById(eventId);
    }

    private boolean checkEventTitle(String eventTitle) {
        return !eventTitle.isEmpty() && eventTitle.length() < 30;
    }

    private boolean checkEventRegistrationEndDate(Date registrationEndDate) {
        return registrationEndDate != null && !registrationEndDate.before(Calendar.getInstance().getTime());
    }

    private boolean checkEventStartDate(Date startDate, Date registrationEndDate) {
        return startDate != null && registrationEndDate != null && startDate.after(registrationEndDate);
    }

    private boolean checkAttendanceLimit(Long attendanceLimit) {
        return attendanceLimit > 0;
    }

    private boolean checkEventImage(String eventImageUri) {
        return eventImageUri != null && !eventImageUri.isEmpty();
    }

    public Single<Event> saveEvent(Event event, Uri eventImage) {
        Set<CreateEventFormValueType> validationErrors = this.validateEvent(event);

        if (!validationErrors.isEmpty()) {
            return Single.error(new CreateEventValidationException(validationErrors));
        }

        if (eventImage == null) {
            return eventRepository.save(event);
        }

        return new Single<Event>() {
            @SuppressLint("CheckResult")
            @Override
            protected void subscribeActual(SingleObserver<? super Event> observer) {
                imageRepository.saveImage(eventImage).subscribe(imagePath -> {
                    event.setImage(imagePath);
                    eventRepository.save(event).subscribe(observer::onSuccess);
                }, observer::onError);

            }
        };
    }

    public void deleteEvent(String eventId, String eventImage) {
        eventRepository.delete(eventId);
        imageRepository.deleteImage(eventImage);
    }

    private Set<CreateEventFormValueType> validateEvent(Event event) {
        Set<CreateEventFormValueType> validationErrors = new HashSet<>();

        if (!checkEventTitle(event.getTitle())) {
            validationErrors.add(CreateEventFormValueType.TITLE);
        }
        if (!checkEventImage(event.getImage())) {
            validationErrors.add(CreateEventFormValueType.IMAGE);
        }
        if (!checkAttendanceLimit(event.getAttendeesLimit())) {
            validationErrors.add(CreateEventFormValueType.ATTENDEES_LIMIT);
        }
        if (!checkEventStartDate(event.getStartTime(), event.getRegistrationEndTime())) {
            validationErrors.add(CreateEventFormValueType.EVENT_START_DATE);
        }
        if (!checkEventRegistrationEndDate(event.getRegistrationEndTime())) {
            validationErrors.add(CreateEventFormValueType.REGISTRATION_END_DATE);
        }
        return validationErrors;
    }

    public void respondToEvent(String eventId, Attendee attendee) throws EventNotFoundException {
        eventRepository.respondToEvent(eventId, attendee, attendee == null ? RSVPAction.GOING : RSVPAction.NOT_GOING);
    }

    public Single<List<Event>> findAllByAttendingUser() {
        return eventRepository.findAllByAttendingUser();
    }
}