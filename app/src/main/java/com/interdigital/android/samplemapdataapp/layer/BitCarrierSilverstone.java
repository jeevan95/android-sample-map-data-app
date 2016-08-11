package com.interdigital.android.samplemapdataapp.layer;

import android.content.Context;
import android.database.Cursor;

import com.google.android.gms.maps.GoogleMap;
import com.interdigital.android.samplemapdataapp.polyline.BitCarrierSketchPolyline;

import net.uk.onetransport.android.modules.bitcarriersilverstone.provider.BcsContentHelper;

public class BitCarrierSilverstone extends PolylineBaseLayer {

    public BitCarrierSilverstone(Context context, GoogleMap googleMap) {
        super(context, googleMap);
    }

    @Override
    public void load() {
        Cursor cursor = BcsContentHelper.getSketches(getContext());
        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                BitCarrierSketchPolyline bcsp = new BitCarrierSketchPolyline(cursor);
                getBasePolylines().add(bcsp);
                cursor.moveToNext();
            }
        }
        cursor.close();
    }
}