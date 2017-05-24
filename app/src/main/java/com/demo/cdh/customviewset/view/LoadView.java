package com.demo.cdh.customviewset.view;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.graphics.RectF;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import java.lang.ref.WeakReference;

/**
 * Created by hang on 2017/5/24.
 * 加载动画
 */

public class LoadView extends View {

    private Paint paint;

    private int width;
    private int height;

    private Path succeedPath;
    private Path failedPath;
    private Path loadingPath;
    private PathMeasure pathMeasure;

    private int defaultDuration = 1500;

    private ValueAnimator startAnimator; //开始动画
    private ValueAnimator loadingAnimator; //加载动画
    private ValueAnimator succeedAnimator; //成功动画
    private ValueAnimator failedAnimator; //失败动画

    private float animValue;

    private int loadResult; //0未返回 1成功 2失败

    private StateHandler handler;

    private State curState = State.NONE;

    public LoadView(Context context) {
        super(context);
        init();
    }

    public LoadView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public LoadView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public void init() {
        initPaint();
        initPath();
        initAnimator();
        handler = new StateHandler(this);
    }

    public void initPaint() {
        paint = new Paint();
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(15);
        paint.setAntiAlias(true);
        paint.setColor(Color.parseColor("#0082D7"));
        paint.setStrokeCap(Paint.Cap.ROUND);
    }

    public void initPath() {
        succeedPath = new Path();
        failedPath = new Path();
        loadingPath = new Path();
        pathMeasure = new PathMeasure();

        RectF oval = new RectF(-100, -100, 100, 100);
        loadingPath.addArc(oval, -45, 359.9f);

        float[] pos = new float[2];
        pathMeasure.setPath(loadingPath, false);
        pathMeasure.getPosTan(0, pos, null);
        float a = (float) Math.sqrt(2500/2);
        succeedPath.moveTo(-2*a, 0);
        succeedPath.lineTo(-a, a);
        succeedPath.lineTo(pos[0], pos[1]);

        Path p1 = new Path();
        p1.moveTo(50, 50);
        p1.lineTo(-50, -50);
        Path p2 = new Path();
        p2.moveTo(-50, 50);
        p2.lineTo(50, -50);
        failedPath.addPath(p1);
        failedPath.addPath(p2);
    }

    public void initAnimator() {
        ValueAnimator.AnimatorUpdateListener updateListener = new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                animValue = (float) animation.getAnimatedValue();
                invalidate();
            }
        };
        Animator.AnimatorListener animatorListener = new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                handler.sendEmptyMessage(0);
            }
        };

        startAnimator = ValueAnimator.ofFloat(0, 1).setDuration(defaultDuration);
        loadingAnimator = ValueAnimator.ofFloat(0, 1).setDuration(defaultDuration);
        succeedAnimator = ValueAnimator.ofFloat(1, 0).setDuration(defaultDuration);
        failedAnimator = ValueAnimator.ofFloat(1, 0).setDuration(defaultDuration);

        startAnimator.addUpdateListener(updateListener);
        loadingAnimator.addUpdateListener(updateListener);
        succeedAnimator.addUpdateListener(updateListener);
        failedAnimator.addUpdateListener(updateListener);

        startAnimator.addListener(animatorListener);
        loadingAnimator.addListener(animatorListener);
        succeedAnimator.addListener(animatorListener);
        failedAnimator.addListener(animatorListener);
    }

    public void startLoading() {
        if(curState != State.NONE)
            return;

        curState = State.STARTING;
        startAnimator.start();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                overLoading();
            }
        }, 5000);
    }

    public int setResult;

    public void overLoading() {
        loadResult = setResult;
    }

    public void handleStateSwitch() {
        switch (curState) {
            case STARTING:
                loadResult = 0;
                curState = State.LOADING;
                loadingAnimator.start();
                break;

            case LOADING:
                if(loadResult == 0) {
                    loadingAnimator.start();
                } else if(loadResult==1) {
                    curState = State.SUCCESS;
                    succeedAnimator.start();
                } else if(loadResult == 2) {
                    curState = State.FAILURE;
                    failedAnimator.start();
                }
                break;

            case SUCCESS:
                curState = State.NONE;
                break;

            case FAILURE:
                curState = State.NONE;
                break;
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        if(w!=oldw || h!=oldh) {
            width = w;
            height = h;
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.translate(width/2, height/2);
        switch (curState) {
            case NONE:
                canvas.drawPath(succeedPath, paint);
                break;

            case STARTING:
                pathMeasure.setPath(succeedPath, false);
                Path dst = new Path();
                float length = pathMeasure.getLength();
                pathMeasure.getSegment(length*animValue, length, dst, true);
                canvas.drawPath(dst, paint);
                break;

            case LOADING:
                pathMeasure.setPath(loadingPath, false);
                Path dst2 = new Path();
                float stop = pathMeasure.getLength() * animValue;
                float start = (float) (stop - ((0.5-Math.abs(animValue-0.5))*200f));
                pathMeasure.getSegment(start, stop, dst2, true);
                canvas.drawPath(dst2, paint);
                break;

            case SUCCESS:
                pathMeasure.setPath(succeedPath, false);
                Path dst3 = new Path();
                float length3 = pathMeasure.getLength();
                pathMeasure.getSegment(length3*animValue, length3, dst3, true);
                canvas.drawPath(dst3, paint);
                break;

            case FAILURE:
                pathMeasure.setPath(failedPath, false);
                Path dst4 = new Path();
                float length4 = pathMeasure.getLength();
                pathMeasure.getSegment(length4*animValue, length4, dst4, true);
                canvas.drawPath(dst4, paint);

                pathMeasure.nextContour();
                Path dst5 = new Path();
                float length5 = pathMeasure.getLength();
                pathMeasure.getSegment(length5*animValue, length5, dst5, true);
                canvas.drawPath(dst5, paint);
                break;
        }
    }

    private static enum State {
        NONE,
        STARTING,
        LOADING,
        SUCCESS,
        FAILURE
    }

    private class StateHandler extends Handler {
        WeakReference<LoadView> reference;
        StateHandler(LoadView loadView) {
            reference = new WeakReference<LoadView>(loadView);
        }
        @Override
        public void handleMessage(Message msg) {
            if(reference.get() != null) {
                reference.get().handleStateSwitch();
            }
        }
    }
}
