package com.itcast.googlepay.fragment;

import java.util.List;

import com.itcast.googlepay.adapter.AppItemAdapter;
import com.itcast.googlepay.base.BaseFragment;
import com.itcast.googlepay.base.BaseHolder;
import com.itcast.googlepay.base.SuperBaseAdapter;
import com.itcast.googlepay.base.LoadingPager.LoadedResult;
import com.itcast.googlepay.bean.AppInfoBean;
import com.itcast.googlepay.bean.HomeBean;
import com.itcast.googlepay.factory.ListViewFactory;
import com.itcast.googlepay.holder.AppItemHolder;
import com.itcast.googlepay.holder.PictureHolder;
import com.itcast.googlepay.manager.DownLoadManager;
import com.itcast.googlepay.protocol.HomeProtocol;
import com.itcast.googlepay.utils.UIUtils;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

public class HomeFragment extends BaseFragment {
	
	private List<AppInfoBean> mAppInfos;
	private List<String> mPictures;
	private HomeProtocol mHomeProtocol;
	private HomeAdapter mAdapter;

	@Override
	public LoadedResult initData() {
		
/*		try {	
			HttpUtils HttpUtils = new HttpUtils();
			//http://192.168.1.102:8080/GooglePlayServer/home?index=0
			String url = Constants.URLS.BASEURL+"home";
			RequestParams parmas = new RequestParams();
			parmas.addQueryStringParameter("index", "0");
			ResponseStream responseStream = HttpUtils.sendSync(HttpMethod.GET, url,parmas );
			
//			ByteArrayOutputStream os = new ByteArrayOutputStream();
//			byte[] buffer = new byte[1024];
//			int len = 0;
//			while (len != -1) {
//				len = responseStream.read(buffer);
//				os.write(buffer);
//			}			
//			String result = new String(os.toByteArray(), "utf-8");
//			System.out.println(result);
//			Ϊʲôת�����������أ�
			
			String result = responseStream.readString();
//			System.out.println("result = "+result);
			
			Gson gson = new Gson();
			HomeBean homeBean = gson.fromJson(result, HomeBean.class);
			LoadedResult state = checkState(homeBean);
			if (state != LoadedResult.SUCCESS) {// ������ɹ�,��ֱ�ӷ���,�ߵ�����˵��homeBean��ok
				return state;
			}

			state = checkState(homeBean.list);
			if (state != LoadedResult.SUCCESS) {// ������ɹ�,��ֱ�ӷ���,�ߵ�����˵��homeBean.list��ok
				return state;
			}
			mAppInfos = homeBean.list;
			mPictures = homeBean.picture;*/
		mHomeProtocol = new HomeProtocol();
		try{			
			HomeBean homeBean = mHomeProtocol.loadData(0);
			LoadedResult state = checkState(homeBean);
			if (state != LoadedResult.SUCCESS) {// ������ɹ�,��ֱ�ӷ���,�ߵ�����˵��homeBean��ok
				return state;
			}

			state = checkState(homeBean.list);
			if (state != LoadedResult.SUCCESS) {// ������ɹ�,��ֱ�ӷ���,�ߵ�����˵��homeBean.list��ok
				return state;
			}
			mAppInfos = homeBean.list;
			mPictures = homeBean.picture;
		
		} catch (Exception e) {
			e.printStackTrace();
			return LoadedResult.ERROR;
		}
		
		return LoadedResult.SUCCESS;
	}

	@Override
	public View initSuccessView() {
		ListView listView = ListViewFactory.getListView();
		mAdapter = new HomeAdapter(listView, mAppInfos);
		
		PictureHolder pictureHolder = new PictureHolder();
		pictureHolder.setDataAndRefreshHolderView(mPictures);
		View pictureView = pictureHolder.getHolderView();	
		listView.addHeaderView(pictureView);
		
		listView.setAdapter(mAdapter);
		return listView;
	}
	
	class HomeAdapter extends AppItemAdapter{
		
		public HomeAdapter(AbsListView absListView, List<AppInfoBean> dataSource) {
			super(absListView, dataSource);
			// TODO Auto-generated constructor stub
		}

		@Override
		public List<AppInfoBean> onLoadMore() throws Exception {
			
			return loadMore(mAppInfos.size());   //Ϊʲô��mAppInfos.size()�أ�  20�����ݵ���������
		}										 //���ظ�����  -> mDataSource.addAll(tempLoadMoreDatas);->notifyDataSetChanged();
												//20+20+13
	}
	private List<AppInfoBean> loadMore(int index) throws Exception {
		/*HttpUtils httpUtils = new HttpUtils();
		String url = Constants.URLS.BASEURL+"home";
		RequestParams parmas = new RequestParams();
		parmas.addQueryStringParameter("index", index+"");
		ResponseStream responseStream = httpUtils.sendSync(HttpMethod.GET, url,parmas );
		String result = responseStream.readString();
		Gson gson = new Gson();
		HomeBean homeBean = gson.fromJson(result, HomeBean.class);*/
		
		HomeBean homeBean = mHomeProtocol.loadData(index);
		
		if (homeBean == null) {
			return null;
		}
		if (homeBean.list == null) {
			return null;
		}
		return homeBean.list;
	}
	
	@Override
	public void onResume() {
		//������Ӽ��� 
		if (mAdapter != null) {
			List<AppItemHolder> appItemHolders = mAdapter.getAppItemHolders();
			for (AppItemHolder appItemHolder : appItemHolders) {
				DownLoadManager.getInstance().addObserver(appItemHolder);
			}
			//�ֶ�ˢ��-->ˢ��UI
			mAdapter.notifyDataSetChanged();
		}
		
	
		
		super.onResume();
	}
	
	@Override
	public void onPause() {
		//�Ƴ�����
		if (mAdapter != null) {
			List<AppItemHolder> appItemHolders = mAdapter.getAppItemHolders();
			for (AppItemHolder appItemHolder : appItemHolders) {
				DownLoadManager.getInstance().deleteObserver(appItemHolder);
			}
		}
		super.onPause();
	}
	
}
