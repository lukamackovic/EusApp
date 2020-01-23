package com.luka.mackovic.eus.repository.network;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.luka.mackovic.eus.domain.model.News;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;

import io.reactivex.Single;
import io.reactivex.SingleObserver;

public class FirebaseNewsRepository implements NewsRepository{

    private static final String COLLECTION_NEWS = "news";

    private final FirebaseFirestore firestore;

    FirebaseNewsRepository() {
        firestore = FirebaseFirestore.getInstance();
    }

    @Override
    public Single<News> save(News news) {
        return new Single<News>() {
            @Override
            protected void subscribeActual(SingleObserver<? super News> observer) {
                CollectionReference newArticleReference = firestore
                        .collection(COLLECTION_NEWS);
                if (news.getId() == null) {
                    String articleId = newArticleReference.document().getId();
                    news.setId(articleId);
                    news.setCreatedDate(Calendar.getInstance().getTime());

                    newArticleReference.document(articleId).set(news)
                            .addOnCompleteListener(task -> observer.onSuccess(news))
                            .addOnFailureListener(observer::onError);
                } else {
                    news.setCreatedDate(Calendar.getInstance().getTime());
                    newArticleReference.document(news.getId()).set(news)
                            .addOnCompleteListener(task -> observer.onSuccess(news))
                            .addOnFailureListener(observer::onError);
                }

            }
        };
    }

    @Override
    public Single<List<News>> getAll() {
        return new Single<List<News>>() {
            @Override
            protected void subscribeActual(SingleObserver<? super List<News>> observer) {
                CollectionReference collectionReference = firestore
                        .collection(COLLECTION_NEWS);
                collectionReference.get()
                        .addOnSuccessListener(task -> {
                            List<News> newsList = new ArrayList<>();
                            for (QueryDocumentSnapshot document : task) {
                                News news = document.toObject(News.class);
                                newsList.add(news);
                            }
                            observer.onSuccess(newsList);
                        }).addOnFailureListener(observer::onError);
            }
        };
    }

    @Override
    public void delete(String newsId) {
        DocumentReference documentReference = firestore
                .collection(COLLECTION_NEWS)
                .document(newsId);
        documentReference.delete();
    }

    @Override
    public Single<News> findById(String newsId) {
        return new Single<News>() {
            @Override
            protected void subscribeActual(SingleObserver<? super News> observer) {
                DocumentReference documentReference = firestore
                        .collection(COLLECTION_NEWS)
                        .document(newsId);
                documentReference.get().addOnCompleteListener(task -> {
                    News news = Objects.requireNonNull(task.getResult()).toObject(News.class);
                    observer.onSuccess(news);
                }).addOnFailureListener(observer::onError);
            }
        };
    }
}