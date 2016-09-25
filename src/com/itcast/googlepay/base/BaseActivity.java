package com.itcast.googlepay.base;

import java.util.LinkedList;
import java.util.List;

import com.itcast.googlepay.activity.MainActivity;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.widget.Toast;

public abstract class BaseActivity extends ActionBarActivity {

	private List<ActionBarActivity> activities = new LinkedList<ActionBarActivity>();
	private long mPreTime;
	private Activity mCurActivity;

	public Activity getCurActivity() {
		return mCurActivity;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		init();
		initView();
		initActionBar();
		initDatas();
		initEvent();
		activities.add(this);
	}
	
	@Override
	protected void onResume() {
		mCurActivity = this; //最上层的activity
		super.onResume();
	}

	@Override
	protected void onDestroy() {
		activities.remove(this);
		super.onDestroy();
	}
	
	//完全退出
	public void exit() {
		for (ActionBarActivity activity : activities) {
			activity.finish();
		}
	}
	
	@Override
	public void onBackPressed() {
		if (this instanceof MainActivity) {
			
			if (System.currentTimeMillis() - mPreTime >2000) {//两次点击间隔大于两秒
				Toast.makeText(getApplicationContext(), "再按一下退出谷歌商城", 0).show();
				mPreTime = System.currentTimeMillis();
				return;
			}
			
		}
		super.onBackPressed(); // <=>finish
	
	}

	public void init() {
		// TODO Auto-generated method stub

	}

	public abstract void initView();

	public void initActionBar() {
		// TODO Auto-generated method stub

	}

	public void initDatas() {
		// TODO Auto-generated method stub

	}

	public void initEvent() {
		// TODO Auto-generated method stub

	}

}
