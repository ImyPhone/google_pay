package com.itcast.googlepay.view;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

/**
 * @��Ŀ��: FlowLayoutDemo
 * @����: org.itheima56.flow
 * @����: FlowLayout
 * @������: Ф��
 * @����ʱ��: 2015-4-29 ����2:25:03
 * @����: ��ʽ����
 * 
 * @svn�汾: $Rev: 34 $
 * @������: $Author: admin $
 * @����ʱ��: $Date: 2015-07-18 15:46:46 +0800 (������, 18 ���� 2015) $
 * @��������: TODO
 */
public class FlowLayout extends ViewGroup
{
	private List<Line>	mLines				= new ArrayList<FlowLayout.Line>(); // ������¼�����ж�����View
	private Line		mCurrrenLine;											// ������¼��ǰ�Ѿ���ӵ�����һ��
	private int			mHorizontalSpace	= 10;
	private int			mVerticalSpace		= 10;

	public FlowLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	public FlowLayout(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	public void setSpace(int horizontalSpace, int verticalSpace)
	{
		this.mHorizontalSpace = horizontalSpace;
		this.mVerticalSpace = verticalSpace;
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
	{
		// ���
		mLines.clear();
		mCurrrenLine = null;

		int layoutWidth = MeasureSpec.getSize(widthMeasureSpec);

		// ��ȡ�����Ŀ��
		int maxLineWidth = layoutWidth - getPaddingLeft() - getPaddingRight();

		// ��������
		int count = getChildCount();
		for (int i = 0; i < count; i++)
		{
			View view = getChildAt(i);

			// ������Ӳ��ɼ�
			if (view.getVisibility() == View.GONE)
			{
				continue;
			}

			// ��������
			measureChild(view, widthMeasureSpec, heightMeasureSpec);

			// ��lines��Ӻ���
			if (mCurrrenLine == null)
			{
				// ˵����û�п�ʼ��Ӻ���
				mCurrrenLine = new Line(maxLineWidth, mHorizontalSpace);

				// ��ӵ� Lines��
				mLines.add(mCurrrenLine);

				// ����һ�����Ӷ�û��
				mCurrrenLine.addView(view);
			}
			else
			{
				// �в�Ϊ��,�����к�����
				boolean canAdd = mCurrrenLine.canAdd(view);
				if (canAdd)
				{
					// �������
					mCurrrenLine.addView(view);
				}
				else
				{
					// ���������,װ����ȥ
					// ����

					// �½���
					mCurrrenLine = new Line(maxLineWidth, mHorizontalSpace);
					// ��ӵ�lines��
					mLines.add(mCurrrenLine);
					// ��view��ӵ�line
					mCurrrenLine.addView(view);
				}
			}
		}

		// �����Լ��Ŀ�Ⱥ͸߶�
		int measuredWidth = layoutWidth;
		// paddingTop + paddingBottom + ���е��м�� + ���е��еĸ߶�

		float allHeight = 0;
		for (int i = 0; i < mLines.size(); i++)
		{
			float mHeigth = mLines.get(i).mHeigth;

			// ���и�
			allHeight += mHeigth;
			// �Ӽ��
			if (i != 0)
			{
				allHeight += mVerticalSpace;
			}
		}

		int measuredHeight = (int) (allHeight + getPaddingTop() + getPaddingBottom() + 0.5f);
		setMeasuredDimension(measuredWidth, measuredHeight);
	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b)
	{
		// ��Child ����---> ��Line����

		int paddingLeft = getPaddingLeft();
		int offsetTop = getPaddingTop();
		for (int i = 0; i < mLines.size(); i++)
		{
			Line line = mLines.get(i);

			// ���в���
			line.layout(paddingLeft, offsetTop);

			offsetTop += line.mHeigth + mVerticalSpace;
		}
	}

	class Line
	{
		// ����
		private List<View>	mViews	= new ArrayList<View>();	// ������¼ÿһ���м���View
		private float		mMaxWidth;							// �����Ŀ��
		private float		mUsedWidth;						// �Ѿ�ʹ���˶��ٿ��
		private float		mHeigth;							// �еĸ߶�
		private float		mMarginLeft;
		private float		mMarginRight;
		private float		mMarginTop;
		private float		mMarginBottom;
		private float		mHorizontalSpace;					// View��view֮���ˮƽ���

		// ����
		public Line(int maxWidth, int horizontalSpace) {
			this.mMaxWidth = maxWidth;
			this.mHorizontalSpace = horizontalSpace;
		}

		// ����
		/**
		 * ���view����¼���Եı仯
		 * 
		 * @param view
		 */
		public void addView(View view)
		{
			// ����View�ķ���

			int size = mViews.size();
			int viewWidth = view.getMeasuredWidth();
			int viewHeight = view.getMeasuredHeight();
			// �����͸�
			if (size == 0)
			{
				// ˵��û�����View
				if (viewWidth > mMaxWidth)
				{
					mUsedWidth = mMaxWidth;
				}
				else
				{
					mUsedWidth = viewWidth;
				}
				mHeigth = viewHeight;
			}
			else
			{
				// ���view�����
				mUsedWidth += viewWidth + mHorizontalSpace;
				mHeigth = mHeigth < viewHeight ? viewHeight : mHeigth;
			}

			// ��View��¼��������
			mViews.add(view);
		}

		/**
		 * �����ж��Ƿ���Խ�View��ӵ�line��
		 * 
		 * @param view
		 * @return
		 */
		public boolean canAdd(View view)
		{
			// �ж��Ƿ������View

			int size = mViews.size();

			if (size == 0) { return true; }

			int viewWidth = view.getMeasuredWidth();

			// Ԥ��ʹ�õĿ��
			float planWidth = mUsedWidth + mHorizontalSpace + viewWidth;

			if (planWidth > mMaxWidth)
			{
				// �Ӳ���ȥ
				return false;
			}

			return true;
		}

		/**
		 * �����Ӳ���
		 * 
		 * @param offsetLeft
		 * @param offsetTop
		 */
		public void layout(int offsetLeft, int offsetTop)
		{
			// �����Ӳ���

			int currentLeft = offsetLeft;

			int size = mViews.size();
			// �ж��Ѿ�ʹ�õĿ���Ƿ�С�����Ŀ��
			float extra = 0;
			float widthAvg = 0;
			if (mMaxWidth > mUsedWidth)
			{
				extra = mMaxWidth - mUsedWidth;
				widthAvg = extra / size;
			}

			for (int i = 0; i < size; i++)
			{
				View view = mViews.get(i);
				int viewWidth = view.getMeasuredWidth();
				int viewHeight = view.getMeasuredHeight();

				// �ж��Ƿ��и���
				if (widthAvg != 0)
				{
					// �ı���
					int newWidth = (int) (viewWidth + widthAvg + 0.5f);
					int widthMeasureSpec = MeasureSpec.makeMeasureSpec(newWidth, MeasureSpec.EXACTLY);
					int heightMeasureSpec = MeasureSpec.makeMeasureSpec(viewHeight, MeasureSpec.EXACTLY);
					view.measure(widthMeasureSpec, heightMeasureSpec);

					viewWidth = view.getMeasuredWidth();
					viewHeight = view.getMeasuredHeight();
				}

				// ����
				int left = currentLeft;
				int top = (int) (offsetTop + (mHeigth - viewHeight) / 2 +
							0.5f);
				// int top = offsetTop;
				int right = left + viewWidth;
				int bottom = top + viewHeight;
				view.layout(left, top, right, bottom);

				currentLeft += viewWidth + mHorizontalSpace;
			}
		}
	}

}
