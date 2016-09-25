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
		// ��ȡӦ�ó����״̬��Ϣ
		DownLoadInfo info = DownLoadManager.getInstance().getDownLoadInfo(data);
		//����״̬��Ϣ��ʾ���ذ�ťUI
		refreshProgressBtnUI(info);
	}

	private void refreshProgressBtnUI(DownLoadInfo info) {
		mProgressButton.setBackgroundResource(R.drawable.selector_app_detail_bottom_normal);
		switch (info.state) {
		case DownLoadManager.STATE_DOWNLOADED: //�������
			mProgressButton.setProgressEnable(false);
			mProgressButton.setText("��װ");
			break;
		case DownLoadManager.STATE_DOWNLOADFAILED://����ʧ��
			mProgressButton.setText("����");
			break;
		case DownLoadManager.STATE_DOWNLOADING: //��������	
			mProgressButton.setBackgroundResource(R.drawable.selector_app_detail_bottom_downloading);
			mProgressButton.setProgressEnable(true);
			mProgressButton.setMax(info.max);
			mProgressButton.setProgress(info.curProgress);
			int progress = (int) (info.curProgress*100.f/info.max+0.5f);
			mProgressButton.setText(progress + "%");
			break;
		case DownLoadManager.STATE_INSTALLED:  //�Ѱ�װ
			mProgressButton.setText("��");
			break;
		case DownLoadManager.STATE_UNDOWNLOAD: //δ����
			mProgressButton.setText("����");
			break;
		case DownLoadManager.STATE_WAITINGDOWNLOAD: //�ȴ�����
			mProgressButton.setText("�ȴ���...");
			break;
		case DownLoadManager.STATE_PAUSEDOWNLOAD: //��ͣ����
			mProgressButton.setText("��������");
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
			case DownLoadManager.STATE_DOWNLOADED: //�������
				installApk(info);
				break;
			case DownLoadManager.STATE_DOWNLOADFAILED://����ʧ��
				doDownLoad(info);
				break;
			case DownLoadManager.STATE_DOWNLOADING: //������
				pauseDownLoad(info);
				break;
			case DownLoadManager.STATE_INSTALLED:  //�Ѱ�װ
				openApk(info);
				break;
			case DownLoadManager.STATE_UNDOWNLOAD: //δ����
				doDownLoad(info);
				break;
			case DownLoadManager.STATE_WAITINGDOWNLOAD: //�ȴ�����
				cancelDownLoad(info);
				break;
			case DownLoadManager.STATE_PAUSEDOWNLOAD: //��ͣ����
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

	//����
	private void doDownLoad(DownLoadInfo info) {
		
		/*
		String dir = FileUtils.getDir("download"); // sdcard/android/data/����/download�ļ���
		File file = new File(dir, mdata.packageName + ".apk");
		String savePath = file.getAbsolutePath();// sdcard/android/data/����/download�ļ���/com.baiduinput.xxx.apk

		DownLoadInfo infoo = new DownLoadInfo();
		info.downloadUrl = mdata.downloadUrl; // �ļ�����·��
		info.savePath = savePath; // �ļ�����·��
		info.packageName = mdata.packageName; // ��������ȥ����
		 */
		DownLoadManager.getInstance().downLoad(info);
	}

	//AppDetailBottomHolder��Ϊ�۲��� �յ����ݸı�֪ͨ  ����UI
	@Override
	public void onDownLoadInfoChange(final DownLoadInfo info) {
		//����DownLoadInfo
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
	
	
	//�˷���Ϊ��Ļresumeʱ����
	public void toBeObserverAndRefreshUI() {
		DownLoadManager.getInstance().addObserver(this);
		DownLoadInfo info = DownLoadManager.getInstance().getDownLoadInfo(mdata);
		DownLoadManager.getInstance().notifyObservers(info); //��ʽһ
		
//		refreshProgressBtnUI(info);//��ʽ��
	}

}
