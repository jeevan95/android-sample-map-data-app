/* Copyright 2016 InterDigital Communications, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * You may not use this file except in compliance with the License.
 *
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.interdigital.android.samplemapdataapp.layer.northants;

import android.content.Context;
import android.util.Log;

import com.google.android.gms.maps.GoogleMap;
import com.interdigital.android.samplemapdataapp.cluster.BaseClusterManager;
import com.interdigital.android.samplemapdataapp.cluster.BaseClusterRenderer;
import com.interdigital.android.samplemapdataapp.cluster.northants.RoadworksClusterItem;
import com.interdigital.android.samplemapdataapp.cluster.northants.RoadworksClusterManager;
import com.interdigital.android.samplemapdataapp.cluster.northants.RoadworksClusterRenderer;
import com.interdigital.android.samplemapdataapp.layer.ClusterBaseLayer;

import net.uk.onetransport.android.county.northants.provider.NorthantsContentHelper;
import net.uk.onetransport.android.county.northants.roadworks.Roadworks;

public class NorthantsRoadworks extends ClusterBaseLayer<RoadworksClusterItem> {

    public NorthantsRoadworks(Context context, GoogleMap googleMap) {
        super(context, googleMap);
    }

    @Override
    public void load() throws Exception {
        Roadworks[] roadworkses = NorthantsContentHelper.getLatestRoadworks(getContext());
        double[] coords = new double[roadworkses.length];
        int c = 0;
        for (int i = roadworkses.length - 1; i >= 0; i--) {
            // Prevent concurrent locations.
            double coord = roadworkses[i].getLatitude() * 360 + roadworkses[i].getLongitude();
            boolean found = false;
            for (int j = 0; j < c; j++) {
                if (coords[j] == coord) {
                    found = true;
                }
            }
            if (c < MAX_ITEMS && !found) {
                if (isInDate(roadworkses[i].getStartOfPeriod())
                        || isInDate(roadworkses[i].getEndOfPeriod())
                        || isInDate(roadworkses[i].getOverallStartTime())
                        || isInDate(roadworkses[i].getOverallEndTime())) {
                    RoadworksClusterItem roadworksClusterItem = new RoadworksClusterItem(roadworkses[i]);
                    getClusterItems().add(roadworksClusterItem);
                    coords[c++] = coord;
                }
            }
        }
        Log.i("NorthantsRoadworks", "Found " + roadworkses.length
                + ", discarded " + (roadworkses.length - getClusterItems().size()));
    }

    @Override
    protected BaseClusterManager<RoadworksClusterItem> newClusterManager() {
        return new RoadworksClusterManager(getContext(), getGoogleMap());
    }

    @Override
    protected BaseClusterRenderer<RoadworksClusterItem> newClusterRenderer() {
        return new RoadworksClusterRenderer(getContext(), getGoogleMap(), getClusterManager());
    }
}
