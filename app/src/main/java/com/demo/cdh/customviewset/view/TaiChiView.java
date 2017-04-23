package com.demo.cdh.customviewset.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by hang on 2017/4/23.
 * 太极图案
 */

public class TaiChiView extends View {

    private Paint blackPaint;
    private Paint whitePaint;

    public TaiChiView(Context context) {
        super(context);
        init();
    }

    public TaiChiView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public TaiChiView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public void init() {
        blackPaint = new Paint();
        blackPaint.setAntiAlias(true);
        blackPaint.setColor(Color.BLACK);

        whitePaint = new Paint();
        whitePaint.setAntiAlias(true);
        whitePaint.setColor(Color.WHITE);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int w = MeasureSpec.getSize(widthMeasureSpec);
        int h = MeasureSpec.getSize(heightMeasureSpec);

        int side = Math.min(w, h);
        widthMeasureSpec = MeasureSpec.makeMeasureSpec(side, widthMode);
        heightMeasureSpec = MeasureSpec.makeMeasureSpec(side, heightMode);

        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        int width = canvas.getWidth();
        int height = canvas.getHeight();
        Point center = new Point(width/2, height/2);
        canvas.translate(center.x, center.y);

        //绘制太极底盘
        int radius = Math.min(width, height) / 2;
        RectF rectF = new RectF(-radius, -radius, radius, radius);
        canvas.drawArc(rectF, 90, 180, true, blackPaint);
        canvas.drawArc(rectF, -90, 180, true, whitePaint);

        //绘制两个大圆制造两级的形状
        int smallRadius = radius / 2;
        canvas.drawCircle(0, -smallRadius, smallRadius, blackPaint);
        canvas.drawCircle(0, smallRadius, smallRadius, whitePaint);

        //绘制两个鱼眼
        int dotRadius = smallRadius / 4;
        canvas.drawCircle(0, -smallRadius, dotRadius, whitePaint);
        canvas.drawCircle(0, smallRadius, dotRadius, blackPaint);
    }
}
