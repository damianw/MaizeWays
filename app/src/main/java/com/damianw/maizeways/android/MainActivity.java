package com.damianw.maizeways.android;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

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
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.Arrays;

public class MainActivity extends Activity
        implements RoutesDrawerFragment.NavigationDrawerCallbacks {

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

    private GoogleMap mMap;
    private ArrayList<Marker> mMarkers;
    private LatLngBounds mVenue;

    private int mPadding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mBuses = new BusesResponse.Bus[0];
        mStops = new StopsResponse.Stop[0];
        mRoutes = new RoutesResponse.Route[0];
        mMarkers = new ArrayList();

        mPadding = 80 * getResources().getDisplayMetrics().densityDpi / 160;

        mMap = ((MapFragment) getFragmentManager().findFragmentById(R.id.mbus_map_fragment)).getMap();

        mNavigationDrawerFragment = (RoutesDrawerFragment)
                getFragmentManager().findFragmentById(R.id.navigation_drawer);
        mStopsDrawerFragment = (StopsDrawerFragment)
                getFragmentManager().findFragmentById(R.id.stops_drawer);
        mTitle = getTitle();
        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));
        mStopsDrawerFragment.setUp(
                R.id.stops_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadData();
    }

    public void loadData() {
        loadFromResponse(BusesResponse.class);
        loadFromResponse(StopsResponse.class);
        loadFromResponse(RoutesResponse.class);
    }

    private void loadFromResponse(final Class<? extends MBusResponse> responseType) {
        AsyncTask task = new MBusTask(responseType, this) {
            @Override
            protected void onPostExecute(MBusResponse mBusResponse) {
                // TODO: check for null pointer, etc
                if (mBusResponse instanceof BusesResponse) {
                    mBuses = ((BusesResponse)mBusResponse).response;
                    initBuses();
                }
                else if (mBusResponse instanceof StopsResponse) {
                    mStops = ((StopsResponse)mBusResponse).response;
                    Arrays.sort(mStops);
                    initStops();
                }
                else if (mBusResponse instanceof RoutesResponse) {
                    mRoutes = ((RoutesResponse)mBusResponse).response;
                    Arrays.sort(mRoutes);
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
        mNavigationDrawerFragment.setRoutes(mRoutes);
    }

    private void initStops() {
        mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
        mMap.setMyLocationEnabled(true);
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

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        // update the main content by replacing fragments

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

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            TextView textView = (TextView) rootView.findViewById(R.id.section_label);
            textView.setText(Integer.toString(getArguments().getInt(ARG_SECTION_NUMBER)));
            return rootView;
        }

//        @Override
//        public void onAttach(Activity activity) {
//            super.onAttach(activity);
//            ((MainActivity) activity).onSectionAttached(
//                    getArguments().getInt(ARG_SECTION_NUMBER));
//        }
    }

}
