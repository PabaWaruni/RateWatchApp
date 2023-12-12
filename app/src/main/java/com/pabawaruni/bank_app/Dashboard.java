package com.pabawaruni.bank_app;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class Dashboard extends AppCompatActivity {

    // Declare variables
    BottomNavigationView bottomNavigationView;
    BanksFragment banksFragment = new BanksFragment();
    UserProfileFragment profileFragment = new UserProfileFragment();
    QrReaderFragment qrReaderFragment = new QrReaderFragment();


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        // Initialize bottom navigation view
        bottomNavigationView = findViewById(R.id.bottomNavigation);

        // Set the initial fragment to display
        getSupportFragmentManager().beginTransaction().replace(R.id.container, banksFragment).commit();

        // Handle item selection in the bottom navigation view
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.banks:
                        // Replace the current fragment with the BanksFragment
                        getSupportFragmentManager().beginTransaction().replace(R.id.container, banksFragment).commit();
                        return true;
                    case R.id.profile:
                        // Replace the current fragment with the UserProfileFragment
                        getSupportFragmentManager().beginTransaction().replace(R.id.container, profileFragment).commit();
                        return true;
                    case R.id.QR:
                        // Replace the current fragment with the QrReaderFragment
                        getSupportFragmentManager().beginTransaction().replace(R.id.container, qrReaderFragment).commit();
                        return true;
                }
                return false;
            }
        });
    }
}
