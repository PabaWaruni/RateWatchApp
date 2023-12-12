package com.pabawaruni.bank_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

// Importing required classes and libraries
public class Login extends AppCompatActivity {

    // Declaring variables for UI elements
    private EditText Name, Password;
    private Button Contniue;
    private TextView ForgetPswd;

    public static String usersName;
    public static String emailAddress;
    public static String mobileNumber;

    // Method that is called when the activity is created
    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Setting the layout for the activity
        setContentView(R.layout.activity_login);

        // Initializing the UI elements
        Name = findViewById(R.id.etUsername);
        Password = findViewById(R.id.etPassword);
        Contniue = findViewById(R.id.btnContinue);
        ForgetPswd = findViewById(R.id.TextForgot);

        // Setting a listener for the "Continue" button
        Contniue.setOnClickListener(view -> {
            // Checking if the username and password are valid
            if (!validateUsername() | !validatePassword()) {
                // If they are not valid, do nothing
            } else {
                checkUser(); // If they are valid, call the checkUser() method
            }
        });

        // Setting a listener for the "Forgot password?" text view
        ForgetPswd.setOnClickListener(view -> {
            // Creating an intent to start the VerifyAccount activity
            Intent intent = new Intent(Login.this, VerifyAccount.class);
            // Starting the VerifyAccount activity
            startActivity(intent);
        });

        // Setting a touch listener for the password EditText to toggle password visibility
        Password.setOnTouchListener((v, event) -> {
            final int DRAWABLE_RIGHT = 2;
            if (event.getAction() == MotionEvent.ACTION_UP) {
                if (event.getRawX() >= (Password.getRight() - Password.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                    // Toggle password visibility
                    if (Password.getTransformationMethod() == PasswordTransformationMethod.getInstance()) {
                        Password.setTransformationMethod(null);
                    } else {
                        Password.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    }
                    Password.setSelection(Password.getText().length());
                    return true;
                }
            }
            return false;
        });

    }

    // Method to check if the username and password are correct
    private void checkUser() {
        String userUsername = Name.getText().toString().trim();
        String userPassword = Password.getText().toString().trim();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users");
        Query checkUserDatabase = reference.orderByChild("username").equalTo(userUsername);
        checkUserDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    Name.setError(null);
                    String passwordFromDB = snapshot.child(userUsername).child("password").getValue(String.class);
                    if (passwordFromDB.equals(userPassword)) {
                        Name.setError(null);

                        // Get the other user details from the database
                        String usernameFromDB = snapshot.child(userUsername).child("username").getValue(String.class);
                        String emailFromDB = snapshot.child(userUsername).child("email").getValue(String.class);
                        String mobileNoFromDB = snapshot.child(userUsername).child("mobileNo").getValue(String.class);

                        // Create an intent to start the Dashboard activity and pass the user details as extras
                        Intent intent = new Intent(Login.this, Dashboard.class);
                        usersName = usernameFromDB;
                        emailAddress = emailFromDB;
                        mobileNumber = mobileNoFromDB;
                        startActivity(intent);

                        // Show a toast message to indicate that the login was successful
                        Toast.makeText(Login.this, "Login Successfully.", Toast.LENGTH_SHORT).show();
                    } else {
                        // Set an error message on the Password field to indicate that the entered credentials are invalid
                        Password.setError("Invalid Credentials");
                        Password.requestFocus();
                    }
                } else {
                    // Set an error message on the Name field to indicate that the entered username does not exist
                    Name.setError("User does not exist");
                    Name.requestFocus();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Do nothing on cancellation of the query
            }
        });
    }

    // Method to validate the password
    private boolean validatePassword() {
        // Getting the text entered in the password EditText
        String val = Password.getText().toString();
        // Checking if the password is empty
        if (val.isEmpty()) {
            // Setting an error message for the password EditText
            Password.setError("Password cannot be empty");
            return false;
        } else {
            // If the password is not empty, return true
            Password.setError(null);
            return true;
        }
    }

    // Method to validate the username
    public boolean validateUsername() {
        // Getting the text entered in the username EditText
        String val = Name.getText().toString();
        // Checking if the username is empty
        if (val.isEmpty()) {
            // Setting an error message for the username EditText
            Name.setError("Username cannot be empty");
            return false;
        } else {
            // If the username is not empty, return true
            Name.setError(null);
            return true;
        }
    }
}
