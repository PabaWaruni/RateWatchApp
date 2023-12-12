package com.pabawaruni.bank_app;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.annotations.NotNull;

public class UserProfileFragment extends Fragment {

    EditText username;
    EditText email;
    EditText mobile;

    Button delete, save;

    String profileUsername, profileEmail, profileMobile;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_user_profile, container, false);

        // Initialize the UI elements
        username = view.findViewById(R.id.etUSERname);
        email = view.findViewById(R.id.etemailAddress);
        mobile = view.findViewById(R.id.etmobilephone);

        // Get the profile details from the login activity
        profileUsername = Login.usersName;
        profileEmail = Login.emailAddress;
        profileMobile = Login.mobileNumber;

        // Set the profile details in the EditText fields
        username.setText(profileUsername);
        email.setText(profileEmail);
        mobile.setText(profileMobile);

        // Initialize the save button and set the click listener
        save = view.findViewById(R.id.btnSave);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Show confirmation dialog before saving changes
                showEditConfirmationDialog();
            }
        });

        // Initialize the delete button and set the click listener
        delete = view.findViewById(R.id.btnDelete);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Show confirmation dialog before deleting the account
                showConfirmationDialog();
            }
        });

        return view;
    }

    // Show confirmation dialog before saving changes
    private void showEditConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Confirm Edit Your Profile");
        builder.setMessage("Are you sure you want to save the changes?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                // User confirmed, update the profile
                updateProfile();
            }
        });
        builder.setNegativeButton("No", null);
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    // Update the user's profile
    private void updateProfile() {
        // Get the new values from the EditText fields
        String newUsername = username.getText().toString().trim();
        String newEmail = email.getText().toString().trim();
        String newMobile = mobile.getText().toString().trim();

        // Get a reference to the Firebase database
        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference("users");
        DatabaseReference currentUsernameRef = dbRef.child(profileUsername);
        DatabaseReference newUsernameRef = dbRef.child(newUsername);

        // Step 1: Retrieve the current username
        currentUsernameRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DataSnapshot snapshot = task.getResult();
                if (snapshot.exists()) {
                    // Step 2: Retrieve the password
                    String password = snapshot.child("password").getValue(String.class);

                    // Step 3: Delete the existing parent node with all child nodes
                    currentUsernameRef.removeValue().addOnSuccessListener(aVoid -> {
                        // Step 4: Create a new parent node with updated username and copy child nodes
                        newUsernameRef.child("username").setValue(newUsername);
                        newUsernameRef.child("password").setValue(password);
                        newUsernameRef.child("email").setValue(newEmail);
                        newUsernameRef.child("mobileNo").setValue(newMobile)
                                .addOnSuccessListener(aVoid1 -> {
                                    showToast(getContext(), "Profile edited successfully");
                                })
                                .addOnFailureListener(e -> {
                                    showToast(getContext(), "Failed to edit profile");
                                });
                    }).addOnFailureListener(e -> {
                        showToast(getContext(), "Failed to delete existing profile");
                    });
                } else {
                    showToast(getContext(), "Profile not found");
                }
            } else {
                showToast(getContext(), "Failed to retrieve profile");
            }
        });
    }

    // Show confirmation dialog before deleting the account
    private void showConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Confirm Delete Your Account");
        builder.setMessage("Are you sure you want to delete your account?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                // User confirmed, delete the account
                deleteRecord(profileUsername, getContext());
            }
        });
        builder.setNegativeButton("No", null);
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    // Delete the user's account
    public void deleteRecord(String usernameUser, Context context) {
        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference("users").child(usernameUser);
        dbRef.removeValue()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        showToast(context, "Account Deleted Successfully");
                        Intent intent = new Intent(getContext(), Home.class);
                        startActivity(intent);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NotNull Exception e) {
                        showToast(context, "Error deleting Account");
                    }
                });
    }

    // Show toast message
    private void showToast(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }
}
