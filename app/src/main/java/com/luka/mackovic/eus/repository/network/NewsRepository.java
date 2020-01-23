package com.luka.mackovic.eus.repository.network;

import com.luka.mackovic.eus.domain.model.News;

import java.util.List;

import io.reactivex.Single;

public interface NewsRepository {

    Single<News> save(News news);

    Single<List<News>> getAll();

    void delete(String newsId);

    Single<News> findById(String newsId);
}