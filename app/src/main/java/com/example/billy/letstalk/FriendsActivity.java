package com.example.billy.letstalk;

import android.app.LauncherActivity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

import static android.R.id.message;

public class FriendsActivity extends AppCompatActivity {

    private final String CHILD_USERS = "users";

    private DatabaseReference mFirebaseDatabaseReference;
    private FirebaseAuth mAuth;

    private ArrayList<String> mFriendList;
    private ArrayList<String> mFriendKeyList;

    private Button mLogoutButton;
    private Button mAddButton;
    private ListView mFriendListView;
    private ArrayAdapter<String> mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends);

        setFirebase();
        setFriendListView();
        setLogoutButton();
        setAddButton();
        setChildEvent();
    }


    private void setFirebase() {
        mAuth = FirebaseAuth.getInstance();
        mFirebaseDatabaseReference = FirebaseDatabase.getInstance().getReference();
    }

    private void setFriendListView() {
        mFriendListView = (ListView) findViewById(R.id.friendListView);
        mFriendList = new ArrayList<>();
        mFriendKeyList = new ArrayList<>();

        mAdapter = new ArrayAdapter<String>(this, R.layout.list_item, mFriendList);
        mFriendListView.setAdapter(mAdapter);
        mFriendListView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(FriendsActivity.this, MainActivity.class);
                intent.putExtra("users", mFriendList.get(position));
                intent.putExtra("key", mFriendKeyList.get(position));
                intent.putExtra("my_key", mAuth.getCurrentUser().getUid());
                startActivity(intent);
            }
        });
    }

    private void setLogoutButton() {
        mLogoutButton = (Button) findViewById(R.id.logoutButton);
        mLogoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();
                Intent intent = new Intent(FriendsActivity.this, LaunchActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
    }

    private void setAddButton() {
        mAddButton = (Button) findViewById(R.id.addButton);
        mAddButton.setOnClickListener( new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FriendsActivity.this, AddActivity.class);
                startActivity(intent);
            }
        });
    }

    private void setChildEvent() {
        ChildEventListener childEventListener= new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Users users  = dataSnapshot.getValue(Users.class);
                mFriendList.add(users.getEmail());
                mFriendKeyList.add(dataSnapshot.getKey());
//                mAdapter.add(users.getEmail());
                mAdapter.notifyDataSetChanged();
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
        };
        mFirebaseDatabaseReference.child(CHILD_USERS).addChildEventListener(childEventListener);
    }
}
