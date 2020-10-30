package com.mm.android.deviceaddmodule.mobilecommon.widget.linechart.listener;

public interface IDragListener {
    /**
     * @param xMin 当前图谱中可见的x轴上的最小值
     * @param xMax 当前图谱中可见的x轴上的最大值
     */
    void onDrag(double xMin, double xMax);
}
