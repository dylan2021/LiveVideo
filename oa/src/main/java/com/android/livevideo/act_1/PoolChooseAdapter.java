package com.android.livevideo.act_1;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.android.livevideo.R;
import com.android.livevideo.bean.PoolInfo;
import com.android.livevideo.core.utils.KeyConst;
import com.android.livevideo.core.utils.TextUtil;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Dylan
 */

public class PoolChooseAdapter extends BaseAdapter {
    private LayoutInflater mInflater;
    private List<PoolInfo> emplyeeInfoList = new ArrayList<>();
    private PoolChooseActivity context;
    private int TYPE;
    private String emptyStr = "       ";

    public PoolChooseAdapter(PoolChooseActivity context, int TYPE) {
        this.TYPE = TYPE;
        this.context = context;
        mInflater = LayoutInflater.from(context);
    }

    public void setData(List<PoolInfo> data) {
        this.emplyeeInfoList = data;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return emplyeeInfoList == null ? 0 : emplyeeInfoList.size();
    }

    @Override
    public Object getItem(int i) {
        return emplyeeInfoList == null ? null : emplyeeInfoList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        final PoolInfo info = emplyeeInfoList.get(position);
        final ViewHolder holder;
        if (convertView == null) {
            holder = new PoolChooseAdapter.ViewHolder();

            convertView = mInflater.inflate(R.layout.item_pool_choosed, viewGroup, false);
            holder.tv1 = convertView.findViewById(R.id.item_tv_1);
            holder.tv2 = convertView.findViewById(R.id.item_tv_2);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        if (info != null) {
            String title1 = "", title2 = "";
            if (TYPE == 0) {//池号
                title1 = info.getName();
                String lastUseTime = info.getLastUseTime();
                title2 = TextUtil.isEmpty(lastUseTime) ? "无入池记录" : lastUseTime + "（最近一次入池记录）";
            } else if (TYPE == 1) {//配料桶次
                title1 = info.getIngredientNo();
                String name = info.getProductName() + "/" + TextUtil.remove_N(info.getSpecification());
                name = name.length() > 15 ? name.substring(0, 14) + "..." : name;
                title2 = name + emptyStr + info.getMixDate();//,规格:
            } else if (TYPE == 2) {//过磅单号
                PoolInfo.ObjectBean obj = info.getObject();
                title1 = info.getProcNum() + "(" + obj.getProductName() + ")";//单号(名称)
                title2 = "过磅时间：" + obj.getWeighDate() + emptyStr + "车牌号：" + obj.getNumberPlate();
            } else if (TYPE == 3) {
                title1 = info.getProductDetailNo() + "(" + info.getProductName() + ")";//单号(名称)
                String brand = info.getBrand();
                title2 = "规格：" + info.getSpecification() + emptyStr + "品牌：" + TextUtil.remove_N(brand);
            } else {
                PoolInfo.ObjectBean obj = info.getObject();
                title1 = info.getProcNum() + "(" + obj.getProductName() + ")";//单号(名称)
                title2 = "检验时间：" + obj.getCheckDate() + emptyStr + "车牌号：" + obj.getNumberPlate();
            }

            holder.tv1.setText(title1);
            holder.tv2.setText(title2);
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent = new Intent();
                    //返回选择的数据
                    Bundle bundle = new Bundle();
                    bundle.putSerializable(KeyConst.OBJ_INFO, (Serializable) info);//序列化,要注意转化(Serializable)
                    intent.putExtras(bundle);
                    context.setResult(TYPE, intent);
                    context.finish();
                }
            });
        }
        return convertView;
    }

    public class ViewHolder {
        private TextView tv1, tv2;

    }

}
