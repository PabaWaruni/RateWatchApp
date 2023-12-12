package com.pabawaruni.bank_app;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class Home extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // Find the Login button in the layout and assign a click listener to it
        Button loginButton = findViewById(R.id.btnLogin);
        loginButton.setOnClickListener(this);

        // Find the Signup button in the layout and assign a click listener to it
        Button signupButton = findViewById(R.id.btnSignup);
        signupButton.setOnClickListener(this);
    }

    // Handle click events for both buttons
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnLogin:
                openLogin(); // Call the openLogin() method when the Login button is clicked
                break;
            case R.id.btnSignup:
                openSignUp(); // Call the openSignUp() method when the Signup button is clicked
                break;
        }
    }

    // Method to open the SignUp activity
    public void openSignUp() {
        Intent intent = new Intent(this, SignUp.class);
        startActivity(intent);
    }

    // Method to open the Login activity
    public void openLogin() {
        Intent intent = new Intent(this, Login.class);
        startActivity(intent);
    }
}
