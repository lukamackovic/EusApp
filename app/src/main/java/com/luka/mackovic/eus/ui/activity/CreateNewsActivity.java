package com.luka.mackovic.eus.ui.activity;

import android.annotation.SuppressLint;
import android.view.View;
import android.widget.FrameLayout;

import androidx.appcompat.widget.Toolbar;

import com.luka.mackovic.eus.R;
import com.luka.mackovic.eus.domain.exception.CreateNewsValidationException;
import com.luka.mackovic.eus.domain.model.News;
import com.luka.mackovic.eus.ui.view.CreateNewsView;
import com.luka.mackovic.eus.ui.view.CreateNewsView_;
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
@EActivity(R.layout.activity_create_news)
public class CreateNewsActivity extends DaggerAppCompatActivity
        implements CreateNewsView.ItemClickedRequestInterface,
        Toolbar.OnClickListener {

    @Inject
    NewsCrudUseCase articleCrudUseCase;

    @ViewById
    FrameLayout createArticleContentView;

    @ViewById
    Toolbar toolbar;

    @Extra("id")
    String newsId;

    private Disposable disposable, disposableGet;

    private CreateNewsView createNewsForm;

    @AfterViews
    void init() {
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle("");
        toolbar.setNavigationOnClickListener(this);
        if (newsId == null) {
            createNewsForm = CreateNewsView_.build(this);
            createNewsForm.setItemClickedListener(this);
            createArticleContentView.addView(createNewsForm);
        } else {
            disposableGet = articleCrudUseCase.getNews(newsId)
                    .doOnSuccess(this::setInitView)
                    .doOnError(this::handleError)
                    .subscribe();
        }
    }

    @Override
    public void itemClick(CreateNewsView.ItemClickAction itemClickAction) {
        //noinspection SwitchStatementWithTooFewBranches
        switch (itemClickAction) {
            case SAVE_BUTTON_CLICKED:
                checkDataValidation();
                break;
        }
    }

    private void checkDataValidation() {
        News news = createNewsForm.getNews();
        disposable = articleCrudUseCase.saveNews(news)
                .subscribe(this::handleSuccess, this::handleError);
    }

    private void handleError(Throwable t) {
        createNewsForm.setSaveButtonVisible();
        createNewsForm.setErrors(((CreateNewsValidationException) t).getValidationViolationValueType());
    }

    private void handleSuccess(News news) {
        HomeActivity_.intent(this).start();
    }

    private void setInitView(News news) {
        createNewsForm = CreateNewsView_.build(this, news);
        createNewsForm.setItemClickedListener(this);
        createArticleContentView.addView(createNewsForm);
    }

    @Override
    public void onClick(View v) {
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (disposable != null && !disposable.isDisposed() || disposableGet != null && !disposableGet.isDisposed()) {
            disposable.dispose();
            disposableGet.dispose();
        }
    }
}