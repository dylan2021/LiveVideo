package com.android.livevideo.act_1;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.android.livevideo.R;
import com.android.livevideo.bean.MonthItemInfo;
import com.android.livevideo.util.Utils;

import java.util.List;

/**
 * Gool Lee
 * 安全生产
 */

public class WageMonthAdapter extends BaseAdapter {
    private LayoutInflater mInflater;
    private List<MonthItemInfo> list;
    private WageMonthActivity context;

    public WageMonthAdapter(WageMonthActivity context,
                            List<MonthItemInfo> datats) {
        this.context = context;
        mInflater = LayoutInflater.from(context);
        list = datats;
    }

    public void setData(List<MonthItemInfo> data) {
        this.list = data;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        if (list == null) {
            return 0;
        } else {
            return list.size();
        }
    }

    @Override
    public Object getItem(int i) {
        if (list == null) {
            return null;
        } else {
            return list.get(i);
        }
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        final MonthItemInfo info = list.get(position);
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.item_month_wage, viewGroup, false);
            holder.titleTv = convertView.findViewById(R.id.name_tv);
            holder.timeTv = convertView.findViewById(R.id.dialy_wage_item_time_tv);

            holder.totalTv = convertView.findViewById(R.id.total_wage_tv);
            holder.emplyeeNumberTv = convertView.findViewById(R.id.emplyee_number_tv);
            holder.statusTv = convertView.findViewById(R.id.status_tv);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        if (info != null) {

            holder.titleTv.setText(info.getHeadline());
            MonthItemInfo.ObjectBean object = info.getObject();
            holder.totalTv.setText("合计工资:" + object.getTotalWage() + "元");


            String statusStr = Utils.getStatusText(info.getStatus());
            int color = Utils.getStatusColor(context, info.getStatus());
            holder.statusTv.setText(statusStr);
            holder.statusTv.setTextColor(color);

            holder.emplyeeNumberTv.setText("参与人数:" + object.getPersonNum() + "人");
            holder.timeTv.setText(info.getCreateTime());

        }

        return convertView;
    }

    public class ViewHolder {
        private TextView titleTv, totalTv, statusTv, emplyeeNumberTv, timeTv, everyNumberWageTv;
    }

}
