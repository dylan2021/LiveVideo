/*
 * 	Flan.Zeng 2011-2016	http://git.oschina.net/signup?inviter=flan
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.android.livevideo.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.livevideo.R;
import com.android.livevideo.bean.ProjDeptInfo;

import java.util.List;

/**
 * Gool Lee
 */

public class ChooseDataAdapter extends RecyclerView.Adapter {

    private boolean isFirstLuncher;
    private Activity context;
    private List<ProjDeptInfo> contacts;

    public ChooseDataAdapter(List<ProjDeptInfo> contacts, Activity c, boolean isFirstLuncher) {
        this.contacts = contacts;
        this.context = c;
        this.isFirstLuncher = isFirstLuncher;
    }

    public void setData(List<ProjDeptInfo> contacts) {
        this.contacts = contacts;
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = View.inflate(context, R.layout.item_data_choose, null);
        return new MyHolder(itemView);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, final int position) {
        final ProjDeptInfo data = contacts.get(position);
        MyHolder holder = (MyHolder) viewHolder;
        //显示index
        if (data != null) {



            if (mOnItemClickListener != null) {
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mOnItemClickListener.onItemClick(view, data.getId(),data.getProjectName());
                    }
                });
            }
        }
    }

    @Override
    public int getItemCount() {
        return contacts.size();
    }

    private class MyHolder extends RecyclerView.ViewHolder {

        TextView inverseTv, belowContractTv, startEndTv;
        TextView tv_name, periodTv, lenthTv;
        View itemView;

        public MyHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
            inverseTv = (TextView) itemView.findViewById(R.id.all_inverse_tv);
            startEndTv = (TextView) itemView.findViewById(R.id.all_perios_tv);//总工期
            belowContractTv = (TextView) itemView.findViewById(R.id.below_contract_tv);//总长度
            lenthTv = (TextView) itemView.findViewById(R.id.start_date_tv);//总长度
            tv_name = (TextView) itemView.findViewById(R.id.name_tv);
            periodTv = (TextView) itemView.findViewById(R.id.period_tv);
        }
    }


    public interface OnItemClickListener {
        void onItemClick(View view, int position, String name);
    }

    private OnItemClickListener mOnItemClickListener;

    public void setOnItemClickListener(OnItemClickListener mOnItemClickListener) {
        this.mOnItemClickListener = mOnItemClickListener;
    }
}










