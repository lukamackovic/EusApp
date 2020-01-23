package com.luka.mackovic.eus.ui.activity;

import android.annotation.SuppressLint;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;
import android.widget.Toolbar;

import com.luka.mackovic.eus.R;
import com.luka.mackovic.eus.domain.exception.EventNotFoundException;
import com.luka.mackovic.eus.domain.model.Attendee;
import com.luka.mackovic.eus.domain.model.Event;
import com.luka.mackovic.eus.service.AuthenticationService;
import com.luka.mackovic.eus.ui.view.EventDetailsView;
import com.luka.mackovic.eus.ui.view.EventDetailsView_;
import com.luka.mackovic.eus.usecase.crud.EventCrudUseCase;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ViewById;

import java.util.Calendar;
import java.util.Objects;

import javax.inject.Inject;

import dagger.android.support.DaggerAppCompatActivity;
import io.reactivex.disposables.Disposable;

@SuppressLint("Registered")
@EActivity(R.layout.activity_event_details)
public class EventDetailsActivity extends DaggerAppCompatActivity
        implements EventDetailsView.ButtonClickedRequestInterface,
        Toolbar.OnClickListener {

    @ViewById
    FrameLayout eventDetailsContentView;

    @ViewById
    androidx.appcompat.widget.Toolbar toolbar;

    @Extra("id")
    String eventId;

    @Inject
    EventCrudUseCase eventCrudUseCase;

    @Inject
    AuthenticationService authenticationService;

    private Attendee attendee;
    private EventDetailsView eventDetailsView;
    private Disposable disposable;

    @AfterViews
    void init() {
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle("");
        toolbar.setNavigationOnClickListener(this);
        eventDetailsView = EventDetailsView_.build(this);
        eventDetailsView.setSignUpRequestListener(this);
        eventDetailsContentView.addView(eventDetailsView);
        updateUI();
    }

    private void updateUI() {
        disposable = eventCrudUseCase.getEvent(eventId)
                .doOnSuccess(this::setInitView)
                .doOnError(Throwable::printStackTrace)
                .subscribe();
    }

    private void setInitView(Event event) {
        attendee = event.addAttendee(authenticationService.getCurrentUser().getEmail());
        eventDetailsView.setEventItems(event);

        if (attendee == null) {
            eventDetailsView.userIsNotAttendeeView();
        } else {
            eventDetailsView.userIsAttendeeView();
        }

        if (event.getRegistrationEndTime().before(Calendar.getInstance().getTime())) {
            eventDetailsView.registrationEnded();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (disposable != null && !disposable.isDisposed()) {
            disposable.dispose();
        }
    }

    @Override
    public void onClick(View v) {
        HomeActivity_.intent(this).start();
    }

    @Override
    public void buttonClicked() {
        try {
            eventCrudUseCase.respondToEvent(eventId, attendee);
            updateUI();
        } catch (EventNotFoundException exception) {
            Toast.makeText(this, getText(R.string.event_not_exist_error_msg), Toast.LENGTH_LONG).show();
        }
    }
}