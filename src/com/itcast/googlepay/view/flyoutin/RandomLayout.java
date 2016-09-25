package com.itcast.googlepay.view.flyoutin;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.Set;

import android.content.Context;
import android.graphics.Rect;
import android.view.View;
import android.view.ViewGroup;

public class RandomLayout extends ViewGroup {

	private Random		mRdm;
	/** X�ֲ������ԣ���ֵԽ�ߣ���view��x����ķֲ�Խ����ƽ������СֵΪ1�� */
	private int			mXRegularity;
	/** Y�ֲ������ԣ���ֵԽ�ߣ���view��y����ķֲ�Խ����ƽ������СֵΪ1�� */
	private int			mYRegularity;
	/** ������� */
	private int			mAreaCount;
	/** ����Ķ�ά���� */
	private int[][]		mAreaDensity;
	/** ����Ѿ�ȷ��λ�õ�View */
	private Set<View>	mFixedViews;
	/** �ṩ��View��adapter */
	private Adapter		mAdapter;
	/** ��¼�����յ�View���Ա��ظ����� */
	private List<View>	mRecycledViews;
	/** �Ƿ��Ѿ�layout */
	private boolean		mLayouted;
	/** �����ص�ʱ��ļ�� */
	private int			mOverlapAdd	= 2;

	/** ���췽�� */
	public RandomLayout(Context context) {
		super(context);
		init();
	}

	/** ��ʼ������ */
	private void init() {
		mLayouted = false;
		mRdm = new Random();
		setRegularity(1, 1);
		mFixedViews = new HashSet<View>();
		mRecycledViews = new LinkedList<View>();
	}

	public boolean hasLayouted() {
		return mLayouted;
	}

	/** ����mXRegularity��mXRegularity��ȷ������ĸ��� */
	public void setRegularity(int xRegularity, int yRegularity) {
		if (xRegularity > 1) {
			this.mXRegularity = xRegularity;
		} else {
			this.mXRegularity = 1;
		}
		if (yRegularity > 1) {
			this.mYRegularity = yRegularity;
		} else {
			this.mYRegularity = 1;
		}
		this.mAreaCount = mXRegularity * mYRegularity;// ��������x����ĸ���*y����ĸ���
		this.mAreaDensity = new int[mYRegularity][mXRegularity];// �������Ķ�ά����
	}

	/** ��������Դ */
	public void setAdapter(Adapter adapter) {
		this.mAdapter = adapter;
	}

	/** �����������򣬰����е������¼����0 */
	private void resetAllAreas() {
		mFixedViews.clear();
		for (int i = 0; i < mYRegularity; i++) {
			for (int j = 0; j < mXRegularity; j++) {
				mAreaDensity[i][j] = 0;
			}
		}
	}

	/** �Ѹ��õ�View���뼯�ϣ��¼���ķ��뼯�ϵ�һ���� */
	private void pushRecycler(View scrapView) {
		if (null != scrapView) {
			mRecycledViews.add(0, scrapView);
		}
	}

	/** ȡ�����õ�View���Ӽ��ϵĵ�һ��λ��ȡ�� */
	private View popRecycler() {
		final int size = mRecycledViews.size();
		if (size > 0) {
			return mRecycledViews.remove(0);
		} else {
			return null;
		}
	}

	/** ������View���������listView���õļ򻯰棬����ԭ��һ�� */
	private void generateChildren() {
		if (null == mAdapter) {
			return;
		}
		// �Ȱ���Viewȫ�����뼯��
		final int childCount = super.getChildCount();
		for (int i = childCount - 1; i >= 0; i--) {
			pushRecycler(super.getChildAt(i));
		}
		// ɾ��������View
		super.removeAllViewsInLayout();
		// �õ�Adapter�е�������
		final int count = mAdapter.getCount();
		for (int i = 0; i < count; i++) {
			// �Ӽ�����ȡ��֮ǰ�������View
			View convertView = popRecycler();
			// �Ѹ���View��Ϊadapter��getView����ʷView���룬�õ����ص�View
			View newChild = mAdapter.getView(i, convertView);
			if (newChild != convertView) {// ��������˸��ã���ônewChildӦ�õ���convertView
				// ��˵��û�������ã��������°����û�õ�����View���뼯����
				pushRecycler(convertView);
			}
			// ���ø���ķ�������View��ӽ���
			super.addView(newChild, new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
		}
	}

	/** ���·������� */
	public void redistribute() {
		resetAllAreas();// ������������
		requestLayout();
	}

	/** ���¸�����View */
	public void refresh() {
		resetAllAreas();// ���·�������
		generateChildren();// ���²�����View
		requestLayout();
	}

	/** ��д�����removeAllViews */
	@Override
	public void removeAllViews() {
		super.removeAllViews();// ��ɾ������View
		resetAllAreas();// ����������������
	}

	/** ȷ����View��λ�ã������������ֲ��Ĺؼ� */
	@Override
	public void onLayout(boolean changed, int l, int t, int r, int b) {
		final int count = getChildCount();
		// ȷ������Ŀ��
		int thisW = r - l - this.getPaddingLeft() - this.getPaddingRight();
		int thisH = b - t - this.getPaddingTop() - this.getPaddingBottom();
		// ��������������ұߺ��±�
		int contentRight = r - getPaddingRight();
		int contentBottom = b - getPaddingBottom();
		// ����˳���Ű������ŵ�������
		List<Integer> availAreas = new ArrayList<Integer>(mAreaCount);
		for (int i = 0; i < mAreaCount; i++) {
			availAreas.add(i);
		}

		int areaCapacity = (count + 1) / mAreaCount + 1; // �����ܶȣ���ʾһ�������ڿ��Էż���View��+1��ʾ����Ҫ��һ��
		int availAreaCount = mAreaCount; // ���õ��������

		for (int i = 0; i < count; i++) {
			final View child = getChildAt(i);
			if (child.getVisibility() == View.GONE) { // gone����view�ǲ����벼��
				continue;
			}

			if (!mFixedViews.contains(child)) {// mFixedViews���ڴ���Ѿ�ȷ����λ�õ�View���浽�˾�û��Ҫ�ٴδ��
				LayoutParams params = (LayoutParams) child.getLayoutParams();
				// �Ȳ�����View�Ĵ�С
				int childWidthMeasureSpec = MeasureSpec.makeMeasureSpec(this.getMeasuredWidth(), MeasureSpec.AT_MOST);// Ϊ��View׼�������Ĳ���
				int childHeightMeasureSpec = MeasureSpec.makeMeasureSpec(this.getMeasuredHeight(), MeasureSpec.AT_MOST);
				child.measure(childWidthMeasureSpec, childHeightMeasureSpec);
				// ��View����֮��Ŀ�͸�
				int childW = child.getMeasuredWidth();
				int childH = child.getMeasuredHeight();
				// ������ĸ߶�ȥ���Է���ֵ���������ÿһ������Ŀ�͸�
				float colW = thisW / (float) mXRegularity;
				float rowH = thisH / (float) mYRegularity;

				while (availAreaCount > 0) { // ���ʹ���������0���Ϳ���Ϊ��View���Է���
					int arrayIdx = mRdm.nextInt(availAreaCount);// ���һ��list�е�λ��
					int areaIdx = availAreas.get(arrayIdx);// �ٸ���list�е�λ�û�ȡһ��������
					int col = areaIdx % mXRegularity;// ������ڶ�ά�����е�λ��
					int row = areaIdx / mXRegularity;
					if (mAreaDensity[row][col] < areaCapacity) {// �����ܶ�δ�����޶�����view���������
						int xOffset = (int) colW - childW; // ������ �� ��View�Ŀ�Ȳ�ֵ����ֵ���������������ڵ�λ�����
						if (xOffset <= 0) {
							xOffset = 1;
						}
						int yOffset = (int) rowH - childH;
						if (yOffset <= 0) {
							yOffset = 1;
						}
						// ȷ����ߣ�����������*��ߵ�����
						params.mLeft = getPaddingLeft() + (int) (colW * col + mRdm.nextInt(xOffset));
						int rightEdge = contentRight - childW;
						if (params.mLeft > rightEdge) {// ������View�Ŀ�Ⱥ��ܳ����ұ߽�
							params.mLeft = rightEdge;
						}
						params.mRight = params.mLeft + childW;

						params.mTop = getPaddingTop() + (int) (rowH * row + mRdm.nextInt(yOffset));
						int bottomEdge = contentBottom - childH;
						if (params.mTop > bottomEdge) {// ������View�Ŀ�Ⱥ��ܳ����ұ߽�
							params.mTop = bottomEdge;
						}
						params.mBottom = params.mTop + childH;

						if (!isOverlap(params)) {// �ж��Ƿ�ͱ��View�ص���
							mAreaDensity[row][col]++;// û���ص����Ѹ�������ܶȼ�1
							child.layout(params.mLeft, params.mTop, params.mRight, params.mBottom);// ������View
							mFixedViews.add(child);// ��ӵ��Ѿ����ֵļ�����
							break;
						} else {// ����ص��ˣ��Ѹ������Ƴ���
							availAreas.remove(arrayIdx);
							availAreaCount--;
						}
					} else {// �����ܶȳ����޶�����������ӿ�ѡ�������Ƴ�
						availAreas.remove(arrayIdx);
						availAreaCount--;
					}
				}
			}
		}
		mLayouted = true;
	}

	/** ��������View�Ƿ��ص�������ص�����ô����֮��һ����һ�����������ǹ��е� */
	private boolean isOverlap(LayoutParams params) {
		int l = params.mLeft - mOverlapAdd;
		int t = params.mTop - mOverlapAdd;
		int r = params.mRight + mOverlapAdd;
		int b = params.mBottom + mOverlapAdd;

		Rect rect = new Rect();

		for (View v : mFixedViews) {
			int vl = v.getLeft() - mOverlapAdd;
			int vt = v.getTop() - mOverlapAdd;
			int vr = v.getRight() + mOverlapAdd;
			int vb = v.getBottom() + mOverlapAdd;
			rect.left = Math.max(l, vl);
			rect.top = Math.max(t, vt);
			rect.right = Math.min(r, vr);
			rect.bottom = Math.min(b, vb);
			if (rect.right >= rect.left && rect.bottom >= rect.top) {
				return true;
			}
		}
		return false;
	}

	/** �ڲ��ࡢ�ӿ� */
	public static interface Adapter {

		public abstract int getCount();

		public abstract View getView(int position, View convertView);
	}

	public static class LayoutParams extends ViewGroup.LayoutParams {

		private int	mLeft;
		private int	mRight;
		private int	mTop;
		private int	mBottom;

		public LayoutParams(ViewGroup.LayoutParams source) {
			super(source);
		}

		public LayoutParams(int w, int h) {
			super(w, h);
		}
	}
}
