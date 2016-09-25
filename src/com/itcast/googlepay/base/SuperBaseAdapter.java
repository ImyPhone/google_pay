package com.itcast.googlepay.base;

import java.util.ArrayList;
import java.util.List;

import com.itcast.googlepay.conf.Constants;
import com.itcast.googlepay.factory.ThreadPoolFactory;
import com.itcast.googlepay.holder.LoadMoreHolder;
import com.itcast.googlepay.utils.UIUtils;

import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;

public abstract class SuperBaseAdapter<ITEMBEANTYPE> extends BaseAdapter implements OnItemClickListener {
	
	public List<ITEMBEANTYPE> mDataSource  = new ArrayList<ITEMBEANTYPE>();
	public static final int VIEWTYPE_LOADMORE = 0; //getViewTypeCount()�ķ���ֵΪ2
	public static final int VIEWTYPE_NORMAL = 1;  //����getItemViewType�ķ�������������[0,2��֮��
	private LoadMoreHolder mLoadMoreHolder;
	private LoadMoreTask mLoadMoreTask;
	private AbsListView mAbsListView;
	
	
	public SuperBaseAdapter(AbsListView absListView, List<ITEMBEANTYPE> dataSource) {
		super();
		absListView.setOnItemClickListener(this);
		mDataSource = dataSource;
		mAbsListView = absListView;
	}

	@Override
	public int getCount() {
		if (mDataSource != null) {
			return mDataSource.size()+1;
		}
		return 0;
	}

	@Override
	public Object getItem(int position) {
		if (mDataSource != null) {
			return mDataSource.get(position);
		}
		return null;
	}

	@Override
	public long getItemId(int position) {
		
		return position;
	}
		
	//listview���������ʾ����viewtype
	@Override
	public int getViewTypeCount() {
		// TODO 
		return super.getViewTypeCount()+1;
	}
	
	@Override
	public int getItemViewType(int position) {
		//��������ײ���viewtype���ظ���
		if (position == getCount()-1) {
			return VIEWTYPE_LOADMORE;
		}
		return getNormalViewType(position);
	}
	
	
	public int getNormalViewType(int position) {
		// TODO Auto-generated method stub
		return VIEWTYPE_NORMAL;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		BaseHolder<ITEMBEANTYPE> holder = null;
		if (convertView == null) {
			if (getItemViewType(position) == VIEWTYPE_LOADMORE) {
				holder = (BaseHolder<ITEMBEANTYPE>) getLoadMoreHolder();
			}else {
				holder = getSpecialHolder(position);
			}
			
		}else {
			holder = (BaseHolder)convertView.getTag();
		}
				
		//��ʾ����
		if (getItemViewType(position) == VIEWTYPE_LOADMORE) {
			if (hasLoadMore()) {
				//���ظ�������
				perFormLoadMore();
			}else{
				mLoadMoreHolder.setDataAndRefreshHolderView(LoadMoreHolder.STATE_NONE);			
			}
		}else {
			holder.setDataAndRefreshHolderView(mDataSource.get(position));
		}	
		
		
		return holder.mHolderView;
	}

	private void perFormLoadMore() {
		if (mLoadMoreTask == null) {    //�����μ��ظ��� ���ظ���      ������ɺ���Ϊ��
			
			//һ�������ݵ�ʱ�����ʾ��"��������..."
			int state = LoadMoreHolder.STATE_LOADING;
			mLoadMoreHolder.setDataAndRefreshHolderView(state);
			
			mLoadMoreTask = new LoadMoreTask();
			ThreadPoolFactory.getNormalPool().execute(mLoadMoreTask);
		}	
	}
	
	class LoadMoreTask implements Runnable{

		@Override
		public void run() {
			int state = LoadMoreHolder.STATE_LOADING;
			//������ʼ������������
			List<ITEMBEANTYPE> loadMoreDatas = null;
			try {
				//�õ����ص�����
				loadMoreDatas = onLoadMore();
				
				//���ݷ��ص����ݣ���������ˢ����ͼ
				if (loadMoreDatas == null) {
					state = LoadMoreHolder.STATE_NONE;
				}else {
					if (loadMoreDatas.size()<Constants.PAGESIZE) {
						state = mLoadMoreHolder.STATE_NONE;
					}else{
						state = LoadMoreHolder.STATE_LOADING;
					}
				}				
			} catch (Exception e) {
				
				e.printStackTrace();    
				state = LoadMoreHolder.STATE_RETRY;    //
			}
			
			
			final int tempState = state;
			final List<ITEMBEANTYPE> tempLoadMoreDatas = loadMoreDatas;
			UIUtils.postTaskSafely(new Runnable() {
				
				@Override
				public void run() {
					
					//ˢ����ͼ loadmore
					mLoadMoreHolder.setDataAndRefreshHolderView(tempState);
					
					//ˢ��Listview  -->mDataSource.addAll()
					if (tempLoadMoreDatas != null) {
						mDataSource.addAll(tempLoadMoreDatas); 
						notifyDataSetChanged();
						
					//˼����  ��loadMoreDatas���㹻������ʱ��LoadMoreHolder��״̬ΪSTATE_LOADING,��ͼ��ʾΪ"���ڼ���..."
					//��ôlistview��ʾ"���ڼ���..."����ͼ����ô��ʧ����--->notifyDataSetChanged();
						
					//LoadMoreHolder��״̬ΪSTATE_RETRY����ͼ��ʾΪ"����ʧ�ܣ�������¼���..."
					//Ϊʲôlistview��ʾ"����ʧ�ܣ�������¼���..."����ͼȴ������notifyDataSetChanged()����ʧ�أ�
					//��ΪLoadMoreHolder��״̬ΪSTATE_RETRY˵��onLoadMore()�����쳣���õ��ķ��ؽ��Ϊ��
					}
				}
			});
			
			mLoadMoreTask = null; 
			
		}
		
	}
	

	private LoadMoreHolder getLoadMoreHolder() {
		if (mLoadMoreHolder == null) {
			mLoadMoreHolder = new LoadMoreHolder();
		}	
		return mLoadMoreHolder;
	}

	public abstract BaseHolder<ITEMBEANTYPE> getSpecialHolder(int positon);
	
	public List<ITEMBEANTYPE> onLoadMore() throws Exception{
		return null;
	};
	
	public boolean hasLoadMore(){
		
		return true;
	}
	
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id){
		
		if (mAbsListView instanceof ListView) {
			position = position - ((ListView)mAbsListView).getHeaderViewsCount();
		}
		
		if (getItemViewType(position) == VIEWTYPE_LOADMORE) {
			perFormLoadMore();
		}else {
			onNormalItemClick(parent, view, position, id);
		}
	}
	
	public void onNormalItemClick(AdapterView<?> parent, View view, int position, long id){
		
	}
}
