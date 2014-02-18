package com.damianw.maizeways.android.data;

import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.Gson;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created by damian on 2/18/14.
 */
public class MBusTask<ResponseType extends MBusResponse> extends AsyncTask<String, Void, ResponseType> {

    public Class<ResponseType> mClassType;
    public MBusTask(Class<ResponseType> classType) {
        mClassType = classType;
    }

    @Override
    protected ResponseType doInBackground(String... params) {
        HttpResponse response = null;
        HttpClient httpclient = new DefaultHttpClient();
        try {
            HttpGet httpget = new HttpGet(params[0]);
            response = httpclient.execute(httpget);
        } catch (ClientProtocolException es)
        {
            Log.e("x", es.getMessage());
        } catch (IOException e)
        {
            Log.e("aasx" , e.getMessage());
        }
        Gson gson = new Gson();
        ResponseType typedResponse = null;
        try {
            InputStreamReader isr = new InputStreamReader(response.getEntity().getContent());
            final BufferedReader reader = new BufferedReader(isr);
            typedResponse = gson.fromJson(reader, mClassType);
        } catch (IOException e) {
            Log.e("aasx" , e.getMessage());
        }
        return typedResponse;
    }
}
