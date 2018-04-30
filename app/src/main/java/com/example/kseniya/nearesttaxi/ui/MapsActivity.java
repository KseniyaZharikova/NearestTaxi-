package com.example.kseniya.nearesttaxi.ui;

import android.Manifest;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.example.kseniya.nearesttaxi.R;
import com.example.kseniya.nearesttaxi.TaxiApplication;
import com.example.kseniya.nearesttaxi.data.RetrofitService;
import com.example.kseniya.nearesttaxi.models.Company;
import com.example.kseniya.nearesttaxi.models.Main;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private RetrofitService service;
    Main model;
    double lat;
    double lon;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        service = TaxiApplication.get(getApplicationContext()).getService();
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;

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

        mMap.getUiSettings().setMyLocationButtonEnabled(true);
        mMap.getUiSettings().setCompassEnabled(true);
        getInformationTaxiGPS();

    }
    private void getInformationTaxiGPS() {
        service.getInformationTaxi(lat, lon)
                .enqueue(new Callback<Main>() {
                    @Override
                    public void onResponse(Call<Main> call, Response<Main> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            model = response.body();
                            for (int i = 0; i < model.getCompanies().size(); i++) {

                                for (int j = 0;  j< model.getCompanies().get(i).getDrivers().size(); j++){

                                    LatLng cars = new LatLng(model.getCompanies().get(i).getDrivers().get(j).getLat(),
                                            model.getCompanies().get(i).getDrivers().get(i).getLon());
                                    MarkerOptions markerOptions = new MarkerOptions();
                                    mMap.addMarker(markerOptions.position(cars)
                                            .flat(true)
                                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.car_icon)));

                                    Company info = model.getCompanies().get(0);
                                    info.setIcon(model.getCompanies().get(0).getIcon());
                                    info.setName(model.getCompanies().get(0).getName());
                                    InfoWindownAdapter infoWindownAdapter = new InfoWindownAdapter(getApplicationContext()
                                            ,model.getCompanies());
                                    mMap.setInfoWindowAdapter(infoWindownAdapter);

                                    Marker marker = mMap.addMarker(markerOptions);
                                    marker.setTag(info);
                                    marker.showInfoWindow();

                                }
                            }

                            Toast.makeText(getApplicationContext(), model.getCompanies().get(0).getName().toString(), Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(getApplicationContext(), "Сервер не отвечает", Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Main> call, Throwable throwable) {
                        Toast.makeText(getApplicationContext(), "onFailure", Toast.LENGTH_LONG).show();
                    }
                });

    }

}


