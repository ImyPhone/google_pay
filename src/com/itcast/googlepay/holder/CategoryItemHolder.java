package com.itcast.googlepay.holder;

import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.itcast.googlepay.R;
import com.itcast.googlepay.base.BaseHolder;
import com.itcast.googlepay.bean.CategoryInfoBean;
import com.itcast.googlepay.conf.Constants.URLS;
import com.itcast.googlepay.utils.BitmapHelper;
import com.itcast.googlepay.utils.StringUtils;
import com.itcast.googlepay.utils.UIUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

public class CategoryItemHolder extends BaseHolder<CategoryInfoBean> {
	
	@ViewInject(R.id.item_category_item_1)
	LinearLayout	mContainerItem1;

	@ViewInject(R.id.item_category_item_2)
	LinearLayout	mContainerItem2;

	@ViewInject(R.id.item_category_item_3)
	LinearLayout	mContainerItem3;

	@ViewInject(R.id.item_category_icon_1)
	ImageView		mIvIcon1;

	@ViewInject(R.id.item_category_icon_2)
	ImageView		mIvIcon2;

	@ViewInject(R.id.item_category_icon_3)
	ImageView		mIvIcon3;

	@ViewInject(R.id.item_category_name_1)
	TextView		mTvName1;

	@ViewInject(R.id.item_category_name_2)
	TextView		mTvName2;

	@ViewInject(R.id.item_category_name_3)
	TextView		mTvName3;
	
	@Override
	public View initHolderView() {
		View view = View.inflate(UIUtils.getContext(), R.layout.item_category_info, null);
		ViewUtils.inject(this,view);
		return view;
	}

	@Override
	public void refreshHolderView(CategoryInfoBean data) {
		setData(data.name1, data.url1, mTvName1, mIvIcon1);
		setData(data.name2, data.url2, mTvName2, mIvIcon2);
		setData(data.name3, data.url3, mTvName3, mIvIcon3);

	}

	public void setData(final String name, String url, TextView tvName, ImageView ivIcon) {
		if (!StringUtils.isEmpty(name) && !StringUtils.isEmpty(url)) {
			tvName.setText(name);
			ivIcon.setImageResource(R.drawable.ic_default);
			BitmapHelper.display(ivIcon, URLS.IMAGEBASEURL + url);
			((ViewGroup) tvName.getParent()).setVisibility(View.VISIBLE);

			((ViewGroup) tvName.getParent()).setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO
					Toast.makeText(UIUtils.getContext(), name, 0).show();
				}
			});
		} else {
			((ViewGroup) tvName.getParent()).setVisibility(View.INVISIBLE);
		}
	}

}
