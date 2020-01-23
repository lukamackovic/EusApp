package com.luka.mackovic.eus.ui.adapter;

import android.content.Context;
import android.view.ViewGroup;

import com.luka.mackovic.eus.domain.model.Event;
import com.luka.mackovic.eus.ui.activity.EventDetailsActivity_;
import com.luka.mackovic.eus.ui.view.EventItemView;
import com.luka.mackovic.eus.ui.view.EventItemView_;

import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;

import java.util.List;

@EBean
public class EventAdapter extends RecyclerViewAdapterBase<Event, EventItemView> {

    @RootContext
    Context context;

    private EventClickListener eventClickListener;

    public void setEvents(List<Event> events, EventClickListener eventClickListener) {
        this.eventClickListener = eventClickListener;
        this.items = events;
    }

    @Override
    protected EventItemView onCreateItemView(ViewGroup parent, int viewType) {
        return EventItemView_.build(context);
    }

    @Override
    public void onBindViewHolder(ViewWrapper<EventItemView> holder, int position) {
        holder.getView().bind(items.get(position));
        holder.getView().setOnClickListener(v -> {
            String eventId = items.get(position).getId();
            EventDetailsActivity_.intent(context).eventId(eventId).start();
        });
        holder.getView().setOnLongClickListener(v -> {
            eventClickListener.onItemLongClick(items.get(position).getId(),
                    items.get(position).getTitle(), items.get(position).getImage());
            return true;
        });
    }

    public interface EventClickListener {
        void onItemLongClick(String eventId, String eventTitle, String eventImage);
    }
}