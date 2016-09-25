package com.itcast.googlepay.view;

import com.itcast.googlepay.R;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.FrameLayout;

public class RatioLayout extends FrameLayout {
	private float mPicRatio = 0f;  //宽高比
	public static final int RELATIVE_WIDTH = 0; //固定宽度
	public static final int RELATIVE_HEIGHT = 1; //固定高度
	private int mRelative = RELATIVE_WIDTH;
	
	public void setRelative(int relative) {
		mRelative = relative;
	}

	public void setPicRatio(float picRatio) {
		mPicRatio = picRatio;
	}

	public RatioLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		 TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.RatioLayout);
		 mPicRatio = typedArray.getFloat(R.styleable.RatioLayout_picRatio, 0); //0为默认值
		 mRelative = typedArray.getInt(R.styleable.RatioLayout_relative,RELATIVE_WIDTH);
		 typedArray.recycle();
	}

	public RatioLayout(Context context) {
		this(context,null);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int parentWidthMode = MeasureSpec.getMode(widthMeasureSpec);
		int parentHeightMode = MeasureSpec.getMode(heightMeasureSpec);
		if (parentWidthMode == MeasureSpec.EXACTLY && mPicRatio != 0 && mRelative == RELATIVE_WIDTH) {//控件宽度固定，已知图片宽高比，求控件的高度
			//父容器宽度
			int parentWidth = MeasureSpec.getSize(widthMeasureSpec);
			//孩子的宽度
			int childWidth = parentWidth - getPaddingLeft() - getPaddingRight();
			//再计算孩子的高度
			int childHeight = (int) (childWidth/mPicRatio+0.5f);
			//父容器的高度
			int parentHeight = childHeight + getPaddingBottom() +getPaddingTop();
			//主动测绘孩子，固定孩子的大小
			int childWidthMeasureSpec = MeasureSpec.makeMeasureSpec(childWidth, MeasureSpec.EXACTLY);
			int childheightMeasureSpec = MeasureSpec.makeMeasureSpec(childHeight, MeasureSpec.EXACTLY);
			measureChildren(childWidthMeasureSpec, childheightMeasureSpec);
			//设置自己的测试结果
			setMeasuredDimension(parentWidth, parentHeight);
		}else if (parentHeightMode == MeasureSpec.EXACTLY && mPicRatio != 0 && mRelative == RELATIVE_HEIGHT) {//控件高度固定，已知图片宽高比，求控件的宽度
			//父亲的高度
			int parentHeight = MeasureSpec.getSize(heightMeasureSpec);
			//孩子高度
			int childHeight = parentHeight - getPaddingBottom() - getPaddingTop();
			//再计算控件的宽度
			int childWidth = (int) (childHeight*mPicRatio+0.5f);
			//得到父亲的宽度
			int parentWidth = childWidth + getPaddingLeft() + getPaddingRight();
			//主动测绘孩子，固定孩子的大小
			int childWidthMeasureSpec = MeasureSpec.makeMeasureSpec(childWidth, MeasureSpec.EXACTLY);
			int childheightMeasureSpec = MeasureSpec.makeMeasureSpec(childHeight, MeasureSpec.EXACTLY);
			measureChildren(childWidthMeasureSpec, childheightMeasureSpec);
			//设置自己的测试结果
			setMeasuredDimension(parentWidth, parentHeight);
		}else {
			super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		}		
	}

}
