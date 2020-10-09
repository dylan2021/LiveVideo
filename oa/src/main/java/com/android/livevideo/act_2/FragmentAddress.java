package com.android.livevideo.act_2;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.livevideo.App;
import com.android.livevideo.R;
import com.android.livevideo.act_0.MainActivity;
import com.android.livevideo.act_video.PlayerLiveActivity;
import com.android.livevideo.base.fragment.BaseSearchFragment;
import com.android.livevideo.bean.CameraInfo;
import com.android.livevideo.bean.CamerasListInfo;
import com.android.livevideo.core.net.GsonRequest;
import com.android.livevideo.core.utils.Constant;
import com.android.livevideo.core.utils.KeyConst;
import com.android.livevideo.core.utils.NetUtil;
import com.android.livevideo.core.utils.TextUtil;
import com.android.livevideo.util.ToastUtil;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.reflect.TypeToken;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

        initView();
        getData();
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            getData();
        }
    }

    private void initLayout(List<CamerasListInfo.DataBean> infoList) {
        deptLayout.removeAllViews();
        if (infoList == null || infoList.size() == 0) {
            ToastUtil.show(context, "无数据");
            return;
        }
        for (CamerasListInfo.DataBean bean : infoList) {
            View itemView = View.inflate(context, R.layout.item_address, null);
            TextView nameTv = (TextView) itemView.findViewById(R.id.address_name_tv);
            TextView descTv = (TextView) itemView.findViewById(R.id.address_tv);
            TextView statusTv = (TextView) itemView.findViewById(R.id.address_number_tv);
            if (bean != null) {
                final int id = bean.getCameraId();
                final String onlineStatus = bean.getOnlineStatus();

                final String title = bean.getCameraAddr();
                final String employeeCount = bean.getCameraMsg();
                nameTv.setText(title);
                descTv.setText(employeeCount);
                statusTv.setText("1".equals(onlineStatus) ? "无信号" : "正常");
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //请求单个摄像头播放地址
                        getItemData(id, title);
                    }
                });
                deptLayout.addView(itemView);
            }
        }
    }

    private void getItemData(int id, String title) {
        if (!NetUtil.isNetworkConnected(context)) {
            return;
        }
        String url = Constant.WEB_SITE + "/ai/camera/getLive?cameraSeq=" + id;

        Response.Listener<CameraInfo> successListener = new Response
                .Listener<CameraInfo>() {
            @Override
            public void onResponse(CameraInfo result) {
                CameraInfo.DataBean data = result.getData();
                if (null == context || data == null) {
                    return;
                }
                String liveUrl = data.getLiveUrl();
                Bundle bundle = new Bundle();
                bundle.putSerializable(KeyConst.OBJ_INFO, (Serializable) null);

                Intent intent = new Intent(context, PlayerLiveActivity.class);
                intent.putExtra(KeyConst.id, id);
                intent.putExtra(KeyConst.url, liveUrl);
                intent.putExtras(bundle);
                intent.putExtra(KeyConst.title, title);
                context.startActivity(intent);
            }
        };

        Request<CameraInfo> versionRequest = new
                GsonRequest<CameraInfo>(Request.Method.GET, url,
                        successListener, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                    }
                }, new TypeToken<CameraInfo>() {
                }.getType()) {
                    @Override
                    public Map<String, String> getHeaders() throws AuthFailureError {
                        Map<String, String> params = new HashMap<>();
                        params.put(KeyConst.Authorization, KeyConst.Bearer + App.token);
                        return params;
                    }
                };
        App.requestQueue.add(versionRequest);
    }

    private void getData() {
        if (!NetUtil.isNetworkConnected(context)) {
            return;
        }
        String url = Constant.WEB_SITE + "/ai/camera/list";
        Response.Listener<CamerasListInfo> successListener = new Response
                .Listener<CamerasListInfo>() {
            @Override
            public void onResponse(CamerasListInfo result) {
                Log.d(TAG, "获取失败,code:" + result.getCode());
                Log.d(TAG, "获取失败,msg:" + result.getMsg());
                if (null == context || result == null) {
                    return;
                }
                initLayout(result.getData());
            }
        };

        Request<CamerasListInfo> versionRequest = new
                GsonRequest<CamerasListInfo>(Request.Method.GET, url,
                        successListener, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Log.d(TAG, "数据返回错误" + TextUtil.getErrorMsg(volleyError));
                    }
                }, new TypeToken<CamerasListInfo>() {
                }.getType()) {
                    @Override
                    public Map<String, String> getHeaders() throws AuthFailureError {
                        Map<String, String> params = new HashMap<>();
                        params.put("X-Requested-With", "XMLHttpRequest");
                        return params;
                    }
                };
        App.requestQueue.add(versionRequest);
    }

    private void initView() {
        view.findViewById(R.id.search_bt).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                context.startActivity(new Intent(context, SearchActivity.class));
            }
        });
        topNameTv = (TextView) view.findViewById(R.id.top_name_tv);
        deptLayout = (LinearLayout) view.findViewById(R.id.department_list_layout);
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
