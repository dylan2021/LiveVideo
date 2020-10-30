package com.mm.android.deviceaddmodule.mobilecommon.annotation;

import android.support.annotation.StringDef;

import java.lang.annotation.Retention;

import static com.mm.android.deviceaddmodule.mobilecommon.annotation.DeviceState.OFFLINE;
import static com.mm.android.deviceaddmodule.mobilecommon.annotation.DeviceState.ONLINE;
import static com.mm.android.deviceaddmodule.mobilecommon.annotation.DeviceState.SLEEP;
import static com.mm.android.deviceaddmodule.mobilecommon.annotation.DeviceState.UPGRADE;
import static java.lang.annotation.RetentionPolicy.SOURCE;

/**
 * 设备在离线状态枚举值
 */
@Retention(SOURCE)
@StringDef({ONLINE, OFFLINE, SLEEP,UPGRADE, ""})
public @interface DeviceState {
       String ONLINE = "online";
       String OFFLINE = "offline";
       String SLEEP = "sleep";
       String UPGRADE = "upgrading";

}
