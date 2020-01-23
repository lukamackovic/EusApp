package com.luka.mackovic.eus.ui.adapter;

import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

public class ViewWrapper<V extends View> extends RecyclerView.ViewHolder {

    private final V view;

    ViewWrapper(V itemView) {
        super(itemView);
        view = itemView;
    }

    public V getView() {
        return view;
    }
}