package com.android.livevideo.act_1;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.GravityEnum;
import com.afollestad.materialdialogs.MaterialDialog;
import com.android.livevideo.act_2.AddressListActivity;
import com.android.livevideo.act_other.CommonBaseActivity;
import com.android.livevideo.act_other.PictBean;
import com.android.livevideo.bean.ContractInfo;
import com.android.livevideo.bean.DeptInfo;
import com.android.livevideo.bean.EmployeeInfo;
import com.android.livevideo.bean.FileInfo;
import com.android.livevideo.bean.MsgInfo;
import com.android.livevideo.bean.TripItemInfo;
import com.android.livevideo.core.net.GsonRequest;
import com.android.livevideo.core.utils.TextUtil;
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
import com.android.livevideo.bean.GroupInfo;
import com.android.livevideo.core.utils.Constant;
import com.android.livevideo.core.utils.DialogHelper;
import com.android.livevideo.core.utils.ImageUtil;
import com.android.livevideo.core.utils.KeyConst;
import com.android.livevideo.core.utils.NetUtil;
import com.android.livevideo.core.utils.RetrofitUtil;
import com.android.livevideo.util.TimeUtils;
import com.android.livevideo.util.DialogUtils;
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
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Gool Lee
 */
public class Work124567911_12Activity extends CommonBaseActivity {
    private Work124567911_12Activity context;
    private TextView remarkTv;
    private FragmentManager fm;
    private List<GroupInfo.ChildrenBean> childrenInfoList;
    private ScrollListView listView;
    private FileListAdapter fileListAdapter;
    private int processId, TYPE, breakOff = -1;
    private TextView topTypeTitleTv, topTypeValueTv;
    private String titleStr[] = {"请假申请", "", "加班申请", "外出申请", "转正申请", "招聘申请",
            "离职申请", "公文审批", "", "", "报销申请", "备用金申请", "付款申请"};
    private String remark;
    private String topTypeTitleValue;
    private EditText hourTv;
    private TextView startTimeValueTv;
    private TextView endTimeValueTv;
    private List<DeptInfo> deptInfos;
    private Dialog emplyeeChoosedDialog;
    private EmployeeInfo employeeInfo;
    private String employeeChoosedName;
    private int employeeChoosedId, employeeChoosedId1, employeeChoosedId2, employeeChoosedId3;
    private TextView item0ValueTv;
    private TextView item1ValueTv;
    private TextView item2ValueTv;
    private String emplyeeChooeedDeptName;
    private List<String> payTypeArr = new ArrayList();
    private TextView nextAuditValueTv1, nextAuditValueTv2, nextAuditValueTv3;
    private String leaveTypeValue;
    private MsgInfo info;
    private EditText payObjectTv;
    private EditText bankNameTv;
    private EditText accountTv;
    private List<EmployeeInfo> parentList = new ArrayList<>();
    private LinearLayout leaveOverTimeLayout;
    private LinearLayout leaveOvertimeTitleLayout;
    private double period;
    private String typeStr = "";
    private JSONArray overtimeDateModelList = new JSONArray();
    private String breakOffAmount;
    private String annualLeaveAmount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initStatusBar();
        setContentView(R.layout.activity_work_023456_10_11);
        fm = getSupportFragmentManager();


        context = this;
        processId = getIntent().getIntExtra(KeyConst.id, 0);
        TYPE = getIntent().getIntExtra(KeyConst.type, 0);
        initTitleBackBt(titleStr[TYPE]);

        initView();

        initFileView();
        //initApprovalProcessLayout(3);

        initFinishBt();
        //草稿箱进来
        if (processId > 0) {
            getInfoData();
            deleteDraftBt();
        } else {
            if (TYPE == 0 || TYPE == 2) {//请假
                notifyLeavelsLayout(leaveList);
            }
        }
    }

    private void notifyLeavelsLayout(List<TripItemInfo> infos) {
        period = 0;
        leaveOverTimeLayout.removeAllViews();
        if (infos == null || infos.size() == 0) {
            return;
        }
        final int size = infos.size();
        for (int i = 0; i < size; i++) {
            View itemView = View.inflate(context, R.layout.item_work_add_leave, null);
            TextView titleTv = (TextView) itemView.findViewById(R.id.work_item_add_title_tv);
            final TextView startTimeTv = (TextView) itemView.findViewById(R.id.work_start_time_tv);
            final TextView endTimeTv = (TextView) itemView.findViewById(R.id.work_end_time_tv);
            TripItemInfo info = infos.get(i);
            if (info == null) {
                return;
            }
            final String titlePotionStr = typeStr + "(" + (i + 1) + ")";
            leaveList.get(i).setTitleName(titlePotionStr);
            titleTv.setText(titlePotionStr);


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
                        leaveList.get(i).setStartTimeL(startTime);
                    }
                    if (!TextUtil.isEmpty(endTimeStr)) {
                        endTime = TimeUtils.getFormatYmdHms().parse(endTimeStr).getTime();
                        leaveList.get(i).setEndTimeL(endTime);
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }

            //单个天数
            final EditText houtEt = itemView.findViewById(R.id.day_duration_tv);
            double hour = info.getPeriod();
            houtEt.setText(TextUtil.remove_0(hour + ""));
            if ((startTime == 0 || endTime == 0) && hour == 0) {
                houtEt.setText("");
            }
            period = period + hour;

            final TextView deleteBt = (TextView) itemView.findViewById(R.id.go_out_delete_bt);


            final int finalI = i;

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
            houtEt.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                }

                @Override
                public void onTextChanged(CharSequence s, int i, int i1, int i2) {
                    if (TextUtil.setInput1Dot(houtEt, s)) {
                        return;
                    }
                    double period = TextUtil.convertToDouble(s.toString(), 0);

                    if (period > hour) {
                        houtEt.setText(TextUtil.remove_0(hour + ""));
                        DialogUtils.showTipDialog(context, getString(R.string.input_time_tip));
                        return;
                    }
                    if (finalI < leaveList.size()) {
                        leaveList.get(finalI).setPeriod(period);
                        Work124567911_12Activity.this.period = 0;
                        for (TripItemInfo itemInfo : leaveList) {
                            Work124567911_12Activity.this.period = Work124567911_12Activity.this.period + itemInfo.getPeriod();
                        }
                        hourTv.setText(TextUtil.remove_0((Work124567911_12Activity.this.period + "")));
                    }
                }

                @Override
                public void afterTextChanged(Editable editable) {

                }
            });

            //删除按钮
            if (size > 1) {
                deleteBt.setVisibility(View.VISIBLE);
                deleteBt.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        new MaterialDialog.Builder(context)
                                .content("确定要删除" + typeStr + (finalI + 1) + "吗?")
                                .positiveText(R.string.sure)
                                .onPositive(new MaterialDialog.SingleButtonCallback() {
                                    @Override
                                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                        leaveList.remove(finalI);
                                        notifyLeavelsLayout(leaveList);
                                    }
                                })
                                .positiveColorRes(R.color.mainColor)
                                .negativeText(R.string.cancel)
                                .negativeColorRes(R.color.mainColor)
                                .show();
                    }
                });
            }
            leaveOverTimeLayout.addView(itemView);
        }

        hourTv.setText(TextUtil.remove_0((period + "")));
    }

    private void showTimeSet(final int type, final int index) {
        TimePickerDialog mDialogAll = new TimePickerDialog.Builder()
                .setCallBack(new OnDateSetListener() {
                    @Override
                    public void onDateSet(TimePickerDialog timePickerView, long millseconds) {
                        long startTime = leaveList.get(index).getStartTimeL();
                        long endTime = leaveList.get(index).getEndTimeL();
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

                        if (type == 0) {
                            leaveList.get(index).setStartTimeL(millseconds);
                            leaveList.get(index).setStartTime(TimeUtils.getTimeYmdHms(millseconds));
                        } else {
                            leaveList.get(index).setEndTimeL(millseconds);
                            leaveList.get(index).setEndTime(TimeUtils.getTimeYmdHms(millseconds));
                        }
                        double hours;
                        if (TYPE == 0) {//请假
                            hours = TimeUtils.getAttendTotalPeroid(leaveList.get(index).getStartTimeL()
                                    , leaveList.get(index).getEndTimeL());
                        } else {//加班
                            //去除午休
                            hours = TimeUtils.getAttendHourOvertime(leaveList.get(index).getStartTimeL(), leaveList.get(index).getEndTimeL());
                            // 不去午休
                            //hours = TimeUtils.getDiffHours8( leaveList.get(index).getStartTimeL(),leaveList.get(index).getEndTimeL());
                        }
                        leaveList.get(index).setPeriod(hours);

                        notifyLeavelsLayout(leaveList);
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

    private List<TripItemInfo> leaveList = new ArrayList<>();
    private List<FileListInfo> fileData = new ArrayList<>();

    private List<ContractInfo> typeList = new ArrayList<>();


    private void seleteType() {
        int size = typeList.size();
        String[] array = new String[size];
        for (int i = 0; i < size; i++) {
            array[i] = typeList.get(i).getName();
        }
        showSeletedDialog(array, topTypeValueTv, 0);
    }

    public void showSeletedDialog(final String[] arr, final TextView titleTv, final int type) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        // 设置了标题就不要设置builder.setMessage()了，否则列表不起作用。
        builder.setItems(arr, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
                dialog.dismiss();

                titleTv.setText(arr[i]);
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
        alertDialog.getWindow().setLayout(LinearLayout.LayoutParams.WRAP_CONTENT, 1000);

    }

    private long startTime;
    private long endTime;

    private void showPickeTimeDilog(final TextView timeBt, final int type) {
        final boolean isYMD = (TYPE > 3 && TYPE < 7) || TYPE == 11 || TYPE == 12;
        TimePickerDialog mDialogAll = new TimePickerDialog.Builder()
                .setCallBack(new OnDateSetListener() {
                    @Override
                    public void onDateSet(TimePickerDialog timePickerView, long millseconds) {
                        if (type == 1) {
                          /*  if (millseconds > System.currentTimeMillis()) {
                                ToastUtil.show(context, "开始时间不能大于现在");
                                return;
                            }*/

                            if (endTime != 0 && millseconds > endTime) {
                                if (TYPE == 11) {
                                    ToastUtil.show(context, "使用日期不能大于归还时间");
                                } else {
                                    ToastUtil.show(context, "开始时间不能大于结束时间");
                                }
                                return;
                            }
                            startTime = millseconds;

                        } else {
                            if (type == 0) {
                                if (millseconds > System.currentTimeMillis()) {
                                    ToastUtil.show(context, "补卡时间不能大于现在");
                                    return;
                                }
                            } else {
                                if (millseconds < startTime) {
                                    if (TYPE == 11) {
                                        ToastUtil.show(context, "归还日期不能小于使用时间");
                                    } else {
                                        ToastUtil.show(context, "结束时间不能小于开始时间");
                                    }
                                    return;
                                }
                            }
                            endTime = millseconds;//最大时间/补卡时间
                        }
                        String timeStr = isYMD ? TimeUtils.getTimeYmd(millseconds) :
                                TimeUtils.getTimeYmdHm(millseconds);
                        timeBt.setText(timeStr);
                        if (TYPE == 2) {//加班
                            if (startTime != 0 && endTime != 0) {
                                setHourSum(startTime, endTime);
                            }
                        } else {
                            hourTv.setText(TimeUtils.betweenHours(startTime, endTime) + "");
                        }
                    }
                })
                .setCancelStringId("取消")
                .setSureStringId("确定")
                .setTitleStringId("")//标题
                .setCyclic(false)
                .setThemeColor(context.getResources().getColor(R.color.mainColor))
                .setType(isYMD ? Type.YEAR_MONTH_DAY : Type.MONTH_DAY_HOUR_MIN)
                .setWheelItemTextSize(16)
                .build();
        mDialogAll.show(context.getSupportFragmentManager(), "");
    }

    private void setHourSum(long startTime, long endTime) {
        leaveOverTimeLayout.removeAllViews();
        long ONE_DAY_MS = 24 * 60 * 60 * 1000;
        Date date_start = new Date(startTime);
        Date date_end = new Date(endTime);
        //计算日期从开始时间于结束时间的0时计算
        Calendar fromCalendar = Calendar.getInstance();
        fromCalendar.setTime(date_start);
        fromCalendar.set(Calendar.HOUR_OF_DAY, 0);
        fromCalendar.set(Calendar.MINUTE, 0);
        fromCalendar.set(Calendar.SECOND, 0);
        fromCalendar.set(Calendar.MILLISECOND, 0);
        Calendar toCalendar = Calendar.getInstance();
        toCalendar.setTime(date_end);
        toCalendar.set(Calendar.HOUR_OF_DAY, 0);
        toCalendar.set(Calendar.MINUTE, 0);
        toCalendar.set(Calendar.SECOND, 0);
        toCalendar.set(Calendar.MILLISECOND, 0);
        int dayNum = (int) ((toCalendar.getTimeInMillis() - fromCalendar.getTimeInMillis()) / (ONE_DAY_MS));
        if (dayNum > 6) {
            DialogUtils.showTipDialog(context, "所选时间范围不能超过7天");
            return;
        }
        if (dayNum > 0) {
            hourTv.setText("");
            leaveOvertimeTitleLayout.setVisibility(View.VISIBLE);
            for (int i = 0; i <= dayNum; i++) {
                long itemTime = fromCalendar.getTimeInMillis() + i * ONE_DAY_MS;
                View itemView = View.inflate(context, R.layout.item_hour_set, null);
                TextView itemTimeTv = itemView.findViewById(R.id.item_time_tv);
                final EditText houtEt = itemView.findViewById(R.id.item_hour_et);

                //请假这里需要计算时长
                if (i == 0) {
                    itemTimeTv.setText(TimeUtils.getTimeYmdHm(startTime));
                } else if (i == dayNum) {
                    itemTimeTv.setText(TimeUtils.getTimeYmdHm(endTime));
                } else {//工作时间
                    itemTimeTv.setText(TimeUtils.getTimeYmd(itemTime));
                }

                houtEt.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    }

                    @Override
                    public void onTextChanged(CharSequence s, int i, int i1, int i2) {
                        if (TextUtil.setInput1Dot(houtEt, s)) {
                            return;
                        }
                        if (!TextUtil.isEmpty(s.toString())) {
                            double num = TextUtil.convertToDouble(s.toString(), 0);
                            if (num > 24) {
                                ToastUtil.show(context, "加班时长不得超过24小时");
                                houtEt.setText("");
                            }
                        }
                        sumTotalHour(false);
                    }

                    @Override
                    public void afterTextChanged(Editable editable) {

                    }
                });
                //产值
                leaveOverTimeLayout.addView(itemView);
            }
        } else {//此时在同一天之内
            leaveOvertimeTitleLayout.setVisibility(View.GONE);
            String time = "";
            if (TYPE == 2) {
                //time = TimeUtils.betweenHours(startTime, endTime) + "";
            } else {
                time = TimeUtils.betweenHoursAttend(startTime, endTime);
            }

            //加班不计算
            if (TYPE != 2) {
                hourTv.setText(TextUtil.remove_0(time));
            }
        }

    }

    //统计加班  请假时长
    private boolean sumTotalHour(boolean showEmpty) {
        overtimeDateModelList = new JSONArray();
        double totalHourDouble = 0;
        int childCount = leaveOverTimeLayout.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View itemView = leaveOverTimeLayout.getChildAt(i);
            EditText hourEt = itemView.findViewById(R.id.item_hour_et);
            String hourStr = hourEt.getText().toString();
            TextView itemTimeTv = itemView.findViewById(R.id.item_time_tv);
            String overtimeDate = itemTimeTv.getText().toString();

            if (TextUtil.isEmpty(hourStr)) {
                if (showEmpty) {//提交时候
                    String hourTitle = TextUtil.subTimeYmd(overtimeDate);
                    ToastUtil.show(context, hourTitle + " 加班时长不能为空");
                    return false;
                }
            } else {
           /*     if (overtimeArr != null && overtimeArr.size() > 0) {
                    try {
                        hourEt.setText(overtimeArr.get(i).getAsJsonObject().get(KeyConst.period).getAsString());
                    } catch (Exception e) {
                    }
                }*/
                double period = TextUtil.convertToDouble(hourStr, 0);
                //加班
                totalHourDouble = totalHourDouble + period;//总时长

                JSONObject resultObj = new JSONObject();
                try {
                    resultObj.put(KeyConst.period, period);
                    resultObj.put(KeyConst.overtimeDate, overtimeDate);
                } catch (JSONException e) {

                }
                overtimeDateModelList.put(resultObj);
            }

        }
        hourTv.setText((totalHourDouble + "").replace(".0", ""));
        return true;
    }

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

    JsonArray employeeIdArr = new JsonArray();

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 2 && resultCode == 2) {//公文
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
            startTimeValueTv.setText(nameStr);
            employeeIdArr = choosedEmployeeIds;

        } else if (requestCode > 10 && resultCode == 3) {
            if (emplyeeChoosedDialog != null) {
                emplyeeChoosedDialog.dismiss();
            }
            employeeInfo = (EmployeeInfo) data.getSerializableExtra(KeyConst.OBJ_INFO);

            if (employeeInfo != null) {
                employeeChoosedName = employeeInfo.getEmployeeName();
                if (TYPE == 4) {
                    String status = employeeInfo.getEmployeeStatus();
                    //1 试用   2  正式  3  离职
                    if (!"1".equals(status)) {
                        ToastUtil.show(context, employeeChoosedName + "不是试用状态,不能发起转正");
                        employeeChoosedId = 0;
                        employeeChoosedName = "";
                        return;
                    }
                }

                employeeChoosedId = employeeInfo.getId();
                if (TYPE == 12) {//付款  下个环节审批人
                    if (requestCode == 11) {
                        employeeChoosedId1 = employeeChoosedId;
                        nextAuditValueTv1.setText(employeeChoosedName);

                    } else if (requestCode == 12) {
                        employeeChoosedId2 = employeeChoosedId;
                        nextAuditValueTv2.setText(employeeChoosedName);
                    } else if (requestCode == 13) {
                        employeeChoosedId3 = employeeChoosedId;
                        nextAuditValueTv3.setText(employeeChoosedName);
                    }

                } else {
                    topTypeValueTv.setText(employeeChoosedName);
                    item0ValueTv.setText(emplyeeChooeedDeptName);
                    item1ValueTv.setText(TextUtil.remove_N(employeeInfo.getEmploymentDate()));//入职
                    String probationPeriod = employeeInfo.getProbationPeriod();
                    probationPeriod = probationPeriod.replace(".00", "");
                    probationPeriod = probationPeriod.replace(".0", "");
                    item2ValueTv.setText(probationPeriod + "月");//试用期
                }

            }
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

    //草稿箱进来
    private void setDraftView() {
        JsonObject infoObj = info.object;
        if (null == infoObj || infoObj.isJsonNull()) {
            return;
        }
        remarkTv.setText(Utils.getObjStr(infoObj, KeyConst.remark));
        switch (TYPE) {
            case 0://请假
            case 2://加班

                boolean isBreakOff = "true".equals(Utils.getObjStr(infoObj, KeyConst.breakOff));
                breakOff = isBreakOff ? 0 : 1;

                startTimeValueTv.setText(getResources().getTextArray(R.array.whether_leave_arr)[breakOff]);

                hourTv.setText(Utils.getObjStr(infoObj, KeyConst.period));

                leaveTypeValue = Utils.getObjStr(infoObj, TYPE == 0 ? KeyConst.leaveType : KeyConst.type);

                topTypeValueTv.setText(Utils.getDictTypeName(context,
                        TYPE == 0 ? KeyConst.LEAVE_TYPE : KeyConst.OVERTIME_TYPE, leaveTypeValue));


                String listTypeKey = TYPE == 0 ?
                        KeyConst.leaveDateModelList : KeyConst.overtimeDateModelList;
                JsonElement objArr = infoObj.get(listTypeKey);

                if (objArr == null || objArr.isJsonNull()) {
                    return;
                }
                JsonArray tripArr = infoObj.getAsJsonArray(listTypeKey);

                if (tripArr == null || tripArr.size() == 0 || tripArr.isJsonNull()) {
                    return;
                }
                leaveList = new Gson().fromJson(tripArr, new TypeToken<List<TripItemInfo>>() {
                }.getType());

                notifyLeavelsLayout(leaveList);
                break;
            case 4://转正
            case 6://离职
                //选择人员
                employeeChoosedId = infoObj.get(KeyConst.employeeId).getAsInt();
                employeeChoosedName = info.applicantName;
                topTypeValueTv.setText(employeeChoosedName);
                item0ValueTv.setText(info.deptName);

                String endDateStr;
                if (TYPE == 4) {
                    item1ValueTv.setText(Utils.getObjStr(infoObj, KeyConst.employmentDate));
                    item2ValueTv.setText(Utils.getObjStr(infoObj, KeyConst.probationPeriod));
                    endDateStr = Utils.getObjStr(infoObj, KeyConst.regularDate);
                } else {
                    item1ValueTv.setText(Utils.getObjStr(infoObj, KeyConst.employmentDate));
                    endDateStr = Utils.getObjStr(infoObj, KeyConst.lastWorkDate);
                }
                try {
                    endTime = TimeUtils.getFormatYmd().parse(endDateStr).getTime();
                } catch (ParseException e) {
                }
                endTimeValueTv.setText(endDateStr);
                break;
            case 5://招聘
                topTypeValueTv.setText(Utils.getObjStr(infoObj, KeyConst.recruitPost));
                startTimeValueTv.setText(Utils.getObjStr(infoObj, KeyConst.recruitNum));
                String arrivalDateStr = Utils.getObjStr(infoObj, KeyConst.arrivalDate);
                try {
                    endTime = TimeUtils.getFormatYmd().parse(arrivalDateStr).getTime();
                } catch (ParseException e) {
                }
                endTimeValueTv.setText(arrivalDateStr);

                break;
            case 7://公文
                if (info.informList != null && !info.informList.isJsonNull()) {
                    employeeIdArr = info.informList.getAsJsonArray();
                }
                //抄送人
                startTimeValueTv.setText(info.informNameList);
                topTypeValueTv.setText(Utils.getObjStr(infoObj, KeyConst.auditDept));
                break;
            case 11://备用金
                String useDateStr = Utils.getObjStr(infoObj, KeyConst.useDate);
                String returnDateStr = Utils.getObjStr(infoObj, KeyConst.returnDate);
                try {
                    startTime = TimeUtils.getFormatYmd().parse(useDateStr).getTime();
                    endTime = TimeUtils.getFormatYmd().parse(returnDateStr).getTime();
                } catch (ParseException e) {
                }
                startTimeValueTv.setText(useDateStr);
                endTimeValueTv.setText(returnDateStr);
                topTypeValueTv.setText(Utils.getObjStr(infoObj, KeyConst.applyAmount));

                break;
            case 12://付款
                topTypeValueTv.setText(Utils.getObjStr(infoObj, KeyConst.amount));
                startTimeValueTv.setText(Utils.getObjStr(infoObj, KeyConst.payType));

                endTimeValueTv.setText(Utils.getObjStr(infoObj, KeyConst.payDate));

                payObjectTv.setText(Utils.getObjStr(infoObj, KeyConst.payObject));
                bankNameTv.setText(Utils.getObjStr(infoObj, KeyConst.bankName));
                accountTv.setText(Utils.getObjStr(infoObj, KeyConst.bankAccount));


                employeeChoosedId1 = infoObj.get(KeyConst.firstAuditor).getAsInt();
                employeeChoosedId2 = infoObj.get(KeyConst.secondAuditor).getAsInt();
                employeeChoosedId3 = infoObj.get(KeyConst.thirdAuditor).getAsInt();
                nextAuditValueTv1.setText(Utils.getObjStr(infoObj, KeyConst.firstAuditorName));
                nextAuditValueTv2.setText(Utils.getObjStr(infoObj, KeyConst.secondAuditorName));
                nextAuditValueTv3.setText(Utils.getObjStr(infoObj, KeyConst.thirdAuditorName));

                break;
        }

    }

    private void initView() {
        RelativeLayout topTypeLayout = (RelativeLayout) findViewById(R.id.leave_type_layout);
        RelativeLayout startTimeLayout = (RelativeLayout) findViewById(R.id.start_time_layout);
        RelativeLayout endTimeLayout = (RelativeLayout) findViewById(R.id.end_time_layout);
        RelativeLayout hoursLayout = (RelativeLayout) findViewById(R.id.hours_layout);
        TextView leaveTimeTitleTv = (TextView) findViewById(R.id.leave_time_title_tv);

        topTypeTitleTv = (TextView) findViewById(R.id.leave_type_title_tv);
        remarkTv = (TextView) findViewById(R.id.remark_tv);
        topTypeValueTv = (TextView) findViewById(R.id.type_value_tv);
        TextView remarkTitleTv = (TextView) findViewById(R.id.work_remark_title_tv);//备注

        TextView startTimeTitleTv = (TextView) findViewById(R.id.start_time_title_tv);
        TextView endTimeTitleTv = (TextView) findViewById(R.id.end_time_title_tv);
        startTimeValueTv = (TextView) findViewById(R.id.work_start_time_tv);
        endTimeValueTv = (TextView) findViewById(R.id.work_end_time_tv);

        startTimeValueTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TYPE == 3) {
                    showPickeTimeDilog((TextView) v, 0);
                } else {
                    showPickeTimeDilog((TextView) v, 1);
                }
            }
        });
        endTimeValueTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPickeTimeDilog((TextView) v, 2);
            }
        });


        View layoutItem0 = findViewById(R.id.work_layout_item_0);
        TextView item0TitleTv = findViewById(R.id.item_0_title_tv);
        item0ValueTv = findViewById(R.id.item_0_value_tv);
        View layoutItem1 = findViewById(R.id.work_layout_item_1);
        TextView item1TitleTv = findViewById(R.id.item_1_title_tv);
        item1ValueTv = findViewById(R.id.item_1_value_tv);
        View layoutItem2 = findViewById(R.id.work_layout_item_2);
        TextView item2TitleTv = findViewById(R.id.item_2_title_tv);
        item2ValueTv = findViewById(R.id.item_2_value_tv);
        hourTv = (EditText) findViewById(R.id.hours_tv);
        //获取年假时长
        leaveOverTimeLayout = findViewById(R.id.leave_overtime_hour_sum_layout);
        leaveOvertimeTitleLayout = findViewById(R.id.leave_overtime_hour_title_layout);
        typeStr = TYPE == 0 ? "请假" : "加班";
        switch (TYPE) {
            case 0://请假
            case 2://加班
                getLeaveTypeData(TYPE == 0);
                remarkTitleTv.setText(TYPE == 0 ? "请假事由" : "加班原因");
                topTypeTitleTv.setText(typeStr + "类型");
                hourTv.setText("");
                hoursLayout.setVisibility(View.VISIBLE);
                topTypeLayout.setVisibility(View.VISIBLE);

                startTimeLayout.setVisibility(TYPE == 0 ? View.GONE : View.VISIBLE);
                startTimeTitleTv.setText("是否调休");
                int whetherArr = R.array.whether_leave_arr;
                startTimeValueTv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        new MaterialDialog.Builder(context)
                                .items(whetherArr)//是否调休
                                .itemsCallback(new MaterialDialog.ListCallback() {
                                    @Override
                                    public void onSelection(MaterialDialog dialog, View itemView,
                                                            int position, CharSequence text) {
                                        breakOff = position;
                                        startTimeValueTv.setText(text);
                                    }
                                })
                                .show();
                    }
                });


                endTimeLayout.setVisibility(View.GONE);
                leaveOverTimeLayout.setPadding(0, 0, 0, 0);

                topTypeValueTv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (leaveTypeNameArr == null || leaveTypeNameArr.length == 0) {
                            getLeaveTypeData(TYPE == 0);
                            return;
                        }
                        int length = leaveTypeNameArr.length;
                        for (int i = 0; i < length; i++) {
                            if (leaveTypeNameArr[i].equals("年假")) {
                                leaveTypeNameArr[i] = "年假（剩余" + annualLeaveAmount + "小时）";
                            }
                            if (leaveTypeNameArr[i].equals("调休")) {
                                leaveTypeNameArr[i] = "调休（剩余" + breakOffAmount + "小时）";
                            }
                        }
                        new MaterialDialog.Builder(context)
                                .items(leaveTypeNameArr)// 列表数据
                                .itemsGravity(GravityEnum.CENTER)
                                .itemsCallback(new MaterialDialog.ListCallback() {
                                    @Override
                                    public void onSelection(MaterialDialog dialog, View itemView,
                                                            int position, CharSequence text) {
                                        topTypeValueTv.setText(text);
                                        leaveTypeValue = leaveTypeValueArr[position];
                                    }
                                })
                                .show();
                    }
                });

                leaveList.add(new TripItemInfo(0, "", "", 0, 0));
                TextView addLeaveBt = findViewById(R.id.add_leave_tv);
                addLeaveBt.setText("+ 添加" + typeStr);

                addLeaveBt.setVisibility(View.VISIBLE);
                addLeaveBt.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        leaveList.add(new TripItemInfo(leaveList.size() + 1, "", "", 0, 0));
                        notifyLeavelsLayout(leaveList);
                    }
                });

                break;
            case 4://转正
            case 6://离职
                startTimeLayout.setVisibility(View.GONE);
                endTimeLayout.setVisibility(View.VISIBLE);
                topTypeValueTv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //选择人员
                        showEmplyeeListDialog(11);
                    }
                });
                layoutItem0.setVisibility(View.VISIBLE);
                topTypeLayout.setVisibility(View.VISIBLE);
                topTypeTitleTv.setText("实际申请人");

                layoutItem0.setVisibility(View.VISIBLE);
                item0TitleTv.setText("所属部门");
                layoutItem1.setVisibility(View.VISIBLE);
                item1TitleTv.setText("入职日期");

                String hint = remarkTv.getHint().toString();
                remarkTv.setFilters(new InputFilter[]{new InputFilter.LengthFilter(500)});
                remarkTv.setHint(hint.replace("200", "500"));
                if (TYPE == 4) {
                    endTimeTitleTv.setText("转正日期");
                    remarkTitleTv.setText("试用期工作表现");
                    layoutItem2.setVisibility(View.VISIBLE);
                    item2TitleTv.setText("试用期");
                } else {
                    endTimeTitleTv.setText("最后工作日期");
                    remarkTitleTv.setText("离职原因");
                }
                break;
            case 5://招聘
                endTimeLayout.setVisibility(View.VISIBLE);
                topTypeLayout.setVisibility(View.VISIBLE);
                endTimeTitleTv.setText("期望到岗日期");
                topTypeTitleTv.setText("需求岗位");
                topTypeValueTv.setHint(R.string.input_hint);
                topTypeValueTv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        new MaterialDialog.Builder(context).title(" ")
                                .input("需求岗位", "", new MaterialDialog.InputCallback() {
                                    @Override
                                    public void onInput(@NonNull MaterialDialog dialog,
                                                        CharSequence value) {
                                        topTypeValueTv.setText(value);
                                    }
                                }).show();
                    }
                });
                startTimeTitleTv.setText("需求人数");
                startTimeValueTv.setHint(R.string.input_hint);
                startTimeValueTv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        new MaterialDialog.Builder(context).title(" ")
                                .inputType(InputType.TYPE_CLASS_NUMBER)
                                .input("输入人数", "", new MaterialDialog.InputCallback() {
                                    @Override
                                    public void onInput(@NonNull MaterialDialog dialog,
                                                        CharSequence value) {
                                        startTimeValueTv.setText(value);
                                    }
                                }).show();
                    }
                });
                remarkTitleTv.setText("岗位职责需求");
                remarkTv.setHint(remarkTv.getHint().toString().replace("200", "1000"));
                remarkTv.setFilters(new InputFilter[]{new InputFilter.LengthFilter(1000)});
                break;
            case 7://公文
                topTypeLayout.setVisibility(View.VISIBLE);
                topTypeTitleTv.setText("审批部门");
                findViewById(R.id.title_top_height_layout).setVisibility(View.GONE);

                topTypeValueTv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        new MaterialDialog.Builder(context)
                                .items(R.array.aduit_dept_arr)// 列表数据
                                .itemsCallback(new MaterialDialog.ListCallback() {
                                    @Override
                                    public void onSelection(MaterialDialog dialog, View itemView,
                                                            int position, CharSequence text) {
                                        topTypeValueTv.setText(text);
                                    }
                                })
                                .show();
                    }
                });

                startTimeTitleTv.setText("抄送人");
                startTimeValueTv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Bundle bundle = new Bundle();
                        bundle.putSerializable(KeyConst.OBJ_INFO, (Serializable) parentList);//序列化,要注意转化(Serializable)
                        Intent intent = new Intent(context, ChooseEmplyeeActivity.class);
                        intent.putExtras(bundle);
                        context.startActivityForResult(intent, 2);
                    }
                });
                remarkTitleTv.setText("公文内容");
                remarkTv.setHint(remarkTv.getHint().toString().replace("200", "1000"));
                remarkTv.setFilters(new InputFilter[]{new InputFilter.LengthFilter(1000)});
                break;
            case 11://备用金
                remarkTitleTv.setText("事由");
                topTypeLayout.setVisibility(View.VISIBLE);
                startTimeLayout.setVisibility(View.VISIBLE);
                endTimeLayout.setVisibility(View.VISIBLE);
                startTimeTitleTv.setText("使用日期");
                endTimeTitleTv.setText("归还日期");
                topTypeTitleTv.setText("申请金额(元)");
                topTypeValueTv.setHint(R.string.input_hint);
                topTypeValueTv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        MaterialDialog.Builder moneyInputDialog = DialogUtils.getMoneyInputDialog(context);
                        moneyInputDialog.onNegative(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                Editable moneyStr = ((EditText) dialog.getCustomView()).getText();
                                topTypeValueTv.setText(moneyStr);
                            }
                        }).show();

                    }
                });
                break;
            case 12://付款
                findViewById(R.id.work_pay_layout).setVisibility(View.VISIBLE);
                remarkTitleTv.setText("付款事由");
                payObjectTv = (EditText) findViewById(R.id.work_pay_to_et);
                bankNameTv = (EditText) findViewById(R.id.work_pay_bank_et);
                accountTv = (EditText) findViewById(R.id.bank_account_tv);
                topTypeLayout.setVisibility(View.VISIBLE);
                startTimeLayout.setVisibility(View.VISIBLE);
                endTimeLayout.setVisibility(View.VISIBLE);
                topTypeTitleTv.setText("付款金额(元)");

                topTypeValueTv.setHint("请输入付款金额");
                topTypeValueTv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        MaterialDialog.Builder moneyInputDialog = DialogUtils.getMoneyInputDialog(context);
                        moneyInputDialog.onNegative(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                Editable moneyStr = ((EditText) dialog.getCustomView()).getText();
                                topTypeValueTv.setText(moneyStr);
                            }
                        }).show();

                    }
                });
                startTimeTitleTv.setText("付款方式");
                startTimeValueTv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        getPayTypeDict();
                    }
                });
                endTimeTitleTv.setText("支付日期");
                nextAuditValueTv1 = findViewById(R.id.next_auditor_tv1);
                nextAuditValueTv2 = findViewById(R.id.next_auditor_tv2);
                nextAuditValueTv3 = findViewById(R.id.next_auditor_tv3);
                setNextAuditDeleted(nextAuditValueTv1, 1);
                setNextAuditDeleted(nextAuditValueTv2, 2);
                setNextAuditDeleted(nextAuditValueTv3, 3);
                nextAuditValueTv1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //第一环节审批人
                        showEmplyeeListDialog(11);
                    }
                });
                nextAuditValueTv2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        showEmplyeeListDialog(12);
                    }
                });
                nextAuditValueTv3.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        showEmplyeeListDialog(13);
                    }
                });

                break;
        }
    }

    private void showEmplyeeListDialog(final int auditNum) {
        if (deptInfos == null) {
            JsonArray dictArr = Utils.getDeptTree(context);
            deptInfos = new Gson().fromJson(dictArr, new TypeToken<List<DeptInfo>>() {
            }.getType());
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style
                .dialog_appcompat_theme_fullscreen);
        LayoutInflater inflater = LayoutInflater.from(context);
        View v = inflater.inflate(R.layout.layout_dialog_emplyee_choose, null);
        LinearLayout itemsLayout = (LinearLayout) v.findViewById(R.id.emplyee_seleted_items_layout);
        itemsLayout.removeAllViews();
        if (deptInfos == null || deptInfos.size() == 0) {
            return;
        }
        DeptInfo dataInfo = deptInfos.get(0);
        List<DeptInfo.ChildrenBeanX> deptInfoChildren = dataInfo.getChildren();
        if (deptInfoChildren == null || deptInfoChildren.size() == 0) {
            return;
        }
        emplyeeChoosedDialog = builder.create();
        emplyeeChoosedDialog.show();
        emplyeeChoosedDialog.getWindow().setContentView(v);
        for (DeptInfo.ChildrenBeanX deptInfo : deptInfoChildren) {
            View itemView = View.inflate(context, R.layout.item_address, null);
            TextView nameTv = (TextView) itemView.findViewById(R.id.address_name_tv);
            nameTv.setPadding(40, 0, 0, 0);
            TextView numberTv = (TextView) itemView.findViewById(R.id.address_number_tv);

            if (deptInfo != null) {
                final int id = deptInfo.getId();
                final String title = deptInfo.getTitle();
                final int employeeCount = deptInfo.getTotal();
                final List<DeptInfo.ChildrenBeanX.ChildrenBean> childrenList = deptInfo.getChildren();
                nameTv.setText(title);
                numberTv.setText(employeeCount + "人");

                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        emplyeeChooeedDeptName = title;
                        Bundle bundle = new Bundle();
                        bundle.putSerializable(KeyConst.OBJ_INFO, (Serializable) childrenList);

                        Intent intent = new Intent(context, AddressListActivity.class);
                        intent.putExtra(KeyConst.id, id);
                        intent.putExtras(bundle);
                        intent.putExtra(KeyConst.numbers, employeeCount);
                        intent.putExtra(KeyConst.title, title);
                        intent.putExtra(KeyConst.type, 1);
                        context.startActivityForResult(intent, auditNum);

                    }
                });
                itemsLayout.addView(itemView);
            }
        }

        v.findViewById(R.id.dialog_btn_cancel).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                emplyeeChoosedDialog.dismiss();
            }
        });
        v.findViewById(R.id.emplyee_seleted_save_bt).setVisibility(View.GONE);
    }


    //获取人员列表

    private String leaveTypeNameArr[] = new String[]{};
    private String leaveTypeValueArr[] = new String[]{};

    private void getLeaveTypeData(boolean isLeave) {
        if (!NetUtil.isNetworkConnected(context)) {
            ToastUtil.show(context, getString(R.string.no_network));
            return;
        }
        getLeaveAmount();
        String url = Constant.WEB_SITE + "/dict/dicts/cached/" +
                (isLeave ? "LEAVE_TYPE" : "OVERTIME_TYPE");

        Response.Listener<JsonArray> successListener = new Response
                .Listener<JsonArray>() {
            @Override
            public void onResponse(JsonArray result) {
                if (result != null) {
                    leaveTypeNameArr = new String[result.size()];
                    leaveTypeValueArr = new String[result.size()];
                    for (int i = 0; i < result.size(); i++) {
                        JsonObject object = result.get(i).getAsJsonObject();
                        leaveTypeNameArr[i] = object.get(KeyConst.name).getAsString();
                        leaveTypeValueArr[i] = object.get(KeyConst.value).getAsString();
                    }
                    if (processId > 0 && !TextUtil.isEmpty(leaveTypeValue)) {
                        topTypeValueTv.setText(leaveTypeNameArr[Integer.valueOf(leaveTypeValue)]);
                    }
                } else {
                    ToastUtil.show(context, "暂无数据");
                }
            }
        };

        Request<JsonArray> versionRequest = new
                GsonRequest<JsonArray>(Request.Method.GET, url,
                        successListener, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        ToastUtil.show(context, R.string.server_exception);
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

    //获取年假时长
    private void getLeaveAmount() {
        String url = Constant.WEB_SITE + "/biz/bizAnnualLeaveRegular/leaveProcess";
        Response.Listener<JsonObject> successListener = new Response
                .Listener<JsonObject>() {
            @Override
            public void onResponse(JsonObject result) {
                if (result != null) {
                    breakOffAmount = result.get(KeyConst.breakOffAmount).getAsString();
                    annualLeaveAmount = result.get(KeyConst.annualLeaveAmount).getAsString();
                }
            }
        };

        Request<JsonObject> versionRequest = new
                GsonRequest<JsonObject>(Request.Method.GET, url,
                        successListener, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {

                        Log.d(TAG, "获取时长失败" + volleyError);

                    }
                }, new TypeToken<JsonObject>() {
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

    private void postReport(int status) {
        String urlDraft = Constant.WEB_SITE + "/biz/process/";
        String url = Constant.WEB_SITE + "/biz/process/";
        String url_draft = "";
        Map<String, Object> map = new HashMap<>();
        remark = remarkTv.getText().toString();
        String topTypeValue = topTypeValueTv.getText().toString();
        switch (TYPE) {
            case 0:
            case 2://加班
                String period = hourTv.getText().toString();//时长
                if (TextUtil.isEmpty(period)) {
                    period = "0";
                }
                if (TextUtil.isEmpty(leaveTypeValue)) {
                    ToastUtil.show(context, "请选择" + typeStr + "类型");
                    return;
                }

                if (TYPE == 2 && breakOff == -1) {
                    ToastUtil.show(context, "请选择是否调休");
                    return;
                }

                JSONArray leaveDateModelList = new JSONArray();
                for (TripItemInfo tripInfo : leaveList) {
                    JSONObject resultObj = new JSONObject();

                    try {
                        String tripName = tripInfo.getTitleName();
                        long startTime = tripInfo.getStartTimeL();
                        long endTime = tripInfo.getEndTimeL();

                        if (0 == startTime) {
                            ToastUtil.show(context, tripName + "开始时间不能为空");
                            return;
                        }
                        if (0 == endTime) {
                            ToastUtil.show(context, tripName + "结束时间不能为空");
                            return;
                        }
                        resultObj.put(KeyConst.period, tripInfo.getPeriod());

                        resultObj.put(KeyConst.startTime, TimeUtils.getTimeYmdHm(startTime) + ":00");
                        resultObj.put(KeyConst.endTime, TimeUtils.getTimeYmdHm(endTime) + ":00");

                        leaveDateModelList.put(resultObj);
                    } catch (Exception e) {
                        Log.d(TAG, "list解析异常:");
                        return;
                    }
                }

                if (Double.valueOf(period) == 0) {
                    ToastUtil.show(context, typeStr + "总时长必须大于0");
                    return;
                }

                if (ToastUtil.showCannotEmpty(context, remark, TYPE == 2 ? "加班原因" : "请假事由")) {
                    return;
                }
                map.put(KeyConst.period, period);

                if (TYPE == 0) {//请假
                    url_draft = "draft/LEAVE";
                    url = url + "leave";
                    map.put(KeyConst.leaveType, leaveTypeValue);
                    map.put(KeyConst.leaveDateModelList, leaveDateModelList);
                } else {//加班
                    url_draft = "draft/OVERTIME";
                    url = url + "overtime";
                    map.put(KeyConst.type, leaveTypeValue);
                    map.put(KeyConst.breakOff, breakOff == 0);//是否调休
                    map.put(KeyConst.overtimeDateModelList, leaveDateModelList);
                }


                break;
            case 4://转正
                url = url + "regularWorker";
                url_draft = "draft/REGULAR_WORKER";
                if (employeeChoosedId == 0) {
                    ToastUtil.show(context, "申请人不能为空");
                    return;
                }
                if (endTime == 0) {
                    ToastUtil.show(context, "请填写转正日期");
                    return;
                }
                if (TextUtil.isEmpty(remark)) {
                    ToastUtil.show(context, "请填写试用期工作表现");
                    return;
                }

                map.put(KeyConst.employeeId, employeeChoosedId);
                map.put(KeyConst.regularDate, TimeUtils.getTimeYmd(endTime));

                break;
            case 5://招聘
                url_draft = "draft/RECRUIT";
                url = url + "recruit";
                if (TextUtil.isEmpty(topTypeValue)) {
                    ToastUtil.show(context, "请填写需求岗位");
                    return;
                }
                String recruitNum = startTimeValueTv.getText().toString();
                if (TextUtil.isEmpty(recruitNum)) {
                    ToastUtil.show(context, "需求人数不能为空");
                    return;
                }
                if (endTime == 0) {
                    ToastUtil.show(context, "期望到岗日期不能为空");
                    return;
                }
                if (TextUtil.isEmpty(remark)) {
                    ToastUtil.show(context, "岗位职责需求不能为空");
                    return;
                }
                map.put(KeyConst.recruitPost, topTypeValue);//需求岗位
                map.put(KeyConst.recruitNum, recruitNum);//需求人数
                map.put(KeyConst.arrivalDate, TimeUtils.getTimeYmd(endTime));
                break;
            case 6://离职
                url = url + "dimission";
                url_draft = "draft/DIMISSION";
                if (employeeChoosedId == 0) {
                    ToastUtil.show(context, "申请人不能为空");
                    return;
                }
                if (endTime == 0) {
                    ToastUtil.show(context, "请填写最后工作日期");
                    return;
                }
                if (TextUtil.isEmpty(remark)) {
                    ToastUtil.show(context, "请填写离职原因");
                    return;
                }

                map.put(KeyConst.employeeId, employeeChoosedId);
                map.put(KeyConst.lastWorkDate, TimeUtils.getTimeYmd(endTime));

                break;

            case 7://公文
                url = url + "officialDocument";
                url_draft = "draft/OFFICIAL_DOCUMENT";
                if (TextUtil.isEmpty(topTypeValue)) {
                    ToastUtil.show(context, "审批部门" + getString(R.string.cannot_empty));
                    return;
                }
                if (TextUtil.isEmpty(remark)) {
                    ToastUtil.show(context, "公文内容" + getString(R.string.cannot_empty));
                    return;
                }
                map.put(KeyConst.auditDept, topTypeValue);
                if (employeeIdArr != null && employeeIdArr.size() > 0 && !employeeIdArr.isJsonNull()) {
                    JSONArray informList = new JSONArray();
                    for (JsonElement element : employeeIdArr) {
                        informList.put(element.getAsInt());
                    }
                    map.put(KeyConst.informList, informList);
                } else {
                    ToastUtil.show(context, "抄送人" + getString(R.string.cannot_empty));
                    return;
                }
                break;
            case 11://备用金
                url = url + "pettyCash";
                url_draft = "draft/PETTY_CASH";
                if (TextUtil.isEmpty(topTypeValue)) {
                    ToastUtil.show(context, "备用金金额" + getString(R.string.cannot_empty));
                    return;
                }
                if (0 == startTime) {
                    ToastUtil.show(context, "使用日期" + getString(R.string.cannot_empty));
                    return;
                }
                if (0 == endTime) {
                    ToastUtil.show(context, "归还日期" + getString(R.string.cannot_empty));
                    return;
                }
                if (ToastUtil.showCannotEmpty(context, remark, "申请事由")) {
                    return;
                }
                map.put(KeyConst.applyAmount, topTypeValue);
                map.put(KeyConst.useDate, TimeUtils.getTimeYmd(startTime));
                map.put(KeyConst.returnDate, TimeUtils.getTimeYmd(endTime));
                break;
            case 12://付款
                url_draft = "draft/PAY";
                url = url + "pay";

                String bankAccount = accountTv.getText().toString();
                String payObject = payObjectTv.getText().toString();
                String bankName = bankNameTv.getText().toString();

                String payType = startTimeValueTv.getText().toString();
                String payDate = endTimeValueTv.getText().toString();
                if (ToastUtil.showCannotEmpty(context, topTypeValue, "付款金额")) {
                    return;
                }
                if (ToastUtil.showCannotEmpty(context, payType, "付款方式")) {
                    return;
                }
                if (ToastUtil.showCannotEmpty(context, payDate, "支付日期")) {
                    return;
                }
                if (ToastUtil.showCannotEmpty(context, payObject, "支付对象")) {
                    return;
                }
                if (ToastUtil.showCannotEmpty(context, bankName, "开户行")) {
                    return;
                }
                if (ToastUtil.showCannotEmpty(context, bankAccount, "银行账户")) {
                    return;
                }
                if (employeeChoosedId1 == 0) {
                    ToastUtil.show(context, "第一审批人不能为空");
                    return;
                }
                if (ToastUtil.showCannotEmpty(context, remark, "付款事由")) {
                    return;
                }
                map.put(KeyConst.amount, topTypeValue);
                map.put(KeyConst.payType, payType);
                map.put(KeyConst.payDate, payDate);
                map.put(KeyConst.payObject, payObject);
                map.put(KeyConst.bankName, bankName);
                map.put(KeyConst.bankAccount, bankAccount);
                map.put(KeyConst.firstAuditor, employeeChoosedId1);
                map.put(KeyConst.secondAuditor, employeeChoosedId2);
                map.put(KeyConst.thirdAuditor, employeeChoosedId3);
                break;
        }
        DialogHelper.showWaiting(fm, "加载中...");

        if (!NetUtil.isNetworkConnected(context)) {
            ToastUtil.show(context, getString(R.string.no_network));
            return;
        }

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

        map.put(KeyConst.remark, remark);
        map.put(KeyConst.status, status);

        int postType = Request.Method.POST;
        if (processId > 0) {
            map.put(KeyConst.id, processId);
            postType = Request.Method.PUT;
            url = urlDraft + url_draft;
        }

        JSONObject jsonObject = new JSONObject(map);
        Log.d(TAG, "提交数据" + jsonObject.toString());
        JsonObjectRequest jsonRequest = new JsonObjectRequest(postType, url,
                jsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject result) {
                        DialogHelper.hideWaiting(fm);
                        if (result != null) {
                            ToastUtil.show(context, "提交成功");
                            context.finish();
                        } else {
                            ToastUtil.show(context, R.string.commit_faild);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                DialogHelper.hideWaiting(fm);
                String errorMsg = TextUtil.getErrorMsg(error);
                if (!TextUtil.isEmpty(errorMsg)) {
                    try {
                        JSONObject obj = new JSONObject(errorMsg);
                        if (obj != null) {
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

    private void getPayTypeDict() {
        if (!NetUtil.isNetworkConnected(context)) {
            return;
        }
        String url = Constant.WEB_SITE + "/dict/dicts/cached/PAY_TYPE";

        Response.Listener<JsonArray> successListener = new Response
                .Listener<JsonArray>() {
            @Override
            public void onResponse(JsonArray result) {
                if (result != null || result.size() >= 0) {
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
                                    startTimeValueTv.setText(payTypeArr.get(position));
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
        //1 草稿   2 提交
        postReport(Constant.COMMIT);
    }

    //保存草稿
    private void initFinishBt() {
        findViewById(R.id.left_bt).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                topTypeTitleValue = topTypeTitleTv.getText().toString();
                remark = remarkTv.getText().toString();
                if (processId > 0) {
                    finish();
                    return;
                }

                if (TYPE == 7 && !TextUtil.isEmpty(remark)) {
                    showDraftBox();
                    return;
                }
                if (fileData != null) {
                    showDraftBox();
                    return;
                }
                if (startTime == 0 || TextUtil.isEmpty(topTypeTitleValue)
                        ) {
                    finish();
                    return;
                }
                showDraftBox();
            }
        });
    }

    //草稿
    private void showDraftBox() {
        DialogUtils.getDraftBoxDialog(context).onPositive(new MaterialDialog.SingleButtonCallback() {
            @Override
            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                context.finish();
            }
        }).onPositive(new MaterialDialog.SingleButtonCallback() {
            @Override
            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                //存到服务器
                postReport(Constant.DRAFT);
            }
        }).onNegative(new MaterialDialog.SingleButtonCallback() {
            @Override
            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                context.finish();
            }
        }).show();
    }

    private void getInfoData() {
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

    //增加删除按钮
    private void setNextAuditDeleted(final TextView nextAuditTv, final int positon) {
        nextAuditTv.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence s, int i, int i1, int i2) {
                if (!TextUtil.isEmpty(s.toString()) && s.length() > 0) {
                    Drawable icDelete = getResources().getDrawable(
                            R.drawable.ic_delete_gray_circle);
                    nextAuditTv.setCompoundDrawablesWithIntrinsicBounds(null, null,
                            icDelete, null);

                    nextAuditTv.setOnTouchListener(new View.OnTouchListener() {
                        @Override
                        public boolean onTouch(View v, MotionEvent event) {
                            Drawable drawable = nextAuditTv.getCompoundDrawables()[2];
                            if (drawable == null) {
                                return false;
                            }
                            //drawleft 是 小于 ,drawright 是 大于  左上 0 1 ,右下23
                            if (event.getX() > nextAuditTv.getWidth() - drawable.getBounds().width()) {
                                nextAuditTv.setText("");
                                nextAuditTv.setCompoundDrawablesWithIntrinsicBounds(null, null,
                                        getResources().getDrawable(R.drawable.ic_next), null);
                                if (positon == 1) {
                                    employeeChoosedId1 = 0;
                                } else if (positon == 2) {
                                    employeeChoosedId2 = 0;
                                } else {
                                    employeeChoosedId3 = 0;
                                }
                                return true;//不要点击事件
                            }
                            return false;
                        }
                    });
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

    }
}
