package com.android.livevideo.act_1;

import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.android.livevideo.R;
import com.android.livevideo.bean.EmplyeeWageInfo;
import com.android.livevideo.core.utils.KeyConst;

import java.util.ArrayList;
import java.util.List;

/**
 * Gool Lee
 */

public class WageEmplyeeListAdapter extends BaseAdapter {
    LayoutInflater mInflater;
    List<EmplyeeWageInfo> emplyeeInfoList = new ArrayList<>();
    FragmentActivity context;
    private boolean isProgress = true;
    private int type, status;

    public WageEmplyeeListAdapter(FragmentActivity context, int type, int status) {
        this.context = context;
        this.type = type;
        this.status = status;
        mInflater = LayoutInflater.from(context);
    }

    public void setData(List<EmplyeeWageInfo> data) {
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
        final EmplyeeWageInfo info = emplyeeInfoList.get(position);
        final ViewHolder holder;
        if (convertView == null) {
            holder = new WageEmplyeeListAdapter.ViewHolder();

            convertView = mInflater.inflate(R.layout.item_daily_wage_emplyee, viewGroup, false);
            holder.nameTv = convertView.findViewById(R.id.emplyee_name_tv);
            holder.wageTv = convertView.findViewById(R.id.emplyee_wage_tv);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        if (info != null) {
            holder.nameTv.setText(info.getEmployeeName());

            if (type == -1) {
                holder.wageTv.setText(info.getPieceNum() + info.getUnit());
            } else {
                double wage = type == 1 ? info.getTotalWage() : info.getRealWage();
                holder.wageTv.setText(wage + "元");
                if (wage < 0) {
                    holder.wageTv.setTextColor(ContextCompat.getColor(context, R.color.red));
                }

            }

            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent();
                    intent.setClass(context, type < 2 ?
                            WageDailyEmplyeeDetailActivity.class :
                            WageMonthEmplyeeDetailActivity.class);
                    intent.putExtra(KeyConst.OBJ_INFO, info);
                    intent.putExtra(KeyConst.type, type);
                    intent.putExtra(KeyConst.status, status);
                    intent.putExtra(KeyConst.id, info.getId());
                    context.startActivity(intent);
                }
            });
        }
        return convertView;
    }


    public class ViewHolder {
        private TextView nameTv, timeTv, beginEndDateTv, tag1Tv, wageTv;

    }

}
