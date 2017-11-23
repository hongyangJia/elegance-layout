package com.elegance.samples;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Adapter;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.elegance.view.Converter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hongyang on 17-11-23.
 */

public class BeseConverter implements Converter {
    List<String> list = new ArrayList<String>();

    private Context context;

    public BeseConverter(Context context) {
        list.add("ff");
        list.add("ff2");
        list.add("ff3");
        list.add("ff4");
        this.context = context;
    }

    @Override
    public int onCreateViewHolder() {
        return R.layout.item_layout;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public void onBindViewHolder(View holder, int position) {
        TextView textView = (TextView) holder.findViewById(R.id.title);
        textView.setText(list.get(position));
    }

    @Override
    public void onBindViewClick(int position) {
        Toast.makeText(context,String.valueOf(position),Toast.LENGTH_SHORT).show();
        Log.e("onBindViewClick",String.valueOf(position));
    }

}
