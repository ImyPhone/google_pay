package com.itcast.googlepay.view.flyoutin;

import android.content.Context;
import android.util.AttributeSet;
import android.view.GestureDetector.OnGestureListener;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.widget.FrameLayout;

public class StellarMap extends FrameLayout implements AnimationListener, OnTouchListener, OnGestureListener {

	private RandomLayout			mHidenGroup;

	private RandomLayout			mShownGroup;

	private Adapter					mAdapter;
	private RandomLayout.Adapter	mShownGroupAdapter;
	private RandomLayout.Adapter	mHidenGroupAdapter;

	private int						mShownGroupIndex;	// ��ʾ����
	private int						mHidenGroupIndex;	// ���ص���
	private int						mGroupCount;		// ����

	/** ���� */
	private Animation				mZoomInNearAnim;
	private Animation				mZoomInAwayAnim;
	private Animation				mZoomOutNearAnim;
	private Animation				mZoomOutAwayAnim;

	private Animation				mPanInAnim;
	private Animation				mPanOutAnim;
	/** ����ʶ���� */
	private GestureDetector			mGestureDetector;

	/** ���췽�� */
	public StellarMap(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	public StellarMap(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public StellarMap(Context context) {
		super(context);
		init();
	}

	/** ��ʼ������ */
	private void init() {
		mGroupCount = 0;
		mHidenGroupIndex = -1;
		mShownGroupIndex = -1;
		mHidenGroup = new RandomLayout(getContext());
		mShownGroup = new RandomLayout(getContext());

		addView(mHidenGroup, new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
		mHidenGroup.setVisibility(View.GONE);
		addView(mShownGroup, new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));

		mGestureDetector = new GestureDetector(this);
		setOnTouchListener(this);
		// ���ö���
		mZoomInNearAnim = AnimationUtil.createZoomInNearAnim();
		mZoomInNearAnim.setAnimationListener(this);
		mZoomInAwayAnim = AnimationUtil.createZoomInAwayAnim();
		mZoomInAwayAnim.setAnimationListener(this);
		mZoomOutNearAnim = AnimationUtil.createZoomOutNearAnim();
		mZoomOutNearAnim.setAnimationListener(this);
		mZoomOutAwayAnim = AnimationUtil.createZoomOutAwayAnim();
		mZoomOutAwayAnim.setAnimationListener(this);
	}

	/** 2. �������������ʾ���x��y�Ĺ��� */
	public void setRegularity(int xRegularity, int yRegularity) {
		mHidenGroup.setRegularity(xRegularity, yRegularity);
		mShownGroup.setRegularity(xRegularity, yRegularity);
	}

	private void setChildAdapter() {
		if (null == mAdapter) {
			return;
		}
		mHidenGroupAdapter = new RandomLayout.Adapter() {
			// ȡ����Adapter��View�����HidenGroup��Adapter
			@Override
			public View getView(int position, View convertView) {
				return mAdapter.getView(mHidenGroupIndex, position, convertView);
			}

			@Override
			public int getCount() {
				return mAdapter.getCount(mHidenGroupIndex);
			}
		};
		mHidenGroup.setAdapter(mHidenGroupAdapter);

		mShownGroupAdapter = new RandomLayout.Adapter() {
			// ȡ����Adapter��View�����ShownGroup��Adapter
			@Override
			public View getView(int position, View convertView) {
				return mAdapter.getView(mShownGroupIndex, position, convertView);
			}

			@Override
			public int getCount() {
				return mAdapter.getCount(mShownGroupIndex);
			}
		};
		mShownGroup.setAdapter(mShownGroupAdapter);
	}

	/** 1. ���ñ�Adapter */
	public void setAdapter(Adapter adapter) {
		mAdapter = adapter;
		mGroupCount = mAdapter.getGroupCount();
		if (mGroupCount > 0) {
			mShownGroupIndex = 0;
		}
		setChildAdapter();
	}

	/** ������ʾ���� */
	public void setInnerPadding(int paddingLeft, int paddingTop, int paddingRight, int paddingBottom) {
		mHidenGroup.setPadding(paddingLeft, paddingTop, paddingRight, paddingBottom);
		mShownGroup.setPadding(paddingLeft, paddingTop, paddingRight, paddingBottom);
	}

	/** 3. ��ָ����Group���ö��� */
	public void setGroup(int groupIndex, boolean playAnimation) {
		switchGroup(groupIndex, playAnimation, mZoomInNearAnim, mZoomInAwayAnim);
	}

	/** ��ȡ��ǰ��ʾ��group�Ǳ� */
	public int getCurrentGroup() {
		return mShownGroupIndex;
	}

	/** ��Group���ö����� */
	public void zoomIn() {
		final int nextGroupIndex = mAdapter.getNextGroupOnZoom(mShownGroupIndex, true);
		switchGroup(nextGroupIndex, true, mZoomInNearAnim, mZoomInAwayAnim);
	}

	/** ��Group���ó����� */
	public void zoomOut() {
		final int nextGroupIndex = mAdapter.getNextGroupOnZoom(mShownGroupIndex, false);
		switchGroup(nextGroupIndex, true, mZoomOutNearAnim, mZoomOutAwayAnim);
	}

	/** ��Group���ö��� */
	public void pan(float degree) {
		final int nextGroupIndex = mAdapter.getNextGroupOnPan(mShownGroupIndex, degree);
		mPanInAnim = AnimationUtil.createPanInAnim(degree);
		mPanInAnim.setAnimationListener(this);
		mPanOutAnim = AnimationUtil.createPanOutAnim(degree);
		mPanOutAnim.setAnimationListener(this);
		switchGroup(nextGroupIndex, true, mPanInAnim, mPanOutAnim);
	}

	/** ����һ��Group���ý������� */
	private void switchGroup(int newGroupIndex, boolean playAnimation, Animation inAnim, Animation outAnim) {
		if (newGroupIndex < 0 || newGroupIndex >= mGroupCount) {
			return;
		}
		// �ѵ�ǰ��ʾGroup�Ǳ�����Ϊ���ص�
		mHidenGroupIndex = mShownGroupIndex;
		// ����һ��Group�Ǳ�����Ϊ��ʾ��
		mShownGroupIndex = newGroupIndex;
		// ��������Group
		RandomLayout temp = mShownGroup;
		mShownGroup = mHidenGroup;
		mShownGroup.setAdapter(mShownGroupAdapter);
		mHidenGroup = temp;
		mHidenGroup.setAdapter(mHidenGroupAdapter);
		// ˢ����ʾ��Group
		mShownGroup.refresh();
		// ��ʾGroup
		mShownGroup.setVisibility(View.VISIBLE);

		// ��������
		if (playAnimation) {
			if (mShownGroup.hasLayouted()) {
				mShownGroup.startAnimation(inAnim);
			}
			mHidenGroup.startAnimation(outAnim);
		} else {
			mHidenGroup.setVisibility(View.GONE);
		}
	}

	// ���·�����ʾ����
	public void redistribute() {
		mShownGroup.redistribute();
	}

	/** �������� */
	@Override
	public void onAnimationStart(Animation animation) {
		// ����������
	}

	@Override
	public void onAnimationEnd(Animation animation) {
		// ����������
		if (animation == mZoomInAwayAnim || animation == mZoomOutAwayAnim || animation == mPanOutAnim) {
			mHidenGroup.setVisibility(View.GONE);
		}
	}

	@Override
	public void onAnimationRepeat(Animation animation) {
		// �������ظ�
	}

	/** ��λ */
	@Override
	public void onLayout(boolean changed, int l, int t, int r, int b) {
		// �����ж�ShownGroup�Ƿ�onLayout�ı���
		boolean hasLayoutedBefore = mShownGroup.hasLayouted();
		super.onLayout(changed, l, t, r, b);
		if (!hasLayoutedBefore && mShownGroup.hasLayouted()) {
			mShownGroup.startAnimation(mZoomInNearAnim);// ��һ��layout��ʱ����������
		} else {
			mShownGroup.setVisibility(View.VISIBLE);
		}
	}

	/** ��дonTouch�¼�����onTouch�¼����������ʶ�� */
	@Override
	public boolean onTouch(View v, MotionEvent event) {
		return mGestureDetector.onTouchEvent(event);
	}

	/** ���ѵ�onDown�¼� */
	@Override
	public boolean onDown(MotionEvent e) {
		return true;
	}

	/** ��ʵ�� */
	@Override
	public void onShowPress(MotionEvent e) {
	}

	@Override
	public boolean onSingleTapUp(MotionEvent e) {
		return false;
	}

	@Override
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
		return false;
	}

	@Override
	public void onLongPress(MotionEvent e) {

	}

	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
		int centerX = getMeasuredWidth() / 2;
		int centerY = getMeasuredWidth() / 2;

		int x1 = (int) e1.getX() - centerX;
		int y1 = (int) e1.getY() - centerY;
		int x2 = (int) e2.getX() - centerX;
		int y2 = (int) e2.getY() - centerY;

		if ((x1 * x1 + y1 * y1) > (x2 * x2 + y2 * y2)) {
			zoomOut();
		} else {
			zoomIn();
		}
		return true;
	}

	/** �ڲ��ࡢ�ӿ� */
	public static interface Adapter {
		public abstract int getGroupCount();

		public abstract int getCount(int group);

		public abstract View getView(int group, int position, View convertView);

		public abstract int getNextGroupOnPan(int group, float degree);

		public abstract int getNextGroupOnZoom(int group, boolean isZoomIn);
	}
}
