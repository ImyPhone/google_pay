package com.itcast.googlepay.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.Button;

/**
 * @author  Administrator
 * @time 	2015-7-19 ����4:41:06
 * @des	TODO
 *
 * @version $Rev: 49 $
 * @updateAuthor $Author: admin $
 * @updateDate $Date: 2015-07-20 11:29:58 +0800 (����һ, 20 ���� 2015) $
 * @updateDes TODO
 */
public class ProgressButton extends Button {
	private boolean		mProgressEnable;
	private long		mMax	= 100;
	private long		mProgress;
	private Drawable	mProgressDrawable;

	/**�����Ƿ��������*/
	public void setProgressEnable(boolean progressEnable) {
		mProgressEnable = progressEnable;
	}

	/**���ý��ȵ����ֵ*/
	public void setMax(long max) {
		mMax = max;
	}

	/**���õ�ǰ�Ľ���,���ҽ����ػ����*/
	public void setProgress(long progress) {
		mProgress = progress;
		invalidate();
	}

	/**����progressButton�Ľ���ͼƬ*/
	public void setProgressDrawable(Drawable progressDrawable) {
		mProgressDrawable = progressDrawable;
	}

	public ProgressButton(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public ProgressButton(Context context) {
		super(context);
	}

	// onmeasure onlayout ondraw
	@Override
	protected void onDraw(Canvas canvas) {
		if (mProgressEnable) {
			Drawable drawable = new ColorDrawable(Color.BLUE);
			int left = 0;
			int top = 0;
			int right = (int) (mProgress * 1.0f / mMax * getMeasuredWidth() + .5f);
			int bottom = getBottom();
			drawable.setBounds(left, top, right, bottom);// �����.��֪���Ƶķ�Χ
			drawable.draw(canvas);
		}

		super.onDraw(canvas);// �����ı�,������Ʊ���
	}
}
