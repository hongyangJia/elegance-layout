package com.elegance.carousel;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

/**
 * Created by hongyang on 17-11-13.
 */

public class CarouselPoint extends View {

    private static final int OFFSET = 10;
    private static final float CIRCLE = 8.9f;
    private static final float RADIUS = 2.3f;
    private static final int CY = 20;
    private static final float TOP = 2.6f;
    private static final float BOTTOM = 7f;
    private static final int ROUND_XY = 15;
    private static final float INTERAVL=12.4f;
    private static final String COLOR="#59FFFFFF";
    private int pointsLength;
    private int positionTransition;
    private boolean isTransition;
    private Paint paint;
    private RectF rectF;
    private int widthSpec;
    private int heightSpec;
    private int circle;
    private int offset;
    private int interval;
    private int radius;
    private int top;
    private int bottom;
    public CarouselPoint(Context context) {
        super(context);
        this.initCarouselPoint();
    }

    public CarouselPoint(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.initCarouselPoint();
    }

    private void initCarouselPoint() {
        paint = new Paint();
        rectF = new RectF();
        circle=dip2px(CIRCLE);
        offset=dip2px(OFFSET);
        radius=dip2px(RADIUS);
        interval=dip2px(INTERAVL);
        top=dip2px(TOP);
        bottom=dip2px(BOTTOM);
        heightSpec=(int) ((top+bottom)*1.5);
        paint.setColor(Color.parseColor("#59FFFFFF"));
        Log.e(CarouselPoint.class.getSimpleName(),String.valueOf("initCarouselPoint"));
    }

    public   int dip2px(float dpValue) {
        final float scale = getContext().getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    public void onDrawPointsLength(int pointsLength) {
        Log.e(CarouselPoint.class.getSimpleName(),String.valueOf("onDrawPointsLength"));
        this.pointsLength = pointsLength;
        widthSpec=circle * pointsLength + offset + interval;
        this.invalidate();
        requestLayout();
    }

    public void setTransition(int positionTransition) {
        Log.e(CarouselPoint.class.getSimpleName(),String.valueOf("positionTransition"));
        this.positionTransition = positionTransition;
        invalidate(circle * positionTransition + offset - interval , top, circle * positionTransition + offset + interval , bottom);
    }

    public int getPositionTransition(){
        return this.positionTransition;
    }

    public void setTransitionReset() {

    }

    @Override
    protected void onDraw(Canvas canvas) {
        Log.e(CarouselPoint.class.getSimpleName(),String.valueOf("onDraw"));
        if (pointsLength <= 1) return;
        this.drawPoints(canvas);
        super.onDraw(canvas);
    }

    public void drawPoints(Canvas canvas) {
        for (int i = 0; i < pointsLength; i++) {
            if (i==positionTransition){
                drawRoundRect(canvas);
                continue;
            }
            paint.setColor(Color.parseColor("#59FFFFFF"));
            canvas.drawCircle((float) (circle * i + offset+(i<positionTransition?0:interval*0.8)), top*2, radius, paint);
        }
    }

    private void drawRoundRect(Canvas canvas) {
        rectF.left = (float) (circle * positionTransition + offset-(positionTransition==0?interval*0.2:interval*0.2));
        rectF.right = (float) (circle * positionTransition + offset + interval);
        rectF.top = (float) (top+top*0.12);
        rectF.bottom = bottom;
        canvas.drawRoundRect(rectF, ROUND_XY, ROUND_XY, paint);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        Log.e(CarouselPoint.class.getSimpleName(),String.valueOf(widthSpec));
        setMeasuredDimension(widthSpec, heightSpec);
    }

}
