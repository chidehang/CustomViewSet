package com.demo.cdh.customviewset;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void svgPath(View v) {
        startActivity(new Intent(this, PathActivity.class));
    }

    public void taiChi(View v) {
        startActivity(new Intent(this, TaiChiActivity.class));
    }

    public void heart(View v) {
        startActivity(new Intent(this, HeartActivity.class));
    }
}
