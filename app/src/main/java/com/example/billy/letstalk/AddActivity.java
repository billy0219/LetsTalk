package com.example.billy.letstalk;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class AddActivity extends AppCompatActivity {

    private DatabaseReference mFirebaseDatabaseReference;

    private EditText mEmailSearchEditText;
    private Button mSearchButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        mFirebaseDatabaseReference = FirebaseDatabase.getInstance().getReference();

        mEmailSearchEditText = (EditText) findViewById(R.id.emailSearchEditText);
        mSearchButton = (Button) findViewById(R.id.searchButton);

        mSearchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mFirebaseDatabaseReference.orderByChild("users")
                        .equalTo(mEmailSearchEditText.getText().toString())
                        .addChildEventListener(new ChildEventListener() {
                            @Override
                            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                            }

                            @Override
                            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                            }

                            @Override
                            public void onChildRemoved(DataSnapshot dataSnapshot) {

                            }

                            @Override
                            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });

            }
        });
    }
}
