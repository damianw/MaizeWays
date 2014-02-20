package com.damianw.maizeways.android.data;

/**
 * Created by damian on 2/18/14.
 */

public class RoutesResponse extends MBusResponse<RoutesResponse.Route> {
    public class Route implements Comparable<Route>, HasID {
        public int id;
        public String name;
        public String color;
        public int[] stops;
        public int top_of_loop_stop_id;

        @Override
        public int compareTo(Route route) {
            return name.compareTo(route.name);
        }
    }
}
