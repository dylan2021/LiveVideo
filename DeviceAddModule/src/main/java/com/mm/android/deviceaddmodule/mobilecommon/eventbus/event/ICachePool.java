package com.mm.android.deviceaddmodule.mobilecommon.eventbus.event;

public interface ICachePool<T> {
	
	public abstract T obtain();
	
	public abstract void recycle(T o);
	
}
