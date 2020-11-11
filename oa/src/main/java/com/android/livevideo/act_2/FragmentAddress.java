package com.android.livevideo.act_2;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.livevideo.App;
import com.android.livevideo.R;
import com.android.livevideo.act_0.MainActivity;
import com.android.livevideo.act_video.PlayerActivity;
import com.android.livevideo.base.fragment.BaseSearchFragment;
import com.android.livevideo.core.net.GsonRequest;
import com.android.livevideo.core.utils.Constant;
import com.android.livevideo.core.utils.KeyConst;
import com.android.livevideo.core.utils.NetUtil;
import com.android.livevideo.core.utils.SPUtils;
import com.android.livevideo.util.ToastUtil;
import com.android.livevideo.util.Utils;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.common.openapi.MethodConst;
import com.common.openapi.entity.DeviceDetailListData;
import com.google.gson.reflect.TypeToken;
import com.lechange.demo.adapter.MainVideoListAdapter;
import com.lechange.demo.ui.DeviceOnlineMediaPlayActivity;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.io.Serializable;
import java.util.ArrayList;
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
    private MainVideoListAdapter deviceListAdapter;
    private List<DeviceDetailListData.ResponseData.DeviceListBean> datas = new ArrayList<>();
    private List<DeviceDetailListData.ResponseData.DeviceListBean.ChannelsBean> channels;
    private DeviceDetailListData.ResponseData.DeviceListBean listBean;
    private DeviceDetailListData.ResponseData.DeviceListBean.ChannelsBean channelsInfo;

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
        getDeviceList();
    }

    private void initRecyclerView() {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
        deviceListAdapter = new MainVideoListAdapter(context, channels);
        mRecyclerView.setAdapter(deviceListAdapter);
        deviceListAdapter.setOnItemClickListener(new MainVideoListAdapter.OnChannelClickListener() {
            @Override
            public void onChannelClick(int position, boolean isSimple) {
                channelsInfo = channels.get(position);
                Intent intent = new Intent();
                Bundle bundle = new Bundle();
                if (SPUtils.isSimpleType(context)) {
                    intent.setClass(context, DeviceOnlineMediaPlayActivity.class);
                    listBean.checkedChannel = position;
                    bundle.putSerializable(MethodConst.ParamConst.deviceDetail, listBean);
                } else {
                    intent.setClass(context, PlayerActivity.class);
                    bundle.putSerializable(KeyConst.OBJ_INFO, (Serializable) null);
                    intent.putExtra(KeyConst.id, channelsInfo.channelId);
                    intent.putExtra(KeyConst.url, channelsInfo.cameraLive);
                    intent.putExtra(KeyConst.title, channelsInfo.channelName);
                }

                intent.putExtras(bundle);
                context.startActivity(intent);
            }
        });
       /* deviceListAdapter.setOnItemClickListener(new DeviceListAdapter.OnItemClickListener() {
            @Override
            public void onSettingClick(int position) {
         *//*       if (datas.size() == 0) {
                    return;
                }
                Bundle bundle = new Bundle();
                bundle.putSerializable(MethodConst.ParamConst.deviceDetail, datas.get(position));
                bundle.putString(MethodConst.ParamConst.fromList, MethodConst.ParamConst.fromList);
                Intent intent = new Intent(context, DeviceDetailActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);*//*
            }

            @Override
            public void onDetailClick(int position) {
            *//*    Log.d("视频数据", "详情点击" + position);
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
                startActivity(intent);*//*
            }

            @Override
            public void onChannelClick(int outPosition, int innerPosition) {
                if (datas.size() == 0) {
                    return;
                }
                DeviceDetailListData.ResponseData.DeviceListBean info = datas.get(outPosition);
                if (!info.channels.get(innerPosition).status.equals("online")) {
                    ToastUtil.show(context,"设备当前处于离线状态,无法观看");
                    return;
                }

                //todo 直接去掉最外层recycleView,用最里层的就行
                Bundle bundle = new Bundle();
                info.checkedChannel = innerPosition;
                bundle.putSerializable(MethodConst.ParamConst.deviceDetail, info);
                Intent intent = new Intent(context, DeviceOnlineMediaPlayActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });*/
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            getDeviceList();
        }
    }

    private void getDeviceList() {
        if (!NetUtil.isNetworkConnected(context)) {
            return;
        }
        datas.clear();
        String url = Constant.WEB_SITE + "/ai/camera/pluginList";
        Response.Listener<DeviceDetailListData.ResponseData> successListener = new Response
                .Listener<DeviceDetailListData.ResponseData>() {
            @Override
            public void onResponse(DeviceDetailListData.ResponseData result) {
                mRefreshLayout.finishRefresh();
                if (null == context || result == null) {
                    ToastUtil.show(context, R.string.no_data);
                    return;
                }

                //datas = result.deviceList;
                List<DeviceDetailListData.ResponseData.DeviceListBean> deviceList = result.deviceList;
                if (deviceList==null) {
                    ToastUtil.show(context, "暂无数据,稍后重试");
                    return;
                }
                listBean = deviceList.get(0);
                channels = listBean.channels;
                deviceListAdapter.setData(channels);
            }
        };

        Request<DeviceDetailListData.ResponseData> versionRequest = new
                GsonRequest<DeviceDetailListData.ResponseData>(Request.Method.GET, url,
                        successListener, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        mRefreshLayout.finishRefresh();
                    }
                }, new TypeToken<DeviceDetailListData.ResponseData>() {
                }.getType()) {

                };
        App.requestQueue.add(versionRequest);
       /* DeviceListService deviceVideoService = ClassInstanceManager.newInstance().
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
        });*/
    }

    private void initView() {
        mRefreshLayout = (RefreshLayout) view.findViewById(R.id.refreshLayout);
        Utils.setLoadHeaderFooter(context, mRefreshLayout);
        mRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                getDeviceList();
            }
        });
        mRefreshLayout.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
                refreshlayout.finishLoadmore();
                ToastUtil.show(context, R.string.no_more_data);
            }
        });
    }

    @Override
    protected void onFirstUserVisible() {
        Log.d("界面显示", "显示0");
    }

    @Override
    protected void onUserVisible() {
        //getDeviceList();
    }

    @Override
    protected void onUserInvisible() {

    }

    @Override
    protected View getLoadView(View view) {
        return null;
    }


}
