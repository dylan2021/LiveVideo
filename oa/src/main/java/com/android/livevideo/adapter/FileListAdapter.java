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

package com.android.livevideo.adapter;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.text.format.Formatter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.webkit.DownloadListener;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.android.livevideo.R;
import com.android.livevideo.act_other.BaseFgActivity;
import com.android.livevideo.bean.FileListInfo;
import com.android.livevideo.core.utils.Constant;
import com.android.livevideo.core.utils.ImageUtil;
import com.android.livevideo.util.ToastUtil;
import com.android.livevideo.widget.TouchImageView;
import com.facebook.drawee.view.SimpleDraweeView;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.List;

import static android.content.Context.DOWNLOAD_SERVICE;

/**
 * @author Gool
 */
public class FileListAdapter extends BaseAdapter {

    private List<FileListInfo> fileList;
    private BaseFgActivity context;
    private boolean allowDelete = false;
    private ImageView loadView;

    public FileListAdapter(BaseFgActivity c, List<FileListInfo> mFileListData) {
        context = c;
        fileList = mFileListData;
    }

    public void setDate(List<FileListInfo> gameInfoList) {
        this.fileList = gameInfoList;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        if (fileList != null) {
            return fileList.size();
        }
        return 0;
    }

    @Override
    public Object getItem(int position) {

        if (fileList != null) {
            return fileList.get(position);
        }
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void setAllowDelete(boolean allowDelete) {
        this.allowDelete = allowDelete;
    }

    public interface DataRemoveCallBack {
        void finish(List<FileListInfo> data);
    }

    DataRemoveCallBack calback;

    public void setCallBack(DataRemoveCallBack c) {
        calback = c;
    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {
        if (fileList == null || fileList.size() == 0) {
            return null;
        }
        final FileListInfo fileInfo = fileList.get(position);

        ViewHolder holder;
        if (convertView == null) {

            holder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout
                            .item_file_list, parent,
                    false);
            holder.filePicIv = (SimpleDraweeView) convertView.findViewById(R.id
                    .card_detail_file_list_item_iv);
            holder.removeBt = (Button) convertView.findViewById(R.id.dialog_btn_sure);
            holder.centerTitleTv = (TextView) convertView.findViewById(R.id.dialog_center_title_tv);
            holder.fileSizeTv = (TextView) convertView.findViewById(R.id.dialog_center_size_tv);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        if (fileInfo != null) {
            //只是查看,删除按钮
            if (fileInfo.fileUrl == Constant.TYPE_SEE) {
                holder.removeBt.setVisibility(View.GONE);
            }
            if (allowDelete) {
                holder.removeBt.setVisibility(View.VISIBLE);
            }
            final String fileName = fileInfo.fileName == null ? "未知" : fileInfo.fileName;
            final String fileUrl = fileInfo.filePath;
            if (ImageUtil.isImageSuffix(fileUrl)) {
                final String uriString = Constant.WEB_FILE_SEE + fileUrl;//测试完打开
                holder.filePicIv.setScaleType(ImageView.ScaleType.CENTER_CROP);
                Picasso.with(context).load(uriString)
                        .placeholder(R.drawable.ic_def_logo_188_188)
                        .error(R.drawable.ic_error_img)
                        .into(holder.filePicIv);
                //holder.filePicIv.setImageURI(uriString);
                convertView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showPicDialog(uriString);
                    }
                });
            } else {
                int resId = null == fileUrl ? R.drawable.ic_file_other : fileUrl.endsWith(".txt") ?
                        R.drawable.ic_txt : fileUrl.endsWith(".pdf") ? R.drawable.ic_pdf :
                        fileUrl.contains(".doc") ? R.drawable.ic_word : R.drawable.ic_file_other;
                holder.filePicIv.setImageResource(resId);
                holder.centerTitleTv.setPadding(0, 10, 0, 0);
                holder.filePicIv.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
                convertView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //预览
                        downLoadFile(Constant.WEB_FILE_SEE + fileUrl, fileName);
                        //调用浏览器打开
                        //Utils.openBrowser(context, Constant.WEB_FILE_SEE + fileUrl);
                    }
                });
            }

            holder.centerTitleTv.setText(fileName);
            holder.fileSizeTv.setText(fileInfo.fileSize == 0 ? "" : Formatter.formatFileSize(context, fileInfo.fileSize));

            holder.removeBt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    fileList.remove(fileInfo);
                    calback.finish(fileList);
                    notifyDataSetChanged();
                }
            });
        }

        return convertView;
    }

    //判断文件是否存在
    public boolean fileIsExists(File file) {
        if (file == null) {
            return false;
        }
        try {
            if (!file.exists()) {
                return false;
            }

        } catch (Exception e) {
            return false;
        }

        return true;
    }

    private void downLoadFile(String url, String fileName) {
     /*  //判断文件是否存在
       File file = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DOWNLOADS) + "//" + fileName);
        if (fileIsExists(file)) {
            Intent handlerIntent = new Intent(Intent.ACTION_VIEW, Uri.fromFile(file));
            context.startActivity(handlerIntent);
            return;
        }*/
        //权限
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                //没有权限则申请权限
                ToastUtil.show(context, R.string.no_store_permission);
                ActivityCompat.requestPermissions(context, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                return;
            }
        }
        Uri uri = Uri.parse(url);
        DownloadManager.Request request = new DownloadManager.Request(uri);
        // 允许媒体扫描，根据下载的文件类型被加入相册、音乐等媒体库
        request.allowScanningByMediaScanner();
        // 设置通知的显示类型，下载进行时和完成后显示通知
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        // 设置通知栏的标题，如果不设置，默认使用文件名
//        request.setTitle("This is title");
        // 设置通知栏的描述
//        request.setDescription("This is description");
        request.setAllowedOverMetered(true);//允许在计费流量下下载
        // 允许该记录在下载管理界面可见
        request.setVisibleInDownloadsUi(true);
        // 允许漫游时下载
        request.setAllowedOverRoaming(true);
        // 允许下载的网路类型 ,默认都可以下载
        //request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI);
        // 设置下载文件保存的路径和文件名
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, fileName);
//        另外可选一下方法，自定义下载路径
//        request.setDestinationUri()
//        request.setDestinationInExternalFilesDir()
        final DownloadManager downloadManager = (DownloadManager) context.getSystemService(DOWNLOAD_SERVICE);
        // 添加一个下载任务
        downloadManager.enqueue(request);
        ToastUtil.show(context, "开始下载...");

        // 使用
        DownloadCompleteReceiver receiver = new DownloadCompleteReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(DownloadManager.ACTION_DOWNLOAD_COMPLETE);
        context.registerReceiver(receiver, intentFilter);
    }

    private class DownloadCompleteReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent != null) {
                if (DownloadManager.ACTION_DOWNLOAD_COMPLETE.equals(intent.getAction())) {
                    long downloadId = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
                    DownloadManager downloadManager = (DownloadManager) context.getSystemService(DOWNLOAD_SERVICE);
                    String type = downloadManager.getMimeTypeForDownloadedFile(downloadId);
                    if (TextUtils.isEmpty(type)) {
                        type = "*/*";
                    }
                    Uri uri = downloadManager.getUriForDownloadedFile(downloadId);
                    if (uri != null) {
                        Intent handlerIntent = new Intent(Intent.ACTION_VIEW);
                        handlerIntent.setDataAndType(uri, type);
                        context.startActivity(handlerIntent);
                    }
                }
            }
        }
    }

    private void downLoadFile2(String url, final String fileNmae) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style
                .dialog_appcompat_theme_fullscreen);
        LayoutInflater inflater = LayoutInflater.from(context);
        View v = inflater.inflate(R.layout.layout_dialog_file_preview, null);

        WebView mWebView = (WebView) v.findViewById(R.id.file_preview_wv);

        mWebView.loadUrl(url);
        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return super.shouldOverrideUrlLoading(view, url);
            }
        });
        mWebView.setDownloadListener(new DownloadListener() {
            @Override
            public void onDownloadStart(String url, String userAgent, String contentDisposition,
                                        String mimetype, long contentLength) {
                Uri parse = Uri.parse(url);
                Intent intent = new Intent(Intent.ACTION_VIEW, parse);
                intent.setData(Uri.parse("文件"));
                context.startActivity(intent);

            }
        });

        Button finishBt = (Button) v.findViewById(R.id.dialog_btn_cancel);
        final Dialog dialog = builder.create();
        dialog.show();
        dialog.getWindow().setContentView(v);
        finishBt.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                dialog.dismiss();
            }
        });
    }

    private void showPicDialog(final String picUrl) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style
                .dialog_fullscree_animation);
        final View v = LayoutInflater.from(context).inflate(R.layout.layout_dialog_file_detail, null);
        final TouchImageView fileDetailSDV = (TouchImageView) v.findViewById(R.id
                .card_detail_file_sdv);
        final ImageView loadView = (ImageView) v.findViewById(R.id.loading_view);
        Animation operatingAnim = AnimationUtils.loadAnimation(context, R.anim.anim_rotate);
        operatingAnim.setInterpolator(new LinearInterpolator());
        loadView.startAnimation(operatingAnim);
        final Dialog dialog = builder.create();
        Picasso.with(context).load(picUrl)
                .error(R.drawable.ic_def_logo_720_288)
                .into(fileDetailSDV, new Callback() {
                    @Override
                    public void onSuccess() {
                        loadView.clearAnimation();
                        loadView.setVisibility(View.GONE);
                    }

                    @Override
                    public void onError() {
                        loadView.clearAnimation();
                        loadView.setVisibility(View.GONE);
                        ToastUtil.show(context, "图片加载失败");
                    }
                });
        dialog.show();
        dialog.getWindow().setContentView(v);
        fileDetailSDV.setOnImageClickListener(new TouchImageView.OnClickListener() {
            @Override
            public void onClick() {
                dialog.dismiss();
            }
        });
        fileDetailSDV.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                showSaveDialog(picUrl);
                return false;
            }
        });
    }

    public class ViewHolder {
        private SimpleDraweeView filePicIv;
        public Button removeBt;
        public TextView centerTitleTv;
        public TextView fileSizeTv;
    }

    //保存图片
    private void showSaveDialog(final String finalUrl) {
        new MaterialDialog.Builder(context)
                .backgroundColor(Color.WHITE)
                .items(new String[]{"保存图片"})// 列表数据
                .itemsCallback(new MaterialDialog.ListCallback() {
                    @Override
                    public void onSelection(MaterialDialog dialog, View itemView,
                                            int position, CharSequence text) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            if (ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                                //没有权限则申请权限
                                ToastUtil.show(context, R.string.no_store_permission);
                                ActivityCompat.requestPermissions(context, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                            } else {
                                //有权限直接执行,docode()不用做处理
                                ImageUtil.savePicUrl(context, finalUrl);
                            }
                        } else {
                            //小于6.0，不用申请权限，直接执行
                            ImageUtil.savePicUrl(context, finalUrl);
                        }
                    }
                })
                .show();
    }

}














