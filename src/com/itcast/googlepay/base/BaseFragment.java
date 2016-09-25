package com.itcast.googlepay.base;

import java.util.List;
import java.util.Map;

import com.itcast.googlepay.base.LoadingPager.LoadedResult;
import com.itcast.googlepay.utils.UIUtils;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public abstract class BaseFragment extends Fragment {
	private LoadingPager mLoadingPager;
	
	public LoadingPager getLoadingPager() {
		return mLoadingPager;
	}

	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		
		if (mLoadingPager == null) {//��һ��ִ��
			mLoadingPager = new LoadingPager(UIUtils.getContext()){

				@Override
				public LoadedResult initData() {
					
					return BaseFragment.this.initData();
				}

				@Override
				public View initSuccessView() {
					
					return BaseFragment.this.initSuccessView();
				}
				
			};
		}else { //�ڶ���ִ��
			((ViewGroup)mLoadingPager.getParent()).removeView(mLoadingPager);
		}
		
		return mLoadingPager;   //�Ҿ���mLoadingPager���ڻ���???
	}

	public abstract LoadedResult initData();
	public abstract View initSuccessView();
	
	/**
	 * @param obj ��������json��֮��Ķ���
	 * @return
	 */
	public LoadedResult checkState(Object obj) {
		if (obj == null) {
			return LoadedResult.EMPTY;
		}
		// list
		if (obj instanceof List) {
			if (((List) obj).size() == 0) {
				return LoadedResult.EMPTY;
			}
		}
		// map
		if (obj instanceof Map) {
			if (((Map) obj).size() == 0) {
				return LoadedResult.EMPTY;
			}
		}
		return LoadedResult.SUCCESS;
	}
}
