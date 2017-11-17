package com.elegance.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.elegance.R;

import java.util.List;

/**
 * Created by hongyang on 17-11-17.
 */

public class ConciseView extends ViewGroup {

    public static final int GRID_ELEGANT = 0x00;
    public static final int CIRCLE_ELEGANT = 0x01;
    public static final int GRID_HORIZONTAL = 2;
    public static final int CIRCLE_VERTICAL = 4;
    private static final int CENTER = 2;
    private OnItemClickListener onItemClickListener;
    private int elegantType = GRID_ELEGANT;
    public ConciseView(Context context) {
        super(context);
    }

    public ConciseView(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.ConciseView);
        if (typedArray != null) {
            elegantType = typedArray.getInt(R.styleable.ConciseView_layout_concise_style, GRID_ELEGANT);
            typedArray.recycle();
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            View children = getChildAt(i);
            int childrenMeasuredWidth = children.getMeasuredWidth();
            int paddingTop = getPaddingTop();
            int paddingBottom = getPaddingBottom();
            int childrenHeight = children.getMeasuredHeight();
            if (children.getVisibility() != View.GONE) {
                switch (elegantType) {
                    case GRID_ELEGANT:
                        layoutGrid(i, children, paddingTop, childrenHeight, paddingBottom);
                        break;
                    case CIRCLE_ELEGANT:
                        layoutCircle(i, children, childCount, childrenMeasuredWidth, paddingTop, paddingBottom);
                        break;

                }
            }
        }
    }

    private void layoutGrid(final int position, final View children, int paddingTop, int childrenHeight, int paddingBottom) {
        int offset = getWidth() / GRID_HORIZONTAL;
        if (position < GRID_HORIZONTAL) {
            children.layout(offset * position, paddingTop, offset * (position + 1), childrenHeight + paddingBottom);
        } else {
            children.layout(offset * (position - CENTER), childrenHeight + paddingTop, offset * (position - CENTER + 1), childrenHeight * CENTER + paddingBottom);
        }
        this.performItemClick(children,position);
    }

    private void layoutCircle(int position, View children, int childCount, int childrenMeasuredWidth, int paddingTop, int paddingBottom) {
        int offset = getWidth() / childCount;
        int childrenLift = offset * position + offset / CENTER - childrenMeasuredWidth / CENTER;
        int childrenRight = offset * position + offset / CENTER + childrenMeasuredWidth / CENTER;
        children.layout(childrenLift, paddingTop, childrenRight,children.getMeasuredHeight() +paddingBottom);
        this.performItemClick(children,position);
    }

    private void performItemClick(final View children, final int position){
        if (onItemClickListener!=null){
            children.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClickListener.onItemClick(children,position);
                }
            });
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int childCount = getChildCount();
        int height = 0;
        for (int i = 0; i < childCount; i++) {
            View children = getChildAt(i);
            measureChild(children, widthMeasureSpec, heightMeasureSpec);
            switch (elegantType) {
                case GRID_ELEGANT:
                    height = children.getMeasuredHeight() * CENTER;
                    break;
                case CIRCLE_ELEGANT:
                    if (childCount <= CIRCLE_VERTICAL) {
                        height = children.getMeasuredHeight();
                    }
                    break;
            }
        }
        setMeasuredDimension(getDefaultSize(getSuggestedMinimumWidth(), widthMeasureSpec),
                height + getPaddingTop() + getPaddingBottom());
    }

    public void setPictures(List<Bitmap> bitmaps){
        ImageView imageView;
        for (Bitmap bitmap:bitmaps){
           imageView = new ImageView(getContext());
           imageView.setImageBitmap(bitmap);
           addView(imageView);
        }
    }

    public void setElegantType(int elegantType){
        this.elegantType=elegantType;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener){
        this.onItemClickListener=onItemClickListener;
    }

    public interface  OnItemClickListener{
           void  onItemClick(View view, int position);
    }

}
