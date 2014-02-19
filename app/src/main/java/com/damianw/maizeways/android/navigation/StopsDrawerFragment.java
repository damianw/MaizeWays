package com.damianw.maizeways.android.navigation;

import android.widget.ArrayAdapter;

import com.damianw.maizeways.android.data.StopsResponse;

import java.util.ArrayList;

/**
 * Created by damian on 2/18/14.
 */
public class StopsDrawerFragment extends NavigationDrawerFragment {
    private StopsResponse.Stop[] mStops = new StopsResponse.Stop[0];

    public StopsDrawerFragment() {
    }

    public void setStops(StopsResponse.Stop[] stops) {
        mStops = stops;
        refreshStops();
    }

    public void refreshStops() {
        ArrayList<String> stopNames = new ArrayList<String>();
        for (StopsResponse.Stop stop : mStops) {
            stopNames.add(stop.human_name);
        }
        mDrawerListView.setAdapter(new ArrayAdapter<String>(
                getActionBar().getThemedContext(),
                android.R.layout.simple_list_item_activated_1,
                android.R.id.text1,
                stopNames));
    }
}
