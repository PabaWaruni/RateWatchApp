package com.pabawaruni.bank_app;

// Import required classes

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignUp extends AppCompatActivity {

    // Initialize variables
    EditText username, password, ConfirmPassword, mobileNo, email;
    Button enter;

    FirebaseDatabase database;
    DatabaseReference reference;

    @SuppressLint({"MissingInflatedId", "ClickableViewAccessibility"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        // Initialize Firebase
        database = FirebaseDatabase.getInstance();
        reference = database.getReference("users");

        // Initialize EditText fields
        username = findViewById(R.id.etName);
        email = findViewById(R.id.etEmail);
        mobileNo = findViewById(R.id.etPhone);
        password = findViewById(R.id.etpassword);
        ConfirmPassword = findViewById(R.id.etConfirmpswd);

        // Initialize button
        enter = findViewById(R.id.btnEnter);

        // Set click listener for the enter button
        enter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Perform form validations
                if (!ValidationUsername() | !ValidationEmail() | !ValidationMobilenumber() | !ValidationPassword() | !ValidateConfirmPassword()) {
                    // If any validation fails, do nothing
                } else {
                    // All validations pass, check user
                    CheckUser();
                }
            }
        });

        // Setting a touch listener for the password EditText to toggle password visibility
        password.setOnTouchListener((v, event) -> {
            final int DRAWABLE_RIGHT = 2;
            if (event.getAction() == MotionEvent.ACTION_UP) {
                if (event.getRawX() >= (password.getRight() - password.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                    // Toggle password visibility
                    if (password.getTransformationMethod() == PasswordTransformationMethod.getInstance()) {
                        password.setTransformationMethod(null);
                    } else {
                        password.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    }
                    password.setSelection(password.getText().length());
                    return true;
                }
            }
            return false;
        });

        // Setting a touch listener for the confirm password EditText to toggle password visibility
        ConfirmPassword.setOnTouchListener((v, event) -> {
            final int DRAWABLE_RIGHT = 2;
            if (event.getAction() == MotionEvent.ACTION_UP) {
                if (event.getRawX() >= (ConfirmPassword.getRight() - ConfirmPassword.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                    // Toggle password visibility
                    if (ConfirmPassword.getTransformationMethod() == PasswordTransformationMethod.getInstance()) {
                        ConfirmPassword.setTransformationMethod(null);
                    } else {
                        ConfirmPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    }
                    ConfirmPassword.setSelection(ConfirmPassword.getText().length());
                    return true;
                }
            }
            return false;
        });
    }

    // Validate username
    public Boolean ValidationUsername() {
        String val = username.getText().toString();
        if (val.isEmpty()) {
            username.setError("Username cannot be empty...");
            return false;
        } else if (val.contains(" ")) {
            username.setError("Username cannot contain spaces...");
            return false;
        } else {
            username.setError(null);
            return true;
        }
    }

    // Validate email
    public Boolean ValidationEmail() {
        String val = email.getText().toString();
        if (val.isEmpty()) {
            email.setError("Email cannot be empty...");
            return false;
        } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(val).matches()) {
            email.setError("Please enter a valid email address...");
            return false;
        } else {
            email.setError(null);
            return true;
        }
    }

    // Validate mobile number
    public Boolean ValidationMobilenumber() {
        String val = mobileNo.getText().toString();
        if (val.isEmpty()) {
            mobileNo.setError("Mobile number cannot be empty...");
            return false;
        } else if (val.length() != 10) {
            mobileNo.setError("Invalid mobile number...");
            return false;
        } else {
            mobileNo.setError(null);
            return true;
        }
    }

    // Validate password
    public Boolean ValidationPassword() {
        String val = password.getText().toString();
        if (val.isEmpty()) {
            password.setError("Password cannot be empty...");
            return false;
        } else if (val.length() < 8) {
            password.setError("Password must be minimum 8 characters in length...");
            return false;
        } else {
            password.setError(null);
            return true;
        }
    }

    // Validate confirm password
    public Boolean ValidateConfirmPassword() {
        String confirmPasswordVar = ConfirmPassword.getText().toString();
        String PasswordVal = password.getText().toString();

        if (confirmPasswordVar.isEmpty()) {
            ConfirmPassword.setError("Password cannot be empty...");
            return false;
        } else if (!confirmPasswordVar.equals(PasswordVal)) {
            ConfirmPassword.setError("Password doesn't match...");
            return false;
        } else {
            ConfirmPassword.setError(null);
            return true;
        }
    }

    // Check user and perform signup
    public void CheckUser() {
        String setusername = username.getText().toString();
        String setEmail = email.getText().toString();
        String setmobileNo = mobileNo.getText().toString();
        String setpassword = password.getText().toString();

        // Create a helper class object with user data
        HelperClass helperClass = new HelperClass(setusername, setEmail, setmobileNo, setpassword);

        // Store user data in Firebase database
        reference.child(setusername).setValue(helperClass);

        // Set click listener for the enter button
        enter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                database = FirebaseDatabase.getInstance();
                reference = database.getReference("users");

                // Show success message and navigate to login activity
                Toast.makeText(SignUp.this, "You have signed up successfully!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(SignUp.this, Login.class);
                startActivity(intent);
            }
        });
    }
}
