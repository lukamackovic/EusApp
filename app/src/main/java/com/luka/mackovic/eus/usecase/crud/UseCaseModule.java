package com.luka.mackovic.eus.usecase.crud;

import com.luka.mackovic.eus.repository.network.EventRepository;
import com.luka.mackovic.eus.repository.network.ImageRepository;
import com.luka.mackovic.eus.repository.network.NewsRepository;
import com.luka.mackovic.eus.repository.network.TopicRepository;

import dagger.Module;
import dagger.Provides;

@Module
public class UseCaseModule {

    @Provides
    EventCrudUseCase provideEventCrudUseCase(EventRepository eventRepository,
                                             ImageRepository imageRepository) {

        return new EventCrudUseCase(eventRepository, imageRepository);
    }

    @Provides
    NotificationUseCase provideNotificationUseCase(TopicRepository topicRepository) {
        return new NotificationUseCase(topicRepository);
    }

    @Provides
    NewsCrudUseCase provideNewsCrudUseCase(NewsRepository newsRepository) {
        return new NewsCrudUseCase(newsRepository);
    }
}