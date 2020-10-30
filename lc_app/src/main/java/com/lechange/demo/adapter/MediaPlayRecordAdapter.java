package com.lechange.demo.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.common.openapi.entity.RecordsData;
import com.lechange.demo.R;
import com.lechange.demo.tools.ImageHelper;

import java.util.List;

public class MediaPlayRecordAdapter extends RecyclerView.Adapter<MediaPlayRecordAdapter.MediaPlayHolder> {
    private List<RecordsData> recordsBeanList;
    private Context context;

    public MediaPlayRecordAdapter(List<RecordsData> recordsBeanList, Context context) {
        this.recordsBeanList = recordsBeanList;
        this.context = context;
    }

    @NonNull
    @Override
    public MediaPlayHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_media_play_record, parent, false);
        return new MediaPlayRecordAdapter.MediaPlayHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MediaPlayHolder holder, final int position) {
        if (position == recordsBeanList.size()) {
            holder.tvLoadingMore.setVisibility(View.VISIBLE);
            holder.tvLoadingMore.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (loadMoreClickListener != null) {
                        loadMoreClickListener.loadMore();
                    }
                }
            });
        } else {
            String time = recordsBeanList.get(position).beginTime.substring(11);
            holder.tvTime.setText(time);
            if (recordsBeanList.get(position).recordType == 0) {
                //云录像
                holder.tvLoadingMore.setVisibility(View.GONE);
                try {
                    ImageHelper.loadCacheImage(recordsBeanList.get(position).thumbUrl, recordsBeanList.get(position).deviceId, recordsBeanList.get(position).deviceId, position, new Handler() {
                        @Override
                        public void handleMessage(Message msg) {
                            super.handleMessage(msg);
                            try {
                                if (recordsBeanList.get(msg.arg1).thumbUrl.hashCode() == msg.what && msg.obj != null) {
                                    holder.ivImg.setImageDrawable((Drawable) msg.obj);
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                //设备录像
                holder.tvLoadingMore.setVisibility(View.GONE);
                holder.ivImg.setImageDrawable(context.getDrawable(R.mipmap.lc_demo_default_bg));
            }
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onItemClickListener != null) {
                        onItemClickListener.click(recordsBeanList.get(position).recordType, position);
                    }
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return recordsBeanList == null ? 0 : (recordsBeanList.size() == 6 ? 7 : (recordsBeanList.size() + 1));
    }

    class MediaPlayHolder extends RecyclerView.ViewHolder {
        ImageView ivImg;
        TextView tvTime;
        TextView tvLoadingMore;

        public MediaPlayHolder(View itemView) {
            super(itemView);
            ivImg = itemView.findViewById(R.id.iv_img);
            tvTime = itemView.findViewById(R.id.tv_time);
            tvLoadingMore = itemView.findViewById(R.id.tv_loadingmore);
        }
    }

    private LoadMoreClickListener loadMoreClickListener;

    public interface LoadMoreClickListener {
        void loadMore();
    }

    public void setLoadMoreClickListener(LoadMoreClickListener loadMoreClickListener) {
        this.loadMoreClickListener = loadMoreClickListener;
    }


    private OnItemClickListener onItemClickListener;

    public interface OnItemClickListener {
        void click(int recordType, int position);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }
}
