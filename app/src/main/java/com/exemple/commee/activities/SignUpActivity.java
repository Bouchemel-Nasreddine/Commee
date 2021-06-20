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

public class SignUpActivity extends AppCompatActivity {

    private MaterialButton back, signUp;
    private TextView loginRedirect;
    private EditText email, user, birthDate, phone, password;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.singup_activity);
        Splash.changeStatusBarColor(SignUpActivity.this, R.color.dark_purple);

        back = findViewById(R.id.signup_back);
        loginRedirect = findViewById(R.id.login_redirect);
        email = findViewById(R.id.profile_signup_email);
        user = findViewById(R.id.profile_signup_user_name);
        birthDate = findViewById(R.id.profile_signup_birth_date);
        phone = findViewById(R.id.profile_signup_phone_number);
        password = findViewById(R.id.profile_signup_password);
        signUp = findViewById(R.id.signup_button);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SignUpActivity.this, MainActivity.class));
                finish();
            }
        });
        loginRedirect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SignUpActivity.this, LoginActivity.class));
                finish();
            }
        });
        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (FirebaseUtils.signIn(SignUpActivity.this, user.getText().toString(), email.getText().toString(),
                        birthDate.getText().toString(), phone.getText().toString(),password.getText().toString())) {
                    startActivity(new Intent(SignUpActivity.this, MainActivity.class));
                }
            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(SignUpActivity.this, MainActivity.class));
        finish();
    }
}
