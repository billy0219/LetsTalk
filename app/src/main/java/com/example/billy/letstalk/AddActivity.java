package com.example.billy.letstalk;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

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
                        .addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if (dataSnapshot.exists()) {
                                    for (DataSnapshot child : dataSnapshot.getChildren()) {
                                        Intent intent = new Intent(AddActivity.this, FriendsActivity.class);
                                        intent.putExtra("users", child.getKey());
                                        break;
                                    }
                                } else {
                                    Toast.makeText(AddActivity.this, "User not found", Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });

            }
        });
    }
}
