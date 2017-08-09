package com.example.billy.letstalk;

import android.content.Intent;
import android.support.annotation.MainThread;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AuthActivity extends AppCompatActivity implements View.OnClickListener {

    private final String CHILD_USERS = "users";
    private DatabaseReference mFirebaseDatabaseReference;
    private FirebaseAuth mAuth;

    private EditText mEmailEditText;
    private EditText mPasswordEditText;
    private Button mConfirmButton;

    private String mPageType;
    private boolean mIsSignUp;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);

        mEmailEditText = (EditText) findViewById(R.id.emailEditText);
        mPasswordEditText = (EditText) findViewById(R.id.passwordEditText);
        mConfirmButton = (Button) findViewById(R.id.confirmButton);
        mFirebaseDatabaseReference = FirebaseDatabase.getInstance().getReference();

        mAuth = FirebaseAuth.getInstance();

        Intent intent = getIntent();
        boolean isSignUp = intent.getBooleanExtra("isSignUp", false);
//        if (isSignUp) {
//            mPageType = "Sign Up";
//        } else {
//            mPageType = "Log In";
//        }
        mIsSignUp = isSignUp;
        mPageType = isSignUp ? "Sign Up" : "Log In";

        mConfirmButton.setText(mPageType);
        mConfirmButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (mEmailEditText.getText().toString() != ""
                && mPasswordEditText.getText().toString() != "") {

            //하단 코드는 따로 수정하지 않았는데, 데이터베이스에 입력이 안됩니다.
            if ( mIsSignUp ) {

                mAuth.createUserWithEmailAndPassword(
                        mEmailEditText.getText().toString(),
                        mPasswordEditText.getText().toString()
                ).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                                    Users users = new Users(mEmailEditText.getText().toString());
                                    mFirebaseDatabaseReference.child(CHILD_USERS).push().setValue(users);

                            startActivity(new Intent(AuthActivity.this, FriendsActivity.class));
                        }
                    }
                });
            } else {
                mAuth.signInWithEmailAndPassword(
                        mEmailEditText.getText().toString(),
                        mPasswordEditText.getText().toString()
                ).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            startActivity(new Intent(AuthActivity.this, FriendsActivity.class));
                        }
                    }
                });
            }

            mEmailEditText.setText("");
            mPasswordEditText.setText("");
        }
    }
}