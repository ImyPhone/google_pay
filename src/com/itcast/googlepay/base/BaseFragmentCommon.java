package com.itcast.googlepay.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public abstract class BaseFragmentCommon extends Fragment {
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		init();
		super.onCreate(savedInstanceState);
	}
	
	
	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		
		return initView();
	}
	

	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		initDatas();
		initEvent();
		super.onActivityCreated(savedInstanceState);
	}
	
	public void init() {
		// TODO Auto-generated method stub
		
	}
	
	public abstract View initView();

	public void initEvent() {
		// TODO Auto-generated method stub
		
	}

	private void initDatas() {
		// TODO Auto-generated method stub
		
	}
}
