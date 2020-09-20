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

package com.android.livevideo.act_1;

import android.support.v7.widget.AppCompatCheckBox;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.livevideo.R;
import com.android.livevideo.act_other.BaseFgActivity;
import com.android.livevideo.bean.GroupInfo;
import com.android.livevideo.core.utils.TextUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Gool
 */
public class ChooseEmplyeeListAdapter extends BaseExpandableListAdapter {
    private BaseFgActivity context;
    public List<GroupInfo> groupStrings = new ArrayList<>();
    private boolean isClickable;

    public ChooseEmplyeeListAdapter(BaseFgActivity context) {
        this.context = context;
    }

    @Override
    public int getGroupCount() {
        return null == groupStrings ? 0 : groupStrings.size();
    }

    //        获取指定分组中的子选项的个数
    @Override
    public int getChildrenCount(int groupPosition) {
        if (groupStrings == null) {
            return 0;
        }
        GroupInfo groupInfo = groupStrings.get(groupPosition);

        List<GroupInfo.ChildrenBean> childrenBeanList = groupInfo.getChildren();
        if (childrenBeanList == null) {
            return 0;
        }
        return childrenBeanList.size();
    }

    //        获取指定的分组数据
    @Override
    public Object getGroup(int groupPosition) {
        return groupStrings == null ? null : groupStrings.get(groupPosition);
    }

    // 获取指定分组中的指定子选项数据
    @Override
    public Object getChild(int groupPosition, int childPosition) {
        GroupInfo groupInfo = groupStrings.get(groupPosition);
        if (groupStrings == null) {
            return null;
        }
        List<GroupInfo.ChildrenBean> childrenBeanList = groupInfo.getChildren();
        if (childrenBeanList == null) {
            return null;
        }
        return childrenBeanList.get(childPosition);
    }

    //        获取指定分组的ID, 这个ID必须是唯一的
    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    //        获取子选项的ID, 这个ID必须是唯一的
    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    //        分组和子选项是否持有稳定的ID, 就是说底层数据的改变会不会影响到它们。
    @Override
    public boolean hasStableIds() {
        return true;
    }

    //        获取显示指定分组的视图
    @Override
    public View getGroupView(final int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        convertView = LayoutInflater.from(context).inflate(R.layout.item_expand_group, parent, false);
        TextView tvTitle = (TextView) convertView.findViewById(R.id.label_expand_group);
        ImageView arrow = (ImageView) convertView.findViewById(R.id.expand_arrow);
        AppCompatCheckBox groupCheckBox = (AppCompatCheckBox) convertView.findViewById(R.id.expand_check_box);
        if (groupStrings != null) {
            GroupInfo groupInfo = groupStrings.get(groupPosition);
            tvTitle.setText(groupInfo.getTitle());

            groupCheckBox.setVisibility(isClickable ? View.VISIBLE : View.GONE);
            groupCheckBox.setChecked(groupInfo.isAllChecked());
            groupCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView,
                                             boolean isChecked) {
                    GroupInfo groupInfo1 = groupStrings.get(groupPosition);
                    groupInfo1.setAllChecked(isChecked);
                    List<GroupInfo.ChildrenBean> childrenBeanList = groupInfo1.getChildren();
                    for (GroupInfo.ChildrenBean childrenBean : childrenBeanList) {
                        childrenBean.setChildChecked(isChecked);
                    }

                    notifyDataSetChanged();
                }
            });
        }

        //箭头方向
        arrow.setSelected(isExpanded);
        return convertView;
    }


    // 获取显示指定分组中的指定子选项的视图
    @Override
    public View getChildView(final int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        convertView = LayoutInflater.from(context).inflate(R.layout.item_expand_child, parent, false);
        TextView tvTitle = (TextView) convertView.findViewById(R.id.label_expand_child);
        AppCompatCheckBox childCheckBox = (AppCompatCheckBox) convertView.findViewById(R.id.expand_child_check_box);
        GroupInfo groupInfo = groupStrings.get(groupPosition);
        List<GroupInfo.ChildrenBean> childrenBeanList = groupInfo.getChildren();
        //子类
        GroupInfo.ChildrenBean childrenBean = childrenBeanList.get(childPosition);
        String employeeName = childrenBean.getEmployeeName();
        tvTitle.setText(TextUtil.getLast2(employeeName));
        childCheckBox.setText(employeeName);

        childCheckBox.setVisibility(isClickable ? View.VISIBLE : View.GONE);
        // 全选,取消
        childCheckBox.setChecked(childrenBean.isChildChecked());

        childCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                GroupInfo groupInfo = groupStrings.get(groupPosition);

                List<GroupInfo.ChildrenBean> childrenBeanList = groupInfo.getChildren();

                //子类
                GroupInfo.ChildrenBean childrenBean = childrenBeanList.get(childPosition);
                childrenBean.setChildChecked(isChecked);


                //该字项<被选> --> 组就选
                if (isChecked) {
                    groupInfo.setAllChecked(true);
                } else {
                    boolean isAllUnSeleted = true;//该组全不选
                    //该字项<取消> --> 遍历该组
                    for (GroupInfo.ChildrenBean info : childrenBeanList) {
                        if (info.isChildChecked()) {
                            isAllUnSeleted = false;
                            break;
                        }
                    }
                    //遍历完,没有选中的子项->全不选
                    if (isAllUnSeleted) {
                        groupInfo.setAllChecked(false);
                    }

                }

                notifyDataSetChanged();

            }
        });
        return convertView;
    }

    //指定位置上的子元素是否可选中
    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    public void setData(List<GroupInfo> groupInfos, boolean isClickable) {
        this.groupStrings = groupInfos;
        this.isClickable = isClickable;

        notifyDataSetChanged();
    }

    static class GroupViewHolder {
        TextView tvTitle;
        ImageView arrow;
        AppCompatCheckBox groupCheckBox;
    }

    static class ChildViewHolder {
        TextView tvTitle;
        AppCompatCheckBox childCheckBox;
    }

}











