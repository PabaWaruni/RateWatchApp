package com.pabawaruni.bank_app;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.PasswordTransformationMethod;
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

public class ResetPassword extends AppCompatActivity {

    private EditText newPassword, confirmPassword;
    private Button resetButton;
    private ImageView backButton;

    private DatabaseReference usersRef;

    private boolean isNewPasswordVisible = false;
    private boolean isConfirmPasswordVisible = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        usersRef = FirebaseDatabase.getInstance().getReference("users");

        newPassword = findViewById(R.id.etNewpassword);
        confirmPassword = findViewById(R.id.etNConfirmpswd);
        resetButton = findViewById(R.id.btnOk);
        ImageView newPasswordToggle = findViewById(R.id.ivNewPasswordToggle);
        ImageView confirmPasswordToggle = findViewById(R.id.ivConfirmPasswordToggle);
        backButton = findViewById(R.id.backButton);

        // Button click listener to initiate password reset
        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resetPassword();
            }
        });

        // Toggle visibility of the new password field
        newPasswordToggle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                togglePasswordVisibility(newPassword, newPasswordToggle);
            }
        });

        // Toggle visibility of the confirm password field
        confirmPasswordToggle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                togglePasswordVisibility(confirmPassword, confirmPasswordToggle);
            }
        });

        // Go back to the previous activity when the back button is clicked
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish(); // Close the current activity and return to the previous one
    }

    // Method to initiate the password reset process
    private void resetPassword() {
        String newPasswordText = newPassword.getText().toString().trim();
        String confirmPasswordText = confirmPassword.getText().toString().trim();

        // Validate the new password
        if (!isPasswordValid(newPasswordText)) {
            return;
        }

        // Validate the confirm password
        if (!isConfirmPasswordValid(newPasswordText, confirmPasswordText)) {
            return;
        }

        // Get the entered username from the previous activity
        final String enteredUsername = getIntent().getStringExtra("username");

        // Query the Firebase database to find the user with the entered username
        usersRef.orderByChild("username").equalTo(enteredUsername).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String userId = dataSnapshot.getChildren().iterator().next().getKey();

                    // Continue with the password reset process
                    DatabaseReference currentUserRef = usersRef.child(userId);
                    currentUserRef.child("password").setValue(newPasswordText)
                            .addOnCompleteListener(task -> {
                                if (task.isSuccessful()) {
                                    Toast.makeText(ResetPassword.this, "Password reset successfully", Toast.LENGTH_SHORT).show();
                                    openLoginActivity(); // Open the login activity after password reset
                                    finish(); // Close the current activity
                                } else {
                                    Toast.makeText(ResetPassword.this, "Failed to reset password. Please try again later.", Toast.LENGTH_SHORT).show();
                                }
                            });
                } else {
                    Toast.makeText(ResetPassword.this, "User not found. Please try again later.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(ResetPassword.this, "Database error: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Validate the new password
    private boolean isPasswordValid(String password) {
        if (TextUtils.isEmpty(password)) {
            newPassword.setError("Please enter a new password");
            return false;
        }

        if (password.length() < 8) {
            newPassword.setError("Password must be at least 8 characters long");
            return false;
        }

        return true;
    }

    // Validate the confirm password
    private boolean isConfirmPasswordValid(String password, String confirmPassword) {
        if (TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Please confirm your new password", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (!confirmPassword.equals(password)) {
            Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    // Toggle the visibility of the password fields
    private void togglePasswordVisibility(EditText editText, ImageView toggleImageView) {
        if (editText.getTransformationMethod() == PasswordTransformationMethod.getInstance()) {
            // Password is currently masked, show the password
            editText.setTransformationMethod(null);
            toggleImageView.setImageResource(R.drawable.ic_eye);
        } else {
            // Password is currently visible, mask the password
            editText.setTransformationMethod(PasswordTransformationMethod.getInstance());
            toggleImageView.setImageResource(R.drawable.ic_eye);
        }
        editText.setSelection(editText.getText().length());
    }

    // Open the login activity
    public void openLoginActivity() {
        Intent intent = new Intent(this, Login.class);
        startActivity(intent);
    }
}
