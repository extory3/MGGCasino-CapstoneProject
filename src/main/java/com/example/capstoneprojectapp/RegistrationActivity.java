package com.example.capstoneprojectapp;

import androidx.annotation.NonNull;
import androidx.annotation.VisibleForTesting;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class RegistrationActivity extends MainActivity implements View.OnClickListener{

    private static final String TAG="REGISTRATION ACTIVITY";


    //user information fields
    private EditText mloginField,mnameField;
    private Button mCreateAccountBtn;
    private EditText memailField;
    private EditText mpasswordField;
    private Button mverifyEmailButton;
    private TextView memailverTextView;
    private Button madduserbutton;
    //declare all the database stuff
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mReference;

    private String UserID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration2);

        mloginField=findViewById(R.id.loginField);
        mnameField=findViewById(R.id.nameField);
        memailField=findViewById(R.id.emailField);
        mpasswordField=findViewById(R.id.passwordField);
        mCreateAccountBtn=findViewById(R.id.createAccountBtn);
        madduserbutton=findViewById(R.id.addUserButton);
        mverifyEmailButton=findViewById(R.id.verifyEmailButton);
        memailverTextView=findViewById(R.id.emailverTextView);
        //declare the database reference object to access the database
        //unless you are signed in, this will not be useable
        mAuth=FirebaseAuth.getInstance();
        mFirebaseDatabase=FirebaseDatabase.getInstance();
        mReference=mFirebaseDatabase.getReference();
        FirebaseUser user=mAuth.getCurrentUser(); //the user is not signed in I get error
        //UserID=user.getUid();

        mAuthListener=new FirebaseAuth.AuthStateListener(){
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user=firebaseAuth.getCurrentUser();
                if(user!=null){
                    //the state: user is logged in
                    Log.d(TAG,"onAuthStateChanged: sign_in" + user.getUid());
                    Toast.makeText(RegistrationActivity.this, "SIGNED IN", Toast.LENGTH_SHORT).show();
                }else{
                    //user is logged out
                    Log.d(TAG,"onAuthStateChanged: sign_out ");
                    Toast.makeText(RegistrationActivity.this, "SIGNED OUT", Toast.LENGTH_SHORT).show();
                }
            }
        };

        //add value to the database
//        mReference.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                Log.d(TAG,"onDataChange: Added to database: \n" + dataSnapshot.getValue());
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//                Log.d(TAG,"OnCancelled: failed to read value: \n", databaseError.toException());
//            }
//        });





//        mCreateAccountBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Log.d(TAG,"createAccountBtn is pressed");
//            String email=memailField.getText().toString();
//            String password=mpasswordField.getText().toString();
//
//            //show message that the info is being sent to database
//            Log.d(TAG,"Submitting this to database: \n" +"\n" +"email: "+ email);
//
//            if(!email.equals("") && !password.equals("")){
//                createAccount(email,password);
//
//            }else{
//                mloginField.setError("required");
//                mnameField.setError("required");
//                memailField.setError("required");
//                Toast.makeText(RegistrationActivity.this, "FillOUT ALL the fields", Toast.LENGTH_SHORT).show();
//            }
//        }
//        });

        mReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Object value=dataSnapshot.getValue();
                Log.d(TAG,"value is: " + value);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.w(TAG,"failed to read the value",databaseError.toException());
            }
        });
mCreateAccountBtn.setOnClickListener(this);
madduserbutton.setOnClickListener(this);
}

    @Override
    public void onStart(){
        super.onStart();
        FirebaseUser user = mAuth.getCurrentUser();
        mAuth.addAuthStateListener(mAuthListener);
    }

    //baseactivity.java



//    private void createAccount(){
//        String email=memailField.getText().toString();
//        String password=mpasswordField.getText().toString();
//        if(!validateForm()){
//            return;
//        }
//
//        //showProgressDialog();
//        showProgressDialog();
//        Log.d(TAG,"Create Account: " + email);
//
//        mAuth.createUserWithEmailAndPassword(email,password)
//                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
//                     @Override
//                    public void onComplete(@NonNull Task<AuthResult> task) {
//                        hideProgressDialog();
//                        if(task.isSuccessful()){
//                            Log.d(TAG,"createuserwithEmail:success!");
//                            FirebaseUser user=mAuth.getCurrentUser();
//                            //if the user successfully created and send the email verification
//                            //sendEmailVerification();
//                            Log.d(TAG,"the user is signed in now");
//                        }else{
//                            Log.w(TAG,"Createuserwithemail: failure", task.getException());
//                            Toast.makeText(RegistrationActivity.this, "Authentication failed", Toast.LENGTH_SHORT).show();
//                        }
//                    }
//                });
//    }

//    private void onAuthSuccess(FirebaseUser user){
//        String password=mpasswordField.getText().toString();
//        String login=mloginField.getText().toString();
//        String name=mnameField.getText().toString();
//        int coin=0;
//        writeNewUser(user.getUid(),name,login,user.getEmail(),password,coin);
//    }
//
//    private void writeNewUser(String UserId,String name, String login, String email,String password, int coin){
//        UserInfo newUser=new UserInfo(name, login, email,password,coin);
//
//        mReference.child("users").child(UserId).setValue(newUser);
//    }

    private void sendEmailVerification(){
        //send verification email

        final FirebaseUser user=mAuth.getCurrentUser();
        user.sendEmailVerification()
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        if(task.isSuccessful()){
                            Toast.makeText(RegistrationActivity.this,"Verification email sent to " + user.getEmail(),Toast.LENGTH_LONG).show();
                        }else{
                            Log.e(TAG,"sendEmailverification", task.getException());
                            Toast.makeText(RegistrationActivity.this,"Failed to send verification email.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }



    @Override
    public void onStop(){
        super.onStop();
        if(mAuthListener!=null){
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }
    @Override
    public void onClick(View view) {
        int i=view.getId();

       if (i == R.id.SignOutBtn) {
            SignOut();
            Toast.makeText(this, "Signout button was clicked", Toast.LENGTH_SHORT).show();
        }
       else if(i==R.id.createAccountBtn){

           final String email=memailField.getText().toString();
           final String password=mpasswordField.getText().toString();
           final String login=mloginField.getText().toString();
           final String name=mnameField.getText().toString();
           final int coin=0;
           mAuth.createUserWithEmailAndPassword(email,password)
                   .addOnCompleteListener(RegistrationActivity.this, new OnCompleteListener<AuthResult>() {
                       @Override
                       public void onComplete(@NonNull Task<AuthResult> task) {
                           if(task.isSuccessful()){
                               Log.d(TAG,"createuserwithemail:success");
                               FirebaseUser user=mAuth.getCurrentUser();
                               String UserID=user.getUid();

                               Map<String, UserInfo> addUser=new HashMap<>();
                               addUser.put(login,new UserInfo(name,login,email,password,coin));
                               mReference.child("users").child(UserID).setValue(addUser);

                           }
                           else{
                               Log.w(TAG,"createuserwithemail:failure",task.getException());
                               Toast.makeText(RegistrationActivity.this, "Authenticationfailed", Toast.LENGTH_SHORT).show();
                           }

                       }
                   });
           memailverTextView.setVisibility(View.VISIBLE);

       }
       else if(i==R.id.addUserButton){
           // add a new user to check if the db is working
           String login=mloginField.getText().toString();
           String name=mnameField.getText().toString();
           int coin=0;
           FirebaseUser user=mAuth.getCurrentUser();
           String UserID=user.getUid();

           Map<String,UserInfo> newUser=new HashMap<>();
           newUser.put(login,new UserInfo(name,login,coin));

           mReference.child("users").child(UserID).setValue(newUser);

       }
    }

}
