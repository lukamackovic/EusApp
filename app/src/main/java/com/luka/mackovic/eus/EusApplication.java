package com.luka.mackovic.eus;

import android.annotation.SuppressLint;

import org.androidannotations.annotations.EApplication;

import dagger.android.AndroidInjector;
import dagger.android.DaggerApplication;

@SuppressLint("Registered")
@EApplication
public class EusApplication extends DaggerApplication {

    @Override
    protected AndroidInjector<? extends DaggerApplication> applicationInjector() {
        return DaggerEusApplicationComponent.builder().application(this).build();
    }
}