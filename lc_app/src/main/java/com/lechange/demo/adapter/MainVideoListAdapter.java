package com.lechange.demo.adapter;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.common.openapi.ClassInstanceManager;
import com.common.openapi.DeviceLocalCacheService;
import com.common.openapi.IGetDeviceInfoCallBack;
import com.common.openapi.entity.DeviceDetailListData;
import com.common.openapi.entity.DeviceLocalCacheData;
import com.lechange.demo.R;
import com.lechange.demo.tools.MediaPlayHelper;

import java.util.List;


public class MainVideoListAdapter extends RecyclerView.Adapter<MainVideoListAdapter.ChannelHolder> {
    private Context mContext;
    private List<DeviceDetailListData.ResponseData.DeviceListBean.ChannelsBean> list;
    private final Drawable picDef;

    public MainVideoListAdapter(Context mContext, List<DeviceDetailListData.ResponseData.DeviceListBean.ChannelsBean> list) {
        this.mContext = mContext;
        this.list = list;
        picDef = mContext.getDrawable(R.mipmap.lc_video_def_bg);
    }

    @NonNull
    @Override
    public ChannelHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_channel_list, parent, false);
        return new MainVideoListAdapter.ChannelHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ChannelHolder holder, final int position) {
        String status = list.get(position).status;
        if ("online".equals(status)) {
            holder.ivPlay.setVisibility(View.VISIBLE);
            holder.rlOffline.setVisibility(View.GONE);
        } else {
            holder.ivPlay.setVisibility(View.GONE);
            holder.rlOffline.setVisibility(View.VISIBLE);
        }
        if (list != null) {
            holder.tvName.setText(list.get(position).channelName);
            //获取设备缓存信息
            DeviceLocalCacheData deviceLocalCacheData = new DeviceLocalCacheData();
            deviceLocalCacheData.setDeviceId(list.get(position).deviceId);
            deviceLocalCacheData.setChannelId(list.get(position).channelId);
            DeviceLocalCacheService deviceLocalCacheService = ClassInstanceManager.newInstance().getDeviceLocalCacheService();
            deviceLocalCacheService.findLocalCache(deviceLocalCacheData, new IGetDeviceInfoCallBack.IDeviceCacheCallBack() {
                @Override
                public void deviceCache(DeviceLocalCacheData deviceLocalCacheData) {
                    String picPath = deviceLocalCacheData.getPicPath();
                    BitmapDrawable bitmapDrawable = MediaPlayHelper.picDrawable(picPath);
                    if (bitmapDrawable != null) {
                        holder.ivBg.setBackground(bitmapDrawable);
                    } else {
                        holder.ivBg.setImageDrawable(picDef);
                    }
                }

                @Override
                public void onError(Throwable throwable) {
                    holder.ivBg.setImageDrawable(picDef);
                }
            });
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onChannelClickListener != null) {
                        onChannelClickListener.onChannelClick(position,false);
                    }
                }
            });
           /* holder.simple_play_bt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onChannelClickListener != null) {
                        onChannelClickListener.onChannelClick(position,true);
                    }
                }
            });*/
        }
    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }

    public void setData(List<DeviceDetailListData.ResponseData.DeviceListBean.ChannelsBean> channels) {
        list=channels;
        notifyDataSetChanged();
    }

    static class ChannelHolder extends RecyclerView.ViewHolder {
        ImageView ivBg;
        Button simple_play_bt;
        RelativeLayout rlOffline;
        TextView tvOffline;
        ImageView ivPlay;
        RelativeLayout rlDetail;
        TextView tvName;

        public ChannelHolder(View itemView) {
            super(itemView);
            ivBg = itemView.findViewById(R.id.iv_bg);
            rlOffline = itemView.findViewById(R.id.rl_offline);
            tvOffline = itemView.findViewById(R.id.tv_offline);
            ivPlay = itemView.findViewById(R.id.iv_play);
            rlDetail = itemView.findViewById(R.id.rl_detail);
            tvName = itemView.findViewById(R.id.tv_name);
            simple_play_bt = itemView.findViewById(R.id.simple_play_bt);
        }
    }

    private OnChannelClickListener onChannelClickListener;

    public interface OnChannelClickListener {
        void onChannelClick(int position,boolean isSimplePlay);
    }

    public void setOnItemClickListener(OnChannelClickListener onChannelClickListener) {
        this.onChannelClickListener = onChannelClickListener;
    }

}
