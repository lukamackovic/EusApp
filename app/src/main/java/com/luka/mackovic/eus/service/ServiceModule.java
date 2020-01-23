package com.luka.mackovic.eus.service;

import android.app.Application;

import dagger.Module;
import dagger.Provides;

@Module
public class ServiceModule {

    @Provides
    AuthenticationService provideAuthService(Application application) {
        return new AuthenticationService(application);
    }
}