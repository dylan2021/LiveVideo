package com.android.livevideo.act_1;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.android.livevideo.App;
import com.android.livevideo.R;
import com.android.livevideo.act_other.CommonBaseActivity;
import com.android.livevideo.act_other.PictBean;
import com.android.livevideo.adapter.FileListAdapter;
import com.android.livevideo.bean.DictInfo;
import com.android.livevideo.bean.FileInfo;
import com.android.livevideo.bean.MsgInfo;
import com.android.livevideo.bean.PoolInfo;
import com.android.livevideo.bean.PurchaseInfo;
import com.android.livevideo.bean.FileListInfo;
import com.android.livevideo.core.net.GsonRequest;
import com.android.livevideo.core.utils.Constant;
import com.android.livevideo.core.utils.DialogHelper;
import com.android.livevideo.core.utils.ImageUtil;
import com.android.livevideo.core.utils.KeyConst;
import com.android.livevideo.core.utils.NetUtil;
import com.android.livevideo.core.utils.RetrofitUtil;
import com.android.livevideo.core.utils.TextUtil;
import com.android.livevideo.util.DialogUtils;
import com.android.livevideo.util.TimeUtils;
import com.android.livevideo.util.FileTypeUtil;
import com.android.livevideo.util.MoneyInputFilter;
import com.android.livevideo.util.ToastUtil;
import com.android.livevideo.util.Utils;
import com.android.livevideo.view.ScrollListView;
import com.android.livevideo.widget.mulpicture.MulPictureActivity;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.jzxiang.pickerview.TimePickerDialog;
import com.jzxiang.pickerview.listener.OnDateSetListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * @author Gool Lee
 * 请购单
 */

public class Work_13Activity extends CommonBaseActivity {
    private Work_13Activity context;
    private TextView deliverDateTv;
    private ScrollListView listView;
    private FileListAdapter fileListAdapter;
    private FragmentManager fm;
    private LinearLayout buyApplyLayout;
    private List<PurchaseInfo> purchaseInfoList = new ArrayList<>();
    private double totalAmount = 0;
    private int TYPE;
    private EditText buyApplyReasonEt, remarkEt;
    private String buyApplyReason;
    private TextView purchaseTypeTv;
    private TextView totalSumTv;
    //private TextView onPayTypeTv;
    private String purchaseType;
    private String deliverDate;
    private String payType;
    private int processId, RESULT_CODE = 3;
    private MsgInfo info;
    private JsonObject infoObj;
    private List<DictInfo.DictValuesBean> unitDictList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initStatusBar();
        setContentView(R.layout.activity_work_13);
        context = this;
        fm = getSupportFragmentManager();

        initTitleBackBt("请购单");

        processId = getIntent().getIntExtra(KeyConst.id, 0);
        TYPE = getIntent().getIntExtra(KeyConst.type, 0);
        getPurchaseTypeDict();

        initView();
        purchaseInfoList = new ArrayList<>();
        purchaseInfoList.add(new PurchaseInfo(0, "", "", "", ""));

        initFileView();

        initFinishBt();


        if (processId != 0) {
            getInfoData();
            deleteDraftBt();
        } else {
            notifyReimburseLayout(purchaseInfoList);
        }

        getDictUnitList("unit");
    }

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

    //草稿箱进来
    private void setDraftView() {
        infoObj = info.object;
        if (infoObj == null || infoObj.isJsonNull()) {
            return;
        }
        JsonElement obj = infoObj.get(KeyConst.purchaseDetail);
        if (obj == null || obj.isJsonNull()) {
            return;
        }
        buyApplyReasonEt.setText(Utils.getObjStr(infoObj, KeyConst.reason));

        payType = Utils.getObjStr(infoObj, KeyConst.payType);
        //onPayTypeTv.setText(payType);

        purchaseType = Utils.getObjStr(infoObj, KeyConst.purchaseType);
        purchaseTypeTv.setText(purchaseType);


        deliverDate = Utils.getObjStr(infoObj, KeyConst.deliverDate);
        deliverDateTv.setText(deliverDate);

        totalAmount = infoObj.get(KeyConst.totalAmount).getAsDouble();
        totalSumTv.setText(totalAmount + "");

        remarkEt.setText(Utils.getObjStr(infoObj, KeyConst.remark));

        JsonArray objArr = info.object.getAsJsonArray(KeyConst.purchaseDetail);
        if (objArr == null || objArr.size() == 0 || objArr.isJsonNull()) {
            return;
        }
        purchaseInfoList = new Gson().fromJson(
                objArr, new TypeToken<List<PurchaseInfo>>() {
                }.getType());

        notifyReimburseLayout(purchaseInfoList);
    }

    private void getInfoData() {
        if (!NetUtil.isNetworkConnected(context)) {
            return;
        }
        DialogHelper.showWaiting(getSupportFragmentManager(), "加载中...");
        String url = Constant.WEB_SITE + "/biz/process/" + processId;
        Response.Listener<MsgInfo> successListener = new Response
                .Listener<MsgInfo>() {
            @Override
            public void onResponse(MsgInfo result) {
                DialogHelper.hideWaiting(getSupportFragmentManager());
                if (result == null) {
                    ToastUtil.show(context, R.string.no_data);
                    return;
                }
                info = result;

                setDraftView();

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

    private String[] purchaseTypeArr;
    private List<String> payTypeArr = new ArrayList();

    private void getPayTypeDict() {
        if (!NetUtil.isNetworkConnected(context)) {
            return;
        }
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
                                    //onPayTypeTv.setText(payType);
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

    //请购类别
    private void getPurchaseTypeDict() {
        String url = Constant.WEB_SITE + "/dict/dicts/cached/PURCHASE_TYPE";

        Response.Listener<JsonArray> successListener = new Response
                .Listener<JsonArray>() {
            @Override
            public void onResponse(JsonArray result) {
                if (result != null) {
                    purchaseTypeArr = new String[result.size()];
                    for (int i = 0; i < result.size(); i++) {
                        JsonObject object = result.get(i).getAsJsonObject();
                        purchaseTypeArr[i] = object.get(KeyConst.name).getAsString();
                    }
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

    private void initView() {
        buyApplyLayout = findViewById(R.id.layout_item_layout_12);
        buyApplyReasonEt = findViewById(R.id.buy_apply_reason_et);
        remarkEt = findViewById(R.id.remark_tv);
  /*      onPayTypeTv = findViewById(R.id.on_pay_type_tv);
        onPayTypeTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getPayTypeDict();
            }
        });*/
        totalSumTv = findViewById(R.id.total_price_tv);
        purchaseTypeTv = findViewById(R.id.buy_type_value_tv);//采购类型
        purchaseTypeTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (purchaseTypeArr == null || purchaseTypeArr.length == 0) {
                    getPurchaseTypeDict();
                    return;
                }
                new MaterialDialog.Builder(context)
                        .items(purchaseTypeArr)// 列表数据
                        .itemsCallback(new MaterialDialog.ListCallback() {
                            @Override
                            public void onSelection(MaterialDialog dialog, View itemView,
                                                    int position, CharSequence text) {
                                purchaseType = purchaseTypeArr[position];
                                purchaseTypeTv.setText(purchaseType);
                            }
                        }).show();

            }
        });
        deliverDateTv = findViewById(R.id.expect_get_time_tv);
        deliverDateTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TimePickerDialog.Builder timeDialog = DialogUtils.getTimePicker(context);
                timeDialog.setCallBack(new OnDateSetListener() {
                    @Override
                    public void onDateSet(TimePickerDialog dialog, long millseconds) {
                        if (millseconds < TimeUtils.getTodayZeroTime()) {
                            ToastUtil.show(context, "期望交付日期不能小于今天");
                            return;
                        }

                        deliverDate = TimeUtils.getTimeYmd(millseconds);
                        deliverDateTv.setText(deliverDate);
                    }
                });
                timeDialog.build().show(context.getSupportFragmentManager(), "");
            }
        });
    }

    //添加行程
    public void onItemAddClick(View view) {
        purchaseInfoList.add(new PurchaseInfo(0, "", "", "", ""));
        notifyReimburseLayout(purchaseInfoList);
    }


    //请购明细
    private void notifyReimburseLayout(List<PurchaseInfo> planInfos) {
        if (buyApplyLayout == null) {
            return;
        }
        buyApplyLayout.removeAllViews();
        final int size = planInfos.size();
        for (int i = 0; i < size; i++) {
            final int finalI = i;
            View itemView = View.inflate(context, R.layout.item_work_add_13, null);
            final TextView deleteBt = (TextView) itemView.findViewById(R.id.item_9_delete_bt);
            TextView titleTv = (TextView) itemView.findViewById(R.id.item_9_title_tv);

            final TextView productDetailNoTv = itemView.findViewById(R.id.buy_material_number_tv);

            final TextView nameTv = itemView.findViewById(R.id.buy_item_name_et);

            final TextView brandTv = itemView.findViewById(R.id.buy_item_brand_et);
            final TextView manufacturerTv = itemView.findViewById(R.id.buy_item_manufacturer_et);

            final TextView specEt = itemView.findViewById(R.id.buy_item_spec_et);
            final TextView unitEt = itemView.findViewById(R.id.buy_item_unit_et);

            final EditText numberEt = itemView.findViewById(R.id.buy_item_number_et);
            InputFilter[] moneyFilters = {new MoneyInputFilter()};
            numberEt.setFilters(moneyFilters);

            final EditText priceEt = itemView.findViewById(R.id.buy_item_price_et);
            priceEt.setFilters(moneyFilters);

            PurchaseInfo info = planInfos.get(i);
            final String titlePotionStr = "采购明细(" + (i + 1) + ")";
            purchaseInfoList.get(i).setTitleName(titlePotionStr);
            titleTv.setText(titlePotionStr);

            String price = info.getPrice();
            String number = info.getNumber();

            productDetailNoTv.setText(info.getProductDetailNo());
            nameTv.setText(info.getName());
            brandTv.setText(info.getBrand());
            manufacturerTv.setText(info.getManufacturer());

            specEt.setText(info.getSpec());

            //单位列表-字典
            String unit = info.getUnit();
            for (DictInfo.DictValuesBean dictValue : unitDictList) {
                String value = dictValue.getValue();
                if (value.equals(unit)) {
                    unitEt.setText(dictValue.getName());
                    break;
                }
            }
            numberEt.setText(number);
            priceEt.setText(price);

            //选择料号
            productDetailNoTv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //选择池号
                    Intent intent = new Intent(context, PoolChooseActivity.class);
                    intent.putExtra(KeyConst.type, RESULT_CODE);//池号
                    context.startActivityForResult(intent, finalI);
                }
            });

            numberEt.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View view, boolean isfocuce) {
                    if (!isfocuce & finalI < purchaseInfoList.size()) {
                        purchaseInfoList.get(finalI).setNumber(numberEt.getText().toString());
                    }
                }
            });
            priceEt.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View view, boolean isfocuce) {
                    if (!isfocuce & finalI < purchaseInfoList.size()) {
                        purchaseInfoList.get(finalI).setPrice(priceEt.getText().toString());
                    }
                }
            });
            priceEt.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void afterTextChanged(Editable editable) {
                    if (finalI < purchaseInfoList.size()) {
                        purchaseInfoList.get(finalI).setPrice(priceEt.getText().toString());
                    }
                    sumMoney(purchaseInfoList);
                }
            });

            //删除按钮
            if (size > 1) {
                deleteBt.setVisibility(View.VISIBLE);
                deleteBt.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        new MaterialDialog.Builder(context)
                                .content("你确定要删除采购明细" + (finalI + 1) + "吗?")
                                .positiveText(R.string.sure)
                                .onPositive(new MaterialDialog.SingleButtonCallback() {
                                    @Override
                                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                        purchaseInfoList.remove(finalI);
                                        notifyReimburseLayout(purchaseInfoList);
                                        sumMoney(purchaseInfoList);
                                    }
                                })
                                .positiveColorRes(R.color.mainColor)
                                .negativeText(R.string.cancel)
                                .negativeColorRes(R.color.mainColor)
                                .show();
                    }
                });
            }
            buyApplyLayout.addView(itemView);
        }
    }

    private void sumMoney(List<PurchaseInfo> buyInfos) {
        totalAmount = 0;
        for (PurchaseInfo buyInfo : buyInfos) {
            String sum = buyInfo.getPrice();
            totalAmount = totalAmount + ((TextUtil.isEmpty(sum) ? 0 : Double.valueOf(sum)));
        }
        BigDecimal bigDecimal = new BigDecimal(totalAmount);
        totalSumTv.setText((bigDecimal.setScale(2, BigDecimal.ROUND_HALF_UP) + ""));
    }

    private void postReport(int status) {
        buyApplyLayout.clearFocus();
        String url = Constant.WEB_SITE + "/biz/process/purchase";
        Map<String, Object> map = new HashMap<>();

        if (!NetUtil.isNetworkConnected(context)) {
            ToastUtil.show(context, getString(R.string.no_network));
            return;
        }

        buyApplyReason = buyApplyReasonEt.getText().toString();
        if (ToastUtil.showCannotEmpty(context, buyApplyReason, "申请事由")) {
            return;
        }
        if (ToastUtil.showCannotEmpty(context, purchaseType, "采购类型")) {
            return;
        }
        if (ToastUtil.showCannotEmpty(context, deliverDate, "期望交付日期")) {
            return;
        }

        JSONArray purchaseDetail = new JSONArray();
        for (PurchaseInfo reimburseInfo : purchaseInfoList) {
            JSONObject resultObj = new JSONObject();

            try {
                String reimburseName = reimburseInfo.getTitleName();

                String productDetailNo = reimburseInfo.getProductDetailNo();

                String name = reimburseInfo.getName();
                String brand = reimburseInfo.getBrand();
                String manufacturer = reimburseInfo.getManufacturer();

                String unit = reimburseInfo.getUnit();
                String spec = reimburseInfo.getSpec();

                String num = reimburseInfo.getNumber();
                String price = reimburseInfo.getPrice();
                if (ToastUtil.showCannotEmpty(context, productDetailNo, reimburseName + "料号")) {
                    return;
                }
                if (ToastUtil.showCannotEmpty(context, num, reimburseName + "数量")) {
                    return;
                }

                if (ToastUtil.showCannotEmpty(context, price, reimburseName + "价格")) {
                    return;
                }

                resultObj.put(KeyConst.productDetailNo, productDetailNo);

                resultObj.put(KeyConst.name, name);
                resultObj.put(KeyConst.brand, brand);
                resultObj.put(KeyConst.manufacturer, manufacturer);

                resultObj.put(KeyConst.spec, spec);
                resultObj.put(KeyConst.num, num);
                resultObj.put(KeyConst.unit, unit);
                resultObj.put(KeyConst.price, price);

                purchaseDetail.put(resultObj);
            } catch (Exception e) {
                Log.d(TAG, "list解析异常:");
                return;
            }

        }
/*        if (ToastUtil.showCannotEmpty(context, payType, "支付方式")) {
            return;
        }*/

        map.put(KeyConst.reason, buyApplyReason);
        map.put(KeyConst.purchaseType, purchaseType);
        map.put(KeyConst.deliverDate, deliverDate);

        map.put(KeyConst.purchaseDetail, purchaseDetail);
        map.put(KeyConst.totalAmount, totalAmount);

        // map.put(KeyConst.payType, payType);

        map.put(KeyConst.status, status);
        map.put(KeyConst.remark, remarkEt.getText().toString());
        //附件
        if (fileData != null) {
            try {
                JSONArray attachList = new JSONArray();
                for (FileListInfo fileInfo : fileData) {
                    String fileUrl = fileInfo.fileUrl;
                    String fileName = fileInfo.fileName;
                    JSONObject fileObj = new JSONObject();
                    fileObj.put(KeyConst.name, fileName);
                    fileObj.put(KeyConst.url, fileUrl);
                    attachList.put(fileObj);
                }
                map.put(KeyConst.attachList, attachList);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        int postType = Request.Method.POST;
        if (processId > 0) {
            url = Constant.WEB_SITE + "/biz/process/draft/PURCHASE";
            map.put(KeyConst.id, processId);
            postType = Request.Method.PUT;
        }

        DialogHelper.showWaiting(fm, "加载中...");
        JSONObject jsonObject = new JSONObject(map);
        Log.d(TAG, "提交数据:" + jsonObject.toString());
        JsonObjectRequest jsonRequest = new JsonObjectRequest(postType, url,
                jsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject result) {
                        if (result != null && result.toString().contains("200")) {
                            DialogHelper.hideWaiting(fm);

                            ToastUtil.show(context, "提交成功");
                            context.finish();
                        } else {
                            ToastUtil.show(context, "提交失败");
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                DialogHelper.hideWaiting(fm);
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


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (RESULT_CODE == resultCode && data != null) {
            PoolInfo info = (PoolInfo) data.getSerializableExtra(KeyConst.OBJ_INFO);
            //选择料号后
            purchaseInfoList.get(requestCode).setProductDetailNo(info.getProductDetailNo());
            purchaseInfoList.get(requestCode).setName(info.getProductName());

            purchaseInfoList.get(requestCode).setBrand(info.getBrand());//品牌
            purchaseInfoList.get(requestCode).setManufacturer(info.getManufacturer());//厂商

            purchaseInfoList.get(requestCode).setSpec(info.getSpecification());
            purchaseInfoList.get(requestCode).setUnit(info.getUnit());

            notifyReimburseLayout(purchaseInfoList);
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

    private List<FileListInfo> fileData = new ArrayList<>();

    private void initFileView() {
        //附件
        listView = (ScrollListView) findViewById(R.id.horizontal_gridview);
        fileListAdapter = new FileListAdapter(context, fileData);
        listView.setAdapter(fileListAdapter);
        fileListAdapter.setCallBack(new FileListAdapter.DataRemoveCallBack() {
            @Override
            public void finish(List<FileListInfo> data) {
                fileData = data;
            }
        });

        findViewById(R.id.card_detail_file_layout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showChooseFileDialog();
            }
        });
    }

    //上传附件
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

    private void showChooseFileDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style
                .dialog_appcompat_theme);
        //    指定下拉列表的显示数据
        final String[] chooseFileArr = {"图片", "手机文件"};
        //    设置一个下拉的列表选择项
        builder.setItems(chooseFileArr, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
                switch (i) {
                    //文件
                    case 0:
                        choisePicture();
                        break;
                    //图片
                    case 1:
                        choiseFile();
                        break;
                }
            }
        });
        builder.show();
    }

    public void onReportCommitClick(View view) {
        postReport(2);
    }


    //保存草稿
    private void initFinishBt() {
        findViewById(R.id.left_bt).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                buyApplyReason = buyApplyReasonEt.getText().toString();
                if (processId > 0) {
                    finish();
                    return;
                }
                if (TextUtil.isEmpty(buyApplyReason) || totalAmount == 0
                        || TextUtil.isEmpty(deliverDate) || TextUtil.isEmpty(purchaseType)
                        ) {
                    finish();
                    return;
                }
                DialogUtils.getDraftBoxDialog(context).onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        context.finish();
                    }
                }).onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        postReport(1);

                    }
                }).onNegative(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        context.finish();
                    }
                }).show();


            }
        });
    }

    private void setFileListData() {
        JsonObject object = info.object;
        if (object == null || object.isJsonNull()) {
            return;
        }
        JsonArray jsonArray = object.getAsJsonArray(KeyConst.attachList);
        List<FileInfo> attList = new Gson().fromJson(jsonArray, new TypeToken<List<FileInfo>>() {
        }.getType());
        if (attList != null) {
            for (FileInfo att : attList) {
                fileData.add(new FileListInfo(att.name, att.url, att.url));
            }
        }
        fileListAdapter.setAllowDelete(true);
        fileListAdapter.setDate(fileData);
    }

    //删除草稿
    private void deleteDraftBt() {
        getTitleRightBt(getString(R.string.delete)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogUtils.getTwoBtDialog(context, "草稿删除后不可恢复,确定删除吗?")
                        .onPositive(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                String url = Constant.WEB_SITE + "/biz/process/draft/" + processId;
                                int postType = Request.Method.DELETE;

                                DialogHelper.showWaiting(fm, "加载中...");
                                JsonObjectRequest jsonRequest = new JsonObjectRequest(postType, url,
                                        new JSONObject(), new Response.Listener<JSONObject>() {
                                    @Override
                                    public void onResponse(JSONObject result) {
                                        DialogHelper.hideWaiting(fm);
                                        if (result != null && result.toString().contains("200")) {
                                            DialogHelper.hideWaiting(fm);

                                            ToastUtil.show(context, "删除成功");
                                            context.finish();
                                        } else {
                                            ToastUtil.show(context, "删除失败,稍后重试");
                                        }
                                    }
                                }, new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        DialogHelper.hideWaiting(fm);
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
}
