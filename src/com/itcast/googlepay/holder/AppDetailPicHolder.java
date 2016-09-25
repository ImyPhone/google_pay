package com.itcast.googlepay.holder;

import java.util.List;

import android.view.View;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;

import com.itcast.googlepay.R;
import com.itcast.googlepay.base.BaseHolder;
import com.itcast.googlepay.bean.AppInfoBean;
import com.itcast.googlepay.conf.Constants.URLS;
import com.itcast.googlepay.utils.BitmapHelper;
import com.itcast.googlepay.utils.UIUtils;
import com.itcast.googlepay.view.RatioLayout;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

public class AppDetailPicHolder extends BaseHolder<AppInfoBean> {
	
	@ViewInject(R.id.app_detail_pic_iv_container)
	LinearLayout mContainerPic;
	
	@Override
	public View initHolderView() {
		View view = View.inflate(UIUtils.getContext(), R.layout.item_app_detail_pic, null);
		ViewUtils.inject(this, view);
		return view;
	}

	@Override
	public void refreshHolderView(AppInfoBean data) {
		List<String> picUrls = data.screen;
		for (int i = 0; i < picUrls.size(); i++) {
			String url = picUrls.get(i);
			ImageView iv = new ImageView(UIUtils.getContext());
			iv.setImageResource(R.drawable.ic_default);
			BitmapHelper.display(iv, URLS.IMAGEBASEURL+url);
			
			//控件的宽度等于屏幕的1/3
//			mContainerPic.measure(0, 0);
//			int measuredWidth = mContainerPic.getMeasuredWidth();
			int widthPixels = UIUtils.getResources().getDisplayMetrics().widthPixels;
			widthPixels = widthPixels - mContainerPic.getPaddingLeft() - mContainerPic.getPaddingRight();
			int childWidth = widthPixels / 3;
			
			//控件宽度已知，图片宽高比已知，动态的计算控件的高度
			RatioLayout rl = new RatioLayout(UIUtils.getContext());
			rl.setPicRatio(150/250);
			rl.setRelative(RatioLayout.RELATIVE_WIDTH);
			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(childWidth, LayoutParams.WRAP_CONTENT);
			rl.addView(iv);
			
			
			if (i != 0) {
				params.leftMargin = UIUtils.dip2Px(3);
			}
			mContainerPic.addView(rl,params);
		}
		
	}

}
