package com.itcast.googlepay.activity;

import com.itcast.googlepay.R;
import com.itcast.googlepay.base.BaseActivity;
import com.itcast.googlepay.base.LoadingPager;
import com.itcast.googlepay.base.LoadingPager.LoadedResult;
import com.itcast.googlepay.bean.AppInfoBean;
import com.itcast.googlepay.holder.AppDetailBottomHolder;
import com.itcast.googlepay.holder.AppDetailDesHolder;
import com.itcast.googlepay.holder.AppDetailInfoHolder;
import com.itcast.googlepay.holder.AppDetailPicHolder;
import com.itcast.googlepay.holder.AppDetailSafeHolder;
import com.itcast.googlepay.manager.DownLoadManager;
import com.itcast.googlepay.protocol.AppDetailProtocol;
import com.itcast.googlepay.utils.UIUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

import android.support.v7.app.ActionBar;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;

public class DetailActivity extends BaseActivity {

	@ViewInject(R.id.app_detail_bottom)
	FrameLayout mContainerBottom;

	@ViewInject(R.id.app_detail_des)
	FrameLayout mContainerDes;

	@ViewInject(R.id.app_detail_info)
	FrameLayout mContainerInfo;

	@ViewInject(R.id.app_detail_pic)
	FrameLayout mContainerPic;

	@ViewInject(R.id.app_detail_safe)
	FrameLayout mContainerSafe;

	private String mPackageName;
	private AppInfoBean mData;
	private LoadingPager mLoadingPager;

	private AppDetailBottomHolder mAppDetailBottomHolder;

	@Override
	public void init() {
		mPackageName = getIntent().getStringExtra("packageName");
	
	}

	@Override
	public void initView() {
		mLoadingPager = new LoadingPager(UIUtils.getContext()) {
	
			@Override
			public View initSuccessView() {
	
				return onLoadSuccessView();
			}
	
			@Override
			public LoadedResult initData() {
				// TODO Auto-generated method stub
				return onInitData();
			}
		};
		
		//
		setContentView(mLoadingPager);
		
	}

	@Override
	public void initActionBar() {
		ActionBar actionBar = getSupportActionBar();
		actionBar.setTitle("谷歌商城");
		actionBar.setDisplayHomeAsUpEnabled(true);
		
	}
	
	@Override
	public void initDatas() {
		// 触发加载数据
		mLoadingPager.loadData();
	}

	private LoadedResult onInitData() {
		// 发起网络请求
		AppDetailProtocol protocol = new AppDetailProtocol(mPackageName);
		try {
			mData = protocol.loadData(0);
			if (mData == null) {
				return LoadedResult.ERROR;
			}
			return LoadedResult.SUCCESS;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return LoadedResult.ERROR;
		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			finish();
			break;

		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	private View onLoadSuccessView() {
		View view = View.inflate(UIUtils.getContext(),
				R.layout.activity_detail, null);
		ViewUtils.inject(this, view);

		// 填充内容
		// 1.信息部分
		AppDetailInfoHolder appDetailInfoHolder = new AppDetailInfoHolder();
		mContainerInfo.addView(appDetailInfoHolder.getHolderView());
		appDetailInfoHolder.setDataAndRefreshHolderView(mData);

		// 2.安全部分
		AppDetailSafeHolder appDetailSafeHolder = new AppDetailSafeHolder();
		mContainerSafe.addView(appDetailSafeHolder.getHolderView());
		appDetailSafeHolder.setDataAndRefreshHolderView(mData);

		// 3.截图部分
		AppDetailPicHolder appDetailPicHolder = new AppDetailPicHolder();
		mContainerPic.addView(appDetailPicHolder.getHolderView());
		appDetailPicHolder.setDataAndRefreshHolderView(mData);

		// 4.描述部分
		AppDetailDesHolder appDetailDesHolder = new AppDetailDesHolder();
		mContainerDes.addView(appDetailDesHolder.getHolderView());
		appDetailDesHolder.setDataAndRefreshHolderView(mData);

		mAppDetailBottomHolder = new AppDetailBottomHolder();
		mContainerBottom.addView(mAppDetailBottomHolder.getHolderView());
		mAppDetailBottomHolder.setDataAndRefreshHolderView(mData);
		
		DownLoadManager.getInstance().addObserver(mAppDetailBottomHolder);
		
		return view;
	}
	
	@Override
	protected void onPause() {
		if (mAppDetailBottomHolder != null) {
			DownLoadManager.getInstance().deleteObserver(mAppDetailBottomHolder);
		}
		super.onPause();
	}
	
	@Override
	protected void onResume() {  //安装完成 让mAppDetailBottomHolder再次成为观察者，并手动刷新最新状态。
		if (mAppDetailBottomHolder != null) {
			mAppDetailBottomHolder.toBeObserverAndRefreshUI();
		}
		super.onResume();
	}

}
