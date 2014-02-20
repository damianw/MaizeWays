package com.damianw.maizeways.android.data;

import java.util.Arrays;
import java.util.List;

/**
 * Created by damian on 2/18/14.
 */

public class RoutesResponse extends MBusResponse<RoutesResponse.Route> {
    public class Route implements Comparable<Route>, MBusDataModel {
        public int id;
        public String name;
        public String color;
        public Integer[] stops;
        public int top_of_loop_stop_id;

        public List<Integer> getStops() {
            return Arrays.asList(stops);
        }

        @Override
        public int compareTo(Route route) {
            return name.compareTo(route.name);
        }

        @Override
        public int getID() {
            return id;
        }
    }
}
