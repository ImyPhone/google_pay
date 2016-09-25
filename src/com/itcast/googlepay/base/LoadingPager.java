package com.itcast.googlepay.base;

import com.itcast.googlepay.R;
import com.itcast.googlepay.factory.ThreadPoolFactory;
import com.itcast.googlepay.utils.UIUtils;

import android.content.Context;
import android.view.View;
import android.widget.FrameLayout;

public abstract class LoadingPager extends FrameLayout {
	
	public static final int	STATE_NONE      = -1;
	public static final int	STATE_LOADING	= 0;			// 正在请求网络
	public static final int	STATE_EMPTY		= 1;			// 空状态
	public static final int	STATE_ERROR		= 2;			// 错误状态
	public static final int	STATE_SUCCESS	= 3;			// 成功状态
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
				// 重新触发加载数据
				loadData();
			}
		});
		this.addView(mEmptyView);
		this.addView(mErrorView);
		this.addView(mLoadingView);
		refreshUI();
	}
	
	//第一次调用 LoadingPager初始化
	//第二次调用 显示正在加载数据
	//第三次调用 加载网络数据之后
	private void refreshUI() {
		mLoadingView.setVisibility((mCurState == STATE_LOADING)||(mCurState == STATE_NONE)? 0:8);
		mEmptyView.setVisibility((mCurState == STATE_EMPTY)? 0:8);
		mErrorView.setVisibility((mCurState == STATE_ERROR)? 0:8);
		if (mSuccessView == null && mCurState == STATE_SUCCESS) {
			//创建成功视图
			mSuccessView = initSuccessView();
			this.addView(mSuccessView);
		}
		if (mSuccessView != null) {
			mSuccessView.setVisibility((mCurState == STATE_SUCCESS)? 0:8);
		}
	}
	
	//触发数据的加载
	public void loadData() {
		//如果加载成功，无需再次加载          正在加载状态也无需再次加载
		if (mCurState != STATE_SUCCESS && mCurState != STATE_LOADING) {
			int state = STATE_LOADING;
			mCurState = state;   //保证每次加载视图的mCurState的状态为正在加载状态STATE_LOADING
			refreshUI();
			
//			new Thread(new LoadDataTask()).start();
			ThreadPoolFactory.getNormalPool().execute(new LoadDataTask());
		}		
	}
	
	private class LoadDataTask implements Runnable{

		@Override
		public void run() {
			//真正加载网络数据
			LoadedResult tempState = initData();
			
			mCurState = tempState.getState();
			//刷新UI
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
