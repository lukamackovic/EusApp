package com.luka.mackovic.eus.repository.network;

import com.google.firebase.messaging.FirebaseMessaging;
import com.luka.mackovic.eus.domain.enumeration.NotificationTopics;

public class FirebaseTopicRepository implements TopicRepository{

    private static final String NEW_EVENTS = "NEW_EVENTS";
    private static final String NEW_NEWS = "NEW_NEWS";

    @Override
    public void subscribe(NotificationTopics notificationTopics) {
        switch (notificationTopics) {
            case NEW_EVENTS:
                FirebaseMessaging.getInstance().subscribeToTopic(NEW_EVENTS);
                break;
            case NEW_NEWS:
                FirebaseMessaging.getInstance().subscribeToTopic(NEW_NEWS);
                break;
        }
    }

    @Override
    public void unsubscribe(NotificationTopics notificationTopics) {
        switch (notificationTopics) {
            case NEW_EVENTS:
                FirebaseMessaging.getInstance().unsubscribeFromTopic(NEW_EVENTS);
                break;
            case NEW_NEWS:
                FirebaseMessaging.getInstance().unsubscribeFromTopic(NEW_NEWS);
                break;
        }
    }
}