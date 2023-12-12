package com.pabawaruni.bank_app;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

@SuppressWarnings("ALL")
public class QrReaderFragment extends Fragment {

    private static final int REQUEST_CAMERA_PERMISSION = 100;

    private Button qrButton;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_qr_reader, container, false);

        // Find the QR button in the layout
        qrButton = rootView.findViewById(R.id.qrButton);
        // Set a click listener for the QR button
        qrButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Show the alert dialog to confirm opening the QR scanner
                showAlertDialog();
            }
        });

        return rootView;
    }

    private void showAlertDialog() {
        // Create an alert dialog to confirm opening the QR scanner
        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
        builder.setTitle("Open QR Scanner");
        builder.setMessage("Are you sure you want to open the QR scanner?");
        // Set the positive button click listener to check camera permission
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Check camera permission before opening the QR scanner
                checkCameraPermission();
            }
        });
        // Set the negative button to cancel the dialog
        builder.setNegativeButton("No", null);
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void checkCameraPermission() {
        // Check if the camera permission is granted
        if (ContextCompat.checkSelfPermission(requireActivity(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            // Request the camera permission if not granted
            ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA_PERMISSION);
        } else {
            // Open the QR scanner if the permission is already granted
            openQRScanner();
        }
    }

    private void openQRScanner() {
        // Create an instance of the QR scanner using IntentIntegrator library
        IntentIntegrator integrator = new IntentIntegrator(requireActivity());
        integrator.setPrompt("Scan a QR code");
        integrator.setOrientationLocked(true);
        integrator.initiateScan();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Get the result of the QR scanner
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            String scannedData = null;
            if (result.getContents() != null) {
                // Get the scanned data if available
                scannedData = result.getContents();
            } else {
                // Set a default message if scanning was canceled
                scannedData = "QR code scanning was canceled.";
            }
            // Show the dialog with the scanned data
            showMessageAlertDialog(scannedData);
        }
    }

    private void showMessageAlertDialog(String message) {
        // Create an alert dialog to show the scanned data
        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
        builder.setTitle("Scanned Data");
        // Inflate the layout for the dialog
        View dialogView = LayoutInflater.from(requireActivity()).inflate(R.layout.dialog_scanned_data, null);
        // Find the TextView in the dialog layout and set the scanned data
        TextView detailsTextView = dialogView.findViewById(R.id.detailsTextView);
        detailsTextView.setText("Details: " + message);
        builder.setView(dialogView);
        builder.setPositiveButton("OK", null);
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_CAMERA_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // If camera permission is granted, open the QR scanner
                openQRScanner();
            } else {
                // Show a dialog indicating that camera permission is required
                showPermissionDeniedDialog();
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    private void showPermissionDeniedDialog() {
        // Show a dialog informing the user that camera permission is required
        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
        builder.setTitle("Camera Permission Required");
        builder.setMessage("Please grant camera permission to scan QR codes. Go to app settings and enable the camera permission.");
        builder.setPositiveButton("Settings", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Open the app settings to enable the camera permission
                openAppSettings();
            }
        });
        builder.setNegativeButton("Cancel", null);
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void openAppSettings() {
        // Open the app settings screen for the current app
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", requireActivity().getPackageName(), null);
        intent.setData(uri);
        startActivity(intent);
    }
}
