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

package com.android.livevideo.act_3;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;


import com.android.livevideo.App;
import com.android.livevideo.R;
import com.android.livevideo.act_0.MsgDetailActivity;
import com.android.livevideo.act_0.NoticeDetailUrlActivity;
import com.android.livevideo.act_1.WageMonthAddActivity;
import com.android.livevideo.act_1.Work124567911_12Activity;
import com.android.livevideo.act_1.Work_13Activity;
import com.android.livevideo.act_1.Work1TripActivity;
import com.android.livevideo.act_1.Work8Activity;
import com.android.livevideo.act_1.Work_14_20Activity;
import com.android.livevideo.bean.MsgInfo;
import com.android.livevideo.core.utils.Constant;
import com.android.livevideo.core.utils.KeyConst;
import com.android.livevideo.core.utils.TextUtil;
import com.android.livevideo.util.Utils;
import com.google.gson.JsonObject;

import java.util.List;

/**
 * @author Gool Lee
 * @since
 */
public class MeApplyListAdapter extends BaseAdapter {

    private List<MsgInfo> msgInfos;

    private MeApplyListActivity context;
    private int meListPositon = 0;

    public MeApplyListAdapter(MeApplyListActivity context, List<MsgInfo> msgInfos) {
        super();
        this.context = context;
        this.msgInfos = msgInfos;
    }

    /**
     * 设置ListView中的数据
     */

    public void setData(List<MsgInfo> msgInfoList, int type) {
        msgInfos = msgInfoList;
        meListPositon = type;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        if (msgInfos != null) {
            return msgInfos.size();
        }
        return 0;
    }

    @Override
    public Object getItem(int position) {
        if (msgInfos != null) {
            return msgInfos.get(position);
        }
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.item_me_apply_lv, parent, false);
            holder.titleTv = (TextView) convertView.findViewById(R.id.apply_item_title_tv);
            holder.tv1 = (TextView) convertView.findViewById(R.id.apply_item_type_tv);
            holder.timeTv = (TextView) convertView.findViewById(R.id.apply_item_time_tv);
            holder.tv2 = (TextView) convertView.findViewById(R.id.apply_item_start_time_tv);
            holder.tv3 = (TextView) convertView.findViewById(R.id.apply_item_end_time_tv);
            holder.statusTv = (TextView) convertView.findViewById(R.id.apply_item_status_tv);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        final MsgInfo info = msgInfos.get(position);
        if (info != null) {

            holder.titleTv.setText(info.headline);

            holder.tv2.setVisibility(View.VISIBLE);
            holder.tv3.setVisibility(View.VISIBLE);
            final int status = info.status;
            holder.statusTv.setText(Utils.getStatusText(status));
            holder.statusTv.setTextColor(
                    Utils.getStatusColor(context, status));

            String type = info.type;
            JsonObject infoObj = info.object;
            final Intent intent = new Intent();
            intent.putExtra(KeyConst.id, info.id);
            //我的草稿 ==3  直接到修改界面 :才有"提交"/"删除"按钮
            if (meListPositon == 3) {
                intent.setClass(context, Work124567911_12Activity.class);
                intent.putExtra(KeyConst.type, 0);
            } else {
                intent.setClass(context, MsgDetailActivity.class);
                if (meListPositon == 0 && status == 2 && "2".equals(info.isAudit)) {
                    //我发起的&正在审核状态的->才能 撤销
                    intent.putExtra(KeyConst.agreeReject_recall, 2);
                } else if (meListPositon == -1 && status == 2 && (App.employeeId + "").equals(info.auditorId)) {//我的代办
                    //我是审核人 才能看到这条消息 才有:同意&驳回
                    intent.putExtra(KeyConst.agreeReject_recall, 1);
                } else {
                    intent.putExtra(KeyConst.agreeReject_recall, 0);

                }

                intent.putExtra(KeyConst.title, info.headline);
            }
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    context.startActivity(intent);
                }
            });
            if (null != infoObj) {
                if (Constant.LEAVE.equals(type)) {//请假
                    String typeValue = Utils.getObjStr(infoObj, KeyConst.leaveType);
                    holder.tv1.setText("请假类型：" + Utils.getDictTypeName(context, KeyConst.LEAVE_TYPE, typeValue));
                    holder.tv2.setText("请假时长：" + Utils.getObjStr(infoObj, KeyConst.period) + "小时");
                    holder.tv3.setText("请假事由：" + Utils.getObjStr(infoObj, KeyConst.remark));
                } else if (Constant.BUSINESS_TRIP.equals(type)) {//出差
                    holder.tv1.setText("出差天数：" + Utils.getObjStr(infoObj, KeyConst.period).
                            replace(".0", "") + "天");
                    String peers = Utils.getObjStr(infoObj, KeyConst.peers);
                    holder.tv2.setText("同行人员：" + (TextUtil.isEmpty(peers) ? "无" : peers));
                    holder.tv3.setVisibility(View.GONE);

                    if (meListPositon == 3) {
                        intent.setClass(context, Work1TripActivity.class);
                    }
                } else if (Constant.OVERTIME.equals(type)) {//加班
                    String typeValue = Utils.getObjStr(infoObj, KeyConst.type);
                    holder.tv1.setText("加班类型：" + Utils.getDictTypeName(context, KeyConst.OVERTIME_TYPE, typeValue));
                    holder.tv2.setText("总时长：" + Utils.getObjStr(infoObj, KeyConst.period) + "小时");
                    holder.tv3.setText("加班原因：" + Utils.getObjStr(infoObj, KeyConst.remark));
                    intent.putExtra(KeyConst.type, 2);
                } else if (Constant.REGULAR_WORKER.equals(type)) {//转正
                    holder.tv1.setText("入职日期：" + Utils.getObjStr(infoObj, KeyConst.employmentDate));
                    holder.tv2.setText("试用期：" + Utils.getObjStr(infoObj, KeyConst.probationPeriod).replace(".00", "个月"));
                    holder.tv3.setText("转正日期：" + Utils.getObjStr(infoObj, KeyConst.regularDate));
                    intent.putExtra(KeyConst.type, 4);
                } else if (Constant.RECRUIT.equals(type)) {//招聘
                    holder.tv1.setText("需求岗位：" + Utils.getObjStr(infoObj, KeyConst.recruitPost));
                    holder.tv2.setText("需求人数：" + Utils.getObjStr(infoObj, KeyConst.recruitNum) + "人");
                    holder.tv3.setVisibility(View.GONE);
                    intent.putExtra(KeyConst.type, 5);
                } else if (Constant.DIMISSION.equals(type)) {//离职
                    holder.tv1.setText("入职日期：" + Utils.getObjStr(infoObj, KeyConst.employmentDate));
                    holder.tv2.setText("最后工作日：" + Utils.getObjStr(infoObj, KeyConst.lastWorkDate));
                    holder.tv3.setVisibility(View.GONE);
                    intent.putExtra(KeyConst.type, 6);
                } else if (Constant.OFFICIAL_DOCUMENT.equals(type)) {//公文
                    holder.tv1.setText("审批部门：" + Utils.getObjStr(infoObj, KeyConst.auditDept));
                    holder.tv2.setVisibility(View.GONE);
                    holder.tv3.setVisibility(View.GONE);
                    intent.putExtra(KeyConst.type, 7);
                } else if (Constant.REIMBURSE.equals(type)) {//报销
                    holder.tv1.setText("报销总金额：" + Utils.getObjStr(infoObj, KeyConst.totalAmount) + "元");
                    holder.tv2.setVisibility(View.GONE);
                    holder.tv3.setVisibility(View.GONE);
                    if (meListPositon == 3) {
                        intent.setClass(context, Work8Activity.class);
                    }
                } else if (Constant.PURCHASE.equals(type)) {//采购
                    holder.tv1.setText("采购类型：" + Utils.getObjStr(infoObj, KeyConst.purchaseType));
                    holder.tv2.setText("申请事由：" + Utils.getObjStr(infoObj, KeyConst.reason));
                    holder.tv3.setText("总价格：" + Utils.getObjStr(infoObj, KeyConst.totalAmount) + "元");
                    if (meListPositon == 3) {
                        intent.setClass(context, Work_13Activity.class);
                    }
                } else if (Constant.PAY.equals(type)) {//付款
                    holder.tv1.setText("付款金额：" + Utils.getObjStr(infoObj, KeyConst.amount) + "元");
                    holder.tv2.setText("付款方式：" + Utils.getObjStr(infoObj, KeyConst.payType));
                    holder.tv3.setVisibility(View.GONE);
                    intent.putExtra(KeyConst.type, 12);
                } else if (Constant.PETTY_CASH.equals(type)) {//备用金
                    holder.tv1.setText("申请金额：" + Utils.getObjStr(infoObj, KeyConst.applyAmount) + "元");
                    holder.tv2.setText("使用日期：" + Utils.getObjStr(infoObj, KeyConst.useDate));
                    holder.tv3.setText("归还日期：" + Utils.getObjStr(infoObj, KeyConst.returnDate));
                    intent.putExtra(KeyConst.type, 11);
                } else if (Constant.WAGE_AUDIT.equals(type)) {//工资审核
                    holder.tv1.setText("合计工资：" + Utils.getObjStr(infoObj, KeyConst.totalWage) + "元");
                    holder.tv2.setText("参与人数：" + Utils.getObjStr(infoObj, KeyConst.personNum) + "人");
                    holder.tv3.setVisibility(View.GONE);
                    //工资审核
                    if (meListPositon == 3) {
                        intent.setClass(context, WageMonthAddActivity.class);
                    }
                } else if (type.contains(Constant.NOTICE)) {//公告
                    holder.tv1.setText("摘要：" + Utils.getObjStr(infoObj, KeyConst.summary));
                    holder.tv2.setText("发布人：" + info.applicantName);
                    holder.tv3.setVisibility(View.GONE);

                    convertView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            intent.setClass(context, NoticeDetailUrlActivity.class);
                            intent.putExtra(KeyConst.id, info.id);
                            intent.putExtra(KeyConst.title, info.headline);
                            if (meListPositon == 0 && status == 2 && "2".equals(info.isAudit)) {
                                //我发起的   未审核的->撤销
                                intent.putExtra(KeyConst.agreeReject_recall, 2);
                            } else if (meListPositon == -1 && status == 2 && (App.employeeId + "").equals(info.auditorId)) {
                                //我是审核人:同意&驳回
                                intent.putExtra(KeyConst.agreeReject_recall, 1);
                            } else {
                                intent.putExtra(KeyConst.agreeReject_recall, 3);//删除

                            }
                            context.startActivity(intent);
                        }
                    });
                    //=============================== 2.0 ============================
                } else if (Constant.WEIGH.equals(type)) {//过磅
                    holder.tv1.setText("产品名称：" + Utils.getObjStr(infoObj, KeyConst.productName));
                    holder.tv2.setText("车牌号：" + Utils.getObjStr(infoObj, KeyConst.numberPlate));
                    holder.tv3.setText("过磅时间：" + Utils.getObjStr(infoObj, KeyConst.weighDate));
                    if (meListPositon == 3) {
                        intent.putExtra(KeyConst.type, 14);
                        intent.setClass(context, Work_14_20Activity.class);
                    }
                    if (meListPositon == 0) {
                        intent.putExtra(KeyConst.type, 14);
                    }
                } else if (Constant.RAW_PUT_IN_POOL.equals(type)) {//入池单
                    holder.tv1.setText("池号：" + Utils.getObjStr(infoObj, KeyConst.poolName));
                    holder.tv2.setText("入池时间：" + Utils.getObjStr(infoObj, KeyConst.putInPoolDate));
                    holder.tv3.setText("入池数量：" + Utils.getObjStr(infoObj, KeyConst.amount) + "kg");
                    if (meListPositon == 3) {
                        intent.putExtra(KeyConst.type, 15);
                        intent.setClass(context, Work_14_20Activity.class);
                    }
                    if (meListPositon == 0) {
                        intent.putExtra(KeyConst.type, 15);
                    }
                } else if (Constant.INGREDIENT_PUT_IN_POOL.equals(type)) {//配料入池
                    holder.tv1.setText("池号：" + Utils.getObjStr(infoObj, KeyConst.poolName));
                    holder.tv2.setText("配料入池时间：" + Utils.getObjStr(infoObj, KeyConst.putInPoolDate));
                    holder.tv3.setText("配料员：" + Utils.getObjStr(infoObj, KeyConst.ingredientor));

                    if (meListPositon == 3) {
                        intent.putExtra(KeyConst.type, 16);
                        intent.setClass(context, Work_14_20Activity.class);
                    }
                    if (meListPositon == 0) {
                        intent.putExtra(KeyConst.type, 16);
                    }
                } else if (Constant.SPRINKLE.equals(type)) {//回淋
                    holder.tv1.setText("池号：" + Utils.getObjStr(infoObj, KeyConst.poolName));
                    holder.tv2.setText("回淋时间：" + Utils.getObjStr(infoObj, KeyConst.sprinkleDate));
                    holder.tv3.setText("回淋时长：" + Utils.getObjStr(infoObj, KeyConst.period) + "min");
                    if (meListPositon == 3) {
                        intent.putExtra(KeyConst.type, 17);
                        intent.setClass(context, Work_14_20Activity.class);
                    }
                    if (meListPositon == 0) {
                        intent.putExtra(KeyConst.type, 17);
                    }
                } else if (Constant.TAKE_OUT_POOL.equals(type)) {//起池
                    holder.tv1.setText("池号：" + Utils.getObjStr(infoObj, KeyConst.poolName));
                    holder.tv2.setText("出池时间：" + Utils.getObjStr(infoObj, KeyConst.takeOutPoolDate));
                    holder.tv3.setText("感官风味确认：" + Utils.getObjStr(infoObj, KeyConst.confirmTaste));
                    if (meListPositon == 3) {
                        intent.putExtra(KeyConst.type, 18);
                        intent.setClass(context, Work_14_20Activity.class);
                    }
                    if (meListPositon == 0) {
                        intent.putExtra(KeyConst.type, 18);
                    }
                } else if (Constant.RAW_MATERIAL_CHECK.equals(type)) {//原料入池检验
                    holder.tv1.setText("产品名称：" + Utils.getObjStr(infoObj, KeyConst.productName));

                    holder.tv2.setText("入货时间：" + Utils.getObjStr(infoObj, KeyConst.goodsArriveDate));
                    holder.tv3.setText("检验时间：" + Utils.getObjStr(infoObj, KeyConst.checkDate));
                    if (meListPositon == 3) {
                        intent.putExtra(KeyConst.type, 19);
                        intent.setClass(context, Work_14_20Activity.class);
                    }

                    if (meListPositon == 0) {
                        intent.putExtra(KeyConst.type, 19);
                    }
                } else if (Constant.SEMIFINISHED_PRODUCT_CHECK.equals(type)) {//半成品
                    holder.tv1.setText("池号：" + Utils.getObjStr(infoObj, KeyConst.poolName));
                    holder.tv2.setText("检验时间：" + Utils.getObjStr(infoObj, KeyConst.checkDate));
                    holder.tv3.setVisibility(View.GONE);
                    if (meListPositon == 3) {
                        intent.putExtra(KeyConst.type, 20);
                        intent.setClass(context, Work_14_20Activity.class);
                    }
                    if (meListPositon == 0) {
                        intent.putExtra(KeyConst.type, 20);
                    }
                }
                //===============================2.0============================================
            }

            holder.timeTv.setText(info.createTime);

        }
        return convertView;
    }

    private class ViewHolder {
        private TextView tv1, tv2, tv3, timeTv, titleTv, statusTv;
    }

}














