package com.mm.android.deviceaddmodule.mobilecommon.widget;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.DrawableRes;
import android.support.annotation.StringRes;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mm.android.deviceaddmodule.R;
import com.mm.android.deviceaddmodule.mobilecommon.utils.UIUtils;


/**
 * <p>
 * xml使用注意：需要在xml文件中使用style="@style/common_switch_titile"
 * </p>
 * <p>
 * 函数使用介绍：
 * </p>
 * <p>
 * initView(int left, int right, int midleft, int midright) 初始化控件：左按钮，中间左按钮，中间右按钮，右按钮，属于普通用法
 * </p>
 */
public class CommonSwitchTitle extends RelativeLayout {

    /**
     * 左侧按钮ID
     */
    public static final int ID_LEFT = 0;

    /**
     * 右侧按钮ID
     */
    public static final int ID_MID_LEFT = 1;

    /**
     * 左侧按钮ID
     */
    public static final int ID_MID_RIGHT = 2;

    /**
     * 左侧按钮ID
     */
    public static final int ID_RIGHT = 3;

    /**
     * 左侧按钮
     */
    private TextView mTitleLeftTv;

    private LinearLayout mTitleLeftLl;

    /**
     * 右侧按钮
     */
    private TextView mTitleRightTv;

    private LinearLayout mTitleRightLl;

    /**
     * 文字标题(左)
     */
    private TextView mMidLeftTv;

    /**
     * 文字标题(右)
     */
    private TextView mMidRightTv;

    /**
     * 点击监听
     */
    private OnTitleClickListener mListener;
    /**
     * 底部横线
     */
    private View mBottomV;
    /**
     * tab切换区域
     */
    private ViewGroup mSwitchBtnLl;
    /**
     * 标题
     */
    private TextView mTitleTv;

    /**
     * 记录中间的左右是文字还是图片，主要兼容国内外差异
     */
    private boolean mIsMidLeftDrawable;

    public CommonSwitchTitle(Context context, AttributeSet attrs) {
        super(context, attrs);

        LayoutInflater.from(context).inflate(R.layout.mobile_common_widget_switch_title, this);
        initView();
        setListeners();
    }

    private void initView() {
        mTitleLeftLl = findViewById(R.id.ll_title_left);
        mTitleRightLl = findViewById(R.id.ll_title_right);
        mTitleLeftTv = findViewById(R.id.tv_title_left);
        mTitleRightTv = findViewById(R.id.tv_title_right);
        mMidLeftTv = findViewById(R.id.tag_left);
        mMidRightTv = findViewById(R.id.tag_right);
        mBottomV = findViewById(R.id.bottom_divider);
        mTitleLeftTv.setTextColor(getResources().getColor(R.color.c0));
        mTitleRightTv.setTextColor(getResources().getColor(R.color.c0));
        mMidLeftTv.setTextColor(getResources().getColor(R.color.common_title_tab_text_color));
        mMidRightTv.setTextColor(getResources().getColor(R.color.common_title_tab_text_color));

        mTitleLeftTv.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources()
                .getDimensionPixelSize(R.dimen.mobile_common_text_size_mid));
        mTitleRightTv.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                getResources().getDimensionPixelSize(R.dimen.mobile_common_text_size_mid));
        mMidLeftTv.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimensionPixelSize(R.dimen.text_size_mid));
        mMidRightTv
                .setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimensionPixelSize(R.dimen.text_size_mid));
        mSwitchBtnLl = findViewById(R.id.ll_switch_btn);
        mTitleTv = findViewById(R.id.tv_title_center);
    }


    public void setSelect(boolean isRight) {
        mSwitchBtnLl.setSelected(isRight);
        mMidLeftTv.setTextColor(isRight ? getResources().getColor(R.color.c5) : getResources().getColor(R.color.c0));
        mMidRightTv.setTextColor(isRight?getResources().getColor(R.color.c0) : getResources().getColor(R.color.c5));
    }

    /**
     * 切换标题栏展示样式
     *
     * @param isSwitchMode
     */
    public void changeTitleMode(boolean isSwitchMode) {
        if (isSwitchMode) {
            mSwitchBtnLl.setVisibility(VISIBLE);
            mTitleTv.setVisibility(GONE);
        } else {
            mSwitchBtnLl.setVisibility(GONE);
            mTitleTv.setVisibility(VISIBLE);
        }
    }

    /**
     * 设置标题栏中间内容
     *
     * @param resId
     */
    public void setTitleCenter(int resId) {
        mTitleTv.setText(getResources().getString(resId));
    }


    private void setListeners() {
        mTitleLeftLl.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (mListener != null)
                    mListener.onCommonTitleClick(ID_LEFT);

            }
        });
        mTitleRightLl.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (mListener != null)
                    mListener.onCommonTitleClick(ID_RIGHT);
            }
        });
        mMidLeftTv.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (mIsMidLeftDrawable){
                    UIUtils.setEnabled(false, v);
                    UIUtils.setEnabled(true, mMidRightTv);
                }else{
                    mSwitchBtnLl.setSelected(false);
                }

                if (mListener != null)
                    mListener.onCommonTitleClick(ID_MID_LEFT);
            }
        });
        mMidRightTv.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (mIsMidLeftDrawable){
                    UIUtils.setEnabled(false, v);
                    UIUtils.setEnabled(true, mMidLeftTv);
                }else{
                    mSwitchBtnLl.setSelected(true);
                }

                if (mListener != null)
                    mListener.onCommonTitleClick(ID_MID_RIGHT);
            }
        });

    }

    /**
     * 初始化
     *
     * @param left
     * @param right
     * @param midLeft
     * @param midRight
     */
    public void initView(int left, int right, int midLeft, int midRight) {
        setTitleLeftView(left, 0, 0);
        setTitleRightView(right, 0, 0);
        Drawable drawable = null;
        try {
            drawable = getResources().getDrawable(midLeft);
        } catch (Exception e) {
            // e.printStackTrace();
        } finally {
            if (drawable != null) {
                mIsMidLeftDrawable = true;
                setMidLeftImageSrc(midLeft);
            } else {
                mIsMidLeftDrawable = false;
                setMidLeftText(midLeft);
            }
        }
        try {
            drawable = getResources().getDrawable(midRight);
        } catch (Exception e) {
            // e.printStackTrace();
        } finally {
            if (drawable != null) {
                setMidRightImageSrc(midRight);
            } else {
                setMidRightText(midRight);
            }
        }
    }

    /*-----------------------------------setTitleXXXView setMidXXXView   START---------------------------------*/
    public void setTitleLeftView(int resId, int colorId, int textSizeDimenId) {
        setTitleLeft(resId);
        setTextColorLeft(colorId);
        setTextSizeLeft(textSizeDimenId);
    }

    public void setTitleRightView(int resId, int colorId, int textSizeDimenId) {
        setTitleRight(resId);
        setTextColorRight(colorId);
        setTextSizeRight(textSizeDimenId);
    }


    /*-----------------------------------setTitleXXXView setMidXXXView END---------------------------------*/
    /*------------------------------------------setTitle START------------------------------*/
    public void setTitleLeft(int leftResId) {
        if (mTitleLeftTv != null) {
            if (leftResId != 0) {
                if (mTitleLeftLl != null && mTitleLeftLl.getVisibility() != View.VISIBLE)
                    mTitleLeftLl.setVisibility(VISIBLE);
                Drawable drawable = null;
                try {
                    drawable = getResources().getDrawable(leftResId);
                } catch (Exception e) {
                    // e.printStackTrace();
                } finally {
                    if (drawable != null) {
                        mTitleLeftTv.setBackgroundResource(leftResId);
                        mTitleLeftTv.setText(null);
                    } else {
                        mTitleLeftTv.setText(leftResId);
                        mTitleLeftTv.setBackgroundResource(0);
                    }
                }
            } else {
                if (mTitleLeftLl != null)
                    mTitleLeftLl.setVisibility(INVISIBLE);
            }
        }
    }

    public void setTitleRight(int rightResId) {
        if (mTitleRightTv != null) {
            if (rightResId != 0) {
                if (mTitleRightLl != null && mTitleRightLl.getVisibility() != View.VISIBLE)
                    mTitleRightLl.setVisibility(VISIBLE);
                Drawable drawable = null;
                try {
                    drawable = getResources().getDrawable(rightResId);
                } catch (Exception e) {
                    // e.printStackTrace();
                } finally {
                    if (drawable != null) {
                        mTitleRightTv.setBackgroundResource(rightResId);
                        mTitleRightTv.setText(null);
                    } else {
                        mTitleRightTv.setText(rightResId);
                        mTitleRightTv.setBackgroundResource(0);
                    }
                }
            } else {
                if (mTitleRightLl != null)
                    mTitleRightLl.setVisibility(INVISIBLE);
            }
        }
    }

    public void setMidLeftText(@StringRes int midLeftResId) {
        if (mMidLeftTv != null) {
            if (midLeftResId != 0) {
                if (mMidLeftTv.getVisibility() != View.VISIBLE) {
                    mMidLeftTv.setVisibility(View.VISIBLE);
                }
                mMidLeftTv.setText(midLeftResId);
            } else {
                mMidLeftTv.setVisibility(INVISIBLE);
            }
        }
    }

    public void setMidLeftImageSrc(@DrawableRes int midLeftResId) {
        if (mMidLeftTv != null) {
            if (midLeftResId != 0) {
                if (mMidLeftTv.getVisibility() != View.VISIBLE) {
                    mMidLeftTv.setVisibility(View.VISIBLE);
                }
                mMidLeftTv.setBackgroundResource(midLeftResId);
            } else {
                mMidLeftTv.setVisibility(INVISIBLE);
            }
        }
    }

    public void setMidRightText(@StringRes int midRightResId) {
        if (mMidRightTv != null) {
            if (midRightResId != 0) {
                if (mMidRightTv.getVisibility() != View.VISIBLE) {
                    mMidRightTv.setVisibility(View.VISIBLE);
                }
                mMidRightTv.setText(midRightResId);
            } else {
                mMidRightTv.setVisibility(INVISIBLE);
            }
        }
    }

    public void setMidRightImageSrc(@DrawableRes int midRightResId) {
        if (mMidRightTv != null) {
            if (midRightResId != 0) {
                if (mMidRightTv.getVisibility() != View.VISIBLE) {
                    mMidRightTv.setVisibility(View.VISIBLE);
                }
                mMidRightTv.setBackgroundResource(midRightResId);
            } else {
                mMidRightTv.setVisibility(INVISIBLE);
            }
        }
    }

    /*-----------------------------------------setTextColor START--------------------------------------*/
    public void setTextColorLeft(int colorResId) {
        if (mTitleLeftTv != null) {
            mTitleLeftTv.setTextColor(colorResId != 0 ? getResources().getColor(colorResId) : getResources().getColor(
                    R.color.c0));
        }
    }

    public void setTextColorRight(int colorResId) {
        if (mTitleRightTv != null) {
            mTitleRightTv.setTextColor(colorResId != 0 ? getResources().getColor(colorResId) : getResources().getColor(
                    R.color.c0));
        }
    }


    /*-----------------------------------------setTextColor END--------------------------------------*/
    /*-----------------------------------------setTextSize START--------------------------------------*/
    public void setTextSizeLeft(int textSizeDimenId) {
        if (mTitleLeftTv != null) {
            mTitleLeftTv.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                    textSizeDimenId != 0 ? getResources().getDimensionPixelSize(textSizeDimenId) : getResources()
                            .getDimensionPixelSize(R.dimen.mobile_common_text_size_mid));
        }
    }

    public void setTextSizeRight(int textSizeDimenId) {
        if (mTitleRightTv != null) {
            mTitleRightTv.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                    textSizeDimenId != 0 ? getResources().getDimensionPixelSize(textSizeDimenId) : getResources()
                            .getDimensionPixelSize(R.dimen.mobile_common_text_size_mid));
        }
    }


    /*-----------------------------------------setTextSize END--------------------------------------*/
    /*-----------------------------------------setIcon START--------------------------------------*/

    public void setIconLeft(int resId) {
        if (mTitleLeftLl != null) {
            if (mTitleLeftLl.getVisibility() != View.VISIBLE) {
                mTitleLeftLl.setVisibility(View.VISIBLE);
            }
            setTitleLeft(resId);
        }
    }

    public void setIconRight(int resId) {
        if (mTitleRightLl != null) {
            if (mTitleRightLl.getVisibility() != View.VISIBLE) {
                mTitleRightLl.setVisibility(View.VISIBLE);
            }
            setTitleRight(resId);
        }
    }


    public void setVisibleBottomDivider(int flag) {
        if (mBottomV != null) {
            mBottomV.setVisibility(flag);
        }
    }


    public void setEnabled(boolean enabled, int id) {
        View parent = findParentViewById(id);
        if (parent != null) {

            View v = findViewByID(id);
            if (v != null) {

                parent.setEnabled(enabled);
                v.setEnabled(enabled);
            }
        }
    }


    private View findParentViewById(int id) {
        switch (id) {
            case ID_LEFT:
                return mTitleLeftLl;

            case ID_RIGHT:
                return mTitleRightLl;

            default:
                return null;
        }
    }

    public View findViewByID(int id) {
        switch (id) {
            case ID_LEFT:
                return mTitleLeftTv;
            case ID_RIGHT:
                return mTitleRightTv;
            case ID_MID_LEFT:
                return mMidLeftTv;
            case ID_MID_RIGHT:
                return mMidRightTv;
            default:
                return null;
        }
    }

    public void setOnTitleClickListener(OnTitleClickListener listener) {
        mListener = listener;
    }

    public interface OnTitleClickListener {
        void onCommonTitleClick(int id);
    }
}
