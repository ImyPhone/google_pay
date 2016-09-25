package com.itcast.googlepay.holder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.itcast.googlepay.R;
import com.itcast.googlepay.base.BaseHolder;
import com.itcast.googlepay.bean.SubjectBean;
import com.itcast.googlepay.conf.Constants;
import com.itcast.googlepay.utils.BitmapHelper;
import com.itcast.googlepay.utils.UIUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

public class SubjectHolder extends BaseHolder<SubjectBean> {
	@ViewInject(R.id.item_subject_iv_icon)
	private ImageView mIvIcon;
	
	@ViewInject(R.id.item_subject_tv_title)
	private TextView mTvTitle;
	@Override
	public View initHolderView() {
		View view = View.inflate(UIUtils.getContext(), R.layout.item_subject, null);
		ViewUtils.inject(this, view);
		return view;
	}

	@Override
	public void refreshHolderView(SubjectBean data) {
		mTvTitle.setText(data.des);
		BitmapHelper.display(mIvIcon, Constants.URLS.IMAGEBASEURL+data.url);
		
	}

}
