package com.luka.mackovic.eus.ui.activity;

import android.annotation.SuppressLint;
import android.content.Intent;

import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.luka.mackovic.eus.R;
import com.luka.mackovic.eus.domain.enumeration.NotificationTopics;
import com.luka.mackovic.eus.service.AuthenticationService;
import com.luka.mackovic.eus.usecase.crud.NotificationUseCase;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import javax.inject.Inject;

import dagger.android.support.DaggerAppCompatActivity;
import io.reactivex.disposables.Disposable;

@SuppressLint("Registered")
@EActivity(R.layout.activity_login)
public class LoginActivity extends DaggerAppCompatActivity {

    private final static Integer LOGIN_REQUEST_CODE = 100;

    @Inject
    AuthenticationService authenticationService;

    @Inject
    NotificationUseCase notificationUseCase;

    @ViewById
    ConstraintLayout rootView;

    private Disposable authDisposable;

    @AfterViews
    public void init() {
        SignInButton signInButton = findViewById(R.id.sign_in_button);
        signInButton.setOnClickListener(v -> startActivityForResult(authenticationService.getSignInIntent(), LOGIN_REQUEST_CODE));
        updateUI(authenticationService.getCurrentUser());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode != LOGIN_REQUEST_CODE) {
            return;
        }
        Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
        try {
            GoogleSignInAccount account = task.getResult(ApiException.class);
            AuthCredential credential = null;
            if (account != null) {
                credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
            }
            authDisposable = authenticationService.signInWithCredential(credential)
                    .doOnSuccess(this::updateUI)
                    .doOnError(Throwable::printStackTrace)
                    .subscribe();
        } catch (ApiException e) {
            Snackbar.make(rootView, getText(R.string.error_task_google_sign_in), Snackbar.LENGTH_SHORT).show();
        }
    }

    private void updateUI(FirebaseUser user) {
        if (user != null) {
            if (authDisposable != null && !authDisposable.isDisposed()) {
                authDisposable.dispose();
            }
            notificationUseCase.subscribe(NotificationTopics.NEW_EVENTS);
            notificationUseCase.subscribe(NotificationTopics.NEW_NEWS);
            HomeActivity_.intent(this).start();

        } else {
            Snackbar.make(rootView, getText(R.string.report_not_sign_in), Snackbar.LENGTH_SHORT).show();
        }
    }
}