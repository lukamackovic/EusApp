package com.luka.mackovic.eus.ui.activity;

import android.annotation.SuppressLint;
import android.view.View;
import android.widget.FrameLayout;

import androidx.appcompat.widget.Toolbar;

import com.luka.mackovic.eus.R;
import com.luka.mackovic.eus.domain.model.News;
import com.luka.mackovic.eus.ui.view.NewsDetailsView;
import com.luka.mackovic.eus.ui.view.NewsDetailsView_;
import com.luka.mackovic.eus.usecase.crud.NewsCrudUseCase;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ViewById;

import java.util.Objects;

import javax.inject.Inject;

import dagger.android.support.DaggerAppCompatActivity;
import io.reactivex.disposables.Disposable;

@SuppressLint("Registered")
@EActivity(R.layout.activity_news_details)
public class NewsDetailsActivity extends DaggerAppCompatActivity
        implements Toolbar.OnClickListener {

    @ViewById
    FrameLayout articleDetailsContentView;

    @ViewById
    Toolbar toolbar;

    @Extra("id")
    String newsId;

    @Inject
    NewsCrudUseCase articleCrudUseCase;

    private NewsDetailsView newsDetailsView;
    private Disposable disposable;

    @AfterViews
    void init() {
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle("");
        toolbar.setNavigationOnClickListener(this);
        newsDetailsView = NewsDetailsView_.build(this);
        articleDetailsContentView.addView(newsDetailsView);
        updateUI();
    }

    private void updateUI() {
        disposable = articleCrudUseCase.getNews(newsId)
                .doOnSuccess(this::setInitView)
                .doOnError(Throwable::printStackTrace)
                .subscribe();
    }

    private void setInitView(News news) {
        newsDetailsView.setNewsItems(news);
    }

    @Override
    public void onClick(View v) {
        HomeActivity_.intent(this).start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (disposable != null && !disposable.isDisposed()) {
            disposable.dispose();
        }
    }
}