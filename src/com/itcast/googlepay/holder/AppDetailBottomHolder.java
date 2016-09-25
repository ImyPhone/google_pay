package com.itcast.googlepay.holder;

import java.io.File;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

import com.itcast.googlepay.R;
import com.itcast.googlepay.base.BaseHolder;
import com.itcast.googlepay.bean.AppInfoBean;
import com.itcast.googlepay.manager.DownLoadInfo;
import com.itcast.googlepay.manager.DownLoadManager;
import com.itcast.googlepay.manager.DownLoadManager.DownLoadObserver;
import com.itcast.googlepay.utils.CommonUtils;
import com.itcast.googlepay.utils.UIUtils;
import com.itcast.googlepay.view.ProgressButton;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

public class AppDetailBottomHolder extends BaseHolder<AppInfoBean> implements
		OnClickListener, DownLoadObserver {

	@ViewInject(R.id.app_detail_download_btn_favo)
	Button mBtnFavo;
	@ViewInject(R.id.app_detail_download_btn_download)
	ProgressButton mProgressButton;
	@ViewInject(R.id.app_detail_download_btn_share)
	Button mBtnShare;

	AppInfoBean mdata;

	@Override
	public View initHolderView() {
		View view = View.inflate(UIUtils.getContext(),
				R.layout.item_app_detail_bottom, null);
		ViewUtils.inject(this, view);
		mBtnFavo.setOnClickListener(this);
		mProgressButton.setOnClickListener(this);
		mBtnShare.setOnClickListener(this);
		return view;
	}

	@Override
	public void refreshHolderView(AppInfoBean data) {
		mdata = data;
		// 读取应用程序的状态信息
		DownLoadInfo info = DownLoadManager.getInstance().getDownLoadInfo(data);
		//根据状态信息显示下载按钮UI
		refreshProgressBtnUI(info);
	}

	private void refreshProgressBtnUI(DownLoadInfo info) {
		mProgressButton.setBackgroundResource(R.drawable.selector_app_detail_bottom_normal);
		switch (info.state) {
		case DownLoadManager.STATE_DOWNLOADED: //下载完成
			mProgressButton.setProgressEnable(false);
			mProgressButton.setText("安装");
			break;
		case DownLoadManager.STATE_DOWNLOADFAILED://下载失败
			mProgressButton.setText("重试");
			break;
		case DownLoadManager.STATE_DOWNLOADING: //正在下载	
			mProgressButton.setBackgroundResource(R.drawable.selector_app_detail_bottom_downloading);
			mProgressButton.setProgressEnable(true);
			mProgressButton.setMax(info.max);
			mProgressButton.setProgress(info.curProgress);
			int progress = (int) (info.curProgress*100.f/info.max+0.5f);
			mProgressButton.setText(progress + "%");
			break;
		case DownLoadManager.STATE_INSTALLED:  //已安装
			mProgressButton.setText("打开");
			break;
		case DownLoadManager.STATE_UNDOWNLOAD: //未下载
			mProgressButton.setText("下载");
			break;
		case DownLoadManager.STATE_WAITINGDOWNLOAD: //等待下载
			mProgressButton.setText("等待中...");
			break;
		case DownLoadManager.STATE_PAUSEDOWNLOAD: //暂停下载
			mProgressButton.setText("继续下载");
			break;
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.app_detail_download_btn_favo:
			
			break;

		case R.id.app_detail_download_btn_share:
			Toast.makeText(UIUtils.getContext(), "share", 0).show();
			break;

		case R.id.app_detail_download_btn_download:
			
			DownLoadInfo info = DownLoadManager.getInstance().getDownLoadInfo(mdata);
			switch (info.state) {
			case DownLoadManager.STATE_DOWNLOADED: //下载完成
				installApk(info);
				break;
			case DownLoadManager.STATE_DOWNLOADFAILED://下载失败
				doDownLoad(info);
				break;
			case DownLoadManager.STATE_DOWNLOADING: //下载中
				pauseDownLoad(info);
				break;
			case DownLoadManager.STATE_INSTALLED:  //已安装
				openApk(info);
				break;
			case DownLoadManager.STATE_UNDOWNLOAD: //未下载
				doDownLoad(info);
				break;
			case DownLoadManager.STATE_WAITINGDOWNLOAD: //等待下载
				cancelDownLoad(info);
				break;
			case DownLoadManager.STATE_PAUSEDOWNLOAD: //暂停下载
				doDownLoad(info);
				break;
			}
			break;
		}

	}

	private void installApk(DownLoadInfo info) {
		File apkFile = new File(info.savePath);
		CommonUtils.installApp(UIUtils.getContext(), apkFile);
		
	}

	private void pauseDownLoad(DownLoadInfo info) {
		DownLoadManager.getInstance().pause(info);
		
	}

	private void openApk(DownLoadInfo info) {
		CommonUtils.openApp(UIUtils.getContext(), info.packageName);
		
	}

	private void cancelDownLoad(DownLoadInfo info) {
		DownLoadManager.getInstance().cancel(info);
		
	}

	//下载
	private void doDownLoad(DownLoadInfo info) {
		
		/*
		String dir = FileUtils.getDir("download"); // sdcard/android/data/包名/download文件夹
		File file = new File(dir, mdata.packageName + ".apk");
		String savePath = file.getAbsolutePath();// sdcard/android/data/包名/download文件夹/com.baiduinput.xxx.apk

		DownLoadInfo infoo = new DownLoadInfo();
		info.downloadUrl = mdata.downloadUrl; // 文件下载路径
		info.savePath = savePath; // 文件保存路径
		info.packageName = mdata.packageName; // 包名传进去有用
		 */
		DownLoadManager.getInstance().downLoad(info);
	}

	//AppDetailBottomHolder作为观察者 收到数据改变通知  更新UI
	@Override
	public void onDownLoadInfoChange(final DownLoadInfo info) {
		//过滤DownLoadInfo
		if (!info.packageName.equals(mdata.packageName)) {
			return;
		}
		
		UIUtils.postTaskSafely(new Runnable() {
			
			@Override
			public void run() {
				refreshProgressBtnUI(info);				
			}
		});	
	}
	
	
	//此方法为屏幕resume时调用
	public void toBeObserverAndRefreshUI() {
		DownLoadManager.getInstance().addObserver(this);
		DownLoadInfo info = DownLoadManager.getInstance().getDownLoadInfo(mdata);
		DownLoadManager.getInstance().notifyObservers(info); //方式一
		
//		refreshProgressBtnUI(info);//方式二
	}

}
