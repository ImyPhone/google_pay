package com.itcast.googlepay.activity;

import com.astuetz.PagerSlidingTabStripExtends;
import com.itcast.googlepay.R;
import com.itcast.googlepay.base.BaseActivity;
import com.itcast.googlepay.base.BaseFragment;
import com.itcast.googlepay.factory.FragmentFactory;
import com.itcast.googlepay.holder.MenuHolder;
import com.itcast.googlepay.utils.UIUtils;

import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.os.Bundle;

public class MainActivity extends BaseActivity {

	private String[] mTitles;
	private PagerSlidingTabStripExtends mTabs;
	private ViewPager mViewPager;
	private DrawerLayout mDrawerLayout;
	private ActionBarDrawerToggle mToggle;
	private FrameLayout mMain_menu;
	
	@Override
	public void initView() {
		setContentView(R.layout.activity_main);
		mTabs = (PagerSlidingTabStripExtends) findViewById(R.id.tabs);
		mViewPager = (ViewPager) findViewById(R.id.viewpager);
		mDrawerLayout = (DrawerLayout) findViewById(R.id.main_drawlayout);
		mMain_menu = (FrameLayout) findViewById(R.id.main_menu);
	}
	
	@Override
	public void initActionBar() {
		ActionBar actionBar = getSupportActionBar();
		actionBar.setLogo(R.drawable.ic_launcher);
		actionBar.setTitle("谷歌商城");
	
		actionBar.setDisplayHomeAsUpEnabled(true);
		
		initActionBarToggle();
	}
	
	@Override
	public void initDatas() {
		mTitles = UIUtils.getStringArray(R.array.main_titles);
		// mViewPager.setAdapter(new MyAdapter());
		mViewPager.setAdapter(new MyyAdapter(getSupportFragmentManager()));
		mTabs.setViewPager(mViewPager);
		
		
		MenuHolder menuHolder = new MenuHolder();
		mMain_menu.addView(menuHolder.getHolderView());
		menuHolder.setDataAndRefreshHolderView(null);
		
	}
	
	@Override
	public void initEvent() {
		mTabs.setOnPageChangeListener(new OnPageChangeListener() {

			// 滑到哪页，哪页就开始加载数据 但第一次打开首页不加载，因为没涉及到PageChange
			@Override
			public void onPageSelected(int position) {
				BaseFragment fragment = FragmentFactory.getFragment(position);
				if (fragment != null) {
					fragment.getLoadingPager().loadData();
				}
			}

			@Override
			public void onPageScrolled(int position, float positionOffset,
					int positionOffsetPixels) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onPageScrollStateChanged(int state) {
				// TODO Auto-generated method stub

			}
		});

	}

	private void initActionBarToggle() {
		mToggle = new ActionBarDrawerToggle(
				this, 
				mDrawerLayout, //此Activity的根布局
				R.drawable.ic_drawer_am,
				R.string.open,
				R.string.close);
		
		//同步状态的方法
		mToggle.syncState();
		//设置mDrawerLayout的拖动监听
		mDrawerLayout.setDrawerListener(mToggle);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			//mToggle控制菜单的打开关闭
			mToggle.onOptionsItemSelected(item);
			break;

		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}
	
	private class MyAdapter extends PagerAdapter {

		@Override
		public int getCount() {
			if (mTitles != null) {
				return mTitles.length;
			}
			return 0;
		}

		@Override
		public boolean isViewFromObject(View view, Object object) {
			// TODO Auto-generated method stub
			return view == object;
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			TextView tv = new TextView(UIUtils.getContext());
			tv.setText(mTitles[position]);
			tv.setGravity(Gravity.CENTER);
			container.addView(tv);
			return tv;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView((View) object);
		}

		@Override
		public CharSequence getPageTitle(int position) {
			// TODO Auto-generated method stub
			return mTitles[position];
		}

	}

	private class MyyAdapter extends FragmentStatePagerAdapter {

		public MyyAdapter(FragmentManager fm) {
			super(fm);

		}

		@Override
		public Fragment getItem(int position) {
			Fragment fragment = FragmentFactory.getFragment(position);
			return fragment;
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return mTitles.length;
		}

		@Override
		public CharSequence getPageTitle(int position) {
			// TODO Auto-generated method stub
			return mTitles[position];
		}
	}
}
