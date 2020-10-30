package com.mm.android.deviceaddmodule.mobilecommon.widget;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mm.android.deviceaddmodule.R;
import com.mm.android.deviceaddmodule.mobilecommon.utils.UIUtils;

/**
 * <p>
 * 工具类使用说明：左按钮，左内按钮，中间按钮，右内按钮，右按钮，均支持图片和文字背景二选一，左右按钮默认支持图片，中间按钮默认支持文字
 * </p>
 * <p>
 * xml使用注意：需要在xml文件中使用style="@style/common_titile"
 * </p>
 * <p>
 * 函数使用介绍：
 * </p>
 * <p>
 * initView(int tvLeftResId, int tvRightResId, int tvCenterResId) 初始化控件：左按钮，中间按钮，右按钮，属于普通用法
 * </p>
 * <p>
 * getTextViewXXX() 获取xxx按钮，进行自定义功能扩展操作。例如，实现展开下来listview的功能
 * </p>
 * <p>
 * setTitleXXXView(int resId, int colorId, int textSizeDimenId) 设置xxx按钮的文字，文字颜色，文字大小
 * </p>
 * <p>
 * setTitleXXX(int resId) 设置xxx按钮图片或文字
 * </p>
 * <p>
 * setTextColorXXX(int colorId) 设置xxx按钮文字的颜色资源
 * </p>
 * <p>
 * setTextSizeXXX(int textSizeDimenId) 设置xxx按钮文字的大小
 * </p>
 * <p>
 * setTitleTextXXX(int resId) 通过resId设置xxx按钮的文字
 * </p>
 * <p>
 * setTitleTextXXX(String titleTextXXX) 通过String设置xxx按钮的文字
 * </p>
 * <p>
 * setVisibleXXX(int flag) 通过flag设置xxx是否隐藏，flag:View.GONE,View.INVISIBLE,View.VISIBLE;
 * </p>
 */
public class CommonSubTitle extends RelativeLayout
// implements OnClickListener
{
    /**
     * 左侧按钮ID
     */
    public static final int ID_LEFT = 0;

    /**
     * 左侧内按钮ID
     */
    public static final int ID_LEFT_2 = 1;

    /**
     * 右侧按钮ID
     */
    public static final int ID_RIGHT = 2;

    /**
     * 右侧内按钮ID
     */
    public static final int ID_RIGHT_2 = 3;

    /**
     * 中间按钮ID, 暂时不加监听器
     */
    public static final int ID_CENTER = 4;
    
    /**
     * 中间下方按钮ID, 暂时不加监听器
     */
    public static final int ID_CENTER_SUB = 5;

    /**
     * 左侧按钮/ 左侧按钮LinearLayout
     */
    private TextView mTitleLeftTv;

    private LinearLayout mTitleLeftLl;

    /**
     * 左侧按钮(靠内) /左侧按钮(靠内)LinearLayout
     */
    private TextView mTitleLeft2Tv;

    private LinearLayout mTitleLeft2Ll;

    /**
     * 右侧按钮 /右侧按钮LinearLayout
     */
    private TextView mTitleRightTv;

    private LinearLayout mTitleRightLl;

    /**
     * 右侧按钮(靠内) /右侧按钮(靠内)LinearLayout
     */
    private TextView mTitleRight2Tv;

    private LinearLayout mTitleRight2Ll;

    /**
     * 大标题(上)/小标题(下)LinearLayout
     */
    private LinearLayout mTitleCenterLl;
    
    private TextView mTitleCenterTv;
    
    private TextView mTitleCenterSubTv;
    
    /**
     * 点击监听
     */
    private OnTitleSubClickListener mListener;

    private View mBottomV;

    /**
     * 默认隐藏左2和右2的按钮 ，创建一个新的实例CommonTitle.
     * 
     * @param context
     * @param attrs
     */
    public CommonSubTitle(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.widget_common_sub_title, this);
        initView();
        setListeners();
        setVisibleLeft2(View.GONE);
        setVisibleRight2(View.GONE);
    }

    private void initView() {
        mBottomV = findViewById(R.id.v_bottom_line);
        mTitleLeftLl = findViewById(R.id.ll_title_left);
        mTitleLeft2Ll = findViewById(R.id.ll_title_left2);
        mTitleRightLl = findViewById(R.id.ll_title_right);
        mTitleRight2Ll = findViewById(R.id.ll_title_right2);
        mTitleCenterLl = findViewById(R.id.ll_title_center);

        mTitleLeftTv = findViewById(R.id.tv_title_left);
        mTitleLeft2Tv = findViewById(R.id.tv_title_left2);
        mTitleRightTv = findViewById(R.id.tv_title_right);
        mTitleRight2Tv = findViewById(R.id.tv_title_right2);
        mTitleCenterTv = findViewById(R.id.tv_title_center);
        mTitleCenterSubTv = findViewById(R.id.tv_title_center_sub);

        mTitleLeftTv.setTextColor(getResources().getColor(R.color.common_title_text_color));
        mTitleLeft2Tv.setTextColor(getResources().getColor(R.color.common_title_text_color));
        mTitleRightTv.setTextColor(getResources().getColor(R.color.common_title_text_color));
        mTitleRight2Tv.setTextColor(getResources().getColor(R.color.common_title_text_color));
        mTitleCenterTv.setTextColor(getResources().getColor(R.color.c2));

        mTitleLeftTv.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources()
                .getDimensionPixelSize(R.dimen.text_size_mid));
        mTitleLeft2Tv.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                getResources().getDimensionPixelSize(R.dimen.text_size_mid));
        mTitleRightTv.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                getResources().getDimensionPixelSize(R.dimen.text_size_mid));
        mTitleRight2Tv.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                getResources().getDimensionPixelSize(R.dimen.text_size_mid));
        mTitleCenterTv.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                getResources().getDimensionPixelSize(R.dimen.text_size_large));
    }

    private void setListeners() {
        mTitleLeftLl.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.onCommonTitleClick(ID_LEFT);
                }

            }
        });

        mTitleLeft2Ll.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.onCommonTitleClick(ID_LEFT_2);
                }

            }
        });

        mTitleRightLl.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.onCommonTitleClick(ID_RIGHT);
                }

            }
        });

        mTitleRight2Ll.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null)
                    mListener.onCommonTitleClick(ID_RIGHT_2);
            }
        });

        mTitleCenterLl.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null)
                    mListener.onCommonTitleClick(ID_CENTER);
            }
        });
    }

    /**
     * <p>
     * 初始化参数，按钮均可支持图片或者文字背景
     * @param tvLeftResId
     *            左按钮
     * @param tvRightResId
     *            右按钮
     * @param tvCenterResId
     *            中间按钮
     */

    public void initView(int tvLeftResId, int tvRightResId, int tvCenterResId) {
        setTitleLeftView(tvLeftResId, 0, 0);
        setTitleRightView(tvRightResId, 0, 0);
        setTitleCenterView(tvCenterResId, 0, 0);
    }

    public TextView getTextViewCenter() {
        return mTitleCenterTv;
    }
    
    public TextView getTextViewCenterSub() {
        return mTitleCenterSubTv;
    }

    /**
     * <p>
     * 设置是否选中按钮
     * </p>
     * @param selected
     *            {true,false}
     * @param id
     *            {ID_LEFT ,ID_LEFT_2,ID_RIGHT,ID_RIGHT_2,ID_CENTER}
     */
    public void setTitleSelected(boolean selected, int id) {
        View v = findViewByID(id);
        if (v != null) {
            v.setSelected(selected);
        }
    }

    /**
     * <p>
     * 设置按钮是否可用
     * </p>
     */
    public void setTitleEnabled(boolean enabled, int id) {
        View v = findParentViewById(id);
        if (v != null) {
            UIUtils.setEnabledEX(enabled, v);
        }
    }

    private View findParentViewById(int id) {
        switch (id) {
            case ID_LEFT:
                return mTitleLeftLl;
            case ID_LEFT_2:
                return mTitleLeft2Ll;
            case ID_RIGHT:
                return mTitleRightLl;
            case ID_RIGHT_2:
                return mTitleRight2Ll;
            case ID_CENTER:
                return mTitleCenterLl;
            default:
                return null;
        }
    }

    private View findViewByID(int id) {
        switch (id) {
            case ID_LEFT:
                return mTitleLeftTv;
            case ID_LEFT_2:
                return mTitleLeft2Tv;
            case ID_RIGHT:
                return mTitleRightTv;
            case ID_RIGHT_2:
                return mTitleRight2Tv;
            case ID_CENTER:
                return mTitleCenterTv;
            case ID_CENTER_SUB:
                return mTitleCenterSubTv;
            default:
                return null;
        }
    }

    /*------------------------------------------getTextView END------------------------------*/

    /*-----------------------------------setTitleView START---------------------------------*/
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

    public void setTitleCenterView(int resId, int colorId, int textSizeDimenId) {
        setTitleCenter(resId);
        setTextColorCenter(colorId);
        setTextSizeCenter(textSizeDimenId);
    }

    /*-----------------------------------setTitleView END---------------------------------*/
    /*------------------------------------------setTitle START------------------------------*/
    /**
     * <p>
     * 设置左边按钮图片或文字
     * </p>
     * @param leftResId
     */
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

    /**
     * <p>
     * 设置左边按钮图片或文字
     * </p>
     */
    public void setTitleLeft2(int left2ResId) {
        if (mTitleLeft2Tv != null) {
            if (left2ResId != 0) {
                if (mTitleLeft2Ll != null && mTitleLeft2Ll.getVisibility() != View.VISIBLE)
                    mTitleLeft2Ll.setVisibility(VISIBLE);
                Drawable drawable = null;
                try {
                    drawable = getResources().getDrawable(left2ResId);
                } catch (Exception e) {
                    // e.printStackTrace();
                } finally {
                    if (drawable != null) {
                        mTitleLeft2Tv.setBackgroundResource(left2ResId);
                        mTitleLeft2Tv.setText(null);
                    } else {
                        mTitleLeft2Tv.setText(left2ResId);
                        mTitleLeft2Tv.setBackgroundResource(0);
                    }
                }
            } else {
                if (mTitleLeft2Ll != null)
                    mTitleLeft2Ll.setVisibility(INVISIBLE);
            }
        }
    }

    /**
     * <p>
     * 设置右边按钮图片或文字
     * </p>
     *
     * @param rightResId
     */
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

    public void setTitleRight2(int right2ResId) {
        if (mTitleRight2Tv != null) {
            if (right2ResId != 0) {
                if (mTitleRight2Ll != null && mTitleRight2Ll.getVisibility() != View.VISIBLE)
                    mTitleRight2Ll.setVisibility(VISIBLE);
                Drawable drawable = null;
                try {
                    drawable = getResources().getDrawable(right2ResId);
                } catch (Exception e) {
                    // e.printStackTrace();
                } finally {
                    if (drawable != null) {
                        mTitleRight2Tv.setBackgroundResource(right2ResId);
                        mTitleRight2Tv.setText(null);
                    } else {
                        mTitleRight2Tv.setText(right2ResId);
                        mTitleRight2Tv.setBackgroundResource(0);
                    }
                }
            } else {
                if (mTitleRight2Ll != null)
                    mTitleRight2Ll.setVisibility(INVISIBLE);
            }
        }
    }

    /**
     * <p>
     * 设置中间按钮图片或文字
     * </p>
     *
     * @param centerResId
     */
    public void setTitleCenter(int centerResId) {
        if (mTitleCenterTv != null) {
            if (centerResId != 0) {
                if (mTitleCenterLl != null && mTitleCenterLl.getVisibility() != View.VISIBLE)
                    mTitleCenterLl.setVisibility(VISIBLE);
                Drawable drawable = null;
                try {
                    drawable = getResources().getDrawable(centerResId);
                } catch (Exception e) {
                    // e.printStackTrace();
                } finally {
                    if (drawable != null) {
                        mTitleCenterTv.setBackgroundResource(centerResId);
                        mTitleCenterTv.setText(null);
                    } else {
                        mTitleCenterTv.setText(centerResId);
                        mTitleCenterTv.setBackgroundResource(0);
                    }
                }
            } else {
                if (mTitleCenterLl != null)
                    mTitleCenterLl.setVisibility(INVISIBLE);
            }
        }
    }
    
    /**
     * <p>
     * 设置中间下方按钮图片或文字
     * </p>
     * 
     * @param centerResId
     */
    public void setTitleCenterSub(int centerResId) {
        if (mTitleCenterSubTv != null) {
            if (centerResId != 0) {
                if (mTitleCenterLl != null && mTitleCenterLl.getVisibility() != View.VISIBLE)
                    mTitleCenterLl.setVisibility(VISIBLE);
                Drawable drawable = null;
                try {
                    drawable = getResources().getDrawable(centerResId);
                } catch (Exception e) {
                    // e.printStackTrace();
                } finally {
                    if (drawable != null) {
                        mTitleCenterSubTv.setBackgroundResource(centerResId);
                        mTitleCenterSubTv.setText(null);
                    } else {
                        mTitleCenterSubTv.setText(centerResId);
                        mTitleCenterSubTv.setBackgroundResource(0);
                    }
                }
            } else {
                if (mTitleCenterLl != null)
                    mTitleCenterLl.setVisibility(INVISIBLE);
            }
        }
    }

    /*------------------------------------------setTitle END------------------------------*/
    /*-----------------------------------------setTextColor START--------------------------------------*/
    public void setTextColorLeft(int colorId) {
        if (mTitleLeftTv != null) {
            mTitleLeftTv.setTextColor(colorId != 0 ? getResources().getColor(colorId) : getResources().getColor(
                    R.color.common_title_text_color));
        }
    }

    public void setTextColorLeft2(int colorId) {
        if (mTitleLeft2Tv != null) {
            mTitleLeft2Tv.setTextColor(colorId != 0 ? getResources().getColor(colorId) : getResources().getColor(
                    R.color.common_title_text_color));
        }
    }

    public void setTextColorRight(int colorId) {
        if (mTitleRightTv != null) {
            mTitleRightTv.setTextColor(colorId != 0 ? getResources().getColorStateList(colorId) : getResources()
                    .getColorStateList(R.color.common_title_text_color));
        }
    }

    public void setTextColorRight2(int colorId) {
        if (mTitleRight2Tv != null) {
            mTitleRight2Tv.setTextColor(colorId != 0 ? getResources().getColor(colorId) : getResources().getColor(
                    R.color.c43));
        }
    }

    public void setTextColorCenter(int colorId) {
        if (mTitleCenterTv != null) {
            mTitleCenterTv.setTextColor(colorId != 0 ? getResources().getColor(colorId) : getResources().getColor(
                    R.color.c2));
        }
    }
    
    public void setTextColorCenterSub(int colorId) {
        if (mTitleCenterSubTv != null) {
            mTitleCenterSubTv.setTextColor(colorId != 0 ? getResources().getColor(colorId) : getResources().getColor(
                    R.color.c2));
        }
    }

    /*-----------------------------------------setTextColor END--------------------------------------*/
    /*-----------------------------------------setTextSize START--------------------------------------*/
    public void setTextSizeLeft(int textSizeDimenId) {
        if (mTitleLeftTv != null) {
            mTitleLeftTv.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                    textSizeDimenId != 0 ? getResources().getDimensionPixelSize(textSizeDimenId) : getResources()
                            .getDimensionPixelSize(R.dimen.text_size_mid));
        }
    }

    public void setTextSizeLeft2(int textSizeDimenId) {
        if (mTitleLeft2Tv != null) {
            mTitleLeft2Tv.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                    textSizeDimenId != 0 ? getResources().getDimensionPixelSize(textSizeDimenId) : getResources()
                            .getDimensionPixelSize(R.dimen.text_size_mid));
        }
    }

    public void setTextSizeRight(int textSizeDimenId) {
        if (mTitleRightTv != null) {
            mTitleRightTv.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                    textSizeDimenId != 0 ? getResources().getDimensionPixelSize(textSizeDimenId) : getResources()
                            .getDimensionPixelSize(R.dimen.text_size_mid));
        }
    }

    public void setTextSizeRight2(int textSizeDimenId) {
        if (mTitleRight2Tv != null) {
            mTitleRight2Tv.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                    textSizeDimenId != 0 ? getResources().getDimensionPixelSize(textSizeDimenId) : getResources()
                            .getDimensionPixelSize(R.dimen.text_size_mid));
        }
    }

    public void setTextSizeCenter(int textSizeDimenId) {
        if (mTitleCenterTv != null) {
            mTitleCenterTv.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                    textSizeDimenId != 0 ? getResources().getDimensionPixelSize(textSizeDimenId) : getResources()
                            .getDimensionPixelSize(R.dimen.text_size_large));
        }
    }
    
    public void setTextSizeCenterSub(int textSizeDimenId) {
        if (mTitleCenterSubTv != null) {
            mTitleCenterSubTv.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                    textSizeDimenId != 0 ? getResources().getDimensionPixelSize(textSizeDimenId) : getResources()
                            .getDimensionPixelSize(R.dimen.text_size_small));
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

    public void setIconLeft2(int resId) {
        if (mTitleLeft2Ll != null) {
            if (mTitleLeft2Ll.getVisibility() != View.VISIBLE) {
                mTitleLeft2Ll.setVisibility(View.VISIBLE);
            }
            setTitleLeft2(resId);
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

    public void setIconRight2(int resId) {
        if (mTitleRight2Ll != null) {
            if (mTitleRight2Ll.getVisibility() != View.VISIBLE) {
                mTitleRight2Ll.setVisibility(View.VISIBLE);
            }
            setTitleRight2(resId);
        }
    }

    public void setIconCenter(int resId) {
        if (mTitleCenterLl != null) {
            if (mTitleCenterLl.getVisibility() != View.VISIBLE) {
                mTitleCenterLl.setVisibility(View.VISIBLE);
            }
            setTitleCenter(resId);
        }
    }

    /*-----------------------------------------setIcon END--------------------------------------*/
    /*-----------------------------------------setTextById START--------------------------------------*/

    public void setTitleTextLeft(int resId) {
        if (mTitleLeftLl != null) {
            if (mTitleLeftLl.getVisibility() != View.VISIBLE) {
                mTitleLeftLl.setVisibility(View.VISIBLE);
            }
            setTitleLeft(resId);
        }
    }

    public void setTitleTextLeft2(int resId) {
        if (mTitleLeft2Ll != null) {
            if (mTitleLeft2Ll.getVisibility() != View.VISIBLE) {
                mTitleLeft2Ll.setVisibility(View.VISIBLE);
            }
            setTitleLeft(resId);
        }
    }

    public void setTitleTextRight(int resId) {
        if (mTitleRightLl != null) {
            if (mTitleRightLl.getVisibility() != View.VISIBLE) {
                mTitleRightLl.setVisibility(View.VISIBLE);
            }
            setTitleRight(resId);
        }
    }

    public void setTitleTextRight2(int resId) {
        if (mTitleRight2Ll != null) {
            if (mTitleRight2Ll.getVisibility() != View.VISIBLE) {
                mTitleRight2Ll.setVisibility(View.VISIBLE);
            }
            setTitleRight2(resId);
        }
    }

    public void setTitleTextCenter(int resId) {
        if (mTitleCenterLl != null) {
            if (mTitleCenterLl.getVisibility() != View.VISIBLE) {
                mTitleCenterLl.setVisibility(View.VISIBLE);
            }
            setTitleCenter(resId);
        }
    }
    
    public void setTitleTextCenterSub(int resId) {
        if (mTitleCenterLl != null) {
            if (mTitleCenterLl.getVisibility() != View.VISIBLE) {
                mTitleCenterLl.setVisibility(View.VISIBLE);
            }
            setTitleCenterSub(resId);
        }
    }

    /*-----------------------------------------setTextById END--------------------------------------*/
    /*-----------------------------------------setTextByString START--------------------------------------*/
    public void setTitleTextLeft(String titleTextLeft) {
        if (mTitleLeftTv != null) {
            if (mTitleLeftTv.getVisibility() != View.VISIBLE) {
                mTitleLeftTv.setVisibility(View.VISIBLE);
            }
            mTitleLeftTv.setText(titleTextLeft);
            mTitleLeftTv.setBackgroundResource(0);
        }
    }

    public void setTitleTextLeft2(String titleTextLeft2) {
        if (mTitleLeft2Tv != null) {
            if (mTitleLeft2Tv.getVisibility() != View.VISIBLE) {
                mTitleLeft2Tv.setVisibility(View.VISIBLE);
            }
            mTitleLeft2Tv.setText(titleTextLeft2);
            mTitleLeft2Tv.setBackgroundResource(0);
        }
    }

    public void setTitleTextRight(String titleTextRight) {
        if (mTitleRightTv != null) {
            if (mTitleRightTv.getVisibility() != View.VISIBLE) {
                mTitleRightTv.setVisibility(View.VISIBLE);
            }
            mTitleRightTv.setText(titleTextRight);
            mTitleRightTv.setBackgroundResource(0);
        }
    }

    public void setTitleTextRight2(String titleTextRight2) {
        if (mTitleRight2Tv != null) {
            if (mTitleRight2Tv.getVisibility() != View.VISIBLE) {
                mTitleRight2Tv.setVisibility(View.VISIBLE);
            }
            mTitleRight2Tv.setText(titleTextRight2);
            mTitleRight2Tv.setBackgroundResource(0);
        }
    }

    public void setTitleTextCenter(String titleTextCenter) {
        if (mTitleCenterLl != null) {
            if (mTitleCenterLl.getVisibility() != View.VISIBLE) {
                mTitleCenterLl.setVisibility(View.VISIBLE);
            }
            mTitleCenterTv.setText(titleTextCenter);
            mTitleCenterTv.setBackgroundResource(0);
        }
    }
    
    public void setTitleTextCenterSub(String titleTextCenter) {
        if (mTitleCenterLl != null) {
            if (mTitleCenterLl.getVisibility() != View.VISIBLE) {
                mTitleCenterLl.setVisibility(View.VISIBLE);
            }
            mTitleCenterSubTv.setText(titleTextCenter);
            mTitleCenterSubTv.setBackgroundResource(0);
        }
    }

    /*-----------------------------------------setTextByString END--------------------------------------*/
    /*-----------------------------------------setVisible START--------------------------------------*/

    public void setVisibleLeft(int flag) {
        if (mTitleLeftLl != null) {
            mTitleLeftLl.setVisibility(flag);
        }
    }

    public void setVisibleLeft2(int flag) {
        if (mTitleLeft2Ll != null) {
            mTitleLeft2Ll.setVisibility(flag);
        }
    }

    public void setVisibleRight(int flag) {
        if (mTitleRightLl != null) {
            mTitleRightLl.setVisibility(flag);
        }
    }

    public void setVisibleRight2(int flag) {
        if (mTitleRight2Ll != null) {
            mTitleRight2Ll.setVisibility(flag);
        }
    }

    public void setVisibleCenter(int flag) {
        if (mTitleCenterLl != null) {
            mTitleCenterLl.setVisibility(flag);
        }
    }
    
    public void setVisibleCenterSub(int flag) {
        if (mTitleCenterSubTv != null) {
            mTitleCenterSubTv.setVisibility(flag);
        }
    }

    public void setVisibleBottom(int flag) {
        if (mBottomV != null) {
            mBottomV.setVisibility(flag);
        }
    }

    /*-----------------------------------------setVisible END--------------------------------------*/
    public void setOnTitleSubClickListener(OnTitleSubClickListener listener) {
        mListener = listener;
    }

    public interface OnTitleSubClickListener {
        public void onCommonTitleClick(int id);
    }
}
