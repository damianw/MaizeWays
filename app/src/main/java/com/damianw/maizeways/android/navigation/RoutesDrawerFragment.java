package com.damianw.maizeways.android.navigation;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;

import com.damianw.maizeways.android.data.RoutesResponse;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.TreeSet;

/**
 * Created by damian on 2/18/14.
 */
public class RoutesDrawerFragment extends NavigationDrawerFragment {
    private RoutesResponse.Route[] mRoutes = new RoutesResponse.Route[0];
    private ArrayList<RoutesDrawerCallback> mCallbacks = new ArrayList<RoutesDrawerCallback>();
    private RoutesDrawerAdapter mAdapter;
    private TreeSet<Integer> mSelectedRoutes = new TreeSet<Integer>();

    public RoutesDrawerFragment() {
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mDrawerListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                clickItem(position);
            }
        });
    }

    public void setRoutes(RoutesResponse.Route[] routes) {
        mRoutes = routes;
        refreshRoutes();
    }

    public void refreshRoutes() {
        mAdapter = new RoutesDrawerAdapter(getActivity(), Arrays.asList(mRoutes));
        mDrawerListView.setAdapter(mAdapter);
    }

    private void clickItem(int position) {
        boolean currentlySelected = mSelectedRoutes.contains(position);
        Log.d("RoutesDrawerFragment", position + " is now " + !currentlySelected);
        if (mDrawerListView != null) {
            mDrawerListView.setItemChecked(position, !currentlySelected);
            // TODO: fix this
            mAdapter.clickItem(position);
        }
        if (mCallbacks != null) {
            for (RoutesDrawerCallback callback : mCallbacks) {
                callback.updateSelectedRoutes();
            }
        }
        if (currentlySelected) {
            mSelectedRoutes.remove(position);
        } else {
            mSelectedRoutes.add(position);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mCallbacks.add((RoutesDrawerCallback) activity);
        } catch (ClassCastException e) {
            throw new ClassCastException("Activity must implement NavigationDrawerCallbacks.");
        }
    }

    public HashMap<Integer, RoutesResponse.Route> getSelectedRoutes() {
        HashMap<Integer, RoutesResponse.Route> result = new HashMap<Integer, RoutesResponse.Route>();
        for (int index : mSelectedRoutes) {
            result.put(mRoutes[index].id, mRoutes[index]);
        }
        return result;
    }

    public interface RoutesDrawerCallback {
        public void updateSelectedRoutes();
    }

}
