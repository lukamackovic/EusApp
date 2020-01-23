package com.luka.mackovic.eus.repository.network;

import dagger.Module;
import dagger.Provides;

@Module
public class RepositoryModule {

    @Provides
    EventRepository provideEventRepository() {
        return new FirebaseEventRepository();
    }

    @Provides
    ImageRepository provideImageRepository() {
        return new FirebaseStorageImageRepository();
    }

    @Provides
    TopicRepository provideTopicRepository() {
        return new FirebaseTopicRepository();
    }

    @Provides
    NewsRepository provideNewsRepository() {
        return new FirebaseNewsRepository();
    }

}