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
package com.interdigital.android.samplemapdataapp.layer.oxon;

import android.content.Context;
import android.util.Log;

import com.google.android.gms.maps.GoogleMap;
import com.interdigital.android.samplemapdataapp.cluster.BaseClusterManager;
import com.interdigital.android.samplemapdataapp.cluster.BaseClusterRenderer;
import com.interdigital.android.samplemapdataapp.cluster.oxon.TrafficQueueClusterItem;
import com.interdigital.android.samplemapdataapp.cluster.oxon.TrafficQueueClusterManager;
import com.interdigital.android.samplemapdataapp.cluster.oxon.TrafficQueueClusterRenderer;
import com.interdigital.android.samplemapdataapp.layer.ClusterBaseLayer;

import net.uk.onetransport.android.county.oxon.provider.OxonContentHelper;
import net.uk.onetransport.android.county.oxon.trafficqueue.TrafficQueue;

public class OxonTrafficQueues extends ClusterBaseLayer<TrafficQueueClusterItem> {

    public OxonTrafficQueues(Context context, GoogleMap googleMap) {
        super(context, googleMap);
    }

    @Override
    public void load() throws Exception {
        TrafficQueue[] trafficQueues = OxonContentHelper.getLatestTrafficQueues(getContext());
        int count = 0;
        for (TrafficQueue trafficQueue : trafficQueues) {
            if (count < MAX_ITEMS) {
                TrafficQueueClusterItem trafficQueueClusterItem = new TrafficQueueClusterItem(trafficQueue);
                if (isInDate(trafficQueue.getTime()) && trafficQueueClusterItem.shouldAdd()) {
                    getClusterItems().add(trafficQueueClusterItem);
                    count++;
                }
            }
        }
        Log.i("OxonTrafficQueues", "Found " + trafficQueues.length
                + ", discarded " + (trafficQueues.length - getClusterItems().size()));
    }

    @Override
    protected BaseClusterManager<TrafficQueueClusterItem> newClusterManager() {
        return new TrafficQueueClusterManager(getContext(), getGoogleMap());
    }

    @Override
    protected BaseClusterRenderer<TrafficQueueClusterItem> newClusterRenderer() {
        return new TrafficQueueClusterRenderer(getContext(), getGoogleMap(), getClusterManager());
    }
}
