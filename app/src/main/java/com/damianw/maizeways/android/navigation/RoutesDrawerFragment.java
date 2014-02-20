package com.damianw.maizeways.android.navigation;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.damianw.maizeways.android.data.RoutesResponse;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

/**
 * Created by damian on 2/18/14.
 */
public class RoutesDrawerFragment extends NavigationDrawerFragment {
    private RoutesResponse.Route[] mRoutes = new RoutesResponse.Route[0];
    private ArrayList<RoutesDrawerCallback> mCallbacks = new ArrayList<RoutesDrawerCallback>();
    private RoutesDrawerAdapter mAdapter;
    private HashMap<Integer, Boolean> mSelectedRoutes = new HashMap<Integer, Boolean>();

    public RoutesDrawerFragment() {
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
//        mDrawerListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                selectItem(position);
//            }
//        });
    }

    public void setRoutes(RoutesResponse.Route[] routes) {
        mRoutes = routes;
        refreshRoutes();
    }

    public void refreshRoutes() {
        mAdapter = new RoutesDrawerAdapter(getActivity(), Arrays.asList(mRoutes));
        mDrawerListView.setAdapter(mAdapter);
    }

    private void selectItem(int position) {
        if (!mSelectedRoutes.containsKey(position)) {
            mSelectedRoutes.put(position, false);
        }
        boolean currentlySelected = mSelectedRoutes.get(position);
        Log.d("RoutesDrawerFragment", position + " is now " + !currentlySelected);
        if (mDrawerListView != null) {
            mDrawerListView.setItemChecked(position, !currentlySelected);
            // TODO: fix this
            mAdapter.setSelected(position, !currentlySelected);
        }
        if (mCallbacks != null) {
            for (RoutesDrawerCallback callback : mCallbacks) {
                callback.onRoutesItemSelected(position);
            }
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

    public interface RoutesDrawerCallback {
        public void onRoutesItemSelected(int position);
    }

}
