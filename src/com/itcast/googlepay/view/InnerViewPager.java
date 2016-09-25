package com.itcast.googlepay.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

public class InnerViewPager extends ViewPager {

	private float mDownX;
	private float mDownY;
	private float mMoveX;
	private float mMoveY;

	public InnerViewPager(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}
	
	public InnerViewPager(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}



	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		switch (ev.getAction()) {
		case MotionEvent.ACTION_DOWN:
			mDownX = ev.getRawX();
			mDownY = ev.getRawY();
			break;
		case MotionEvent.ACTION_MOVE:
			mMoveX = ev.getRawX();
			mMoveY = ev.getRawY();
			
			float difX = mMoveX - mDownX;
			float difY = mMoveY - mDownY;
			
			if ((Math.abs(difX)-Math.abs(difY))>0) {
				getParent().requestDisallowInterceptTouchEvent(true);
			}else {
				getParent().requestDisallowInterceptTouchEvent(false);
			}
			
			break;
		case MotionEvent.ACTION_UP:

			break;
		}
		return super.onTouchEvent(ev);
	}
}
