package com.vladimirdanielyan.firebasesupportlibrary.activities;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by vlad on 2/24/2017.
 * The BaseAuthentication Class
 */

public abstract class BaseAuthentication extends FullScreenActivity {

    final private static String TAG = BaseAuthentication.class.getSimpleName();

    // [START declare_auth]
    public FirebaseAuth mAuth;
    // [END declare_auth]

    // [START declare_auth_listener]
    private FirebaseAuth.AuthStateListener mAuthListener;
    // [END declare_auth_listener]

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initializeAuth();
    }

    private void initializeAuth() {
        // [START initialize_auth]
        mAuth = FirebaseAuth.getInstance();
        // [END initialize_auth]

        // [START auth_state_listener]
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.d(TAG, "onAuthStateChanged:signed_in: " + user.getUid());
                    userSignedIn(user);
                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
            }
        };
        // [END auth_state_listener]
    }

    /**
     * The Method Is Updating User Info, When User Authentication Updating
     */
    private void setupAuthListener() {
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        removeAuthListener();
    }

    @Override
    public void onStart() {
        super.onStart();
        setupAuthListener();
    }

    private void removeAuthListener() {
        if (mAuth != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    @SuppressWarnings("unused")
    public void signOut() {
        mAuth.signOut();
        reportSignOutStatus();

    }

    private void userSignedIn(FirebaseUser user) {
        updateUser(user);
    }

    public abstract void reportSignOutStatus();
    public abstract void updateUser(FirebaseUser user);

    private static final FirebaseDatabase database = FirebaseDatabase.getInstance();
    private static final DatabaseReference root = database.getReference();

    @SuppressWarnings("unused")
    public DatabaseReference getRoot() {
        return root;
    }

    @SuppressWarnings("unused")
    public String getUserId() {
        if (mAuth.getCurrentUser() != null) {
            return mAuth.getCurrentUser().getUid();
        } else {
            return "NONE";
        }
    }
}
