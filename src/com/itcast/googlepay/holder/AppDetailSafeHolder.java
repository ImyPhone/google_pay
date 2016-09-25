package com.itcast.googlepay.holder;

import java.util.List;

import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.itcast.googlepay.R;
import com.itcast.googlepay.base.BaseHolder;
import com.itcast.googlepay.bean.AppInfoBean;
import com.itcast.googlepay.bean.AppInfoBean.AppInfoSafeBean;
import com.itcast.googlepay.conf.Constants.URLS;
import com.itcast.googlepay.utils.BitmapHelper;
import com.itcast.googlepay.utils.UIUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.nineoldandroids.animation.ObjectAnimator;
import com.nineoldandroids.animation.ValueAnimator;
import com.nineoldandroids.animation.ValueAnimator.AnimatorUpdateListener;

public class AppDetailSafeHolder extends BaseHolder<AppInfoBean> implements OnClickListener {
	
	@ViewInject(R.id.app_detail_safe_pic_container)
	LinearLayout mContainerPic;
	@ViewInject(R.id.app_detail_safe_iv_arrow)
	ImageView mIvArrow;
	@ViewInject(R.id.app_detail_safe_des_container)
	LinearLayout mContainerDes;
	
	private boolean isOpen = true;
	
	@Override
	public View initHolderView() {
		View view = View.inflate(UIUtils.getContext(), R.layout.item_app_detail_safe, null);
		ViewUtils.inject(this, view);
		view.setOnClickListener(this);
		return view;
	}

	@Override
	public void refreshHolderView(AppInfoBean data) {
		List<AppInfoSafeBean> safeBeans = data.safe;
		for (int i = 0; i < safeBeans.size(); i++) {
			ImageView ivIcon = new ImageView(UIUtils.getContext());
			BitmapHelper.display(ivIcon, URLS.IMAGEBASEURL+safeBeans.get(i).safeUrl);
			mContainerPic.addView(ivIcon);
			
			LinearLayout ll = new LinearLayout(UIUtils.getContext()); //默认horizontal
			int padding = UIUtils.dip2Px(5);
			ll.setPadding(padding , padding , padding, padding);
			//描述信息左侧的图标
			ImageView ivDes = new ImageView(UIUtils.getContext());
			BitmapHelper.display(ivDes, URLS.IMAGEBASEURL+safeBeans.get(i).safeDesUrl);
			//描述内容
			TextView tv = new TextView(UIUtils.getContext());
			tv.setText(safeBeans.get(i).safeDes);
			if (safeBeans.get(i).safeDesColor == 0) {
				tv.setTextColor(UIUtils.getColor(R.color.app_detail_safe_normal));
			}else {
				tv.setTextColor(UIUtils.getColor(R.color.app_detail_safe_warning));
			}
			tv.setGravity(Gravity.CENTER);
			
			
			ll.addView(ivDes);
			ll.addView(tv);
			mContainerDes.addView(ll);
		}
		
		//默认折叠
		toggle(false);
	}

	@Override
	public void onClick(View v) {
		toggle(true);
		
	}
	
	//折叠 展开 ->mContainerDes的高度变化
	private void toggle(boolean isAnimation) {
		if (isOpen) {//折叠 mContainerDes
			mContainerDes.measure(0, 0);
			int measuredHeight = mContainerDes.getMeasuredHeight();
			int start = measuredHeight;
			int end = 0;
			if (isAnimation) {
				doAnimation(start, end);
			}else{//直接修改高度
				LayoutParams params = mContainerDes.getLayoutParams();
				params.height = end;
				mContainerDes.setLayoutParams(params);
			}
			
			
		}else {//展开
			mContainerDes.measure(0, 0);
			int measuredHeight = mContainerDes.getMeasuredHeight();
			int start = 0;
			int end = measuredHeight;
			if (isAnimation) {
				doAnimation(start, end);
			}else {
				LayoutParams params = mContainerDes.getLayoutParams();
				params.height = end;
				mContainerDes.setLayoutParams(params);
			}			
		}	
		
		//箭头的旋转
		if (isAnimation) {
			if (isOpen) {
				//mIvArrow.setRotation(rotation);
				ObjectAnimator animator = ObjectAnimator.ofFloat(mIvArrow, "rotation", 180,0);
				animator.start();
			}else {
				ObjectAnimator animator = ObjectAnimator.ofFloat(mIvArrow, "rotation", 0,180);
				animator.start();
			}
		}
		
		isOpen = !isOpen;
	}

	private void doAnimation(int start, int end) {
		//mContainerDes.setHeiht没有此方法（没height属性） 所以只能new ValueAnimator()
		ValueAnimator animator = ValueAnimator.ofInt(start,end); //生成start到end的渐变值
		animator.setDuration(300);
		animator.start();
		animator.addUpdateListener(new AnimatorUpdateListener() {
			
			@Override
			public void onAnimationUpdate(ValueAnimator value) {
				int height = (Integer) value.getAnimatedValue();
				//修改高度
				LayoutParams params = mContainerDes.getLayoutParams();
				params.height = height;
				mContainerDes.setLayoutParams(params);
			}
		});
	}

}
