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
import com.android.livevideo.act_other.CommonBaseActivity;
import com.android.livevideo.act_other.PictBean;
import com.android.livevideo.bean.FileInfo;
import com.android.livevideo.bean.MsgInfo;
import com.android.livevideo.core.net.GsonRequest;
import com.android.livevideo.core.utils.TextUtil;
import com.android.livevideo.util.DialogUtils;
import com.android.livevideo.util.MoneyInputFilter;
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
import com.android.livevideo.bean.ReimburseInfo;
import com.android.livevideo.core.utils.Constant;
import com.android.livevideo.core.utils.DialogHelper;
import com.android.livevideo.core.utils.ImageUtil;
import com.android.livevideo.core.utils.KeyConst;
import com.android.livevideo.core.utils.NetUtil;
import com.android.livevideo.core.utils.RetrofitUtil;
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
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * @author Gool Lee
 * 报销申请
 */

public class Work8Activity extends CommonBaseActivity {
    private Work8Activity context;
    private int processId;
    private TextView totalSumTv;
    private ScrollListView listView;
    private FileListAdapter fileListAdapter;
    private FragmentManager fm;
    private LinearLayout reimburseLayout;
    private List<ReimburseInfo> reimburseInfoList = new ArrayList<>();
    private int TYPE;
    private double totalAmount = 0;
    private String[] reimburseTypeArr;
    private String[] tripNameArr;
    private String[] tripProcNumArr;
    private TextView connectTripTv;
    private String tripProcNum;
    private MsgInfo info;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initStatusBar();
        setContentView(R.layout.activity_work_8);
        context = this;
        fm = getSupportFragmentManager();
        processId = getIntent().getIntExtra(KeyConst.id, 0);
        TYPE = getIntent().getIntExtra(KeyConst.type, 0);

        initTitleBackBt("报销申请");
        getReimburseTypeDict();//报销类别
        reimburseLayout = findViewById(R.id.layout_item_layout_10);
        totalSumTv = findViewById(R.id.total_sum_tv);
        connectTripTv = findViewById(R.id.connect_trip_tv);

        connectTripTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getTripList(null);
            }
        });

        reimburseInfoList = new ArrayList<>();
        reimburseInfoList.add(new ReimburseInfo(0, "", ""));

        initFileView();

        initFinishBt();


        if (processId != 0) {
            getInfoData();
            deleteDraftBt();
        } else {
            notifyReimburseLayout(reimburseInfoList);
        }
        //initApprovalProcessLayout(3);
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

    private void getTripList(final String objStr) {
        if (!NetUtil.isNetworkConnected(context)) {
            return;
        }
        String url = Constant.WEB_SITE + "/biz/process/businessTrip/all";

        Response.Listener<JsonArray> successListener = new Response
                .Listener<JsonArray>() {
            @Override
            public void onResponse(JsonArray result) {
                if (result != null && result.size() > 0) {
                    int size = result.size();
                    tripNameArr = new String[size];
                    tripProcNumArr = new String[size];
                    for (int i = 0; i < size; i++) {
                        JsonObject object = result.get(i).getAsJsonObject();
                        tripNameArr[i] = object.get(KeyConst.procNum).getAsString();
                        tripProcNumArr[i] = object.get(KeyConst.procNum).getAsString();
                    }

                    if (objStr == null) {
                        new MaterialDialog.Builder(context)
                                .items(tripNameArr)// 列表数据
                                .itemsCallback(new MaterialDialog.ListCallback() {
                                    @Override
                                    public void onSelection(MaterialDialog dialog, View itemView,
                                                            int position, CharSequence text) {
                                        connectTripTv.setText(text);
                                        tripProcNum = tripProcNumArr[position];
                                    }
                                })
                                .show();
                    } else {
                        int arrIndex = TextUtil.getArrIndex(tripProcNumArr, objStr);
                        tripProcNum = tripProcNumArr[arrIndex];
                        connectTripTv.setText(tripNameArr[TextUtil.getArrIndex(tripProcNumArr, objStr)]);
                    }
                } else {
                    if (objStr == null) {
                        new MaterialDialog.Builder(context)
                                .content("无")
                                .show();
                    }
                }
            }
        };

        Request<JsonArray> versionRequest = new
                GsonRequest<JsonArray>(Request.Method.GET, url,
                        successListener, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
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

    //报销类别
    private void getReimburseTypeDict() {
        if (!NetUtil.isNetworkConnected(context)) {
            return;
        }
        String url = Constant.WEB_SITE + "/dict/dicts/cached/REIMBURSE_TYPE";

        Response.Listener<JsonArray> successListener = new Response
                .Listener<JsonArray>() {
            @Override
            public void onResponse(JsonArray result) {
                if (result != null) {
                    reimburseTypeArr = new String[result.size()];
                    for (int i = 0; i < result.size(); i++) {
                        JsonObject object = result.get(i).getAsJsonObject();
                        reimburseTypeArr[i] = object.get(KeyConst.name).getAsString();
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

    //添加行程
    public void onWorkAddClick11(View view) {
        reimburseInfoList.add(new ReimburseInfo(reimburseInfoList.size() + 1, "",
                ""));
        notifyReimburseLayout(reimburseInfoList);
    }

    //明细
    private void notifyReimburseLayout(List<ReimburseInfo> planInfos) {
        if (reimburseLayout == null) {
            return;
        }
        reimburseLayout.removeAllViews();
        final int size = planInfos.size();
        for (int i = 0; i < size; i++) {
            final int finalI = i;
            View itemView = View.inflate(context, R.layout.item_work_add_9, null);
            final TextView deleteBt = (TextView) itemView.findViewById(R.id.item_9_delete_bt);
            TextView titleTv = (TextView) itemView.findViewById(R.id.item_9_title_tv);
            final EditText sumEt = (EditText) itemView.findViewById(R.id.work_reimburse_sum_et);
            InputFilter[] moneyFilters = {new MoneyInputFilter()};
            sumEt.setFilters(moneyFilters);
            final TextView typeTv = itemView.findViewById(R.id.work_reimburse_type_tv);
            final EditText detailEt = (EditText) itemView.findViewById(R.id.work_9_detail_et);
            ReimburseInfo info = planInfos.get(i);
            final String titlePotionStr = "报销明细(" + (i + 1) + ")";
            titleTv.setText(titlePotionStr);
            reimburseInfoList.get(i).setTitleName(titlePotionStr);
            String sum = info.getAmount();


            sumEt.setText(sum);
            typeTv.setText(info.getType());
            detailEt.setText(info.getRemark());
            sumEt.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                }

                @Override
                public void afterTextChanged(Editable editable) {
                    if (finalI < reimburseInfoList.size()) {
                        reimburseInfoList.get(finalI).setAmount(sumEt.getText().toString());
                    }
                    sumMoney(reimburseInfoList);
                }
            });
            sumEt.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View view, boolean isfocuce) {
                    if (!isfocuce && finalI < reimburseInfoList.size()) {
                        reimburseInfoList.get(finalI).setAmount(sumEt.getText().toString());
                    }
                }
            });
            typeTv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (reimburseTypeArr == null || reimburseTypeArr.length == 0) {
                        getReimburseTypeDict();
                        return;
                    }
                    new MaterialDialog.Builder(context)
                            .items(reimburseTypeArr)// 列表数据
                            .itemsCallback(new MaterialDialog.ListCallback() {
                                @Override
                                public void onSelection(MaterialDialog dialog, View itemView,
                                                        int position, CharSequence text) {
                                    typeTv.setText(text);
                                    if (finalI < reimburseInfoList.size()) {
                                        reimburseInfoList.get(finalI).setType(text.toString());
                                    }
                                }
                            })
                            .show();

                }
            });
            detailEt.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View view, boolean isfocuce) {
                    if (!isfocuce & finalI < reimburseInfoList.size()) {
                        reimburseInfoList.get(finalI).setRemark(detailEt.getText().toString());
                    }
                }
            });
            //删除按钮
            if (size > 1) {
                deleteBt.setVisibility(View.VISIBLE);
                deleteBt.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        new MaterialDialog.Builder(context)
                                .content("你确定要删除报销明细" + (finalI + 1) + "吗?")
                                .positiveText(R.string.sure)
                                .onPositive(new MaterialDialog.SingleButtonCallback() {
                                    @Override
                                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                        reimburseInfoList.remove(finalI);
                                        notifyReimburseLayout(reimburseInfoList);
                                        sumMoney(reimburseInfoList);
                                    }
                                })
                                .positiveColorRes(R.color.mainColor)
                                .negativeText(R.string.cancel)
                                .negativeColorRes(R.color.mainColor)
                                .show();
                    }
                });
            }
            reimburseLayout.addView(itemView);
        }
    }

    private void sumMoney(List<ReimburseInfo> reimburseInfoList) {
        totalAmount = 0;
        for (ReimburseInfo reimburseInfo : reimburseInfoList) {
            String sum = reimburseInfo.getAmount();
            totalAmount = totalAmount + ((TextUtil.isEmpty(sum) ? 0 : Double.valueOf(sum)));
        }
        BigDecimal bigDecimal = new BigDecimal(totalAmount);
        totalSumTv.setText((bigDecimal.setScale(2, BigDecimal.ROUND_HALF_UP) + "").replace(".00", ""));
    }


    //草稿箱进来
    private void setDraftView() {
        JsonObject infoObj = info.object;
        if (null == infoObj || infoObj.isJsonNull()) {
            return;
        }
        //总金额
        String totalSum = Utils.getObjStr(infoObj, KeyConst.totalAmount);
        totalSumTv.setText(totalSum);
        String objStr = Utils.getObjStr(infoObj, KeyConst.businessTripNum);
        if (tripProcNumArr == null || tripProcNumArr.length == 0) {
            getTripList(objStr);
        } else {
            int arrIndex = TextUtil.getArrIndex(tripProcNumArr, objStr);
            tripProcNum = tripProcNumArr[arrIndex];
            connectTripTv.setText(tripNameArr[TextUtil.getArrIndex(tripProcNumArr, objStr)]);
        }

        JsonElement objArr = infoObj.get(KeyConst.reimburseDetailList);
        if (objArr == null || objArr.isJsonNull()) {
            return;
        }
        JsonArray tripArr = infoObj.getAsJsonArray(KeyConst.reimburseDetailList);
        if (tripArr == null || tripArr.size() == 0 || tripArr.isJsonNull()) {
            return;
        }
        reimburseInfoList = new Gson().fromJson(tripArr, new TypeToken<List<ReimburseInfo>>() {
        }.getType());

        notifyReimburseLayout(reimburseInfoList);
        sumMoney(reimburseInfoList);
    }

    private void postReport(int status) {
        reimburseLayout.clearFocus();
        String url = Constant.WEB_SITE + "/biz/process/reimburse";
        Map<String, Object> map = new HashMap<>();


        if (!NetUtil.isNetworkConnected(context)) {
            ToastUtil.show(context, getString(R.string.no_network));
            return;
        }

        JSONArray reimburseDetailArr = new JSONArray();
        for (ReimburseInfo reimburseInfo : reimburseInfoList) {
            JSONObject resultObj = new JSONObject();

            try {
                String reimburseName = reimburseInfo.getTitleName();
                String amount = reimburseInfo.getAmount();
                String type = reimburseInfo.getType();
                String detailRemark = reimburseInfo.getRemark();//费用明细描述

                if (TextUtil.isEmpty(amount)) {
                    ToastUtil.show(context, reimburseName + "报销金额不能为空");
                    return;
                }
                if (TextUtil.isEmpty(type)) {
                    ToastUtil.show(context, reimburseName + "报销类别不能为空");
                    return;
                }
                resultObj.put(KeyConst.amount, amount);
                resultObj.put(KeyConst.type, type);
                resultObj.put(KeyConst.remark, detailRemark);

                reimburseDetailArr.put(resultObj);
            } catch (Exception e) {
                Log.d(TAG, "出差list解析异常:");
                return;
            }

        }

        map.put(KeyConst.reimburseDetailList, reimburseDetailArr);

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

        map.put(KeyConst.totalAmount, totalAmount);
        map.put(KeyConst.status, status);
        if (!TextUtil.isEmpty(tripProcNum)) {
            map.put(KeyConst.businessTripNum, tripProcNum);//关联出差id
        }
        int postType = Request.Method.POST;
        if (processId > 0) {
            url = Constant.WEB_SITE + "/biz/process/draft/REIMBURSE";
            map.put(KeyConst.id, processId);
            postType = Request.Method.PUT;
        }
        DialogHelper.showWaiting(fm, "加载中...");
        JSONObject jsonObject = new JSONObject(map);
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
                                    if (200 == code && responseUrl != null) {
                                        final String finalResponseUrl = responseUrl;
                                        context.runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                fileData.add(new FileListInfo(
                                                        file.getName(), finalResponseUrl, file.length(), finalResponseUrl));
                                                fileListAdapter.setDate(fileData);
                                                ImageUtil.reSetLVHeight(context, listView);
                                                DialogHelper.hideWaiting(getSupportFragmentManager());
                                            }
                                        });
                                    } else {
                                        DialogHelper.hideWaiting(getSupportFragmentManager());
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
        //保存
        postReport(2);
    }

    public void onFellowPeopleClick(View view) {
        ToastUtil.show(context, "同行人员");
    }

    //保存草稿
    private void initFinishBt() {
        findViewById(R.id.left_bt).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (processId > 0) {
                    finish();
                    return;
                }
                if (totalAmount == 0 && TextUtil.isEmpty(tripProcNum)) {
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
