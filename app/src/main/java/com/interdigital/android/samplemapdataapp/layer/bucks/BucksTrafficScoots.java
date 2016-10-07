package com.interdigital.android.samplemapdataapp.layer.bucks;

import android.content.Context;

import com.google.android.gms.maps.GoogleMap;
import com.interdigital.android.samplemapdataapp.cluster.BaseClusterManager;
import com.interdigital.android.samplemapdataapp.cluster.BaseClusterRenderer;
import com.interdigital.android.samplemapdataapp.cluster.bucks.TrafficScootClusterItem;
import com.interdigital.android.samplemapdataapp.cluster.bucks.TrafficScootClusterManager;
import com.interdigital.android.samplemapdataapp.cluster.bucks.TrafficScootClusterRenderer;
import com.interdigital.android.samplemapdataapp.layer.ClusterBaseLayer;

import net.uk.onetransport.android.county.bucks.provider.BucksContentHelper;
import net.uk.onetransport.android.county.bucks.trafficscoot.TrafficScoot;

public class BucksTrafficScoots extends ClusterBaseLayer<TrafficScootClusterItem> {

    public BucksTrafficScoots(Context context, GoogleMap googleMap) {
        super(context, googleMap);
    }

    @Override
    public void load() throws Exception {
        TrafficScoot[] trafficScoots = BucksContentHelper.getLatestTrafficScoots(getContext());
        for (TrafficScoot trafficscoot : trafficScoots) {
            TrafficScootClusterItem trafficScootClusterItem = new TrafficScootClusterItem(trafficscoot);
            if (trafficScootClusterItem.shouldAdd()) {
                getClusterItems().add(trafficScootClusterItem);
            }
        }
    }

    @Override
    protected BaseClusterManager<TrafficScootClusterItem> newClusterManager() {
        return new TrafficScootClusterManager(getContext(), getGoogleMap());
    }

    @Override
    protected BaseClusterRenderer<TrafficScootClusterItem> newClusterRenderer() {
        return new TrafficScootClusterRenderer(getContext(), getGoogleMap(), getClusterManager());
    }
}