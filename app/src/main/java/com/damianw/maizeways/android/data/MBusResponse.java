package com.damianw.maizeways.android.data;

/**
 * Created by damian on 2/18/14.
 */
public abstract class MBusResponse<DataType extends Comparable> {
    public DataType[] response;
}
