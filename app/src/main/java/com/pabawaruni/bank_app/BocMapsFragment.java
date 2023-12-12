package com.pabawaruni.bank_app;

import static com.google.android.gms.maps.model.BitmapDescriptorFactory.HUE_GREEN;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class BocMapsFragment extends Fragment implements OnMapReadyCallback {

    private GoogleMap mMap;
    private Map<String, LatLng> branchLocations = new HashMap<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_boc_maps, container, false);

        // Get the map fragment from the XML layout
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        return v;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Check for location permissions
        if (ActivityCompat.checkSelfPermission(getContext(),
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(getContext(),
                        Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    1);

            return;
        }

        // Enable the My Location button and show the user's location
        mMap.setMyLocationEnabled(true);

        // Enable zoom controls, compass, map toolbar, tilt and rotate gestures, and my location button
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.getUiSettings().setCompassEnabled(true);
        mMap.getUiSettings().setMapToolbarEnabled(true);
        mMap.getUiSettings().setTiltGesturesEnabled(true);
        mMap.getUiSettings().setRotateGesturesEnabled(true);
        mMap.getUiSettings().setMyLocationButtonEnabled(true);

        // Add a listener to fetch the branch locations from the Firebase Realtime Database
        FirebaseDatabase.getInstance().getReference("Banks/BOC")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot placeSnapshot : dataSnapshot.getChildren()) {
                            String placeName = placeSnapshot.getKey();
                            double latitude = placeSnapshot.child("latitude").getValue(Double.class);
                            double longitude = placeSnapshot.child("longitude").getValue(Double.class);
                            LatLng placeLatLng = new LatLng(latitude, longitude);
                            branchLocations.put(placeName, placeLatLng);
                            mMap.addMarker(new MarkerOptions()
                                    .position(placeLatLng)
                                    .title(placeName)
                                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        // Handle database error if needed
                    }
                });

        // Set a location change listener to update the map when the user's location changes
        mMap.setOnMyLocationChangeListener(new GoogleMap.OnMyLocationChangeListener() {
            @Override
            public void onMyLocationChange(Location location) {
                onLocationChanged(location);
            }
        });
    }

    private void onLocationChanged(Location location) {
        // Called when the user's location changes

        // Get the user's current latitude and longitude
        double currentLatitude = location.getLatitude();
        double currentLongitude = location.getLongitude();
        LatLng currentLatLng = new LatLng(currentLatitude, currentLongitude);

        // Add a marker for the user's location
        mMap.addMarker(new MarkerOptions()
                .position(currentLatLng)
                .title("Current Location")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));

//        // Move the camera to the user's location and zoom to level 6
//        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 8));

        // Find the nearest bank branch
        LatLng nearestBranchLatLng = findNearestBranch(currentLatLng);

        if (nearestBranchLatLng != null) {
            // Add a marker for the nearest branch and highlight it
            Marker nearestBranchMarker = mMap.addMarker(new MarkerOptions()
                    .position(nearestBranchLatLng)
                    .title("Nearest Branch")
                    .icon(BitmapDescriptorFactory.defaultMarker(HUE_GREEN)));

            // Display the name of the nearest branch
            String nearestBranchName = getBranchName(nearestBranchLatLng);
            if (nearestBranchName != null) {
                Toast.makeText(getContext(), "Nearest Branch: " + nearestBranchName, Toast.LENGTH_SHORT).show();
            }
        }
    }

    private LatLng findNearestBranch(LatLng currentLatLng) {
        // Find the nearest branch based on the user's current location
        LatLng nearestBranchLatLng = null;
        double shortestDistance = Double.MAX_VALUE;

        for (LatLng branchLatLng : branchLocations.values()) {
            double distance = calculateDistance(currentLatLng, branchLatLng);
            if (distance < shortestDistance) {
                shortestDistance = distance;
                nearestBranchLatLng = branchLatLng;
            }
        }

        return nearestBranchLatLng;
    }

    private double calculateDistance(LatLng startLatLng, LatLng endLatLng) {
        // Calculate the distance between two LatLng points using the Haversine formula
        double lat1 = startLatLng.latitude;
        double lon1 = startLatLng.longitude;
        double lat2 = endLatLng.latitude;
        double lon2 = endLatLng.longitude;

        double R = 6371; // Radius of the earth in km
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                        Math.sin(dLon / 2) * Math.sin(dLon / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double distance = R * c;

        return distance;
    }

    private String getBranchName(LatLng branchLatLng) {
        // Iterate over each branch location and check if it matches the given LatLng
        for (Map.Entry<String, LatLng> entry : branchLocations.entrySet()) {
            if (entry.getValue().equals(branchLatLng)) {
                return entry.getKey();
            }
        }
        return null;
    }
}
