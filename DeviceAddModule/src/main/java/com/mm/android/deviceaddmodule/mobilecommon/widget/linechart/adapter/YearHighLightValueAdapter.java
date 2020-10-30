package com.mm.android.deviceaddmodule.mobilecommon.widget.linechart.adapter;

import java.text.DecimalFormat;

/**
 * 按年显示的报表，气泡X轴显示适配器
 */

public class YearHighLightValueAdapter implements IValueAdapter {
    private DecimalFormat _formatter;

    public YearHighLightValueAdapter() {
    }

    @Override
    public String value2String(double value) {
        return String.format("%02d月", (int)value);

    }
}
