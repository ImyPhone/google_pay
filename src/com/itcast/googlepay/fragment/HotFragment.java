package com.itcast.googlepay.fragment;

import java.util.List;
import java.util.Random;

import com.itcast.googlepay.base.BaseFragment;
import com.itcast.googlepay.base.LoadingPager.LoadedResult;
import com.itcast.googlepay.protocol.HotProtocol;
import com.itcast.googlepay.utils.UIUtils;
import com.itcast.googlepay.view.FlowLayout;

import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.StateListDrawable;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

public class HotFragment extends BaseFragment {

	private List<String> mDatas;

	@Override
	public LoadedResult initData() {
		HotProtocol protocol = new HotProtocol();
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
		ScrollView sv= new ScrollView(UIUtils.getContext());
		FlowLayout fl = new FlowLayout(UIUtils.getContext());

		for (final String data : mDatas) {
			TextView tv = new TextView(UIUtils.getContext());
			tv.setText(data);
			tv.setTextColor(Color.WHITE);
			tv.setTextSize(16);
			int padding = UIUtils.dip2Px(5);
			tv.setPadding(padding, padding, padding, padding);
			tv.setGravity(Gravity.CENTER);
			fl.addView(tv);
			
			//shape����-->GradientDrawable����   ѡ����-->StateListDrawable����
			//ѡ������һ�����ݲ�ͬ���������ò�ͬ�ı���
			
			//��������
			GradientDrawable normalDrawable = new GradientDrawable();			
			Random random = new Random();
			int alpha = 255;//͸����			
			int green = random.nextInt(190) + 30; // 30-220
			int red = random.nextInt(190) + 30;// 30-220
			int blue = random.nextInt(190) + 30;// 30-220
			int argb = Color.argb(alpha, red, green, blue);//��ɫ�ϳ�
			normalDrawable.setColor(argb);	
			normalDrawable.setCornerRadius(UIUtils.dip2Px(6));//��Ӧshape��radius��ֵ
			
			//��ס����
			GradientDrawable pressedDrawable = new GradientDrawable();
			pressedDrawable.setColor(Color.DKGRAY);
			pressedDrawable.setCornerRadius(UIUtils.dip2Px(6));
			
			//ѡ����
			StateListDrawable stateListDrawable = new StateListDrawable();
			//stateListDrawable.addState(int[] stateSet, Drawable drawable)
			stateListDrawable.addState(new int[]{android.R.attr.state_pressed}, pressedDrawable);
			stateListDrawable.addState(new int[]{}, normalDrawable);
			
			//��tv����ѡ����
			tv.setClickable(true);
			tv.setBackgroundDrawable(stateListDrawable);
			
			tv.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					Toast.makeText(UIUtils.getContext(), data, 0).show();
				}
			});
			
		}
		sv.addView(fl);
		return sv;
	}
	
}
