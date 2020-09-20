/*
 * 	Flan.Zeng 2011-2016	http://git.oschina.net/signup?inviter=flan
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.android.livevideo.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;

import com.android.livevideo.R;
import com.android.livevideo.util.Utils;

/**
 * 不可滚动的listview，
 *  Gool Lee
 * scrollview嵌套listview的冲突
 */
public class NoScrollListView extends LinearLayout {

    public final String TAG = NoScrollListView.class.getSimpleName();

    private BaseAdapter adapter;
    private Context context;
    private OnClickListener listener;
    private int dividerHeight;

    public NoScrollListView(Context context) {
        super(context, null);
    }

    public NoScrollListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.NoScrollListView);
        dividerHeight = typedArray.getInt(R.styleable.NoScrollListView_dividerHeight, 4);
        typedArray.recycle();

        setOrientation(VERTICAL);
    }

    public BaseAdapter getAdapter() {
        return adapter;
    }

    /**
     * 设置适配器
     *
     * @param adapter
     */
    public void setAdapter(BaseAdapter adapter) {
        this.adapter = adapter;
        notifyChange();
    }

    public void notifyDataSetChanged() {

        this.removeAllViews();

        notifyChange();
    }

    /**
     * 将adapter中的View绑定到LinearLayout中，并显示
     */
    private void notifyChange() {

        if (adapter != null) {

            int itemCount = adapter.getCount();

            LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.setMargins(0, Utils.dip2px(context, dividerHeight), 0, 0);

            for (int i = 0; i < itemCount; i++) {

                final View view = adapter.getView(i, null, null);
                view.setId(i);
                view.setLayoutParams(params);

                view.setOnTouchListener(new OnTouchListener() {

                    int count = 0;

                    @Override
                    public boolean onTouch(View v, MotionEvent event) {

                        switch (event.getAction()) {
                            case MotionEvent.ACTION_DOWN:
                                count = 0;
                                break;
                            case MotionEvent.ACTION_MOVE:
                                count++;
                                if (count > 2) {
                                    view.setBackgroundColor(getResources().getColor(R.color.orange));
                                }
                                break;
                            case MotionEvent.ACTION_UP:
                                view.setBackgroundColor(getResources().getColor(R.color.white));
                                if (listener != null)
                                    listener.onClick(v);
                                break;
                            case MotionEvent.ACTION_CANCEL:
                                count = 0;
                                view.setBackgroundColor(getResources().getColor(R.color.white));
                                break;
                        }
                        return true;
                    }
                });

                addView(view, i);

            }
        }
    }

    /**
     * 设置每个Item被点击后的事件
     */
    public void setOnItemClickListener(OnClickListener listener) {
        this.listener = listener;
    }

}
