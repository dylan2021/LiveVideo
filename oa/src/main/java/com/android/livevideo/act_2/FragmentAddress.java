package com.android.livevideo.act_2;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.livevideo.App;
import com.android.livevideo.R;
import com.android.livevideo.act_0.MainActivity;
import com.android.livevideo.act_video.PlayerLiveActivity;
import com.android.livevideo.base.fragment.BaseSearchFragment;
import com.android.livevideo.bean.DeptInfo;
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
import java.util.ArrayList;
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
    private List<DeptInfo> deptInfos = new ArrayList<>();
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
        initLayout(deptInfos);
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            getData();
        }
    }

    private void initLayout(List<DeptInfo> infoList) {
        deptLayout.removeAllViews();
        if (infoList == null || infoList.size() == 0) {
            ToastUtil.show(context, "无数据");
            return;
        }
        DeptInfo dataInfo = deptInfos.get(0);
        App.companyName = TextUtil.remove_N(dataInfo.getTitle());
        context.setCompanyName();
        int total = dataInfo.getTotal();
        topNameTv.setText(App.companyName + "(" + total + "人)");
        List<DeptInfo.ChildrenBeanX> deptInfoChildren = dataInfo.getChildren();
        if (deptInfoChildren == null || deptInfoChildren.size() == 0) {
            return;
        }
        for (DeptInfo.ChildrenBeanX deptInfo : deptInfoChildren) {
            View itemView = View.inflate(context, R.layout.item_address, null);
            TextView nameTv = (TextView) itemView.findViewById(R.id.address_name_tv);
            TextView addressTv = (TextView) itemView.findViewById(R.id.address_tv);
            TextView statusTv = (TextView) itemView.findViewById(R.id.address_number_tv);

            if (deptInfo != null) {
                final int id = deptInfo.getId();
                final String title = deptInfo.getTitle();
                final int employeeCount = deptInfo.getTotal();
                final List<DeptInfo.ChildrenBeanX.ChildrenBean> childrenList = deptInfo.getChildren();
                nameTv.setText("摄像头" + id);
                addressTv.setText("武汉市江夏区光谷大道" + id + "号");
                statusTv.setText(id < 10 ? "无信号" : "正常");

                 //String url = "rtmp://58.200.131.2:1935/livetv/hunantv";
                 String url = "http://ivi.bupt.edu.cn/hls/cctv1hd.m3u8";
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Bundle bundle = new Bundle();
                        bundle.putSerializable(KeyConst.OBJ_INFO, (Serializable) childrenList);

                        Intent intent = new Intent(context, PlayerLiveActivity.class);
                        intent.putExtra(KeyConst.id, id);
                        intent.putExtras(bundle);
                        intent.putExtra(KeyConst.url, url);
                        intent.putExtra(KeyConst.title, nameTv.getText()+"");
                        context.startActivity(intent);
                    }
                });
                //产值
                deptLayout.addView(itemView);
            }
        }
    }

    private void getData() {
        DeptInfo info = new DeptInfo();
        deptInfos.add(info);

        if (!NetUtil.isNetworkConnected(context)) {
            return;
        }
        String url = Constant.WEB_SITE + "/upms/departments/tree";

        Response.Listener<List<DeptInfo>> successListener = new Response
                .Listener<List<DeptInfo>>() {
            @Override
            public void onResponse(List<DeptInfo> result) {
                if (null == context) {
                    return;
                }
                deptInfos = result;
                initLayout(deptInfos);
            }
        };

        Request<List<DeptInfo>> versionRequest = new
                GsonRequest<List<DeptInfo>>(Request.Method.GET, url,
                        successListener, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                    }
                }, new TypeToken<List<DeptInfo>>() {
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
