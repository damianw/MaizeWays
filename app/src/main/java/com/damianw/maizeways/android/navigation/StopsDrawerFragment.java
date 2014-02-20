package com.damianw.maizeways.android.navigation;

import com.damianw.maizeways.android.data.StopsResponse;

import java.util.HashMap;

/**
 * Created by damian on 2/18/14.
 */
public class StopsDrawerFragment extends NavigationDrawerFragment<StopsResponse.Stop> {

    public HashMap<Integer, StopsResponse.Stop> getSelectedItems() {
        HashMap<Integer, StopsResponse.Stop> result = new HashMap<Integer, StopsResponse.Stop>();
        for (int index : getSelectedIndices()) {
            result.put(getItems().get(index).id, getItems().get(index));
        }
        return result;
    }

}
