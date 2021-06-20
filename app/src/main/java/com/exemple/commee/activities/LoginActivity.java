package com.exemple.commee.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.exemple.commee.FirebaseUtils;
import com.exemple.commee.R;
import com.google.android.material.button.MaterialButton;

public class LoginActivity extends AppCompatActivity {

    private MaterialButton back, login;
    private TextView signUpRedirect;
    private EditText email, password;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);
        Splash.changeStatusBarColor(LoginActivity.this, R.color.dark_purple);

        back = findViewById(R.id.login_back);
        signUpRedirect = findViewById(R.id.signup_redirect);
        email = findViewById(R.id.profile_login_email);
        password = findViewById(R.id.profile_login_password);
        login = findViewById(R.id.login_button);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                finish();
            }
        });
        signUpRedirect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, SignUpActivity.class));
                finish();
            }
        });
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (email.getText() != null && password.getText() != null) {
                    try {
                        boolean b = FirebaseUtils.login(LoginActivity.this, email.getText().toString(), password.getText().toString());
                        if (b) {
                            startActivity(new Intent(LoginActivity.this, MainActivity.class));
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(LoginActivity.this, MainActivity.class));
        finish();
    }

}
