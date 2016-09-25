package com.itcast.googlepay.holder;

import android.view.View;

import com.itcast.googlepay.R;
import com.itcast.googlepay.base.BaseHolder;
import com.itcast.googlepay.utils.UIUtils;

public class MenuHolder extends BaseHolder<Object> {

	@Override
	public View initHolderView() {
		View view = View.inflate(UIUtils.getContext(), R.layout.menu_view, null);
		return view;
	}

	@Override
	public void refreshHolderView(Object data) {
		// TODO Auto-generated method stub
		
	}

}
