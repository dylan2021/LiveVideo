package com.android.livevideo.act_0;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.android.livevideo.act_1.ChooseEmplyeeActivity;
import com.android.livevideo.act_1.WageEmplyeeListActivity;
import com.android.livevideo.act_1.WageMonthProjectListActivity;
import com.android.livevideo.act_other.CommonBaseActivity;
import com.android.livevideo.act_other.PictBean;
import com.android.livevideo.bean.DictInfo;
import com.android.livevideo.bean.EmployeeInfo;
import com.android.livevideo.bean.FileInfo;
import com.android.livevideo.bean.GroupInfo;
import com.android.livevideo.bean.MsgInfo;
import com.android.livevideo.bean.PoolInfo;
import com.android.livevideo.bean.ProductInfo;
import com.android.livevideo.bean.PurchaseInfo;
import com.android.livevideo.bean.ReimburseInfo;
import com.android.livevideo.bean.TripItemInfo;
import com.android.livevideo.util.DialogUtils;
import com.android.livevideo.util.Utils;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.livevideo.App;
import com.android.livevideo.R;
import com.android.livevideo.adapter.FileListAdapter;
import com.android.livevideo.bean.FileListInfo;
import com.android.livevideo.bean.HistoryInfo;
import com.android.livevideo.core.net.GsonRequest;
import com.android.livevideo.core.utils.Constant;
import com.android.livevideo.core.utils.DialogHelper;
import com.android.livevideo.core.utils.ImageUtil;
import com.android.livevideo.core.utils.KeyConst;
import com.android.livevideo.core.utils.NetUtil;
import com.android.livevideo.core.utils.RetrofitUtil;
import com.android.livevideo.core.utils.TextUtil;
import com.android.livevideo.util.FileTypeUtil;
import com.android.livevideo.util.ToastUtil;
import com.android.livevideo.view.ScrollListView;
import com.android.livevideo.widget.mulpicture.MulPictureActivity;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Gool Lee
 * @Date 申请详情
 */
public class MsgDetailActivity extends CommonBaseActivity {

    private int processId = 1;
    private String createName, WAGE_YEAR, WAGE_MONTH;
    public MsgDetailActivity context;
    private LinearLayout planItemLayout;
    private ScrollListView listView;
    private FileListAdapter fileListAdapter;
    private List<FileListInfo> fileData = new ArrayList<>();
    private LinearLayout copyerItemsLayout, itemsLayout1, itemsLayout2;
    private RelativeLayout auditLayout;
    private Button auditBt1, auditBt2, auditBt3;
    private MsgInfo info;
    private ImageView statusTagIv;
    private String TYPE;
    private JsonObject infoObj = new JsonObject();
    private View itemView;
    private View itemView2;
    private int agreeReject_recall;
    private List<EmployeeInfo> parentList = new ArrayList<>();
    private ImageView informPeopleAddBt;
    private boolean IS_NEW_TASK = false;
    private int typPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initStatusBar();
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        setContentView(R.layout.activity_msg_detail);
        Intent intent = getIntent();
        processId = intent.getIntExtra(KeyConst.id, 0);
        typPosition = getIntent().getIntExtra(KeyConst.type, 0);
        agreeReject_recall = intent.getIntExtra(KeyConst.agreeReject_recall, 0);
        IS_NEW_TASK = intent.getBooleanExtra(KeyConst.is_new_task, false);

        initTitleBackBt(intent.getStringExtra(KeyConst.title));
        context = this;

        initView();

        getAuditLogData();//审批日志

        getDictUnitList("unit");

        getData();
    }

    //删除流程(驳回状态可以删除)
    private void deletePost() {
        FragmentManager fm = getSupportFragmentManager();
        getTitleRightBt(getString(R.string.delete)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogUtils.getTwoBtDialog(context, "流程删除后不可恢复,确定删除吗?")
                        .onPositive(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                String url = Constant.WEB_SITE + "/biz/process/produce/" + processId;
                                int postType = Request.Method.DELETE;

                                DialogHelper.showWaiting(fm, "加载中...");
                                JsonObjectRequest jsonRequest = new JsonObjectRequest(postType, url,
                                        new JSONObject(), new Response.Listener<JSONObject>() {
                                    @Override
                                    public void onResponse(JSONObject result) {
                                        DialogHelper.hideWaiting(fm);
                                        if (result != null) {
                                            ToastUtil.show(context, "删除成功");
                                            context.finish();
                                        } else {
                                            ToastUtil.show(context, "删除失败");
                                        }
                                    }
                                }, new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        DialogHelper.hideWaiting(fm);
                                        String errorMsg = TextUtil.getErrorMsg(error);
                                        Log.d(TAG, "删除失败" + errorMsg);
                                        if (!TextUtil.isEmpty(errorMsg)) {
                                            try {
                                                JSONObject obj = new JSONObject(errorMsg);
                                                if (obj != null && obj.getInt(KeyConst.error) > 0) {
                                                    DialogUtils.showTipDialog(context, obj.getString(KeyConst.message));
                                                    return;
                                                }
                                            } catch (JSONException e) {
                                            }
                                        }
                                        ToastUtil.show(context, getString(R.string.server_exception));
                                    }
                                }) {
                                    @Override
                                    public Map<String, String> getHeaders() {
                                        Map<String, String> params = new HashMap<>();
                                        params.put(KeyConst.Content_Type, Constant.application_json);
                                        params.put(KeyConst.Authorization, KeyConst.Bearer + App.token);
                                        return params;
                                    }
                                };
                                App.requestQueue.add(jsonRequest);
                            }
                        }).show();


            }
        });
    }

    private void addTopItems(String title, String value) {
        itemView = View.inflate(context, R.layout.item_key_value, null);
        TextView keyTv = (TextView) itemView.findViewById(R.id.item_key_tv);
        TextView valueTv = (TextView) itemView.findViewById(R.id.item_value_tv);//工期

        if (title.equals("出差天数")) {
            value = value.replace(".0", "");
        }
        if (title.equals("关联原料检验单号")) {
            valueTv.setTextColor(ContextCompat.getColor(context, R.color.mainColor));
            String reformId = Utils.getObjStr(infoObj, KeyConst.processId);
            if (!TextUtil.isEmpty(reformId)) {
                final Intent intent = new Intent(context, MsgDetailActivity.class);
                intent.putExtra(KeyConst.id, Integer.valueOf(reformId));
                intent.putExtra(KeyConst.title, "");
                intent.putExtra(KeyConst.is_new_task, true);
                intent.putExtra(KeyConst.agreeReject_recall, 0);
                valueTv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (IS_NEW_TASK) {
                            finish();
                        } else {
                            context.startActivity(intent);
                        }
                    }
                });
            } else {
                valueTv.setTextColor(ContextCompat.getColor(context, R.color.color212121));
                value = "无";
            }

        }
        if ("关联报销".equals(title) || "关联出差".equals(title)) {
            valueTv.setTextColor(ContextCompat.getColor(context, R.color.mainColor));

            if ("关联报销".equals(title)) {
                //出差 --> 关联多个报销
                JsonElement objArr = infoObj.get(KeyConst.bizProcessList);
                if (objArr != null && !objArr.isJsonNull()) {
                    JsonArray processArr = infoObj.getAsJsonArray(KeyConst.bizProcessList);
                    if (processArr != null && processArr.size() != 0 && !processArr.isJsonNull()) {
                        for (int i = 0; i < processArr.size(); i++) {
                            itemView = View.inflate(context, R.layout.item_key_value, null);
                            keyTv = (TextView) itemView.findViewById(R.id.item_key_tv);
                            valueTv = (TextView) itemView.findViewById(R.id.item_value_tv);


                            JsonObject processObj = processArr.get(i).getAsJsonObject();
                            String reformId = Utils.getObjStr(processObj, KeyConst.id);
                            String headline = Utils.getObjStr(processObj, KeyConst.headline);
                            String procNum = Utils.getObjStr(processObj, KeyConst.procNum);

                            final Intent intent = new Intent(context, MsgDetailActivity.class);
                            intent.putExtra(KeyConst.id, Integer.valueOf(reformId));
                            intent.putExtra(KeyConst.title, headline);
                            intent.putExtra(KeyConst.is_new_task, true);
                            intent.putExtra(KeyConst.agreeReject_recall, 0);

                            if (!IS_NEW_TASK) {
                                valueTv.setTextColor(ContextCompat.getColor(context, R.color.mainColor));

                                valueTv.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        context.startActivity(intent);
                                    }
                                });
                            }
                            if (i == 0) {
                                keyTv.setText(title);
                            }
                            valueTv.setText(procNum);
                            //产值
                            itemsLayout1.addView(itemView);
                        }
                        return;
                    }
                }
                valueTv.setTextColor(ContextCompat.getColor(context, R.color.color212121));
                value = "无";
            } else {
                String reformId = Utils.getObjStr(infoObj, KeyConst.businessTripId);
                String headline = Utils.getObjStr(infoObj, KeyConst.businessTripHeadline);
                if (!TextUtil.isEmpty(reformId)) {
                    final Intent intent = new Intent(context, MsgDetailActivity.class);
                    intent.putExtra(KeyConst.id, Integer.valueOf(reformId));
                    intent.putExtra(KeyConst.title, headline);
                    intent.putExtra(KeyConst.is_new_task, true);
                    intent.putExtra(KeyConst.agreeReject_recall, 0);
                    valueTv.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (IS_NEW_TASK) {
                                finish();
                            } else {
                                context.startActivity(intent);
                            }
                        }
                    });
                } else {
                    valueTv.setTextColor(ContextCompat.getColor(context, R.color.color212121));
                    value = "无";
                }
            }

        }
        keyTv.setText(title);
        valueTv.setText(value);
        //产值
        itemsLayout1.addView(itemView);
    }

    private void addTopRedItems(String title, String value) {
        itemView = View.inflate(context, R.layout.item_key_value, null);
        TextView keyTv = (TextView) itemView.findViewById(R.id.item_key_tv);
        TextView valueTv = (TextView) itemView.findViewById(R.id.item_value_tv);//工期
        keyTv.setTextColor(ContextCompat.getColor(context, R.color.red));
        valueTv.setTextColor(ContextCompat.getColor(context, R.color.red));
        keyTv.setText(title);
        valueTv.setText(value);
        //产值
        itemsLayout1.addView(itemView);
    }

    //月度工资 -明细布局
    private void addWageMonthItems() {
        itemView = View.inflate(context, R.layout.layout_wage_month_two_item, null);
        itemsLayout2.addView(itemView);
    }

    //月度工资-人员明细
    public void onEmplyeeMonthDetailClick(View view) {
        Intent intent = new Intent(context, WageEmplyeeListActivity.class);
        intent.putExtra(KeyConst.id, processId + "");
        intent.putExtra(KeyConst.type, 2);
        context.startActivity(intent);
    }

    //月度工资-项目明细
    public void onProjsMonthDetailClick(View view) {
        Intent intent = new Intent(context, WageMonthProjectListActivity.class);
        intent.putExtra(KeyConst.wageMonth, WAGE_YEAR + "-" + WAGE_MONTH);
        context.startActivity(intent);
    }

    //出差
    private void setTripsItemsLayout() {
        JsonElement objArr = infoObj.get(KeyConst.tripList);
        if (objArr == null || objArr.isJsonNull()) {
            return;
        }
        JsonArray tripArr = infoObj.getAsJsonArray(KeyConst.tripList);

        if (tripArr == null || tripArr.size() == 0 || tripArr.isJsonNull()) {
            return;
        }
        List<TripItemInfo> infoList = new Gson().fromJson(tripArr, new TypeToken<List<TripItemInfo>>() {
        }.getType());

        for (int i = 0; i < infoList.size(); i++) {
            TripItemInfo itemInfo = infoList.get(i);
            if (itemInfo == null) {
                return;
            }
            itemView2 = View.inflate(context, R.layout.item_go_out_item, null);
            TextView titleTv = (TextView) itemView2.findViewById(R.id.go_out_title_tv);
            titleTv.setText("行程" + (i + 1));

            addItemLayout2("交通工具", itemInfo.getVehicle(), R.id.keyTv0, R.id.valueTv0);
            addItemLayout2("单程往返", itemInfo.getType(), R.id.keyTv1, R.id.valueTv1);
            addItemLayout2("往返城市", itemInfo.getDeparture() + "-"
                    + itemInfo.getDestination(), R.id.keyTv2, R.id.valueTv2);
            addItemLayout2("开始时间", TextUtil.subTimeYmdHm(itemInfo.getStartTime()),
                    R.id.keyTv3, R.id.valueTv3);
            addItemLayout2("结束时间", TextUtil.subTimeYmdHm(itemInfo.getEndTime()),
                    R.id.keyTv4, R.id.valueTv4);
            addItemLayout2("时长(天)", TextUtil.remove_0(itemInfo.getPeriod() + ""),
                    R.id.keyTv5, R.id.valueTv5);
            //产值
            itemsLayout2.addView(itemView2);

        }
    }

    //报销
    private void setReimbursesItemsLayout() {
        JsonElement objArr = infoObj.get(KeyConst.reimburseDetailList);
        if (objArr == null || objArr.isJsonNull()) {
            return;
        }
        JsonArray tripArr = infoObj.getAsJsonArray(KeyConst.reimburseDetailList);
        if (tripArr == null || tripArr.size() == 0 || tripArr.isJsonNull()) {
            return;
        }
        List<ReimburseInfo> infoList = new Gson().fromJson(tripArr, new TypeToken<List<ReimburseInfo>>() {
        }.getType());

        for (int i = 0; i < infoList.size(); i++) {
            ReimburseInfo itemInfo = infoList.get(i);
            if (itemInfo == null) {
                return;
            }
            itemView2 = View.inflate(context, R.layout.item_go_out_item, null);
            TextView titleTv = (TextView) itemView2.findViewById(R.id.go_out_title_tv);
            titleTv.setText("报销明细" + (i + 1));

            addItemLayout2("报销金额(元)", itemInfo.getAmount(), R.id.keyTv0, R.id.valueTv0);
            addItemLayout2("报销类别", itemInfo.getType(), R.id.keyTv1, R.id.valueTv1);
            addItemLayout2("费用明细", itemInfo.getRemark(), R.id.keyTv2, R.id.valueTv2);
            itemView2.findViewById(R.id.detail_layout2_item3).setVisibility(View.GONE);
            itemView2.findViewById(R.id.detail_layout2_item4).setVisibility(View.GONE);
            itemView2.findViewById(R.id.detail_layout2_item5).setVisibility(View.GONE);
            //产值
            itemsLayout2.addView(itemView2);
        }

    }

    //请购单
    private void setPurchaseItemsLayout() {
        JsonElement obj = infoObj.get(KeyConst.purchaseDetail);
        if (obj == null || obj.isJsonNull()) {
            return;
        }
        JsonArray objArr = infoObj.getAsJsonArray(KeyConst.purchaseDetail);
        if (objArr == null || objArr.size() == 0 || objArr.isJsonNull()) {
            return;
        }
        List<PurchaseInfo> infoList = new Gson().fromJson(
                objArr, new TypeToken<List<PurchaseInfo>>() {
                }.getType());

        for (int i = 0; i < infoList.size(); i++) {
            PurchaseInfo itemInfo = infoList.get(i);
            if (itemInfo == null) {
                return;
            }
            itemView2 = View.inflate(context, R.layout.item_go_out_item, null);
            TextView titleTv = (TextView) itemView2.findViewById(R.id.go_out_title_tv);
            titleTv.setText("采购明细" + (i + 1));

            addItemLayout2("料号", itemInfo.getProductDetailNo(), R.id.keyTv0, R.id.valueTv0);

            addItemLayout2("名称", itemInfo.getName(), R.id.keyTv1, R.id.valueTv1);
            addItemLayout2("品牌", itemInfo.getBrand(), R.id.keyTv2, R.id.valueTv2);
            addItemLayout2("厂商", itemInfo.getManufacturer(), R.id.keyTv3, R.id.valueTv3);

            addItemLayout2("规格", itemInfo.getSpec(), R.id.keyTv4, R.id.valueTv4);

            String unit = itemInfo.getUnit();
            String num = new BigDecimal(itemInfo.getNumber()).toPlainString();
            Log.d(TAG, "数量" + num);
            for (DictInfo.DictValuesBean dictValue : unitDictList) {
                String value = dictValue.getValue();
                if (value.equals(unit)) {
                    addItemLayout2("数量", num + dictValue.getName(), R.id.keyTv5, R.id.valueTv5);
                    break;
                }
            }

            itemView2.findViewById(R.id.detail_layout2_item6).setVisibility(View.VISIBLE);
            itemView2.findViewById(R.id.detail_layout2_item7).setVisibility(View.VISIBLE);

            addItemLayout2("金额合计", itemInfo.getPrice() + "元", R.id.keyTv6, R.id.valueTv6);
            //产值
            itemsLayout2.addView(itemView2);
        }

    }

    private List<DictInfo.DictValuesBean> unitDictList = new ArrayList<>();

    private void getDictUnitList(String dictType) {
        String url = Constant.WEB_SITE + "/dict/dicts/cached/" + dictType;
        Response.Listener<List<DictInfo.DictValuesBean>> successListener = new Response
                .Listener<List<DictInfo.DictValuesBean>>() {
            @Override
            public void onResponse(List<DictInfo.DictValuesBean> result) {
                if (result != null) {
                    unitDictList = result;
                }
            }
        };

        Request<List<DictInfo.DictValuesBean>> versionRequest = new
                GsonRequest<List<DictInfo.DictValuesBean>>(Request.Method.GET, url,
                        successListener, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Log.d("字典异常", "");
                    }
                }, new TypeToken<List<DictInfo.DictValuesBean>>() {
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

    private void addItemLayout2(String key, String value, int keyViewId, int valueViewId) {
        ((TextView) itemView2.findViewById(keyViewId)).setText(key);
        ((TextView) itemView2.findViewById(valueViewId)).setText(value);
    }

    //抄送人列表
    private void initCopyerLayout(String employeeName) {
        if (TextUtil.isEmpty(employeeName) || copyerItemsLayout == null) {
            return;
        }
        copyerItemsLayout.removeAllViews();
        String[] nameArr = new String[1];
        if (!employeeName.contains(",")) {
            nameArr[0] = employeeName;
        } else {
            nameArr = employeeName.split(",");
        }
        for (String name : nameArr) {
            View itemView = View.inflate(context, R.layout.item_tv_iv, null);
            TextView copyerIv = (TextView) itemView.findViewById(R.id.ciycle_bg_iv);
            TextView copyerNameTv = (TextView) itemView.findViewById(R.id.ciycle_tv);
            copyerNameTv.setText(name);
            copyerIv.setText(TextUtil.getLast2(name));
            //产值
            copyerItemsLayout.addView(itemView);
        }
    }

    private List<String> payTypeArr = new ArrayList();
    private String payType;

    private void initView() {
        initData();
        itemsLayout1 = (LinearLayout) findViewById(R.id.items_layout_1);
        itemsLayout2 = (LinearLayout) findViewById(R.id.items_layout_2);
        statusTagIv = (ImageView) findViewById(R.id.status_tag_iv);
        informPeopleAddBt = (ImageView) findViewById(R.id.inform_people_add_bt);
        auditLayout = findViewById(R.id.audit_layout);
        auditBt1 = (Button) findViewById(R.id.audit_bt_1);
        auditBt2 = (Button) findViewById(R.id.audit_bt_2);
        auditBt3 = (Button) findViewById(R.id.audit_bt_3);

        if (agreeReject_recall == 1) {//同意,驳回
            auditLayout.setVisibility(View.VISIBLE);
            auditBt3.setVisibility(View.GONE);
            final Map<String, Object> map = new HashMap<>();
            map.put(KeyConst.processId, processId);

            //同意
            auditBt1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    agreeReject_recall = 0;

                    if (info != null && "1".equals(info.lastNode) && Constant.PURCHASE.equals(TYPE)) {
                        View inflate = LayoutInflater.from(context).inflate(R.layout.
                                layout_purchase_agress, null);
                        final TextView payTypeTv = inflate.findViewById(R.id.pay_type_tv);
                        final EditText totalAmountEt = inflate.findViewById(R.id.total_amount_tv);
                        if (null != infoObj && !infoObj.isJsonNull()) {
                            String totalMoney = Utils.getObjStr(infoObj, KeyConst.totalAmount);
                            totalAmountEt.setText(totalMoney);
                            totalAmountEt.setSelection(totalMoney.length());
                        }
                        final EditText remarkEt = inflate.findViewById(R.id.dialog_remark_tv);

                        new MaterialDialog.Builder(context)
                                .customView(inflate, false)
                                .positiveColorRes(R.color.mainColor)
                                .positiveText(R.string.sure)
                                .autoDismiss(false)
                                .onPositive(new MaterialDialog.SingleButtonCallback() {
                                    @Override
                                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                        String totalAmount = totalAmountEt.getText().toString();
                                        if (ToastUtil.showCannotEmpty(context, totalAmount, "采购总价格")) {
                                            return;
                                        }
                                        if (ToastUtil.showCannotEmpty(context, payType, "支付方式")) {
                                            return;
                                        }
                                        String remark = remarkEt.getText().toString();
                                        if (employeeIdArr != null && employeeIdArr.size() > 0 && !employeeIdArr.isJsonNull()) {
                                            JSONArray informList = new JSONArray();
                                            for (JsonElement element : employeeIdArr) {
                                                informList.put(element.getAsInt());
                                            }
                                            map.put(KeyConst.informList, informList);
                                        }
                                        map.put(KeyConst.totalAmount, totalAmount);
                                        map.put(KeyConst.payType, payType);
                                        map.put(KeyConst.remark, remark);
                                        map.put(KeyConst.auditResult, true);
                                        bottomAuditBtPost(new JSONObject(map));
                                        dialog.dismiss();
                                    }
                                })
                                .negativeColorRes(R.color.mainColor)
                                .negativeText(R.string.cancel)
                                .onNegative(new MaterialDialog.SingleButtonCallback() {
                                    @Override
                                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                        dialog.dismiss();
                                    }
                                })
                                .show();

                        DialogUtils.showKeyBorad(totalAmountEt, context);

                        payTypeTv.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                String url = Constant.WEB_SITE + "/dict/dicts/cached/PAY_TYPE";

                                Response.Listener<JsonArray> successListener = new Response
                                        .Listener<JsonArray>() {
                                    @Override
                                    public void onResponse(JsonArray result) {

                                        if (result != null || result.size() == 0) {
                                            payTypeArr.clear();
                                            for (int i = 0; i < result.size(); i++) {
                                                JsonObject object = result.get(i).getAsJsonObject();
                                                String status = object.get(KeyConst.status).getAsString();
                                                if (Constant.DICT_STATUS_USED.equals(status)) {
                                                    payTypeArr.add(object.get(KeyConst.name).getAsString());
                                                }
                                            }
                                            new MaterialDialog.Builder(context)
                                                    .items(payTypeArr)// 列表数据
                                                    .itemsCallback(new MaterialDialog.ListCallback() {
                                                        @Override
                                                        public void onSelection(MaterialDialog dialog, View itemView,
                                                                                int position, CharSequence text) {
                                                            payType = payTypeArr.get(position);
                                                            payTypeTv.setText(payType);
                                                        }
                                                    }).show();

                                        } else {
                                            ToastUtil.show(context, "获取支付方式失败");
                                        }
                                    }
                                };

                                Request<JsonArray> versionRequest = new
                                        GsonRequest<JsonArray>(Request.Method.GET, url,
                                                successListener, new Response.ErrorListener() {
                                            @Override
                                            public void onErrorResponse(VolleyError volleyError) {
                                                Log.d(TAG, "解析:" + volleyError.toString());
                                            }
                                        }, new TypeToken<JsonArray>() {
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
                        });


                    } else {
                        MaterialDialog.Builder inputDialog = DialogUtils.getInputDialog(context, "请输入备注信息");
                        inputDialog.negativeText(R.string.sure);
                        inputDialog.onNegative(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {

                                String remark = ((EditText) dialog.getCustomView()).getText().toString();
                                if (employeeIdArr != null && employeeIdArr.size() > 0 && !employeeIdArr.isJsonNull()) {
                                    JSONArray informList = new JSONArray();
                                    for (JsonElement element : employeeIdArr) {
                                        informList.put(element.getAsInt());
                                    }
                                    map.put(KeyConst.informList, informList);
                                }
                                map.put(KeyConst.remark, remark);
                                map.put(KeyConst.auditResult, true);
                                bottomAuditBtPost(new JSONObject(map));

                            }
                        }).show();
                    }
                }
            });
            //驳回
            auditBt2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    agreeReject_recall = 0;
                    MaterialDialog.Builder inputDialog =
                            DialogUtils.getInputDialog(context, "请输入备注信息");
                    inputDialog.negativeText(R.string.sure);
                    inputDialog.onNegative(new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                            String remark = ((EditText) dialog.getCustomView()).getText().toString();
                            if (employeeIdArr != null && employeeIdArr.size() > 0 && !employeeIdArr.isJsonNull()) {
                                JSONArray informList = new JSONArray();
                                for (JsonElement element : employeeIdArr) {
                                    informList.put(element.getAsInt());
                                }
                                map.put(KeyConst.informList, informList);
                            }
                            map.put(KeyConst.remark, remark + "");
                            map.put(KeyConst.auditResult, false);
                            bottomAuditBtPost(new JSONObject(map));
                        }
                    }).show();

                }
            });
        } else if (agreeReject_recall == 2) {//撤销
            auditLayout.setVisibility(View.VISIBLE);
            auditBt3.setVisibility(View.VISIBLE);
            //撤销
            auditBt3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    DialogUtils.getTwoBtDialog(context, "确定撤销该申请吗?\n(撤销后可在我的草稿里再次发起)")
                            .onPositive(new MaterialDialog.SingleButtonCallback() {
                                @Override
                                public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                    agreeReject_recall = 0;
                                    bottomAuditBtPost(null);
                                }
                            }).show();

                }
            });
        }


    }

    private void bottomAuditBtPost(JSONObject jsonObject) {
        if (!NetUtil.isNetworkConnected(context)) {
            ToastUtil.show(context, getString(R.string.no_network));
            return;
        }

        String url = Constant.WEB_SITE;
        if (jsonObject == null) {
            url = url + "/biz/process/launch/" + processId;
        } else {
            url = url + "/biz/process/needDo";
        }
        DialogHelper.showWaiting(getSupportFragmentManager(), getString(R.string.loading));
        JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.PUT, url,
                jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject result) {
                if (null != context && !context.isFinishing()) {
                    DialogHelper.hideWaiting(getSupportFragmentManager());
                }
                if (result != null && result.toString().contains("200")) {
                    context.finish();
                } else {
                    ToastUtil.show(context, R.string.commit_faild);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (null != context && !context.isFinishing()) {
                    DialogHelper.hideWaiting(getSupportFragmentManager());
                }
                if (error != null && error.networkResponse != null &&
                        400 == error.networkResponse.statusCode) {
                    DialogUtils.showTipDialog(context, getString(R.string.recall_failed));
                } else {
                    ToastUtil.show(context, getString(R.string.server_exception));
                }
            }
        }) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> params = new HashMap<>();
                params.put(KeyConst.Content_Type, Constant.application_json);
                params.put(KeyConst.Authorization, KeyConst.Bearer + App.token);

                return params;
            }
        };
        App.requestQueue.add(jsonRequest);
    }


    //设置数据
    private void setView() {
        createName = info.createName;
        initTitleBackBt(info.headline);
        ((TextView) findViewById(R.id.msg_name_tv)).setText(createName);
        ((TextView) findViewById(R.id.msg_name_tag_tv)).setText(TextUtil.getLast2(createName));
        TextView statusTv = (TextView) findViewById(R.id.work_start_status_tv);
        int status = info.status;
        statusTv.setText(Utils.getStatusText(status));
        statusTv.setTextColor(Utils.getStatusColor(context, status));

        statusTagIv.setImageResource(Utils.getStatusDrawable(status));

        //驳回
        if (status == 4 && typPosition > 13) {
            deletePost();
        }
    }

    private void setAuditLogView(List<HistoryInfo> planInfos) {
        copyerItemsLayout = (LinearLayout) findViewById(R.id.copyer_item_layout);
        planItemLayout.removeAllViews();
        planItemLayout.setVisibility(View.VISIBLE);
        int paddingTop = getResources().getDimensionPixelSize(R.dimen.dm023);
        for (int i = 0; i < planInfos.size(); i++) {
            final HistoryInfo itemInfo = planInfos.get(i);
            if (itemInfo == null) {
                return;
            }
            if ("已抄送".equals(itemInfo.getAuditStaus())) {
                initCopyerLayout(itemInfo.getEmployeeName());
                continue;
            }

            View itemView = View.inflate(context, R.layout.item_work_hostory, null);
            TextView ciycleNameTv = (TextView) itemView.findViewById(R.id.name_ciycle_tv);
            TextView nameTv = (TextView) itemView.findViewById(R.id.name_tv);
            TextView timeTv = (TextView) itemView.findViewById(R.id.time_tv);
            TextView remarkTv = (TextView) itemView.findViewById(R.id.remark_tv);
            TextView actionTv = (TextView) itemView.findViewById(R.id.action_tv);

            timeTv.setText(TextUtil.subTimeMDHm(itemInfo.getAuditTime()));//时间
            // "";
            if (TextUtil.isEmpty(itemInfo.getEmployeeName())) {
                Drawable drawablePassed = ContextCompat.getDrawable(context, R.drawable.ic_circle_passed);
                ciycleNameTv.setBackground(null);

                ciycleNameTv.setPadding(0, paddingTop, 0, 0);
                ciycleNameTv.setCompoundDrawablesWithIntrinsicBounds(null, drawablePassed, null, null);
                actionTv.setPadding(0, 0, 0, 0);
                actionTv.setText(Html.fromHtml("<font color='#4db1fc' >未找到审批人</font>，已自动通过"));
                planItemLayout.addView(itemView);
                continue;
            }
            String employeeName = TextUtil.remove_N(itemInfo.getEmployeeName());
            nameTv.setText(employeeName);
            ciycleNameTv.setText(TextUtil.getLast2(employeeName));//操作人
            actionTv.setText(0 == i ? "发起申请" : itemInfo.getAuditStaus());
            remarkTv.setText(itemInfo.getRemark());


            planItemLayout.addView(itemView);

        }
    }

    private void choisePicture() {
        int choose = 9 - pictures.size();
        Intent intent = new Intent(context, MulPictureActivity.class);
        bundle = setBundle();
        bundle.putInt("imageNum", choose);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtras(bundle);
        startActivityForResult(intent, 101);
    }

    //选择文件
    private void choiseFile() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");//设置类型，我这里是任意类型，任意后缀的可以这样写。
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        startActivityForResult(intent, 100);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //同行人员
        if (requestCode == 1 && resultCode == 2) {
            JsonArray choosedEmployeeIds = new JsonArray();
            String nameStr = "";
            parentList = (List<EmployeeInfo>) data.
                    getSerializableExtra(KeyConst.OBJ_INFO);

            for (EmployeeInfo employeeInfo : parentList) {

                choosedEmployeeIds.add(employeeInfo.getId());
                String name = employeeInfo.getEmployeeName();
                if (choosedEmployeeIds.size() == 1) {
                    nameStr = name;
                } else if (choosedEmployeeIds.size() < Constant.EMPLYEE_SHOW_NUMBER) {
                    nameStr = name + "、" + nameStr;
                }

            }
            if (choosedEmployeeIds.size() >= Constant.EMPLYEE_SHOW_NUMBER) {
                nameStr = nameStr + getString(R.string.ellipsis_more) + choosedEmployeeIds.size() + "人";
            }

            initCopyerLayout(nameStr);
            employeeIdArr = choosedEmployeeIds;
        }
        //上传附件
        String fileType = "";
        String path = "";
        if (data != null && data.getData() != null) {
            path = FileTypeUtil.getPath(context, data.getData());
            //不是合格的类型
            if (!FileTypeUtil.isFileType(path) && !ImageUtil.isImageSuffix(path)) {
                ToastUtil.show(context, "暂不支持该文件类型");
                return;
            }
            fileType = ImageUtil.isImageSuffix(path) ? Constant.FILE_TYPE_IMG : Constant.FILE_TYPE_DOC;
        }
        //上传图片
        if (requestCode == 101 && data != null) {
            setIntent(data);
            getBundleP();
            if (pictures != null && pictures.size() > 0) {
                fileType = Constant.FILE_TYPE_IMG;
                for (int i = 0; i < pictures.size(); i++) {
                    path = pictures.get(i).getLocalURL();
                    fileType = Constant.FILE_TYPE_IMG;
                }
            }
        }
        if (TextUtil.isEmpty(path)) {
            return;
        }
        File file = new File(path);
        uploadPictureThread(file, fileType);
    }

    public void getBundleP() {
        if (getIntent() != null) {
            bundle = getIntent().getExtras();
            if (bundle != null) {
                pictures = (List<PictBean>) bundle.getSerializable("pictures") != null ?
                        (List<PictBean>) bundle.getSerializable("pictures") : new
                        ArrayList<PictBean>();
            }
        }
    }

    private void uploadPictureThread(final File file, final String fileType) {
        if (!NetUtil.isNetworkConnected(context)) {
            ToastUtil.show(context, getString(R.string.no_network));
            return;
        }
        DialogHelper.showWaiting(getSupportFragmentManager(), getString(R.string.uploading));
        final HashMap<String, String> map = new HashMap<String, String>();
        map.put(KeyConst.fileType, fileType);
        final String url = Constant.WEB_FILE_UPLOAD;
        new Thread() {
            @Override
            public void run() {
                try {
                    RetrofitUtil.upLoadByCommonPost(url, file, map,
                            new RetrofitUtil.FileUploadListener() {
                                @Override
                                public void onProgress(long pro, double precent) {
                                }

                                @Override
                                public void onFinish(int code, final String responseUrl, Map<String, List<String>> headers) {
                                    DialogHelper.hideWaiting(getSupportFragmentManager());
                                    if (200 == code && responseUrl != null) {
                                        final String finalResponseUrl = responseUrl;
                                        context.runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                fileData.add(new FileListInfo(
                                                        file.getName(), finalResponseUrl, file.length(), finalResponseUrl));
                                                fileListAdapter.setDate(fileData);
                                                ImageUtil.reSetLVHeight(context, listView);
                                            }
                                        });
                                    }
                                }
                            });
                } catch (IOException e) {
                    DialogHelper.hideWaiting(getSupportFragmentManager());
                }

            }
        }.start();
    }

    private void setFileListData() {
        TextView linkTv = (TextView) findViewById(R.id.file_link_iv);
        TextView fileTitleTv = (TextView) findViewById(R.id.card_detail_file_title);
        fileTitleTv.setText(R.string.file_link_list);
        fileTitleTv.setTextColor(ContextCompat.getColor(this, R.color.color999));
        ScrollListView listView = (ScrollListView) findViewById(R.id.horizontal_gridview);
        JsonObject object = info.object;
        if (object == null) {
            return;
        }
        JsonArray jsonArray = object.getAsJsonArray(KeyConst.attachList);
        List<FileInfo> attList = new Gson().fromJson(jsonArray, new TypeToken<List<FileInfo>>() {
        }.getType());
        if (attList != null) {
            for (FileInfo att : attList) {
                fileData.add(new FileListInfo(att.name, att.url, Constant.TYPE_SEE));
            }
        }
        if (fileData == null || fileData.size() == 0) {
            findViewById(R.id.card_detail_file_layout).setVisibility(View.GONE);
        } else {
            linkTv.setVisibility(View.GONE);
        }
        FileListAdapter fileListAdapter = new FileListAdapter(this, fileData);
        listView.setAdapter(fileListAdapter);
    }

    //查询流程数据
    private void getData() {
        if (!NetUtil.isNetworkConnected(context)) {
            return;
        }
        DialogHelper.showWaiting(getSupportFragmentManager(), getString(R.string.loading));
        String url = Constant.WEB_SITE + "/biz/process/" + processId;
        Response.Listener<MsgInfo> successListener = new Response
                .Listener<MsgInfo>() {
            @Override
            public void onResponse(MsgInfo result) {
                DialogHelper.hideWaiting(getSupportFragmentManager());
                if (context == null || result == null) {
                    ToastUtil.show(context, R.string.no_data);
                    return;
                }
                info = result;
                setView();

                setTypeView13();
                setFileListData();//附件
            }
        };

        Request<MsgInfo> versionRequest = new
                GsonRequest<MsgInfo>(
                        Request.Method.GET, url,
                        successListener, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        ToastUtil.show(context, R.string.server_exception);
                        DialogHelper.hideWaiting(getSupportFragmentManager());
                    }
                }, new TypeToken<MsgInfo>() {
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

    private void getAuditLogData() {
        planItemLayout = (LinearLayout) findViewById(R.id.history_item_layout);
        String url = Constant.WEB_SITE + "/biz/process/" + processId + "/log";
        Response.Listener<List<HistoryInfo>> successListener = new Response.
                Listener<List<HistoryInfo>>() {
            @Override
            public void onResponse(List<HistoryInfo> result) {
                if (result == null || result.size() == 0) {
                    return;
                }
                setAuditLogView(result);
            }
        };
        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
            }
        };

        Request<List<HistoryInfo>> request = new
                GsonRequest<List<HistoryInfo>>(Request.Method.GET,
                        url, successListener, errorListener, new TypeToken<List<HistoryInfo>>() {
                }.getType()) {

                    @Override
                    public Map<String, String> getHeaders() throws AuthFailureError {
                        Map<String, String> params = new HashMap<>();
                        params.put(KeyConst.Authorization, KeyConst.Bearer + App.token);
                        return params;
                    }
                };
        App.requestQueue.add(request);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        context = null;
    }


    JsonArray employeeIdArr = new JsonArray();

    //不同的申请类型  不同字段
    private void setTypeView13() {
        TYPE = info.type;
        infoObj = info.object;
        String procNum = info.procNum;//审批编号;
        String deptName = info.createDeptName;//所在部门;
        addTopItems("审批编号", procNum);
        if (null != infoObj && !infoObj.isJsonNull()) {
            addTopItems(Constant.WAGE_AUDIT.equals(TYPE) ? "申请部门" : "所在部门", deptName);
            if (Constant.LEAVE.equals(TYPE)) {//请假
                addTopItems("请假类型", Utils.getDictTypeName(context,
                        KeyConst.LEAVE_TYPE, Utils.getObjStr(infoObj, KeyConst.leaveType)));

                JsonElement objArr = infoObj.get(KeyConst.leaveDateModelList);

                if (objArr != null && !objArr.isJsonNull()) {
                    JsonArray tripArr = objArr.getAsJsonArray();

                    if (tripArr != null && !tripArr.isJsonNull()) {
                        List<TripItemInfo> leaveList = new Gson().fromJson(tripArr, new TypeToken<List<TripItemInfo>>() {
                        }.getType());
                        for (int i = 0; i < leaveList.size(); i++) {
                            TripItemInfo info = leaveList.get(i);
                            String timeDuration = TextUtil.subTimeYmdHm(info.getStartTime())
                                    + "至" + TextUtil.subTimeYmdHm(info.getEndTime());
                            String period = "(" + info.getPeriod() + "小时)";
                            addTopItems("请假" + (i + 1), timeDuration + period);
                        }
                    }
                }
                setTypeData(0);
            } else if (Constant.BUSINESS_TRIP.equals(TYPE)) {//出差
                setTypeData(1);

                setTripsItemsLayout();//行程列表
            } else if (Constant.OVERTIME.equals(TYPE)) {//加班
                String typeValue = Utils.getObjStr(infoObj, KeyConst.type);
                addTopItems("加班类型", Utils.getDictTypeName(context, KeyConst.OVERTIME_TYPE, typeValue));
                boolean isBreakOff = "true".equals(Utils.getObjStr(infoObj, KeyConst.breakOff));

                String[] arr = getResources().getStringArray(R.array.whether_leave_arr);
                addTopItems("是否调休", arr[isBreakOff ? 0 : 1]);

                JsonElement objArr = infoObj.get(KeyConst.overtimeDateModelList);

                if (objArr == null || objArr.isJsonNull()) {
                    return;
                }
                JsonArray tripArr = infoObj.getAsJsonArray(KeyConst.overtimeDateModelList);

                if (tripArr == null || tripArr.size() == 0 || tripArr.isJsonNull()) {
                    return;
                }
                List<TripItemInfo> leaveList = new Gson().fromJson(tripArr, new TypeToken<List<TripItemInfo>>() {
                }.getType());

                for (int i = 0; i < leaveList.size(); i++) {
                    TripItemInfo info = leaveList.get(i);
                    String timeDuration = TextUtil.subTimeYmdHm(info.getStartTime())
                            + "至" + TextUtil.subTimeYmdHm(info.getEndTime());
                    String period = "(" + info.getPeriod() + "小时)";
                    addTopItems("加班" + (i + 1), timeDuration + period);
                }

                setTypeData(2);
            } else if (Constant.REGULAR_WORKER.equals(TYPE)) {//转正
                addTopItems("实际申请人", info.applicantName);
                addTopItems("所属部门", info.applicantDeptName);
                setTypeData(3);
            } else if (Constant.RECRUIT.equals(TYPE)) {//招聘
                setTypeData(4);
            } else if (Constant.DIMISSION.equals(TYPE)) {//离职
                addTopItems("实际申请人", info.applicantName);
                addTopItems("所属部门", info.applicantDeptName);
                setTypeData(5);
            } else if (Constant.OFFICIAL_DOCUMENT.equals(TYPE)) {//公文
                setTypeData(6);

                //审批权限  设置抄送人
                if (agreeReject_recall == 1) {
                    setInformPeopleBt();
                }
            } else if (TYPE.equals(Constant.REIMBURSE)) {//报销
                setTypeData(7);
                setReimbursesItemsLayout();
            } else if (Constant.PETTY_CASH.equals(TYPE)) {//备用金
                setTypeData(8);
            } else if (Constant.PAY.equals(TYPE)) {//付款
                setTypeData(9);
            } else if (Constant.PURCHASE.equals(TYPE)) {//申购采购
                setTypeData(10);
                setPurchaseItemsLayout();
            } else if (Constant.WAGE_AUDIT.equals(TYPE)) {//工资审核
                WAGE_YEAR = Utils.getObjStr(infoObj, KeyConst.wageYear);
                WAGE_MONTH = Utils.getObjStr(infoObj, KeyConst.wageMonth);
                if (!TextUtil.isEmpty(WAGE_MONTH)&&WAGE_MONTH.length()==1) {
                    WAGE_MONTH="0"+WAGE_MONTH;
                }
                addTopItems("日期", WAGE_YEAR + "年" + WAGE_MONTH + "月");

                String objStr = Utils.getObjStr(infoObj, KeyConst.totalHourNum);
                addTopItems("总计时", objStr.replace(".0", "") + "小时");

                String objStr0 = Utils.getObjStr(infoObj, KeyConst.totalPieceMonthNum);
                addTopItems("小料包计件", TextUtil.isEmpty(objStr0) ? "0包" : objStr0 + "包");
                String objStr02 = Utils.getObjStr(infoObj, KeyConst.totalPieceMonthPrice);
                addTopItems("小料包计件工资", TextUtil.isEmpty(objStr02) ? "0元" : objStr02 + "元");

                String objStr1 = Utils.getObjStr(infoObj, KeyConst.totalPieceNum);
                addTopItems("其他计件", objStr1.replace(".0", ""));
              /*  String objStr12 = Utils.getObjStr(infoObj, KeyConst.pieceWage);
                addTopItems("其他计件工资", TextUtil.isEmpty(objStr12) ? "0元" : objStr12 + "元");*/

                String objStr2 = Utils.getObjStr(infoObj, KeyConst.totalDeduction);
                addTopItems("总扣款", TextUtil.isEmpty(objStr2) ? "0元" : objStr2.replace(".00", "") + "元");
                String objStr3 = Utils.getObjStr(infoObj, KeyConst.totalAttendReward);
                addTopItems("奖金", TextUtil.isEmpty(objStr3) ? "0元" : objStr3.replace(".00", "") + "元");
                String personNum = Utils.getObjStr(infoObj, KeyConst.personNum);
                addTopItems("参与人员", TextUtil.isEmpty(personNum) ? "0人" : personNum + "人");
                String totalWage = Utils.getObjStr(infoObj, KeyConst.totalWage);
                addTopRedItems("合计", TextUtil.isEmpty(totalWage) ? "0元" : totalWage + "元");

                //项目月度工资
                addWageMonthItems();

                        //审批权限  设置抄送人
                if (agreeReject_recall == 1) {
                    setInformPeopleBt();
                }
            } else if (TYPE.equals(Constant.NOTICE)) {//公告

            } else if (Constant.WEIGH.equals(TYPE)) {//过磅
          /*      View view1 = View.inflate(context, R.layout.layout_msg_detail_title_tv, null);
                ((TextView) view1.findViewById(R.id.title_tv)).setText("产品信息");
                itemsLayout1.addView(view1);*/
                setTypeData(11);

                addLayout2Title("过磅明细");
                addView2("毛重", Utils.getObjDouble(infoObj, KeyConst.grossWeight) + "kg",
                        "皮重", Utils.getObjDouble(infoObj, KeyConst.tareWeight) + "kg");

                addView2("扣杂", Utils.getObjDouble(infoObj, KeyConst.deductImpurity) + "kg",
                        "净重", Utils.getObjDouble(infoObj, KeyConst.netWeight) + "kg");

                addView2("不良率", Utils.getObjDouble(infoObj, KeyConst.result) + "%",
                        "实重", Utils.getObjDouble(infoObj, KeyConst.realWeight) + "kg");
                addView2("单价", Utils.getObjDouble(infoObj, KeyConst.price) + "元/kg",
                        "金额", Utils.getObjDouble(infoObj, KeyConst.totalPrice) + "元");
            } else if (Constant.RAW_PUT_IN_POOL.equals(TYPE)) {//入池单

                addTopItems("关联过磅单号", Utils.getObjStr(infoObj, KeyConst.weighProcNum));

                addTopItems("产品名称", Utils.getObjStr(infoObj, KeyConst.productName));
                addTopItems("产品规格", Utils.getObjStr(infoObj, KeyConst.productSpecification));
                JsonElement detailEle = infoObj.get(KeyConst.weighVar);
                if (detailEle != null && !detailEle.isJsonNull()) {
                    JsonObject detailObj = detailEle.getAsJsonObject();
                    addTopItems("车牌号", Utils.getObjStr(detailObj, KeyConst.numberPlate));
                    addTopItems("产地", Utils.getObjStr(detailObj, KeyConst.originPlace));
                }
                addTopItems("备注", Utils.getObjStr(infoObj, KeyConst.remark));

                addLayout2Title("入池明细");
                addView1("池号", Utils.getObjStr(infoObj, KeyConst.poolName));
                addView1("入池时间", Utils.getObjStr(infoObj, KeyConst.putInPoolDate));
                addView1("入池数量(kg)", Utils.getObjStr(infoObj, KeyConst.amount));

                String discharger = Utils.getObjStr(infoObj, KeyConst.discharger);
                if (!TextUtil.isEmpty(discharger)) {
                    addView1("卸车负责人", discharger);
                }

            } else if (Constant.INGREDIENT_PUT_IN_POOL.equals(TYPE)) {//配料入池

                addTopItems("池号", Utils.getObjStr(infoObj, KeyConst.poolName));
                addTopItems("配料入池时间", Utils.getObjStr(infoObj, KeyConst.putInPoolDate));
                addTopItems("产品名称", Utils.getObjStr(infoObj, KeyConst.productName));
                addTopItems("产品规格", Utils.getObjStr(infoObj, KeyConst.productSpecification));

                addTopItems("配料员", Utils.getObjStr(infoObj, KeyConst.ingredientor));


                addTopItems("备注", Utils.getObjStr(infoObj, KeyConst.remark));

                addLayout2Title("配料明细");

                JsonElement objArr = infoObj.get(KeyConst.componentItemList);
                if (objArr == null || objArr.isJsonNull()) {
                    return;
                }
                JsonArray detailArr = objArr.getAsJsonArray();
                if (detailArr == null || detailArr.isJsonNull()) {
                    return;
                }
                List<PoolInfo.ComponentItemListBean> componentItemList = new Gson().fromJson(detailArr,
                        new TypeToken<List<PoolInfo.ComponentItemListBean>>() {
                        }.getType());

                if (componentItemList != null) {
                    addComponentItemView(componentItemList);
                }

            } else if (Constant.SPRINKLE.equals(TYPE)) {//回淋
                setTypeData(12);
                addTopItems("操作员", info.applicantName);
                addTopItems("主管", info.directorNameOfApplicant);
                addTopItems("备注", Utils.getObjStr(infoObj, KeyConst.remark));

            } else if (Constant.TAKE_OUT_POOL.equals(TYPE)) {//起池
                addTopItems("池号", Utils.getObjStr(infoObj, KeyConst.poolName));
                addTopItems("产品名称", Utils.getObjStr(infoObj, KeyConst.productName));
                addTopItems("产品规格", Utils.getObjStr(infoObj, KeyConst.productSpecification));
                addTopItems("出池时间", Utils.getObjStr(infoObj, KeyConst.takeOutPoolDate));
                addTopItems("出池数量(kg)", Utils.getObjStr(infoObj, KeyConst.amount));

                int objectPoolType = Integer.valueOf(Utils.getObjDouble(infoObj, KeyConst.type));//是否并池 1:否   2: 是

                addTopItems("是否并池", TextUtil.getObjectPoolStr(objectPoolType));
                if (objectPoolType == Constant.OBJECT_POOL_TRUE) {
                    addTopItems("并入池号", Utils.getObjStr(infoObj, KeyConst.objectPoolName));
                }
                addTopItems("感官风味确认", Utils.getObjStr(infoObj, KeyConst.confirmTaste));
                addTopItems("操作员", info.applicantName);
                addTopItems("主管", info.directorNameOfApplicant);
                addTopItems("备注", Utils.getObjStr(infoObj, KeyConst.remark));

            } else if (Constant.RAW_MATERIAL_CHECK.equals(TYPE)) {//原料入池检验
                //addTopItems("过磅单号", Utils.getObjStr(infoObj, KeyConst.weighProcNum));
                addTopItems("产品名称", Utils.getObjStr(infoObj, KeyConst.productName));
                addTopItems("车牌号", Utils.getObjStr(infoObj, KeyConst.numberPlate));
                //addTopItems("过磅时间", Utils.getObjStr(infoObj, KeyConst.weighDate));
                addTopItems("备注", Utils.getObjStr(infoObj, KeyConst.remark));

                addLayout2Title("原料检验明细");
                addView1("入货时间", Utils.getObjStr(infoObj, KeyConst.goodsArriveDate));
                addView1("检验时间", Utils.getObjStr(infoObj, KeyConst.checkDate));
                addView1("基地", Utils.getObjStr(infoObj, KeyConst.baseLocate));
                addView1("综合判定", Utils.getObjStr(infoObj, KeyConst.comprehensiveDetermination));

                JsonElement checkItemListEle = infoObj.get(KeyConst.checkItemList);
                if (checkItemListEle != null && !checkItemListEle.isJsonNull()) {
                    JsonArray checkItemListArr = checkItemListEle.getAsJsonArray();
                    if (checkItemListArr != null && !checkItemListArr.isJsonNull()) {
                        List<ProductInfo> checkItemList = new Gson().fromJson(checkItemListArr,
                                new TypeToken<List<ProductInfo>>() {
                                }.getType());
                        addCheckItemView(checkItemList);
                    }
                }

            } else if (Constant.SEMIFINISHED_PRODUCT_CHECK.equals(TYPE)) {//半成品
                addTopItems("池号", Utils.getObjStr(infoObj, KeyConst.poolName));
                addTopItems("产品名称", Utils.getObjStr(infoObj, KeyConst.productName));
                addTopItems("产品规格", Utils.getObjStr(infoObj, KeyConst.productSpecification));
                addTopItems("检验时间", Utils.getObjStr(infoObj, KeyConst.checkDate));
                addTopItems("备注", Utils.getObjStr(infoObj, KeyConst.remark));

                addLayout2Title("半成品检验明细");
                JsonElement checkItemListEle = infoObj.get(KeyConst.checkItemList);
                if (checkItemListEle != null && !checkItemListEle.isJsonNull()) {
                    JsonArray checkItemListArr = checkItemListEle.getAsJsonArray();
                    if (checkItemListArr != null && !checkItemListArr.isJsonNull()) {
                        List<ProductInfo> checkItemList = new Gson().fromJson(checkItemListArr,
                                new TypeToken<List<ProductInfo>>() {
                                }.getType());
                        addCheckItemView(checkItemList);
                    }
                }
            }

        }
    }

    private void addCheckItemView(List<ProductInfo> checkItemList) {
        for (ProductInfo info : checkItemList) {
            itemView = View.inflate(context, R.layout.item_msg_detail_check_item, null);
            TextView nameTv = (TextView) itemView.findViewById(R.id.title_tv);
            TextView nameTagTv = (TextView) itemView.findViewById(R.id.title_norm_tv);

            TextView resultTv = (TextView) itemView.findViewById(R.id.check_item_result_tv);
            TextView determinationTv = (TextView) itemView.findViewById(R.id.check_item_determination_tv);//检测结果

            nameTv.setText(info.getName());
            resultTv.setText(info.getResult());
            determinationTv.setText(info.getDetermination());

            String standardStr = "";
            ProductInfo.StandardBean standard = info.getStandard();
            if (standard != null) {
                if ("2".equals(info.getType())) {//百分比 %
                    int startPoint = standard.getStartPoint();
                    int endPoint = standard.getEndPoint();
                    standardStr = startPoint + "~" + endPoint;
                } else {//只有1种
                    standardStr = standard.getRemark();
                }

            }
            nameTagTv.setText("(标准:" + standardStr + ")");
            itemsLayout2.addView(itemView);
        }
    }

    //配料
    private void addComponentItemView
    (List<PoolInfo.ComponentItemListBean> componentItemList) {
        for (PoolInfo.ComponentItemListBean info : componentItemList) {
            itemView = View.inflate(context, R.layout.item_msg_detail_component_item, null);
            TextView nameTv = (TextView) itemView.findViewById(R.id.title_tv);
            TextView nameTagTv = (TextView) itemView.findViewById(R.id.title_norm_tv);

            TextView amountTv = (TextView) itemView.findViewById(R.id.item_amount_tv);
            TextView dateTv = (TextView) itemView.findViewById(R.id.item_manufacture_date_tv);
            TextView manufacturerTv = (TextView) itemView.findViewById(R.id.item_manufacturer_tv);//检测结果

            nameTv.setText(info.getName());

            amountTv.setText(info.getAmount() + "kg");
            dateTv.setText(info.getManufactureDate());
            manufacturerTv.setText(info.getManufacturer());

            itemsLayout2.addView(itemView);
        }

    }

    private void addLayout2Title(String title) {
        View view2 = View.inflate(context, R.layout.layout_msg_detail_title_tv, null);
        ((TextView) view2.findViewById(R.id.title_tv)).setText(title);
        itemsLayout2.addView(view2);
    }

    private void addView1(String key1, String value1) {
        itemView = View.inflate(context, R.layout.item_key_value, null);
        ((TextView) itemView.findViewById(R.id.item_key_tv)).setText(key1);
        ((TextView) itemView.findViewById(R.id.item_value_tv)).setText(value1);
        itemsLayout2.addView(itemView);
    }

    private void addView2(String key1, String value1, String key2, String value2) {
        itemView = View.inflate(context, R.layout.item_key_value_2, null);
        ((TextView) itemView.findViewById(R.id.item_key_tv1)).setText(key1);
        ((TextView) itemView.findViewById(R.id.item_key_tv2)).setText(key2);
        ((TextView) itemView.findViewById(R.id.item_value_tv1)).setText(value1);
        ((TextView) itemView.findViewById(R.id.item_value_tv2)).setText(value2);
        itemsLayout2.addView(itemView);
    }

    //添加抄送人
    private void setInformPeopleBt() {
        informPeopleAddBt.setVisibility(View.VISIBLE);
        informPeopleAddBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putSerializable(KeyConst.OBJ_INFO, (Serializable) parentList);//序列化,要注意转化(Serializable)
                Intent intent = new Intent(context, ChooseEmplyeeActivity.class);
                intent.putExtras(bundle);
                context.startActivityForResult(intent, 1);
            }
        });

        if (info.informList != null && !info.informList.isJsonNull()) {
            employeeIdArr = info.informList.getAsJsonArray();
        }
        //抄送人
        String informListName = info.informNameList;
        initCopyerLayout(informListName);//公文抄送人
    }

    String[] titleArr0 = {"总时长(小时)", "请假事由"};
    String[] valueArr0 = {KeyConst.period, KeyConst.remark};

    String[] titleArr1 = {"出差事由", "关联报销", "出差天数", "同行人员", "出差备注"};
    String[] valueArr1 = {KeyConst.reason, KeyConst.reimburseProcessNum, KeyConst.period,
            KeyConst.peers, KeyConst.remark};

    String[] titleArr2 = {"总时长(小时)", "加班原因"};
    String[] valueArr2 = {KeyConst.period, KeyConst.remark};

    String[] titleArr3 = {"入职日期", "试用期(月)", "转正日期", "工作期间表现"};//转正
    String[] valueArr3 = {KeyConst.employmentDate,
            KeyConst.probationPeriod, KeyConst.regularDate, KeyConst.remark};

    String[] titleArr4 = {"需求岗位", "需求人数(人)", "期望到岗日期", "岗位职责需求"};//招聘
    String[] valueArr4 = {KeyConst.recruitPost, KeyConst.recruitNum, KeyConst.arrivalDate, KeyConst.remark};

    String[] titleArr5 = {"入职日期", "最后工作日", "离职原因"};//离职
    String[] valueArr5 = {KeyConst.employmentDate, KeyConst.lastWorkDate, KeyConst.remark};

    String[] titleArr6 = {"公文内容"};
    String[] valueArr6 = {KeyConst.remark};

    String[] titleArr7 = {"关联出差", "报销总金额(元)"};
    String[] valueArr7 = {KeyConst.businessTripNum, KeyConst.totalAmount};

    String[] titleArr8 = {"申请金额(元)", "使用日期", "归还日期", "申请事由"};
    String[] valueArr8 = {KeyConst.applyAmount, KeyConst.useDate, KeyConst.returnDate, KeyConst.remark};

    String[] titleArr9 = {"付款金额(元)", "付款方式", "支付日期", "支付对象", "开户行", "银行账户", "付款事由"};
    String[] valueArr9 = {KeyConst.amount, KeyConst.payType, KeyConst.payDate,
            KeyConst.payObject, KeyConst.bankName, KeyConst.bankAccount, KeyConst.remark
    };

    String[] titleArr10 = {"申请事由", "采购类型", "期望交付日期", "总金额合计(元)", "支付方式", "备注"};//申购
    String[] valueArr10 = {KeyConst.reason, KeyConst.purchaseType, KeyConst.deliverDate,
            KeyConst.totalAmount, KeyConst.payType,
            KeyConst.remark};
    String[] titleArr11 = {"产品名称", "产地", "车牌号", "驾驶员", "过磅时间", "关联原料检验单号", "备注"};
    String[] valueArr11 = {KeyConst.productName, KeyConst.originPlace,
            KeyConst.numberPlate, KeyConst.driverName, KeyConst.weighDate, KeyConst.processNum, KeyConst.remark};

    String[] titleArr12 = {"池号", "产品名称", "产品规格", "腌渍时间", "回淋时间", "回淋时长\n(min)", "波美度(1\n1±2°Bé)"};
    String[] valueArr12 = {KeyConst.poolName, KeyConst.productName, KeyConst.productSpecification, KeyConst.pickleDate, KeyConst.sprinkleDate,
            KeyConst.period, KeyConst.baumeDegree};


    List<String[]> titleArrList = new ArrayList<>();
    List<String[]> valueArrList = new ArrayList<>();

    private void initData() {
        titleArrList.add(titleArr0);
        titleArrList.add(titleArr1);
        titleArrList.add(titleArr2);
        titleArrList.add(titleArr3);
        titleArrList.add(titleArr4);
        titleArrList.add(titleArr5);
        titleArrList.add(titleArr6);
        titleArrList.add(titleArr7);
        titleArrList.add(titleArr8);
        titleArrList.add(titleArr9);
        titleArrList.add(titleArr10);
        titleArrList.add(titleArr11);
        titleArrList.add(titleArr12);

        valueArrList.add(valueArr0);
        valueArrList.add(valueArr1);
        valueArrList.add(valueArr2);
        valueArrList.add(valueArr3);
        valueArrList.add(valueArr4);
        valueArrList.add(valueArr5);
        valueArrList.add(valueArr6);
        valueArrList.add(valueArr7);
        valueArrList.add(valueArr8);
        valueArrList.add(valueArr9);
        valueArrList.add(valueArr10);
        valueArrList.add(valueArr11);
        valueArrList.add(valueArr12);
    }

    private void setTypeData(int type) {
        String[] titleArr = titleArrList.get(type);
        String[] valueArr = valueArrList.get(type);
        for (int i = 0; i < titleArr.length; i++) {
            String titleStr = titleArr[i];
            String key = valueArr[i];
            if ("试用期(月)".equals(titleStr)) {
                addTopItems(titleStr, Utils.getObjStr(infoObj, key).replace(".00", ""));
            } else if ("驾驶员".equals(titleStr)) {
                addTopItems(titleStr, Utils.getObjStr(infoObj, key) + "(" +
                        Utils.getObjStr(infoObj, KeyConst.driverPhone) + ")"
                );
            } else {
                addTopItems(titleStr, Utils.getObjStr(infoObj, key));
            }

        }
    }

    private void getItemData(final int deptId, final String title, final boolean lastIndex,
                             final int orderBy) {
        if (parentList == null) {
            parentList = new ArrayList<>();
        }
        if (!NetUtil.isNetworkConnected(context)) {
            return;
        }
        String url = Constant.WEB_SITE + "/upms/departments/" + deptId + "/employees/all?included=0";
        Response.Listener<List<GroupInfo.ChildrenBean>> successListener = new Response
                .Listener<List<GroupInfo.ChildrenBean>>() {
            @Override
            public void onResponse(List<GroupInfo.ChildrenBean> result) {

            }
        };

        Request<List<GroupInfo.ChildrenBean>> versionRequest = new
                GsonRequest<List<GroupInfo.ChildrenBean>>(Request.Method.GET, url,
                        successListener, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                    }
                }, new TypeToken<List<GroupInfo.ChildrenBean>>() {
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


}
