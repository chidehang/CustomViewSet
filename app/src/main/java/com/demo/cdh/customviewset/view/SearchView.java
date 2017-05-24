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
 */

public class SearchView extends View {

    private Paint paint;

    private int width;
    private int height;

    private State curState = State.NONE;

    private Path searchPath; //放大镜图标图案
    private Path circlePath; //加载进度圆圈图案
    private PathMeasure measure;

    private int defaultDuration = 2000; //动画时间

    private ValueAnimator startAnimator; //搜索前的准备动画
    private ValueAnimator searchAnimator; //搜索中的动画
    private ValueAnimator endAnimator; //搜索结束的动画

    private float animValue; //当前动画进度值

    private StateHandler handler; //控制动画状态转换

    private boolean isOver; //搜索是否结束

    public SearchView(Context context) {
        super(context);
        init();
    }

    public SearchView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public SearchView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
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
        paint.setColor(Color.parseColor("#0082D7"));
        paint.setStrokeWidth(15);
        paint.setStrokeCap(Paint.Cap.ROUND); //端点圆弧
        paint.setAntiAlias(true);
    }

    public void initPath() {
        //初始化放大镜图案
        searchPath = new Path();
        //初始化加载时大圆圈图案
        circlePath = new Path();
        measure = new PathMeasure();

        //放大镜小圆环
        RectF oval1 = new RectF(-50, -50, 50, 50);
        searchPath.addArc(oval1, 45, 359.9f);
        //搜索中大圆圈
        RectF oval2 = new RectF(-100, -100, 100, 100);
        circlePath.addArc(oval2, 45, 359.9f);

        //获取大圆圈圆弧起始点的坐标
        float[] pos = new float[2];
        measure.setPath(circlePath, false);
        measure.getPosTan(0, pos, null);
        //添加放大镜把手
        searchPath.lineTo(pos[0], pos[1]);
    }

    public void initAnimator() {
        ValueAnimator.AnimatorUpdateListener updateListener = new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                //获取当前动画更新进度值，刷新绘制
                animValue = (float) animation.getAnimatedValue();
                invalidate();
            }
        };

        Animator.AnimatorListener animatorListener = new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                //动画结束通知状态改变
                handler.sendEmptyMessage(0);
            }
        };

        startAnimator = ValueAnimator.ofFloat(0, 1).setDuration(defaultDuration);
        searchAnimator = ValueAnimator.ofFloat(0, 1).setDuration(defaultDuration);
        endAnimator = ValueAnimator.ofFloat(1, 0).setDuration(defaultDuration); //结束动画和开始动画相反

        startAnimator.addUpdateListener(updateListener);
        searchAnimator.addUpdateListener(updateListener);
        endAnimator.addUpdateListener(updateListener);

        startAnimator.addListener(animatorListener);
        searchAnimator.addListener(animatorListener);
        endAnimator.addListener(animatorListener);
    }

    public void startSearch() {
        if(curState != State.NONE)
            return;

        curState = State.STARTING;
        startAnimator.start();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                overSearch();
            }
        }, 5000);
    }

    public void overSearch() {
        isOver = true;
    }

    /**
     * 处理状态改变
     */
    public void handleStateSwitch() {
        switch (curState) {
            case STARTING:
                isOver = false;
                curState = State.SEARCHING;
                searchAnimator.start();
                break;

            case SEARCHING:
                if(!isOver) {
                    searchAnimator.start();
                } else {
                    curState = State.ENDING;
                    endAnimator.start();
                }
                break;

            case ENDING:
                curState = State.NONE;
                break;
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        if(w!=oldw || h!=oldh) {
            width =  w;
            height = h;
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.translate(width/2, height/2);
        switch (curState) {
            case NONE:
                canvas.drawPath(searchPath, paint);
                break;

            case STARTING:
                measure.setPath(searchPath, false);
                Path dst = new Path();
                float length = measure.getLength();
                //*** 获取片段，绘制该片段 ***
                measure.getSegment(length*animValue, length, dst, true);
                canvas.drawPath(dst, paint);
                break;

            case SEARCHING:
                measure.setPath(circlePath, false);
                Path dst2 = new Path();
                float stop = measure.getLength() * animValue;
                float start = (float) (stop - ((0.5-Math.abs(animValue-0.5))*200f));
                measure.getSegment(start, stop, dst2, true);
                canvas.drawPath(dst2, paint);
                break;

            case ENDING:
                measure.setPath(searchPath, false);
                Path dst3 = new Path();
                float length3 = measure.getLength();
                measure.getSegment(length3*animValue, length3, dst3, true);
                canvas.drawPath(dst3, paint);
                break;
        }
    }

    public static enum State {
        NONE,
        STARTING,
        SEARCHING,
        ENDING
    }

    static class StateHandler extends Handler {
        private WeakReference<SearchView> reference;
        StateHandler(SearchView searchView) {
            reference = new WeakReference<SearchView>(searchView);
        }
        @Override
        public void handleMessage(Message msg) {
            if(reference.get() != null) {
                reference.get().handleStateSwitch();
            }
        }
    }
}
