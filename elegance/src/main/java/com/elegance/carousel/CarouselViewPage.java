package com.elegance.carousel;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.ViewConfiguration;

/**
 * Created by hongyang on 17-11-15.
 */

public class CarouselViewPage extends ViewPager {

    private static final int INVALID_POINTER = -1;
    private float mFirstMotionX;
    private VelocityTracker mVelocityTracker;
    private onTouchChangeListener onTouchChangeListener;
    private int mMaximumVelocity;
    private int mActivePointerId = INVALID_POINTER;
    private int mMinimumVelocity;
    private static final int MIN_FLING_VELOCITY = 400;
    private int mFlingDistance;
    private static final int MIN_DISTANCE_FOR_FLING = 25;
    int mCurItem;
    public CarouselViewPage(Context context) {
        super(context);
        this.initCarouselViewPage();
    }

    public CarouselViewPage(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.initCarouselViewPage();
    }

    public void initCarouselViewPage(){
        mVelocityTracker = VelocityTracker.obtain();
        final ViewConfiguration configuration = ViewConfiguration.get(getContext());
        mMaximumVelocity = configuration.getScaledMaximumFlingVelocity();
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        mVelocityTracker.addMovement(ev);
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mFirstMotionX = ev.getX();
                mActivePointerId = ev.getPointerId(0);
                break;
            case MotionEvent.ACTION_MOVE:
                if (ev.getX() > mFirstMotionX) {
                    if (onTouchChangeListener != null) {
                        onTouchChangeListener.touchChangeLift((int) (ev.getX()-mFirstMotionX));
                    }
                } else {
                    if (onTouchChangeListener != null) {
                        onTouchChangeListener.touchChangeRight((int) (ev.getX()-mFirstMotionX));
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
            /*    mVelocityTracker.computeCurrentVelocity(1000, mMaximumVelocity);
                int initialVelocity = (int) mVelocityTracker.getXVelocity(mActivePointerId);
                final int currentPage = 0;
                if (onTouchChangeListener != null) {
                    onTouchChangeListener.touchChangeReset();
                }*/
                break;
        }

        return super.onTouchEvent(ev);
    }

    public void addTouchChangeListener(onTouchChangeListener onTouchChangeListener) {
        this.onTouchChangeListener = onTouchChangeListener;
    }

    public interface onTouchChangeListener extends OnPageChangeListener {
        void touchChangeLift(int i);

        void touchChangeRight(int i);

        void touchChangeReset();
    }

}
