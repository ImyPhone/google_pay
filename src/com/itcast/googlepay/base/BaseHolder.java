package com.itcast.googlepay.base;

import android.view.View;

public abstract class BaseHolder<HOLDERBEANTYPE> {
	
	public View mHolderView;
	
	public View getHolderView() {
		return mHolderView;
	}
	private HOLDERBEANTYPE mData;
	
	public BaseHolder(){
		mHolderView = initHolderView();
		mHolderView.setTag(this);
	}

	
	public void setDataAndRefreshHolderView(HOLDERBEANTYPE data) {
		//保存数据
		mData = data;
		//刷新显示
		refreshHolderView(data);
	}
	
	public abstract View initHolderView() ;
	public abstract void refreshHolderView(HOLDERBEANTYPE data);
}
