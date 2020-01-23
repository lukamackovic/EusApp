package com.luka.mackovic.eus.ui.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import android.provider.Settings;
import android.view.View;
import android.widget.FrameLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;
import com.luka.mackovic.eus.R;
import com.luka.mackovic.eus.domain.exception.CreateEventValidationException;
import com.luka.mackovic.eus.domain.model.Event;
import com.luka.mackovic.eus.ui.view.CreateEventView;
import com.luka.mackovic.eus.ui.view.CreateEventView_;
import com.luka.mackovic.eus.usecase.crud.EventCrudUseCase;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ViewById;

import java.util.Objects;

import javax.inject.Inject;

import dagger.android.support.DaggerAppCompatActivity;
import io.reactivex.disposables.Disposable;

@SuppressLint("Registered")
@EActivity(R.layout.activity_create_event)
public class CreateEventActivity extends DaggerAppCompatActivity
        implements CreateEventView.ItemClickedRequestInterface,
        PermissionListener,
        Toolbar.OnClickListener{

    private final static int ACTION_APPLICATION_DETAILS_SETTINGS_REQUEST_CODE = 9;
    private final static int PICK_IMAGE = 8;

    private final static String SCHEME_OF_URI = "package";

    @Inject
    EventCrudUseCase eventCrudUseCase;
    @ViewById
    FrameLayout createEventContentView;
    @ViewById
    Toolbar toolbar;
    @Extra("id")
    String eventId;

    private Uri uri;
    private Disposable disposable, disposableGet;
    private CreateEventView createEventForm;

    @AfterViews
    void init() {
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle("");
        toolbar.setNavigationOnClickListener(this);
        if (eventId == null) {
            createEventForm = CreateEventView_.build(this);
            createEventForm.setItemRequestListener(this);
            createEventContentView.addView(createEventForm);
        } else {
            disposableGet = eventCrudUseCase.getEvent(eventId)
                    .doOnSuccess(this::setInitView)
                    .doOnError(Throwable::printStackTrace)
                    .subscribe();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE) {
            uri = data != null ? data.getData() : null;
            createEventForm.setEventImageUri(uri);
            createEventForm.setEventImageView(uri.toString());
        }
    }

    @Override
    public void itemClick(CreateEventView.ItemClickAction itemClickAction) {
        switch (itemClickAction) {
            case IMAGE_CLICKED:
                Dexter.withActivity(this)
                        .withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                        .withListener(this).check();
                break;

            case SAVE_BUTTON_CLICKED:
                checkDataValidation();
                break;
        }
    }

    @SuppressLint("CheckResult")
    private void checkDataValidation() {
        Event event = createEventForm.getEvent();
        disposable = eventCrudUseCase.saveEvent(event, uri)
                .subscribe(this::handleSuccess, this::handleError);
    }

    public void handleSuccess(Event event) {
        HomeActivity_.intent(this).start();
    }

    public void handleError(Throwable t) {
        createEventForm.setSaveButtonVisible();
        createEventForm.setErrors(((CreateEventValidationException) t).getValidationViolationValueType());
    }

    private void setInitView(Event event) {
        createEventForm = CreateEventView_.build(this, event);
        createEventForm.setItemRequestListener(this);
        createEventContentView.addView(createEventForm);
        createEventForm.setEventImageView(event.getImage());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (disposable != null && !disposable.isDisposed() ||
                disposableGet != null && !disposableGet.isDisposed()) {
            disposable.dispose();
            disposableGet.dispose();
        }
    }

    @Override
    public void onPermissionGranted(PermissionGrantedResponse response) {
        Intent pickPhoto = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(pickPhoto, PICK_IMAGE);
    }

    @Override
    public void onPermissionDenied(PermissionDeniedResponse response) {
        if (response.isPermanentlyDenied()) {
            AlertDialog.Builder builder = new AlertDialog.Builder(CreateEventActivity.this);
            builder.setTitle(getText(R.string.builder_title))
                    .setMessage(getText(R.string.builder_message))
                    .setPositiveButton(getText(R.string.builder_positive_button), (dialog, which) -> {
                        Intent intent = new Intent();
                        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        intent.setData(Uri.fromParts(SCHEME_OF_URI, getPackageName(), null));
                        startActivityForResult(intent, ACTION_APPLICATION_DETAILS_SETTINGS_REQUEST_CODE);
                    })
                    .setNegativeButton(getText(R.string.builder_negative_button), null)
                    .show();
        }
    }

    @Override
    public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {
        token.continuePermissionRequest();
    }

    @Override
    public void onClick(View v) {
        finish();
    }
}