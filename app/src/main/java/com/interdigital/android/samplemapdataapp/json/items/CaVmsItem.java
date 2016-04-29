package com.interdigital.android.samplemapdataapp.json.items;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v4.view.GravityCompat;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.interdigital.android.samplemapdataapp.R;
import com.interdigital.android.samplemapdataapp.json.PredefinedLocation;

import java.util.HashMap;

public class CaVmsItem extends Item {

    @Expose
    @SerializedName("locationReference")
    private String locationReference;
    @Expose
    @SerializedName("numberOfCharacters")
    private String numberOfCharacters;
    @Expose
    @SerializedName("numberOfRows")
    private String numberOfRows;
    @Expose
    @SerializedName("vmsLegends")
    private String[] vmsLegends;
    @Expose
    @SerializedName("vmsType")
    private String vmsType;

    public CaVmsItem(){
        setType(TYPE_VMS);
    }

    @Override
    public boolean shouldAdd() {
        if (vmsLegends == null || vmsLegends.length == 0) {
            return false;
        }
        if (getLatLng().latitude == 0 && getLatLng().longitude == 0) {
            return false;
        }
        for (String line : vmsLegends) {
            if (line.trim().length() > 0) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void addMarker(GoogleMap googleMap, HashMap<Marker, Item> markerMap) {
        setMarker(googleMap.addMarker(
                new MarkerOptions()
                        .title(getTitle())
                        .position(getLatLng())
                        .anchor(0.5f, 0.8f)
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.vm_sign_icon))));
        markerMap.put(getMarker(), this);
    }

    @Override
    public void updateLocation(HashMap<String, PredefinedLocation> predefinedLocationMap) {
        if (predefinedLocationMap.containsKey(locationReference)) {
            PredefinedLocation predefinedLocation = predefinedLocationMap.get(locationReference);
            setLatLng(new LatLng(
                    Double.valueOf(predefinedLocation.getLatitude()),
                    Double.valueOf(predefinedLocation.getLongitude())));
            if (!TextUtils.isEmpty(predefinedLocation.getDescriptor())) {
                locationReference = predefinedLocation.getDescriptor();
            } else if (!TextUtils.isEmpty(predefinedLocation.getToDescriptor())) {
                locationReference = predefinedLocation.getToDescriptor();
            } else if (!TextUtils.isEmpty(predefinedLocation.getFromDescriptor())) {
                locationReference = predefinedLocation.getFromDescriptor();
            }
        } else {
            setLatLng(new LatLng(0, 0));
        }
    }

    @Override
    public View getInfoContents(Context context) {
        LinearLayout linearLayout = new LinearLayout(context);
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        TextView signTextView = new TextView(context);
        signTextView.setTextColor(0xff000000);
        signTextView.setGravity(Gravity.CENTER_HORIZONTAL);
        signTextView.setTypeface(Typeface.DEFAULT_BOLD);
        StringBuffer buf = new StringBuffer();
        for (String line : vmsLegends) {
            buf.append(line.trim());
            if (line.trim().length() > 0) {
                buf.append("\n");
            }
        }
        signTextView.setText(buf.toString().trim());
        linearLayout.addView(signTextView, new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        TextView nameTextView = new TextView(context);
        nameTextView.setText(locationReference);
        nameTextView.setTextColor(0xff808080);
        nameTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
        nameTextView.setGravity(GravityCompat.END);
        linearLayout.addView(nameTextView, new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        return linearLayout;
    }
}
