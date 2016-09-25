package com.itcast.googlepay.factory;

import com.itcast.googlepay.base.BaseFragment;
import com.itcast.googlepay.fragment.AppFragment;
import com.itcast.googlepay.fragment.CategoryFragment;
import com.itcast.googlepay.fragment.GameFragment;
import com.itcast.googlepay.fragment.HomeFragment;
import com.itcast.googlepay.fragment.HotFragment;
import com.itcast.googlepay.fragment.RecommendFragment;
import com.itcast.googlepay.fragment.SubjectFragment;

import android.support.v4.util.SparseArrayCompat;

public class FragmentFactory {
	
	public static final int					FRAGMENT_HOME		= 0;
	public static final int					FRAGMENT_APP		= 1;
	public static final int					FRAGMENT_GAME		= 2;
	public static final int					FRAGMENT_SUBJECT	= 3;
	public static final int					FRAGMENT_RECOMMEND	= 4;
	public static final int					FRAGMENT_CATEGORY	= 5;
	public static final int					FRAGMENT_HOT		= 6;
	
	static SparseArrayCompat<BaseFragment> cachesFragment = new SparseArrayCompat<BaseFragment>();
	public static BaseFragment getFragment(int position) {
//		Map<Integer, Fragment> cachesFragmentMap = new HashMap<Integer, Fragment>();
//		有缓存
//		if (cachesFragmentMap.containsKey(position)) {
//		fragment = cachesFragmentMap.get(position);
//			return fragment;
//		}
		

		BaseFragment fragment = null;
		BaseFragment fragment2 = cachesFragment.get(position);
		if (fragment2 != null) {
			fragment = fragment2;
			return fragment;
		}
				
		switch (position) {
		case FRAGMENT_HOME:
			fragment = new HomeFragment();
			break;
		case FRAGMENT_APP:
			fragment = new AppFragment();
			break;
		case FRAGMENT_CATEGORY:
			fragment = new CategoryFragment();
			break;
		case FRAGMENT_GAME:
			fragment = new GameFragment();
			break;
		case FRAGMENT_HOT:
			fragment = new HotFragment();
			break;
		case FRAGMENT_SUBJECT:
			fragment = new SubjectFragment();
			break;
		case FRAGMENT_RECOMMEND:
			fragment = new RecommendFragment();
			break;
		}
		
		//保存对应的fragment
		cachesFragment.put(position, fragment);
		
		return fragment;
	}
}
