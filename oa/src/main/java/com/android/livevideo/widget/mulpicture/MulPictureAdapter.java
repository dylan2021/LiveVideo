package com.android.livevideo.widget.mulpicture;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.GridView;
import android.widget.ImageView;

import com.android.livevideo.act_other.PictBean;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;

import com.android.livevideo.R;
import com.android.livevideo.core.utils.FileUtil;

/**
 * Created by jztdzsw on 15-8-16.
 */
public class MulPictureAdapter extends BaseAdapter {

    private Context context;
    private LayoutInflater inflater;
    private GridView.LayoutParams mItemLayoutParams;

    private List<PictBean> pictures = new ArrayList<PictBean>();
    public Bitmap bitmaps[];
    OnItemClickClass onItemClickClass; //点击后进行处理

    ImageLoader imageLoader = ImageLoader.getInstance();
    List<View> holderlist;
    private int index = -1;

    private Boolean isShowCamera;
    private List<PictBean> select;

    public MulPictureAdapter(Context context, List<PictBean> list, OnItemClickClass onItemClickClass, boolean isShowCamera, List<PictBean> select) {
        this.pictures = list;
        this.context = context;
        this.onItemClickClass = onItemClickClass;
        this.isShowCamera = isShowCamera;
        holderlist = new ArrayList<View>();
        inflater = LayoutInflater.from(context);
        this.select = select;
    }


    @Override
    public int getCount() {
        return pictures.size() + 1;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {

        ViewHolder holder = null;

        if (position != index && position > index) {
            index = position;
            if (position == 0) {
                view = LayoutInflater.from(context).inflate(R.layout.mul_picture_item_camera, null);
                view.setTag(null);
                holderlist.add(view);
            } else {
                view = LayoutInflater.from(context).inflate(R.layout.inquiry_mul_picture_item, null);
                holder = new ViewHolder();
                holder.imageView = (ImageView) view.findViewById(R.id.iv_mul_picture_item);
                holder.checkImg = (CheckBox) view.findViewById(R.id.cb_mul_picture_item);
                holder.imageOverLay = (View) view.findViewById(R.id.view_mul_picture_item);
                view.setTag(holder);
                holderlist.add(view);
            }

        } else {
            holder = (ViewHolder) holderlist.get(position).getTag();
            view = holderlist.get(position);
        }


        if (holder != null) {
            PictBean pictBean = pictures.get(position - 1);
            for(int i =0; i< select.size();i++){
                if(pictBean.getLocalURL().equals(select.get(i).getLocalURL())){
                    holder.checkImg.setChecked(true);
                    holder.imageOverLay.setVisibility(View.VISIBLE);
                    break;
                }
            }
            imageLoader.displayImage("file://" + pictBean.getLocalURL(), holder.imageView, FileUtil.getModelOptions(R.drawable.ic_error_img, 0));
            view.setOnClickListener(new OnPhotoClick(position, holder.checkImg, holder.imageOverLay));
        } else {
            view.setOnClickListener(new OnPhotoClick(position, null, null));
        }

        return view;
    }

    class ViewHolder {
        ImageView imageView; //图片
        CheckBox checkImg; //选择框
        View imageOverLay; //覆盖层
    }


    // item点击事件
    class OnPhotoClick implements View.OnClickListener {
        int position;
        CheckBox checkBox;
        View imageOverLay;

        public OnPhotoClick(int position, CheckBox checkBox, View overLay) {
            this.position = position;
            this.checkBox = checkBox;
            this.imageOverLay = overLay;
        }

        @Override
        public void onClick(View v) {
            if (pictures != null && onItemClickClass != null) {
                onItemClickClass.OnItemClick(v, position, checkBox, imageOverLay);
            }
        }
    }

    //item点击后时行处理
    public interface OnItemClickClass {
        public void OnItemClick(View v, int Position, CheckBox checkBox, View imageOverLay);
    }
}
