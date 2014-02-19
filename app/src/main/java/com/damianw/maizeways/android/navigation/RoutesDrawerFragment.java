package com.damianw.maizeways.android.navigation;

import android.widget.ArrayAdapter;

import com.damianw.maizeways.android.data.RoutesResponse;

import java.util.ArrayList;

/**
 * Created by damian on 2/18/14.
 */
public class RoutesDrawerFragment extends NavigationDrawerFragment {
    private RoutesResponse.Route[] mRoutes = new RoutesResponse.Route[0];

    public RoutesDrawerFragment() {
    }

    public void setRoutes(RoutesResponse.Route[] routes) {
        mRoutes = routes;
        refreshRoutes();
    }

    public void refreshRoutes() {
        ArrayList<String> routeNames = new ArrayList<String>();
        for (RoutesResponse.Route route : mRoutes) {
            routeNames.add(route.name);
        }
        mDrawerListView.setAdapter(new ArrayAdapter<String>(
                getActionBar().getThemedContext(),
                android.R.layout.simple_list_item_activated_1,
                android.R.id.text1,
                routeNames));
    }
}
