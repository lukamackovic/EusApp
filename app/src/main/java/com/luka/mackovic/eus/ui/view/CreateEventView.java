package com.luka.mackovic.eus.ui.view;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.net.Uri;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textview.MaterialTextView;
import com.luka.mackovic.eus.R;
import com.luka.mackovic.eus.domain.model.Event;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;

import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Objects;

@EViewGroup(R.layout.view_create_event)
public class CreateEventView extends ConstraintLayout implements
        DatePickerDialog.OnDateSetListener,
        TimePickerDialog.OnTimeSetListener {

    public static final int FLAG_START_DATE_AND_TIME = 0;
    public static final int FLAG_REGISTRATION_END_DATE_AND_TIME = 1;

    private int flag;

    private Event event;

    private DatePickerDialog datePickerDialog;
    private Calendar calendar;
    private Date eventStartDate, eventRegistrationEndDate;
    private Uri uri;

    private int setDay, setMonth, setYear;
    private int dayFinal;
    private int monthFinal;
    private int yearFinal;

    @ViewById
    TextInputEditText eventTitleEditText;

    @ViewById
    TextInputEditText eventDescriptionEditText;

    @ViewById
    ImageView addPhotoImageView;

    @ViewById
    MaterialTextView addPhotoTextView;

    @ViewById
    TextInputEditText eventAttendeeLimitEditText;

    @ViewById
    MaterialTextView eventStartDateTextView;

    @ViewById
    MaterialTextView eventRegistrationEndDateTextView;

    @ViewById
    MaterialButton saveEvent;

    private ItemClickedRequestInterface itemClickedRequestInterface;

    public void setItemRequestListener(ItemClickedRequestInterface itemClickedRequestInterface) {
        this.itemClickedRequestInterface = itemClickedRequestInterface;
    }

    public CreateEventView(Context context) {
        super(context);
    }

    public CreateEventView(Context context, Event event) {
        super(context);
        this.event = event;
    }

    public void setEventImageUri(Uri imageUri) {
        uri = imageUri;
    }

    @SuppressLint("CheckResult")
    public void setEventImageView(String eventImage) {
        RequestOptions placeholderOption = new RequestOptions();
        placeholderOption.placeholder(R.drawable.ic_events);
        Glide.with(this).setDefaultRequestOptions(placeholderOption).load(eventImage).into(addPhotoImageView);
    }

    public void setSaveButtonVisible() {
        saveEvent.setVisibility(VISIBLE);
    }

    public Event getEvent() {
        Event newEvent;
        if (event != null) {
            newEvent = event;
        } else {
            newEvent = new Event();
        }

        newEvent.setTitle(Objects.requireNonNull(eventTitleEditText.getText()).toString());
        newEvent.setDescription(Objects.requireNonNull(eventDescriptionEditText.getText()).toString());
        if (uri != null){
            newEvent.setImage(uri.toString());
        }
        if (Objects.requireNonNull(eventAttendeeLimitEditText.getText()).toString().isEmpty()) {
            newEvent.setAttendeesLimit((long) 0);
        } else {
            newEvent.setAttendeesLimit(Long.parseLong(eventAttendeeLimitEditText.getText().toString()));
        }
        newEvent.setStartTime(eventStartDate);
        newEvent.setRegistrationEndTime(eventRegistrationEndDate);
        return newEvent;
    }

    @Click
    void addPhotoImageView() {
        itemClickedRequestInterface.itemClick(ItemClickAction.IMAGE_CLICKED);
    }

    @Click
    void saveEvent() {
        saveEvent.setVisibility(GONE);
        itemClickedRequestInterface.itemClick(ItemClickAction.SAVE_BUTTON_CLICKED);
    }

    @SuppressLint("DefaultLocale")
    @AfterViews
    public void init() {
        if (event == null) {
            Toast.makeText(getContext(), getContext().getString(R.string.create_event_message), Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getContext(), getContext().getString(R.string.edit_event_message), Toast.LENGTH_SHORT).show();
            eventTitleEditText.setText(event.getTitle());
            eventDescriptionEditText.setText(event.getDescription());
            eventAttendeeLimitEditText.setText(String.valueOf(event.getAttendeesLimit()));
            eventStartDateTextView.setText(getContext().getString(R.string.event_start, event.getStartTime()));
            eventStartDate = event.getStartTime();
            eventRegistrationEndDateTextView.setText(getContext().getString(R.string.event_start, event.getRegistrationEndTime()));
            eventRegistrationEndDate = event.getRegistrationEndTime();
        }
    }

    @SuppressLint("SetTextI18n")
    @Click
    void pickEventStartTime() {
        flag = FLAG_START_DATE_AND_TIME;
        calendar = Calendar.getInstance();
        setYear = calendar.get(Calendar.YEAR);
        setMonth = calendar.get(Calendar.MONTH);
        setDay = calendar.get(Calendar.DAY_OF_MONTH);

        datePickerDialog = new DatePickerDialog(getContext(), this, setYear, setMonth, setDay);
        datePickerDialog.show();
    }

    @SuppressLint("SetTextI18n")
    @Click
    void pickEventRegistrationEndDate() {
        flag = FLAG_REGISTRATION_END_DATE_AND_TIME;
        calendar = Calendar.getInstance();
        setYear = calendar.get(Calendar.YEAR);
        setMonth = calendar.get(Calendar.MONTH);
        setDay = calendar.get(Calendar.DAY_OF_MONTH);

        datePickerDialog = new DatePickerDialog(getContext(), this, setYear, setMonth, setDay);
        datePickerDialog.show();
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int day) {
        yearFinal = year;
        monthFinal = month;
        dayFinal = day;

        int setHour = 0, setMinute = 0;

        TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(), this,
                setHour, setMinute, true);
        timePickerDialog.show();
    }

    @SuppressLint({"SetTextI18n", "DefaultLocale"})
    @Override
    public void onTimeSet(TimePicker view, int hour, int minute) {
        calendar.set(yearFinal, monthFinal, dayFinal, hour, minute);

        switch (flag) {
            case FLAG_START_DATE_AND_TIME:
                eventStartDate = calendar.getTime();
                eventStartDateTextView.setText(getContext().getString(R.string.event_start, eventStartDate));
                break;
            case FLAG_REGISTRATION_END_DATE_AND_TIME:
                eventRegistrationEndDate = calendar.getTime();
                eventRegistrationEndDateTextView.setText(getContext().getString(R.string.event_registration_end, eventRegistrationEndDate));
                break;
        }
    }

    public void setErrors(Collection<CreateEventFormValueType> validationViolationValueType) {
        for (CreateEventFormValueType createEventFormValueType : validationViolationValueType) {
            switch (createEventFormValueType) {
                case TITLE:
                    eventTitleEditText.setError(getContext().getString(R.string.event_title_error_message));
                    break;
                case DESCRIPTION:
                    break;
                case IMAGE:
                    addPhotoTextView.setError(getContext().getString(R.string.event_image_error_message));
                    break;
                case ATTENDEES_LIMIT:
                    eventAttendeeLimitEditText.setError(getContext().getString(R.string.event_registration_limit_error_message));
                    break;
                case EVENT_START_DATE:
                    eventStartDateTextView.setError(getContext().getString(R.string.event_start_date_error_message));
                    break;
                case REGISTRATION_END_DATE:
                    eventRegistrationEndDateTextView.setError(getContext().getString(R.string.event_registration_end_date_error_message));
                    break;
            }
        }
    }

    public interface ItemClickedRequestInterface {
        void itemClick(ItemClickAction itemClickAction);
    }

    public enum ItemClickAction {
        IMAGE_CLICKED,
        SAVE_BUTTON_CLICKED
    }
}