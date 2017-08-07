package com.example.billy.letstalk;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private final String CHILD_MESSAGES = "messages";

    private ListView mListView;
    private EditText mMsgText;
    private Button mSendButton;

    private String mEmail;
    private String mKey;
    private String mMyKey;

    private ArrayAdapter<String> mAdapter;

    private DatabaseReference mFirebaseDatabaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getDataFromIntent();
        setFirebase();
        connectView();
        setListView();
        setSendButton();
        setChildEvent();
    }

    private void getDataFromIntent() {
        Intent intent = getIntent();
        mEmail = intent.getStringExtra("email");
        mKey = intent.getStringExtra("key");
        mMyKey = intent.getStringExtra("my_key");
    }

    private void setFirebase() {
        mFirebaseDatabaseReference = FirebaseDatabase.getInstance().getReference();
    }

    private void connectView() {
        mListView = (ListView) findViewById(R.id.listView);
        mMsgText = (EditText) findViewById(R.id.msgText);
        mSendButton = (Button) findViewById(R.id.sendButton);
    }

    private void setListView() {
        ArrayList<String> samples = new ArrayList<>();
        mAdapter = new ArrayAdapter<String>(this, R.layout.list_item, samples);
        mListView.setAdapter(mAdapter);
    }

    private void setSendButton() {
        mSendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Message message = new Message(
                        mMyKey,
                        mKey,
                        mMsgText.getText().toString());
                mFirebaseDatabaseReference.child(CHILD_MESSAGES).push().setValue(message);
                mMsgText.setText("");
            }
        });
    }

    private void setChildEvent() {
        ChildEventListener childEventListener= new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Message message = dataSnapshot.getValue(Message.class);
                boolean isSent = message.getSender().equals(mMyKey) && message.getReceiver().equals(mKey);
                boolean isReceived = message.getSender().equals(mKey) && message.getReceiver().equals(mMyKey);
                if (isSent || isReceived ) {
                    if (isSent) {
                        mAdapter.add(message.getText());
                    } else {
                        mAdapter.add(mEmail + " : " + message.getText());
                    }
                }
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
        mFirebaseDatabaseReference.child(CHILD_MESSAGES).addChildEventListener(childEventListener);
    }
}

