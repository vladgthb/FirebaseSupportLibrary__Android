package com.vladimirdanielyan.firebasesupportlibrary.activities;

import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.vladimirdanielyan.firebasesupportlibrary.models.BaseUser;

/**
 * Created by vlad on 6/28/17.
 * The Authentication Class Is Advance Class Of The Base Authentication
 */

public abstract class Authentication extends BaseAuthentication {

    private static final String TAG = Authentication.class.getSimpleName();

    private static final String ALL_USERS_KEY = "all_users";

    DatabaseReference baseUserDatabaseReference;

    private ValueEventListener userValueEventListener;

    // User Basic Info
    private BaseUser baseUser;

    @Override
    public void onStop() {
        super.onStop();
        removeAuthListener();
    }

    private void removeAuthListener() {
        if (baseUserDatabaseReference != null) {
            if (userValueEventListener != null) {
                baseUserDatabaseReference.removeEventListener(userValueEventListener);
            }
        }
    }

    private void initializeUserDataListener(FirebaseUser user) {
        // [START auth_state_listener]
        userValueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                baseUser = dataSnapshot.getValue(BaseUser.class);
                if (baseUser != null) {
                    Log.d(TAG, "User Signed In With uID\t" + baseUser.getUserName());
                    reportSignInStatus(baseUser.getUserType(), baseUser.getUserName());
                } else {
                    reportFailedAuth("Cannot Receive User Data");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        // [END auth_state_listener]

        baseUserDatabaseReference = getRoot().child("users").child(ALL_USERS_KEY).child(user.getUid());
    }

    private void setupUserDataListener(FirebaseUser user) {

        initializeUserDataListener(user);

        if (baseUserDatabaseReference != null) {
            baseUserDatabaseReference.addValueEventListener(userValueEventListener);
        }
    }

    @SuppressWarnings("unused")
    public void signIn(final String email, final String password) {
        Log.d(TAG, "signIn:" + email);

        showProgressDialog();

        // [START sign_in_with_email]
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "signInWithEmail:onComplete:" + task.isSuccessful());

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "signInWithEmail:failed", task.getException());
                            String message = "Something goes wrong";
                            //noinspection ThrowableResultOfMethodCallIgnored
                            if (task.getException() != null) {
                                //noinspection ThrowableResultOfMethodCallIgnored
                                message = task.getException().getMessage();
                            }
                            hideProgressDialog();
                            reportFailedAuth(message);
                        } else {
                            hideProgressDialog();
                            Log.d(TAG, "signedIn Successfully:" + email);
                            if (mAuth.getCurrentUser() != null) {
                                reportSignInStatus("" ,mAuth.getCurrentUser().getUid());
                            }
                        }
                    }
                })
                .addOnFailureListener(this, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG, "Cannot Access " + e.getMessage());
                    }
                });
        // [END sign_in_with_email]
    }

    private void reportFailedAuth(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    public void updateUser(FirebaseUser user) {
        setupUserDataListener(user);
    }

    // Need To Implement Following Methods
    public abstract void reportSignInStatus(String userType, String userId);
}
