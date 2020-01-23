package com.luka.mackovic.eus.repository.network;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.luka.mackovic.eus.domain.enumeration.RSVPAction;
import com.luka.mackovic.eus.domain.exception.EventNotFoundException;
import com.luka.mackovic.eus.domain.model.Attendee;
import com.luka.mackovic.eus.domain.model.Event;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import io.reactivex.Single;
import io.reactivex.SingleObserver;

public class FirebaseEventRepository implements EventRepository {

    private final static String COLLECTION_EVENT = "event";
    private final static String FIELD_ATTENDEE = "attendees";

    private final FirebaseFirestore firestore;

    private final FirebaseUser firebaseUser;

    FirebaseEventRepository() {
        firestore = FirebaseFirestore.getInstance();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
    }

    @Override
    public Single<List<Event>> getAll() {
        return new Single<List<Event>>() {
            @Override
            protected void subscribeActual(SingleObserver<? super List<Event>> observer) {
                CollectionReference collectionReference = firestore
                        .collection(COLLECTION_EVENT);
                collectionReference.get()
                        .addOnSuccessListener(task -> {
                            List<Event> events = new ArrayList<>();
                            for (QueryDocumentSnapshot document : task) {
                                Event event = document.toObject(Event.class);
                                if (event.getStartTime().after(Calendar.getInstance().getTime())) {
                                    events.add(event);
                                }
                            }
                            observer.onSuccess(events);
                        }).addOnFailureListener(observer::onError);
            }
        };
    }

    @Override
    public Single<Event> save(Event event) {
        return new Single<Event>() {
            @Override
            protected void subscribeActual(SingleObserver<? super Event> observer) {
                CollectionReference newEventRef = firestore
                        .collection(COLLECTION_EVENT);
                if (event.getId() == null) {
                    String eventId = newEventRef.document().getId();
                    event.setId(eventId);
                    event.setCreatedDate(Calendar.getInstance().getTime());

                    newEventRef.document(eventId).set(event)
                            .addOnCompleteListener(task -> observer.onSuccess(event))
                            .addOnFailureListener(observer::onError);
                } else {
                    event.setCreatedDate(Calendar.getInstance().getTime());
                    newEventRef.document(event.getId()).set(event)
                            .addOnCompleteListener(task -> observer.onSuccess(event))
                            .addOnFailureListener(observer::onError);
                }
            }
        };
    }

    @Override
    public void delete(String eventId) {
        DocumentReference deleteEventRef = firestore
                .collection(COLLECTION_EVENT)
                .document(eventId);
        deleteEventRef.delete();
    }

    @Override
    public Single<Event> findById(String eventId) throws EventNotFoundException{
        return new Single<Event>() {
            @Override
            protected void subscribeActual(SingleObserver<? super Event> observer) {
                DocumentReference newEventRef = firestore
                        .collection(COLLECTION_EVENT)
                        .document(eventId);
                newEventRef.get().addOnCompleteListener(task -> {
                    if (task.getResult() == null || !task.getResult().exists()) {
                        throw new EventNotFoundException(eventId);
                    } else {
                        Event event = task.getResult().toObject(Event.class);
                        observer.onSuccess(event);
                    }
                }).addOnFailureListener(observer::onError);
            }
        };
    }

    @Override
    public void respondToEvent(String eventId,
                               Attendee attendee,
                               RSVPAction attendanceAction) throws EventNotFoundException {

        DocumentReference documentReference = firestore
                .collection(COLLECTION_EVENT)
                .document(eventId);

        documentReference.get().addOnFailureListener(e -> {

        }).addOnCompleteListener(task -> {
            if (task.getResult() == null || !task.getResult().exists()) {
                throw new EventNotFoundException(eventId);
            }
        });

        Attendee newAttendee = new Attendee();
        if (attendanceAction == RSVPAction.GOING) {
            newAttendee.setName(firebaseUser.getDisplayName());
            newAttendee.setEmail(firebaseUser.getEmail());
            newAttendee.setAssignedDate(Calendar.getInstance().getTime());
        }
        documentReference.update(FIELD_ATTENDEE, RSVPAction.GOING == attendanceAction
                ? FieldValue.arrayUnion(newAttendee)
                : FieldValue.arrayRemove(attendee));
    }

    @Override
    public Single<List<Event>> findAllByAttendingUser() {
        return new Single<List<Event>>() {
            @Override
            protected void subscribeActual(SingleObserver<? super List<Event>> observer) {
                CollectionReference collectionReference = firestore
                        .collection(COLLECTION_EVENT);
                collectionReference.get()
                        .addOnSuccessListener(task -> {
                            List<Event> events = new ArrayList<>();
                            for (QueryDocumentSnapshot document : task) {
                                Event event = document.toObject(Event.class);
                                if (event.getStartTime().after(Calendar.getInstance().getTime())
                                        && event.addAttendee(firebaseUser.getEmail()) != null) {
                                    events.add(event);
                                }
                            }
                            observer.onSuccess(events);
                        }).addOnFailureListener(observer::onError);
            }
        };
    }
}