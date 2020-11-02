package com.android.livevideo.act_2;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.livevideo.R;
import com.android.livevideo.act_0.MainActivity;
import com.android.livevideo.base.fragment.BaseSearchFragment;
import com.android.livevideo.util.ToastUtil;
import com.android.livevideo.util.Utils;
import com.common.openapi.ClassInstanceManager;
import com.common.openapi.DeviceListService;
import com.common.openapi.IGetDeviceInfoCallBack;
import com.common.openapi.MethodConst;
import com.common.openapi.entity.DeviceDetailListData;
import com.common.openapi.entity.DeviceListData;
import com.lechange.demo.adapter.DeviceListAdapter;
import com.lechange.demo.ui.DeviceDetailActivity;
import com.lechange.demo.ui.DeviceOnlineMediaPlayActivity;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Gool Lee
 */
@SuppressLint({"WrongConstant", "ValidFragment"})
public class FragmentAddress extends BaseSearchFragment {
    private int id;
    private MainActivity context;
    private View view;
    private LinearLayout deptLayout;
    private TextView topNameTv;
    private RefreshLayout mRefreshLayout;
    private RecyclerView mRecyclerView;
    private DeviceListAdapter deviceListAdapter;
    private List<DeviceDetailListData.ResponseData.DeviceListBean> datas = new ArrayList<>();

    public FragmentAddress() {
    }

    public FragmentAddress(int chooseId) {
        id = chooseId;
    }

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.fragment_address;
    }

    @Override
    protected void initViewsAndEvents(View v) {
        context = (MainActivity) getActivity();
        view = v;
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerView_address);
        initView();
        initRecyclerView();
        getDeviceList(false);
    }

    private void initRecyclerView() {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
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
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            getDeviceList(true);
        }
    }

    private void getDeviceList(boolean isLoadMore) {
        datas.clear();
        DeviceListService deviceVideoService = ClassInstanceManager.newInstance().
                getDeviceListService();
        DeviceListData deviceListData = new DeviceListData();

        deviceVideoService.deviceBaseList(deviceListData, new IGetDeviceInfoCallBack.IDeviceListCallBack() {
            @Override
            public void deviceList(DeviceDetailListData.Response responseData) {
                mRefreshLayout.finishRefresh();
                mRefreshLayout.finishLoadmore();
                if (context == null) {
                    return;
                }
                if ((responseData.data == null || responseData.data.deviceList == null || responseData.data.deviceList.size() == 0)) {
                    ToastUtil.show(context, R.string.no_data);
                    return;
                }

                Iterator<DeviceDetailListData.ResponseData.DeviceListBean> iterator = responseData.data.deviceList.iterator();
                while (iterator.hasNext()) {
                    DeviceDetailListData.ResponseData.DeviceListBean next = iterator.next();
                    if (next.channels.size() == 0 && !next.catalog.contains("NVR")) {
                        // 使用迭代器中的remove()方法,可以删除元素.
                        iterator.remove();
                    }
                }

                datas.addAll(responseData.data.deviceList);
                deviceListAdapter.notifyDataSetChanged();
            }

            @Override
            public void onError(Throwable throwable) {
                mRefreshLayout.finishRefresh();
                mRefreshLayout.finishLoadmore();
              //获取数据异常
            }
        });
    }

    private void initView() {
        mRefreshLayout = (RefreshLayout) view.findViewById(R.id.refreshLayout);
        Utils.setLoadHeaderFooter(context, mRefreshLayout);
        mRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                getDeviceList(false);
            }
        });
    }

    @Override
    protected void onFirstUserVisible() {

    }

    @Override
    protected void onUserVisible() {

    }

    @Override
    protected void onUserInvisible() {

    }

    @Override
    protected View getLoadView(View view) {
        return null;
    }


}
