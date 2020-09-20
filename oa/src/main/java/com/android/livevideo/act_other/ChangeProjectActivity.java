package com.android.livevideo.act_other;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.livevideo.App;
import com.android.livevideo.R;
import com.android.livevideo.adapter.MyRecyclerAdapter;
import com.android.livevideo.bean.Contact;
import com.android.livevideo.core.net.GsonRequest;
import com.android.livevideo.core.utils.Constant;
import com.android.livevideo.core.utils.DialogHelper;
import com.android.livevideo.core.utils.KeyConst;
import com.android.livevideo.core.utils.NetUtil;
import com.android.livevideo.core.utils.UrlConstant;
import com.android.livevideo.util.ToastUtil;
import com.gjiazhe.wavesidebar.WaveSideBar;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * Gool Lee
 * 项目切换
 */
public class ChangeProjectActivity extends BaseFgActivity {
    private Button title_bar;
    private TextView tv_content;
    private TextView guideContentTv, titleTv;
    private LinearLayout guildeLayout;
    private ChangeProjectActivity context;
    private TabLayout mTopTab0, mTopTab1;
    private RecyclerView recyclerView;
    private WaveSideBar waveSideBar;
    private ArrayList<Contact> contacts = new ArrayList<>();
    private ArrayList<String> pinYinList = new ArrayList<>();
    private MyRecyclerAdapter adapter;
    private SharedPreferences.Editor spEd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initStatusBar();
        context = this;
        setContentView(R.layout.activity_change_project);
        initTitleBackBt("");

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        waveSideBar = (WaveSideBar) findViewById(R.id.waveSideBar);
        waveSideBar.setIndexItems("");
        initRecyclerView();

        getData();
    }

    private void initRecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(context, DividerItemDecoration.VERTICAL));
        adapter = new MyRecyclerAdapter(contacts);
        recyclerView.setAdapter(adapter);

        spEd = getSharedPreferences(Constant.CONFIG_FILE_NAME, MODE_PRIVATE).edit();
        adapter.setOnItemClickListener(new MyRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int id) {
                spEd.putInt(KeyConst.SP_PROJECT_ID, id ).commit();
                context.finish();

            }
        });
    }


    public class SortComparator implements Comparator {
        @Override
        public int compare(Object lhs, Object rhs) {
            Contact a = (Contact) lhs;
            Contact b = (Contact) rhs;

            String aindex = a.getIndex();
            String bindex = b.getIndex();
            return aindex.compareTo(bindex);
        }
    }

    //获取合同段列表
    private void getData() {
        if (!NetUtil.isNetworkConnected(context)) {
            ToastUtil.show(context, getString(R.string.no_network));
            return;
        }
        final DialogHelper dialogHelper = new DialogHelper(getSupportFragmentManager(), context);
        dialogHelper.showAlert("获取中...", true);
        String url = Constant.WEB_SITE + UrlConstant.url_biz_projects;
        Response.Listener<JsonObject> successListener = new Response
                .Listener<JsonObject>() {
            @Override
            public void onResponse(JsonObject result) {
                if (result == null || result.size() == 0) {
                    if (null != context && !context.isFinishing()) {
                        dialogHelper.hideAlert();
                    }
                    ToastUtil.show(context, "暂无项目");
                    return;
                }
                if (null != context && !context.isFinishing()) {
                    dialogHelper.hideAlert();
                }
                contacts.clear();
                pinYinList.clear();
                JsonArray jsonArray = result.getAsJsonArray("content");
                for (int i = 0; i < jsonArray.size(); i++) {
                    JsonObject infoObj = jsonArray.get(i).getAsJsonObject();
                    int id = infoObj.get(KeyConst.id).getAsInt();
                    String name = infoObj.get(KeyConst.name).toString().replace("\"", "");
                    String pinYinIndex = getPYIndexStr(name.substring(0, 1), true);

                    contacts.add(new Contact(id, pinYinIndex, name));
                    if (!pinYinList.contains(pinYinIndex)) {
                        pinYinList.add(pinYinIndex);
                    }
                }
                Comparator comp = new SortComparator();
                Collections.sort(contacts, comp);

                Collections.sort(pinYinList);

                int size = pinYinList.size();
                String[] pinYinArr = new String[size];
                for (int i = 0; i < size; i++) {
                    pinYinArr[i] = pinYinList.get(i);
                }
                //排序
                waveSideBar.setIndexItems(pinYinArr);

                adapter.setData(contacts);

                waveSideBar.setOnSelectIndexItemListener(new WaveSideBar.OnSelectIndexItemListener() {
                    @Override
                    public void onSelectIndexItem(String index) {
                        for (int i = 0; i < contacts.size(); i++) {
                            if (index.equals(contacts.get(i).getIndex())) {
                                recyclerView.scrollToPosition(i);
                                return;
                            }
                        }
                    }
                });

            }
        };

        Request<JsonObject> versionRequest = new
                GsonRequest<JsonObject>(
                        Request.Method.GET, url,
                        successListener, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        volleyError.printStackTrace();
                        Log.d(TAG, "错误返回" + volleyError.getMessage());
                        ToastUtil.show(context, getString(R.string.request_failed_retry_later));
                        if (null != context && !context.isFinishing()) {
                            dialogHelper.hideAlert();
                        }
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

    /**
     * 返回首字母
     */
    public String getPYIndexStr(String strChinese, boolean bUpCase) {
        try {
            StringBuffer buffer = new StringBuffer();
            strChinese = strChinese.replace("（", "").replace("）", "");// 特殊符号处理
            byte b[] = strChinese.getBytes("GBK");// 把中文转化成byte数组
            for (int i = 0; i < b.length; i++) {
                if ((b[i] & 255) > 128) {
                    int char1 = b[i++] & 255;
                    char1 <<= 8;// 左移运算符用“<<”表示，是将运算符左边的对象，向左移动运算符右边指定的位数，并且在低位补零。其实，向左移n位，就相当于乘上2的n次方
                    int chart = char1 + (b[i] & 255);
                    buffer.append(getPYIndexChar((char) chart, bUpCase));
                    continue;
                }
                char c = (char) b[i];
                if (!Character.isJavaIdentifierPart(c))// 确定指定字符是否可以是 Java
                    // 标识符中首字符以外的部分。
                    c = 'A';
                buffer.append(c);
            }
            return buffer.toString();
        } catch (Exception e) {
            System.out.println((new StringBuilder()).append("\u53D6\u4E2D\u6587\u62FC\u97F3\u6709\u9519")
                    .append(e.getMessage()).toString());
        }
        return null;
    }

    /**
     * 得到首字母
     */
    private char getPYIndexChar(char strChinese, boolean bUpCase) {

        int charGBK = strChinese;

        char result;

        if (charGBK >= 45217 && charGBK <= 45252)

            result = 'A';

        else if (charGBK >= 45253 && charGBK <= 45760)

            result = 'B';

        else if (charGBK >= 45761 && charGBK <= 46317)

            result = 'C';

        else if (charGBK >= 46318 && charGBK <= 46825)

            result = 'D';

        else if (charGBK >= 46826 && charGBK <= 47009)

            result = 'E';

        else if (charGBK >= 47010 && charGBK <= 47296)

            result = 'F';

        else if (charGBK >= 47297 && charGBK <= 47613)

            result = 'G';

        else if (charGBK >= 47614 && charGBK <= 48118)

            result = 'H';

        else if (charGBK >= 48119 && charGBK <= 49061)

            result = 'J';

        else if (charGBK >= 49062 && charGBK <= 49323)

            result = 'K';

        else if (charGBK >= 49324 && charGBK <= 49895)

            result = 'L';

        else if (charGBK >= 49896 && charGBK <= 50370)

            result = 'M';

        else if (charGBK >= 50371 && charGBK <= 50613)

            result = 'N';

        else if (charGBK >= 50614 && charGBK <= 50621)

            result = 'O';

        else if (charGBK >= 50622 && charGBK <= 50905)

            result = 'P';

        else if (charGBK >= 50906 && charGBK <= 51386)

            result = 'Q';

        else if (charGBK >= 51387 && charGBK <= 51445)

            result = 'R';

        else if (charGBK >= 51446 && charGBK <= 52217)

            result = 'S';

        else if (charGBK >= 52218 && charGBK <= 52697)

            result = 'T';

        else if (charGBK >= 52698 && charGBK <= 52979)

            result = 'W';

        else if (charGBK >= 52980 && charGBK <= 53688)

            result = 'X';

        else if (charGBK >= 53689 && charGBK <= 54480)

            result = 'Y';

        else if (charGBK >= 54481 && charGBK <= 55289)

            result = 'Z';

        else

            result = (char) (65 + (new Random()).nextInt(25));

        if (!bUpCase)

            result = Character.toLowerCase(result);

        return result;

    }
}