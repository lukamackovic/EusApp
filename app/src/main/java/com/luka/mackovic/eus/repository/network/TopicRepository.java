package com.luka.mackovic.eus.repository.network;

import com.luka.mackovic.eus.domain.enumeration.NotificationTopics;

public interface TopicRepository {

    void subscribe(NotificationTopics notificationTopics);

    void unsubscribe(NotificationTopics notificationTopics);
}