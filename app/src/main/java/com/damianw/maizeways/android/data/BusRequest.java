package com.damianw.maizeways.android.data;

import android.os.AsyncTask;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.DefaultHttpClient;

/**
 * Created by damian on 2/18/14.
 */

public class BusRequest<ResponseType> extends AsyncTask<Void, Void, ResponseType>{

    private static String ENDPOINT = "http://mbus.pts.umich.edu/api/v0/buses/";

    Runnable mSuccessRunnable;
    Runnable mFailRunnable;

    public BusRequest(Runnable successRunnable, Runnable failRunnable) {
        super();
        mSuccessRunnable = successRunnable;
        mFailRunnable = failRunnable;
    }

    @Override
    public ResponseType doInBackground(Void... params) {
        HttpResponse response = null;
        HttpClient httpclient = new DefaultHttpClient();
        return null;
    }
}
