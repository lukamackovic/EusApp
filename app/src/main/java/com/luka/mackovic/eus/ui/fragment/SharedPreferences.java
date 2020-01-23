package com.luka.mackovic.eus.ui.fragment;

import com.luka.mackovic.eus.R;

import org.androidannotations.annotations.sharedpreferences.DefaultInt;
import org.androidannotations.annotations.sharedpreferences.DefaultRes;
import org.androidannotations.annotations.sharedpreferences.SharedPref;

@SharedPref
public interface SharedPreferences {

    @DefaultRes(R.string.ascending)
    String sortEventDirection();

    @DefaultRes(R.string.ascending)
    String sortNewsDirection();

    @DefaultInt(R.drawable.ic_sort_ascending)
    int imageEventResource();

    @DefaultInt(R.drawable.ic_sort_ascending)
    int imageNewsResource();

    @DefaultRes(R.string.created_date)
    String sortByEvents();

    @DefaultRes(R.string.created_date)
    String sortByNews();
}