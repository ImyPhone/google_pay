package com.itcast.googlepay.fragment;

import java.util.List;

import com.itcast.googlepay.base.BaseFragment;
import com.itcast.googlepay.base.BaseHolder;
import com.itcast.googlepay.base.LoadingPager.LoadedResult;
import com.itcast.googlepay.base.SuperBaseAdapter;
import com.itcast.googlepay.bean.CategoryInfoBean;
import com.itcast.googlepay.factory.ListViewFactory;
import com.itcast.googlepay.holder.CategoryItemHolder;
import com.itcast.googlepay.holder.CategoryTitleHolder;
import com.itcast.googlepay.protocol.CategoreProtocol;

import android.view.View;
import android.widget.AbsListView;
import android.widget.ListView;

public class CategoryFragment extends BaseFragment {

	private List<CategoryInfoBean> mDatas;

	@Override
	public LoadedResult initData() {
		CategoreProtocol protocol = new CategoreProtocol();
		try {
			mDatas = protocol.loadData(0);
			return checkState(mDatas);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return LoadedResult.ERROR;
		}		
	}

	@Override
	public View initSuccessView() {
		ListView listView = ListViewFactory.getListView();
		CategoryAdapter adapter = new CategoryAdapter(listView, mDatas);
		listView.setAdapter(adapter);
		return listView;
	}
	
	class CategoryAdapter extends SuperBaseAdapter<CategoryInfoBean>{

		public CategoryAdapter(AbsListView absListView,
				List<CategoryInfoBean> dataSource) {
			super(absListView, dataSource);
			// TODO Auto-generated constructor stub
		}

		@Override
		public BaseHolder<CategoryInfoBean> getSpecialHolder(int positon) {
			CategoryInfoBean categoryBean = mDatas.get(positon);
			if (categoryBean.isTitle) {
				return new CategoryTitleHolder();
			}else {
				return new CategoryItemHolder();
			}			
		}
		
		@Override
		public int getViewTypeCount() {
			// TODO Auto-generated method stub
			return super.getViewTypeCount()+1;     //3种类型
		}
		
		@Override
		public int getNormalViewType(int position) {   //0 1 2  每个position条目对应的类型
			CategoryInfoBean categoryBean = mDatas.get(position);
			if (categoryBean.isTitle) {
				return super.getNormalViewType(position)+1;   //类型 2  
			}else {
				return super.getNormalViewType(position);	  //类型 1                  类型 0 为父类中的加载更多
			}		
		}
		
	}
	
}
