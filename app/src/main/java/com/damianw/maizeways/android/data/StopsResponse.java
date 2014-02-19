package com.damianw.maizeways.android.data;

/**
 * Created by damian on 2/18/14.
 */

public class StopsResponse extends MBusResponse<StopsResponse.Stop> {
    public class Stop {
        public int id;
        public String unique_name;
        public String human_name;
        public String additional_name;
        public double latitude;
        public double longitude;
        public int heading;
    }
}
