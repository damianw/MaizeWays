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
import com.damianw.maizeways.android.data.RoutesResponse;
import com.damianw.maizeways.android.data.StopsResponse;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

public class MBusMapFragment extends MapFragment {

    private View mView;
    private int mPadding;
    private GoogleMap mMap;
    private Activity mActivity;
    private LatLngBounds mVenue;
    private ArrayList<Marker> mMarkers;

    private BusesResponse.Bus[] mBuses;
    private StopsResponse.Stop[] mStops;
    private RoutesResponse.Route[] mRoutes;

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

        mBuses = new BusesResponse.Bus[0];
        mStops = new StopsResponse.Stop[0];
        mRoutes = new RoutesResponse.Route[0];

        return mView;
    }

    public void loadData() {
        loadFromResponse(BusesResponse.class);
        loadFromResponse(StopsResponse.class);
        loadFromResponse(RoutesResponse.class);
    }

    private void loadFromResponse(final Class<? extends MBusResponse> responseType) {
        AsyncTask task = new MBusTask(responseType, mActivity) {
            @Override
            protected void onPostExecute(MBusResponse mBusResponse) {
                // TODO: check for null pointer, etc
                if (mBusResponse instanceof BusesResponse) {
                    mBuses = ((BusesResponse)mBusResponse).response;
                    initBuses();
                }
                else if (mBusResponse instanceof StopsResponse) {
                    mStops = ((StopsResponse)mBusResponse).response;
                    initStops();
                }
                else if (mBusResponse instanceof RoutesResponse) {
                    mRoutes = ((RoutesResponse)mBusResponse).response;
                    initRoutes();
                }
            }
        };
        task.execute();
    }

    private void initBuses() {
        mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
        mMap.setMyLocationEnabled(true);
        for (BusesResponse.Bus bus : mBuses) {
            LatLng coordinates = new LatLng(bus.latitude, bus.longitude);
            MarkerOptions options = new MarkerOptions()
                    .position(coordinates)
                    .title(bus.route_name)
                            // TODO: add useful stuff here
                    .snippet("wat");
            mMarkers.add(mMap.addMarker(options));
        }
    }

    private void initRoutes() {

    }

    private void initStops() {
        mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
        mMap.setMyLocationEnabled(true);
        LatLngBounds.Builder builder = LatLngBounds.builder();
        for (StopsResponse.Stop stop: mStops) {
            LatLng coordinates = new LatLng(stop.latitude, stop.longitude);
//            MarkerOptions options = new MarkerOptions()
//                    .position(coordinates)
//                    .title(stop.human_name)
//                            // TODO: add useful stuff here
//                    .snippet(stop.unique_name);
//            mMarkers.add(mMap.addMarker(options));
            builder.include(coordinates);
        }
        if (mStops.length != 0) {
            mVenue = builder.build();
            mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(mVenue, mPadding));
        }
    }
}
