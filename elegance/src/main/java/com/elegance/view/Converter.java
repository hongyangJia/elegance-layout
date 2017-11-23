package com.elegance.view;

import android.view.View;
import android.view.ViewGroup;

/**
 * Created by hongyang on 17-11-23.
 */

public interface Converter {
      int  onCreateViewHolder();
      int  getCount();
      void onBindViewHolder(View holder, int position);
      void onBindViewClick(int position);
}
