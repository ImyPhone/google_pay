package com.itcast.googlepay.holder;

import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewParent;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.itcast.googlepay.R;
import com.itcast.googlepay.base.BaseHolder;
import com.itcast.googlepay.bean.AppInfoBean;
import com.itcast.googlepay.utils.UIUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.Animator.AnimatorListener;
import com.nineoldandroids.animation.ObjectAnimator;

public class AppDetailDesHolder extends BaseHolder<AppInfoBean> implements OnClickListener {
		
	@ViewInject(R.id.app_detail_des_tv_author)
	TextView			mTvAuthor;

	@ViewInject(R.id.app_detail_des_iv_arrow)
	ImageView			mIvArrow;

	@ViewInject(R.id.app_detail_des_tv_des)
	TextView			mTvDes;
	
	private boolean		isOpen	= true;
	private int			mTvDesMeasuredHeight;
	private AppInfoBean mData;
	@Override
	public View initHolderView() {
		View view = View.inflate(UIUtils.getContext(), R.layout.item_app_detail_des, null);
		ViewUtils.inject(this, view);
		view.setOnClickListener(this);
		return view;
	}

	@Override
	public void refreshHolderView(AppInfoBean data) {
		mData = data;
		mTvAuthor.setText(data.author);
		mTvDes.setText(data.des);
		
		mTvDes.getViewTreeObserver().addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
			
			@Override
			public void onGlobalLayout() {
				mTvDesMeasuredHeight = mTvDes.getMeasuredHeight();
				//mTvDes.getViewTreeObserver().removeOnGlobalLayoutListener(this);
				mTvDes.getViewTreeObserver().removeGlobalOnLayoutListener(this);
				toggle(false);
			}
		});
		
	}

	@Override
	public void onClick(View v) {
		toggle(true);
		
	}

	private void toggle(boolean isAnimation) {
		if (isOpen) { //关闭
			int start = mTvDesMeasuredHeight;
			int end = getShortHeight(7,mData);
			if (isAnimation) {
				doAnimation(start, end);
			}else{
				mTvDes.setHeight(end);
			}
			
			
		}else {//打开
			int end = mTvDesMeasuredHeight;
			int start = getShortHeight(7,mData);
			if (isAnimation) {
				doAnimation(start, end);
			}else {
				mTvDes.setHeight(end);
			}
			
		}
		
		if (isAnimation) {
			if (isOpen) {
				//mIvArrow.setRotation(rotation)
				ObjectAnimator.ofFloat(mIvArrow, "rotation", 180, 0).start();
			}else {
				ObjectAnimator.ofFloat(mIvArrow, "rotation", 0, 180).start();
			}
		}
		
		isOpen = !isOpen;
	}

	private void doAnimation(int start, int end) {
		//mTvDes.setHeight(pixels)
		ObjectAnimator animator = ObjectAnimator.ofInt(mTvDes, "height", start,end);
		animator.start();
		animator.addListener(new AnimatorListener() {
			
			@Override
			public void onAnimationStart(Animator arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onAnimationRepeat(Animator arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onAnimationEnd(Animator arg0) {
				// 动画结束
				ViewParent parent = mTvDes.getParent();
				while (true) {
					parent = parent.getParent(); //最新的爹
					if (parent == null) {
						break;
					}
					if (parent instanceof ScrollView) {
						((ScrollView)parent).fullScroll(View.FOCUS_DOWN);
						break;
					}
				}
			}
			
			@Override
			public void onAnimationCancel(Animator arg0) {
				// TODO Auto-generated method stub
				
			}
		});
	}

	private int getShortHeight(int i, AppInfoBean data) {
		TextView tempTextView = new TextView(UIUtils.getContext());
		tempTextView.setLines(7);
		tempTextView.setText(data.des);
		
		tempTextView.measure(0, 0);
		int measuredHeight = tempTextView.getMeasuredHeight();		
		return measuredHeight;
	}

}
