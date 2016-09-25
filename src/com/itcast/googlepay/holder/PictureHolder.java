package com.itcast.googlepay.holder;

import java.util.List;

import android.R.integer;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;

import com.itcast.googlepay.R;
import com.itcast.googlepay.base.BaseHolder;
import com.itcast.googlepay.conf.Constants.URLS;
import com.itcast.googlepay.utils.BitmapHelper;
import com.itcast.googlepay.utils.UIUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

public class PictureHolder extends BaseHolder<List<String>> {
	@ViewInject(R.id.item_home_picture_pager)
	private ViewPager mViewPager;
	@ViewInject(R.id.item_home_picture_container_indicator)
	private LinearLayout mContainerIndicator;
	private List<String> mDatas;
	
	@Override
	public View initHolderView() {
		View view = View.inflate(UIUtils.getContext(), R.layout.item_home_picture, null);
		ViewUtils.inject(this, view);
		return view;
	}

	@Override
	public void refreshHolderView(List<String> datas) {
		mDatas = datas;
		mViewPager.setAdapter(new PictureAdapter());
		
		//添加点
		for (int i = 0; i < datas.size(); i++) {
			View v = new View(UIUtils.getContext());
			if (i == 0) {
				v.setBackgroundResource(R.drawable.indicator_selected);
			}else{
				v.setBackgroundResource(R.drawable.indicator_normal);
			}
			LayoutParams params = new LayoutParams(UIUtils.dip2Px(6), UIUtils.dip2Px(6));//px->dp
			params.leftMargin = 6;
			v.setLayoutParams(params);
			mContainerIndicator.addView(v);
		}
		
		mViewPager.setOnPageChangeListener(new OnPageChangeListener() {
			
			@Override
			public void onPageSelected(int position) {
				position = position % mDatas.size();
				for (int i = 0; i < mDatas.size(); i++) {
					View v = mContainerIndicator.getChildAt(i);
					if (position == i) {
						v.setBackgroundResource(R.drawable.indicator_selected);
					}else {
						v.setBackgroundResource(R.drawable.indicator_normal);						
					}				
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
		
		//解决一开始不能向左滑，只能无线向右滑！
		//int index = Integer.MAX_VALUE/2;
		//mViewPager.setCurrentItem(index);	
		//但是这样做初始点又不对了
		
		int index = Integer.MAX_VALUE/2;
		int diff = index % mDatas.size();
		mViewPager.setCurrentItem(index - diff);
				
		//自动轮播
		final AutoScrollTask task = new AutoScrollTask();
		task.start();
		
		//用户点击图片停止轮播
		mViewPager.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					task.stop();
					break;

				case MotionEvent.ACTION_MOVE:
					break;
					
				case MotionEvent.ACTION_UP:
					task.start();
					break;
				}
				return false;
			}
		});		
	}
	
	class AutoScrollTask implements Runnable{
		
		public void start() {
			UIUtils.postTaskDelay(this, 2000);
		}
		
		public void stop() {
			UIUtils.removeTask(this);
		}

		@Override
		public void run() {
			int item = mViewPager.getCurrentItem();
			item++;
			mViewPager.setCurrentItem(item);
			
			start();
		}	
	}
	
	class PictureAdapter extends PagerAdapter{

		@Override
		public int getCount() {
			if (mDatas != null) {
				return Integer.MAX_VALUE;
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
			position = position % mDatas.size();
			ImageView iv = new ImageView(UIUtils.getContext());
			iv.setScaleType(ScaleType.FIT_XY);
			iv.setImageResource(R.drawable.ic_default);
			BitmapHelper.display(iv, URLS.IMAGEBASEURL+mDatas.get(position));
			container.addView(iv);
			return iv;
		}
		
		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			
			container.removeView((View) object);
		}
	}

}
