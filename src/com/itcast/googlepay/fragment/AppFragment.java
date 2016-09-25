package com.itcast.googlepay.fragment;

import java.util.List;

import com.itcast.googlepay.adapter.AppItemAdapter;
import com.itcast.googlepay.base.BaseFragment;
import com.itcast.googlepay.base.BaseHolder;
import com.itcast.googlepay.base.LoadingPager.LoadedResult;
import com.itcast.googlepay.base.SuperBaseAdapter;
import com.itcast.googlepay.bean.AppInfoBean;
import com.itcast.googlepay.factory.ListViewFactory;
import com.itcast.googlepay.holder.AppItemHolder;
import com.itcast.googlepay.manager.DownLoadManager;
import com.itcast.googlepay.protocol.AppProtocol;
import com.itcast.googlepay.utils.UIUtils;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ListView;

public class AppFragment extends BaseFragment {

	private List<AppInfoBean> mDatas;
	private AppProtocol mProtocol;
	private AppAdapter mAdapter;

	@Override
	public LoadedResult initData() {
		mProtocol = new AppProtocol();
		try {
			mDatas = mProtocol.loadData(0);
			LoadedResult checkState = checkState(mDatas);
			return checkState;
		} catch (Exception e) {
			
			e.printStackTrace();
			return LoadedResult.ERROR;
		}
		
	}

	@Override
	public View initSuccessView() {
		ListView listView = ListViewFactory.getListView();
		mAdapter = new AppAdapter(listView, mDatas);
		listView.setAdapter(mAdapter);
		return listView;
	}
	
	
	class AppAdapter extends AppItemAdapter{
	
		public AppAdapter(AbsListView absListView, List<AppInfoBean> dataSource) {
			super(absListView, dataSource);
			// TODO Auto-generated constructor stub
		}

		@Override
		public List<AppInfoBean> onLoadMore() throws Exception {
			
			return mProtocol.loadData(mDatas.size());
		}
		
		
		
	}
	
	@Override
	public void onResume() {
		//重新添加监听 
		if (mAdapter != null) {
			List<AppItemHolder> appItemHolders = mAdapter.getAppItemHolders();
			for (AppItemHolder appItemHolder : appItemHolders) {
				DownLoadManager.getInstance().addObserver(appItemHolder);
			}
			//手动刷新-->刷新UI
			mAdapter.notifyDataSetChanged();
		}		
		super.onResume();
	}
	
	@Override
	public void onPause() {
		//移除监听
		if (mAdapter != null) {
			List<AppItemHolder> appItemHolders = mAdapter.getAppItemHolders();
			for (AppItemHolder appItemHolder : appItemHolders) {
				DownLoadManager.getInstance().deleteObserver(appItemHolder);
			}
		}
		super.onPause();
	}
	
	
}
