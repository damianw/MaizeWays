package com.damianw.maizeways.android.data;

/**
 * Created by damian on 2/18/14.
 */

public class BusesResponse extends MBusResponse<BusesResponse.Bus> {
    public class Bus {
        public int id;
        public double latitude;
        public double longitude;
        public int heading;
        public int route;
        public String route_name;
    }
}
