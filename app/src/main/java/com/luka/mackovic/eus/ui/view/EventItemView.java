package com.luka.mackovic.eus.ui.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.textview.MaterialTextView;
import com.luka.mackovic.eus.R;
import com.luka.mackovic.eus.domain.model.Event;

import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;

@EViewGroup(R.layout.item_view_event)
public class EventItemView extends RelativeLayout {

    @ViewById
    ImageView eventImage;

    @ViewById
    MaterialTextView eventTitle;

    @ViewById
    MaterialTextView eventRemainingSeats;

    @ViewById
    MaterialTextView eventRegistrationEndDate;

    public EventItemView(Context context) {
        super(context);
    }

    @SuppressLint("CheckResult")
    public void bind(Event event) {
        RequestOptions placeholderOption = new RequestOptions();
        placeholderOption.placeholder(R.drawable.ic_events);
        Glide.with(this).setDefaultRequestOptions(placeholderOption).load(event.getImage()).into(this.eventImage);
        this.eventTitle.setText(event.getTitle());
        this.eventRemainingSeats.setText(getContext().getString(R.string.remaining_seats_left, event.getRemainingSeats()));
        this.eventRegistrationEndDate.setText(getContext().getString(R.string.registration_end_date, event.getRegistrationEndTime()));
    }
}