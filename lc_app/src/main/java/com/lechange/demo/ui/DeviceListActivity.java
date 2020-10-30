package com.lechange.demo.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.common.openapi.ClassInstanceManager;
import com.common.openapi.DeviceListService;
import com.common.openapi.IGetDeviceInfoCallBack;
import com.common.openapi.MethodConst;
import com.common.openapi.entity.DeviceDetailListData;
import com.common.openapi.entity.DeviceListData;
import com.lechange.demo.R;
import com.lechange.demo.adapter.DeviceListAdapter;
import com.lechange.demo.view.LcPullToRefreshRecyclerView;
import com.lechange.pulltorefreshlistview.Mode;
import com.lechange.pulltorefreshlistview.PullToRefreshBase;
import com.mm.android.deviceaddmodule.LCDeviceEngine;
import com.mm.android.deviceaddmodule.mobilecommon.utils.LogUtil;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class DeviceListActivity extends Activity implements
        PullToRefreshBase.OnRefreshListener2, View.OnClickListener {
    private static final String TAG = DeviceListActivity.class.getSimpleName();
    private LcPullToRefreshRecyclerView deviceList;
    private RecyclerView mRecyclerView;
    private List<DeviceDetailListData.ResponseData.DeviceListBean> datas = new ArrayList<>();
    private DeviceListAdapter deviceListAdapter;
    private LinearLayout llAdd;
    private LinearLayout llBack;
    private RelativeLayout rlNoDevice;
    //乐橙分页index
    public long baseBindId = -1;
    //开放平台分页index
    public long openBindId = -1;
    private DeviceListActivity context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_list);
        context=this;
        initView();
    }

    private void initView() {
        llAdd = findViewById(R.id.ll_add);
        llBack = findViewById(R.id.ll_back);
        rlNoDevice = findViewById(R.id.rl_no_device);
        deviceList = findViewById(R.id.device_list);
        deviceList.setOnRefreshListener(this);
        llAdd.setOnClickListener(this);
        llBack.setOnClickListener(this);
        refreshState(false);
        mRecyclerView = deviceList.getRefreshableView();
        LinearLayoutManager layout = new LinearLayoutManager(context);
        layout.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(layout);
        deviceListAdapter = new DeviceListAdapter(context, datas);
        mRecyclerView.setAdapter(deviceListAdapter);
        deviceListAdapter.setOnItemClickListener(new DeviceListAdapter.OnItemClickListener() {
            @Override
            public void onSettingClick(int position) {
                if (datas.size() == 0) {
                    return;
                }
                Bundle bundle = new Bundle();
                bundle.putSerializable(MethodConst.ParamConst.deviceDetail, datas.get(position));
                bundle.putString(MethodConst.ParamConst.fromList, MethodConst.ParamConst.fromList);
                Intent intent = new Intent(context, DeviceDetailActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);
            }

            @Override
            public void onDetailClick(int position) {
                if (datas.size() == 0) {
                    return;
                }
                if (!datas.get(position).status.equals("online")) {
                    return;
                }
                Bundle bundle = new Bundle();
                bundle.putSerializable(MethodConst.ParamConst.deviceDetail, datas.get(position));
                Intent intent = new Intent(context, DeviceOnlineMediaPlayActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);
            }

            @Override
            public void onChannelClick(int outPosition, int innerPosition) {
                if (datas.size() == 0) {
                    return;
                }
                if (!datas.get(outPosition).channels.get(innerPosition).status.equals("online")) {
                    return;
                }
                Bundle bundle = new Bundle();
                DeviceDetailListData.ResponseData.DeviceListBean deviceListBean = datas.get(outPosition);
                deviceListBean.checkedChannel = innerPosition;
                bundle.putSerializable(MethodConst.ParamConst.deviceDetail, deviceListBean);
                Intent intent = new Intent(context, DeviceOnlineMediaPlayActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    public void onPullDownToRefresh(PullToRefreshBase pullToRefreshBase) {
        getDeviceList(false);
    }

    @Override
    public void onPullUpToRefresh(PullToRefreshBase pullToRefreshBase) {
        getDeviceList(true);
    }

    private void getDeviceList(boolean isLoadMore) {
        if (!isLoadMore) {
            baseBindId = -1;
            openBindId = -1;
            datas.clear();
        }
        DeviceListService deviceVideoService = ClassInstanceManager.newInstance().
                getDeviceListService();
        DeviceListData deviceListData = new DeviceListData();
        deviceListData.data.openBindId = this.openBindId;
        deviceListData.data.baseBindId = this.baseBindId;
        deviceVideoService.deviceBaseList(deviceListData, new IGetDeviceInfoCallBack.IDeviceListCallBack() {
            @Override
            public void deviceList(DeviceDetailListData.Response responseData) {
                    if (isDestroyed()) {
                        return;
                    }
                    refreshState(false);
                    if (responseData.baseBindId != -1) {
                        baseBindId = responseData.baseBindId;
                    }
                    if (responseData.openBindId != -1) {
                        openBindId = responseData.openBindId;
                    }
                    if (responseData.data != null && responseData.data.deviceList != null && responseData.data.deviceList.size() != 0) {
                        Iterator<DeviceDetailListData.ResponseData.DeviceListBean> iterator =  responseData.data.deviceList.iterator();
                        while(iterator.hasNext()){
                            DeviceDetailListData.ResponseData.DeviceListBean next = iterator.next();
                            if (next.channels.size() == 0 && !next.catalog.contains("NVR")) {
                                // 使用迭代器中的remove()方法,可以删除元素.
                                iterator.remove();
                            }
                        }
                    }
                    //没有数据
                    if ((responseData.data == null || responseData.data.deviceList == null || responseData.data.deviceList.size() == 0) && datas.size() == 0) {
                        //本次未拉到数据且上次也没有数据
                        rlNoDevice.setVisibility(View.VISIBLE);
                        deviceList.setVisibility(View.GONE);
                    } else {
                        if ((responseData.data == null || responseData.data.deviceList == null || responseData.data.deviceList.size() == 0)) {
                            return;
                        }
                        rlNoDevice.setVisibility(View.GONE);
                        deviceList.setVisibility(View.VISIBLE);
                        datas.addAll(responseData.data.deviceList);

                        deviceListAdapter.notifyDataSetChanged();
                        if (datas.size() >= DeviceListService.pageSize) {
                            refreshMode(Mode.BOTH);
                        } else {
                            refreshMode(Mode.PULL_FROM_START);
                        }
                    }
            }

            @Override
            public void onError(Throwable throwable) {
                if (isDestroyed()) {
                    return;
                }
                refreshState(false);
                LogUtil.errorLog(TAG, "error", throwable);
                Toast.makeText(context, throwable.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
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

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.ll_back) {
            finish();
        } else if (id == R.id.ll_add) {
            try {
                LCDeviceEngine.newInstance().addDevice(this);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
