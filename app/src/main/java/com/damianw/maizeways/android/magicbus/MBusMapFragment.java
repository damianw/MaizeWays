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
import com.damianw.maizeways.android.data.MBusTask;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

public class MBusMapFragment extends MapFragment {

    private static String API_URL = "http://mbus.pts.umich.edu/api/v0/";
    private static String BUSES_ENDPOINT = "buses/";

    private View mView;
    private int mPadding;
    private GoogleMap mMap;
    private Activity mActivity;
    private LatLngBounds mVenue;
    private ArrayList<Marker> mMarkers;
    private BusesResponse.Bus[] mBuses;

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
        return mView;
    }

    private void loadData() {
        AsyncTask task = new MBusTask<BusesResponse>(BusesResponse.class) {
            @Override
            protected void onPostExecute(BusesResponse busesResponse) {
                // TODO: check for null pointer, etc
                mBuses = busesResponse.response;
                initMap();
            }
        };
        task.execute(new String[] {API_URL + BUSES_ENDPOINT} );
    }

    private void initMap() {
        mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
        mMap.setMyLocationEnabled(true);
        LatLngBounds.Builder builder = LatLngBounds.builder();
        for (BusesResponse.Bus bus : mBuses) {
            LatLng coordinates = new LatLng(bus.latitude, bus.longitude);
            MarkerOptions options = new MarkerOptions()
                    .position(coordinates)
                    .title(bus.route_name)
                    // TODO: add useful stuff here
                    .snippet("wat");
            mMarkers.add(mMap.addMarker(options));
            builder.include(coordinates);
        }
        mVenue = builder.build();
        mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(mVenue, mPadding));
    }

}
