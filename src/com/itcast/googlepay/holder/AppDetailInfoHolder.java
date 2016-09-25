package com.itcast.googlepay.holder;

import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.itcast.googlepay.R;
import com.itcast.googlepay.base.BaseHolder;
import com.itcast.googlepay.bean.AppInfoBean;
import com.itcast.googlepay.conf.Constants.URLS;
import com.itcast.googlepay.utils.BitmapHelper;
import com.itcast.googlepay.utils.StringUtils;
import com.itcast.googlepay.utils.UIUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

public class AppDetailInfoHolder extends BaseHolder<AppInfoBean> {
	
	@ViewInject(R.id.app_detail_info_iv_icon)
	ImageView	mIvIcon;

	@ViewInject(R.id.app_detail_info_rb_star)
	RatingBar	mRbStar;

	@ViewInject(R.id.app_detail_info_tv_downloadnum)
	TextView	mTvDownLoadNum;

	@ViewInject(R.id.app_detail_info_tv_name)
	TextView	mTvName;

	@ViewInject(R.id.app_detail_info_tv_time)
	TextView	mTvTime;

	@ViewInject(R.id.app_detail_info_tv_version)
	TextView	mTvVersion;

	@ViewInject(R.id.app_detail_info_tv_size)
	TextView	mTvSize;

	@Override
	public View initHolderView() {
		View view = View.inflate(UIUtils.getContext(), R.layout.item_app_detail_info, null);
		ViewUtils.inject(this, view);
		return view;
	}

	@Override
	public void refreshHolderView(AppInfoBean data) {
		
		String date = UIUtils.getString(R.string.detail_date, data.date);
		String downloadNum = UIUtils.getString(R.string.detail_downloadnum, data.downloadNum);
		String size = UIUtils.getString(R.string.detail_size, StringUtils.formatFileSize(data.size));
		String version = UIUtils.getString(R.string.detail_version, data.version);
		
		mTvName.setText(data.name);
		mTvDownLoadNum.setText(downloadNum);
		mTvTime.setText(date);
		mTvVersion.setText(version);
		mTvSize.setText(size);

		mIvIcon.setImageResource(R.drawable.ic_default);
		BitmapHelper.display(mIvIcon, URLS.IMAGEBASEURL + data.iconUrl);

		mRbStar.setRating(data.stars);
		
	}

}
