package com.itcast.googlepay.base;

import com.itcast.googlepay.R;
import com.itcast.googlepay.factory.ThreadPoolFactory;
import com.itcast.googlepay.utils.UIUtils;

import android.content.Context;
import android.view.View;
import android.widget.FrameLayout;

public abstract class LoadingPager extends FrameLayout {
	
	public static final int	STATE_NONE      = -1;
	public static final int	STATE_LOADING	= 0;			// ������������
	public static final int	STATE_EMPTY		= 1;			// ��״̬
	public static final int	STATE_ERROR		= 2;			// ����״̬
	public static final int	STATE_SUCCESS	= 3;			// �ɹ�״̬
	public int mCurState = STATE_NONE;

	private View mLoadingView;
	private View mEmptyView;
	private View mErrorView;
	private View mSuccessView;
	

	public LoadingPager(Context context) {
		super(context);
		initCommonView();
	}

	private void initCommonView() {
		mLoadingView = View.inflate(UIUtils.getContext(), R.layout.pager_loading, null);
		mErrorView = View.inflate(UIUtils.getContext(), R.layout.pager_error, null);
		mEmptyView = View.inflate(UIUtils.getContext(), R.layout.pager_empty, null);
		mErrorView.findViewById(R.id.error_btn_retry).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// ���´�����������
				loadData();
			}
		});
		this.addView(mEmptyView);
		this.addView(mErrorView);
		this.addView(mLoadingView);
		refreshUI();
	}
	
	//��һ�ε��� LoadingPager��ʼ��
	//�ڶ��ε��� ��ʾ���ڼ�������
	//�����ε��� ������������֮��
	private void refreshUI() {
		mLoadingView.setVisibility((mCurState == STATE_LOADING)||(mCurState == STATE_NONE)? 0:8);
		mEmptyView.setVisibility((mCurState == STATE_EMPTY)? 0:8);
		mErrorView.setVisibility((mCurState == STATE_ERROR)? 0:8);
		if (mSuccessView == null && mCurState == STATE_SUCCESS) {
			//�����ɹ���ͼ
			mSuccessView = initSuccessView();
			this.addView(mSuccessView);
		}
		if (mSuccessView != null) {
			mSuccessView.setVisibility((mCurState == STATE_SUCCESS)? 0:8);
		}
	}
	
	//�������ݵļ���
	public void loadData() {
		//������سɹ��������ٴμ���          ���ڼ���״̬Ҳ�����ٴμ���
		if (mCurState != STATE_SUCCESS && mCurState != STATE_LOADING) {
			int state = STATE_LOADING;
			mCurState = state;   //��֤ÿ�μ�����ͼ��mCurState��״̬Ϊ���ڼ���״̬STATE_LOADING
			refreshUI();
			
//			new Thread(new LoadDataTask()).start();
			ThreadPoolFactory.getNormalPool().execute(new LoadDataTask());
		}		
	}
	
	private class LoadDataTask implements Runnable{

		@Override
		public void run() {
			//����������������
			LoadedResult tempState = initData();
			
			mCurState = tempState.getState();
			//ˢ��UI
			UIUtils.postTaskSafely(new Runnable() {
				
				@Override
				public void run() {
					refreshUI();					
				}
			});
		}
		
	}
	
	
	public abstract LoadedResult initData();
	public abstract View initSuccessView();
	
	public enum LoadedResult{
		SUCCESS(STATE_SUCCESS), ERROR(STATE_ERROR), EMPTY(STATE_EMPTY);
		int	state;

		public int getState() {
			return state;
		}

		private LoadedResult(int state) {
			this.state = state;
		}
	}
}
