package com.luka.mackovic.eus.ui.fragment;

import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;

import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;
import com.luka.mackovic.eus.R;
import com.luka.mackovic.eus.domain.enumeration.SortDirection;
import com.luka.mackovic.eus.domain.model.Event;
import com.luka.mackovic.eus.ui.activity.CreateEventActivity_;
import com.luka.mackovic.eus.ui.activity.HomeActivity_;
import com.luka.mackovic.eus.ui.adapter.EventAdapter;
import com.luka.mackovic.eus.usecase.crud.EventCrudUseCase;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.sharedpreferences.Pref;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

import javax.inject.Inject;

import dagger.android.support.DaggerFragment;
import io.reactivex.disposables.Disposable;

@EFragment(R.layout.fragment_events)
public class EventsFragment extends DaggerFragment
        implements Spinner.OnItemSelectedListener,
        EventAdapter.EventClickListener,
        Toolbar.OnClickListener{

    private static final String DATE_CREATED = "date created";
    private static final String DATE_START = "date start";
    private static final String REGISTRATION_END = "registration end";
    private static final String TITLE = "title";

    private static final Comparator<Event> sortAscendingDateCreated = (event, nextEvent) ->
            event.getCreatedDate().compareTo(nextEvent.getCreatedDate());
    private static final Comparator<Event> sortDescendingDateCreated = (event, nextEvent) ->
            nextEvent.getCreatedDate().compareTo(event.getCreatedDate());

    private static final Comparator<Event> sortAscendingStartDate = (event, nextEvent) ->
            event.getStartTime().compareTo(nextEvent.getStartTime());
    private static final Comparator<Event> sortDescendingStartDate = (event, nextEvent) ->
            nextEvent.getStartTime().compareTo(event.getStartTime());

    private static final Comparator<Event> sortAscendingRegistrationEndDate = (event, nextEvent) ->
            event.getRegistrationEndTime().compareTo(nextEvent.getRegistrationEndTime());
    private static final Comparator<Event> sortDescendingRegistrationEndDate = (event, nextEvent) ->
            nextEvent.getRegistrationEndTime().compareTo(event.getRegistrationEndTime());

    private static final Comparator<Event> sortAscendingTitle = (event, nextEvent) ->
            event.getTitle().compareTo(nextEvent.getTitle());
    private static final Comparator<Event> sortDescendingTitle = (event, nextEvent) ->
            nextEvent.getTitle().compareTo(event.getTitle());

    @Inject
    EventCrudUseCase eventCrudUseCase;

    @ViewById
    CoordinatorLayout eventFragmentContentView;

    @ViewById
    RecyclerView eventRecyclerView;

    @ViewById
    ImageView sortEvents;

    @ViewById
    Spinner sortByEvents;

    @ViewById
    Toolbar toolbar;

    @Bean
    EventAdapter eventAdapter;

    @Pref
    SharedPreferences_ sharedPreferences;

    @Click
    void sortEvents() {
        if (SortDirection.ASCENDING == SortDirection.valueOf(sharedPreferences.sortEventDirection().get())) {
            sharedPreferences.sortEventDirection().put(SortDirection.DESCENDING.name());
            sharedPreferences.imageEventResource().put(R.drawable.ic_sort_descending);
        } else {
            sharedPreferences.sortEventDirection().put(SortDirection.ASCENDING.name());
            sharedPreferences.imageEventResource().put(R.drawable.ic_sort_ascending);
        }
        initView();
    }

    @Click
    void addNewEvent() {
        CreateEventActivity_.intent(this).start();
    }

    private Disposable disposable;

    @AfterViews
    void init() {
        eventRecyclerView.setHasFixedSize(true);
        eventRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        initView();
        initSpinner();
    }

    private void initView() {
        disposable = eventCrudUseCase.getEvents()
                .subscribe(this::updateUI, this::showErrorMessage);
    }

    private void updateUI(List<Event> events) {
        if (events == null || events.isEmpty()) {
            Snackbar.make(eventFragmentContentView, Objects.requireNonNull(getContext()).getText(R.string.no_events_error_message), Snackbar.LENGTH_LONG).show();
        } else {
            SortDirection desiredSortDirection = SortDirection.valueOf(sharedPreferences.sortEventDirection().get());

            switch (sharedPreferences.sortByEvents().get()) {
                case DATE_CREATED:
                    Collections.sort(events, desiredSortDirection == SortDirection.ASCENDING
                            ? sortAscendingDateCreated
                            : sortDescendingDateCreated
                    );
                    break;
                case DATE_START:
                    Collections.sort(events, desiredSortDirection == SortDirection.ASCENDING
                            ? sortAscendingStartDate
                            : sortDescendingStartDate
                    );
                    break;
                case REGISTRATION_END:
                    Collections.sort(events, desiredSortDirection == SortDirection.ASCENDING
                            ? sortAscendingRegistrationEndDate
                            : sortDescendingRegistrationEndDate
                    );
                    break;
                case TITLE:
                    Collections.sort(events, desiredSortDirection == SortDirection.ASCENDING
                            ? sortAscendingTitle
                            : sortDescendingTitle
                    );
                    break;
            }

            eventAdapter.setEvents(events, this);
            eventRecyclerView.setAdapter(eventAdapter);
        }
        sortEvents.setImageResource(sharedPreferences.imageEventResource().get());
    }

    private void initSpinner() {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(Objects.requireNonNull(getContext()),
                R.array.events_sort_by_list, R.layout.spinner_item);
        adapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        sortByEvents.setAdapter(adapter);
        sortByEvents.setSelection(adapter.getPosition(sharedPreferences.sortByEvents().get()));
        sortByEvents.setOnItemSelectedListener(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (disposable != null && !disposable.isDisposed()) {
            disposable.dispose();
        }
    }

    private void showErrorMessage(Throwable throwable) {
        Snackbar.make(eventFragmentContentView, getString(R.string.error_message_get_events), Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        sharedPreferences.sortByEvents().put(parent.getItemAtPosition(position).toString());
        initView();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }

    @Override
    public void onItemLongClick(String eventId, String eventTitle, String eventImage) {
        toolbar.setVisibility(View.VISIBLE);
        toolbar.setTitle(getString(R.string.event_toolbar_title, eventTitle));
        toolbar.setNavigationOnClickListener(this);
        toolbar.setOnMenuItemClickListener(item -> {
            switch (item.getItemId()) {
                case R.id.delete:
                    eventCrudUseCase.deleteEvent(eventId, eventImage);
                    HomeActivity_.intent(getContext()).start();
                    return true;
                case R.id.edit:
                    CreateEventActivity_.intent(getContext()).eventId(eventId).start();
                    return true;
            }
            return false;
        });
    }

    @Override
    public void onClick(View v) {
        toolbar.setVisibility(View.GONE);
    }
}