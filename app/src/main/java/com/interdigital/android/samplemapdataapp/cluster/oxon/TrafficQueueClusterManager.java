package com.interdigital.android.samplemapdataapp.cluster.oxon;

import android.content.Context;

import com.google.android.gms.maps.GoogleMap;
import com.interdigital.android.samplemapdataapp.cluster.BaseClusterManager;

public class TrafficQueueClusterManager extends BaseClusterManager<TrafficQueueClusterItem> {

    public TrafficQueueClusterManager(Context context, GoogleMap googleMap) {
        super(context, googleMap);
    }

}
