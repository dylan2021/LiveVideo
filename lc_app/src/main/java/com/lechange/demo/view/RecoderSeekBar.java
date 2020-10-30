package com.lechange.demo.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

public class RecoderSeekBar extends android.support.v7.widget.AppCompatSeekBar {

    private boolean canTouchAble = true;

    public RecoderSeekBar(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public RecoderSeekBar(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public RecoderSeekBar(Context context) {
        super(context);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (!canTouchAble) {
            return true;
        }
        return super.onTouchEvent(event);
    }

    public void setCanTouchAble(boolean canTouchAble) {
        this.canTouchAble = canTouchAble;
    }
}
