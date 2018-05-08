package com.example.kseniya.nearesttaxi.ui;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kseniya.nearesttaxi.R;
import com.example.kseniya.nearesttaxi.models.Company;
import com.example.kseniya.nearesttaxi.models.Main;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;
import com.squareup.picasso.Picasso;

import java.util.List;

public class InfoWindowAdapter implements GoogleMap.InfoWindowAdapter {
    Context context;
    List<Company> list;



    public InfoWindowAdapter(Context ctx) {
        this.context = ctx;


    }


    @Override
    public View getInfoWindow(Marker marker) {

        return null;
    }

    @Override
    public View getInfoContents(Marker marker) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.map_custom_infowindow, null);
        ImageView img = view.findViewById(R.id.image);
        TextView title =  view.findViewById(R.id.title);


        Company model = (Company) marker.getTag();

        if (model != null) {
            Picasso.get().load(model.getIcon().toString()).into(img);
            title.setText(model.getName().toString());
        }

        return view;
    }
}
