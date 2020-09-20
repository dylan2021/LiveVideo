package com.android.livevideo.core.utils;

import android.text.InputFilter;
import android.text.Spanned;

/**
 * 播放器控制类
 * Created by zeng on 2016/7/23.
 */
public class MyInputFilter implements InputFilter {
    public MyInputFilter(int dotLength) {
        this.dotLength = dotLength;
    }

    int dotLength;

    @Override
    public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
        // source:当前输入的字符
        // start:输入字符的开始位置
        // end:输入字符的结束位置
        // dest：当前已显示的内容
        // dstart:当前光标开始位置
        // dent:当前光标结束位置
        if (dest.length() == 0 && source.equals(".")) {
            return "0.";
        }
        String dValue = dest.toString();
        String[] splitArray = dValue.split("\\.");
        if (splitArray.length > 1) {
            String dotValue = splitArray[1];
            if (dotValue.length() == dotLength) {//输入框小数的位数
                return "";
            }
        }
        return null;
    }
}
