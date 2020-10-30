package com.lechange.demo.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.common.openapi.ClassInstanceManager;
import com.common.openapi.DeviceRecordService;
import com.common.openapi.IGetDeviceInfoCallBack;
import com.common.openapi.MethodConst;
import com.common.openapi.entity.DeviceDetailListData;
import com.common.openapi.entity.LocalRecordsData;
import com.common.openapi.entity.RecordListData;
import com.common.openapi.entity.RecordsData;
import com.lechange.demo.R;
import com.lechange.demo.adapter.DeviceRecordListAdapter;
import com.lechange.demo.tools.DateHelper;
import com.lechange.demo.tools.DialogUtils;
import com.lechange.demo.view.LcPullToRefreshRecyclerView;
import com.lechange.pulltorefreshlistview.Mode;
import com.lechange.pulltorefreshlistview.PullToRefreshBase;
import com.mm.android.deviceaddmodule.mobilecommon.utils.LogUtil;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DeviceLocalRecordListFragment extends Fragment implements View.OnClickListener, IGetDeviceInfoCallBack.IDeviceLocalRecordCallBack, PullToRefreshBase.OnRefreshListener2 {
    private static final String TAG = DeviceLocalRecordListFragment.class.getSimpleName();
    private Bundle arguments;
    private String searchDate;
    private long searchDate1;
    private DeviceDetailListData.ResponseData.DeviceListBean deviceListBean;
    private RecyclerView recyclerView;
    private TextView tvMonthDay;
    private long oneDay = 24 * 60 * 60 * 1000;
    private List<RecordListData> recordListDataList = new ArrayList<>();
    private DeviceRecordListAdapter deviceRecordListAdapter;
    private DeviceRecordService deviceRecordService = ClassInstanceManager.newInstance().getDeviceRecordService();
    private static DeviceLocalRecordListFragment fragment;
    private DeviceRecordListActivity deviceRecordListActivity;
    private LcPullToRefreshRecyclerView deviceList;
    private int pageSize = 30;
    private int pageIndex = 1;
    private String time = "";
    private TextView tvToday;

    public static DeviceLocalRecordListFragment newInstance() {
        fragment = new DeviceLocalRecordListFragment();
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        arguments = getArguments();
        searchDate = DateHelper.dateFormat(new Date(System.currentTimeMillis()));
        searchDate1 = DateHelper.parseMills(searchDate + " 00:00:00");
        deviceRecordListActivity = (DeviceRecordListActivity) getActivity();
        deviceRecordListActivity.llEdit.setVisibility(View.GONE);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_device_cloud_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
        if (arguments == null) {
            return;
        }
        deviceListBean = (DeviceDetailListData.ResponseData.DeviceListBean) arguments.getSerializable(MethodConst.ParamConst.deviceDetail);
        if (deviceListBean == null) {
            return;
        }
        initData();
    }

    private void initView(View view) {
        view.findViewById(R.id.iv_day_pre).setOnClickListener(this);
        view.findViewById(R.id.iv_day_next).setOnClickListener(this);
        tvMonthDay = view.findViewById(R.id.tv_month_day);
        deviceList = view.findViewById(R.id.record_list);
        tvToday = view.findViewById(R.id.tv_today);
        deviceList.setOnRefreshListener(this);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                refreshMode(Mode.PULL_FROM_START);
                refreshState(true);
            }
        }, 200);
        recyclerView = deviceList.getRefreshableView();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
    }

    private void refreshState(boolean refresh) {
        if (refresh) {
            deviceList.setRefreshing(true);
        } else {
            deviceList.onRefreshComplete();
        }
    }

    private void refreshMode(Mode mode) {
        deviceList.setMode(mode);
    }

    private void initData() {
        tvMonthDay.setText(searchDate);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.iv_day_pre) {
            searchDate1 = searchDate1 - oneDay;
            searchDate = DateHelper.dateFormat(new Date(searchDate1));
            tvMonthDay.setText(searchDate);
            initLocalRecord(false);
        } else if (id == R.id.iv_day_next) {
            searchDate1 = searchDate1 + oneDay;
            searchDate = DateHelper.dateFormat(new Date(searchDate1));
            tvMonthDay.setText(searchDate);
            initLocalRecord(false);
        }
    }

    @Override
    public void deviceLocalRecord(LocalRecordsData.Response result) {
        if (!isAdded()){
            return;
        }
        tvToday.setVisibility(View.GONE);
        recyclerView.setVisibility(View.VISIBLE);
        DialogUtils.dismiss();
        refreshState(false);
        if (result != null && result.data != null && result.data.records != null && result.data.records.size() > 0) {
            if (result.data.records.size() >= pageSize) {
                refreshMode(Mode.BOTH);
            } else {
                refreshMode(Mode.PULL_FROM_START);
            }
            recordListDataList = dealLocalRecord(result);
        } else {
            if (pageIndex == 1){
                tvToday.setVisibility(View.VISIBLE);
                recyclerView.setVisibility(View.GONE);
            }
            refreshMode(Mode.PULL_FROM_START);
        }
        showList();
    }

    @Override
    public void onError(Throwable throwable) {
        if (!isAdded()){
            return;
        }
        LogUtil.errorLog(TAG, "error", throwable);
        DialogUtils.dismiss();
        refreshState(false);
        refreshMode(Mode.PULL_FROM_START);
        Toast.makeText(getContext(), throwable.getMessage(), Toast.LENGTH_SHORT).show();
        pageIndex = 1;
        time = "";
        recordListDataList.clear();
        showList();
    }

    private void initLocalRecord(boolean isLoadMore) {
        if (isLoadMore) {
            pageIndex = pageIndex + pageSize;
        } else {
            pageIndex = 1;
            time = "";
            recordListDataList.clear();
        }
        DialogUtils.show(getActivity());
        LocalRecordsData localRecordsData = new LocalRecordsData();
        localRecordsData.data.deviceId = deviceListBean.deviceId;
        localRecordsData.data.channelId = deviceListBean.channels.get(deviceListBean.checkedChannel).channelId;
        localRecordsData.data.beginTime = searchDate + " 00:00:00";
        localRecordsData.data.endTime = searchDate + " 23:59:59";
        localRecordsData.data.type = "All";
        localRecordsData.data.queryRange =  pageIndex + "-" + (pageIndex + pageSize - 1);
        deviceRecordService.queryLocalRecords(localRecordsData, this);
    }

    private void showList() {
        if (deviceRecordListAdapter == null) {
            deviceRecordListAdapter = new DeviceRecordListAdapter(getContext(), recordListDataList);
            recyclerView.setAdapter(deviceRecordListAdapter);
        } else {
            deviceRecordListAdapter.notifyDataSetChanged();
        }
        deviceRecordListAdapter.setEditClickListener(new DeviceRecordListAdapter.EditClickListener() {
            @Override
            public void edit(int outPosition, int innerPosition) {
                LogUtil.debugLog(TAG, outPosition + "..." + innerPosition);
                Bundle bundle = new Bundle();
                bundle.putSerializable(MethodConst.ParamConst.deviceDetail, deviceListBean);
                bundle.putSerializable(MethodConst.ParamConst.recordData, recordListDataList.get(outPosition).recordsData.get(innerPosition));
                bundle.putInt(MethodConst.ParamConst.recordType, MethodConst.ParamConst.recordTypeLocal);
                Intent intent = new Intent(getContext(), DeviceRecordPlayActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
    }

    private List<RecordListData> dealLocalRecord(LocalRecordsData.Response result) {
        for (LocalRecordsData.ResponseData.RecordsBean recordsBean : result.data.records) {
            String innerTime = recordsBean.beginTime.substring(11, 13);
            RecordsData a = new RecordsData();
            a.recordType = 1;
            a.recordId = recordsBean.recordId;
            a.channelID = recordsBean.channelID;
            a.beginTime = recordsBean.beginTime;
            a.endTime = recordsBean.endTime;
            a.fileLength = recordsBean.fileLength;
            a.type = recordsBean.type;
            if (!innerTime.equals(time)) {
                RecordListData r = new RecordListData();
                r.period = innerTime + ":00";
                r.recordsData = new ArrayList<>();
                r.recordsData.add(a);
                recordListDataList.add(r);
                time = innerTime;
            } else {
                RecordListData b = recordListDataList.get(recordListDataList.size() - 1);
                b.recordsData.add(a);
            }
        }
        LogUtil.debugLog(TAG, recordListDataList.size() + "");
        return recordListDataList;
    }

    @Override
    public void onPullDownToRefresh(PullToRefreshBase pullToRefreshBase) {
        initLocalRecord(false);

    }

    @Override
    public void onPullUpToRefresh(PullToRefreshBase pullToRefreshBase) {
        initLocalRecord(true);
    }
}
