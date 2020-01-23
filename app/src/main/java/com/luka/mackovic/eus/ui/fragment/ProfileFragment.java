package com.luka.mackovic.eus.ui.fragment;

import android.view.View;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseUser;
import com.luka.mackovic.eus.R;
import com.luka.mackovic.eus.domain.enumeration.NotificationTopics;
import com.luka.mackovic.eus.domain.model.Event;
import com.luka.mackovic.eus.service.AuthenticationService;
import com.luka.mackovic.eus.ui.activity.LoginActivity_;
import com.luka.mackovic.eus.ui.adapter.EventAdapter;
import com.luka.mackovic.eus.ui.view.ProfileInfoItemView;
import com.luka.mackovic.eus.ui.view.ProfileInfoItemView_;
import com.luka.mackovic.eus.usecase.crud.EventCrudUseCase;
import com.luka.mackovic.eus.usecase.crud.NotificationUseCase;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.inject.Inject;

import dagger.android.support.DaggerFragment;
import io.reactivex.disposables.Disposable;

@EFragment(R.layout.fragment_profile)
public class ProfileFragment extends DaggerFragment
        implements ProfileInfoItemView.ProfileInfoListener,
        EventAdapter.EventClickListener{

    private static final Comparator<Event> sortAscendingStartDate = (event, nextEvent) ->
            event.getStartTime().compareTo(nextEvent.getStartTime());

    @Inject
    AuthenticationService authenticationService;

    @Inject
    EventCrudUseCase eventCrudUseCase;

    @Inject
    NotificationUseCase notificationUseCase;

    @ViewById
    CardView profileFragmentUserInformationView;

    @ViewById
    CardView profileFragmentUserEventsView;

    @ViewById
    RecyclerView eventProfileRecyclerView;

    @ViewById
    TextView profileNoEventsTextView;

    @Bean
    EventAdapter eventAdapter;

    private Disposable authDisposable, disposable;

    @AfterViews
    void init() {
        View profileInfo = ProfileInfoItemView_.build(getContext(), authenticationService.getCurrentUser(), this);
        profileFragmentUserInformationView.addView(profileInfo);
        eventProfileRecyclerView.setHasFixedSize(true);
        eventProfileRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        disposable = eventCrudUseCase.findAllByAttendingUser()
                .subscribe(this::setEventsList, this::showErrorMessage);
    }

    @Override
    public void authAction(ProfileInfoItemView.AuthAction action) {
        switch (action) {
            case SIGN_OUT:
                authenticationService.signOut();
                notificationUseCase.unsubscribe(NotificationTopics.NEW_EVENTS);
                notificationUseCase.unsubscribe(NotificationTopics.NEW_NEWS);
                LoginActivity_.intent(getContext()).start();
                break;
            case DELETE_DATA:
                authDisposable = authenticationService.deleteData()
                        .doOnSuccess(this::updateUI)
                        .doOnError(Throwable::printStackTrace)
                        .subscribe();
                break;
        }
    }

    private void updateUI(FirebaseUser user) {
        if (user == null) {
            if (authDisposable != null && !authDisposable.isDisposed()) {
                authDisposable.dispose();
            }
            notificationUseCase.unsubscribe(NotificationTopics.NEW_EVENTS);
            notificationUseCase.unsubscribe(NotificationTopics.NEW_NEWS);
            LoginActivity_.intent(this).start();
        }
    }

    private void setEventsList(List<Event> events) {
        if (events == null) {
            return;
        }
        if (events.isEmpty()) {
            profileNoEventsTextView.setVisibility(View.VISIBLE);
        }
        Collections.sort(events, sortAscendingStartDate);
        eventAdapter.setEvents(events, this);
        eventProfileRecyclerView.setAdapter(eventAdapter);

    }

    private void showErrorMessage(Throwable throwable) {
        Snackbar.make(profileFragmentUserEventsView, getString(R.string.error_message_get_events), Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (disposable != null && !disposable.isDisposed()) {
            disposable.dispose();
        }
    }

    @Override
    public void onItemLongClick(String eventId, String eventTitle, String eventImage) {
    }
}