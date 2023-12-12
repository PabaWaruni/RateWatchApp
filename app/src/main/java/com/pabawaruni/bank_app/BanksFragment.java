package com.pabawaruni.bank_app;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

public class BanksFragment extends Fragment {

    private SearchView searchView;
    private ScrollView scrollView;
    private AppCompatImageView btnBank1;
    private AppCompatImageView btnBank2;
    private AppCompatImageView btnBank3;
    private AppCompatImageView btnBank4;
    private AppCompatImageView btnBank5;
    private AppCompatImageView btnBank6;
    private ImageView btnLogOut, imageSampath, imageCommercial, imageBOC, imageDFCC, imagePeoples;

    public BanksFragment() {
        // Required empty public constructor
    }

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_banks, container, false);

        // set opacity in image
        ImageView imageView = view.findViewById(R.id.backImage);
        float opacity = 0.5f; // Opacity value between 0.0f and 1.0f (0% to 100%)
        imageView.setAlpha(opacity);

        // Get references to views
        searchView = view.findViewById(R.id.searchView);
        scrollView = view.findViewById(R.id.scrollView);
        btnBank1 = view.findViewById(R.id.btnBank1);
        btnBank2 = view.findViewById(R.id.btnBank2);
        btnBank3 = view.findViewById(R.id.btnBank3);
        btnBank4 = view.findViewById(R.id.btnBank4);
        btnBank5 = view.findViewById(R.id.btnBank5);
        btnBank6 = view.findViewById(R.id.btnBank6);

        // Set up search view listener
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // Perform search and scroll to the appropriate bank
                searchAndScroll(query);
                return true;
            }

            private void searchAndScroll(String query) {
                // Convert the query to lowercase for case-insensitive comparison
                String lowerCaseQuery = query.toLowerCase();

                // Find the appropriate bank based on the query
                View targetView = null;
                if (lowerCaseQuery.contains("sampath")) {
                    targetView = btnBank1;
                } else if (lowerCaseQuery.contains("commercial")) {
                    targetView = btnBank2;
                } else if (lowerCaseQuery.contains("boc")) {
                    targetView = btnBank3;
                } else if (lowerCaseQuery.contains("peoples")) {
                    targetView = btnBank4;
                } else if (lowerCaseQuery.contains("dfcc")) {
                    targetView = btnBank5;
                } else if (lowerCaseQuery.contains("nsb")) {
                    targetView = btnBank6;
                }

                // Scroll to the target view if found
                if (targetView != null) {
                    View finalTargetView = targetView;
                    scrollView.post(new Runnable() {
                        @Override
                        public void run() {
                            int scrollTo = finalTargetView.getTop() + scrollView.getTop();
                            scrollView.smoothScrollTo(0, scrollTo);
                        }
                    });
                }
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // Optional: Implement live search as the user types
                // searchAndScroll(newText);
                return true;
            }
        });

        //  create clickable Sampath bank image
        imageSampath = view.findViewById(R.id.btnBank1);
        imageSampath.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), SampathBankActivity.class);
                startActivity(intent);
            }
        });

        //  create clickable Commercial bank image
        imageCommercial = view.findViewById(R.id.btnBank2);
        imageCommercial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), CommercialBankActivity.class);
                startActivity(intent);
            }
        });

        //  create clickable BOC bank image
        imageBOC = view.findViewById(R.id.btnBank3);
        imageBOC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), BocBankActivity.class);
                startActivity(intent);
            }
        });

        // Get reference to logout button
        btnLogOut = view.findViewById(R.id.btnLogOut);
        btnLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Perform logout action
                logout();
            }
        });

        return view;
    }

    private void logout() {
        // Clear session data
        // Use SharedPreferences to store session information
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("MySession", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();

        // Navigate to the login screen
        Intent intent = new Intent(getContext(), Login.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        getActivity().finish();
    }
}
