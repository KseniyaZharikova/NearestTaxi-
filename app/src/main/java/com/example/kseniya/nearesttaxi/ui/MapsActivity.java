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

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private RetrofitService service;
    private Marker marker;
    double lat;
    double lon;
    Main main;


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
                                    MarkerOptions markerOptions = new MarkerOptions();
                                    mMap.addMarker(markerOptions.position(cars)
                                            .flat(true)
                                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.car_icon)));
                                }
                                company.setName(company.getName().toString());

                                InfoWindownAdapter infoWindownAdapter = new InfoWindownAdapter(getApplicationContext()
                                        ,main.getCompanies());
                                mMap.setInfoWindowAdapter(infoWindownAdapter);
                                marker.setTag(company);
                                marker.showInfoWindow();
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
    }
}
//        Company info = model.getCompanies().get(i);
//        info.setIcon(model.getCompanies().get(i).getIcon());
//        info.setName(model.getCompanies().get(i).getName());

//        InfoWindownAdapter infoWindownAdapter = new InfoWindownAdapter(getApplicationContext()
//        ,model.getCompanies());
//        mMap.setInfoWindowAdapter(infoWindownAdapter);
//        Marker marker = mMap.addMarker(markerOptions.position(cars));
//        marker.setTag(info);
//        marker.showInfoWindow();

