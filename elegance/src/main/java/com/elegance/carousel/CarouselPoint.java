package com.elegance.carousel;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

/**
 * Created by hongyang on 17-11-13.
 */

public class CarouselPoint extends View {

    private static final int OFFSET = 35;
    private static final int CIRCLE = 40;
    private static final int RADIUS = 10;
    private static final int CY = 20;
    private static final int TOP = 10;
    private static final int BOTTOM = 30;
    private static final int ROUND_XY = 15;

    private int transitionLift;
    private int transitionRight;
    private int pointsLength;
    private int positionTransition;
    private boolean isTransition;
    private Paint paint;
    private RectF rectF;
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

    }

    public void onDrawPointsLength(int pointsLength) {
        this.pointsLength = pointsLength;
        this.transitionLift = 0;
        this.transitionRight = 0;
        this.invalidate();
        setMeasuredDimension(CIRCLE * pointsLength + OFFSET + 20, 60);
    }

    public void setTransition(int positionTransition) {
        this.positionTransition = positionTransition;
        this.transitionLift = 0;
        this.transitionRight = 0;
        invalidate(CIRCLE * positionTransition + OFFSET - 20 - transitionLift, TOP, CIRCLE * positionTransition + OFFSET + 20 + transitionRight, BOTTOM);
        setMeasuredDimension(CIRCLE * pointsLength + OFFSET + 20, 60);
    }

    public int getPositionTransition(){
        return this.positionTransition;
    }


    public void setTransitionLift(int transitionLift) {
            this.transitionLift = transitionLift;
            invalidate(CIRCLE * positionTransition + OFFSET - 20 - transitionLift, TOP, CIRCLE * positionTransition + OFFSET + 20 + transitionRight, BOTTOM);

    }

    public void setTransitionRight(int transitionRight) {
            this.transitionRight = transitionRight;
            invalidate(CIRCLE * positionTransition + OFFSET - 20 - transitionLift, TOP, CIRCLE * positionTransition + OFFSET + 20 + transitionRight, BOTTOM);
    }

    public void setTransitionReset() {
        if (transitionLift != 0) {
            Log.e(CarouselPoint.class.getSimpleName(), "左移动释放");
        }
        if (transitionRight != 0) {
            Log.e(CarouselPoint.class.getSimpleName(), "右移动释放");
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (pointsLength <= 1) return;
        this.drawPoints(canvas);
        super.onDraw(canvas);
    }

    public void drawPoints(Canvas canvas) {
        for (int i = 0; i < pointsLength; i++) {
            paint.setColor(Color.parseColor("#99FFFFFF"));
            canvas.drawCircle(CIRCLE * i + OFFSET, CY, RADIUS, paint);
            Log.e("onDrawPointsLength",String.valueOf(String.valueOf(CIRCLE * i + OFFSET)));
        }
        drawRoundRect(canvas);

    }

    private void drawRoundRect(Canvas canvas) {
        paint.setColor(Color.parseColor("#CCFFFF"));
        rectF.left = CIRCLE * positionTransition + OFFSET - 20 - transitionLift;
        rectF.right = CIRCLE * positionTransition + OFFSET + 20 + transitionRight;
        rectF.top = TOP;
        rectF.bottom = BOTTOM;
        canvas.drawRoundRect(rectF, ROUND_XY, ROUND_XY, paint);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

    }

}
