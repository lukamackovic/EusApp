package com.luka.mackovic.eus.repository.network;

import android.net.Uri;

import io.reactivex.Single;

public interface ImageRepository {

    Single<String> saveImage(Uri uri);

    void deleteImage(String uriImage);
}