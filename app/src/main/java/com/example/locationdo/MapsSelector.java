package com.example.locationdo;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
/**
 * Names: Jonas, Weston, Grant, Mike
 * Course: CIT368-01
 * Assignment: Group Part 2
 * Date: 4/28/2019
 * Purpose: This is the activity that allows you to select a location for a specific task.
 * Assumptions: Min SDK 23, Target SDK 28
 *              Updated google play services
 *
 */

public class MapsSelector extends FragmentActivity implements GoogleMap.OnMyLocationButtonClickListener, GoogleMap.OnMyLocationClickListener, GoogleMap.OnMapClickListener, GoogleMap.OnMapLongClickListener, OnMapReadyCallback {

    private GoogleMap mMap;
    private String title, desc;
    private int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        Intent intent = getIntent();
        id = intent.getIntExtra("id", -1);
        title = intent.getStringExtra("title");
        desc = intent.getStringExtra("desc");
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);
        } else {
            // Show rationale and request permission.
        }
        mMap.setOnMyLocationButtonClickListener(this);
        mMap.setOnMyLocationClickListener(this);
        mMap.setOnMapClickListener(this);
        mMap.setOnMapLongClickListener(this);

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }

    /**
     * Shows the location title tag
     * @param location
     */
    @Override
    public void onMyLocationClick(@NonNull Location location) {
        Toast.makeText(this, "Current location:\n" + location, Toast.LENGTH_LONG).show();
    }

    /**
     * Return false so that we don't consume the event and the default behavior still occurs
     *    (the camera animates to the user's current position).
     * @return
     */
    @Override
    public boolean onMyLocationButtonClick() {
        Toast.makeText(this, "MyLocation button clicked", Toast.LENGTH_SHORT).show();
        return false;
    }

    @Override
    public void onMapClick(LatLng point) {

    }

    /**
     * On a long click it will grab the point and assign the values to the task
     * @param point
     */
    @Override
    public void onMapLongClick(LatLng point) {

        try {
            String strStatement = "INSERT INTO TASK (name, description, status, latitude, longitude) VALUES (?, ?, 0, ?, ?)";//, Statement.RETURN_GENERATED_KEYS);
            PreparedStatement psUpdate = Settings.getInstance().getConnection().prepareStatement(strStatement);
            psUpdate.setString(1, title);
            psUpdate.setString(2, desc);
            psUpdate.setDouble(3, point.latitude);
            psUpdate.setDouble(4, point.longitude);
            int result = psUpdate.executeUpdate();
            if (result == 1) {
                ResultSet rs = psUpdate.getGeneratedKeys();
                while (rs.next()) {
                    int taskid = rs.getInt("id");
                    String strStatement2 = "INSERT INTO USERTASK (account_id, task_id) VALUES (?, ?)";
                    PreparedStatement psUpdate2 = Settings.getInstance().getConnection().prepareStatement(strStatement);
                    psUpdate2.setInt(1, id);
                    psUpdate2.setInt(2, taskid);
                    int result2 = psUpdate2.executeUpdate();
                    if (result2 == 1) {

                    }
                    psUpdate2.close();
                }
            }

            psUpdate.close();

            Intent intent = new Intent(getApplicationContext(), com.example.locationdo.ListActivity.class);
            intent.putExtra("id", id);
            startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
