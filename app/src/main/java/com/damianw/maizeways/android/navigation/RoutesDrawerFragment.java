package com.damianw.maizeways.android.navigation;

import com.damianw.maizeways.android.data.RoutesResponse;

import java.util.HashMap;

/**
 * Created by damian on 2/18/14.
 */
public class RoutesDrawerFragment extends NavigationDrawerFragment<RoutesResponse.Route> {

    public HashMap<Integer, RoutesResponse.Route> getSelectedItems() {
        HashMap<Integer, RoutesResponse.Route> result = new HashMap<Integer, RoutesResponse.Route>();
        for (int index : getSelectedIndices()) {
            result.put(getItems().get(index).id, getItems().get(index));
        }
        return result;
    }

}
