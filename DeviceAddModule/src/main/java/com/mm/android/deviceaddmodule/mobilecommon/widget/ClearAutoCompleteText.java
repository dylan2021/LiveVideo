package com.mm.android.deviceaddmodule.mobilecommon.widget;

import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.mm.android.deviceaddmodule.mobilecommon.utils.StringUtils;

public class ClearAutoCompleteText extends android.support.v7.widget.AppCompatAutoCompleteTextView {// delete the onFocusChangeListener,
                                                         // incorrect use, at 20150512
    private Drawable imgLeftFoucus = null;

    private Drawable imgLeftUnFoucus = null;

    private Drawable imgRightFoucus = null;

    private Drawable imgRightUnFoucus = null;

    private Context context;

    private int mLimitedLen = -1;

    private boolean mbUnregFilter = false;

    private boolean mbPWDFilter = false;

    private boolean mbFocus = false;

    private int errorTip;

    private TextWatcher mTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            ClearAutoCompleteText.this.removeTextChangedListener(mTextWatcher);
            filter(s);
            setDrawable();
            ClearAutoCompleteText.this.addTextChangedListener(mTextWatcher);
        }
    };

    public ClearAutoCompleteText(Context context) {
        super(context);
        this.context = context;
        init();
    }

    public ClearAutoCompleteText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.context = context;
        init();
    }

    public ClearAutoCompleteText(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init();
    }

    private void init() {
        addTextChangedListener(mTextWatcher);
        // setOnFocusChangeListener(this); //not need, deleted at 20150512
        setDrawable();
    }

    /**
     * 输入框是否可以复制|粘贴
     * 
     * @param copyAble
     */
    public void setCopyAble(boolean copyAble) {
        this.setLongClickable(copyAble);
    }

    /**
     * 设置编辑框前后图像
     * 
     * @param leftFocusResId
     *            （获取焦点时显示）当为0时表示不显示，其它则取资源文件
     * @param leftUnFocusResId
     *            （失去焦点时显示）当为0时表示不显示，其它则取资源文件
     * @param rightFocusResId
     *            当为0时表示不显示，其它则取资源文件
     * @param rightUnFocusResId
     *            当为0时表示不显示，其它则取资源文件
     */
    public void setDrawables(int leftFocusResId, int leftUnFocusResId, int rightFocusResId, int rightUnFocusResId) {

        if (leftFocusResId > 0) {
            imgLeftFoucus = context.getResources().getDrawable(leftFocusResId);
        }

        if (leftUnFocusResId > 0) {
            imgLeftUnFoucus = context.getResources().getDrawable(leftUnFocusResId);
        }

        if (rightFocusResId > 0) {
            imgRightFoucus = context.getResources().getDrawable(rightFocusResId);
        }

        if (rightUnFocusResId > 0) {
            imgRightUnFoucus = context.getResources().getDrawable(rightUnFocusResId);
        } else {
            imgRightUnFoucus = null;
        }

        setDrawable();
    }

    public void setFocus(boolean mFocus) {
        mbFocus = mFocus;
    }

    /**
     * 设置删除图片
     */
    private void setDrawable() {
        if (mbFocus) {
            if (length() == 0) {
                setCompoundDrawablesWithIntrinsicBounds(imgLeftFoucus, null, null, null);
            } else {
                setCompoundDrawablesWithIntrinsicBounds(imgLeftFoucus, null, imgRightFoucus, null);
            }
        } else {
            if (length() == 0) {
                setCompoundDrawablesWithIntrinsicBounds(imgLeftUnFoucus, null, null, null);
            } else {
                setCompoundDrawablesWithIntrinsicBounds(imgLeftUnFoucus, null, imgRightUnFoucus, null);
            }
        }
    }

    /**
     * event.getX() 获取相对应自身左上角的X坐标 event.getY() 获取相对应自身左上角的Y坐标 getWidth() 获取控件的宽度
     * getTotalPaddingRight() 获取删除图标左边缘到控件右边缘的距离 getPaddingRight() 获取删除图标右边缘到控件右边缘的距离 getWidth() -
     * getTotalPaddingRight() 计算删除图标左边缘到控件左边缘的距离 getWidth() - getPaddingRight() 计算删除图标右边缘到控件左边缘的距离
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (imgRightFoucus != null && event.getAction() == MotionEvent.ACTION_UP) {
            int x = (int) event.getX();
            // 判断触摸点是否在水平范围内
            boolean isInnerWidth = (x > (getWidth() - getTotalPaddingRight()))
                    && (x < (getWidth() - getPaddingRight()));
            // 获取删除图标的边界，返回一个Rect对象
            Rect rect = imgRightFoucus.getBounds();
            // 获取删除图标的高度
            int height = rect.height();
            int y = (int) event.getY();
            // 计算图标底部到控件底部的距离
            int distance = (getHeight() - height) / 2;
            // 判断触摸点是否在竖直范围内(可能会有点误差)
            // 触摸点的纵坐标在distance到（distance+图标自身的高度）之内，则视为点中删除图标
            boolean isInnerHeight = (y > distance) && (y < (distance + height));
            if (isInnerWidth && isInnerHeight && mbFocus) {
                setText("");
            }

        }

        return super.onTouchEvent(event);
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
    }

    public void onFocusChangeSelf(View v, boolean hasFocus) {
        mbFocus = hasFocus;
        setDrawable();
    }

    @Override
    protected void onFocusChanged(boolean focused, int direction, Rect previouslyFocusedRect) {

        super.onFocusChanged(focused, direction, previouslyFocusedRect);

        onFocusChangeSelf(this, focused);
    }

    /**
     * 设置最大输入长度，1个中文计两个英文，且自动屏蔽不规则字符
     * 
     * @param maxlen
     */
    public void setMaxLenth(int maxlen) {
        if (maxlen > 0) {
            mLimitedLen = maxlen;
        } else {
            mLimitedLen = 20;
        }
    }

    /**
     * 是否限制特殊字符输入
     * 
     * @param enbale
     */
    public void setUnregFilterEnbale(boolean enbale) {
        mbUnregFilter = enbale;
    }

    /**
     * 是否限中文及制特殊字符输入，常用于密码输入
     * 
     * @param enbale
     */
    public void setCHFilterEnbale(boolean enbale) {
        mbPWDFilter = enbale;
    }

    private int calcultateLength(CharSequence c) {
        int len = 0;
        for (int i = 0; i < c.length(); i++) {
            int tmp = (int) c.charAt(i);
            if (tmp > 0 && tmp < 127) {
                len++;
            } else {
                len += 2;
            }
        }
        return len;
    }

    private void filter(Editable s) {
        String str = s.toString();
        int indexStart = ClearAutoCompleteText.this.getSelectionStart();

        if (mbPWDFilter) {
            str = StringUtils.strPwdFilter(str);
            int iDelLen = s.length() - str.length();
            if (iDelLen > 0) {
                ClearAutoCompleteText.this.setText(str);
                if (indexStart - iDelLen >= 0 && indexStart - iDelLen <= str.length()) {
                    ClearAutoCompleteText.this.setSelection(indexStart - iDelLen);
                    indexStart -= iDelLen;
                }
            }
        } else if (mbUnregFilter) {
            str = StringUtils.strFilter(s.toString());
            int iDelLen = s.length() - str.length();
            if (iDelLen > 0) {
                ClearAutoCompleteText.this.setText(str);
                if (indexStart - iDelLen >= 0 && indexStart - iDelLen <= str.length()) {
                    ClearAutoCompleteText.this.setSelection(indexStart - iDelLen);
                    indexStart -= iDelLen;
                }
            }
        }

        limitLenght(str, indexStart);
    }

    private void limitLenght(String str, int indexStart) {
        if (mLimitedLen > 0) {

            boolean bFlag = false;
            if (calcultateLength(str) > mLimitedLen) {
                bFlag = true;
            }
            while (calcultateLength(str) > mLimitedLen) {
                if (indexStart > 0 && indexStart <= str.length()) {
                    str = str.substring(0, indexStart - 1) + str.substring(indexStart, str.length());
                } else {
                    str = str.substring(0, str.length() - 1);
                }
                indexStart = indexStart - 1;
            }

            if (bFlag) {
                ClearAutoCompleteText.this.setText(str);
                if (indexStart >= 0 && indexStart <= str.length()) {
                    ClearAutoCompleteText.this.setSelection(indexStart);
                }
            }
        }
    }

    public int getErrorTip() {
        return errorTip;
    }

    public void setErrorTip(int errorTip) {
        this.errorTip = errorTip;
    }

}