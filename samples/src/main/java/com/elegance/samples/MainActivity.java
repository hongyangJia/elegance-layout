package com.elegance.samples;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.transition.Transition;
import com.elegance.carousel.Carousels;
import com.elegance.util.IntervalCountDown;
import com.elegance.view.ConciseView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {


    private static final String url_01="http://img1.xiazaizhijia.com/walls/20151216/mid_833ed2e63b5d6fb.jpg";
    private static final String url_02="http://img4.xiazaizhijia.com/walls/20151216/mid_4522b1b011e653a.jpg";
    private static final String url_03="http://img3.xiazaizhijia.com/walls/20160324/mid_16d36c56d13ef7f.jpg";
    private static final String url_04="http://img1.xiazaizhijia.com/walls/20160909/mid_f35e64458b91d62.jpg";
    private static final String url_05="http://img1.xiazaizhijia.com/walls/20160909/mid_f35e64458b91d62.jpg";
    Carousels  conciseView;
    private static View v;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        conciseView=  findViewById(R.id.conciseView);

    }

    private void init(){
        String[] s = new String[]{url_01,url_02,url_03,url_04,url_05};
        List<View> a = new ArrayList<>();
        for (String ss:s){
            ImageView imageView = new ImageView(this);
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            Glide.with(this).load(ss).into(imageView);
            a.add(imageView);
        }
        conciseView.setViews(a,3000);
    }
    @Override
    protected void onResume() {
        this.init();
        conciseView.startCarouse();
        super.onResume();
    }

    @Override
    protected void onStop() {
        conciseView.stopCarouse();
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        conciseView=null;
        super.onDestroy();
    }
}
