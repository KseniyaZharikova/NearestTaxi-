package com.example.kseniya.nearesttaxi.ui;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.kseniya.nearesttaxi.R;
import com.example.kseniya.nearesttaxi.models.Company;
import com.example.kseniya.nearesttaxi.models.Main;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;
import com.squareup.picasso.Picasso;

import java.util.List;

public class InfoWindownAdapter implements GoogleMap.InfoWindowAdapter {
    Context context;
    List<Company> companyList;


    public InfoWindownAdapter(Context ctx, List<Company> list) {
        this.context = ctx;
        this.companyList=list;

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
        Picasso.get().load(model.getIcon()).into(img);
        title.setText(model.getName().toString());



        return view;
    }
}
