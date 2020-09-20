package com.android.livevideo.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.widget.TextView;

import com.android.livevideo.R;


/**
 * Dylan
 * 用于作为标签显示的TextView
 * 边框默认与文字颜色一致
 */
@SuppressLint("AppCompatCustomView")
public class BorderLabelTextView extends TextView{
    // 默认边框宽度, 1dp
    public static final float DEFAULT_STROKE_WIDTH = 0.7f;
    // 默认圆角, 2dp
    public static final float DEFAULT_CORNER_RADIUS = 2.6f;
    // 默认左右内边距
    public static final float DEFAULT_LR_PADDING = 6f;
    // 默认上下内边距
    public static final float DEFAULT_TB_PADDING = 2f;

    // 边框线宽
    private int strokeWidth;
    // 边框颜色
    private int strokeColor;
    // 圆角半径
    private int cornerRadius;
    // 边框颜色是否跟随文字颜色
    private boolean mFollowTextColor=true;
    //实心或空心，默认空心
    private boolean isSolid;

    // 画边框所使用画笔对象
    private Paint mPaint = new Paint();
    // 画边框要使用的矩形
    private RectF mRectF;
    private DisplayMetrics displayMetrics;

    public BorderLabelTextView(Context context) {
        this(context, null);
    }

    public BorderLabelTextView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BorderLabelTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        // 将DIP单位默认值转为PX
        displayMetrics = context.getResources().getDisplayMetrics();
        strokeWidth = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, DEFAULT_STROKE_WIDTH, displayMetrics);
        cornerRadius = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, DEFAULT_CORNER_RADIUS, displayMetrics);

        // 读取属性值
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.BorderLabelTextView);
        strokeWidth = ta.getDimensionPixelSize(R.styleable.BorderLabelTextView_strokeWidth, strokeWidth);
        cornerRadius = ta.getDimensionPixelSize(R.styleable.BorderLabelTextView_cornerRadius, cornerRadius);
        strokeColor = ta.getColor(R.styleable.BorderLabelTextView_strokeColor, Color.TRANSPARENT);
        mFollowTextColor = ta.getBoolean(R.styleable.BorderLabelTextView_followTextColor, false);
        isSolid = ta.getBoolean(R.styleable.BorderLabelTextView_isSolid, false);
        ta.recycle();

        mRectF = new RectF();

        // 边框默认颜色与文字颜色一致
//        if(strokeColor == Color.TRANSPARENT){
//            strokeColor = getCurrentTextColor();
//        }

        // 如果使用时没有设置内边距, 设置默认边距
        int paddingLeft = getPaddingLeft() == 0 ? (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, DEFAULT_LR_PADDING, displayMetrics) : getPaddingLeft();
        int paddingRight = getPaddingRight() == 0 ? (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, DEFAULT_LR_PADDING, displayMetrics) : getPaddingRight();
        int paddingTop = getPaddingTop() == 0 ? (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, DEFAULT_TB_PADDING, displayMetrics) : getPaddingTop();
        int paddingBottom = getPaddingBottom() == 0 ? (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, DEFAULT_TB_PADDING, displayMetrics) : getPaddingBottom();
        setPadding(paddingLeft, paddingTop, paddingRight, paddingBottom);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if(isSolid){
            // 实心效果
            mPaint.setStyle(Paint.Style.FILL);
        }else{
            // 空心效果
            mPaint.setStyle(Paint.Style.STROKE);
        }

        // 设置画笔为无锯齿
        mPaint.setAntiAlias(true);
        // 线宽
        mPaint.setStrokeWidth(strokeWidth);

        // 设置边框线的颜色, 如果声明为边框跟随文字颜色且当前边框颜色与文字颜色不同时重新设置边框颜色
        if(mFollowTextColor && strokeColor != getCurrentTextColor()){
            strokeColor = getCurrentTextColor();
        }
        mPaint.setColor(strokeColor);

        // 画空心圆角矩形
        mRectF.left = mRectF.top = 0.5f * strokeWidth;
        mRectF.right = getMeasuredWidth() - strokeWidth;
        mRectF.bottom = getMeasuredHeight() - strokeWidth;
        canvas.drawRoundRect(mRectF, cornerRadius, cornerRadius, mPaint);

        super.onDraw(canvas);
        //drawRoundRect放在 super.onDraw(canvas)前面
        //如果drawRoundRect放在 super.onDraw(canvas)后面，父类先画TextView，然后再画子类矩形，
        //如果画的是实心矩形，则会遮盖父类的text字体
    }

    /**
     * 设置边框线宽度
     * @param size
     */
    public void setStrokeWidth(int size){
        strokeWidth = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, size, displayMetrics);
        invalidate();
    }

    /**
     * 设置边框颜色
     * @param color
     */
    public void setStrokeColor(int color){
        strokeColor = color;
        invalidate();
    }

    /**
     * 设置圆角半径
     * @param radius
     */
    public void setCornerRadius(int radius){
        cornerRadius = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, radius, displayMetrics);
        invalidate();
    }

    /**
     * 设置边框颜色是否跟随文字颜色
     * @param isFollow
     */
    public void setFollowTextColor(boolean isFollow){
        mFollowTextColor = isFollow;
        invalidate();
    }

    /**
     * 设置实心或空心，默认空心
     * @param solid
     */
    public void setSolid(boolean solid){
        isSolid = solid;
        invalidate();
    }

}
