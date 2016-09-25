package com.itcast.googlepay.fragment;

import java.util.List;


import android.view.View;
import android.widget.AbsListView;
import android.widget.ListView;
import com.itcast.googlepay.base.BaseFragment;
import com.itcast.googlepay.base.BaseHolder;
import com.itcast.googlepay.base.LoadingPager.LoadedResult;
import com.itcast.googlepay.base.SuperBaseAdapter;
import com.itcast.googlepay.bean.SubjectBean;
import com.itcast.googlepay.factory.ListViewFactory;
import com.itcast.googlepay.holder.SubjectHolder;
import com.itcast.googlepay.protocol.SubjectProtocol;


public class SubjectFragment extends BaseFragment {

	

	private List<SubjectBean> mDatas;

	@Override
	public LoadedResult initData() {
		SubjectProtocol protocol = new SubjectProtocol();
		try {
			mDatas = protocol.loadData(0);
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
		SubjectAdapter adapter = new SubjectAdapter(listView, mDatas);
		listView.setAdapter(adapter);
		return listView;
	}
	
	class SubjectAdapter extends SuperBaseAdapter<SubjectBean>{

		public SubjectAdapter(AbsListView absListView,
				List<SubjectBean> dataSource) {
			super(absListView, dataSource);
			// TODO Auto-generated constructor stub
		}

		@Override
		public BaseHolder<SubjectBean> getSpecialHolder(int position) {
			
			return new SubjectHolder();
		}
		
	}
	
}
