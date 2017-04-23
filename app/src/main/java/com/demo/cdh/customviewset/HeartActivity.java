package com.demo.cdh.customviewset;

import android.app.Activity;
import android.os.Bundle;

import com.demo.cdh.customviewset.view.HeartView;

public class HeartActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_heart);

        HeartView view = (HeartView) findViewById(R.id.heartView);
        view.transform();
    }
}
