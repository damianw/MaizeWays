package com.damianw.maizeways.android;

import android.app.Activity;
import android.os.Bundle;

/**
 * Created by damian on 2/20/14.
 */
public class StopDetailActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bus_detail);
    }

    @Override
    public void onBackPressed() {
        finish();
        overridePendingTransition(R.anim.slide_left_to_center, R.anim.slide_center_to_right);
    }
}
