package com.example.kseniya.nearesttaxi.ui;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.ImageVideoBitmapDecoder;
import com.bumptech.glide.load.resource.gif.GifResourceDecoder;
import com.bumptech.glide.load.resource.gifbitmap.GifBitmapWrapperResourceDecoder;
import com.caverock.androidsvg.SVG;
import com.caverock.androidsvg.SVGParser;
import com.example.kseniya.nearesttaxi.R;
import com.example.kseniya.nearesttaxi.models.Company;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;
import com.squareup.picasso.Picasso;
public class InfoWindowAdapter implements GoogleMap.InfoWindowAdapter {
    Context context;
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
        TextView title = view.findViewById(R.id.title);
        TextView sms = view.findViewById(R.id.sms);
        final TextView phone = view.findViewById(R.id.phone);
        Company model = (Company) marker.getTag();
        if (model != null) {
            BitmapPool pool =  Glide.get(context).getBitmapPool();
            if (model.getName().equals("NambaTaxi")) {

                Glide.with(img.getContext())
                        .load(model.getIcon())
                        .asBitmap()
                        .override(50, 50)
                        .animate(android.R.anim.fade_in)
                        .imageDecoder(new SvgBitmapDecoder(pool)) // implements ResourceDecoder<InputStream, Bitmap>
                        .into(img);
            } else {
                Glide.with(img.getContext())
                        .load(model.getIcon())
                        .override(50, 50)
                        .into(img);
            }
            title.setText(model.getName().toString());
            sms.setText(model.getContacts().get(0).getContact().toString());
            phone.setText(model.getContacts().get(1).getContact().toString());
        }
        return view;
    }
}
