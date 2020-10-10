package com.android.livevideo.act_0;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.android.livevideo.App;
import com.android.livevideo.R;
import com.android.livevideo.act_other.LoginActivity;
import com.android.livevideo.base.fragment.BaseSearchFragment;
import com.android.livevideo.bean.MsgInfo;
import com.android.livevideo.core.net.GsonRequest;
import com.android.livevideo.core.utils.Constant;
import com.android.livevideo.core.utils.KeyConst;
import com.android.livevideo.core.utils.NetUtil;
import com.android.livevideo.dialogfragment.SimpleDialogFragment;
import com.android.livevideo.util.ToastUtil;
import com.android.livevideo.util.Utils;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.reflect.TypeToken;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.content.Context.MODE_PRIVATE;

/**
 * Gool Lee
 */
@SuppressLint({"WrongConstant", "ValidFragment"})
public class FragmentMsg extends BaseSearchFragment {
    private int id;
    private String[] tabArr = {"消息", "公告"};
    private MainActivity context;
    private RefreshLayout mRefreshLayout;
    private MsgAdapter msgAdapter;
    private int TYPE = 1;
    private ListView lv;
    private List<MsgInfo> noticeList, msgList;
    private SharedPreferences.Editor sp;
    private TextView emptyTv;
    private RadioGroup msgTabRg;
    private SimpleDialogFragment reLoginDialog;

    public FragmentMsg() {
    }

    public FragmentMsg(int chooseId) {
        id = chooseId;
    }

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.fragment_msg;
    }

    @Override
    protected void initViewsAndEvents(View view) {
        context = (MainActivity) getActivity();
        mRefreshLayout = (RefreshLayout) view.findViewById(R.id.refreshLayout);
        Utils.setLoadHeaderFooter(context, mRefreshLayout);


        lv = (ListView) view.findViewById(R.id.listView);
        msgAdapter = new MsgAdapter(context);
        lv.setAdapter(msgAdapter);

        emptyTv = view.findViewById(R.id.empty_tv);

        msgTabRg = (RadioGroup) view.findViewById(R.id.msg_tab_rg);
        msgTabRg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                TYPE = radioGroup.indexOfChild(radioGroup.findViewById(i));
                msgAdapter.setDate(TYPE == 0 ? msgList : noticeList, TYPE);
                getMsgData(TYPE);
            }
        });

        mRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                getMsgData(TYPE);
            }
        });
        mRefreshLayout.autoRefresh();

    }

    @Override
    public void onStart() {
        super.onStart();
        getMsgData(TYPE);
    }

    //获取消息数据
    private void getMsgData(final int tabType) {
        if (true) {
            mRefreshLayout.finishRefresh(0);
            return;
        }
        if (!NetUtil.isNetworkConnected(context)) {
            mRefreshLayout.finishRefresh(0);
            msgAdapter.setDate(null, TYPE);
            return;
        }
       /* String url = Constant.WEB_SITE + "/biz/process/" + (tabType == 0 ?
                "message" : "notice/all");*/
        String url = Constant.WEB_SITE + "/biz/process/notice/all";
        Response.Listener<List<MsgInfo>> successListener = new Response
                .Listener<List<MsgInfo>>() {
            @Override
            public void onResponse(List<MsgInfo> result) {
                if (null != context && !context.isFinishing()) {
                    mRefreshLayout.finishRefresh(0);
                }
                if (result == null || result.size() == 0) {
                    emptyTv.setText(context.getString(R.string.no_data));
                    emptyTv.setVisibility(View.VISIBLE);
                    msgAdapter.setDate(null, tabType);
                    return;
                }
                emptyTv.setVisibility(View.GONE);
                if (tabType == 0) {
                    msgList = result;
                } else {
                    noticeList = result;
                }
                if (tabType == TYPE) {
                    msgAdapter.setDate(result, tabType);
                }
            }
        };

        Request<List<MsgInfo>> versionRequest = new
                GsonRequest<List<MsgInfo>>(
                        Request.Method.GET, url,
                        successListener, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        mRefreshLayout.finishRefresh(0);
                        emptyTv.setVisibility(View.GONE);
                        if (volleyError != null && volleyError.networkResponse != null
                                && volleyError.networkResponse.data != null) {
                            byte[] data = volleyError.networkResponse.data;
                            String errMsg = new String(data);
                            try {
                                JSONObject errObj = new JSONObject(errMsg);

                                int errCode = errObj.getInt(KeyConst.error);
                                if (10002 == errCode) {
                                    showDialog(context.getString(R.string.token_invalid));
                                    return;
                                }
                            } catch (JSONException e) {
                            }
                        }
                        ToastUtil.show(context, R.string.server_exception);
                    }
                }, new TypeToken<List<MsgInfo>>() {
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

    private void showDialog(String msg) {
        if (reLoginDialog != null) {
            return;
        }
        reLoginDialog = new SimpleDialogFragment();
        reLoginDialog.setDialogWidth(220);
        reLoginDialog.setCancelable(false);

        TextView tv = new TextView(context);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams
                .MATCH_PARENT, ViewGroup
                .LayoutParams.MATCH_PARENT);
        params.setMargins(0, 0, 0, 0);
        params.gravity = Gravity.CENTER;
        tv.setLayoutParams(params);
        tv.setGravity(Gravity.CENTER);
        tv.setText(msg);
        tv.setTextColor(getResources().getColor(R.color.color666));
        reLoginDialog.setContentView(tv);

        reLoginDialog.setNegativeButton(R.string.reLogin, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reLoginDialog.dismiss();
                sp = context.getSharedPreferences(Constant.CONFIG_FILE_NAME, MODE_PRIVATE).edit();
                sp.putString(Constant.sp_pwd, "").commit();

                context.finish();
                Intent intent = new Intent(context, LoginActivity.class);
                startActivity(intent);

            }
        });
        reLoginDialog.show(getSupportFragmentManager().beginTransaction(), "successDialog");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
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
