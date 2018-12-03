package com.group2.pacepal;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener; //may need to update other files
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity  implements View.OnClickListener, GoogleApiClient.OnConnectionFailedListener {

    private SignInButton signIn;
    private TextView Name, Email;
    private GoogleApiClient mGoogleApiClient;
    private static final int RC_SIGN_IN = 111;
    private GoogleSignInClient mGoogleSignInClient;
    private static final String TAG = "MainActivity";
    private FirebaseAuth mAuth;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    Map<String, Object> data = new HashMap<>();

    //GoogleSignInOptions signInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
    // Build a GoogleSignInClient with the options specified by gso.
    //mGoogleSignInClient = GoogleSignIn.getClient(this, signInOptions);
    //GoogleApiClient = new GoogleApiClient.Builder(this).enableAutoManage(this, this).addApi(Auth.GOOGLE_SIGN_IN_API, signInOptions);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        setContentView(R.layout.activity_main);
        findViewById(R.id.sign_in_button).setOnClickListener(this);
        findViewById(R.id.continue_button).setOnClickListener(this);
        findViewById(R.id.sign_out).setOnClickListener(this);
        findViewById(R.id.submitButton).setOnClickListener(this);
        findViewById(R.id.continue_button).setVisibility(View.INVISIBLE);
        findViewById(R.id.sign_out).setVisibility(View.VISIBLE);
        findViewById(R.id.fnameField).setVisibility(View.INVISIBLE);
        findViewById(R.id.lnameField).setVisibility(View.INVISIBLE);
        findViewById(R.id.unameField).setVisibility(View.INVISIBLE);
        findViewById(R.id.submitButton).setVisibility(View.INVISIBLE);


        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.server_client_id))
                .requestEmail()
                .build();

        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
    }

    /*
        private void refreshIdToken() {
            // Attempt to silently refresh the GoogleSignInAccount. If the GoogleSignInAccount
            // already has a valid token this method may complete immediately.
            //
            // If the user has not previously signed in on this device or the sign-in has expired,
            // this asynchronous branch will attempt to sign in the user silently and get a valid
            // ID token. Cross-device single sign on will occur in this branch.
            mGoogleSignInClient.silentSignIn()
                    .addOnCompleteListener(this, new OnCompleteListener<GoogleSignInAccount>() {
                        @Override
                        public void onComplete(@NonNull Task<GoogleSignInAccount> task) {
                            handleSignInResult(task);
                        }
                    });
        }
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sign_in_button:
                signIn();
                break;
            case R.id.continue_button:
                toMenu();
                break;
            case R.id.sign_out:
                signOut();
                break;
            case R.id.submitButton:
                toMenuSubmit();
                break;

        }
    }

    public void toMenu() {
        startActivity(new Intent(MainActivity.this, Main2Activity.class));
    }

    public void toMenuSubmit() {
        EditText fName = findViewById(R.id.fnameField);
        EditText lName = findViewById(R.id.lnameField);
        EditText uName = findViewById(R.id.unameField);
        final boolean[] usernameExists = new boolean[1];

        if((fName.getText().toString() != "") && (lName.getText().toString() != "") && (uName.getText().toString() != ""))
        {
            String userid = mAuth.getInstance().getCurrentUser().getUid();
            Toast.makeText(this,userid,Toast.LENGTH_SHORT);
            Map<String, Object> data = new HashMap<>();

            data.put("first", fName.getText().toString());
            data.put("last", lName.getText().toString());
            data.put("username", uName.getText().toString());
            data.put("miles", 0);
            data.put("friends",0);
            data.put("challenges",0);
            data.put("profilepic", "url");

            db.collection("users")
                    .whereEqualTo("username", uName.getText().toString())
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    Log.d(TAG, document.getId() + " => " + document.getData());
                                    usernameExists[0] = true;
                                }
                            } else {
                                Log.d(TAG, "Error getting documents: ", task.getException());
                            }
                        }
                    });
            if(usernameExists[0]){
                Toast.makeText(this,"Username taken!", Toast.LENGTH_SHORT);
            }
            else
            {
                db.collection("users").document(userid).set(data);
                toMenu();
            }



        }



    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    private void signOut() {
        mGoogleSignInClient.signOut()
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        updateUI(null);
                    }
                });
    }
/*
    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            String idToken = account.getIdToken();

            // Signed in successfully, show authenticated UI.
            updateUI(account);
        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w(TAG, "signInResult:failed code=" + e.getStatusCode());
            updateUI(null);
        }
    } */

    private void updateUI(FirebaseUser account) {
        if (account != null) {
            //user has account

            findViewById(R.id.sign_in_button).setVisibility(View.INVISIBLE);
            String userid = FirebaseAuth.getInstance().getCurrentUser().getUid();

            DocumentReference docRef = db.collection("users").document(userid);
            docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                            findViewById(R.id.continue_button).setVisibility(View.VISIBLE);
                            findViewById(R.id.sign_out).setVisibility(View.VISIBLE);
                        } else {
                            Log.d(TAG, "No such document");
                            findViewById(R.id.fnameField).setVisibility(View.VISIBLE);
                            findViewById(R.id.lnameField).setVisibility(View.VISIBLE);
                            findViewById(R.id.unameField).setVisibility(View.VISIBLE);
                            findViewById(R.id.submitButton).setVisibility(View.VISIBLE);



                        }
                    } else {
                        Log.d(TAG, "get failed with ", task.getException());
                    }
                }
            });

        } else {
            //user does not have an account yet
            //display google sign in button
            findViewById(R.id.sign_in_button).setVisibility(View.VISIBLE);
            findViewById(R.id.sign_out).setVisibility(View.INVISIBLE);
        }
    }

    /*
        @Override
        public void onActivityResult (int requestCode, int resultCode, Intent data) {
            super.onActivityResult(requestCode, resultCode,data);
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            if(requestCode == RC_SIGN_IN) {
                Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
                handleSignInResult(task);

            }
        }
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w(TAG, "Google sign in failed", e);
                // ...
            }
        }
    }


    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Snackbar.make(findViewById(R.id.continue_button), "Authentication Failed.", Snackbar.LENGTH_SHORT).show();
                            updateUI(null);
                        }

                        // ...
                    }
                });
    }

}
