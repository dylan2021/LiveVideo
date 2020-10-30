package com.common.openapi.entity;

import com.lechange.demo.adapter.DeviceInnerRecordListAdapter;

import java.io.Serializable;
import java.util.List;

public class RecordListData implements Serializable {
    public String period;
    public List<RecordsData> recordsData;
    public DeviceInnerRecordListAdapter deviceInnerRecordListAdapters;
}
