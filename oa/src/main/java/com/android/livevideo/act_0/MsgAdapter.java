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

import com.android.livevideo.App;
import com.android.livevideo.R;
import com.android.livevideo.act_other.BaseFgActivity;
import com.android.livevideo.bean.MsgInfo;
import com.android.livevideo.core.utils.Constant;
import com.android.livevideo.core.utils.KeyConst;
import com.android.livevideo.core.utils.TextUtil;
import com.android.livevideo.util.Utils;
import com.android.livevideo.view.BorderLabelTextView;
import com.google.gson.JsonObject;

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
            holder.statusTv = (BorderLabelTextView) convertView.findViewById(R.id.status_tv);
            holder.timeTv = (TextView) convertView.findViewById(R.id.msg_item_time_tv);

            holder.tv1 = (TextView) convertView.findViewById(R.id.msg_item_tv_1);
            holder.tv2 = (TextView) convertView.findViewById(R.id.msg_item_tv_2);
            holder.tv3 = (TextView) convertView.findViewById(R.id.msg_item_tv_3);
            holder.redTag = convertView.findViewById(R.id.red_tag);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        if (info != null) {
            String isRead = info.isRead;
            holder.redTag.setVisibility("1".equals(isRead) ? View.VISIBLE : View.GONE);

            holder.titleTv.setText("预警消息" + (position + 1));
            final int status = 4;

            holder.statusTv.setText("摄像头1" + position);
            int color = Utils.getStatusColor(context, status);
            holder.statusTv.setTextColor(color);
            holder.statusTv.setStrokeColor(color);
            holder.statusTv.setVisibility(View.VISIBLE);

            holder.timeTv.setText(info.createTime);

            holder.tv2.setVisibility(View.VISIBLE);
            holder.tv3.setVisibility(View.VISIBLE);


            String type = info.type;
            JsonObject infoObj = info.object;
            if (null != infoObj) {
                Intent i = new Intent(context, MsgDetailActivity.class);
                if (Constant.LEAVE.equals(type)) {//请假
                    String typeValue = Utils.getObjStr(infoObj, KeyConst.leaveType);
                    holder.tv1.setText("请假类型：" + Utils.getDictTypeName(context, KeyConst.LEAVE_TYPE, typeValue));
                    holder.tv2.setText("请假时长：" + Utils.getObjStr(infoObj, KeyConst.period) + "小时");
                    holder.tv3.setText("请假事由：" + Utils.getObjStr(infoObj, KeyConst.remark));
                } else if (Constant.BUSINESS_TRIP.equals(type)) {//出差
                    String objStr = Utils.getObjStr(infoObj, KeyConst.period);
                    holder.tv1.setText("出差天数：" + objStr.replace(".0", "") + "天");
                    String peers = Utils.getObjStr(infoObj, KeyConst.peers);
                    holder.tv2.setText("同行人员：" + (TextUtil.isEmpty(peers) ? "无" : peers));
                    holder.tv3.setVisibility(View.GONE);
                } else if (Constant.OVERTIME.equals(type)) {//加班
                    String typeValue = Utils.getObjStr(infoObj, KeyConst.type);
                    holder.tv1.setText("加班类型：" + Utils.getDictTypeName(context, KeyConst.OVERTIME_TYPE, typeValue));
                    holder.tv2.setText("总时长：" + Utils.getObjStr(infoObj, KeyConst.period) + "小时");
                    holder.tv3.setText("加班原因：" + Utils.getObjStr(infoObj, KeyConst.remark));
                } else if (Constant.REGULAR_WORKER.equals(type)) {//转正
                    holder.tv1.setText("入职日期：" + Utils.getObjStr(infoObj, KeyConst.employmentDate));
                    holder.tv2.setText("试用期：" +
                            Utils.getObjStr(infoObj, KeyConst.probationPeriod).replace(".00", "个月"));
                    holder.tv3.setText("转正日期：" + Utils.getObjStr(infoObj, KeyConst.regularDate));
                } else if (Constant.RECRUIT.equals(type)) {//招聘
                    holder.tv1.setText("需求岗位：" + Utils.getObjStr(infoObj, KeyConst.recruitPost));
                    holder.tv2.setText("需求人数：" + Utils.getObjStr(infoObj, KeyConst.recruitNum) + "人");
                    holder.tv3.setVisibility(View.GONE);
                } else if (Constant.DIMISSION.equals(type)) {//离职
                    holder.tv1.setText("入职日期：" + Utils.getObjStr(infoObj, KeyConst.employmentDate));
                    holder.tv2.setText("最后工作日：" + Utils.getObjStr(infoObj, KeyConst.lastWorkDate));
                    holder.tv3.setVisibility(View.GONE);
                } else if (Constant.OFFICIAL_DOCUMENT.equals(type)) {//公文
                    holder.tv1.setText("审批部门：" + Utils.getObjStr(infoObj, KeyConst.auditDept));
                    holder.tv2.setVisibility(View.GONE);
                    holder.tv3.setVisibility(View.GONE);
                } else if (Constant.REIMBURSE.equals(type)) {//报销
                    holder.tv1.setText("报销总金额：" +
                            Utils.getObjStr(infoObj, KeyConst.totalAmount) + "元");
                    holder.tv2.setVisibility(View.GONE);
                    holder.tv3.setVisibility(View.GONE);
                } else if (Constant.PURCHASE.equals(type)) {//申购
                    holder.tv1.setText("采购类型：" + Utils.getObjStr(infoObj, KeyConst.purchaseType));
                    holder.tv2.setText("申请事由：" + Utils.getObjStr(infoObj, KeyConst.reason));
                    holder.tv3.setText("总价格：" + Utils.getObjStr(infoObj, KeyConst.totalAmount) + "元");
                } else if (Constant.PAY.equals(type)) {//付款
                    holder.tv1.setText("付款金额：" + Utils.getObjStr(infoObj, KeyConst.amount) + "元");
                    holder.tv2.setText("付款方式：" + Utils.getObjStr(infoObj, KeyConst.payType));
                    holder.tv3.setVisibility(View.GONE);
                } else if (Constant.PETTY_CASH.equals(type)) {//备用金
                    holder.tv1.setText("申请金额：" + Utils.getObjStr(infoObj, KeyConst.applyAmount) + "元");
                    holder.tv2.setText("使用日期：" + Utils.getObjStr(infoObj, KeyConst.useDate));
                    holder.tv3.setText("归还日期：" + Utils.getObjStr(infoObj, KeyConst.returnDate));
                } else if (Constant.WAGE_AUDIT.equals(type)) {//工资审核
                    holder.tv1.setText("合计工资：" + Utils.getObjStr(infoObj, KeyConst.totalWage) + "元");
                    holder.tv2.setText("参与人数：" + Utils.getObjStr(infoObj, KeyConst.personNum) + "人");
                    holder.tv3.setVisibility(View.GONE);
                } else if (type.contains(Constant.NOTICE)) {//公告
                    holder.tv1.setText("预警类型：安全危险");
                    holder.tv2.setText("所在位置：武汉市江夏区金融港一路30号");
                    holder.tv3.setVisibility(View.GONE);

                    convertView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(context, NoticeDetailUrlActivity.class);
                            intent.putExtra(KeyConst.id, info.id);
                            intent.putExtra(KeyConst.title, info.headline);
                            if (status == 2 && (App.employeeId + "").equals(info.auditorId)) {
                                //我是审核人:同意&驳回
                                intent.putExtra(KeyConst.agreeReject_recall, 1);
                            }
                            context.startActivity(intent);
                            holder.redTag.setVisibility(View.GONE);
                        }
                    });
                    //=============================== 2.0 ============================
                } else if (Constant.WEIGH.equals(type)) {//过磅
                    holder.tv1.setText("产品名称：" + Utils.getObjStr(infoObj, KeyConst.productName));
                    holder.tv2.setText("车牌号：" + Utils.getObjStr(infoObj, KeyConst.numberPlate));
                    holder.tv3.setText("过磅时间：" + Utils.getObjStr(infoObj, KeyConst.weighDate));
                    i.putExtra(KeyConst.type, 14);
                } else if (Constant.RAW_PUT_IN_POOL.equals(type)) {//入池单
                    holder.tv1.setText("池号：" + Utils.getObjStr(infoObj, KeyConst.poolName));
                    holder.tv2.setText("入池时间：" + Utils.getObjStr(infoObj, KeyConst.putInPoolDate));
                    holder.tv3.setText("入池数量：" + Utils.getObjStr(infoObj, KeyConst.amount) + "kg");
                    i.putExtra(KeyConst.type, 15);
                } else if (Constant.INGREDIENT_PUT_IN_POOL.equals(type)) {//配料入池
                    holder.tv1.setText("池号：" + Utils.getObjStr(infoObj, KeyConst.poolName));
                    holder.tv2.setText("配料入池时间：" + Utils.getObjStr(infoObj, KeyConst.putInPoolDate));
                    holder.tv3.setText("配料员：" + Utils.getObjStr(infoObj, KeyConst.ingredientor));
                    i.putExtra(KeyConst.type, 16);
                } else if (Constant.SPRINKLE.equals(type)) {//回淋
                    holder.tv1.setText("池号：" + Utils.getObjStr(infoObj, KeyConst.poolName));
                    holder.tv3.setText("回淋时间：" + Utils.getObjStr(infoObj, KeyConst.sprinkleDate));
                    holder.tv2.setText("回淋时长：" + Utils.getObjStr(infoObj, KeyConst.period) + "min");
                    i.putExtra(KeyConst.type, 17);
                } else if (Constant.TAKE_OUT_POOL.equals(type)) {//起池
                    holder.tv1.setText("池号：" + Utils.getObjStr(infoObj, KeyConst.poolName));
                    holder.tv2.setText("出池时间：" + Utils.getObjStr(infoObj, KeyConst.takeOutPoolDate));
                    holder.tv3.setText("感官风味确认：" + Utils.getObjStr(infoObj, KeyConst.confirmTaste));
                    i.putExtra(KeyConst.type, 18);
                } else if (Constant.RAW_MATERIAL_CHECK.equals(type)) {//原料入池检验
                    holder.tv1.setText("产品名称：" + Utils.getObjStr(infoObj, KeyConst.productName));
                    holder.tv2.setText("入货时间：" + Utils.getObjStr(infoObj, KeyConst.goodsArriveDate));
                    holder.tv3.setText("检验时间：" + Utils.getObjStr(infoObj, KeyConst.checkDate));
                    i.putExtra(KeyConst.type, 19);
                } else if (Constant.SEMIFINISHED_PRODUCT_CHECK.equals(type)) {//半成品
                    holder.tv1.setText("池号：" + Utils.getObjStr(infoObj, KeyConst.poolName));
                    holder.tv2.setText("检验时间：" + Utils.getObjStr(infoObj, KeyConst.checkDate));
                    holder.tv3.setVisibility(View.GONE);
                    i.putExtra(KeyConst.type, 20);
                }
                //=================================2.0=======================

                if (!type.contains(Constant.NOTICE)) {
                    convertView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            i.putExtra(KeyConst.id, info.id);
                            i.putExtra(KeyConst.title, info.headline);
                            if (status == 2 && (App.employeeId + "").equals(info.auditorId)) {//我是审核人才能看到这条消息 -所以只有:同意&驳回
                                i.putExtra(KeyConst.agreeReject_recall, 1);
                            }
                            context.startActivity(i);
                            holder.redTag.setVisibility(View.GONE);
                        }
                    });
                }
            }
            holder.timeTv.setText(info.createTime);

        }
        return convertView;
    }

    private class ViewHolder {
        private TextView titleTv, timeTv, tv1, tv2, tv3, redTag;
        private BorderLabelTextView statusTv;

    }

}














