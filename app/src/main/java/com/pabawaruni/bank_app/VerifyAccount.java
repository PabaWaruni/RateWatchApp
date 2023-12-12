package com.pabawaruni.bank_app;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class VerifyAccount extends AppCompatActivity {

    // Declare variables
    private EditText username, email, mobile;
    private Button enter;
    private ImageView backButton;

    private DatabaseReference usersRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_account);

        // Initialize the Firebase database reference
        usersRef = FirebaseDatabase.getInstance().getReference("users");

        // Initialize the UI elements
        username = findViewById(R.id.etname);
        email = findViewById(R.id.etemailaddress);
        mobile = findViewById(R.id.etmobile);
        enter = findViewById(R.id.btnenter);
        backButton = findViewById(R.id.backButton);

        // Set click listener for enter button
        enter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                verifyAccount();
            }
        });

        // Set click listener for back button
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        finish(); // Close the current activity and return to the previous one
    }

    // Method to verify the user's account
    private void verifyAccount() {
        // Get the entered username, email, and mobile number
        final String enteredUsername = username.getText().toString().trim();
        final String enteredEmail = email.getText().toString().trim();
        final String enteredMobile = mobile.getText().toString().trim();

        // Listen for a single value event from the Firebase database reference
        usersRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                boolean accountVerified = false;

                // Iterate through each user in the database
                for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                    // Get the user data from the snapshot
                    HelperClass user = userSnapshot.getValue(HelperClass.class);

                    // Check if the entered username, email, and mobile number match with any user in the database
                    if (user != null && user.getUsername().equals(enteredUsername)
                            && user.getEmail().equals(enteredEmail)
                            && user.getmobileNo().equals(enteredMobile)) {
                        accountVerified = true;
                        break;
                    }
                }

                // Check if the account is verified
                if (accountVerified) {
                    Toast.makeText(VerifyAccount.this, "Account verified", Toast.LENGTH_SHORT).show();
                    openResetPassword();
                } else {
                    Toast.makeText(VerifyAccount.this, "Account verification failed", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(VerifyAccount.this, "Database error: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Method to open the reset password activity
    public void openResetPassword() {
        // Get the entered username, email, and mobile number
        String enteredUsername = username.getText().toString().trim();
        String enteredEmail = email.getText().toString().trim();
        String enteredMobile = mobile.getText().toString().trim();

        // Create an intent to open the ResetPassword activity
        Intent intent = new Intent(this, ResetPassword.class);
        intent.putExtra("username", enteredUsername);
        intent.putExtra("email", enteredEmail);
        intent.putExtra("mobile", enteredMobile);
        startActivity(intent);
    }
}
