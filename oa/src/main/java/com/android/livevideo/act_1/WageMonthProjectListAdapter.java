package com.android.livevideo.act_1;

import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;


import com.android.livevideo.R;
import com.android.livevideo.bean.WageMonthProjectInfo;
import com.android.livevideo.core.utils.TextUtil;

import java.util.List;

/**
 * Dylan
 */

public class WageMonthProjectListAdapter extends BaseAdapter {
    LayoutInflater mInflater;
    List<WageMonthProjectInfo.RecordsBean> infoList ;
    FragmentActivity context;

    public WageMonthProjectListAdapter(FragmentActivity context) {
        this.context = context;
        mInflater = LayoutInflater.from(context);
    }

    public void setData(List<WageMonthProjectInfo.RecordsBean> data) {
        this.infoList = data;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return infoList == null ? 0 : infoList.size();
    }

    @Override
    public Object getItem(int i) {
        return infoList == null ? null : infoList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        final WageMonthProjectInfo.RecordsBean info = infoList.get(position);
        final ViewHolder holder;
        if (convertView == null) {
            holder = new WageMonthProjectListAdapter.ViewHolder();

            convertView = mInflater.inflate(R.layout.item_month_project_lv, viewGroup, false);
            holder.titleTv = convertView.findViewById(R.id.title_tv);
            holder.valueTv2 = convertView.findViewById(R.id.item_value_2);
            holder.valueTv3 = convertView.findViewById(R.id.item_value_3);

            holder.valueTv2_2 = convertView.findViewById(R.id.item_value_2_2);//计划数量
            holder.valueTv3_2 = convertView.findViewById(R.id.item_value_3_2);//复核数量
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        if (info != null) {
            String unit = TextUtil.remove_N(info.getUnit());
            holder.titleTv.setText(info.getProjectName());

            holder.valueTv2.setText(info.getTotalHourOnMonth() + "小时");
            holder.valueTv2_2.setText(info.getTotalHourlyWage() + "元");

            holder.valueTv3.setText(info.getTotalWageOnMonth() + unit);
            holder.valueTv3_2.setText(info.getTotalPieceWage() + "元");

        }
        return convertView;
    }


    public class ViewHolder {
        private TextView titleTv, valueTv1, valueTv2, valueTv3;
        private TextView valueTv1_2, valueTv2_2, valueTv3_2;

    }

}
