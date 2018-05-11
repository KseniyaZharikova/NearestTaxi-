package com.example.kseniya.nearesttaxi.ui;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.example.kseniya.nearesttaxi.R;
import com.example.kseniya.nearesttaxi.TaxiApplication;
import com.example.kseniya.nearesttaxi.data.RetrofitService;
import com.example.kseniya.nearesttaxi.models.Company;
import com.example.kseniya.nearesttaxi.models.Driver;
import com.example.kseniya.nearesttaxi.models.Main;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.squareup.picasso.Picasso;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnInfoWindowLongClickListener {

    private GoogleMap mMap;
    private RetrofitService service;
    double lat;
    double lon;
    String phone;
    MarkerOptions markerOptions;
    private Marker mMarker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        service = TaxiApplication.get(getApplicationContext()).getService();
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        markerOptions = new MarkerOptions();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        googleMap.setOnInfoWindowLongClickListener(this);
        lat = getIntent().getDoubleExtra("location1", 0);
        lon = getIntent().getDoubleExtra("location2", 0);

        LatLng bishkek = new LatLng(lat, lon);
        mMap.addMarker(new MarkerOptions().position(bishkek).title("Marker in Bishkek"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(bishkek));
        mMap.setMaxZoomPreference(18);
        mMap.setMinZoomPreference(15);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission
                (this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mMap.setMyLocationEnabled(true);
        getInformationTaxiGPS();
    }

    private void getInformationTaxiGPS() {

        service.getInformationTaxi(lat, lon)
                .enqueue(new Callback<Main>() {
                    @Override
                    public void onResponse(Call<Main> call, Response<Main> response) {
                        if (response.isSuccessful() && response.body() != null) {

                            for (Company company : response.body().getCompanies()) {
                                for (Driver driver : company.getDrivers()) {
                                    LatLng cars = new LatLng(driver.getLat(),
                                            driver.getLon());
                                    mMarker = mMap.addMarker(markerOptions.position(cars)
                                            .flat(true)
                                            .title(company.getName().toString())
                                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.car_icon)));

                                    InfoWindowAdapter infoWindowAdapter = new InfoWindowAdapter(getApplicationContext());
                                    mMap.setInfoWindowAdapter(infoWindowAdapter);
                                    Company model = new Company();
                                    model.setName(company.getName());
                                    model.setIcon(company.getIcon());
                                    mMarker = mMap.addMarker(markerOptions);
                                    mMarker.setTag(company);
                                    mMarker.showInfoWindow();
                                }
                            }
                        } else {
                            Toast.makeText(getApplicationContext(), "Сервер не отвечает", Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Main> call, Throwable throwable) {
                        Toast.makeText(getApplicationContext(), "onFailure", Toast.LENGTH_LONG).show();
                    }
                });
        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {


                Company model = (Company) marker.getTag();

                if (model != null) {
                    Log.e("adapter", "show is " + model.getName());
                    Log.e("adapter", "show is " + model.getDrivers().size());
                    marker.showInfoWindow();
                }
                return false;
            }
        });
    }

    @Override
    public void onInfoWindowLongClick(Marker marker) {
        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", marker.getSnippet(), null));
        startActivity(intent);
    }
}
