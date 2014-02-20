package com.damianw.maizeways.android.data;

/**
 * Created by damian on 2/18/14.
 */

public class BusesResponse extends MBusResponse<BusesResponse.Bus> {
    public class Bus implements Comparable<Bus>, HasID {
        public int id;
        public double latitude;
        public double longitude;
        public int heading;
        public int route;
        public String route_name;

        @Override
        public int compareTo(Bus bus) {
            return route_name.compareTo(bus.route_name);
        }
    }
}
