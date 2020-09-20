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

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.livevideo.R;
import com.android.livevideo.bean.Contact;

import java.util.List;

/**
 * Gool Lee
 */

public class MyRecyclerAdapter extends RecyclerView.Adapter {

    private List<Contact> contacts;

    public MyRecyclerAdapter(List<Contact> contacts) {
        this.contacts = contacts;
    }
    public void setData(List<Contact> contacts) {
        this.contacts = contacts;
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = View.inflate(parent.getContext(), R.layout.item_project_choose, null);
        return new MyHolder(itemView);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, final int position) {
        final Contact contact = contacts.get(position);
        MyHolder holder = (MyHolder) viewHolder;
        //显示index
        if (position == 0 || !contact.getIndex().equals(contacts.get(position - 1).getIndex())) {
            holder.tv_index.setVisibility(View.VISIBLE);
            holder.tv_index.setText(contact.getIndex());
        } else {
            holder.tv_index.setVisibility(View.GONE);
        }

        holder.tv_name.setText(contact.getName());

        if (mOnItemClickListener != null){
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mOnItemClickListener.onItemClick(view,contact.getId());
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return contacts.size();
    }

    private class MyHolder extends RecyclerView.ViewHolder {

        TextView tv_index;
        TextView tv_name;

        public MyHolder(View itemView) {
            super(itemView);
            tv_index = (TextView) itemView.findViewById(R.id.tv_index);
            tv_name = (TextView) itemView.findViewById(R.id.tv_name);
        }
    }


    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    private OnItemClickListener mOnItemClickListener;

    public void setOnItemClickListener(OnItemClickListener mOnItemClickListener) {
        this.mOnItemClickListener = mOnItemClickListener;
    }
}










