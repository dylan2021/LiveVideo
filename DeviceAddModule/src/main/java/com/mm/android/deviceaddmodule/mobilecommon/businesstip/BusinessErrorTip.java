package com.mm.android.deviceaddmodule.mobilecommon.businesstip;

import android.os.Message;
import android.text.TextUtils;

import com.mm.android.deviceaddmodule.mobilecommon.AppConsume.BusinessException;

public class BusinessErrorTip {

	public static String getErrorTip(Message message) {
		String errorDesc = "";

		if (message.obj != null && message.obj instanceof BusinessException) {
			BusinessException exception = (BusinessException)message.obj;
			if (!TextUtils.isEmpty(exception.errorDescription)) {
				errorDesc = exception.errorDescription;
			}
		}

		return errorDesc;
	}

	public static Throwable throwError(Message message) {
		BusinessException exception=new BusinessException();
		if (message.obj != null && message.obj instanceof BusinessException) {
			 exception = (BusinessException)message.obj;
		}
		return new Throwable(exception.errorDescription);
	}

}
