package com.android.livevideo.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.android.livevideo.act_other.PictBean;
import com.android.livevideo.R;
import com.android.livevideo.core.utils.FileUtil;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

/**
 * Gool
 */
public class QuickConsultationAdapter extends BaseAdapter {
    Context context;
    List<PictBean> list;
    LayoutInflater layoutInflater;
    OnGridViewItemClickListener listener;
    private ImageLoader imageLoader = ImageLoader.getInstance();

    public QuickConsultationAdapter(Context context, List<PictBean> list,
                                    OnGridViewItemClickListener listener) {
        this.list = list;
        this.context = context;
        layoutInflater = LayoutInflater.from(context);
        this.listener = listener;
    }

    @Override
    public int getCount() {
        return list != null ? list.size() : 0;
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = layoutInflater.inflate(R.layout.inquiry_horizontal_gridview_item, null);
            holder.imageView = (ImageView) convertView.findViewById(R.id
                    .iv_horizontal_gridview_item);
            holder.deleteButton = (ImageButton) convertView.findViewById(R.id
                    .btn_horizontal_gridview_item);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        if (list != null && list.size() != 0) {
            PictBean pictBean = list.get(position);
            imageLoader.displayImage("file://" + pictBean.getLocalURL(), holder.imageView,
                    FileUtil.getModelOptions(R.drawable.ic_error_img, 0));
            // holder.imageView.setImageBitmap(FileTools.getThumbnailByFileUrl(context
            // .getContentResolver(),pictureBean.getLocalURL()));

            holder.imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onGridViewItemClick(position, false);
                }
            });
            holder.deleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onGridViewItemClick(position, true);
                }
            });
        } else {
            return null;
        }
        return convertView;
    }

    class ViewHolder {
        ImageView imageView;
        ImageButton deleteButton;
    }

    //item点击后时行处理
    public interface OnGridViewItemClickListener {
        public void onGridViewItemClick(int position, Boolean isDelete);
    }
}