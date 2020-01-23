package com.luka.mackovic.eus.service;

import android.content.Context;
import android.content.Intent;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.luka.mackovic.eus.R;

import io.reactivex.Single;
import io.reactivex.SingleObserver;

public class AuthenticationService {

    private static final String CURRENT_USER_ERROR = "Cannot find current user";

    private final GoogleSignInClient googleSignInClient;
    private final FirebaseAuth firebaseAuth;

    AuthenticationService(Context context) {
        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(context.getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        googleSignInClient = GoogleSignIn.getClient(context, googleSignInOptions);
        firebaseAuth = FirebaseAuth.getInstance();
    }

    public Single<FirebaseUser> signInWithCredential(AuthCredential authCredential) {
        return new Single<FirebaseUser>() {
            @Override
            protected void subscribeActual(SingleObserver<? super FirebaseUser> observer) {
                firebaseAuth.signInWithCredential(authCredential)
                        .addOnCompleteListener(result -> observer.onSuccess(firebaseAuth.getCurrentUser()))
                        .addOnFailureListener(observer::onError);
            }
        };
    }

    public Intent getSignInIntent() {
        return googleSignInClient.getSignInIntent();
    }

    public FirebaseUser getCurrentUser() {
        return firebaseAuth.getCurrentUser();
    }

    public void signOut() {
        firebaseAuth.signOut();
    }

    public Single<FirebaseUser> deleteData() {

        if (firebaseAuth.getCurrentUser() == null) {
            return Single.error(new RuntimeException(CURRENT_USER_ERROR));
        }

        return new Single<FirebaseUser>() {
            @Override
            protected void subscribeActual(SingleObserver<? super FirebaseUser> observer) {
                firebaseAuth.getCurrentUser().delete()
                        .addOnCompleteListener(task -> observer.onSuccess(firebaseAuth.getCurrentUser()))
                        .addOnFailureListener(observer::onError);
            }
        };
    }
}