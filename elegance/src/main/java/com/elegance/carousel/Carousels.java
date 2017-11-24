package com.elegance.carousel;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.elegance.R;
import com.elegance.util.IntervalCountDown;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Handler;

/**
 * Created by hongyang on 17-11-13.
 */

public class Carousels extends FrameLayout implements CarouselViewPage.onTouchChangeListener {

    private final static float DEFULT = 0.3f;
    private CarouselViewPage mViewPager;
    private DisplayMetrics mDisplayMetrics;
    private float heightScale;
    private CarouselPoint mCarouselPoints;

    public Carousels(@NonNull Context context) {
        super(context);
        this.initScreen();
        this.initCarousels();
    }

    public Carousels(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.Carousels);
        if (typedArray != null) {
            heightScale = typedArray.getFloat(R.styleable.Carousels_height_scale, -1);
            typedArray.recycle();
        }
        this.initScreen();
        this.initCarousels();
    }

    /**
     * initialization resolution
     */
    private void initScreen() {
        mDisplayMetrics = new DisplayMetrics();//屏幕分辨率容器
        if (getContext() instanceof Activity) {
            Activity mActivity = (Activity) getContext();
            mActivity.getWindowManager().getDefaultDisplay().getMetrics(mDisplayMetrics);
        }
    }

    /**
     * initialization measuring
     */
    private void initCarousels() {
        mViewPager = new CarouselViewPage(getContext());
        mCarouselPoints = new CarouselPoint(getContext());
        LayoutParams layoutParams = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.gravity = Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL;
        mCarouselPoints.setLayoutParams(layoutParams);
        mViewPager.setLayoutParams(new LayoutParams(mDisplayMetrics.widthPixels, (int) (heightScale * mDisplayMetrics.heightPixels)));
        addView(mViewPager);
        addView(mCarouselPoints);

        mViewPager.addOnPageChangeListener(this);
        mViewPager.addTouchChangeListener(this);

    }

    int viewPagePosition = 0;

    /**
     * @param views add views
     */
    IntervalCountDown intervalCountDown;

    public void setViews(List<View> views, int delayed) {
        CarouselsPagerAdapter carouselsPagerAdapter = new CarouselsPagerAdapter(views);
        mViewPager.setAdapter(carouselsPagerAdapter);
        this.drawPoints(views);
        final int length=views.size();
        if (intervalCountDown == null) {
            intervalCountDown = new IntervalCountDown();
            final android.os.Handler handler = new android.os.Handler(Looper.getMainLooper());
            intervalCountDown.postDelayed(delayed, new IntervalCountDown.IntervalExecute() {
                @Override
                public void onComplete() {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            if (length==0)return;
                            if (viewPagePosition >= length) {
                                viewPagePosition = 0;
                            }
                            mViewPager.setCurrentItem(viewPagePosition);
                            viewPagePosition++;
                        }
                    });
                }
            });
        }
    }

    public void restore(){
        if (!intervalCountDown.isSuspend()){
            intervalCountDown.restore();
        }
    }

    public void runing(){
        if (intervalCountDown.isSuspend()){
            intervalCountDown.rest();
        }
    }

    /**
     * @param bitmaps add pictures
     */
    public void setBitmaps(List<Bitmap> bitmaps, int delayed) {
        List<View> views = new ArrayList<>();
        for (Bitmap bitmap : bitmaps) {
            ImageView imageView = new ImageView(getContext());
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            imageView.setImageBitmap(bitmap);
            views.add(imageView);
        }
        setViews(views, delayed);
    }

    private void drawPoints(List<View> views) {
        mCarouselPoints.onDrawPointsLength(views.size());
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        //set child view size
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        //set current view size
        setMeasuredDimension(MeasureSpec.getSize(widthMeasureSpec), (int) (heightScale * mDisplayMetrics.heightPixels));
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        this.viewPagePosition = position;
        mCarouselPoints.setTransition(viewPagePosition);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void touchChangeLift(int value) {
        if (!intervalCountDown.isSuspend()) {
            intervalCountDown.restore();
        }
    }

    @Override
    public void touchChangeRight(int value) {
        if (!intervalCountDown.isSuspend()) {
            intervalCountDown.restore();
        }
    }

    @Override
    public void touchChangeReset() {
        if (intervalCountDown.isSuspend()) {
            intervalCountDown.rest();
        }
    }

    /**
     * add Adapter on ViewPager
     */
    private class CarouselsPagerAdapter extends PagerAdapter {

        private List<View> list;

        public CarouselsPagerAdapter(List<View> list) {
            this.list = list;
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            container.addView(list.get(position));
            return list.get(position);
        }

        @Override
        public void destroyItem(ViewGroup container, int position,
                                Object object) {
            container.removeView(list.get(position));
        }
    }

}
