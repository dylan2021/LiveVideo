package com.mm.android.deviceaddmodule.mobilecommon.utils;

import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;

public class UIHelper {
    /**
     * 设置按钮选中与否
     * <p>
     * @param selected
     * @param views
     */
    public static void setSelected(boolean selected, View... views) {
        for (View view : views) {
            view.setSelected(selected);
        }
    }

    /**
     * 设置空间是否可用（不置灰）
     * <p>
     * </p>
     * @param enabled
     * @param views
     */
    public static void setEnabled(boolean enabled, View... views) {
        for (View view : views) {
            view.setEnabled(enabled);
        }
    }

    /**
     * 启用/禁用控件，包括所有子控件
     * <p>
     * </p>
     * @param enabled
     */
    public static void setEnabledSub(boolean enabled, ViewGroup... viewGroups) {
        for (ViewGroup viewGroup : viewGroups) {
            viewGroup.setEnabled(enabled);
            for (int i = 0; i < viewGroup.getChildCount(); i++) {
                View v = viewGroup.getChildAt(i);
                if (v instanceof ViewGroup) {
                    setEnabledSub(enabled, (ViewGroup) v);
                } else {
                    setEnabled(enabled, v);
                }
            }
        }
    }

    /**
     * 设置控件可用与否 (置灰)
     * 
     * @param enabled
     *            是否可用
     */
    public static void setEnabledEX(boolean enabled, View... views) {
        for (View view : views) {
            if (enabled) {
                view.setEnabled(enabled);
                view.clearAnimation();
            } else {
                Animation aniAlp = new AlphaAnimation(1f, 0.5f);
                aniAlp.setDuration(0);
                aniAlp.setInterpolator(new AccelerateDecelerateInterpolator());
                aniAlp.setFillEnabled(true);
                aniAlp.setFillAfter(true);
                view.startAnimation(aniAlp);
                view.setEnabled(enabled);
            }
        }
    }

    /**
     * 遍历布局，禁用/启用所有子控件
     * <p>
     * </p>
     * @param enabled
     */
    public static void setEnabledSubEX(boolean enabled, ViewGroup... viewGroups) {
        for (ViewGroup viewGroup : viewGroups) {
            setEnabledEX(enabled, viewGroup);
            for (int i = 0; i < viewGroup.getChildCount(); i++) {
                View v = viewGroup.getChildAt(i);
                if (v instanceof ViewGroup) {
                    setEnabledSubEX(enabled, (ViewGroup) v);
                } else {
                    setEnabledEX(enabled, v);
                }
            }
        }
    }

    /**
     * 隐藏/显示
     * <p>
     * </p>
     * @param visibility
     * @param views
     */
    public static void setVisibility(int visibility, View... views) {
        for (View view : views) {
            view.setVisibility(visibility);
        }
    }
}
