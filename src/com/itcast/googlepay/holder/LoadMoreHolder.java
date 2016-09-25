package com.itcast.googlepay.holder;


import android.view.View;
import android.widget.LinearLayout;

import com.itcast.googlepay.R;
import com.itcast.googlepay.base.BaseHolder;
import com.itcast.googlepay.utils.UIUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

public class LoadMoreHolder extends BaseHolder<Integer> {
	
	@ViewInject(R.id.item_loadmore_container_loading)
	private LinearLayout mContainerLoading;
	
	@ViewInject(R.id.item_loadmore_container_retry)
	private LinearLayout mContainerRetry;
	
	public static final int STATE_LOADING = 0;
	public static final  int STATE_RETRY = 1;
	public static final  int STATE_NONE = 2;
	
	@Override
	public View initHolderView() {
		// 
		View view = View.inflate(UIUtils.getContext(), R.layout.item_loadmore, null);
		ViewUtils.inject(this, view);
		return view;
	}

	@Override
	public void refreshHolderView(Integer state) {
		mContainerLoading.setVisibility(8);
		mContainerRetry.setVisibility(8);
		
		switch (state) {
		case STATE_LOADING:
			mContainerLoading.setVisibility(0);
			break;

		case STATE_RETRY:
			mContainerRetry.setVisibility(0);
			break;
			
		case STATE_NONE:
			
			break;	
		}
		
		
	}

}
