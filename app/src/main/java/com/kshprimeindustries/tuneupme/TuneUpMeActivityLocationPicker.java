package com.kshprimeindustries.tuneupme;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class TuneUpMeActivityLocationPicker extends AppCompatActivity {


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
//            onBackPressed();
            Intent resultIntent = new Intent();
            resultIntent.putExtra("latitude", "null");
            setResult(RESULT_OK, resultIntent);
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    double pickedLatitude;
    double pickedLongitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_tune_up_me_location_picker);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.mainLayout), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        SupportMapFragment supportMapFragment = new SupportMapFragment();
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.frameLayoutPickLocation, supportMapFragment);
        fragmentTransaction.commit();


        supportMapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(@NonNull GoogleMap googleMap) {

                LatLng latLng1 = new LatLng(6.928859675443896, 79.84497297309474);
                googleMap.animateCamera(
                        CameraUpdateFactory.newCameraPosition(
                                new CameraPosition.Builder()
                                        .target(latLng1)
                                        .zoom(15)
                                        .build()
                        )
                );

                googleMap.setOnMapClickListener(latLng -> {
                    Log.d("TuneUpMe", "Map Clicked!");

                    googleMap.clear();
                    googleMap.addMarker(new MarkerOptions().position(latLng).title("Selected Location"));
                    googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));


                    pickedLatitude = latLng.latitude;
                    pickedLongitude = latLng.longitude;

                    Log.d("TuneUpMe", "Received pickedLatitude: " + pickedLatitude);
                    Log.d("TuneUpMe", "Received pickedLongitude: " + pickedLongitude);

                });
            }
        });

        Button buttonPickLocation = findViewById(R.id.buttonPickLocation);
        buttonPickLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent resultIntent = new Intent();
                Bundle bundle = new Bundle();
                if (!String.valueOf(pickedLatitude).isEmpty() && !String.valueOf(pickedLongitude).isEmpty()) {
                    Log.d("TuneUpMe", "Not Null data " + pickedLatitude + " " + pickedLongitude);
                    bundle.putString("latitude",String.valueOf(pickedLatitude));
                    bundle.putString("longitude",String.valueOf(pickedLongitude));
                    resultIntent.putExtras(bundle);
//                    TuneUpMeActivityCustomerBandBookNow.latitude =String.valueOf(pickedLatitude);
//                    TuneUpMeActivityCustomerBandBookNow.longitude =String.valueOf(pickedLongitude);
                } else {
                    Log.d("TuneUpMe", " Null data");
//                    TuneUpMeActivityCustomerBandBookNow.latitude =String.valueOf(null);
//                    TuneUpMeActivityCustomerBandBookNow.longitude =String.valueOf(null);
                    bundle.putString("latitude",String.valueOf(null));
                    bundle.putString("longitude",String.valueOf(null));
                    resultIntent.putExtras(bundle);
                }
                setResult(RESULT_OK, resultIntent);
                finish();
            }
        });

    }
}