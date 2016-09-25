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
	public static final int VIEWTYPE_LOADMORE = 0; //getViewTypeCount()的返回值为2
	public static final int VIEWTYPE_NORMAL = 1;  //所以getItemViewType的返回整数必须在[0,2）之间
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
		
	//listview里面可以显示几种viewtype
	@Override
	public int getViewTypeCount() {
		// TODO 
		return super.getViewTypeCount()+1;
	}
	
	@Override
	public int getItemViewType(int position) {
		//如果滑到底部，viewtype加载更多
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
				
		//显示数据
		if (getItemViewType(position) == VIEWTYPE_LOADMORE) {
			if (hasLoadMore()) {
				//加载更多数据
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
		if (mLoadMoreTask == null) {    //避免多次加载更多 加载更多      任务完成后置为空
			
			//一加载数据的时候就显示下"正在下载..."
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
			//真正开始请求网络数据
			List<ITEMBEANTYPE> loadMoreDatas = null;
			try {
				//得到返回的数据
				loadMoreDatas = onLoadMore();
				
				//根据返回的数据，处理结果，刷新视图
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
					
					//刷新视图 loadmore
					mLoadMoreHolder.setDataAndRefreshHolderView(tempState);
					
					//刷新Listview  -->mDataSource.addAll()
					if (tempLoadMoreDatas != null) {
						mDataSource.addAll(tempLoadMoreDatas); 
						notifyDataSetChanged();
						
					//思考？  当loadMoreDatas由足够的数据时，LoadMoreHolder的状态为STATE_LOADING,视图显示为"正在加载..."
					//那么listview显示"正在加载..."的视图是怎么消失的呢--->notifyDataSetChanged();
						
					//LoadMoreHolder的状态为STATE_RETRY，视图显示为"加载失败，点击重新加载..."
					//为什么listview显示"加载失败，点击重新加载..."的视图却不会因notifyDataSetChanged()而消失呢？
					//因为LoadMoreHolder的状态为STATE_RETRY说明onLoadMore()方法异常，得到的返回结果为空
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
