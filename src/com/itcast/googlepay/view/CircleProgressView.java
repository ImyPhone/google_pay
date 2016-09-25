package com.itcast.googlepay.view;

import com.itcast.googlepay.R;
import com.itcast.googlepay.utils.UIUtils;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class CircleProgressView extends LinearLayout {
	
	private ImageView mIvIcon;
	private TextView mTvNote;
	private boolean mProgressEnable;  //�Ƿ��������
	private long mMax = 100;
	private long mProgress;
	

	public void setMax(long max) {
		mMax = max;
	}
	public void setProgress(long progress) {
		mProgress = progress;
		invalidate();
	}
	public void setProgressEnable(boolean progressEnable) {
		mProgressEnable = progressEnable;
		
	}
	public void setIvIcon(int resId) {
		mIvIcon.setImageResource(resId);
	}

	public void setNote(String note) {
	
		mTvNote.setText(note);
	}

	

	public CircleProgressView(Context context) {
		this(context, null);
	}

	public CircleProgressView(Context context, AttributeSet attrs) {
		super(context, attrs);
		View view = View.inflate(getContext(), R.layout.circleprogress_root_view, this);
		mIvIcon = (ImageView) view.findViewById(R.id.circleProgressView_iv_icon);
		mTvNote = (TextView) view.findViewById(R.id.circleProgressView_tv_note);
		
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		
		super.onDraw(canvas); //���Ʊ���
		
//		RectF oval = new RectF(mIvIcon.getLeft(), mIvIcon.getTop(), mIvIcon.getRight(), mIvIcon.getBottom());
//		Paint paint = new Paint();
//		paint.setColor(Color.BLUE);
//		
//		paint.setStyle(Style.STROKE);  // paint.setStyle(Style.FILL); ���
//		paint.setStrokeWidth(5); //�ʶ��  px
//		
//		paint.setAntiAlias(true);// �������
//		canvas.drawArc(oval, -90, 180, false, paint);  //��RectF�������ϻ�Բ��
//		//                   ��ʼλ�ã�-90  12���ӷ���     0   3���ӷ���
//		//					��һ��180�ȳ��Ļ����൱�ڰ�Բ��
//		//                  false���������뾶����ʾ
	}
	
	@Override
	protected void dispatchDraw(Canvas canvas) {
		
		super.dispatchDraw(canvas);  // ���ƾ��������(ͼƬ������)
		if (mProgressEnable) {
			RectF oval = new RectF(mIvIcon.getLeft(), mIvIcon.getTop(), mIvIcon.getRight(), mIvIcon.getBottom());
			float startAngle = -90;
			float sweepAngle = mProgress*360.f/mMax;
			boolean useCenter = false;
			
			Paint paint = new Paint();
			paint.setColor(Color.BLUE);
			paint.setAntiAlias(true);
			paint.setStyle(Style.STROKE);
			paint.setStrokeWidth(UIUtils.dip2Px(3));
			
			canvas.drawArc(oval, startAngle, sweepAngle, useCenter, paint);
		}	
		
		
	}

}
