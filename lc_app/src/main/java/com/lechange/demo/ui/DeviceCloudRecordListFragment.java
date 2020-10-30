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
import com.common.openapi.entity.CloudRecordsData;
import com.common.openapi.entity.DeleteCloudRecordsData;
import com.common.openapi.entity.DeviceDetailListData;
import com.common.openapi.entity.RecordListData;
import com.common.openapi.entity.RecordsData;
import com.lechange.demo.R;
import com.lechange.demo.adapter.DeviceRecordListAdapter;
import com.lechange.demo.tools.DateHelper;
import com.lechange.demo.tools.DialogUtils;
import com.lechange.demo.view.LcPullToRefreshRecyclerView;
import com.lechange.pulltorefreshlistview.Mode;
import com.lechange.pulltorefreshlistview.PullToRefreshBase;
import com.mm.android.deviceaddmodule.mobilecommon.dialog.LCAlertDialog;
import com.mm.android.deviceaddmodule.mobilecommon.utils.LogUtil;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DeviceCloudRecordListFragment extends Fragment implements View.OnClickListener, IGetDeviceInfoCallBack.IDeviceCloudRecordCallBack, IGetDeviceInfoCallBack.IDeviceDeleteRecordCallBack, PullToRefreshBase.OnRefreshListener2 {
    private static final String TAG = DeviceCloudRecordListFragment.class.getSimpleName();
    private Bundle arguments;
    private String searchDate;
    private long searchDate1;
    private DeviceDetailListData.ResponseData.DeviceListBean deviceListBean;
    private RecyclerView recyclerView;
    private TextView tvMonthDay;
    private DeviceRecordService deviceRecordService = ClassInstanceManager.newInstance().getDeviceRecordService();
    private List<RecordListData> recordListDataList = new ArrayList<>();
    private DeviceRecordListAdapter deviceRecordListAdapter;
    private long oneDay = 24 * 60 * 60 * 1000;
    private static DeviceCloudRecordListFragment fragment;
    private DeviceRecordListActivity deviceRecordListActivity;
    private boolean editStatus = false;
    private TextView tvDelete;
    private List<String> recordRegionIds = new ArrayList<>();
    private LcPullToRefreshRecyclerView deviceList;
    private int pageSize = 30;
    private long nextRecordId = -1;
    private LCAlertDialog mLCAlertDialog;
    private TextView tvToday;

    public static DeviceCloudRecordListFragment newInstance() {
        fragment = new DeviceCloudRecordListFragment();
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        arguments = getArguments();
        searchDate = DateHelper.dateFormat(new Date(System.currentTimeMillis()));
        searchDate1 = DateHelper.parseMills(searchDate + " 00:00:00");
        deviceRecordListActivity = (DeviceRecordListActivity) getActivity();
        deviceRecordListActivity.llEdit.setOnClickListener(this);
        deviceRecordListActivity.llAll.setOnClickListener(this);
        deviceRecordListActivity.llEdit.setVisibility(View.VISIBLE);
        deviceRecordListActivity.llAll.setVisibility( View.GONE);
        deviceRecordListActivity.llBack.setVisibility( View.VISIBLE);
    }

    public void deleteCloudRecord() {
        if (!isAdded()) {
            return;
        }
        initCloudRecord(false);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_device_cloud_list, container, false);
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            deviceRecordListActivity.llEdit.setVisibility(View.VISIBLE);
            deviceRecordListActivity.llAll.setVisibility(editStatus ? View.VISIBLE : View.GONE);
            deviceRecordListActivity.llBack.setVisibility(editStatus ? View.GONE : View.VISIBLE);
        } else {
            deviceRecordListActivity.llEdit.setVisibility(View.GONE);
            deviceRecordListActivity.llAll.setVisibility(View.GONE);
            deviceRecordListActivity.llBack.setVisibility(View.VISIBLE);
        }
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
        tvDelete = view.findViewById(R.id.tv_delete);
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
        tvDelete.setOnClickListener(this);
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

    private void initCloudRecord(boolean isLoadMore) {
        if (isLoadMore) {
            nextRecordId = nextRecordId;
        } else {
            nextRecordId = -1;
            time = "";
            recordListDataList.clear();
        }
        DialogUtils.show(getActivity());
        CloudRecordsData cloudRecordsData = new CloudRecordsData();
        cloudRecordsData.data.deviceId = deviceListBean.deviceId;
        cloudRecordsData.data.channelId = deviceListBean.channels.get(deviceListBean.checkedChannel).channelId;
        cloudRecordsData.data.beginTime = searchDate + " 00:00:00";
        cloudRecordsData.data.endTime = searchDate + " 23:59:59";
        cloudRecordsData.data.nextRecordId = nextRecordId;
        cloudRecordsData.data.count = pageSize;
        deviceRecordService.getCloudRecords(cloudRecordsData, this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.iv_day_pre) {
            searchDate1 = searchDate1 - oneDay;
            searchDate = DateHelper.dateFormat(new Date(searchDate1));
            tvMonthDay.setText(searchDate);
            initCloudRecord(false);
        } else if (id == R.id.tv_delete) {
            recordRegionIds.clear();
            for (RecordListData recordListData : recordListDataList) {
                for (RecordsData recordsData : recordListData.recordsData) {
                    if (recordsData.check) {
                        recordRegionIds.add(recordsData.recordRegionId);
                    }
                }
            }
            if (recordRegionIds.size() == 0) {
                return;
            }
            LCAlertDialog.Builder builder = new LCAlertDialog.Builder(getContext());
            builder.setTitle(R.string.lc_demo_device_delete_sure);
            builder.setMessage("");
            builder.setCancelButton(com.mm.android.deviceaddmodule.R.string.common_cancel, null);
            builder.setConfirmButton(com.mm.android.deviceaddmodule.R.string.common_confirm,
                    new LCAlertDialog.OnClickListener() {
                        @Override
                        public void onClick(LCAlertDialog dialog, int which, boolean isChecked) {
                            //删除
                            DialogUtils.show(getActivity());
                            DeleteCloudRecordsData deleteCloudRecordsData = new DeleteCloudRecordsData();
                            deleteCloudRecordsData.data.recordRegionIds = recordRegionIds;
                            deviceRecordService.deleteCloudRecords(deleteCloudRecordsData, DeviceCloudRecordListFragment.this);
                        }
                    });

            mLCAlertDialog = builder.create();
            mLCAlertDialog.show(getActivity().getSupportFragmentManager(), "delete");
        } else if (id == R.id.iv_day_next) {
            searchDate1 = searchDate1 + oneDay;
            searchDate = DateHelper.dateFormat(new Date(searchDate1));
            tvMonthDay.setText(searchDate);
            initCloudRecord(false);
        } else if (id == R.id.ll_edit) {
            editStatus(!editStatus);
            if (editStatus && recordListDataList.size() <= 0) {
                return;
            }
            for (RecordListData recordListData : recordListDataList) {
                for (RecordsData recordsData : recordListData.recordsData) {
                    recordsData.check = false;
                }
            }
            deviceRecordListAdapter.notifyDataSetChanged();
        } else if (id == R.id.ll_all) {
            if (recordListDataList.size() <= 0) {
                return;
            }
            for (RecordListData recordListData : recordListDataList) {
                for (RecordsData recordsData : recordListData.recordsData) {
                    recordsData.check = true;
                }
            }
            deviceRecordListAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void deviceCloudRecord(CloudRecordsData.Response result) {
        if (!isAdded()) {
            return;
        }
        tvToday.setVisibility(View.GONE);
        recyclerView.setVisibility(View.VISIBLE);
        DialogUtils.dismiss();
        refreshState(false);
        editStatus(false);
        if (result != null && result.data != null && result.data.records != null && result.data.records.size() > 0) {
            if (result.data.records.size() >= pageSize) {
                refreshMode(Mode.BOTH);
                nextRecordId = Long.parseLong(result.data.records.get(result.data.records.size() - 1).recordId);
            } else {
                refreshMode(Mode.PULL_FROM_START);
            }
            recordListDataList = dealCloudRecord(result);
        } else {
            if (nextRecordId==-1){
                tvToday.setVisibility(View.VISIBLE);
                recyclerView.setVisibility(View.GONE);
            }
            refreshMode(Mode.PULL_FROM_START);
        }
        showList();
    }

    private void editStatus(boolean b) {
        editStatus = b;
        tvDelete.setVisibility(editStatus ? View.VISIBLE : View.GONE);
        deviceRecordListActivity.tvEdit.setText(editStatus ? getResources().getString(R.string.lc_demo_device_record_edit_finish) : getResources().getString(R.string.lc_demo_device_record_edit));
        deviceRecordListActivity.llAll.setVisibility(editStatus ? View.VISIBLE : View.GONE);
        deviceRecordListActivity.llBack.setVisibility(editStatus ? View.GONE : View.VISIBLE);
    }

    @Override
    public void deleteDeviceRecord() {
        if (!isAdded()) {
            return;
        }
        DialogUtils.dismiss();
        initCloudRecord(false);
    }

    @Override
    public void onError(Throwable throwable) {
        if (!isAdded()) {
            return;
        }
        LogUtil.errorLog(TAG, "error", throwable);
        DialogUtils.dismiss();
        refreshState(false);
        refreshMode(Mode.PULL_FROM_START);
        Toast.makeText(getContext(), throwable.getMessage(), Toast.LENGTH_SHORT).show();
        nextRecordId = -1;
        time = "";
        recordListDataList.clear();
        editStatus(false);
        showList();
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
                if (editStatus) {
                    recordListDataList.get(outPosition).recordsData.get(innerPosition).check = !recordListDataList.get(outPosition).recordsData.get(innerPosition).check;
                    deviceRecordListAdapter.notifyDataSetChanged();
                } else {
                    Bundle bundle = new Bundle();
                    bundle.putSerializable(MethodConst.ParamConst.deviceDetail, deviceListBean);
                    bundle.putSerializable(MethodConst.ParamConst.recordData, recordListDataList.get(outPosition).recordsData.get(innerPosition));
                    bundle.putInt(MethodConst.ParamConst.recordType, MethodConst.ParamConst.recordTypeCloud);
                    Intent intent = new Intent(getContext(), DeviceRecordPlayActivity.class);
                    intent.putExtras(bundle);
                    startActivityForResult(intent, 0);
                }
            }
        });
    }

    String time = "";

    private List<RecordListData> dealCloudRecord(CloudRecordsData.Response result) {
        for (CloudRecordsData.ResponseData.RecordsBean recordsBean : result.data.records) {
            String innerTime = recordsBean.beginTime.substring(11, 13);
            RecordsData a = new RecordsData();
            a.recordType = 0;
            a.recordId = recordsBean.recordId;
            a.deviceId = recordsBean.deviceId;
            a.channelID = recordsBean.channelId;
            a.beginTime = recordsBean.beginTime;
            a.endTime = recordsBean.endTime;
            a.size = recordsBean.size;
            a.thumbUrl = recordsBean.thumbUrl;
            a.encryptMode = recordsBean.encryptMode;
            a.recordRegionId = recordsBean.recordRegionId;
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
        initCloudRecord(false);
    }

    @Override
    public void onPullUpToRefresh(PullToRefreshBase pullToRefreshBase) {
        initCloudRecord(true);
    }
}
