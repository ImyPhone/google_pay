package com.itcast.googlepay.base;

import android.app.Application;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;

public class BaseApplication extends Application{
	
	private static Context mContext;
	private static Thread mMainThread;
	private static long mMainThreadId;
	private static Looper mMainLooper;
	private static Handler mHandler;

	

	public static Context getContext() {
		return mContext;
	}



	public static Thread getMainThread() {
		return mMainThread;
	}



	public static long getMainThreadId() {
		return mMainThreadId;
	}



	public static Looper getMainThreadLooper() {
		return mMainLooper;
	}



	public static Handler getHandler() {
		return mHandler;
	}



	@Override
	public void onCreate() {
		mContext = getApplicationContext();
		mMainThread = Thread.currentThread();
		mMainThreadId = android.os.Process.myTid();
		mMainLooper = getMainLooper();
		mHandler = new Handler();
		super.onCreate();
	}
}
