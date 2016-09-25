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
		//��������
		mData = data;
		//ˢ����ʾ
		refreshHolderView(data);
	}
	
	public abstract View initHolderView() ;
	public abstract void refreshHolderView(HOLDERBEANTYPE data);
}
