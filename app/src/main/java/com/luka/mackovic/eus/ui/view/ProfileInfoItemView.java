package com.luka.mackovic.eus.ui.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.textview.MaterialTextView;
import com.google.firebase.auth.FirebaseUser;
import com.luka.mackovic.eus.R;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;

@SuppressLint("ViewConstructor")
@EViewGroup(R.layout.item_view_profile_info)
public class ProfileInfoItemView extends ConstraintLayout {

    @ViewById
    ImageView image;
    @ViewById
    MaterialTextView name;
    @ViewById
    MaterialTextView email;

    private final ProfileInfoListener listener;
    private final FirebaseUser firebaseUser;

    public ProfileInfoItemView(Context context, FirebaseUser user, ProfileInfoListener listener) {
        super(context);
        this.listener = listener;
        this.firebaseUser = user;
    }

    @SuppressLint("CheckResult")
    @AfterViews
    public void init() {
        RequestOptions placeholderOption = new RequestOptions();
        placeholderOption.placeholder(R.drawable.ic_profile);
        Glide.with(getContext()).setDefaultRequestOptions(placeholderOption).load(firebaseUser.getPhotoUrl()).into(image);
        name.setText(firebaseUser.getDisplayName());
        email.setText(firebaseUser.getEmail());
    }

    @Click
    void signOut() {
        listener.authAction(AuthAction.SIGN_OUT);
    }

    @Click
    void deleteData() {
        listener.authAction(AuthAction.DELETE_DATA);
    }

    public interface ProfileInfoListener {
        void authAction(AuthAction action);
    }

    public enum AuthAction {
        SIGN_OUT,
        DELETE_DATA
    }
}