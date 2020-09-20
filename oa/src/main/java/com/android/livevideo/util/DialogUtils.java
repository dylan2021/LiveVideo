package com.android.livevideo.util;

import android.app.Dialog;
import android.content.Context;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.text.InputFilter;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.android.livevideo.act_other.BaseFgActivity;
import com.android.livevideo.dialogfragment.SimpleDialogFragment;
import com.android.livevideo.R;
import com.android.livevideo.core.utils.ImageUtil;
import com.jzxiang.pickerview.TimePickerDialog;
import com.jzxiang.pickerview.data.Type;

import java.lang.reflect.Field;

/**
 * Gool Lee
 */

public class DialogUtils {

    public static void setDialogWindow(BaseFgActivity context, Dialog dialog, int gravity) {
        Window w = dialog.getWindow();
        w.setGravity(gravity);
        w.setType(WindowManager.LayoutParams.TYPE_APPLICATION_PANEL);

        WindowManager.LayoutParams params = w.getAttributes();
        //params.y = 20;  Dialog距离底部的距离
        params.width = ImageUtil.getScreenWidth(context);
        w.setAttributes(params);
        dialog.show();
    }
    public static void setDialogWindowFullScreen(BaseFgActivity context, Dialog dialog, int gravity) {
        Window window = dialog.getWindow();
        window.setGravity(gravity);
        window.setType(WindowManager.LayoutParams.TYPE_APPLICATION_PANEL);

        int dm80 = context.getResources().getDimensionPixelOffset(R.dimen.dm080);//Dialog距离底部的距离
        WindowManager.LayoutParams params = window.getAttributes();
        params.width = ImageUtil.getScreenWidth(context);
        params.height = ImageUtil.getScreenHeight(context);
        params.y = dm80;
        window.setAttributes(params);
        dialog.show();
    }

    public static void setDialogWindowShapeCenter(BaseFgActivity context, Dialog dialog) {
        Window dialogWindow = dialog.getWindow();
        dialogWindow.setBackgroundDrawable(ContextCompat.
                getDrawable(context, R.drawable.shape_f5f5f5_10px));
        dialogWindow.setGravity(Gravity.CENTER);
        dialogWindow.setType(WindowManager.LayoutParams.TYPE_APPLICATION_PANEL);

        WindowManager.LayoutParams params = dialogWindow.getAttributes();
        params.width = ImageUtil.getScreenWidth(context) - 60;
        dialogWindow.setAttributes(params);
        dialog.show();
    }

    public static void showTipDialog(BaseFgActivity activity, String content) {
        FragmentTransaction ft = activity.getSupportFragmentManager().beginTransaction();

        final SimpleDialogFragment dialogFragment = new SimpleDialogFragment();
        dialogFragment.setDialogWidth(255);
        dialogFragment.setCancelable(false);
        TextView tv = new TextView(activity);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams
                .MATCH_PARENT, ViewGroup
                .LayoutParams.MATCH_PARENT);
        params.setMargins(0, 20, 0, 0);
        params.gravity = Gravity.CENTER;
        tv.setLayoutParams(params);
        tv.setLineSpacing(0f, 1.5f);
        tv.setText(content);
        tv.setTextColor(ContextCompat.getColor(activity, R.color.color212121));
        tv.setTextSize(15.5F);
        dialogFragment.setContentView(tv);

        dialogFragment.setNegativeButton("确定", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogFragment.dismiss();
            }
        });
        dialogFragment.show(ft, "successDialog");
    }

    public static MaterialDialog.Builder getInputDialog(BaseFgActivity context, String hint) {
        final EditText et = new EditText(context);
        et.setPadding(100, 0, 100, 50);
        et.setBackground(null);
        et.setHint(hint);
        et.setTextSize(15);
        et.setHintTextColor(ContextCompat.getColor(context, R.color.color_hint));
        MaterialDialog.Builder builder = new MaterialDialog.Builder(context).title(" ")
                .customView(et, false)
                .negativeColorRes(R.color.mainColor)
                .negativeText(R.string.sure);

        et.setFocusable(true);
        et.setFocusableInTouchMode(true);
        et.requestFocus();

        showKeyBorad(et, context);

        return builder;
    }

    // 隐藏软键盘
    public static void hideKeyBorad(BaseFgActivity activity) {
        try {
            ((InputMethodManager) activity
                    .getSystemService(Context.INPUT_METHOD_SERVICE))
                    .hideSoftInputFromWindow(
                            activity.getCurrentFocus().getWindowToken(),
                            InputMethodManager.HIDE_NOT_ALWAYS);
        } catch (Exception e) {

        }
    }

    //显示软键盘
    public static void showKeyBorad(EditText et, final BaseFgActivity context) {
        et.post(new Runnable() {
            @Override
            public void run() {
                InputMethodManager inputMethodManager = (InputMethodManager) (context).getSystemService(Context.INPUT_METHOD_SERVICE);
                inputMethodManager.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
            }
        });
    }

    public static MaterialDialog.Builder getMoneyInputDialog(BaseFgActivity context) {
        final EditText et = new EditText(context);
        et.setPadding(100, 0, 100, 50);
        et.setBackground(null);
        et.setHint("请输入");
        try {
            Field f = TextView.class.getDeclaredField("mCursorDrawableRes");
            f.setAccessible(true);
            f.set(et, R.drawable.shape_cursor_color_main_color);
        } catch (Exception e) {
        }
        et.setTextSize(15);
        et.setHintTextColor(ContextCompat.getColor(context, R.color.color_hint));
        InputFilter[] filters = {new MoneyInputFilter()};
        et.setFilters(filters);
        MaterialDialog.Builder builder = new MaterialDialog.Builder(context).title(" ")
                .customView(et, false)
                .negativeColorRes(R.color.mainColor)
                .negativeText(R.string.sure);

        showKeyBorad(et, context);
        return builder;
    }

    public static MaterialDialog.Builder getMoneyInputDialog(BaseFgActivity context, String text) {
        final EditText et = new EditText(context);
        et.setPadding(100, 0, 100, 50);
        et.setBackground(null);

        try {
            Field cursorField = TextView.class.getDeclaredField("mCursorDrawableRes");
            cursorField.setAccessible(true);
            cursorField.set(et, R.drawable.shape_cursor_color_main_color);
        } catch (Exception e) {
        }
        if (text != null) {
            text = text.replace(".00", "");
            et.setText(text);
            et.setSelection(text.length());
        }
        et.setTextSize(15);
        et.setHintTextColor(ContextCompat.getColor(context, R.color.color_hint));
        InputFilter[] filters = {new MoneyInputFilter()};
        et.setFilters(filters);
        MaterialDialog.Builder builder = new MaterialDialog.Builder(context).title(" ")
                .customView(et, false)
                .negativeColorRes(R.color.mainColor)
                .negativeText(R.string.sure);

        showKeyBorad(et, context);
        return builder;
    }

    public static MaterialDialog.Builder getTwoBtDialog(BaseFgActivity context, String content) {
        MaterialDialog.Builder builder = new MaterialDialog.Builder(context)
                .contentColorRes(R.color.color212121)
                .content(content)
                .positiveText(R.string.sure)
                .negativeText(R.string.cancel)
                .positiveColorRes(R.color.mainColor)
                .negativeColorRes(R.color.color999);
        return builder;
    }

    public static MaterialDialog.Builder getDraftBoxDialog(BaseFgActivity context) {
        MaterialDialog.Builder builder = new MaterialDialog.Builder(context)
                .contentColorRes(R.color.color212121)
                .content(R.string.save_in_draft_box)
                .positiveText(R.string.yes)
                .negativeText(R.string.no)
                .positiveColorRes(R.color.mainColor)
                .negativeColorRes(R.color.mainColor);
        return builder;
    }


    public static TimePickerDialog.Builder getTimePicker(BaseFgActivity context) {
        TimePickerDialog.Builder builder = new TimePickerDialog.Builder();
        builder.setTitleStringId("")//标题
                .setCyclic(false)
                .setCancelStringId(context.getString(R.string.time_dialog_title_cancel))
                .setSureStringId(context.getString(R.string.time_dialog_title_sure))
                .setWheelItemTextSelectorColorId(context.getResources().getColor(R.color.mainColor))
                .setWheelItemTextNormalColorId(context.getResources().getColor(R.color.time_nomal_text_color))
                .setThemeColor(context.getResources().getColor(R.color.mainColorDrak))
                .setWheelItemTextSize(16)
                .setType(Type.YEAR_MONTH_DAY)
                .build();

        return builder;
    }

    public static TimePickerDialog.Builder getMonthPicker(BaseFgActivity context) {
        TimePickerDialog.Builder builder = new TimePickerDialog.Builder();
        builder.setCancelStringId("取消")
                .setSureStringId("确定")
                .setTitleStringId("")//标题
                .setCyclic(false)
                .setThemeColor(context.getResources().getColor(R.color.mainColor))
                .setWheelItemTextSize(16)
                .setType(Type.YEAR_MONTH)
                .build();
        return builder;
    }
}
