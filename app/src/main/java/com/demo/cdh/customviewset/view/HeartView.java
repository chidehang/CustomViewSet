package com.demo.cdh.customviewset.view;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by hang on 2017/4/23.
 * 圆形变换成心形
 */

public class HeartView extends View {

    private static final float C = 0.551915024494f;

    private Paint paint;
    private float radius = 200;
    private float distance = radius * C;

    private int centerX, centerY;

    //利用三阶贝塞尔曲线绘制圆形
    //将圆形按四个象限拆分成四段
    //有四个端点和八个锚点
    private PointF[] dataPoint = new PointF[4];
    private PointF[] ctrlPoint = new PointF[8];

    //变换时偏移距离
    private float offset;
    //保存原始坐标
    private float oriDataY;
    private float oriCtrlX1, oriCtrlX2;
    private float oriCtrlY1, oriCtrlY2;


    public HeartView(Context context) {
        super(context);
        init();
    }

    public HeartView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public HeartView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public void init() {
        paint = new Paint();
        paint.setColor(Color.RED);
        paint.setStrokeWidth(8);
        paint.setStyle(Paint.Style.STROKE);

        dataPoint[0] = new PointF(-radius, 0); //left
        dataPoint[1] = new PointF(0, -radius); //top
        dataPoint[2] = new PointF(radius, 0);  //right
        dataPoint[3] = new PointF(0, radius);  //bottom

        ctrlPoint[0] = new PointF(dataPoint[0].x, dataPoint[0].y-distance); //第二象限
        ctrlPoint[1] = new PointF(dataPoint[1].x-distance, dataPoint[1].y);
        ctrlPoint[2] = new PointF(dataPoint[1].x+distance, dataPoint[1].y); //第一象限
        ctrlPoint[3] = new PointF(dataPoint[2].x, dataPoint[2].y-distance);
        ctrlPoint[4] = new PointF(dataPoint[2].x, dataPoint[2].y+distance); //第四象限
        ctrlPoint[5] = new PointF(dataPoint[3].x+distance, dataPoint[3].y);
        ctrlPoint[6] = new PointF(dataPoint[3].x-distance, dataPoint[3].y); //第三象限
        ctrlPoint[7] = new PointF(dataPoint[0].x, dataPoint[0].y+distance);

        oriDataY = dataPoint[1].y;
        oriCtrlX1 = ctrlPoint[4].x;
        oriCtrlX2 = ctrlPoint[7].x;
        oriCtrlY1 = ctrlPoint[5].y;
        oriCtrlY2 = ctrlPoint[6].y;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        centerX = w / 2;
        centerY = h / 2;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.translate(centerX, centerY);

        Path path = new Path();
        path.moveTo(dataPoint[0].x, dataPoint[0].y);
        //三阶贝塞尔曲线
        path.cubicTo(ctrlPoint[0].x, ctrlPoint[0].y, ctrlPoint[1].x, ctrlPoint[1].y, dataPoint[1].x, dataPoint[1].y);
        path.cubicTo(ctrlPoint[2].x, ctrlPoint[2].y, ctrlPoint[3].x, ctrlPoint[3].y, dataPoint[2].x, dataPoint[2].y);
        path.cubicTo(ctrlPoint[4].x, ctrlPoint[4].y, ctrlPoint[5].x, ctrlPoint[5].y, dataPoint[3].x, dataPoint[3].y);
        path.cubicTo(ctrlPoint[6].x, ctrlPoint[6].y, ctrlPoint[7].x, ctrlPoint[7].y, dataPoint[0].x, dataPoint[0].y);

        canvas.drawPath(path, paint);
    }

    public float getDistance() {
        return distance;
    }

    public void setDistance(float distance) {
        this.distance = distance;

        dataPoint[1].y = (float) (oriDataY + 1.2*distance);
        ctrlPoint[4].x = (float) (oriCtrlX1 - 0.2*distance);
        ctrlPoint[7].x = (float) (oriCtrlX2 + 0.2*distance);
        ctrlPoint[5].y = (float) (oriCtrlY1 - 0.8*distance);
        ctrlPoint[6].y = (float) (oriCtrlY2 - 0.8*distance);

        invalidate();
    }

    /**
     * 变换成心形
     */
    public void transform() {
        ObjectAnimator.ofFloat(this, "distance", 0, 100)
                .setDuration(1000)
                .start();
    }
}
