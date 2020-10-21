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

package com.android.livevideo.act_0;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.android.livevideo.R;
import com.android.livevideo.act_other.BaseFgActivity;
import com.android.livevideo.act_other.SeePicActivity;
import com.android.livevideo.bean.MsgInfo;
import com.android.livevideo.core.utils.KeyConst;
import com.android.livevideo.util.Utils;
import com.facebook.drawee.view.SimpleDraweeView;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;


/**
 * @author Goll Lee
 */
public class MsgAdapter extends BaseAdapter {
    private List<MsgInfo> msgList;
    private BaseFgActivity context;
    private int TYPE = 0;

    public MsgAdapter(BaseFgActivity context) {
        super();
        this.context = context;

    }

    public void setDate(List<MsgInfo> fileInfoList, int TYPE) {
        this.TYPE = TYPE;
        this.msgList = fileInfoList;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        if (msgList != null) {
            return msgList.size();
        }
        return 0;
    }


    @Override
    public Object getItem(int position) {
        if (msgList != null) {
            return msgList.get(position);
        }
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, final ViewGroup parent) {
        final MsgInfo info = msgList.get(position);
        final ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(context)
                    .inflate(R.layout.item_msg_lv_1, parent, false);
            holder.titleTv = (TextView) convertView.findViewById(R.id.msg_item_title_tv);
            holder.sdvPic = (SimpleDraweeView) convertView.findViewById(R.id.msg_sdv_iv);
            holder.statusTv = (TextView) convertView.findViewById(R.id.msg_item_time_tv);

            holder.tv1 = (TextView) convertView.findViewById(R.id.msg_item_tv_1);
            holder.tv2 = (TextView) convertView.findViewById(R.id.msg_item_tv_2);
            holder.tv3 = (TextView) convertView.findViewById(R.id.msg_item_tv_3);
            holder.redTag = convertView.findViewById(R.id.red_tag);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        if (info != null) {
            int resultStatus = info.resultStatus;
            //holder.redTag.setVisibility("1".equals(isRead) ? View.VISIBLE : View.GONE);

            holder.titleTv.setText(info.camera);
            int color = Utils.getStatusColor(context, resultStatus);
            holder.statusTv.setText(Utils.getStatusText(resultStatus));
            holder.statusTv.setTextColor(color);

            double resultValue = info.resultValue;
            NumberFormat nf = NumberFormat.getPercentInstance();
            nf.setMaximumFractionDigits(2);

            holder.tv1.setText("检查时间:"+info.checkTime);
            holder.tv2.setText("开裂相似度:" + nf.format(resultValue));

            final String uriString = info.resultPic;
            holder.sdvPic.setImageURI(uriString);
            ArrayList<String> urls = new ArrayList<>();
            urls.add(uriString);
            holder.sdvPic.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(context, SeePicActivity.class);
                    i.putExtra(KeyConst.type, 1);
                    i.putStringArrayListExtra(KeyConst.LIST_STR, urls);
                    i.putExtra(KeyConst.selectPosition, 0);
                    context.startActivity(i);
                }
            });

        }
        return convertView;
    }

    private class ViewHolder {
        private TextView titleTv, statusTv, tv1, tv2, tv3, redTag;
        private SimpleDraweeView sdvPic;

    }

}














