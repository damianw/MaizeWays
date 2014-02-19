package com.damianw.maizeways.android;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.damianw.maizeways.android.data.BusesResponse;
import com.damianw.maizeways.android.data.MBusResponse;
import com.damianw.maizeways.android.data.MBusTask;
import com.damianw.maizeways.android.data.RoutesResponse;
import com.damianw.maizeways.android.data.StopsResponse;
import com.damianw.maizeways.android.navigation.RoutesDrawerFragment;
import com.damianw.maizeways.android.navigation.StopsDrawerFragment;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends Activity {

    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private RoutesDrawerFragment mNavigationDrawerFragment;
    private StopsDrawerFragment mStopsDrawerFragment;

    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    private CharSequence mTitle;

    private ArrayList<Fragment> mFragments;

    private BusesResponse.Bus[] mBuses;
    private StopsResponse.Stop[] mStops;
    private RoutesResponse.Route[] mRoutes;

    private HashMap<Integer, BusesResponse.Bus> mBusMap;
    private HashMap<Integer, StopsResponse.Stop> mStopMap;
    private HashMap<Integer, RoutesResponse.Route> mRouteMap;

    private GoogleMap mMap;
    private HashMap<Integer, Marker> mBusMarkers;
    private LatLngBounds mVenue;

    private int mPadding;
    private int mBusMarkerSize;

    private Timer mRefreshTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mBuses = new BusesResponse.Bus[0];
        mStops = new StopsResponse.Stop[0];
        mRoutes = new RoutesResponse.Route[0];
        mBusMarkers = new HashMap<Integer, Marker>();

        mBusMap = new HashMap<Integer, BusesResponse.Bus>();
        mStopMap = new HashMap<Integer, StopsResponse.Stop>();
        mRouteMap = new HashMap<Integer, RoutesResponse.Route>();

        mPadding = 80 * getResources().getDisplayMetrics().densityDpi / 160;
        mBusMarkerSize = 20 * getResources().getDisplayMetrics().densityDpi / 160;

        mMap = ((MapFragment) getFragmentManager().findFragmentById(R.id.mbus_map_fragment)).getMap();

        mNavigationDrawerFragment = (RoutesDrawerFragment)
                getFragmentManager().findFragmentById(R.id.navigation_drawer);
        mStopsDrawerFragment = (StopsDrawerFragment)
                getFragmentManager().findFragmentById(R.id.stops_drawer);
        mTitle = getTitle();
        // Set up the drawers.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));
        mStopsDrawerFragment.setUp(
                R.id.stops_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));
        mNavigationDrawerFragment.setCallbacks(new RoutesDrawerCallback());
        mStopsDrawerFragment.setCallbacks(new StopsDrawerCallback());
    }

    @Override
    protected void onResume() {
        super.onResume();
        mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
        mMap.setMyLocationEnabled(true);
        loadData();
        TimerTask task = new TimerTask(){
            @Override
            public void run(){
                MainActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Log.d("MapRefresh", "The map data is being refreshed.");
                        loadFromResponse(BusesResponse.class);
                    }
                });
            }
        };
        mRefreshTimer = new Timer();
        mRefreshTimer.scheduleAtFixedRate(task, 0, 500);
    }

    public void loadData() {
        loadFromResponse(RoutesResponse.class);
        loadFromResponse(BusesResponse.class);
        loadFromResponse(StopsResponse.class);
    }

    private void loadFromResponse(final Class<? extends MBusResponse> responseType) {
        AsyncTask task = new MBusTask(responseType, this) {
            @Override
            protected void onPostExecute(MBusResponse mBusResponse) {
                // TODO: check for null pointer, etc
                if (mBusResponse instanceof BusesResponse) {
                    mBuses = ((BusesResponse)mBusResponse).response;
                    mBusMap = new HashMap<Integer, BusesResponse.Bus>();
                    for (BusesResponse.Bus bus : mBuses) {
                        mBusMap.put(bus.id, bus);
                    }
                    initBuses();
                }
                else if (mBusResponse instanceof StopsResponse) {
                    mStops = ((StopsResponse)mBusResponse).response;
                    mStopMap = new HashMap<Integer, StopsResponse.Stop>();
                    for (StopsResponse.Stop stop : mStops) {
                        mStopMap.put(stop.id, stop);
                    }
                    Arrays.sort(mStops);
                    initStops();
                }
                else if (mBusResponse instanceof RoutesResponse) {
                    mRoutes = ((RoutesResponse)mBusResponse).response;
                    mRouteMap = new HashMap<Integer, RoutesResponse.Route>();
                    for (RoutesResponse.Route route : mRoutes) {
                        mRouteMap.put(route.id, route);
                    }
                    Arrays.sort(mRoutes);
                    initRoutes();
                }
            }
        };
        task.execute();
    }

    private void initBuses() {
        for (BusesResponse.Bus bus : mBuses) {
            LatLng coordinates = new LatLng(bus.latitude, bus.longitude);
            if (mBusMarkers.containsKey(bus.id)) {
                mBusMarkers.get(bus.id).setPosition(coordinates);
            }
            else {
                MarkerOptions options = new MarkerOptions()
                        .position(coordinates)
                        .title(bus.route_name)
                                // TODO: add useful stuff here
                        .icon(getBusIcon(mRouteMap.get(bus.route).color));
                mBusMarkers.put(bus.id, mMap.addMarker(options));
            }
        }
    }

    private BitmapDescriptor getBusIcon(String color) {
        Bitmap mDotMarkerBitmap = Bitmap.createBitmap(mBusMarkerSize, mBusMarkerSize, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(mDotMarkerBitmap);
//        Drawable shape = getResources().getDrawable(R.drawable.bus_icon_drawable);
        ShapeDrawable shape = new ShapeDrawable(new OvalShape());
        shape.getPaint().setColor(Color.parseColor(color));
        shape.getPaint().setStyle(Paint.Style.FILL_AND_STROKE);
        shape.getPaint().setStrokeWidth(2);
        shape.getPaint().setAntiAlias(true);
        shape.setBounds(0, 0, mDotMarkerBitmap.getWidth(), mDotMarkerBitmap.getHeight());
        shape.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(mDotMarkerBitmap);
    }

    private void initRoutes() {
        mNavigationDrawerFragment.setRoutes(mRoutes);
    }

    private void initStops() {
        LatLngBounds.Builder builder = LatLngBounds.builder();
        for (StopsResponse.Stop stop: mStops) {
            LatLng coordinates = new LatLng(stop.latitude, stop.longitude);
            builder.include(coordinates);
        }
        if (mStops.length != 0) {
            mVenue = builder.build();
            mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(mVenue, mPadding));
        }
        mStopsDrawerFragment.setStops(mStops);
    }

    public void restoreActionBar() {
        ActionBar actionBar = getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            // Only show items in the action bar relevant to this screen
            // if the drawer is not showing. Otherwise, let the drawer
            // decide what to show in the action bar.
            getMenuInflater().inflate(R.menu.main, menu);
            restoreActionBar();
            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public class RoutesDrawerCallback implements RoutesDrawerFragment.NavigationDrawerCallbacks {
        @Override
        public void onNavigationDrawerItemSelected(int position) {

        }
    }

    public class StopsDrawerCallback implements StopsDrawerFragment.NavigationDrawerCallbacks {
        @Override
        public void onNavigationDrawerItemSelected(int position) {

        }
    }

}
