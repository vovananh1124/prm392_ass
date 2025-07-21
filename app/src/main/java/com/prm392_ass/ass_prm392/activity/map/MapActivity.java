package com.prm392_ass.ass_prm392.activity.map;

import androidx.fragment.app.FragmentActivity;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.prm392_ass.ass_prm392.R;

public class MapActivity extends FragmentActivity implements OnMapReadyCallback {

    private double lat;
    private double lng;
    private String companyName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        lat = getIntent().getDoubleExtra("lat", 0);
        lng = getIntent().getDoubleExtra("lng", 0);
        companyName = getIntent().getStringExtra("company");

        SupportMapFragment mapFragment = (SupportMapFragment)
                getSupportFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        LatLng location = new LatLng(lat, lng);
        googleMap.addMarker(new MarkerOptions().position(location).title(companyName));
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 15));
    }
}
