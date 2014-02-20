package com.damianw.maizeways.android.data;

/**
 * Created by damian on 2/18/14.
 */

public class StopsResponse extends MBusResponse<StopsResponse.Stop> {
    public class Stop implements Comparable<Stop>, MBusDataModel {
        public int id;
        public String unique_name;
        public String human_name;
        public String additional_name;
        public double latitude;
        public double longitude;
        public int heading;

        @Override
        public int compareTo(Stop stop) {
            return human_name.compareTo(stop.human_name);
        }

        @Override
        public int getID() {
            return id;
        }
    }
}
