package com.damianw.maizeways.android.data;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.damianw.maizeways.android.R;
import com.google.gson.Gson;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;

/**
 * Created by damian on 2/18/14.
 */
public class MBusTask extends AsyncTask<Object, Void, MBusResponse> {

    private Context mContext;
    private HashMap<
            Class<? extends MBusResponse>,
            String
            > mURLs = new HashMap<Class<? extends MBusResponse>, String>();

    public Class<? extends MBusResponse> mClassType;
    public MBusTask(Class<? extends MBusResponse> classType, Context context) {
        mClassType = classType;
        mContext = context;
        // Add the URLs
        mURLs.put(BusesResponse.class, mContext.getString(R.string.mbus_buses_endpoint));
        mURLs.put(StopsResponse.class, mContext.getString(R.string.mbus_stops_endpoint));
        mURLs.put(RoutesResponse.class, mContext.getString(R.string.mbus_routes_endpoint));
    }

    @Override
    protected MBusResponse doInBackground(Object... params) {
        HttpResponse response = null;
        HttpClient httpclient = new DefaultHttpClient();
        String url = mURLs.get(mClassType);
        try {
//            Log.d("MBusTask", url);
            HttpGet httpget = new HttpGet(url);
            response = httpclient.execute(httpget);
        } catch (ClientProtocolException es) {
            Log.e("x", es.getMessage());
        } catch (IOException e) {
            Log.e("aasx" , e.getMessage());
        }
        Gson gson = new Gson();
        MBusResponse typedResponse = null;
        try {
            InputStreamReader isr = new InputStreamReader(response.getEntity().getContent());
            final BufferedReader reader = new BufferedReader(isr);
            typedResponse = gson.fromJson(reader, mClassType);
        } catch (IOException e) {
            Log.e("aasx" , e.getMessage());
        } catch (IllegalStateException e) {
            Log.e("MBusTask", "Failed to parse JSON for " + mClassType.getSimpleName());
        }
        return typedResponse;
    }
}
