package com.mm.android.deviceaddmodule.mobilecommon.widget.linechart.adapter;

import java.text.DecimalFormat;

/**
 * 按天显示的报表，气泡X轴显示适配器
 */

public class DayHighLightValueAdapter implements IValueAdapter {
    private DecimalFormat _formatter;

    public DayHighLightValueAdapter() {
    }

    @Override
    public String value2String(double value) {
        return String.format("%02d:00", (int)value);

    }
}
