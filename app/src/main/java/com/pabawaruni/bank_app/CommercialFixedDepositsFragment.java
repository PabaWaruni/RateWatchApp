package com.pabawaruni.bank_app;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;

import com.google.android.material.floatingactionbutton.FloatingActionButton;


public class CommercialFixedDepositsFragment extends Fragment {
    Button button;
    FloatingActionButton floatingActionButton ;
    CalculatorFragment calculatorFragment = new CalculatorFragment();

    @SuppressLint({"SetJavaScriptEnabled", "MissingInflatedId"})
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_commercial_fixed_deposits, container, false);

        // Initialize the floating action Button
        floatingActionButton = view.findViewById(R.id.floatingButton);

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Replace the current fragment with the CalculatorFragment
                requireActivity().getSupportFragmentManager().beginTransaction().replace(R.id.container, calculatorFragment).commit();
            }
        });

        button = view.findViewById(R.id.btnDetails1);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAlertDialog();
            }
        });
        return view;
    }

    // create alert dialog, asking the question " view more details in webView? yes or no"
    private void showAlertDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("More Details")
                .setMessage("View more details in WebView?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        showWebViewDialog();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Handle No button click
                    }
                })
                .show();
    }

    // after the giving yes, webView dialog open using in the provided link.
    private void showWebViewDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_webview, null);
        builder.setView(dialogView);
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Handle Cancel button click

            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();

        WebView webView = dialogView.findViewById(R.id.webview);
        webView.setWebViewClient(new WebViewClient());
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webView.loadUrl("https://www.combank.lk/rates-tariff#interest-rates"); // link about the bank
    }

}
