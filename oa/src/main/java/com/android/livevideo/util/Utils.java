/*
 * 	Flan.Zeng 2011-2016	http://git.oschina.net/signup?inviter=flan
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.android.livevideo.util;

import android.Manifest;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Environment;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.livevideo.act_other.BaseFgActivity;
import com.android.livevideo.act_other.LoginActivity;
import com.android.livevideo.bean.DictInfo;
import com.android.livevideo.bean.GroupInfo;
import com.android.livevideo.core.net.GsonRequest;
import com.android.livevideo.core.utils.Constant;
import com.android.livevideo.core.utils.ImageUtil;
import com.android.livevideo.core.utils.KeyConst;
import com.android.livevideo.core.utils.TextUtil;
import com.android.livevideo.dialogfragment.OneBtDialogFragment;
import com.android.livevideo.exception.NoSDCardException;
import com.android.livevideo.R;
import com.android.livevideo.App;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;

import java.io.File;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.content.Context.MODE_PRIVATE;

/**
 * 工具类
 *
 * @author Dylan
 */
public class Utils {

    private static final String TAG = Utils.class.getSimpleName();

    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE};
    private static String[] READ_PHONE_STATE = {
            Manifest.permission.READ_PHONE_STATE};

    /**
     * 坚持系统是否有读写SDCard的权限
     *
     * @param activity
     */
    public static void requestStoragePermissions(BaseFgActivity activity) {
        int permission = ActivityCompat.checkSelfPermission(activity,
                Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (permission != PackageManager.PERMISSION_GRANTED) {
            // 木有权限
            ActivityCompat.requestPermissions(activity, PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE);
        }
    }

    /**
     * 是否有读写手机硬件信息 如设备id，手机版本
     *
     * @param activity
     */
    public static void verifyStatePermissions(Activity activity) {
        // Check if we have write permission
//        int permission = ActivityCompat.checkSelfPermission(activity,
//                Manifest.permission.READ_PHONE_STATE);

        if (ContextCompat.checkSelfPermission(activity, Manifest.permission.READ_PHONE_STATE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity, READ_PHONE_STATE,
                    REQUEST_EXTERNAL_STORAGE);
        }
    }

    /**
     * 获取文件下载存放基础路径
     *
     * @return
     */
    public static String getFileLoadBasePath() throws NoSDCardException {

        String path;
        boolean sdCardExist = Environment.getExternalStorageState()
                .equals(android.os.Environment.MEDIA_MOUNTED); //判断sd卡是否存在
        if (sdCardExist) {
            path = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator +
                    "ngame" + File.separator +
                    "download" + File.separator;
        } else {
            throw new NoSDCardException("设备上没有找到SDCard");
        }
        return path;
    }

    /**
     * 获取图片存放基础路径
     *
     * @return
     */
    public static String getImageBasePath() throws NoSDCardException {

        String path;
        boolean sdCardExist = Environment.getExternalStorageState()
                .equals(android.os.Environment.MEDIA_MOUNTED); //判断sd卡是否存在
        if (sdCardExist) {
            path = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator +
                    "taskallo" + File.separator +
                    "image" + File.separator;
        } else {
            throw new NoSDCardException("设备上没有找到SDCard");
        }
        return path;
    }

    /**
     * 将文件的尺寸，由字节单位转为相应单位表示
     *
     * @param size    文件的尺寸，单位为字节
     * @param decimal 保留小数位数，0表示取整
     * @return
     */
    public static String formatFileSize(double size, int decimal) {

        String sizeStr;
        size = size / 1024;
        if (size > 1024) {
            size = size / 1024;
            if (size > 1024) {

                sizeStr = String.valueOf(size / 1024);
                sizeStr = sizeStr.format("%." + decimal + "f") + "G";
            } else {
                sizeStr = String.valueOf(size);
                sizeStr = sizeStr.format("%." + decimal + "f") + "M";
            }

        } else {
            sizeStr = String.valueOf(size);
            sizeStr = sizeStr.format("%." + decimal + "f") + "K";
        }

        return sizeStr;
    }

    /**
     * 获取APP的版本号
     *
     * @param context
     * @return
     */
    public static String getVersionCode(Context context) {

        String versionCode = "1.0";
        try {
            versionCode = context.getPackageManager().getPackageInfo("com.android.livevideo", 0)
                    .versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return versionCode;
    }

    /**
     * 获取APP的版本名称
     *
     * @param context
     * @return
     */
    public static int getVersionName(Context context) {
        try {
            PackageManager manager = context.getPackageManager();
            PackageInfo info = manager.getPackageInfo(context.getPackageName(), 0);
            int version = info.versionCode;
            return version;

        } catch (Exception e) {
            e.printStackTrace();
            return 999999;
        }
    }

    /**
     * 将px值转换为dip或dp值，保证尺寸大小不变
     *
     * @param pxValue
     * @return
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * 将dip或dp值转换为px值，保证尺寸大小不变
     *
     * @param dipValue
     * @return
     */
    public static int dip2px(Context context, float dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

    /**
     * 将px值转换为sp值，保证文字大小不变
     *
     * @param pxValue
     * @return
     */
    public static int px2sp(Context context, float pxValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (pxValue / fontScale + 0.5f);
    }

    public static void showUnLoginDialog(FragmentManager fm, final Context content, final int
            showMsgId) {
        final OneBtDialogFragment dialogFragment = new OneBtDialogFragment();
        dialogFragment.setTitle(showMsgId);
        dialogFragment.setDialogWidth(content.getResources().getDimensionPixelSize(R.dimen
                .unlogin_dialog_width));
        dialogFragment.setNegativeButton(R.string.login_now, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogFragment.dismiss();
                content.startActivity(new Intent(content, LoginActivity.class));
            }
        });
        dialogFragment.show(fm.beginTransaction(), "successDialog");
    }

    public static void callPhone(FragmentActivity content, String phoneNum) {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        Uri data = Uri.parse("tel:" + phoneNum);
        intent.setData(data);
        content.startActivity(intent);
    }

    /**
     * 调用第三方浏览器打开
     *
     * @param context
     * @param url     要浏览的资源地址
     */
    public static void openBrowser(Context context, String url) {
        final Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(url));
        // 注意此处的判断intent.resolveActivity()可以返回显示该Intent的Activity对应的组件名
        // 官方解释 : Name of the component implementing an activity that can display the intent
        if (intent.resolveActivity(context.getPackageManager()) != null) {
            final ComponentName componentName = intent.resolveActivity(context.getPackageManager());
            context.startActivity(Intent.createChooser(intent, "请选择浏览器"));
        } else {
            ToastUtil.show(context, "请下载浏览器");
        }
    }

    public static void setLoadHeaderFooter(Activity context, RefreshLayout refreshLayout) {
        refreshLayout.setPrimaryColors(Color.WHITE);
        // Header
        final ClassicsHeader header = new ClassicsHeader(context);
        header.setTextSizeTitle(14);
        TextView headerLastUpdateTv = header.getLastUpdateText();
        headerLastUpdateTv.setVisibility(View.GONE);
        header.setDrawableProgressSizePx(62);
        header.setDrawableArrowSizePx(57);
        header.setEnableLastTime(false);
        refreshLayout.setRefreshHeader(header, ImageUtil.getScreenWidth(context), 200);
        // Footer
        ClassicsFooter footer = new ClassicsFooter(context);
        footer.setPrimaryColor(Color.WHITE);
        footer.setTextSizeTitle(14);
        footer.setDrawableArrowSizePx(57);
        footer.setDrawableProgressSizePx(62);
        refreshLayout.setRefreshFooter(footer, ImageUtil.getScreenWidth(context), 200);
        refreshLayout.setEnableFooterFollowWhenLoadFinished(true);
    }

    public static void setIndicator(final TabLayout tabLayout, final int leftRightMargin) {
        //了解源码得知 线的宽度是根据 tabView的宽度来设置的
        tabLayout.post(new Runnable() {
            @Override
            public void run() {
                try {
                    //拿到tabLayout的mTabStrip属性
                    Field mTabStripField = tabLayout.getClass().getDeclaredField("mTabStrip");
                    mTabStripField.setAccessible(true);

                    LinearLayout mTabStrip = (LinearLayout) mTabStripField.get(tabLayout);
                    for (int i = 0; i < mTabStrip.getChildCount(); i++) {
                        View tabView = mTabStrip.getChildAt(i);

                        //拿到tabView的mTextView属性
                        Field mTextViewField = tabView.getClass().getDeclaredField("mTextView");
                        mTextViewField.setAccessible(true);

                        TextView mTextView = (TextView) mTextViewField.get(tabView);

                        tabView.setPadding(0, 0, 0, 0);

                        //因为我想要的效果是   字多宽线就多宽，所以测量mTextView的宽度
                        int width = 0;
                        width = mTextView.getWidth();
                        if (width == 0) {
                            mTextView.measure(0, 0);
                            width = mTextView.getMeasuredWidth();
                        }

                        //设置tab左右间距为10dp  注意这里不能使用Padding 因为源码中线的宽度是根据 tabView的宽度来设置的
                        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) tabView.getLayoutParams();
                        params.width = width;
                        params.leftMargin = leftRightMargin;
                        params.rightMargin = leftRightMargin;
                        tabView.setLayoutParams(params);

                        tabView.invalidate();
                    }

                } catch (NoSuchFieldException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public static int getStatusColor(Context context, int status) {
        int[] statusColorArr = {R.color.status_waiting, R.color.status_auditing,
                R.color.status_rejuct, R.color.status_passed,
                R.color.status_rejuct, R.color.status_back, R.color.status_deleted};
        return ContextCompat.getColor(context,
                (status > statusColorArr.length - 1 ? statusColorArr[0] : statusColorArr[status]));
    }

    public static int getStatusDrawable(int status) {
        int[] arr = {0, R.drawable.ic_waiting, R.drawable.ic_auditing,
                R.drawable.ic_passed, R.drawable.ic_rejuct, R.drawable.ic_recall, R.drawable.ic_abolish,
        };
        return (status > arr.length - 1 ? 0 : arr[status]);
    }

    public static String getStatusText(int status) {
        String[] statusArr = {"未检测", "正常", "开裂",
                "鼓起", "", ""};
        return status > statusArr.length - 1 ? "检测问题" : statusArr[status];
    }

    public static String getObjDouble(JsonObject object, String key) {
        JsonElement element = object.get(key);
        if (element == null || element.isJsonNull()) {
            return "0";
        }
        String asString = element.getAsString();
        if (TextUtil.isEmpty(asString)) {
            return "0";
        }
        if (asString.contains(".") && asString.length() > 16 && (asString.contains("E") || asString.contains("E+"))) {
            asString = new BigDecimal(asString).toPlainString().substring(0, 16);
        }
        return asString;
    }

    public static String getObjStr(JsonObject object, String key) {
        JsonElement element = object.get(key);
        if (element == null || element.isJsonNull()) {
            return "";
        }
        String asString = element.getAsString();
        if (asString.contains(".") && asString.length() > 16 && (asString.contains("E") || asString.contains("E+"))) {
            asString = new BigDecimal(asString).toPlainString().substring(0, 16);
        }
        return asString;
    }

    public static String getDictTypeName(BaseFgActivity context, String DICT_TYPE, String typeValue) {
        List<DictInfo.DictValuesBean> dictValues = new ArrayList<>();
        String dictArr = getSP(context).getString(KeyConst.DICT_ARRAY, "");
        List<DictInfo> dictList = new Gson().fromJson(dictArr, new TypeToken<List<DictInfo>>() {
        }.getType());

        if (dictList != null) {
            for (DictInfo dictInfo : dictList) {
                String code = dictInfo.getCode();
                if (code.equals(DICT_TYPE)) {
                    dictValues = dictInfo.getDictValues();
                    break;
                }
            }
        }
        if (dictValues != null) {
            for (DictInfo.DictValuesBean dictValue : dictValues) {
                String value = dictValue.getValue();
                if (value.equals(typeValue)) {
                    return dictValue.getName();
                }
            }

        }

        return "";
    }

    public static void requestDeptTreeData(final BaseFgActivity context) {
        String url = Constant.WEB_SITE + "/upms/departments/tree";
        Response.Listener<JsonArray> successListener = new Response
                .Listener<JsonArray>() {
            @Override
            public void onResponse(JsonArray result) {
                if (result != null && !result.isJsonNull() && result.size() > 0) {
                    getSP(context).edit().putString(KeyConst.DEPT_TREE, result.toString()).commit();
                }
            }
        };

        Request<JsonArray> versionRequest = new
                GsonRequest<JsonArray>(Request.Method.GET, url,
                        successListener, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        android.util.Log.d("字典异常", "");
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

    public static void requestDictData(final BaseFgActivity context) {
        String url = Constant.WEB_SITE + "/dict/dicts/all?type=2";
        Response.Listener<JsonArray> successListener = new Response
                .Listener<JsonArray>() {
            @Override
            public void onResponse(JsonArray result) {
                if (result != null) {
                    getSP(context).edit().putString(KeyConst.DICT_ARRAY, result.toString()).commit();
                }
            }
        };

        Request<JsonArray> versionRequest = new
                GsonRequest<JsonArray>(Request.Method.GET, url,
                        successListener, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        android.util.Log.d("字典异常", "");
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

    public static void sortList(List<GroupInfo> parentList) {
        Collections.sort(parentList, new Comparator<GroupInfo>() {
            @Override
            public int compare(GroupInfo bean1, GroupInfo bean2) {
                return Integer.valueOf(bean1.getOrderBy()).compareTo(Integer.valueOf(bean2.getOrderBy()));
            }
        });
    }

    /*
      请求数据,本地缓存
     */
    public static void initLocalData(BaseFgActivity context) {
        Utils.requestDictData(context);//字典
        Utils.requestAuthData(context);//字典
        Utils.requestDeptTreeData(context);//部门tree
    }

    public static SharedPreferences getSP(BaseFgActivity context) {
        return context.getSharedPreferences(Constant.CONFIG_FILE_NAME, MODE_PRIVATE);
    }

    public static JsonArray getDeptTree(BaseFgActivity context) {
        String string = getSP(context).getString(KeyConst.DEPT_TREE, "");
        if (string == null) {
            requestDeptTreeData(context);
            ToastUtil.show(context, "部门数据为空");
            return new JsonArray();
        }
        JsonArray dictList = new Gson().fromJson(string, new TypeToken<JsonArray>() {
        }.getType());
        return dictList;
    }

    public static void requestAuthData(BaseFgActivity context) {
        String url = Constant.WEB_SITE + "/upms/accounts/authorities";
        Response.Listener<JsonArray> successListener = new Response
                .Listener<JsonArray>() {
            @Override
            public void onResponse(JsonArray result) {
                if (result == null || result.size() == 0) {
                    return;
                }
                getSP(context).edit().putString(KeyConst.AUTH_ARR_STR, result.toString()).commit();
            }
        };

        Request<JsonArray> versionRequest = new
                GsonRequest<JsonArray>(
                        Request.Method.GET, url,
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
}
