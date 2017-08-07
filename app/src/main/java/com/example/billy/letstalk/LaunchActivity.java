package com.example.billy.letstalk;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class LaunchActivity extends AppCompatActivity implements View.OnClickListener {

    private Button signUpButton;
    private Button loginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launch);

        signUpButton = (Button) findViewById(R.id.signUpButton);
        loginButton = (Button) findViewById(R.id.loginButton);

        signUpButton.setOnClickListener(this);
        loginButton.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(LaunchActivity.this, AuthActivity.class);
        // if (v.getId() == R.id.signUpButton) {}
        switch (v.getId()) {
            case (R.id.signUpButton):
                intent.putExtra("isSignUp", true );
                break;
            case(R.id.loginButton):
                default:
                    intent.putExtra("isSignUp", false);
                    break;
        }
        startActivity(intent);
    }
}
