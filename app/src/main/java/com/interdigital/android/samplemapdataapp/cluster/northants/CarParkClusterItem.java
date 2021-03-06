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
package com.interdigital.android.samplemapdataapp.cluster.northants;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterItem;

import net.uk.onetransport.android.county.northants.carparks.CarPark;

public class CarParkClusterItem implements ClusterItem {

    private CarPark carPark;
    private LatLng position;

    public CarParkClusterItem(CarPark carPark) {
        this.carPark = carPark;
        position = new LatLng(carPark.getLatitude(), carPark.getLongitude());
    }

    @Override
    public LatLng getPosition() {
        return position;
    }

    public CarPark getCarPark() {
        return carPark;
    }
}
