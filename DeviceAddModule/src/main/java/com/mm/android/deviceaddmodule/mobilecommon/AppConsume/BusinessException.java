/**
 * Exception style class encapsulating Business errors
 */
package com.mm.android.deviceaddmodule.mobilecommon.AppConsume;


@SuppressWarnings("serial")
public class BusinessException extends Exception{
	/**
	 * 执行错误码
	 */
	public int errorCode = 1;

	/**
	 * 错误描述
	 */
	public String errorDescription = "UNKNOWN_ERROR";
	
	public BusinessException(){
		
	}
	
	public BusinessException(String e) {
		super(e);
		this.errorDescription =e;
	}

	public BusinessException(Throwable cause) {
        super(cause);
		this.errorDescription =cause.getMessage();
    }

	public BusinessException(int errorCode) {
        this.errorCode = errorCode;
    }
}
