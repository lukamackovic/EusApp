package com.luka.mackovic.eus.repository.network;

import android.net.Uri;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.Objects;
import java.util.UUID;

import io.reactivex.Single;
import io.reactivex.SingleObserver;

public class FirebaseStorageImageRepository implements ImageRepository{

    private final static String COLLECTION_EVENT = "event";

    @Override
    public Single<String> saveImage(Uri uri) {
        StorageReference storageReference = FirebaseStorage.getInstance().getReference(COLLECTION_EVENT).child(UUID.randomUUID().toString());
        UploadTask uploadTask = storageReference.putFile(uri);

        return new Single<String>() {
            @Override
            protected void subscribeActual(SingleObserver<? super String> observer) {
                uploadTask.continueWithTask(task -> {
                    if (!task.isSuccessful()) {
                        throw Objects.requireNonNull(task.getException());
                    }
                    return storageReference.getDownloadUrl();
                }).addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        observer.onSuccess(Objects.requireNonNull(task.getResult()).toString());
                    }
                }).addOnFailureListener(observer::onError);
            }
        };
    }

    @Override
    public void deleteImage(String imageStorageName) {
        StorageReference storageReference = FirebaseStorage.getInstance().getReferenceFromUrl(imageStorageName);
        storageReference.delete();
    }
}