package com.mm.android.deviceaddmodule.mobilecommon.base;

import java.util.Map;

public interface ICacheUseMap<T1,T2> {
    void put(T1 key, T2 data);     //更新数据
    void clearAll();            //清除全部数据
    void clear(T1 key);         //清除数据
    Map<T1, T2> getList();      //获取数据
    void remove(T1 key);
}
