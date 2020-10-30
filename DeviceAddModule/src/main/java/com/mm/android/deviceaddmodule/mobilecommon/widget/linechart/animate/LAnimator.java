package com.mm.android.deviceaddmodule.mobilecommon.widget.linechart.animate;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;

import com.mm.android.deviceaddmodule.mobilecommon.widget.linechart.charts.LineChart;


public class LAnimator {

    float _hitValueY = 1;
    float _hitValueX = 1;

    LineChart _LineChart;

    public LAnimator(LineChart lineChart) {
        this._LineChart = lineChart;
    }


    public void animateX(long duration) {
        ValueAnimator animator = ObjectAnimator.ofFloat(0f, 1f);
        animator.setDuration(1000);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                _hitValueX = (float) animation.getAnimatedValue();

                _LineChart.postInvalidate();
            }
        });
        animator.start();
    }

    public void animateY(long duration) {
        ValueAnimator animator = ObjectAnimator.ofFloat(0f, 1f);
        animator.setDuration(1000);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                _hitValueY = (float) animation.getAnimatedValue();

                _LineChart.postInvalidate();
            }
        });
        animator.start();
    }

    public void animateXY(long duration) {
        ValueAnimator animator = ObjectAnimator.ofFloat(0f, 1f);
        animator.setDuration(1000);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                _hitValueX = (float) animation.getAnimatedValue();
                _hitValueY = _hitValueX;

                _LineChart.postInvalidate();
            }
        });
        animator.start();
    }

    public float get_hitValueY() {
        return _hitValueY;
    }

    public void set_hitValueY(float _hitValueY) {
        this._hitValueY = _hitValueY;
    }

    public float get_hitValueX() {
        return _hitValueX;
    }

    public void set_hitValueX(float _hitValueX) {
        this._hitValueX = _hitValueX;
    }
}
