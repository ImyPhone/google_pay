package com.itcast.googlepay.utils;

import com.itcast.googlepay.base.BaseApplication;

import android.content.Context;
import android.content.res.Resources;
import android.os.Handler;

public class UIUtils {

	public static Context getContext() {
		return BaseApplication.getContext();
	}
	
	public static Resources getResources() {
		return getContext().getResources();
	}
	
	public static String getString(int id, Object... formatArgs){
		return getResources().getString(id, formatArgs);
	}
	
	public static String getString(int resId){
		return getResources().getString(resId);
	}
	
	public static String[] getStringArray(int resid) {
		return getResources().getStringArray(resid);
	}
	
	public static int getColor(int colorId) {
		return getResources().getColor(colorId);
	}
	
	public static String getPackageName() {
		return getContext().getPackageName();
	}
	
	public static long getMainThreadId(){
		return BaseApplication.getMainThreadId();
	}
	
	public static Handler getMainThreadHandler() {
		return BaseApplication.getHandler();
	}
	
	//安全执行一个任务
	public static void postTaskSafely(Runnable task) {
		int curThreadId = android.os.Process.myTid();
		if (curThreadId == getMainThreadId()) {
			task.run();
		}else {
			getMainThreadHandler().post(task);   //如果不是是主线程--->handler.post(task)--->本质handler发送一个消息
		}
	}
	
	//延迟执行任务
	public static void postTaskDelay(Runnable task, int delayMillis) {
		getMainThreadHandler().postDelayed(task, delayMillis);  //主线程handler
	}
	
	//移除任务
	public static void removeTask(Runnable task) {
		getMainThreadHandler().removeCallbacks(task);		
	}
	
	
	public static int dip2Px(int dip) {
		// px/dip = density;
		float density = getResources().getDisplayMetrics().density;
		int px = (int) (dip * density + .5f);
		return px;
	}
	
	public static int px2Dip(int px) {
		float density = getResources().getDisplayMetrics().density;
		int dip = (int) (px / density + .5f);
		return dip;
	}

	
}
