package com.example.capstoneprojectapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;


public class AddItemToDatabase extends AppCompatActivity {

    private static final String TAG="AddItemToDatabase";

    private Button maddItemButton;
    private EditText mfoodNameField;
    private EditText mshop;
    private EditText mlocationField;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mReference;
    private DatabaseReference keyDatabaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item_to_database);
        maddItemButton=findViewById(R.id.addItemButton);
        mfoodNameField=findViewById(R.id.foodNameField);
        mshop=findViewById(R.id.shopField);
        mlocationField=findViewById(R.id.locationField);

        mAuth=FirebaseAuth.getInstance();
        mFirebaseDatabase=FirebaseDatabase.getInstance();
        mReference=mFirebaseDatabase.getReference();
        keyDatabaseReference=mReference.push();

        mAuthListener=new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user =firebaseAuth.getCurrentUser();
                if(user!=null){
                    Log.d(TAG,"onAuthState: signed_in: " + user.getEmail());
                    toastMessage("Successfully signed in with: " + user.getUid());
                }else{
                    Log.d(TAG,"onAuthStateChanged: signed_out:");
                }

            }
        };

        mReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Object value=dataSnapshot.getValue();
                Log.d(TAG,"value is: " +value);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.w(TAG,"failed to read value.", databaseError.toException());
            }
        });

        maddItemButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG,"onclick: attempting to add object to database");
                String newFood =mfoodNameField.getText().toString();
                String newShop=mshop.getText().toString();
                int newcoin=0;
                String key=mReference.child("Another foo").push().getKey();
                String location=mlocationField.getText().toString();


                if(!newFood.equals("")){

                    FirebaseUser user=mAuth.getCurrentUser();
                    Map<String,foodInfo> addFoodNode=new HashMap<>();
                    addFoodNode.put(location,new foodInfo(newFood,newShop,newcoin));




                    mReference.child("userid").child("Food").child("Another foo").child(key).setValue(addFoodNode);
                    mfoodNameField.setText("");
                }
            }
        });
    }


    @Override
    public void onStart(){
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop(){
        super.onStop();
        if(mAuthListener!=null){
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }
    private void toastMessage(String message){
        Toast.makeText(AddItemToDatabase.this,message,Toast.LENGTH_SHORT).show();
    }
}


