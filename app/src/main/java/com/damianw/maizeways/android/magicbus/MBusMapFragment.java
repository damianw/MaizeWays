package com.damianw.maizeways.android.magicbus;

/**
 * Created by damian on 2/18/14.
 */

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;

import com.damianw.maizeways.android.data.BusesResponse;
import com.damianw.maizeways.android.data.MBusResponse;
import com.damianw.maizeways.android.data.MBusTask;
import com.damianw.maizeways.android.data.StopsResponse;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class MBusMapFragment extends MapFragment {

    private View mView;
    private int mPadding;
    private GoogleMap mMap;
    private Activity mActivity;
    private LatLngBounds mVenue;
    private ArrayList<Marker> mMarkers;

    private HashMap<Class<? extends MBusResponse>, List> mData = new HashMap<Class<? extends MBusResponse>, List>();
//    private BusesResponse.Bus[] mBuses;

    public MBusMapFragment() { super(); }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = super.onCreateView(inflater, container, savedInstanceState);
        mActivity = getActivity();
        mMap = getMap();
        mMarkers = new ArrayList();
        mPadding = 80 * mActivity.getResources().getDisplayMetrics().densityDpi / 160;
        mView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @SuppressLint("NewApi") // We check which build version we are using.
            @Override
            public void onGlobalLayout() {
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
                    mView.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                } else {
                    mView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                }
                loadData();
            }
        });

        mData.put(BusesResponse.class, new ArrayList());
        mData.put(StopsResponse.class, new ArrayList());

        return mView;
    }

    public void loadData() {
        loadFromResponse(BusesResponse.class);
        loadFromResponse(StopsResponse.class);
    }

    private void loadFromResponse(final Class<? extends MBusResponse> responseType) {
        AsyncTask task = new MBusTask(responseType, mActivity) {
            @Override
            protected void onPostExecute(MBusResponse mBusResponse) {
                // TODO: check for null pointer, etc
                mData.put(responseType, Arrays.asList(responseType.cast(mBusResponse).response));
                presentData(responseType);
            }
        };
        task.execute();
    }

    private void presentData(final Class<? extends MBusResponse> responseType) {
        mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
        mMap.setMyLocationEnabled(true);
        for (BusesResponse.Bus bus : getBuses()) {
            LatLng coordinates = new LatLng(bus.latitude, bus.longitude);
            MarkerOptions options = new MarkerOptions()
                    .position(coordinates)
                    .title(bus.route_name)
                    // TODO: add useful stuff here
                    .snippet("wat");
            mMarkers.add(mMap.addMarker(options));
        }
        List<StopsResponse.Stop> stops = getStops();
        LatLngBounds.Builder builder = LatLngBounds.builder();
        for (StopsResponse.Stop stop: stops) {
            LatLng coordinates = new LatLng(stop.latitude, stop.longitude);
            MarkerOptions options = new MarkerOptions()
                    .position(coordinates)
                    .title(stop.human_name)
                            // TODO: add useful stuff here
                    .snippet(stop.unique_name);
            mMarkers.add(mMap.addMarker(options));
            builder.include(coordinates);
        }
        if (!stops.isEmpty()) {
            mVenue = builder.build();
            mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(mVenue, mPadding));
        }
    }

    public List<StopsResponse.Stop> getStops() {
        return (List<StopsResponse.Stop>)(mData.get(StopsResponse.class));
    }

    public List<BusesResponse.Bus> getBuses() {
        return (List<BusesResponse.Bus>)(mData.get(BusesResponse.class));
    }

}
