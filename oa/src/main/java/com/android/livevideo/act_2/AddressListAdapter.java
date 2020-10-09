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

package com.android.livevideo.act_2;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;


import com.android.livevideo.R;
import com.android.livevideo.bean.EmployeeInfo;
import com.android.livevideo.bean.StatInfo;
import com.android.livevideo.core.utils.KeyConst;
import com.android.livevideo.util.Utils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class AddressListAdapter extends BaseExpandableListAdapter {
    private AddressListActivity context;
    private List<StatInfo> groupList = new ArrayList<>();
    private int type;

    public AddressListAdapter(AddressListActivity context) {
        this.context = context;
    }

    @Override
    public int getGroupCount() {
        return null == groupList ? 0 : groupList.size();
    }

    //        获取指定分组中的子选项的个数
    @Override
    public int getChildrenCount(int groupPosition) {
        if (groupList == null) {
            return 0;
        }
        StatInfo StatInfo = groupList.get(groupPosition);

        List<com.android.livevideo.bean.EmployeeInfo> childrenBeanList = StatInfo.getDetails();
        if (childrenBeanList == null || childrenBeanList.size() == 0) {
            return 0;
        }
        return childrenBeanList.size();
    }

    //        获取指定的分组数据
    @Override
    public Object getGroup(int groupPosition) {
        return groupList == null ? null : groupList.get(groupPosition);
    }

    // 获取指定分组中的指定子选项数据
    @Override
    public Object getChild(int groupPosition, int childPosition) {
        StatInfo StatInfo = groupList.get(groupPosition);
        if (groupList == null) {
            return null;
        }
        List<com.android.livevideo.bean.EmployeeInfo> childrenBeanList = StatInfo.getDetails();
        if (childrenBeanList == null || childrenBeanList.size() == 0) {
            return null;
        }
        return childrenBeanList.get(childPosition);
    }


    //获取指定分组的ID, 这个ID必须是唯一的
    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    //获取子选项的ID, 这个ID必须是唯一的
    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    //分组和子选项是否持有稳定的ID, 就是说底层数据的改变会不会影响到它们
    @Override
    public boolean hasStableIds() {
        return true;
    }

    // 获取显示指定分组的视图
    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        GroupViewHolder groupViewHolder;
        final Context context = parent.getContext();
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.e_lv_partent_item, parent, false);
            groupViewHolder = new GroupViewHolder();
            groupViewHolder.tvTitle = (TextView) convertView.findViewById(R.id.label_group_title);
            groupViewHolder.tvNumber = (TextView) convertView.findViewById(R.id.group_number_tv);
            convertView.setTag(groupViewHolder);
        } else {
            groupViewHolder = (GroupViewHolder) convertView.getTag();
        }
        StatInfo statInfo = groupList.get(groupPosition);
        if (statInfo != null) {

            List<EmployeeInfo> details = statInfo.getDetails();
            String name = statInfo.getName();
            groupViewHolder.tvTitle.setText(name);
            groupViewHolder.tvNumber.setSelected(isExpanded);
     /*       if (details == null || details.size() == 0) {
            Log.d("市场部数据", name + ":" + details);
                groupViewHolder.tvNumber.setText("0人");
                convertView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ToastUtil.show(context, "无人员");
                    }
                });
            } else {
                groupViewHolder.tvNumber.setText(details.size() + "人");
            }*/
            groupViewHolder.tvNumber.setText(details == null ? "0人" : details.size() + "人");
        }
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        ChildViewHolder childViewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.e_lv_child_item, parent, false);
            childViewHolder = new ChildViewHolder();
            childViewHolder.childTitleTv = (TextView) convertView.findViewById(R.id.expand_child);
            childViewHolder.childNumberTv = (TextView) convertView.findViewById(R.id.child_number_tv);
            childViewHolder.positionNameTv = (TextView) convertView.findViewById(R.id.position_name_tv);
            childViewHolder.phonePic = convertView.findViewById(R.id.child_phone_iv);
            convertView.setTag(childViewHolder);

        } else {
            childViewHolder = (ChildViewHolder) convertView.getTag();
        }
        if (type == 1) {
            childViewHolder.phonePic.setVisibility(View.GONE);
        }
        StatInfo StatInfo = groupList.get(groupPosition);
        if (StatInfo != null) {
            List<EmployeeInfo> itemBeanList = StatInfo.getDetails();
            //子类
            if (itemBeanList != null && itemBeanList.size() > 0) {

                final EmployeeInfo detailsBean = itemBeanList.get(childPosition);
                if (null != detailsBean) {
                    final String phoneNumber = detailsBean.getEmployeeMobile();
                    childViewHolder.childTitleTv.setText(detailsBean.getEmployeeName());
                    childViewHolder.childNumberTv.setText(phoneNumber);
                    /*String positionName = detailsBean.getPositionName();//职位
                    childViewHolder.positionNameTv.setText(null == positionName ? "" : "(" + positionName + ")");*/

                    convertView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (type == 1) {
                                //返回选择的数据
                                Bundle bundle = new Bundle();
                                bundle.putSerializable(KeyConst.OBJ_INFO, (Serializable)
                                        detailsBean);//序列化,要注意转化(Serializable)
                                Intent intent = new Intent();
                                intent.putExtras(bundle);
                                context.setResult(3, intent);
                                context.finish();
                            } else {
                                Utils.callPhone(context, phoneNumber);
                            }
                        }
                    });
                }
            }
        }
        return convertView;
    }

    //指定位置上的子元素是否可选中
    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    public void setData(List<StatInfo> result, int type) {
        this.groupList = result;
        this.type = type;
        notifyDataSetChanged();
    }

    static class GroupViewHolder {
        TextView tvTitle, tvNumber;
    }

    static class ChildViewHolder {
        TextView childTitleTv, positionNameTv, childNumberTv;
        View phonePic;

    }
}














