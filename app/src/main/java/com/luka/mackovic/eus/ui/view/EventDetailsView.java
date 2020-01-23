package com.luka.mackovic.eus.ui.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.widget.ImageView;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textview.MaterialTextView;
import com.luka.mackovic.eus.R;
import com.luka.mackovic.eus.domain.model.Event;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;

@EViewGroup(R.layout.view_event_details)
public class EventDetailsView extends ConstraintLayout {

    @ViewById
    MaterialTextView eventTitleDetailsTextView;

    @ViewById
    ImageView eventImageDetailsImageView;

    @ViewById
    MaterialTextView eventDescriptionDetailsTextView;

    @ViewById
    MaterialTextView eventStartDateDetailsTextView;

    @ViewById
    MaterialTextView eventRegistrationEndDateDetailsTextView;

    @ViewById
    MaterialTextView eventRemainingSeatsDetailsTextView;

    @ViewById
    MaterialTextView registrationEndedTextView;

    @ViewById
    MaterialButton signUpForEvent;

    @ViewById
    MaterialButton signOutForEvent;

    private ButtonClickedRequestInterface buttonClickedRequestInterface;

    public void setSignUpRequestListener(ButtonClickedRequestInterface buttonClickedRequestInterface) {
        this.buttonClickedRequestInterface = buttonClickedRequestInterface;
    }

    @Click
    void signUpForEvent() {
        buttonClickedRequestInterface.buttonClicked();
    }

    @Click
    void signOutForEvent() {
        buttonClickedRequestInterface.buttonClicked();
    }

    public EventDetailsView(Context context) {
        super(context);
    }

    @SuppressLint("CheckResult")
    public void setEventItems(Event event) {
        eventTitleDetailsTextView.setText(event.getTitle());
        RequestOptions placeholderOption = new RequestOptions();
        placeholderOption.placeholder(R.drawable.ic_events);
        Glide.with(this).setDefaultRequestOptions(placeholderOption).load(event.getImage()).into(eventImageDetailsImageView);
        eventDescriptionDetailsTextView.setText(event.getDescription());
        eventStartDateDetailsTextView.setText(getContext().getString(R.string.start_date, event.getStartTime()));
        eventRegistrationEndDateDetailsTextView.setText(getContext().getString(R.string.registration_end_date, event.getRegistrationEndTime()));
        eventRemainingSeatsDetailsTextView.setText(getContext().getString(R.string.remaining_seats_left, event.getRemainingSeats()));
    }

    public void userIsAttendeeView() {
        eventRemainingSeatsDetailsTextView.setVisibility(GONE);
        signUpForEvent.setVisibility(GONE);
        signOutForEvent.setVisibility(VISIBLE);
    }

    public void userIsNotAttendeeView() {
        eventRemainingSeatsDetailsTextView.setVisibility(VISIBLE);
        signUpForEvent.setVisibility(VISIBLE);
        signOutForEvent.setVisibility(GONE);
    }

    public void registrationEnded() {
        signUpForEvent.setVisibility(GONE);
        signOutForEvent.setVisibility(GONE);
        eventRemainingSeatsDetailsTextView.setVisibility(GONE);
        registrationEndedTextView.setVisibility(VISIBLE);
    }

    public interface ButtonClickedRequestInterface {
        void buttonClicked();
    }
}