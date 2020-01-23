package com.luka.mackovic.eus;

import android.app.Application;

import com.luka.mackovic.eus.repository.network.RepositoryModule;
import com.luka.mackovic.eus.service.ServiceModule;
import com.luka.mackovic.eus.usecase.crud.UseCaseModule;

import dagger.BindsInstance;
import dagger.Component;
import dagger.android.AndroidInjector;
import dagger.android.support.AndroidSupportInjectionModule;

@Component(modules = {
        AndroidSupportInjectionModule.class,
        EusApplicationModule.class,
        ServiceModule.class,
        RepositoryModule.class,
        UseCaseModule.class
})
public interface EusApplicationComponent extends AndroidInjector<EusApplication> {

    @Component.Builder
    interface Builder{

        @BindsInstance
        Builder application(Application application);

        EusApplicationComponent build();
    }
}