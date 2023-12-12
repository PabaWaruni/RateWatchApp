package com.pabawaruni.bank_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class BocBankActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;
    BocAboutUsFragment bocAboutUsFragment = new BocAboutUsFragment();
    BocFixedDepositsFragment bocFixedDepositsFragment = new BocFixedDepositsFragment();
    BocMapsFragment bocMapsFragment = new BocMapsFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_boc_bank);

        // Initialize the bottom navigation view
        bottomNavigationView = findViewById(R.id.bottomNavigation);

        // Set the default fragment to display when the activity is created
        getSupportFragmentManager().beginTransaction().replace(R.id.container,bocAboutUsFragment).commit();

        // Set an event listener for the bottom navigation view
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener(){

            // Handle the event when an item in the bottom navigation view is selected
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    // If "About" is selected, replace the current fragment with the "About Us" fragment
                    case R.id.about:
                        getSupportFragmentManager().beginTransaction().replace(R.id.container, bocAboutUsFragment).commit();
                        return true;
                    // If "Rates" is selected, replace the current fragment with the "Fixed Deposits" fragment
                    case R.id.rate:
                        getSupportFragmentManager().beginTransaction().replace(R.id.container, bocFixedDepositsFragment).commit();
                        return true;
                    // If "Map" is selected, replace the current fragment with the "Maps" fragment
                    case R.id.map:
                        getSupportFragmentManager().beginTransaction().replace(R.id.container, bocMapsFragment).commit();
                        return true;
                }
                return false;
            }
        });
    }
}