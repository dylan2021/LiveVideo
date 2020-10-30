package com.lechange.demo.adapter;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.common.openapi.ClassInstanceManager;
import com.common.openapi.DeviceLocalCacheService;
import com.common.openapi.IGetDeviceInfoCallBack;
import com.common.openapi.entity.DeviceDetailListData;
import com.common.openapi.entity.DeviceLocalCacheData;
import com.lechange.demo.R;
import com.lechange.demo.tools.MediaPlayHelper;
import com.lechange.demo.view.SpacesItemDecoration;

import java.util.List;

public class DeviceListAdapter extends RecyclerView.Adapter<DeviceListAdapter.DeviceListHolder> {

    public Context mContext;
    private List<DeviceDetailListData.ResponseData.DeviceListBean> datas;
    private SpacesItemDecoration decoration = new SpacesItemDecoration(20);

    public DeviceListAdapter(Context mContext, List<DeviceDetailListData.ResponseData.DeviceListBean> datas) {
        this.mContext = mContext;
        this.datas = datas;
    }

    @NonNull
    @Override
    public DeviceListHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_device_list, parent, false);
        return new DeviceListHolder(mContext, view);
    }

    @Override
    public void onBindViewHolder(@NonNull final DeviceListHolder holder, final int position) {
        holder.tvName.setText(datas.get(position).name);
        holder.ivDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemClickListener != null) {
                    onItemClickListener.onSettingClick(position);
                }
            }
        });
        if (datas.get(position).channels.size() > 1) {
            //多通道
            holder.ivDetail.setVisibility(View.VISIBLE);
            holder.rlDetail.setVisibility(View.GONE);
            holder.rcvChannel.setVisibility(View.VISIBLE);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
            linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
            holder.rcvChannel.setLayoutManager(linearLayoutManager);
            holder.rcvChannel.removeItemDecoration(decoration);
            holder.rcvChannel.addItemDecoration(decoration);
            ChannelListAdapter channelListAdapter = new ChannelListAdapter(mContext, datas.get(position).channels);
            holder.rcvChannel.setAdapter(channelListAdapter);
            channelListAdapter.setOnItemClickListener(new ChannelListAdapter.OnChannelClickListener() {
                @Override
                public void onChannelClick(int channelPosition) {
                    if (onItemClickListener != null) {
                        onItemClickListener.onChannelClick(position, channelPosition);
                    }
                }
            });
        } else if (datas.get(position).channels.size() == 0){
            //多通道NVR,但是没有通道数
            holder.ivDetail.setVisibility(View.VISIBLE);
            holder.ivPlay.setVisibility(View.GONE);
            holder.rcvChannel.setVisibility(View.GONE);
            holder.rlDetail.setVisibility(View.VISIBLE);
            holder.ivBg.setImageDrawable(mContext.getDrawable(R.mipmap.lc_demo_default_bg));
            holder.rlOffline.setVisibility(View.VISIBLE);
            holder.rlOffline.setBackground(mContext.getDrawable(R.color.transparent));
            holder.tvOffline.setText(R.string.lc_demo_device_nvr_no_channel);
            holder.rlDetail.setOnClickListener(null);
        } else{
            //单通道
            holder.ivDetail.setVisibility(View.VISIBLE);
            holder.rlDetail.setVisibility(View.VISIBLE);
            holder.rcvChannel.setVisibility(View.GONE);
            if ("online".equals(datas.get(position).status)&&datas.get(position).channels.size()>0) {
                holder.ivPlay.setVisibility(View.VISIBLE);
                holder.rlOffline.setVisibility(View.GONE);
                holder.rlDetail.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (onItemClickListener != null) {
                            onItemClickListener.onDetailClick(position);
                        }
                    }
                });
            } else {
                holder.ivPlay.setVisibility(View.GONE);
                holder.rlOffline.setVisibility(View.VISIBLE);
                holder.rlOffline.setBackground(mContext.getDrawable(R.color.lc_demo_color_66000000));
                holder.tvOffline.setText(R.string.lc_demo_main_offline);
                holder.rlDetail.setOnClickListener(null);
            }
            //获取设备缓存信息
            DeviceLocalCacheData deviceLocalCacheData = new DeviceLocalCacheData();
            deviceLocalCacheData.setDeviceId(datas.get(position).deviceId);
            if (datas.get(position).channels != null && datas.get(position).channels.size() > 0) {
                deviceLocalCacheData.setChannelId(datas.get(position).channels.get(datas.get(position).checkedChannel).channelId);
            }
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
        }
    }

    @Override
    public int getItemCount() {
        return datas == null ? 0 : datas.size();
    }

    private OnItemClickListener onItemClickListener;

    public interface OnItemClickListener {
        void onSettingClick(int position);

        void onDetailClick(int position);

        void onChannelClick(int outPosition, int innerPosition);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    static class DeviceListHolder extends RecyclerView.ViewHolder {
        TextView tvName;
        ImageView ivDetail;
        ImageView ivBg;
        RelativeLayout rlOffline;
        TextView tvOffline;
        ImageView ivPlay;
        RelativeLayout rlDetail;
        RecyclerView rcvChannel;
        FrameLayout frDetail;

        public DeviceListHolder(Context context, View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tv_name);
            ivDetail = itemView.findViewById(R.id.iv_detail);
            ivBg = itemView.findViewById(R.id.iv_bg);
            rlDetail = itemView.findViewById(R.id.rl_detail);
            frDetail = itemView.findViewById(R.id.fr_detail);
            rlOffline = itemView.findViewById(R.id.rl_offline);
            tvOffline = itemView.findViewById(R.id.tv_offline);
            ivPlay = itemView.findViewById(R.id.iv_play);
            rcvChannel = itemView.findViewById(R.id.rcv_channel);
            frDetail.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    LinearLayout.LayoutParams mLayoutParams = (LinearLayout.LayoutParams) frDetail.getLayoutParams();
                    mLayoutParams.height = mLayoutParams.width * 9 / 16;
                    mLayoutParams.width = frDetail.getWidth(); // 屏幕宽度（像素）
                    mLayoutParams.setMargins(0, 0, 0, 0);
                    frDetail.setLayoutParams(mLayoutParams);
                }
            });
        }
    }


}
