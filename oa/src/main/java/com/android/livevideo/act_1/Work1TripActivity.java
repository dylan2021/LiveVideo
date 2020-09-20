package com.android.livevideo.act_1;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.android.livevideo.act_other.CommonBaseActivity;
import com.android.livevideo.act_other.PictBean;
import com.android.livevideo.bean.EmployeeInfo;
import com.android.livevideo.bean.FileInfo;
import com.android.livevideo.bean.MsgInfo;
import com.android.livevideo.bean.TripItemInfo;
import com.android.livevideo.core.net.GsonRequest;
import com.android.livevideo.core.utils.TextUtil;
import com.android.livevideo.util.DialogUtils;
import com.android.livevideo.util.TimeUtils;
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
import com.jzxiang.pickerview.TimePickerDialog;
import com.jzxiang.pickerview.data.Type;
import com.jzxiang.pickerview.listener.OnDateSetListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Gool Lee   出差申请
 */
public class Work1TripActivity extends CommonBaseActivity {
    private Work1TripActivity context;
    private int processId;
    private TextView fellowPeopleTv, totalDaysTv;
    private ScrollListView listView;
    private FileListAdapter fileListAdapter;
    private FragmentManager fm;
    private LinearLayout travelsLayout;
    private List<TripItemInfo> tripList = new ArrayList<>();
    private int TYPE;
    private EditText remarkEt, resonEt;
    private double period = 0;
    private String remark;
    private String reson;
    private MsgInfo info;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initStatusBar();
        setContentView(R.layout.activity_work_1);
        context = this;
        fm = getSupportFragmentManager();
        processId = getIntent().getIntExtra(KeyConst.id, 0);


        initTitleBackBt("出差申请");

        TYPE = getIntent().getIntExtra(KeyConst.type, 0);
        resonEt = findViewById(R.id.go_out_reason_et);
        fellowPeopleTv = findViewById(R.id.fellow_people_tv);
        travelsLayout = findViewById(R.id.layout_item_layout_1);
        remarkEt = findViewById(R.id.work_remark_tv);
        totalDaysTv = findViewById(R.id.total_days_tv);
        tripList = new ArrayList<>();
        tripList.add(new TripItemInfo(0, vehicleArr[0], typeArr[1], 0, 0));

        initFileView();

        initFinishBt();

        if (processId != 0) {
            getInfoData();

            deleteDraftBt();
        } else {
            notifyTravelsLayout(tripList);
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

    //草稿箱进来
    private void setDraftView() {
        JsonObject infoObj = info.object;
        if (null == infoObj || infoObj.isJsonNull()) {
            return;
        }
        resonEt.setText(Utils.getObjStr(infoObj, KeyConst.reason));
        remarkEt.setText(Utils.getObjStr(infoObj, KeyConst.remark));
        totalDaysTv.setText(Utils.getObjStr(infoObj, KeyConst.period));

        fellowPeopleTv.setText(Utils.getObjStr(infoObj, KeyConst.peers));

        //同行人员idArr
        JsonElement objArr1 = infoObj.get(KeyConst.employeeIdList);
        if (objArr1 != null && !objArr1.isJsonNull()) {
            employeeIdArr = infoObj.getAsJsonArray(KeyConst.employeeIdList);
        }

        JsonElement objArr = infoObj.get(KeyConst.tripList);
        if (objArr == null || objArr.isJsonNull()) {
            return;
        }
        JsonArray tripArr = infoObj.getAsJsonArray(KeyConst.tripList);

        if (tripArr == null || tripArr.size() == 0 || tripArr.isJsonNull()) {
            return;
        }
        tripList = new Gson().fromJson(tripArr, new TypeToken<List<TripItemInfo>>() {
        }.getType());


        notifyTravelsLayout(tripList);

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

    //添加行程
    public void onTravelAddClick(View view) {
        tripList.add(new TripItemInfo(tripList.size() + 1,
                vehicleArr[0], typeArr[1], 0, 0));
        notifyTravelsLayout(tripList);
    }

    //出差行程
    private void notifyTravelsLayout(List<TripItemInfo> planInfos) {
        period = 0;
        travelsLayout.removeAllViews();
        if (planInfos == null || planInfos.size() == 0) {
            return;
        }
        final int size = planInfos.size();
        for (int i = 0; i < size; i++) {
            View itemView = View.inflate(context, R.layout.item_work_add_1, null);
            TextView titleTv = (TextView) itemView.findViewById(R.id.work_item_add_title_tv);
            final TextView startTimeTv = (TextView) itemView.findViewById(R.id.work_start_time_tv);
            final TextView startCity = (TextView) itemView.findViewById(R.id.start_city_tv);
            final TextView endCity = (TextView) itemView.findViewById(R.id.end_city_tv);
            final TextView endTimeTv = (TextView) itemView.findViewById(R.id.work_end_time_tv);
            TripItemInfo info = planInfos.get(i);
            if (info == null) {
                return;
            }
            final String titlePotionStr = "行程(" + (i + 1) + ")";
            tripList.get(i).setTitleName(titlePotionStr);
            titleTv.setText(titlePotionStr);

            //出发/目的城市
            startCity.setText(info.getDeparture());
            endCity.setText(info.getDestination());

            String startTimeStr = info.getStartTime();
            startTimeTv.setText(TextUtil.subTimeYmdHm(startTimeStr));
            String endTimeStr = info.getEndTime();
            endTimeTv.setText(TextUtil.subTimeYmdHm(endTimeStr));


            //开始-结束时间
            long startTime = info.getStartTimeL();
            long endTime = info.getEndTimeL();
            if (processId > 0) {
                try {
                    if (!TextUtil.isEmpty(startTimeStr)) {
                        startTime = TimeUtils.getFormatYmdHms().parse(startTimeStr).getTime();
                        tripList.get(i).setStartTimeL(startTime);
                    }
                    if (!TextUtil.isEmpty(endTimeStr)) {
                        endTime = TimeUtils.getFormatYmdHms().parse(endTimeStr).getTime();
                        tripList.get(i).setEndTimeL(endTime);
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }

            //单个天数
            final EditText hourEt = itemView.findViewById(R.id.day_duration_tv);

            double day = info.getPeriod();
            hourEt.setText(TextUtil.remove_0(day + ""));
            if ((startTime == 0 || endTime == 0) && day == 0) {
                hourEt.setText("");
            }
            period = period + day;

            final TextView deleteBt = (TextView) itemView.findViewById(R.id.go_out_delete_bt);


            final RadioGroup transportationRg = (RadioGroup) itemView.findViewById(R.id.transportation_rg);
            final RadioGroup singleReturnRg = (RadioGroup) itemView.findViewById(R.id.single_return_rg);

            transportationRg.check(transportationRg.
                    getChildAt(TextUtil.getArrIndex(vehicleArr, info.getVehicle())).getId());
            singleReturnRg.check(singleReturnRg.getChildAt(TextUtil.getArrIndex(typeArr, info.getType())).getId());

            final int finalI = i;
            transportationRg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup radioGroup, int i) {
                    int position = radioGroup.indexOfChild(radioGroup.findViewById(i));
                    if (finalI < tripList.size()) {
                        tripList.get(finalI).setVehicle(vehicleArr[position]);
                    }
                }
            });
            singleReturnRg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup radioGroup, int i) {
                    if (finalI < tripList.size()) {
                        int position = radioGroup.indexOfChild(radioGroup.findViewById(i));
                        tripList.get(finalI).setType(typeArr[position]);
                    }
                }
            });

            startCity.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                   /* CityPicker.from(context).setOnPickListener(new OnPickListener() {
                        @Override
                        public void onPick(int position, City city) {
                            startCity.setText(city.getName());
                            if (finalI < tripList.size()) {
                                tripList.get(finalI).setDeparture(city.getName());
                            }
                        }

                        @Override
                        public void onCancel() {
                        }
                    }).show();*/
                }
            });

            endCity.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                }
            });
            hourEt.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                }

                @Override
                public void onTextChanged(CharSequence str, int i, int i1, int i2) {
                    if (finalI < tripList.size()) {
                        if (TextUtil.setInput1Dot(hourEt, str)) {
                            return;
                        }
                        tripList.get(finalI).setPeriod(TextUtil.convertToDouble(str.toString(), 0));
                        period = 0;
                        for (TripItemInfo itemInfo : tripList) {
                            period = period + itemInfo.getPeriod();
                        }
                        totalDaysTv.setText(TextUtil.remove_0((period + "")));
                    }
                }

                @Override
                public void afterTextChanged(Editable editable) {

                }
            });

            startTimeTv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showTimeSet(0, finalI);
                }
            });
            endTimeTv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showTimeSet(1, finalI);
                }
            });

            //删除按钮
            if (size > 1) {
                deleteBt.setVisibility(View.VISIBLE);
                deleteBt.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        new MaterialDialog.Builder(context)
                                .content("你确定要删除行程" + (finalI + 1) + "吗?")
                                .positiveText(R.string.sure)
                                .onPositive(new MaterialDialog.SingleButtonCallback() {
                                    @Override
                                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                        tripList.remove(finalI);
                                        notifyTravelsLayout(tripList);
                                    }
                                })
                                .positiveColorRes(R.color.mainColor)
                                .negativeText(R.string.cancel)
                                .negativeColorRes(R.color.mainColor)
                                .show();
                    }
                });
            }
            travelsLayout.addView(itemView);
        }

        totalDaysTv.setText(TextUtil.remove_0((period + "")));
    }

    private void showTimeSet(final int type, final int index) {
        TimePickerDialog mDialogAll = new TimePickerDialog.Builder()
                .setCallBack(new OnDateSetListener() {
                    @Override
                    public void onDateSet(TimePickerDialog timePickerView, long millseconds) {
                        long startTime = tripList.get(index).getStartTimeL();
                        long endTime = tripList.get(index).getEndTimeL();
                        if (type == 0) {
                            if (endTime != 0 && millseconds >= endTime) {
                                ToastUtil.show(context, "开始时间要早于结束时间");
                                return;
                            }
                        } else {
                            if (millseconds <= startTime) {
                                ToastUtil.show(context, "结束时间要晚于开始时间");
                                return;
                            }
                        }
                        //时间冲突检测
                 /*       if (tripList.size() > 1) {
                            for (TripItemInfo goOutInfo : tripList) {
                                long startTime1 = goOutInfo.getStartTimeL();
                                long endTime1 = goOutInfo.getEndTimeL();
                                if (millseconds >= startTime1 && millseconds <= endTime1) {
                                    int id = goOutInfo.getId() + 1;
                                    ToastUtil.show(context,
                                            "该时间与行程" + id + "冲突");
                                    return;
                                }
                            }
                        }*/

                        if (type == 0) {
                            tripList.get(index).setStartTimeL(millseconds);
                            tripList.get(index).setStartTime(TimeUtils.getTimeYmdHms(millseconds));
                        } else {
                            tripList.get(index).setEndTimeL(millseconds);
                            tripList.get(index).setEndTime(TimeUtils.getTimeYmdHms(millseconds));
                        }
                        double day = TimeUtils.getTripInfoDay(tripList.get(index).getStartTimeL()
                                , tripList.get(index).getEndTimeL());
                        tripList.get(index).setPeriod(day);
                        notifyTravelsLayout(tripList);
                    }
                })
                .setTitleStringId("")//标题
                .setCyclic(false)
                .setCancelStringId(getString(R.string.time_dialog_title_cancel))
                .setSureStringId(getString(R.string.time_dialog_title_sure))
                .setWheelItemTextSelectorColorId(context.getResources().getColor(R.color.mainColor))
                .setWheelItemTextNormalColorId(context.getResources().getColor(R.color.time_nomal_text_color))
                .setThemeColor(context.getResources().getColor(R.color.mainColorDrak))
                .setWheelItemTextSize(16)
                .setType(Type.MONTH_DAY_HOUR_MIN)
                .build();
        mDialogAll.show(context.getSupportFragmentManager(), "");
    }

    private String vehicleArr[] = {"飞机", "火车", "汽车", "其他"};
    private String typeArr[] = {"单程", "往返"};

    JsonArray employeeIdArr = new JsonArray();

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

            fellowPeopleTv.setText(nameStr);
            employeeIdArr = choosedEmployeeIds;
        } else {
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

    private List<EmployeeInfo> parentList = new ArrayList<>();

    //同行人员
    public void onFellowPeopleClick(View view) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(KeyConst.OBJ_INFO, (Serializable) parentList);//序列化,要注意转化(Serializable)
        Intent intent = new Intent(context, ChooseEmplyeeActivity.class);
        intent.putExtras(bundle);
        context.startActivityForResult(intent, 1);
    }
    //保存草稿
    private void initFinishBt() {
        findViewById(R.id.left_bt).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                reson = resonEt.getText().toString();
                String totalPeriod = totalDaysTv.getText().toString();
                if (processId > 0) {
                    finish();
                    return;
                }
                if (TextUtil.isEmpty(reson) && "0".equals(totalPeriod)) {
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
                        //存到服务器
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

    /*
    提交申请
     */
    private void postReport(int status) {// 1 保存草稿  2 提交
        if (!NetUtil.isNetworkConnected(context)) {
            ToastUtil.show(context, getString(R.string.no_network));
            return;
        }
        reson = resonEt.getText().toString();
        remark = remarkEt.getText().toString();
        if (TextUtil.isEmpty(reson)) {
            ToastUtil.show(context, "请填写出差事由");
            return;
        }

        Map<String, Object> map = new HashMap<>();
        map.put(KeyConst.status, status);
        map.put(KeyConst.reason, reson);
        map.put(KeyConst.remark, remark);
        map.put(KeyConst.period, period);
        if (employeeIdArr != null && employeeIdArr.size() >= 0 && !employeeIdArr.isJsonNull()) {

            JSONArray employeeIdList = new JSONArray();
            for (JsonElement element : employeeIdArr) {
                employeeIdList.put(element.getAsInt());
            }
            map.put(KeyConst.employeeIdList, employeeIdList);
        }

        JSONArray tripListArr = new JSONArray();
        for (TripItemInfo tripInfo : tripList) {
            JSONObject resultObj = new JSONObject();

            try {
                String tripName = tripInfo.getTitleName();
                String vehicle = tripInfo.getVehicle();
                long startTime = tripInfo.getStartTimeL();
                long endTime = tripInfo.getEndTimeL();

                String type = tripInfo.getType();

                String departure = tripInfo.getDeparture();//出发地
                String destination = tripInfo.getDestination();//目的地
                if (TextUtil.isEmpty(departure)) {
                    ToastUtil.show(context, tripName + "出发城市不能为空");
                    return;
                }
                if (TextUtil.isEmpty(destination)) {
                    ToastUtil.show(context, tripName + "目的城市不能为空");
                    return;
                }
                if (0 == startTime) {
                    ToastUtil.show(context, tripName + "开始时间不能为空");
                    return;
                }
                if (0 == endTime) {
                    ToastUtil.show(context, tripName + "结束时间不能为空");
                    return;
                }
                resultObj.put(KeyConst.vehicle, vehicle);
                resultObj.put(KeyConst.type, type);
                resultObj.put(KeyConst.departure, departure);
                resultObj.put(KeyConst.destination, destination);
                resultObj.put(KeyConst.period, tripInfo.getPeriod());

                resultObj.put(KeyConst.startTime, TimeUtils.getTimeYmdHm(startTime) + ":00");
                resultObj.put(KeyConst.endTime, TimeUtils.getTimeYmdHm(endTime) + ":00");

                tripListArr.put(resultObj);
            } catch (Exception e) {
                Log.d(TAG, "出差list解析异常:");
                return;
            }
        }

        map.put(KeyConst.tripList, tripListArr);
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

        String url = Constant.WEB_SITE + "/biz/process/businessTrip";
        int postType = Request.Method.POST;
        if (processId > 0) {
            url = Constant.WEB_SITE + "/biz/process/draft/BUSINESS_TRIP";
            map.put(KeyConst.id, processId);
            postType = Request.Method.PUT;
        }

        JSONObject jsonObject = new JSONObject(map);
        Log.d(TAG, "出差申请:" + jsonObject.toString());

        DialogHelper.showWaiting(fm, "加载中...");
        JsonObjectRequest jsonRequest = new JsonObjectRequest(postType, url,
                jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject result) {
                DialogHelper.hideWaiting(fm);
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
