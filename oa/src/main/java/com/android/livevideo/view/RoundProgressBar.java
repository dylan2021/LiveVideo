package com.android.livevideo.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import com.android.livevideo.R;

/**
 * 圆形进度条
 */
public class RoundProgressBar extends View {

    /**
     * 画笔对象的引用
     */
    private Paint paint;

    /**
     * 背景圆环的颜色
     */
    private int roundColor;
    /**
     * 背景圆环线宽度
     */
    private float roundWidth;

    /**
     * 进度圆的颜色
     */
    private int roundProgressColor;


    /**
     * 拇指圆半径
     */
    private float thumbRadius;
    /**
     * 拇指圆颜色
     */
    private float thumbColor;


    /**
     * 最大进度
     */
    private int max;

    /**
     * 当前进度
     */
    private int progress;

    /**
     * 进度的风格，实心或者空心
     */
    private int style;
    public static final int STROKE = 0;
    public static final int FILL = 1;

    public RoundProgressBar(Context context) {
        this(context, null);
    }

    public RoundProgressBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RoundProgressBar(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        paint = new Paint();


        TypedArray mTypedArray = context.obtainStyledAttributes(attrs,
                R.styleable.RoundProgressBar);

        //获取自定义属性和默认值

        max = mTypedArray.getInteger(R.styleable.RoundProgressBar_maxProgress, 100);
        /**
         * 背景圆
         */
        roundColor = mTypedArray.getColor(R.styleable.RoundProgressBar_roundColor, Color.GRAY);
        roundWidth = mTypedArray.getDimension(R.styleable.RoundProgressBar_roundWidth, 14);
        /**
         * 进度圆
         */
        roundProgressColor = mTypedArray.getColor(R.styleable.RoundProgressBar_roundProgressColor, Color.RED);
        /**
         * 拇指圆
         */
        thumbRadius = mTypedArray.getDimension(R.styleable.RoundProgressBar_thumbRadius, 28);
        thumbColor = mTypedArray.getColor(R.styleable.RoundProgressBar_thumbColor, Color.RED);


        style = mTypedArray.getInt(R.styleable.RoundProgressBar_style, 0);

        mTypedArray.recycle();
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        /**
         * 画最外层的大圆环
         */
        int centre = getWidth() / 2; //获取圆心的x坐标
        int radius = (int) (centre - thumbRadius); //圆环的半径
        paint.setColor(roundColor); //设置圆环的颜色
        paint.setStyle(Paint.Style.STROKE); //设置空心
        paint.setStrokeWidth(roundWidth); //设置圆环的宽度
        paint.setAntiAlias(true);  //消除锯齿
        canvas.drawCircle(centre, centre, radius, paint); //画出圆环

        /**
         * 画圆弧 ，画圆环的进度
         */
        BlurMaskFilter maskFilter = new BlurMaskFilter(10, BlurMaskFilter.Blur.SOLID);
        paint.setMaskFilter(maskFilter);
        //设置进度是实心还是空心
        paint.setStrokeWidth(roundWidth); //设置圆环的宽度
        paint.setColor(roundProgressColor);  //设置进度的颜色
        RectF oval = new RectF(centre - radius, centre - radius, centre
                + radius, centre + radius);  //用于定义的圆弧的形状和大小的界限
        paint.setStyle(Paint.Style.STROKE);
        //范围
        //起点
        //总进度
        canvas.drawArc(oval, -90, -360 * progress / max, false, paint);  //根据进度画圆弧


//        if (progress > 0 && progress < max) {
//            /**
//             * 画一个小圆
//             */
//            paint.setStrokeWidth(1);
//            paint.setStyle(Paint.Style.FILL);
//
//            float littleCentreX = centre;
//            float littleCentreY = centre - radius;
//            float lRadius = roundWidth * 0.5f;
//            RectF ovalLittle = new RectF(littleCentreX - lRadius, littleCentreY - lRadius, littleCentreX
//                    + lRadius, littleCentreY + lRadius);  //用于定义的圆弧的形状和大小的界限
//            canvas.drawArc(ovalLittle, 90, -180, false, paint);
//            /**
//             *画一个大圆
//             * */
//            float centreX = (float) (centre - radius * (Math.sin((float) progress / (float) max * Math.PI * 2)));
//            float centreY = (float) (centre - radius * (Math.cos((float) progress / (float) max * Math.PI * 2)));
//            canvas.drawCircle(centreX, centreY, thumbRadius, paint);
//        } else if (progress == 0) { //没电 进度为0
//            /**
//             * 画一个小圆
//             */
//            paint.setStrokeWidth(1);
//            paint.setStyle(Paint.Style.FILL);
//
//            float littleCentreX = centre;
//            float littleCentreY = centre - radius;
//            float lRadius = roundWidth * 0.5f;
//            RectF ovalLittle = new RectF(littleCentreX - lRadius, littleCentreY - lRadius, littleCentreX
//                    + lRadius, littleCentreY + lRadius);  //用于定义的圆弧的形状和大小的界限
//            canvas.drawArc(ovalLittle, 90, -360, false, paint);
//        }


        switch (style) {
            case STROKE: {
                paint.setStyle(Paint.Style.STROKE);
                break;
            }
            case FILL: {
                paint.setStyle(Paint.Style.FILL_AND_STROKE);
                if (progress != 0)
                    canvas.drawArc(oval, 0, 360 * progress / max, true, paint);  //根据进度画圆弧
                break;
            }
        }

    }


    public synchronized int getMax() {
        return max;
    }

    /**
     * 设置进度的最大值
     *
     * @param max
     */
    public synchronized void setMax(int max) {
        if (max < 0) {
            throw new IllegalArgumentException("max not less than 0");
        }
        this.max = max;
    }

    /**
     * 获取进度.需要同步
     *
     * @return
     */
    public synchronized int getProgress() {
        return progress;
    }

    /**
     * 设置进度，此为线程安全控件，由于考虑多线的问题，需要同步
     * 刷新界面调用postInvalidate()能在非UI线程刷新
     *
     * @param progress
     */
    public synchronized void setProgress(int progress) {
        if (progress < 0) {
            throw new IllegalArgumentException("progress not less than 0");
        }
        if (progress > max) {
            progress = max;
        }
        if (progress <= max) {
            this.progress = progress;
            postInvalidate();
        }
    }

    public int getRoundColor() {
        return roundColor;
    }

    public void setRoundColor(int roundColor) {
        this.roundColor = roundColor;
    }

    public float getRoundWidth() {
        return roundWidth;
    }

    public void setRoundWidth(float roundWidth) {
        this.roundWidth = roundWidth;
    }

    public int getRoundProgressColor() {
        return roundProgressColor;
    }

    public void setRoundProgressColor(int roundProgressColor) {
        this.roundProgressColor = roundProgressColor;
    }

    public float getThumbRadius() {
        return thumbRadius;
    }

    public void setThumbRadius(float thumbRadius) {
        this.thumbRadius = thumbRadius;
    }

    public float getThumbColor() {
        return thumbColor;
    }

    public void setThumbColor(float thumbColor) {
        this.thumbColor = thumbColor;
    }
}
