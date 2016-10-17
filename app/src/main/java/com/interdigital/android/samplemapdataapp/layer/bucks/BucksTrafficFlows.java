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
package com.interdigital.android.samplemapdataapp.layer.bucks;

import android.content.Context;
import android.util.Log;

import com.google.android.gms.maps.GoogleMap;
import com.interdigital.android.samplemapdataapp.cluster.BaseClusterManager;
import com.interdigital.android.samplemapdataapp.cluster.BaseClusterRenderer;
import com.interdigital.android.samplemapdataapp.cluster.bucks.TrafficFlowClusterItem;
import com.interdigital.android.samplemapdataapp.cluster.bucks.TrafficFlowClusterManager;
import com.interdigital.android.samplemapdataapp.cluster.bucks.TrafficFlowClusterRenderer;
import com.interdigital.android.samplemapdataapp.layer.ClusterBaseLayer;

import net.uk.onetransport.android.county.bucks.provider.BucksContentHelper;
import net.uk.onetransport.android.county.bucks.trafficflow.TrafficFlow;

public class BucksTrafficFlows extends ClusterBaseLayer<TrafficFlowClusterItem> {

    public BucksTrafficFlows(Context context, GoogleMap googleMap) {
        super(context, googleMap);
    }

    @Override
    public void load() throws Exception {
        TrafficFlow[] trafficFlows = BucksContentHelper.getLatestTrafficFlows(getContext());
        int count = 0;
        for (TrafficFlow trafficFlow : trafficFlows) {
            if (count < MAX_ITEMS) {
                TrafficFlowClusterItem trafficFlowClusterItem = new TrafficFlowClusterItem(trafficFlow);
                if (isInDate(trafficFlow.getTime())
                        && trafficFlowClusterItem.shouldAdd()) {
                    getClusterItems().add(trafficFlowClusterItem);
                    count++;
                }
            }
        }
        Log.i("BucksTrafficFlows", "Found " + trafficFlows.length
                + ", discarded " + (trafficFlows.length - getClusterItems().size()));
    }

    @Override
    protected BaseClusterManager<TrafficFlowClusterItem> newClusterManager() {
        return new TrafficFlowClusterManager(getContext(), getGoogleMap());
    }

    @Override
    protected BaseClusterRenderer<TrafficFlowClusterItem> newClusterRenderer() {
        return new TrafficFlowClusterRenderer(getContext(), getGoogleMap(), getClusterManager());
    }
}
