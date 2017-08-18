package com.vladimirdanielyan.firebasesupportlibrary.activities;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.List;

/**
 * Created by vlad on 8/18/17.
 * The Storage Upload Activity
 */

public abstract class FirebaseStorageActivity extends BaseAuthentication {

//    Internal TAG Variable
    private static final String TAG = FirebaseStorageActivity.class.getSimpleName();

//    Firebase Storage and Storage Reference Type Of Variables
    private static final FirebaseStorage storage = FirebaseStorage.getInstance();
    private static final StorageReference storageReference = storage.getReference();

//    The Upload Task Variable
    private UploadTask uploadTask;


    /**
     * The Method Is Uploading File To The Dedicated Path
     * @param imageLocalPath - imageLocalPath Uri Format
     * @param firebaseStorageChildList - firebase storage path
     */

    @SuppressWarnings("unused")
    public void uploadFile(Uri imageLocalPath,
                                             List<String> firebaseStorageChildList) {
        if (imageLocalPath != null) {
            if (firebaseStorageChildList != null && firebaseStorageChildList.size() > 0) {
                StorageReference filePath = storageReference;
                for (String firebaseStorageChild : firebaseStorageChildList) {
                    filePath = filePath.child(firebaseStorageChild);
                }

                uploadTask = filePath.putFile(imageLocalPath);
                setUploadTaskListener();
            }
        }
    }


    /**
     * The Method Is Setting Up Upload Task Listener
     * It Will Execute reportSuccessfulUpload or reportUnsuccessfulUpload on completed
     */

    private void setUploadTaskListener() {
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful uploads
                internalReportUnsuccessfulUpload(exception.getMessage());
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
                Uri downloadUrl = taskSnapshot.getDownloadUrl();

                if (downloadUrl != null) {
                    internalReportSuccessfulUpload(downloadUrl.toString());
                }
            }
        });
    }


    /**
     * The Method Is Logging The Message If Unsuccessful Upload
     * @param reportMessage - the message to be reported
     */

    private void internalReportUnsuccessfulUpload(String reportMessage) {
        Log.d(TAG, reportMessage);
        reportUnsuccessfulUpload(reportMessage);
    }

    /**
     * The Method Is Logging The Message If Successful Upload
     * @param imageUrl - returning image downloadable path of the storage
     */

    private void internalReportSuccessfulUpload(String imageUrl) {
        Log.d(TAG, "Successfully uploaded file " + imageUrl);
        reportSuccessfulUpload(imageUrl);
    }

    /**
     * The Abstract Method That Need To Be Implemented In Activity
     * The Method Is Executed When Uploading File Task Completed Successfully
     * @param imageUrl - the imageUrl Path That will be reported
     */

    public abstract void reportSuccessfulUpload(String imageUrl);


    /**
     * The Abstract Method That Need To Be Implemented In Activity
     * The Method Is Executed When Uploading File Task Completed Successfully
     * @param reportMessage - report message
     */
    public abstract void reportUnsuccessfulUpload(String reportMessage);

}
