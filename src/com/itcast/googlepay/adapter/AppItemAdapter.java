package com.itcast.googlepay.adapter;

import java.util.LinkedList;
import java.util.List;

import android.content.Intent;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import com.itcast.googlepay.activity.DetailActivity;
import com.itcast.googlepay.base.BaseHolder;
import com.itcast.googlepay.base.SuperBaseAdapter;
import com.itcast.googlepay.bean.AppInfoBean;
import com.itcast.googlepay.holder.AppItemHolder;
import com.itcast.googlepay.manager.DownLoadManager;
import com.itcast.googlepay.utils.UIUtils;

public class AppItemAdapter extends SuperBaseAdapter<AppInfoBean> {
	
	private List<AppItemHolder> mAppItemHolders = new LinkedList<AppItemHolder>();

	public List<AppItemHolder> getAppItemHolders() {
		return mAppItemHolders;
	}

	@Override
	public BaseHolder<AppInfoBean> getSpecialHolder(int positon) {
		AppItemHolder appItemHolder = new AppItemHolder();
		
		mAppItemHolders.add(appItemHolder);//保存adapter里面对应的holder
		
		//初始化时(显示时)让AppItemHolder成为DownLoadManager的观察者
		DownLoadManager.getInstance().addObserver(appItemHolder);
		
		return appItemHolder;
	}
	
	public AppItemAdapter(AbsListView absListView, List<AppInfoBean> dataSource) {
		super(absListView, dataSource);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public void onNormalItemClick(AdapterView<?> parent, View view,
			int position, long id) {
		goToDetailActivity(mDataSource.get(position).packageName);
	}

	private void goToDetailActivity(String packageName) {
		Intent intent = new Intent(UIUtils.getContext(), DetailActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);  
//		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.putExtra("packageName", packageName);
		UIUtils.getContext().startActivity(intent); 
	}

}
