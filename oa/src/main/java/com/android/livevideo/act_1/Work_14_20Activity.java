package com.android.livevideo.act_1;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
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
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.android.livevideo.App;
import com.android.livevideo.R;
import com.android.livevideo.act_2.AddressListActivity;
import com.android.livevideo.act_other.CommonBaseActivity;
import com.android.livevideo.act_other.PictBean;
import com.android.livevideo.adapter.FileListAdapter;
import com.android.livevideo.bean.DeptInfo;
import com.android.livevideo.bean.EmployeeInfo;
import com.android.livevideo.bean.FileInfo;
import com.android.livevideo.bean.FileListInfo;
import com.android.livevideo.bean.HistoryInfo;
import com.android.livevideo.bean.MsgInfo;
import com.android.livevideo.bean.PoolInfo;
import com.android.livevideo.bean.ProductInfo;
import com.android.livevideo.core.net.GsonRequest;
import com.android.livevideo.core.utils.Constant;
import com.android.livevideo.core.utils.DialogHelper;
import com.android.livevideo.core.utils.ImageUtil;
import com.android.livevideo.core.utils.KeyConst;
import com.android.livevideo.core.utils.NetUtil;
import com.android.livevideo.core.utils.RetrofitUtil;
import com.android.livevideo.core.utils.TextUtil;
import com.android.livevideo.util.DialogUtils;
import com.android.livevideo.util.FileTypeUtil;
import com.android.livevideo.util.MoneyInputFilter;
import com.android.livevideo.util.TimeUtils;
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
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * @author Gool Lee
 * 过磅单
 */

public class Work_14_20Activity extends CommonBaseActivity {
    private Work_14_20Activity context;
    private int processId;
    private TextView m14AddressEt;
    private ScrollListView listView;
    private FileListAdapter fileListAdapter;
    private FragmentManager fm;
    private int TYPE;
    private List<ProductInfo> checkItemInfoList;
    private List<ProductInfo> productNameInfoList, productSpecInfoList;
    private TextView choosePoolTv, mTimeTv;
    private String commonProductName, commonProductSpec;
    private MsgInfo info;
    private LinearLayout copyerItemsLayout;
    private String resultTypeArr[] = {"合格", "不合格"};
    private String titleStr[] = {"过磅单", "入池单", "配料入池单", "回淋单", "起池单",
            "原料入池检验单", "半成品检验单"};
    private EditText remarkTv, m14CarNumEt, m14DriverNameEt, m14DriverPhoneEt, m14PriceEt,
            m14WeightDeductEt, m14WeightTareEt, m14WeightRoughEt;
    private String timeYmd, sprinkleDate, goodsArriveDate;
    private String remark;
    private LinearLayout layoutView7, layoutView1920;
    private TextView operatorDirectorTv, operatorTv, choose15_16_19Tv, addPoolBt15;
    private TextView mTimeTv2, m15AddressTv, m15CarNumerTv, m18FeelAffirmTv;
    private EditText m15CarUnloadPeopleEt;
    private EditText mBaumeDegreeEt, m17MinuteEt;
    private EditText m18OutPoolNumEt, m15_19CarNumerTv;
    private EditText m19BasePlaceEt, m19FinalJudgeEt;
    private AlertDialog emplyeeChoosedDialog;
    private EmployeeInfo employeeInfo;
    private String operatorName;
    private int operatorId;
    private String CHOOSED_15_16_19_ID, CHOOSE_POOL_ID, OBJECT_POOL_ID;
    private String commonProductId, commonProductSpecId;
    private TextView creatorName16Tv;
    private TextView m18WhetherMergePoolTv, m18MergePoolNumTv;
    private TextView commonProductSpecTv, productNameTv;
    private int objectPoolType;
    private JsonObject infoObj;
    private JsonElement detailEle;
    private RelativeLayout objectPoolLayout;
    private List<PoolInfo.ComponentItemListBean> componentItemList;
    private LinearLayout layout15;
    private int CATEGORY_ID = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initStatusBar();
        setContentView(R.layout.activity_work_14_20);
        context = this;
        fm = getSupportFragmentManager();
        processId = getIntent().getIntExtra(KeyConst.id, 0);
        TYPE = getIntent().getIntExtra(KeyConst.type, 0);
        initTitleBackBt(titleStr[TYPE - 14]);


        initView();

        initFileView();

        initFinishBt();

        if (processId != 0) {
            //草稿箱进来
            getDraftData();
            deleteDraftBt();
        } else {
            if (TYPE == 19 || TYPE == 20) {
                getItem1920List();//配置项
            }
            getCopyerList();//抄送人
        }
        getAuditLogData();//审批流程

        initCopyer();
    }

    private int copyerPositionArr[] = {15, 17, 18, 19, 21, 16, 20};

    //抄送人
    private void getCopyerList() {
        String url = Constant.WEB_SITE + "/biz/process/informEmployee?type=" + copyerPositionArr[TYPE - 14];
        Response.Listener<List<EmployeeInfo>> successListener = new Response.
                Listener<List<EmployeeInfo>>() {
            @Override
            public void onResponse(List<EmployeeInfo> result) {
                if (result == null || result.size() == 0) {
                    return;
                }
                String nameStr = "";
                int size = result.size();
                for (int i = 0; i < size; i++) {
                    EmployeeInfo copyInfo = result.get(i);
                    copyerIdArr.add(copyInfo.getId());

                    String name = copyInfo.getEmployeeName();

                    if (size == 1 || i == 0) {
                        nameStr = name;
                    } else {
                        nameStr = nameStr + "," + name;
                    }
                }
                initCopyerLayout(nameStr);//抄送人
            }
        };
        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.d(TAG, "获取抄送人:" + TextUtil.getErrorMsg(volleyError));
            }
        };

        Request<List<EmployeeInfo>> request = new
                GsonRequest<List<EmployeeInfo>>(Request.Method.GET,
                        url, successListener, errorListener, new TypeToken<List<EmployeeInfo>>() {
                }.getType()) {
                    @Override
                    public Map<String, String> getHeaders() throws AuthFailureError {
                        Map<String, String> params = new HashMap<>();
                        params.put(KeyConst.Content_Type, Constant.application_json);
                        params.put(KeyConst.Authorization, KeyConst.Bearer + App.token);
                        return params;
                    }
                };
        App.requestQueue.add(request);
    }

    private int layoutIdArr[] = {R.layout.layout_14, R.layout.layout_15, R.layout.layout_16,
            R.layout.layout_17, R.layout.layout_18, R.layout.layout_19, R.layout.layout_20
    };

    private void initView() {
        operatorId = App.employeeId;
        operatorName = App.employeeName;

        getDirectorInfo();

        layoutView7 = findViewById(R.id.layout_view_7);
        View v = LayoutInflater.from(context).inflate(layoutIdArr[TYPE - 14], null);
        layoutView7.addView(v);

        layoutView1920 = v.findViewById(R.id.layout_16_19_20);
        mTimeTv = (TextView) v.findViewById(R.id.tv_time);
        remarkTv = (EditText) findViewById(R.id.remark_tv);

        choosePoolTv = v.findViewById(R.id.choose_pool_tv);//选择池号
        operatorTv = v.findViewById(R.id.operator_tv);//操作员
        operatorDirectorTv = v.findViewById(R.id.director_tv);//主管

        productNameTv = v.findViewById(R.id.product_name_tv);//产品名称
        commonProductSpecTv = v.findViewById(R.id.product_spec_tv);//产品规格
        if (productNameTv != null) {
            productNameTv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showProductNameDialog();
                }
            });
        }

        if (commonProductSpecTv != null) {
            commonProductSpecTv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showProductSpecDialog();
                }
            });
        }

        if (mTimeTv != null) {
            mTimeTv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    TimePickerDialog.Builder timePickerDialog = DialogUtils.getTimePicker(context);
                    timePickerDialog.setCallBack(new OnDateSetListener() {
                        @Override
                        public void onDateSet(TimePickerDialog timePickerView, long millseconds) {
                            if (millseconds > System.currentTimeMillis()) {
                                ToastUtil.show(context, "时间不可以大于今日");
                                return;
                            }
                            timeYmd = TimeUtils.getTimeYmd(millseconds);
                            mTimeTv.setText(timeYmd);
                        }
                    });

                    timePickerDialog.build().show(context.getSupportFragmentManager(), "");
                }
            });

        }

        switch (TYPE) {
            case 14://过磅
                CATEGORY_ID = 3;

                m14AddressEt = v.findViewById(R.id.tv_14_address);
                m14CarNumEt = v.findViewById(R.id.tv_14_car_num);//车牌号

                m14DriverNameEt = v.findViewById(R.id.tv_14_driver_name);//驾驶员
                m14DriverPhoneEt = v.findViewById(R.id.tv_14_driver_phone);

                m14WeightRoughEt = v.findViewById(R.id.tv_14_weight_rough);//毛重
                m14WeightTareEt = v.findViewById(R.id.tv_14_weight_tare);//皮重

                m14WeightDeductEt = v.findViewById(R.id.tv_14_weight_deduct);//扣杂

                m14PriceEt = v.findViewById(R.id.tv_14_price);//单价

                choose15_16_19Tv = v.findViewById(R.id.connect_in_pool_num_tv);//选择过磅单
                choose15_16_19Tv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //过磅单号
                        Intent intent = new Intent(context, PoolChooseActivity.class);
                        intent.putExtra(KeyConst.type, 4);
                        context.startActivityForResult(intent, 5);
                    }
                });
                break;
            case 15://入池
                choose15_16_19Tv = v.findViewById(R.id.weigh_number_tv);
                choose15_16_19Tv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //关联过磅单号
                        Intent intent = new Intent(context, PoolChooseActivity.class);
                        intent.putExtra(KeyConst.type, 2);
                        intent.putExtra(KeyConst.status, 3);
                        context.startActivityForResult(intent, 5);

                    }
                });

                m15CarNumerTv = v.findViewById(R.id.tv_15_car_number);//车牌号
                m15AddressTv = v.findViewById(R.id.tv_15_address);//产地

                m15CarUnloadPeopleEt = v.findViewById(R.id.tv_15_car_person);
                layout15 = v.findViewById(R.id.layout_item_15);


                addPoolBt15 = v.findViewById(R.id.add_pool_15_bt);
                if (processId != 0) {
                    addPoolBt15.setVisibility(View.GONE);
                }
                addPoolBt15.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(context, PoolChooseActivity.class);
                        intent.putExtra(KeyConst.type, 0);//池号
                        context.startActivityForResult(intent, 5);
                    }
                });

                break;
            case 16://配料入池
                creatorName16Tv = v.findViewById(R.id.tv_16_creator);
                creatorName16Tv.setText(operatorName);// 配料员
                choose15_16_19Tv = v.findViewById(R.id.pltc_tv);
                choose15_16_19Tv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(context, PoolChooseActivity.class);
                        intent.putExtra(KeyConst.type, 1);
                        context.startActivityForResult(intent, 5);
                    }
                });


                //请求配料明细列表数据
                getIngredientComponentList();
                break;
            case 17://回淋
                m17MinuteEt = v.findViewById(R.id.et_16_min_et);//回淋时长(分)
                mBaumeDegreeEt = v.findViewById(R.id.et_17_bmd);//波美度

                mTimeTv2 = v.findViewById(R.id.time_tv_2);//
                mTimeTv2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        TimePickerDialog.Builder timePickerDialog = DialogUtils.getTimePicker(context);
                        timePickerDialog.setCallBack(new OnDateSetListener() {
                            @Override
                            public void onDateSet(TimePickerDialog timePickerView, long millseconds) {
                                if (millseconds > System.currentTimeMillis()) {
                                    ToastUtil.show(context, "时间不可以大于今日");
                                    return;
                                }
                                sprinkleDate = TimeUtils.getTimeYmd(millseconds);
                                mTimeTv2.setText(sprinkleDate);
                            }
                        });

                        timePickerDialog.build().show(context.getSupportFragmentManager(), "");
                    }
                });
                mTimeTv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (TextUtil.isEmpty(CHOOSE_POOL_ID)) {
                            DialogUtils.showTipDialog(context, "请先选择池号");
                            return;
                        }
                        getSprinkleTimeList();
                    }
                });

                break;
            case 18://起池

                //感官风味确认
                objectPoolLayout = v.findViewById(R.id.merge_into_pool_layout);

                m18WhetherMergePoolTv = v.findViewById(R.id.whether_merge_into_pool_tv); //是否并池
                m18MergePoolNumTv = v.findViewById(R.id.merge_into_pool_id_tv);
                m18MergePoolNumTv.setOnClickListener(new View.OnClickListener() {//并入池号
                    @Override
                    public void onClick(View view) {
                        //并入池号
                        Intent intent = new Intent(context, PoolChooseActivity.class);
                        intent.putExtra(KeyConst.type, 0);//池号
                        context.startActivityForResult(intent, 6);
                    }
                });


                m18WhetherMergePoolTv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        new MaterialDialog.Builder(context)
                                .items(TextUtil.getObjectPoolArr())// 列表数据
                                .itemsCallback(new MaterialDialog.ListCallback() {
                                    @Override
                                    public void onSelection(MaterialDialog dialog, View itemView,
                                                            int position, CharSequence text) {
                                        objectPoolType = position + 1;
                                        //1  是   2  否
                                        objectPoolLayout.setVisibility(objectPoolType == Constant.OBJECT_POOL_TRUE ? View.VISIBLE : View.GONE);
                                        m18WhetherMergePoolTv.setText(text);
                                    }
                                })
                                .show();
                    }
                });

                m18OutPoolNumEt = v.findViewById(R.id.out_pool_num_et);//出池数量
                m18FeelAffirmTv = v.findViewById(R.id.out_feel_addirm_et);//感官风味确认
                m18FeelAffirmTv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        new MaterialDialog.Builder(context)
                                .items(new String[]{"正常", "异常"})
                                .itemsCallback(new MaterialDialog.ListCallback() {
                                    @Override
                                    public void onSelection(MaterialDialog dialog, View itemView,
                                                            int position, CharSequence text) {
                                        m18FeelAffirmTv.setText(text);
                                    }
                                })
                                .show();
                    }
                });

                break;
            case 19://原料入池检

            /*    choose15_16_19Tv = v.findViewById(R.id.weigh_number_tv);//选择过磅单
                choose15_16_19Tv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //过磅单号
                        Intent intent = new Intent(context, PoolChooseActivity.class);
                        intent.putExtra(KeyConst.type, 2);
                        intent.putExtra(KeyConst.status, 1);
                        context.startActivityForResult(intent, 5);

                    }
                });*/
                CATEGORY_ID = 3;
                m15_19CarNumerTv = v.findViewById(R.id.tv_19_car_num);

                mTimeTv2 = v.findViewById(R.id.time_tv_2);//入货时间
                mTimeTv2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        TimePickerDialog.Builder timePickerDialog = DialogUtils.getTimePicker(context);
                        timePickerDialog.setCallBack(new OnDateSetListener() {
                            @Override
                            public void onDateSet(TimePickerDialog timePickerView, long millseconds) {
                                if (millseconds > System.currentTimeMillis()) {
                                    ToastUtil.show(context, "时间不可以大于今日");
                                    return;
                                }
                                goodsArriveDate = TimeUtils.getTimeYmd(millseconds);
                                mTimeTv2.setText(goodsArriveDate);
                            }
                        });

                        timePickerDialog.build().show(context.getSupportFragmentManager(), "");
                    }
                });

                m19BasePlaceEt = v.findViewById(R.id.base_place_et);//出池数量
                m19FinalJudgeEt = v.findViewById(R.id.final_judge_et);//出池数量

                break;
            case 20://半成品检验
                break;
        }

        if (choosePoolTv != null) {
            choosePoolTv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //选择池号
                    Intent intent = new Intent(context, PoolChooseActivity.class);
                    intent.putExtra(KeyConst.type, 0);//池号
                    context.startActivityForResult(intent, 5);
                }
            });
        }
        if (operatorTv != null) {
            //查询主管信息
            operatorTv.setText(operatorName);
            operatorTv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showEmplyeeListDialog(11);
                }
            });
        }
        getProductNameList();//产品名称
        getProductSpecList();//产品规格
    }

    List<String> timeInfoList = new ArrayList<>();

    private void getSprinkleTimeList() {
        String url = Constant.WEB_SITE + "/biz/process/rawPutInPool/list?poolId=" + CHOOSE_POOL_ID;
        DialogHelper.showWaiting(fm, getString(R.string.get_data_ing));
        Response.Listener<List<PoolInfo>> successListener = new Response
                .Listener<List<PoolInfo>>() {
            @Override
            public void onResponse(List<PoolInfo> infoList) {
                timeInfoList.clear();
                if (infoList == null) {
                    ToastUtil.show(context, "暂无腌渍时间");
                } else {
                    for (PoolInfo i : infoList) {
                        PoolInfo.ObjectBean info = i.getObject();
                        String timeInfo = info.getPutInPoolDate() +
                                //腌渍时间+产品名称规格+""入池数量
                                "\n(" + info.getProductName() + "/" + info.getProductSpecification()
                                + "  入池数量:" +
                                TextUtil.remove_0(info.getAmount()) + ")";
                        timeInfoList.add(timeInfo);
                    }
                    showSprinkleTimeDialog();
                }
                DialogHelper.hideWaiting(fm);
            }
        };

        Request<List<PoolInfo>> versionRequest = new
                GsonRequest<List<PoolInfo>>(Request.Method.GET, url,
                        successListener, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        DialogHelper.hideWaiting(fm);
                        ToastUtil.show(context, R.string.server_exception);
                    }
                }, new TypeToken<List<PoolInfo>>() {
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

    //腌渍时间
    private void showSprinkleTimeDialog() {
        new MaterialDialog.Builder(context)
                .items(timeInfoList)
                .itemsCallback(new MaterialDialog.ListCallback() {
                    @Override
                    public void onSelection(MaterialDialog dialog, View itemView,
                                            int position, CharSequence text) {
                        if (text != null && text.length() > 10) {
                            timeYmd = text.subSequence(0, 10) + "";
                            mTimeTv.setText(text);
                        }
                    }
                })
                .show();
    }

    private void getIngredientComponentList() {//配料明细列表
        String url = Constant.WEB_SITE + "/biz/ingredientComponentItem/list";
        Response.Listener<List<PoolInfo.ComponentItemListBean>> successListener = new Response
                .Listener<List<PoolInfo.ComponentItemListBean>>() {
            @Override
            public void onResponse(List<PoolInfo.ComponentItemListBean> result) {
                componentItemList = result;
                notifyItemView16();
            }
        };

        Request<List<PoolInfo.ComponentItemListBean>> versionRequest = new
                GsonRequest<List<PoolInfo.ComponentItemListBean>>(Request.Method.GET, url,
                        successListener, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                    }
                }, new TypeToken<List<PoolInfo.ComponentItemListBean>>() {
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
        String url = Constant.WEB_SITE + "/biz/process/";
        String urlDraft = url;
        String draft_path = "draft/";
        Map<String, Object> map = new HashMap<>();
        remark = remarkTv.getText().toString();

        JSONArray checkItemList = new JSONArray();
        if (this.checkItemInfoList != null) {
            for (ProductInfo info : this.checkItemInfoList) {
                JSONObject resultObj = new JSONObject();
                try {
                    String name = info.getName();
                    String result = info.getResult();
                    String determination = info.getDetermination();
                    if (ToastUtil.showCannotEmpty(context, result, name + "检测结果")) {
                        return;

                    }
                    if (ToastUtil.showCannotEmpty(context, determination, name + "判定")) {
                        return;
                    }

                    String type = info.getType();
                    JSONObject standardObj = new JSONObject();
                    ProductInfo.StandardBean standard = info.getStandard();
                    if (standard != null) {
                        if ("2".equals(info.getType())) {//百分比 %
                            int startPoint = standard.getStartPoint();
                            int endPoint = standard.getEndPoint();
                            standardObj.put(KeyConst.startPoint, startPoint);
                            standardObj.put(KeyConst.endPoint, endPoint);
                        } else {//只有1种
                            standardObj.put(KeyConst.remark, standard.getRemark());
                        }

                    }

                    resultObj.put(KeyConst.id, info.getId());
                    resultObj.put(KeyConst.name, name);
                    resultObj.put(KeyConst.type, type);
                    resultObj.put(KeyConst.canDelete, info.getCanDelete());
                    resultObj.put(KeyConst.result, result);
                    resultObj.put(KeyConst.determination, determination);
                    resultObj.put(KeyConst.standard, standardObj);

                    checkItemList.put(resultObj);
                } catch (Exception e) {
                    Log.d(TAG, "list解析异常:");
                    return;
                }

            }

        }
        String numberPlate;
        switch (TYPE) {
            case 14://过磅
                url = url + "weigh";
                draft_path = draft_path + Constant.WEIGH;

                String originPlace = m14AddressEt.getText().toString();
                numberPlate = m14CarNumEt.getText().toString();
                String driverName = m14DriverNameEt.getText().toString();
                String driverPhone = m14DriverPhoneEt.getText().toString();

                if (ToastUtil.showCannotEmpty(context, commonProductName, "产品名称")) {
                    return;
                }
                if (ToastUtil.showCannotEmpty(context, originPlace, "产地")) {
                    return;
                }
                if (ToastUtil.showCannotEmpty(context, numberPlate, "车牌号")) {
                    return;
                }
                if (ToastUtil.showCannotEmpty(context, timeYmd, "过磅时间")) {
                    return;
                }
                if (ToastUtil.showCannotEmpty(context, CHOOSED_15_16_19_ID, "原料入池检验单")) {
                    return;
                }
                String grossWeight = m14WeightRoughEt.getText().toString();//毛重
                String tareWeight = m14WeightTareEt.getText().toString();//皮重

                String price = m14PriceEt.getText().toString();//单价


                if (ToastUtil.showCannotEmpty(context, grossWeight, "毛重")) {
                    return;
                }
                if (ToastUtil.showCannotEmpty(context, tareWeight, "皮重")) {
                    return;
                }

                if (TextUtil.isEmpty(price)) {
                    price = "0";
                }

                map.put(KeyConst.productId, commonProductId);
                map.put(KeyConst.originPlace, originPlace);
                map.put(KeyConst.numberPlate, numberPlate);

                map.put(KeyConst.driverName, driverName);
                map.put(KeyConst.driverPhone, driverPhone);
                map.put(KeyConst.price, price);

                map.put(KeyConst.grossWeight, grossWeight);//毛重
                map.put(KeyConst.tareWeight, tareWeight);//皮重

                //map.put(KeyConst.deductImpurity, deductImpurity);

                map.put(KeyConst.weighDate, timeYmd);
                map.put(KeyConst.processId, CHOOSED_15_16_19_ID);//关联原料入池检验单

                break;
            case 15://入池
                url = url + "rawPutInPool";
                draft_path = draft_path + Constant.RAW_PUT_IN_POOL;
                String discharger = m15CarUnloadPeopleEt.getText().toString();//卸车负责人

                if (ToastUtil.showCannotEmpty(context, CHOOSED_15_16_19_ID, "过磅单号")) {
                    return;
                }
                if (ToastUtil.showCannotEmpty(context, commonProductId, "产品名称")) {
                    return;
                }
                if (ToastUtil.showCannotEmpty(context, commonProductSpecId, "产品规格")) {
                    return;
                }

                if (poolInfoList != null && poolInfoList.size() > 0) {
                    if (processId > 0) {
                        PoolInfo info = poolInfoList.get(0);
                        map.put(KeyConst.producePoolId, info.getId());
                        map.put(KeyConst.putInPoolDate, info.getPutInPoolDate());
                        map.put(KeyConst.amount, info.getAmount());
                    } else {
                        JSONArray detailArr = new JSONArray();
                        for (PoolInfo reimburseInfo : poolInfoList) {
                            JSONObject resultObj = new JSONObject();
                            try {
                                String producePoolId = reimburseInfo.getId();
                                String name = reimburseInfo.getName();

                                String putInPoolDate = reimburseInfo.getPutInPoolDate();
                                String amount = reimburseInfo.getAmount();

                                if (TextUtil.isEmpty(putInPoolDate)) {
                                    ToastUtil.show(context, name + "-入池时间不能为空");
                                    return;
                                }
                                if (TextUtil.isEmpty(amount)) {
                                    ToastUtil.show(context, name + "-入池数量不能为空");
                                    return;
                                }
                                resultObj.put(KeyConst.producePoolId, producePoolId);
                                resultObj.put(KeyConst.putInPoolDate, putInPoolDate);
                                resultObj.put(KeyConst.amount, amount);

                                detailArr.put(resultObj);
                            } catch (Exception e) {
                                Log.d(TAG, "出差list解析异常:");
                                return;
                            }

                        }

                        map.put(KeyConst.rawPutInPoolDetailModelList, detailArr);
                    }
                } else {
                    ToastUtil.show(context, "至少要添加一个入池");
                    return;
                }

                map.put(KeyConst.processId, CHOOSED_15_16_19_ID);//过磅单号

                map.put(KeyConst.productId, commonProductId);//名称
                map.put(KeyConst.productDetailId, commonProductSpecId);//规格
                map.put(KeyConst.discharger, discharger);
                break;
            case 16://配料入池
                url = url + "ingredientPutInPool";
                draft_path = draft_path + Constant.INGREDIENT_PUT_IN_POOL;

              /*  if (ToastUtil.showCannotEmpty(context, CHOOSED_15_16_19_ID, "配料桶次")) {
                    return;
                }*/
                if (ToastUtil.showCannotEmpty(context, CHOOSE_POOL_ID, "池号")) {
                    return;
                }
                if (ToastUtil.showCannotEmpty(context, timeYmd, "配料入池时间")) {
                    return;
                }
                if (ToastUtil.showCannotEmpty(context, commonProductId, "产品名称")) {
                    return;
                }
                if (ToastUtil.showCannotEmpty(context, commonProductSpecId, "产品规格")) {
                    return;
                }

                map.put(KeyConst.producePoolId, CHOOSE_POOL_ID);//池号
                map.put(KeyConst.putInPoolDate, timeYmd);//配料入池时间

                map.put(KeyConst.productId, commonProductId);//名称
                map.put(KeyConst.productDetailId, commonProductSpecId);//规格

                map.put(KeyConst.ingredientor, operatorName);//配料员

                //配料桶次列表
                if (componentItemList != null && componentItemList.size() > 0) {
                    JSONArray detailArr = new JSONArray();
                    for (PoolInfo.ComponentItemListBean info : componentItemList) {
                        JSONObject resultObj = new JSONObject();

                        try {
                            String id = info.getIdX();
                            String name = info.getName();

                            String amount = info.getAmount();
                            String manufactureDate = info.getManufactureDate();
                            String manufacturer = info.getManufacturer();
                            if (TextUtil.isEmpty(amount)) {
                                ToastUtil.show(context, name + "-使用量不能为空");
                                return;
                            }
                            if (TextUtil.isEmpty(manufactureDate)) {
                                ToastUtil.show(context, name + "-生产日期不能为空");
                                return;
                            }

                            if (TextUtil.isEmpty(manufacturer)) {
                                ToastUtil.show(context, name + "-生产厂家不能为空");
                                return;
                            }
                            resultObj.put(KeyConst.id, id);
                            resultObj.put(KeyConst.name, name);
                            resultObj.put(KeyConst.canDelete, info.getCanDelete());
                            resultObj.put(KeyConst.amount, amount);
                            resultObj.put(KeyConst.manufactureDate, manufactureDate);
                            resultObj.put(KeyConst.manufacturer, manufacturer);

                            detailArr.put(resultObj);
                        } catch (Exception e) {
                            Log.d(TAG, "出差list解析异常:");
                            return;
                        }

                    }

                    map.put(KeyConst.componentItemList, detailArr);
                } else {
                    map.put(KeyConst.componentItemList, null);
                }
                break;
            case 17://回淋
                url = url + "sprinkle";
                draft_path = draft_path + Constant.SPRINKLE;

                if (ToastUtil.showCannotEmpty(context, CHOOSE_POOL_ID, "池号")) {
                    return;
                }
                if (ToastUtil.showCannotEmpty(context, commonProductId, "产品名称")) {
                    return;
                }
                if (ToastUtil.showCannotEmpty(context, commonProductSpecId, "产品规格")) {
                    return;
                }
                if (ToastUtil.showCannotEmpty(context, timeYmd, "腌渍时间")) {
                    return;
                }
                if (ToastUtil.showCannotEmpty(context, sprinkleDate, "回淋时间")) {
                    return;
                }

                String period = m17MinuteEt.getText().toString();//回淋时长
                if (ToastUtil.showCannotEmpty(context, period, "回淋时长")) {
                    return;
                }
                String baumeDegree = mBaumeDegreeEt.getText().toString();//波美度
                if (ToastUtil.showCannotEmpty(context, baumeDegree, "波美度")) {
                    return;
                }

                if (operatorId == 0) {
                    ToastUtil.show(context, "请选择操作员");
                    return;
                }
                map.put(KeyConst.producePoolId, CHOOSE_POOL_ID);//池号

                map.put(KeyConst.productId, commonProductId);//名称
                map.put(KeyConst.productDetailId, commonProductSpecId);//规格

                map.put(KeyConst.sprinkleDate, sprinkleDate);//回淋时间
                map.put(KeyConst.pickleDate, timeYmd);//腌渍时间
                map.put(KeyConst.period, period);//回淋时长
                map.put(KeyConst.baumeDegree, baumeDegree);//波美度
                map.put(KeyConst.operator, operatorId);//操作员

                break;
            case 18://起池
                url = url + "takeOutPool";
                draft_path = draft_path + Constant.TAKE_OUT_POOL;
                if (ToastUtil.showCannotEmpty(context, CHOOSE_POOL_ID, "池号")) {
                    return;
                }
                if (ToastUtil.showCannotEmpty(context, commonProductId, "产品名称")) {
                    return;
                }
                if (ToastUtil.showCannotEmpty(context, commonProductSpecId, "产品规格")) {
                    return;
                }
                if (ToastUtil.showCannotEmpty(context, timeYmd, "出池时间")) {
                    return;
                }

                String amountOut = m18OutPoolNumEt.getText().toString();//出池数量
                String confirmTaste = m18FeelAffirmTv.getText().toString();//感官确认
                if (ToastUtil.showCannotEmpty(context, amountOut, "出池数量")) {
                    return;
                }
                if (objectPoolType == 0) {
                    ToastUtil.show(context, "请选择是否并池");
                    return;
                } else if (objectPoolType == Constant.OBJECT_POOL_TRUE) {
                    if (ToastUtil.showCannotEmpty(context, OBJECT_POOL_ID, "并入池号")) {
                        return;
                    }
                    if (CHOOSE_POOL_ID.equals(OBJECT_POOL_ID)) {
                        ToastUtil.show(context, "并入池号与起池号不能相同");
                        return;
                    }
                }

                if (ToastUtil.showCannotEmpty(context, confirmTaste, "感官风味确认")) {
                    return;
                }

                if (operatorId == 0) {
                    ToastUtil.show(context, "请选择操作员");
                    return;
                }

                map.put(KeyConst.productId, commonProductId);//名称
                map.put(KeyConst.productDetailId, commonProductSpecId);//规格

                map.put(KeyConst.producePoolId, CHOOSE_POOL_ID);//池号
                map.put(KeyConst.takeOutPoolDate, timeYmd);//出池时间
                map.put(KeyConst.amount, amountOut);//出池数量

                map.put(KeyConst.type, objectPoolType);//是否并池
                map.put(KeyConst.objectPoolId, OBJECT_POOL_ID);//并入池号

                map.put(KeyConst.confirmTaste, confirmTaste);
                map.put(KeyConst.operator, operatorId);//操作员

                break;
            case 19://原料入池检验
                url = url + "rawMaterialCheck";
                draft_path = draft_path + Constant.RAW_MATERIAL_CHECK;

                numberPlate = m15_19CarNumerTv.getText().toString();

                if (ToastUtil.showCannotEmpty(context, commonProductName, "产品名称")) {
                    return;
                }

                if (ToastUtil.showCannotEmpty(context, numberPlate, "车牌号")) {
                    return;
                }

                if (ToastUtil.showCannotEmpty(context, goodsArriveDate, "入货时间")) {
                    return;
                }
                if (ToastUtil.showCannotEmpty(context, timeYmd, "检验时间")) {
                    return;
                }
                String baseLocate = m19BasePlaceEt.getText().toString();
                if (ToastUtil.showCannotEmpty(context, baseLocate, "基地")) {
                    return;
                }

                String comprehensiveDeter = m19FinalJudgeEt.getText().toString();
                if (ToastUtil.showCannotEmpty(context, comprehensiveDeter, "综合判定")) {
                    return;
                }

                map.put(KeyConst.productId, commonProductId);//产品名称id
                map.put(KeyConst.numberPlate, numberPlate);//车牌号

                map.put(KeyConst.goodsArriveDate, goodsArriveDate);//入货时间
                map.put(KeyConst.checkDate, timeYmd);//检验时间
                map.put(KeyConst.baseLocate, baseLocate);//基地
                map.put(KeyConst.comprehensiveDetermination, comprehensiveDeter);//综合判定

                map.put(KeyConst.checkItemList, checkItemList);//检验明细列表

                break;
            case 20://半成品检验
                url = url + "semifinishedProductCheck";
                draft_path = draft_path + Constant.SEMIFINISHED_PRODUCT_CHECK;

                if (ToastUtil.showCannotEmpty(context, CHOOSE_POOL_ID, "池号")) {
                    return;
                }
                if (ToastUtil.showCannotEmpty(context, commonProductId, "产品名称")) {
                    return;
                }
                if (ToastUtil.showCannotEmpty(context, commonProductSpecId, "产品规格")) {
                    return;
                }
                if (ToastUtil.showCannotEmpty(context, timeYmd, "检验时间")) {
                    return;
                }
                map.put(KeyConst.producePoolId, CHOOSE_POOL_ID);//池号
                map.put(KeyConst.productId, commonProductId);//名称
                map.put(KeyConst.productDetailId, commonProductSpecId);//规格
                map.put(KeyConst.checkDate, timeYmd);//入货时间

                map.put(KeyConst.checkItemList, checkItemList);//检验明细列表

                break;
        }

        //抄送人
        if (copyerIdArr != null && copyerIdArr.size() > 0 && !copyerIdArr.isJsonNull()) {
            JSONArray informList = new JSONArray();
            for (JsonElement element : copyerIdArr) {
                informList.put(element.getAsInt());
            }
            map.put(KeyConst.informList, informList);
        } else {
            ToastUtil.show(context, "抄送人" + getString(R.string.cannot_empty));
            return;
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
        //map.put(KeyConst.status, status);

        int postType = Request.Method.POST;
        if (processId > 0) {
            map.put(KeyConst.id, processId);
            postType = Request.Method.PUT;
            url = urlDraft + draft_path;
        }
        JSONObject jsonObject = new JSONObject(map);
        Log.d(TAG, "流程提交:" + jsonObject.toString());
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
                            ToastUtil.show(context, "提交失败");
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                DialogHelper.hideWaiting(fm);
                if (TYPE == 15) {
                    if (error != null && error.toString().contains("End of input at character 0 of")) {
                        ToastUtil.show(context, "提交成功");
                        context.finish();
                        return;
                    }
                }
                if (!TextUtil.isEmpty(TextUtil.getErrorMsg(error))) {
                    try {
                        JSONObject obj = new JSONObject(TextUtil.getErrorMsg(error));
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

    private void getProductNameList() {
        if (!NetUtil.isNetworkConnected(context)) {
            ToastUtil.show(context, getString(R.string.no_network));
            return;
        }
        String url = Constant.WEB_SITE + "/biz/product/list?categoryId=" + CATEGORY_ID;
        Response.Listener<List<ProductInfo>> successListener = new Response
                .Listener<List<ProductInfo>>() {
            @Override
            public void onResponse(List<ProductInfo> result) {
                productNameInfoList = result;
            }
        };

        Request<List<ProductInfo>> versionRequest = new
                GsonRequest<List<ProductInfo>>(
                        Request.Method.GET, url,
                        successListener, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        ToastUtil.show(context, getString(R.string.get_data_faild));
                    }
                }, new TypeToken<List<ProductInfo>>() {
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

    private void getProductSpecList() {
        if (!NetUtil.isNetworkConnected(context)) {
            ToastUtil.show(context, getString(R.string.no_network));
            return;
        }
        String url = Constant.WEB_SITE + "/biz/product/detail/list";
        Response.Listener<List<ProductInfo>> successListener = new Response
                .Listener<List<ProductInfo>>() {
            @Override
            public void onResponse(List<ProductInfo> result) {
                productSpecInfoList = result;
            }
        };

        Request<List<ProductInfo>> versionRequest = new
                GsonRequest<List<ProductInfo>>(
                        Request.Method.GET, url,
                        successListener, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        ToastUtil.show(context, getString(R.string.get_data_faild));
                    }
                }, new TypeToken<List<ProductInfo>>() {
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


    private List<DeptInfo> deptInfos;

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


    // 配料
    private void notifyItemView16() {
        if (componentItemList == null || layoutView1920 == null) {
            return;
        }
        layoutView1920.removeAllViews();
        for (int i = 0; i < componentItemList.size(); i++) {
            int finalI = i;
            PoolInfo.ComponentItemListBean info = componentItemList.get(i);
            View itemView = View.inflate(context, R.layout.layout_16_item, null);
            ((TextView) itemView.findViewById(R.id.title_tv)).setText(info.getName());

            TextView timeTv = itemView.findViewById(R.id.value_tv_2);
            timeTv.setText(info.getManufactureDate());//生产日期
            timeTv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    TimePickerDialog.Builder timePickerDialog = DialogUtils.getTimePicker(context);
                    timePickerDialog.setCallBack(new OnDateSetListener() {
                        @Override
                        public void onDateSet(TimePickerDialog timePickerView, long millseconds) {
                            if (millseconds > System.currentTimeMillis()) {
                                ToastUtil.show(context, "时间不可以大于今日");
                                return;
                            }

                            String manufactureDate = TimeUtils.getTimeYmd(millseconds);
                            componentItemList.get(finalI).setManufactureDate(manufactureDate);
                            timeTv.setText(manufactureDate);
                        }
                    });

                    timePickerDialog.build().show(context.getSupportFragmentManager(), "");
                }
            });

            EditText usedNumtV = itemView.findViewById(R.id.value_tv_1);
            usedNumtV.setText(info.getAmount());//使用量
            usedNumtV.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                }

                @Override
                public void onTextChanged(CharSequence s, int i, int i1, int i2) {
                    componentItemList.get(finalI).setAmount(s + "");
                }

                @Override
                public void afterTextChanged(Editable editable) {

                }
            });

            EditText manufacturerEt = itemView.findViewById(R.id.value_tv_3);
            manufacturerEt.setText(info.getManufacturer());//厂商
            manufacturerEt.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    componentItemList.get(finalI).setManufacturer(charSequence + "");
                }

                @Override
                public void afterTextChanged(Editable editable) {
                }
            });
            layoutView1920.addView(itemView);
        }
    }

    private void getItem1920List() {
        String url = Constant.WEB_SITE + "/biz";
        if (TYPE == 19) {//原料入池检验项
            url = url + "/rawMaterialCheckItem/list";
        } else if (TYPE == 20) {//半成品
            url = url + "/semifinishedProductCheckItem/list";
        }
        Response.Listener<List<ProductInfo>> successListener = new Response
                .Listener<List<ProductInfo>>() {
            @Override
            public void onResponse(List<ProductInfo> result) {
                if (result == null) {
                    return;
                }
                checkItemInfoList = result;
                notifyItemView1920();
            }
        };

        Request<List<ProductInfo>> versionRequest = new
                GsonRequest<List<ProductInfo>>(
                        Request.Method.GET, url,
                        successListener, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        ToastUtil.show(context, getString(R.string.get_data_faild));
                    }
                }, new TypeToken<List<ProductInfo>>() {
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

    private void notifyItemView1920() {
        layoutView1920.removeAllViews();
        for (int i = 0; i < checkItemInfoList.size(); i++) {
            ProductInfo info = checkItemInfoList.get(i);
            View itemView = View.inflate(context, R.layout.layout_19_20_item, null);

            TextView nameTv = (TextView) itemView.findViewById(R.id.title_tv);
            TextView nameTagTv = (TextView) itemView.findViewById(R.id.title_norm_tv);

            EditText resultEt = itemView.findViewById(R.id.check_item_determination_et);//检测结果
            TextView determinationTv = (TextView) itemView.findViewById(R.id.check_item_result_tv);//判定

            nameTv.setText(info.getName());

            String standardStr = "";
            ProductInfo.StandardBean standard = info.getStandard();
            if (standard != null) {
                if ("2".equals(info.getType())) {//百分比 %
                    int startPoint = standard.getStartPoint();
                    int endPoint = standard.getEndPoint();
                    standardStr = startPoint + "~" + endPoint;

                    resultEt.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
                    InputFilter[] filters = {new MoneyInputFilter(), new InputFilter.LengthFilter(5)};
                    resultEt.setFilters(filters);
                } else {//只有1种
                    standardStr = standard.getRemark();
                }

            }
            nameTagTv.setText("(标准:" + standardStr + ")");
            int finalI = i;
            resultEt.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                }

                @Override
                public void afterTextChanged(Editable editable) {
                    if (finalI < checkItemInfoList.size()) {
                        checkItemInfoList.get(finalI).setResult(resultEt.getText().toString());
                    }

                }
            });
            determinationTv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    new MaterialDialog.Builder(context)
                            .items(resultTypeArr)
                            .itemsCallback(new MaterialDialog.ListCallback() {
                                @Override
                                public void onSelection(MaterialDialog dialog, View itemView,
                                                        int position, CharSequence text) {
                                    //1  合格   2  不合格
                                    determinationTv.setText(text);
                                    if (finalI < checkItemInfoList.size()) {
                                        checkItemInfoList.get(finalI).setDetermination(text + "");
                                    }

                                }
                            })
                            .show();
                }
            });

            //必须放在最后(草稿箱里进来--更新数据集合里的数据)
            resultEt.setText(info.getResult());
            determinationTv.setText(info.getDetermination());

            layoutView1920.addView(itemView);
        }
    }

    //添加抄送人
    private void initCopyer() {
        copyerItemsLayout = (LinearLayout) findViewById(R.id.copyer_layout);
        ImageView informPeopleAddBt = (ImageView) findViewById(R.id.inform_people_add_bt);
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
    }

    private List<EmployeeInfo> parentList = new ArrayList<>();

    //审批流程
    private void setAuditLogView(List<HistoryInfo> result) {
        LinearLayout auditProcessLayout = (LinearLayout) findViewById(R.id.approval_process_layout);
        auditProcessLayout.removeAllViews();

        int size = result.size();
        for (int i = 0; i < size; i++) {
            HistoryInfo itemInfo = result.get(i);
            View itemView = View.inflate(context, R.layout.layout_item_approval_process, null);
            TextView titleTv = (TextView) itemView.findViewById(R.id.item_title_tv);
            TextView descTv = (TextView) itemView.findViewById(R.id.item_desc_tv);
            titleTv.setText(itemInfo.getTaskName());
            if (i == 0) {
                descTv.setText("第一级主管");
            }
            if (0 == i) {
                itemView.findViewById(R.id.item_top_line).setVisibility(View.INVISIBLE);
            }
            if (i == size - 1) {
                itemView.findViewById(R.id.item_bottom_line).setVisibility(View.INVISIBLE);
            }

            LinearLayout peopleLayout = itemView.findViewById(R.id.copyer_item_layout);
            setCiycleItemView(peopleLayout, itemInfo.getEmployeeName());
            //产值
            auditProcessLayout.addView(itemView);
        }
    }

    //审批人员
    private void setCiycleItemView(LinearLayout peopleLayout, String employeeName) {
        peopleLayout.removeAllViews();
        int pxSize = getResources().getDimensionPixelSize(R.dimen.dm055);
        View itemView = View.inflate(context, R.layout.item_tv_iv, null);
        TextView ciycleIv = itemView.findViewById(R.id.ciycle_bg_iv);
        TextView ciycleTv = (TextView) itemView.findViewById(R.id.ciycle_tv);
        ViewGroup.LayoutParams lp = ciycleIv.getLayoutParams();
        lp.width = pxSize;
        lp.height = pxSize;
        ciycleIv.setLayoutParams(lp);
        ciycleIv.setText(TextUtil.getLast2(employeeName));//操作人

        ciycleTv.setText(employeeName);
        //产值
        peopleLayout.addView(itemView);
    }

    private void getDraftData() {
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
                //抄送人
                if (info.informList != null && !info.informList.isJsonNull()) {
                    copyerIdArr = info.informList.getAsJsonArray();
                }
                String informListName = info.informNameList;
                initCopyerLayout(informListName);

                infoObj = info.object;
                if (null == infoObj || infoObj.isJsonNull()) {
                    return;
                }
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
        //总金额
        remarkTv.setText(Utils.getObjStr(infoObj, KeyConst.remark));

        if (productNameTv != null) {
            commonProductId = Utils.getObjStr(infoObj, KeyConst.productId);
            commonProductName = Utils.getObjStr(infoObj, KeyConst.productName);
            productNameTv.setText(commonProductName);
        }

        if (commonProductSpecTv != null) {
            commonProductSpecId = Utils.getObjStr(infoObj, KeyConst.productDetailId);
            commonProductSpec = Utils.getObjStr(infoObj, KeyConst.productSpecification);
            commonProductSpecTv.setText(commonProductSpec);
        }

        switch (TYPE) {
            case 14://过磅
                m14AddressEt.setText(Utils.getObjStr(infoObj, KeyConst.originPlace));
                m14CarNumEt.setText(Utils.getObjStr(infoObj, KeyConst.numberPlate));
                m14DriverNameEt.setText(Utils.getObjStr(infoObj, KeyConst.driverName));
                m14DriverPhoneEt.setText(Utils.getObjStr(infoObj, KeyConst.driverPhone));

                m14WeightRoughEt.setText(Utils.getObjStr(infoObj, KeyConst.grossWeight));
                m14WeightTareEt.setText(Utils.getObjStr(infoObj, KeyConst.tareWeight));
                m14WeightDeductEt.setText(Utils.getObjStr(infoObj, KeyConst.deductImpurity));
                m14PriceEt.setText(Utils.getObjStr(infoObj, KeyConst.price));

                timeYmd = Utils.getObjStr(infoObj, KeyConst.weighDate);
                mTimeTv.setText(timeYmd);

                CHOOSED_15_16_19_ID = Utils.getObjStr(infoObj, KeyConst.processId);
                choose15_16_19Tv.setText(Utils.getObjStr(infoObj, KeyConst.processNum));
                ;
                break;
            case 15://入池

                CHOOSED_15_16_19_ID = Utils.getObjStr(infoObj, KeyConst.processId);
                CHOOSE_POOL_ID = Utils.getObjStr(infoObj, KeyConst.producePoolId);


                choose15_16_19Tv.setText(Utils.getObjStr(infoObj, KeyConst.weighProcNum));

                detailEle = infoObj.get(KeyConst.weighVar);
                if (detailEle != null && !detailEle.isJsonNull()) {
                    JsonObject detailObj = detailEle.getAsJsonObject();
                    //车牌号,产地
                    m15CarNumerTv.setText(Utils.getObjStr(detailObj, KeyConst.numberPlate));
                    m15AddressTv.setText(Utils.getObjStr(detailObj, KeyConst.originPlace));
                }

                m15CarUnloadPeopleEt.setText(Utils.getObjStr(infoObj, KeyConst.discharger));


                //多个入池
                PoolInfo i = new PoolInfo();
                i.setId(Utils.getObjStr(infoObj, KeyConst.producePoolId));
                i.setName(Utils.getObjStr(infoObj, KeyConst.poolName));
                i.setPutInPoolDate(Utils.getObjStr(infoObj, KeyConst.putInPoolDate));
                i.setAmount(Utils.getObjStr(infoObj, KeyConst.amount));
                poolInfoList.add(i);

                notifyPoolItemList();

                break;
            case 16://配料入池
                CHOOSED_15_16_19_ID = Utils.getObjStr(infoObj, KeyConst.ingredientId);
                CHOOSE_POOL_ID = Utils.getObjStr(infoObj, KeyConst.producePoolId);
                choosePoolTv.setText(Utils.getObjStr(infoObj, KeyConst.poolName));

                timeYmd = Utils.getObjStr(infoObj, KeyConst.putInPoolDate);
                mTimeTv.setText(timeYmd);

                //草稿箱进来,配料明细列表
                JsonElement arr2 = infoObj.get(KeyConst.componentItemList);
                if (arr2 == null || arr2.isJsonNull()) {
                    return;
                }
                JsonArray detailArr2 = arr2.getAsJsonArray();
                if (detailArr2 == null || detailArr2.isJsonNull()) {
                    return;
                }
                componentItemList = new Gson().fromJson(detailArr2,
                        new TypeToken<List<PoolInfo.ComponentItemListBean>>() {
                        }.getType());

                notifyItemView16();

                break;
            case 17://回淋
                CHOOSE_POOL_ID = Utils.getObjStr(infoObj, KeyConst.producePoolId);
                choosePoolTv.setText(Utils.getObjStr(infoObj, KeyConst.poolName));

                timeYmd = Utils.getObjStr(infoObj, KeyConst.pickleDate);
                mTimeTv.setText(timeYmd);//腌渍时间

                sprinkleDate = Utils.getObjStr(infoObj, KeyConst.sprinkleDate);
                mTimeTv2.setText(sprinkleDate);

                m17MinuteEt.setText(Utils.getObjStr(infoObj, KeyConst.period));

                mBaumeDegreeEt.setText(Utils.getObjStr(infoObj, KeyConst.baumeDegree));

                operatorId = info.applicant;
                operatorTv.setText(info.applicantName);
                operatorDirectorTv.setText(info.directorNameOfApplicant);

                break;
            case 18://起池
                CHOOSE_POOL_ID = Utils.getObjStr(infoObj, KeyConst.producePoolId);
                choosePoolTv.setText(Utils.getObjStr(infoObj, KeyConst.poolName));

                timeYmd = Utils.getObjStr(infoObj, KeyConst.takeOutPoolDate);
                mTimeTv.setText(timeYmd);//出池时间
                m18OutPoolNumEt.setText(Utils.getObjStr(infoObj, KeyConst.amount));//出池数量

                objectPoolType = Integer.valueOf(Utils.getObjDouble(infoObj, KeyConst.type));//是否并池 1:否   2: 是

                m18WhetherMergePoolTv.setText(TextUtil.getObjectPoolStr(objectPoolType));
                if (objectPoolType == Constant.OBJECT_POOL_TRUE) { //并池
                    objectPoolLayout.setVisibility(View.VISIBLE);

                    OBJECT_POOL_ID = Utils.getObjStr(infoObj, KeyConst.objectPoolId);
                    m18MergePoolNumTv.setText(Utils.getObjStr(infoObj, KeyConst.objectPoolName));
                }


                m18FeelAffirmTv.setText(Utils.getObjStr(infoObj, KeyConst.confirmTaste));//感官确认

                operatorId = info.applicant;
                operatorTv.setText(info.applicantName);
                operatorDirectorTv.setText(info.directorNameOfApplicant);

                break;
            case 19://原料入池
                CHOOSED_15_16_19_ID = Utils.getObjStr(infoObj, KeyConst.processId);

                m15_19CarNumerTv.setText(Utils.getObjStr(infoObj, KeyConst.numberPlate));

                timeYmd = Utils.getObjStr(infoObj, KeyConst.checkDate);
                mTimeTv.setText(timeYmd);

                goodsArriveDate = Utils.getObjStr(infoObj, KeyConst.goodsArriveDate);
                mTimeTv2.setText(goodsArriveDate);

                m19BasePlaceEt.setText(Utils.getObjStr(infoObj, KeyConst.baseLocate));
                m19FinalJudgeEt.setText(Utils.getObjStr(infoObj, KeyConst.comprehensiveDetermination));

                break;
            case 20:
                //半成品检验 草稿
                CHOOSE_POOL_ID = Utils.getObjStr(infoObj, KeyConst.producePoolId);
                choosePoolTv.setText(Utils.getObjStr(infoObj, KeyConst.poolName));

                timeYmd = Utils.getObjStr(infoObj, KeyConst.checkDate);
                mTimeTv.setText(timeYmd);

                break;
        }

        if (TYPE == 19 || TYPE == 20) {
            JsonElement objArr = infoObj.get(KeyConst.checkItemList);
            if (objArr == null || objArr.isJsonNull()) {
                return;
            }
            JsonArray detailArr = objArr.getAsJsonArray();
            if (detailArr == null || detailArr.isJsonNull()) {
                getItem1920List();
                return;
            }
            checkItemInfoList = new Gson().fromJson(detailArr, new TypeToken<List<ProductInfo>>() {
            }.getType());

            notifyItemView1920();
        }
    }

    List<PoolInfo> poolInfoList = new ArrayList<>();

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 5 && data != null) {
            PoolInfo info = (PoolInfo) data.getSerializableExtra(KeyConst.OBJ_INFO);
            if (resultCode == 0) {//池号
                if (TYPE == 15) {//入池
                    if (info != null) {
                        poolInfoList.add(info);
                        notifyPoolItemList();
                    }
                } else {
                    CHOOSE_POOL_ID = info.getId();
                    choosePoolTv.setText(info.getName());

                    if (TYPE == 17) {
                        timeYmd = "";
                        mTimeTv.setText("");
                    }
                }
            } else {
                CHOOSED_15_16_19_ID = info.getId();

                if (resultCode == 1) {//配料桶次==1
                    choose15_16_19Tv.setText(info.getIngredientNo());
                } else if (resultCode == 4) {
                    choose15_16_19Tv.setText(info.getProcNum());
                } else {   //过磅单号==2
                    choose15_16_19Tv.setText(info.getProcNum());

                    PoolInfo.ObjectBean obj = info.getObject();
                    if (TYPE == 15) {//入池
                        m15CarNumerTv.setText(obj.getNumberPlate());//车牌号
                        m15AddressTv.setText(obj.getOriginPlace());//产地
                    }

                }
            }
        }

        //并入池号
        if (requestCode == 6 && data != null) {
            PoolInfo info = (PoolInfo) data.getSerializableExtra(KeyConst.OBJ_INFO);
            OBJECT_POOL_ID = info.getId();
            m18MergePoolNumTv.setText(info.getName());
        }
        //操作员
        if (requestCode > 10 && resultCode == 3) {
            if (emplyeeChoosedDialog != null) {
                emplyeeChoosedDialog.dismiss();
            }
            employeeInfo = (EmployeeInfo) data.getSerializableExtra(KeyConst.OBJ_INFO);
            if (employeeInfo != null) {
                operatorId = employeeInfo.getId();
                operatorName = employeeInfo.getEmployeeName();
                operatorTv.setText(operatorName);
                operatorDirectorTv.setText("");
                //查询主管信息
                getDirectorInfo();
            }
        }

        //抄送人
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
            copyerIdArr = choosedEmployeeIds;
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

    private void notifyPoolItemList() {
        if (layout15 == null || poolInfoList == null) {
            return;
        }
        layout15.removeAllViews();
        final int size = poolInfoList.size();
        for (int i = 0; i < size; i++) {
            final int finalI = i;
            View itemView = View.inflate(context, R.layout.layout_15_item, null);
            final TextView deleteBt = (TextView) itemView.findViewById(R.id.item_9_delete_bt);
            TextView titleTv = (TextView) itemView.findViewById(R.id.item_9_title_tv);
            PoolInfo info = poolInfoList.get(i);


            final TextView inPoolTimeTv = itemView.findViewById(R.id.in_pool_time_tv);
            inPoolTimeTv.setText(info.getPutInPoolDate());
            inPoolTimeTv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    TimePickerDialog.Builder timePickerDialog = DialogUtils.getTimePicker(context);
                    timePickerDialog.setCallBack(new OnDateSetListener() {
                        @Override
                        public void onDateSet(TimePickerDialog timePickerView, long millseconds) {
                            if (millseconds > System.currentTimeMillis()) {
                                ToastUtil.show(context, "时间不可以大于今日");
                                return;
                            }
                            String inPoolTime = TimeUtils.getTimeYmd(millseconds);
                            inPoolTimeTv.setText(inPoolTime);

                            if (finalI < poolInfoList.size()) {
                                poolInfoList.get(finalI).setPutInPoolDate(inPoolTime);
                            }

                        }
                    });

                    timePickerDialog.build().show(context.getSupportFragmentManager(), "");
                }
            });
            //池号
            String name = info.getName();
            titleTv.setText(name);

            //入池数量
            final EditText amountEt = itemView.findViewById(R.id.in_pool_num_et);
            InputFilter[] moneyFilters = {new MoneyInputFilter()};
            amountEt.setFilters(moneyFilters);

            amountEt.setText(info.getAmount());
            amountEt.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                }

                @Override
                public void afterTextChanged(Editable editable) {
                    if (finalI < poolInfoList.size()) {
                        poolInfoList.get(finalI).setAmount(amountEt.getText().toString());
                    }
                }
            });

            //删除按钮
            deleteBt.setVisibility(processId > 0 ? View.GONE : View.VISIBLE);//草稿箱进来,就一个入池,不删除
            deleteBt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    new MaterialDialog.Builder(context)
                            .content("确定要删除" + name + "吗?")
                            .positiveText(R.string.sure)
                            .onPositive(new MaterialDialog.SingleButtonCallback() {
                                @Override
                                public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                    poolInfoList.remove(finalI);
                                    notifyPoolItemList();
                                }
                            })
                            .positiveColorRes(R.color.mainColor)
                            .negativeText(R.string.cancel)
                            .negativeColorRes(R.color.mainColor)
                            .show();
                }
            });
            layout15.addView(itemView);
        }

    }

    //主管信息
    private void getDirectorInfo() {
        if (!NetUtil.isNetworkConnected(context)) {
            return;
        }
        String url = Constant.WEB_SITE + "/biz/process/directorInfo/" + operatorId;
        Response.Listener<EmployeeInfo> successListener = new Response
                .Listener<EmployeeInfo>() {
            @Override
            public void onResponse(EmployeeInfo result) {
                if (operatorDirectorTv != null && result != null && result.getEmployeeName() != null) {
                    operatorDirectorTv.setText(result.getEmployeeName());
                } else {
                    if (operatorDirectorTv != null) {
                        operatorDirectorTv.setText("");
                    }
                }
            }
        };

        Request<EmployeeInfo> versionRequest = new
                GsonRequest<EmployeeInfo>(Request.Method.GET, url,
                        successListener, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                    }
                }, new TypeToken<EmployeeInfo>() {
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

    private List<FileListInfo> fileData = new ArrayList<>();

    //抄送人列表
    private void initCopyerLayout(String employeeName) {
        copyerItemsLayout.removeAllViews();
        if (TextUtil.isEmpty(employeeName) || copyerItemsLayout == null) {
            return;
        }
        String[] nameArr = new String[1];
        if (!employeeName.contains(",")) {//不包含
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

    //保存草稿
    private void initFinishBt() {
        findViewById(R.id.left_bt).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
               /* if (processId > 0) {
                    finish();
                    return;
                }
                if (totalAmount == 0 && TextUtil.isEmpty(commonProductName)) {
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
                }).show();*/


            }
        });
    }

    private void setFileListData() {
        JsonArray jsonArray = infoObj.getAsJsonArray(KeyConst.attachList);
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
                                            ToastUtil.show(context, "删除失败");
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

    JsonArray copyerIdArr = new JsonArray();

    private void getAuditLogData() {
        String url = Constant.WEB_SITE + "/biz/process/audit/node";
        Response.Listener<List<HistoryInfo>> successListener = new Response.
                Listener<List<HistoryInfo>>() {
            @Override
            public void onResponse(List<HistoryInfo> result) {
                if (result == null || result.size() < 2) {
                    return;
                }
                result.remove(0);
                setAuditLogView(result);
            }
        };
        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.d(TAG, "获取审批流程:" + TextUtil.getErrorMsg(volleyError));
            }
        };

        Request<List<HistoryInfo>> request = new
                GsonRequest<List<HistoryInfo>>(Request.Method.POST,
                        url, successListener, errorListener, new TypeToken<List<HistoryInfo>>() {
                }.getType()) {
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> params = new HashMap<>();
                        params.put(KeyConst.type, copyerPositionArr[TYPE - 14] + "");
                        return params;
                    }

                    @Override
                    public Map<String, String> getHeaders() throws AuthFailureError {
                        Map<String, String> params = new HashMap<>();
                        params.put(KeyConst.Content_Type, Constant.application_json);
                        params.put(KeyConst.Authorization, KeyConst.Bearer + App.token);
                        return params;
                    }
                };
        App.requestQueue.add(request);
    }

    private void showProductNameDialog() {
        if (productNameInfoList == null || productNameInfoList.size() == 0) {
            ToastUtil.show(context, R.string.no_data);
            getProductNameList();
            return;
        }
        final Dialog dialog = new Dialog(context, R.style.dialog_appcompat_theme);
        View inflate = LayoutInflater.from(context).inflate(R.layout.
                layout_dialog_product_name, null);
        LinearLayout layoutView = inflate.findViewById(R.id.produce_name_layout);
        EditText searchEt = inflate.findViewById(R.id.search_et);
        inflate.findViewById(R.id.produce_name_cancel_bt).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        searchEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(final Editable editable) {
                layoutView.removeAllViews();
                List<ProductInfo> list = new ArrayList<>();
                for (ProductInfo productInfo : productNameInfoList) {
                    if (productInfo.getName().contains(editable)) {
                        list.add(productInfo);
                    }
                }
                if (list.size() == 0) {
                    ToastUtil.show(context, R.string.search_no_data);
                }
                initLayoutView(dialog, list, layoutView);
            }
        });

        initLayoutView(dialog, productNameInfoList, layoutView);

        dialog.setContentView(inflate);

        DialogUtils.setDialogWindowShapeCenter(context, dialog);
    }

    private void initLayoutView(Dialog dialog, List<ProductInfo> list, LinearLayout layout) {
        int textSize = getResources().getDimensionPixelSize(R.dimen.dialog_tv_size);
        for (ProductInfo info : list) {
            String name = info.getName();
            int id = info.getId();
            TextView tv = new TextView(context);
            tv.setText(name);
            tv.setPadding(0, textSize * 2 / 3, 0, textSize * 2 / 3);
            tv.setTextSize(textSize);
            tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                    commonProductId = id + "";
                    commonProductName = name;
                    productNameTv.setText(commonProductName);

                    productSpecInfoList = info.getBizProductDetailList();
                    if (commonProductSpecTv != null) {
                        commonProductSpecId = "";
                        commonProductSpec = "";
                        commonProductSpecTv.setText("");
                    }
                }
            });
            layout.addView(tv);
        }
    }
    //产品名称
   /* private void showProductNameDialog() {
        if (productNameInfoList == null || productNameInfoList.size() == 0) {
            ToastUtil.show(context, R.string.no_data);
            getProductNameList();
            return;
        }
        ArrayList<String> nameList = new ArrayList<>();
        for (ProductInfo info : productNameInfoList) {
            nameList.add(info.getName());
        }

        //todo 搜索
        new MaterialDialog.Builder(context)
                .items(nameList)// 列表数据
                .itemsCallback(new MaterialDialog.ListCallback() {
                    @Override
                    public void onSelection(MaterialDialog dialog, View itemView,
                                            int position, CharSequence text) {
                        ProductInfo productInfo = productNameInfoList.get(position);
                        commonProductId = productInfo.getId() + "";
                        commonProductName = productInfo.getName();
                        productNameTv.setText(commonProductName);
                    }
                })
                .show();
    }*/

    //产品规格选择框
    private void showProductSpecDialog() {
        if (productSpecInfoList == null || productSpecInfoList.size() == 0) {
            ToastUtil.show(context, R.string.no_data);
            getProductNameList();
            return;
        }
        ArrayList<String> nameList = new ArrayList<>();
        for (ProductInfo info : productSpecInfoList) {
            nameList.add(info.getSpecification());
        }
        new MaterialDialog.Builder(context)
                .items(nameList)// 列表数据
                .itemsCallback(new MaterialDialog.ListCallback() {
                    @Override
                    public void onSelection(MaterialDialog dialog, View itemView,
                                            int position, CharSequence text) {
                        ProductInfo productInfo = productSpecInfoList.get(position);
                        commonProductSpecId = productInfo.getId() + "";
                        commonProductSpec = text + "";
                        commonProductSpecTv.setText(commonProductSpec);
                    }
                })
                .show();
    }
}
