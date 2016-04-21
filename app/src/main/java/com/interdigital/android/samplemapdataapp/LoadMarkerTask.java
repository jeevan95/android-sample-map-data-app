package com.interdigital.android.samplemapdataapp;

import android.os.AsyncTask;
import android.text.TextUtils;
import android.view.View;
import android.widget.ProgressBar;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.interdigital.android.dougal.resource.Container;
import com.interdigital.android.dougal.resource.ContentInstance;
import com.interdigital.android.samplemapdataapp.json.PredefinedLocation;
import com.interdigital.android.samplemapdataapp.json.items.CaCarParkItem;
import com.interdigital.android.samplemapdataapp.json.items.CaTrafficFlowItem;
import com.interdigital.android.samplemapdataapp.json.items.CaVmsItem;
import com.interdigital.android.samplemapdataapp.json.items.Item;

import java.util.ArrayList;
import java.util.HashMap;

public class LoadMarkerTask extends AsyncTask<Void, Integer, Void> {

    private GoogleMap googleMap;
    private ArrayList<Item> itemList;
    private HashMap<Marker, Item> markerMap = new HashMap<>();
    private ProgressBar progressBar;
    private Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
    private HashMap<String, PredefinedLocation> predefinedLocationMap = new HashMap<>();

    public LoadMarkerTask(GoogleMap googleMap, ArrayList<Item> itemList,
                          HashMap<Marker, Item> markerMap, ProgressBar progressBar) {
        this.googleMap = googleMap;
        this.itemList = itemList;
        this.markerMap = markerMap;
        this.progressBar = progressBar;
    }

    @Override
    protected Void doInBackground(Void... voids) {
        try {
            addWorldSensing();
            loadPredefinedLocations();
            loadCaVms();
            publishProgress(66);
            loadCaCarParks();
            publishProgress(83);
            loadCaTrafficFlow();
            publishProgress(100);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
        progressBar.setProgress(values[0]);
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        for (Item item : itemList) {
            item.addMarker(googleMap, markerMap);
        }
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                itemList.get(0).getMarker().getPosition(), 12));
    }

    private void addWorldSensing() {
        // TODO Add loading time on progress bar.
        itemList.add(new WorldsensingItem(0));
        itemList.add(new WorldsensingItem(1));
        itemList.add(new WorldsensingItem(2));
    }

    private void loadPredefinedLocations() throws Exception {
        PredefinedLocation[][] predefinedLocations = new PredefinedLocation[3][];
        ContentInstance contentInstance = Container.retrieveLatest(CseDetails.aeId,
                CseDetails.BASE_URL, "BCCFeedImportPredefinedSectionLocation/All",
                CseDetails.USER_NAME, CseDetails.PASSWORD);
        predefinedLocations[0] = gson.fromJson(contentInstance.getContent(),
                PredefinedLocation[].class);
        publishProgress(16);
        // TODO This feed is currently broken.
        // TODO But we don't think there is much in it that we need.
//        contentInstance = Container.retrieveLatest(CseDetails.aeId,
//                CseDetails.BASE_URL, "BCCFeedImportPredefinedTrLocation/All",
//                CseDetails.USER_NAME, CseDetails.PASSWORD);
//        predefinedLocations[1] = gson.fromJson(contentInstance.getContent(),
//                PredefinedLocation[].class);
//        publishProgress(--);
        contentInstance = Container.retrieveLatest(CseDetails.aeId,
                CseDetails.BASE_URL, "BCCFeedImportPredefinedVmsLocation/All",
                CseDetails.USER_NAME, CseDetails.PASSWORD);
        predefinedLocations[1] = gson.fromJson(contentInstance.getContent(),
                PredefinedLocation[].class);
        publishProgress(33);
        contentInstance = Container.retrieveLatest(CseDetails.aeId,
                CseDetails.BASE_URL, "BCCFeedImportPredefinedLinkLocation/All",
                CseDetails.USER_NAME, CseDetails.PASSWORD);
        predefinedLocations[2] = gson.fromJson(contentInstance.getContent(),
                PredefinedLocation[].class);
        publishProgress(49);
        for (int i = 0; i < predefinedLocations.length; i++) {
            for (int j = 0; j < predefinedLocations[i].length; j++) {
                predefinedLocationMap.put(predefinedLocations[i][j].locationId, predefinedLocations[i][j]);
            }
        }
    }

    private void loadCaVms() throws Exception {
        ContentInstance contentInstance = Container.retrieveLatest(CseDetails.aeId,
                CseDetails.BASE_URL, "BCCSignSettingFeedImport/All",
                CseDetails.USER_NAME, CseDetails.PASSWORD);
        CaVmsItem[] caVmsItems = gson.fromJson(contentInstance.getContent(), CaVmsItem[].class);
        for (CaVmsItem caVmsItem : caVmsItems) {
            caVmsItem.updateLocation(predefinedLocationMap);
            if (caVmsItem.shouldAdd()) {
                itemList.add(caVmsItem);
            }
        }
    }

    private void loadCaCarParks() throws Exception {
        ContentInstance contentInstance = Container.retrieveLatest(CseDetails.aeId,
                CseDetails.BASE_URL, "BCCCarPark2FeedImport/All",
                CseDetails.USER_NAME, CseDetails.PASSWORD);
        String content = contentInstance.getContent();
        CaCarParkItem[] caCarParkItems = gson.fromJson(content, CaCarParkItem[].class);
        for (CaCarParkItem caCarParkItem : caCarParkItems) {
            if (caCarParkItem.shouldAdd()) {
                itemList.add(caCarParkItem);
            }
        }
    }

    private void loadCaTrafficFlow() throws Exception {
        ContentInstance contentInstance = Container.retrieveLatest(CseDetails.aeId,
                CseDetails.BASE_URL, "BCCTrafficFlowFeedImport/All",
                CseDetails.USER_NAME, CseDetails.PASSWORD);
        String content = contentInstance.getContent();
        CaTrafficFlowItem[] caTrafficFlowItems = gson.fromJson(content, CaTrafficFlowItem[].class);
        HashMap<String, CaTrafficFlowItem> flowMap = new HashMap<>();
        for (CaTrafficFlowItem caTrafficFlowItem : caTrafficFlowItems) {
            String locationReference = caTrafficFlowItem.getLocationReference();
            if (!flowMap.containsKey(locationReference)) {
                flowMap.put(locationReference, caTrafficFlowItem);
            } else {
                CaTrafficFlowItem existingCaTrafficFlowItem = flowMap.get(locationReference);
                if (!TextUtils.isEmpty(caTrafficFlowItem.getAverageVehicleSpeed())) {
                    existingCaTrafficFlowItem.setAverageVehicleSpeed(caTrafficFlowItem.getAverageVehicleSpeed());
                }
                if (!TextUtils.isEmpty(caTrafficFlowItem.getFreeFlowSpeed())) {
                    existingCaTrafficFlowItem.setFreeFlowSpeed(caTrafficFlowItem.getFreeFlowSpeed());
                }
                if (!TextUtils.isEmpty(caTrafficFlowItem.getFreeFlowTravelTime())) {
                    existingCaTrafficFlowItem.setFreeFlowTravelTime(caTrafficFlowItem.getFreeFlowTravelTime());
                }
                if (!TextUtils.isEmpty(caTrafficFlowItem.getTravelTime())) {
                    existingCaTrafficFlowItem.setTravelTime(caTrafficFlowItem.getTravelTime());
                }
                if (!TextUtils.isEmpty(caTrafficFlowItem.getVehicleFlow())) {
                    existingCaTrafficFlowItem.setVehicleFlow(caTrafficFlowItem.getVehicleFlow());
                }
            }
        }
        for (String key : flowMap.keySet()) {
            CaTrafficFlowItem caTrafficFlowItem = flowMap.get(key);
            caTrafficFlowItem.updateLocation(predefinedLocationMap);
            if (caTrafficFlowItem.shouldAdd()) {
                itemList.add(caTrafficFlowItem);
            }
        }
    }
}