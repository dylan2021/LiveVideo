package com.mm.android.deviceaddmodule.adapter;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.mm.android.deviceaddmodule.R;
import com.mm.android.deviceaddmodule.mobilecommon.base.adapter.CommonAdapter;
import com.mm.android.deviceaddmodule.mobilecommon.common.ViewHolder;
import com.mm.android.deviceaddmodule.mobilecommon.entity.device.DHDevice;

import java.util.List;

/**
 * 网关设备列表适配器
 */
public class GatewayListAdapter extends CommonAdapter<DHDevice> {
	private int mSelectPosition=-1;
	public GatewayListAdapter(List<DHDevice> list, Context context) {
		super(R.layout.gateway_list_item, list, context);
	}

	@Override
	public void convert(ViewHolder viewHolder, final DHDevice entity,
						final int position, ViewGroup parent) {
		CheckBox checkBox = (CheckBox) viewHolder
				.findViewById(R.id.checkbox);
		TextView deviceName = (TextView) viewHolder
				.findViewById(R.id.device_name);

		deviceName.setText(entity.getName());
		if (entity.isOnline()) {
			deviceName.setTextColor(mContext.getResources().getColor(R.color.c2));
		} else {
			deviceName.setTextColor(mContext.getResources().getColor(R.color.c5));
		}

		if (mSelectPosition == position)
			checkBox.setChecked(true);
		else
			checkBox.setChecked(false);
	}

	public void setSelectPosition(int selectPosition){
		mSelectPosition = selectPosition;
		notifyDataSetChanged();
	}

	public int getSelectPosition() {
		return mSelectPosition;
	}
}
