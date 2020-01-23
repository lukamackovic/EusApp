package com.luka.mackovic.eus;

import com.luka.mackovic.eus.ui.activity.CreateEventActivity_;
import com.luka.mackovic.eus.ui.activity.CreateNewsActivity_;
import com.luka.mackovic.eus.ui.activity.EventDetailsActivity_;
import com.luka.mackovic.eus.ui.activity.HomeActivity_;
import com.luka.mackovic.eus.ui.activity.LoginActivity_;
import com.luka.mackovic.eus.ui.activity.NewsDetailsActivity_;
import com.luka.mackovic.eus.ui.fragment.EventsFragment_;
import com.luka.mackovic.eus.ui.fragment.NewsFragment_;
import com.luka.mackovic.eus.ui.fragment.ProfileFragment_;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
abstract class EusApplicationModule {

    @ContributesAndroidInjector
    abstract LoginActivity_ contributeLoginActivity();

    @ContributesAndroidInjector
    abstract HomeActivity_ contributeHomeActivity();

    @ContributesAndroidInjector
    abstract ProfileFragment_ contributeProfileFragment();

    @ContributesAndroidInjector
    abstract EventsFragment_ contributeEventsFragment();

    @ContributesAndroidInjector
    abstract NewsFragment_ contributeNewsFragment();

    @ContributesAndroidInjector
    abstract CreateEventActivity_ contributeCreateEventActivity();

    @ContributesAndroidInjector
    abstract EventDetailsActivity_ contributeEventDetailsActivity();

    @ContributesAndroidInjector
    abstract CreateNewsActivity_ contributeCreateNewsActivity();

    @ContributesAndroidInjector
    abstract NewsDetailsActivity_ contributeNewsDetailsActivity();
}