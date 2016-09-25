package com.itcast.googlepay.view;

import com.itcast.googlepay.R;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.FrameLayout;

public class RatioLayout extends FrameLayout {
	private float mPicRatio = 0f;  //��߱�
	public static final int RELATIVE_WIDTH = 0; //�̶����
	public static final int RELATIVE_HEIGHT = 1; //�̶��߶�
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
		 mPicRatio = typedArray.getFloat(R.styleable.RatioLayout_picRatio, 0); //0ΪĬ��ֵ
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
		if (parentWidthMode == MeasureSpec.EXACTLY && mPicRatio != 0 && mRelative == RELATIVE_WIDTH) {//�ؼ���ȹ̶�����֪ͼƬ��߱ȣ���ؼ��ĸ߶�
			//���������
			int parentWidth = MeasureSpec.getSize(widthMeasureSpec);
			//���ӵĿ��
			int childWidth = parentWidth - getPaddingLeft() - getPaddingRight();
			//�ټ��㺢�ӵĸ߶�
			int childHeight = (int) (childWidth/mPicRatio+0.5f);
			//�������ĸ߶�
			int parentHeight = childHeight + getPaddingBottom() +getPaddingTop();
			//������溢�ӣ��̶����ӵĴ�С
			int childWidthMeasureSpec = MeasureSpec.makeMeasureSpec(childWidth, MeasureSpec.EXACTLY);
			int childheightMeasureSpec = MeasureSpec.makeMeasureSpec(childHeight, MeasureSpec.EXACTLY);
			measureChildren(childWidthMeasureSpec, childheightMeasureSpec);
			//�����Լ��Ĳ��Խ��
			setMeasuredDimension(parentWidth, parentHeight);
		}else if (parentHeightMode == MeasureSpec.EXACTLY && mPicRatio != 0 && mRelative == RELATIVE_HEIGHT) {//�ؼ��߶ȹ̶�����֪ͼƬ��߱ȣ���ؼ��Ŀ��
			//���׵ĸ߶�
			int parentHeight = MeasureSpec.getSize(heightMeasureSpec);
			//���Ӹ߶�
			int childHeight = parentHeight - getPaddingBottom() - getPaddingTop();
			//�ټ���ؼ��Ŀ��
			int childWidth = (int) (childHeight*mPicRatio+0.5f);
			//�õ����׵Ŀ��
			int parentWidth = childWidth + getPaddingLeft() + getPaddingRight();
			//������溢�ӣ��̶����ӵĴ�С
			int childWidthMeasureSpec = MeasureSpec.makeMeasureSpec(childWidth, MeasureSpec.EXACTLY);
			int childheightMeasureSpec = MeasureSpec.makeMeasureSpec(childHeight, MeasureSpec.EXACTLY);
			measureChildren(childWidthMeasureSpec, childheightMeasureSpec);
			//�����Լ��Ĳ��Խ��
			setMeasuredDimension(parentWidth, parentHeight);
		}else {
			super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		}		
	}

}
