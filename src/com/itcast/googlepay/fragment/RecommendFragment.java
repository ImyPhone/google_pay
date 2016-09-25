package com.itcast.googlepay.fragment;

import java.util.List;
import java.util.Random;

import com.itcast.googlepay.base.BaseFragment;
import com.itcast.googlepay.base.LoadingPager.LoadedResult;
import com.itcast.googlepay.conf.Constants.PAY;
import com.itcast.googlepay.protocol.RecommendProtocol;
import com.itcast.googlepay.utils.UIUtils;
import com.itcast.googlepay.view.flyoutin.ShakeListener;
import com.itcast.googlepay.view.flyoutin.ShakeListener.OnShakeListener;
import com.itcast.googlepay.view.flyoutin.StellarMap;

import android.graphics.Color;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class RecommendFragment extends BaseFragment {

	private List<String> mDatas;
	public static final int PAGESIZE = 15;
	private ShakeListener mShakeListener;

	@Override
	public LoadedResult initData() {
		RecommendProtocol protocol = new RecommendProtocol();
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
		final StellarMap map = new StellarMap(UIUtils.getContext());

		final RecommendAdapter adapter = new RecommendAdapter();
		map.setAdapter(adapter);
		// 设置第一页的时候显示
		map.setGroup(0, true);
		// 设置把屏幕拆分成多少个格子
		map.setRegularity(15, 20);// 总的就有300个格子

		mShakeListener = new ShakeListener(UIUtils.getContext());
		mShakeListener.setOnShakeListener(new OnShakeListener() {

			@Override
			public void onShake() {
				int groupIndex = map.getCurrentGroup();
				if (groupIndex == adapter.getGroupCount() - 1) {
					groupIndex = 0;
				} else {
					groupIndex++;
				}
				map.setGroup(groupIndex, true);
			}
		});

		return map;
	}

	@Override
	public void onResume() {
		if (mShakeListener != null) {
			mShakeListener.resume();
		}
		super.onResume();
	}

	@Override
	public void onPause() {
		if (mShakeListener != null) {
			mShakeListener.pause();
		}
		super.onPause();
	}

	class RecommendAdapter implements StellarMap.Adapter {

		@Override
		public int getGroupCount() { // 分几组
			int groupCount = mDatas.size() / PAGESIZE;
			if (mDatas.size() % PAGESIZE != 0) {
				return groupCount + 1;
			}
			return groupCount;
		}

		@Override
		public int getCount(int group) {// 每组数据多少

			if (group == getGroupCount() - 1) { // 如果是最后一组
				if (mDatas.size() % PAGESIZE > 0) {
					return mDatas.size() % PAGESIZE;
				}
			}
			return PAGESIZE;
		}

		@Override
		public View getView(int group, int position, View convertView) {
			TextView tv = new TextView(UIUtils.getContext());
			position = PAGESIZE * group + position;
			tv.setText(mDatas.get(position));
			int pading = UIUtils.dip2Px(5);
			tv.setPadding(pading, pading, pading, pading);

			// 字体大小
			Random random = new Random();
			int size = random.nextInt(10) + 10;
			tv.setTextSize(size);
			// 字体颜色
			int alpha = 255;
			int red = random.nextInt(180) + 30;
			int green = random.nextInt(180) + 30;
			int blue = random.nextInt(180) + 30;
			int argb = Color.argb(alpha, red, green, blue);
			tv.setTextColor(argb);
			return tv;
		}

		@Override
		public int getNextGroupOnPan(int group, float degree) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public int getNextGroupOnZoom(int group, boolean isZoomIn) {
			// TODO Auto-generated method stub
			return 0;
		}

	}

}
