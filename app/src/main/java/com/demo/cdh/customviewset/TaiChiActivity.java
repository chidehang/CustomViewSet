package com.demo.cdh.customviewset;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.os.Bundle;

import com.demo.cdh.customviewset.view.TaiChiView;

public class TaiChiActivity extends Activity {

    private TaiChiView taiChiView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tai_chi);
        taiChiView = (TaiChiView) findViewById(R.id.taiChiView);

        ObjectAnimator animator = ObjectAnimator.ofFloat(taiChiView, "rotation", 0, 360);
        animator.setRepeatCount(ValueAnimator.INFINITE);
        animator.setDuration(2000);
        animator.start();
    }
}
