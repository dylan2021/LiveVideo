package com.lechange.demo.adapter;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
    private List<DeviceDetailListData.ResponseData.DeviceListBean.ChannelsBean> datas;

    public MainVideoListAdapter(Context mContext, List<DeviceDetailListData.ResponseData.DeviceListBean.ChannelsBean> datas) {
        this.mContext = mContext;
        this.datas = datas;
    }

    @NonNull
    @Override
    public ChannelHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_channel_list, parent, false);
        return new MainVideoListAdapter.ChannelHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ChannelHolder holder, final int position) {
        if ("online".equals(datas.get(position).status)) {
            holder.ivPlay.setVisibility(View.VISIBLE);
            holder.rlOffline.setVisibility(View.GONE);
        } else {
            holder.ivPlay.setVisibility(View.GONE);
            holder.rlOffline.setVisibility(View.VISIBLE);
        }
        holder.tvName.setText(datas.get(position).channelName);
        //获取设备缓存信息
        DeviceLocalCacheData deviceLocalCacheData = new DeviceLocalCacheData();
        deviceLocalCacheData.setDeviceId(datas.get(position).deviceId);
        deviceLocalCacheData.setChannelId(datas.get(position).channelId);
        DeviceLocalCacheService deviceLocalCacheService = ClassInstanceManager.newInstance().getDeviceLocalCacheService();
        deviceLocalCacheService.findLocalCache(deviceLocalCacheData, new IGetDeviceInfoCallBack.IDeviceCacheCallBack() {
            @Override
            public void deviceCache(DeviceLocalCacheData deviceLocalCacheData) {
                BitmapDrawable bitmapDrawable = MediaPlayHelper.picDrawable(deviceLocalCacheData.getPicPath());
                if (bitmapDrawable != null) {
                    holder.ivBg.setImageDrawable(bitmapDrawable);
                } else {
                    holder.ivBg.setImageDrawable(mContext.getDrawable(R.mipmap.lc_demo_default_bg));
                }
            }

            @Override
            public void onError(Throwable throwable) {
                holder.ivBg.setImageDrawable(mContext.getDrawable(R.mipmap.lc_demo_default_bg));
            }
        });
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onChannelClickListener != null) {
                    onChannelClickListener.onChannelClick(position);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return datas == null ? 0 : datas.size();
    }

    static class ChannelHolder extends RecyclerView.ViewHolder {
        ImageView ivBg;
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
        }
    }

    private OnChannelClickListener onChannelClickListener;

    public interface OnChannelClickListener {
        void onChannelClick(int position);
    }

    public void setOnItemClickListener(OnChannelClickListener onChannelClickListener) {
        this.onChannelClickListener = onChannelClickListener;
    }

}
