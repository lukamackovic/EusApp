package com.luka.mackovic.eus.usecase.crud;

import com.luka.mackovic.eus.domain.enumeration.NotificationTopics;
import com.luka.mackovic.eus.repository.network.TopicRepository;

import javax.inject.Inject;

public class NotificationUseCase {

    private final TopicRepository topicRepository;

    @Inject
    NotificationUseCase(TopicRepository topicRepository) {
        this.topicRepository = topicRepository;
    }

    public void subscribe(NotificationTopics notificationTopics){
        topicRepository.subscribe(notificationTopics);
    }

    public void unsubscribe(NotificationTopics notificationTopics){
        topicRepository.unsubscribe(notificationTopics);
    }
}