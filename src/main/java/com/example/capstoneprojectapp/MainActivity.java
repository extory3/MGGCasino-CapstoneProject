package com.example.capstoneprojectapp;

import androidx.annotation.NonNull;
import androidx.annotation.VisibleForTesting;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    private static final String TAG = "MainActivity";

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mReference;

    @VisibleForTesting
    public ProgressDialog mProgressDialog;

    public EditText memailField;
    public EditText mpasswordField;
    public TextView mStatus;

    //checking the user state:



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //email & password fields

        memailField= findViewById(R.id.locationField);
        mpasswordField=findViewById(R.id.RpasswordField);
        mStatus=findViewById(R.id.textStatus);
        //buttons
        findViewById(R.id.SignInButton).setOnClickListener(this);
        findViewById(R.id.signUpButton).setOnClickListener(this);
        findViewById(R.id.SignOutBtn).setOnClickListener(this);
        mAuth = FirebaseAuth.getInstance();

        //auth state:

        mAuthListener= new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user=firebaseAuth.getCurrentUser();
                if(user!=null){
                    Log.d(TAG,"onAuthStateChanged: signed_in " +user.getUid());
                    Toast.makeText(MainActivity.this, "Successfully signed in with : " +user.getEmail(), Toast.LENGTH_SHORT).show();
                }else{
                    //user signed out
                    Log.d(TAG,"onAuthStateChanged: signed_out ");
                    Toast.makeText(MainActivity.this, "Successfully signed out", Toast.LENGTH_SHORT).show();
                }
            }
        };

    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser user = mAuth.getCurrentUser();
        mAuth.addAuthStateListener(mAuthListener);
        status(user);
    }






    public boolean validateForm(){
        boolean valid=true;

        String email=memailField.getText().toString();
        if(TextUtils.isEmpty(email)){
            memailField.setError("Required.");
            valid=false;
        }else{
            memailField.setError(null);
        }


        String password=mpasswordField.getText().toString();
        if(TextUtils.isEmpty(password)){
            mpasswordField.setError("required.");
            valid=false;
        }else{
            mpasswordField.setError(null);
        }
        return valid;
    }




    private void signIn(String email,String password){
        Log.d(TAG,"sign In:" +email);
        if(!validateForm()){
            return;
        }

        showProgressDialog();
        mAuth.signInWithEmailAndPassword(email,password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Log.d(TAG,"SignInWithEmail:success");
                            FirebaseUser user=mAuth.getCurrentUser();
                        }else{
                            Log.w(TAG,"SignInEmail:failure",task.getException());
                            Toast.makeText(MainActivity.this,"Authentication failed.",Toast.LENGTH_SHORT).show();
                        }

                        if(!task.isSuccessful()){
                            Toast.makeText(MainActivity.this, "Failed!", Toast.LENGTH_LONG).show();
                        }
                        hideProgressDialog();

                    }
                });
        //signin through login
        //1) get the value from database
    }

    //baseactivity.java

    public void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setMessage(getString(R.string.loading));
            mProgressDialog.setIndeterminate(true);
        }
        mProgressDialog.show();
    }

    public void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }


    @Override
    public void onStop() {
        super.onStop();
        hideProgressDialog();
    }
    public void SignOut(){
        mAuth.signOut();
    }

    public void status(FirebaseUser user){
        if(user!=null){
            mStatus.setText("SIGNED IN");
        }else{
            mStatus.setText("SIGNED OUT");
        }
    }

    @Override
    public void onClick(View view) {
        int i=view.getId();

            if (i == R.id.SignInButton) {
                signIn(memailField.getText().toString(), mpasswordField.getText().toString());
                Intent goToAddItemDatabase=new Intent(MainActivity.this,AddItemToDatabase.class);
                startActivity(goToAddItemDatabase);
            }
            else if (i == R.id.SignOutBtn) {
                SignOut();
                Toast.makeText(this, "Signout button was clicked", Toast.LENGTH_SHORT).show();
            }
            else if(i==R.id.signUpButton){ //if SignUp button is pressed go to RegistrationActivity.java
                Intent goToRegistrationActivity=new Intent(this, RegistrationActivity.class);
                startActivity(goToRegistrationActivity);
            }
        }
    }

