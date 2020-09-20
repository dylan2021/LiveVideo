package com.android.livevideo.act_0;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LevelListDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.text.Editable;
import android.text.Html;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ImageSpan;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.android.livevideo.App;
import com.android.livevideo.R;
import com.android.livevideo.act_other.BaseFgActivity;
import com.android.livevideo.act_other.SeePicActivity;
import com.android.livevideo.bean.MsgInfo;
import com.android.livevideo.core.net.GsonRequest;
import com.android.livevideo.core.utils.Constant;
import com.android.livevideo.core.utils.DialogHelper;
import com.android.livevideo.core.utils.ImageUtil;
import com.android.livevideo.core.utils.KeyConst;
import com.android.livevideo.core.utils.NetUtil;
import com.android.livevideo.core.utils.TextUtil;
import com.android.livevideo.util.DialogUtils;
import com.android.livevideo.util.ToastUtil;
import com.android.livevideo.util.Utils;
import com.android.livevideo.widget.TouchImageView;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import org.json.JSONObject;
import org.xml.sax.XMLReader;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * 公告
 * Gool
 */
public class NoticeDetailBase64Activity extends BaseFgActivity {

    private NoticeDetailBase64Activity context;
    private MsgInfo info;
    private TextView contentTv;
    private int id;
    private int agreeReject_recall;
    private TextView deptNameTimeTv;
    private JsonObject infoObj;
    private String title;
    private FragmentManager fm;
    private int maxWidth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_msg_notice_detail);

        initStatusBar();
        context = this;
        fm = getSupportFragmentManager();
        id = getIntent().getIntExtra(KeyConst.id, 0);
        title = getIntent().getStringExtra(KeyConst.title);
        agreeReject_recall = getIntent().getIntExtra(KeyConst.agreeReject_recall, 0);
        initTitleBackBt(title);

        initAgressRejectView();

        contentTv = (TextView) findViewById(R.id.msg_detail_content_tv);
        deptNameTimeTv = (TextView) findViewById(R.id.msg_detail_publish_tv);

        maxWidth = ImageUtil.getScreenWidth(context) - getResources().getDimensionPixelSize(R.dimen.dm060);
        strings.clear();
        try {
            getData();
        } catch (OutOfMemoryError e) {
        }
    }

    private void initAgressRejectView() {
        RelativeLayout auditLayout = findViewById(R.id.audit_layout);
        Button auditBt1 = (Button) findViewById(R.id.audit_bt_1);
        Button auditBt2 = (Button) findViewById(R.id.audit_bt_2);
        Button auditBt3 = (Button) findViewById(R.id.audit_bt_3);

        if (agreeReject_recall == 1) {//同意,驳回
            auditLayout.setVisibility(View.VISIBLE);
            auditBt3.setVisibility(View.GONE);
            final Map<String, Object> map = new HashMap<>();
            map.put(KeyConst.processId, id);
            //同意
            auditBt1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    agreeReject_recall = 0;
                    map.put(KeyConst.remark, "");
                    map.put(KeyConst.auditResult, true);
                    bottomAuditBtPost(new JSONObject(map));
                }
            });
            //驳回
            auditBt2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    agreeReject_recall = 0;
                    map.put(KeyConst.remark, "");
                    map.put(KeyConst.auditResult, false);
                    bottomAuditBtPost(new JSONObject(map));
                }
            });
        } else if (agreeReject_recall == 2) {//撤销
            auditLayout.setVisibility(View.VISIBLE);
            auditBt3.setVisibility(View.VISIBLE);
            //撤销
            auditBt3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    agreeReject_recall = 0;

                    bottomAuditBtPost(null);
                }
            });
        } else if (agreeReject_recall == 3) {//删除
            deleteDraftBt();
        }
    }

    private void bottomAuditBtPost(JSONObject jsonObject) {
        if (!NetUtil.isNetworkConnected(context)) {
            ToastUtil.show(context, getString(R.string.no_network));
            return;
        }
        String url = Constant.WEB_SITE;
        if (jsonObject == null) {
            url = url + "/biz/process/launch/" + id;
        } else {
            url = url + "/biz/process/needDo";
        }

        DialogHelper.showWaiting(fm, getString(R.string.loading));
        JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.PUT, url,
                jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject result) {
                if (null != context && !context.isFinishing()) {
                    DialogHelper.hideWaiting(fm);
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
                    DialogHelper.hideWaiting(fm);
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

    //删除草稿
    private void deleteDraftBt() {

        getTitleRightBt(getString(R.string.delete)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogUtils.getTwoBtDialog(context, "草稿删除后不可恢复,确定删除吗?")
                        .onPositive(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                String url = Constant.WEB_SITE + "/biz/process/draft/" + id;
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

    //查询流程数据
    private void getData() {
        if (!NetUtil.isNetworkConnected(context)) {
            return;
        }
        DialogHelper.showWaiting(fm, "加载中...");
        String url = Constant.WEB_SITE + "/biz/process/" + id;
        Response.Listener<MsgInfo> successListener = new Response
                .Listener<MsgInfo>() {
            @Override
            public void onResponse(MsgInfo result) {
                if (result == null) {
                    DialogHelper.hideWaiting(fm);
                    ToastUtil.show(context, R.string.no_data);
                    return;
                }
                info = result;
                setView();
            }
        };

        Request<MsgInfo> versionRequest = new
                GsonRequest<MsgInfo>(
                        Request.Method.GET, url,
                        successListener, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        ToastUtil.show(context, R.string.server_exception);
                        DialogHelper.hideWaiting(fm);
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

    private void setView() {
        String time = TextUtil.subTimeYmd(info.createTime);
        String publisherName = TextUtil.remove_N(info.createName);
        String deptName = TextUtil.remove_N(info.createDeptName);
        deptNameTimeTv.setText(publisherName
                + "\n" + deptName + "\n" + time);

        infoObj = info.object;
        if (null == infoObj || infoObj.isJsonNull()) {
            return;
        }

        setTv(R.id.msg_detail_title_tv, Utils.getObjStr(infoObj, KeyConst.summary));//摘要
        JsonElement element = infoObj.get(KeyConst.remark);
        if (element == null || element.isJsonNull()) {
            DialogHelper.hideWaiting(fm);
            return;
        }
        String remark = element.getAsString();//内容

        DialogHelper.showWaiting(fm, getString(R.string.loading));

        if (remark != null && !remark.contains("img src=")) {//不包含图片
            DialogHelper.hideWaiting(fm);
        }
        //加载图片 开始
        try {
            MyTagHandler tagHandler = new MyTagHandler();
            contentTv.setText(Html.fromHtml(remark, new HtmlImageGetter(), tagHandler));
            contentTv.setMovementMethod(LinkMovementMethod.getInstance());
        } catch (Exception e) {
            DialogHelper.hideWaiting(fm);
        } catch (OutOfMemoryError e) {
            DialogHelper.hideWaiting(fm);
            ToastUtil.show(context, "数据加载异常");
        }

    }

    class HtmlImageGetter implements Html.ImageGetter {
        @Override
        public Drawable getDrawable(String source) {
            LevelListDrawable d = new LevelListDrawable();
            Drawable empty = getResources().getDrawable(
                    R.color.gray_1);
            d.addLevel(0, 0, empty);
            d.setBounds(0, 0, empty.getIntrinsicWidth(), empty.getIntrinsicHeight());
            new LoadImage().execute(source, d);
            return d;
        }

        /**
         * 异步下载图片类
         */
        class LoadImage extends AsyncTask<Object, Void, Bitmap> {

            private LevelListDrawable mDrawable;

            @Override
            protected Bitmap doInBackground(Object... params) {
                //base64 解析方式
                String base64Str = (String) params[0];
                mDrawable = (LevelListDrawable) params[1];
                return base64UrlStrToBitmap(base64Str);
               /*
                //url的解析方式
                String source = (String) params[0];
                mDrawable = (LevelListDrawable) params[1];
                try {
                    InputStream is = new URL(source).openStream();
                    BitmapFactory.Options options = new BitmapFactory.Options();
                    if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.KITKAT) {
                        options.inSampleSize = 2;
                    }
                    return BitmapFactory.decodeStream(is, null, options);
                }  catch (Exception e) {
                    e.printStackTrace();
                }
                return null;*/
            }


            /**
             * 图片下载完成后执行
             */
            @Override
            protected void onPostExecute(final Bitmap bitmap) {
                if (context == null || context.isFinishing()) {
                    return;
                }
                if (bitmap != null) {
                    mDrawable.addLevel(1, 1, new BitmapDrawable(bitmap));

                    int width = bitmap.getWidth();
                    int height = bitmap.getHeight();
                    if (width > maxWidth) {
                        double f1 = new BigDecimal((float) width / maxWidth).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
                        height = (int) (height / f1);
                        width = maxWidth;
                    }
                    mDrawable.setBounds(0, 0, width, height);
                    mDrawable.setLevel(1);
                    contentTv.invalidate();
                    CharSequence t = contentTv.getText();
                    contentTv.setText(t);
                }
                DialogHelper.hideWaiting(fm);

            }
        }

    }

    public static ArrayList<String> strings = new ArrayList<>();

    @Override
    protected void onDestroy() {
        super.onDestroy();
        strings.clear();
    }

    public class MyTagHandler implements Html.TagHandler {

        @Override
        public void handleTag(boolean opening, String tag, Editable output, XMLReader xmlReader) {
            // 处理标签<img>
            if ("img".equals(tag.toLowerCase(Locale.getDefault()))) {
                // 获取长度
                int len = output.length();
                // 获取图片地址
                ImageSpan[] images = output.getSpans(len - 1, len, ImageSpan.class);
                String imgURL = images[0].getSource();
                // 记录所有图片地址
                strings.add(imgURL);
                // 记录是第几张图片
                final int position = strings.size() - 1;
                Log.d(TAG, "图片点击,索引:" + position);
                // 使图片可点击并监听点击事件
                output.setSpan(new ClickableImage(context, position), len - 1, len,
                        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
        }

        private class ClickableImage extends ClickableSpan {
            private Context context;
            private int position;

            public ClickableImage(Context context, int position) {
                this.context = context;
                this.position = position;
            }

            @Override
            public void onClick(View widget) {
                Log.d(TAG, "图片点击,索引:" + position);
                Intent i = new Intent();
                i.setClass(context, SeePicActivity.class);
                i.putExtra(KeyConst.type, 1);
                i.putExtra("selectPosition", position);
                startActivity(i);
            }
        }

    }

    private void showPicDialog(Bitmap picUrl) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style
                .dialog_fullscree_animation);
        LayoutInflater inflater = LayoutInflater.from(context);
        View v = inflater.inflate(R.layout.layout_dialog_file_detail, null);
        TouchImageView fileDetailSDV = (TouchImageView) v.findViewById(R.id
                .card_detail_file_sdv);
        v.findViewById(R.id.loading_view).setVisibility(View.GONE);
        fileDetailSDV.setImageBitmap(picUrl);

        final Dialog dialog = builder.create();
        dialog.show();
        dialog.getWindow().setContentView(v);

        fileDetailSDV.setOnImageClickListener(new TouchImageView.OnClickListener() {
            @Override
            public void onClick() {
                dialog.dismiss();
            }
        });


    }

    //图片base64 网络地址url转bitmap
    private Bitmap base64UrlStrToBitmap(String urlBase64) {
        Bitmap bitmap = null;
        try {
            byte[] bitmapArray = Base64.decode(urlBase64.split(",")[1], Base64.DEFAULT);
            //解析base64类型的图片
            bitmap = BitmapFactory.decodeByteArray(bitmapArray, 0, bitmapArray.length);
        } catch (Exception e) {
        } catch (OutOfMemoryError e) {
            try {
                byte[] bitmapArray = Base64.decode(urlBase64.split(",")[1], Base64.DEFAULT);
                //解析base64类型的图片
                bitmap = BitmapFactory.decodeByteArray(bitmapArray, 0, bitmapArray.length);
            } catch (OutOfMemoryError e1) {
            }
        }
        return bitmap;
    }

}