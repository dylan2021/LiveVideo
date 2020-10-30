package com.mm.android.deviceaddmodule.mobilecommon.businesstip;

public class BusinessErrorCode {
    private final static int BEC_COMMON_BASE = 0; // 通用

    private final static int BEC_DEVICE_BASE = 3000; // 设备管理模块

    /**
     * 未知错误
     */
    public final static int BEC_COMMON_UNKNOWN = BEC_COMMON_BASE + 1; // 未知错误

    /**
     * 业务空指针异常
     */
    public final static int BEC_COMMON_NULL_POINT = BEC_COMMON_BASE + 9;

    /**
     * 连接超时异常
     */
    public final static int BEC_COMMON_TIME_OUT = BEC_COMMON_BASE + 11;

    public static final int BEC_DEVICE_ADD_AP_UPPER_LIMIT = BEC_DEVICE_BASE + 65;
}