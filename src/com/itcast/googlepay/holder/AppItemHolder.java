package com.itcast.googlepay.holder;

import java.io.File;

import com.itcast.googlepay.R;
import com.itcast.googlepay.base.BaseHolder;
import com.itcast.googlepay.bean.AppInfoBean;
import com.itcast.googlepay.conf.Constants;
import com.itcast.googlepay.manager.DownLoadInfo;
import com.itcast.googlepay.manager.DownLoadManager;
import com.itcast.googlepay.manager.DownLoadManager.DownLoadObserver;
import com.itcast.googlepay.utils.BitmapHelper;
import com.itcast.googlepay.utils.CommonUtils;
import com.itcast.googlepay.utils.StringUtils;
import com.itcast.googlepay.utils.UIUtils;
import com.itcast.googlepay.view.CircleProgressView;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

public class AppItemHolder extends BaseHolder<AppInfoBean> implements
		OnClickListener, DownLoadObserver {
	@ViewInject(R.id.item_appinfo_iv_icon)
	private ImageView mIvIcon;

	@ViewInject(R.id.item_appinfo_rb_stars)
	private RatingBar mRbStar;

	@ViewInject(R.id.item_appinfo_tv_des)
	private TextView mTvDes;

	@ViewInject(R.id.item_appinfo_tv_size)
	private TextView mTvSize;

	@ViewInject(R.id.item_appinfo_tv_title)
	private TextView mTvTitle;

	@ViewInject(R.id.item_appinfo_circleprogressview)
	private CircleProgressView mCircleProgressView;

	private AppInfoBean mdata;

	@Override
	public View initHolderView() {
		View view = View.inflate(UIUtils.getContext(), R.layout.item_app_info,
				null);
		ViewUtils.inject(this, view);
		mCircleProgressView.setOnClickListener(this);
		return view;
	}

	@Override
	public void refreshHolderView(AppInfoBean data) {
		//清除复用convertView之后的progress效果
		mCircleProgressView.setProgress(0);
		
		mdata = data;
		mRbStar.setRating(data.stars);
		mTvDes.setText(data.des);
		mTvTitle.setText(data.name);
		mTvSize.setText(StringUtils.formatFileSize(data.size));

		String url = Constants.URLS.IMAGEBASEURL + data.iconUrl;
		BitmapHelper.display(mIvIcon, url);

		DownLoadInfo info = DownLoadManager.getInstance().getDownLoadInfo(data);
		refreshCircleProgressUI(info);
	}

	private void refreshCircleProgressUI(DownLoadInfo info) {

		switch (info.state) {
		case DownLoadManager.STATE_DOWNLOADED: // 下载完成
			mCircleProgressView.setProgressEnable(false);
			mCircleProgressView.setNote("安装");
			mCircleProgressView.setIvIcon(R.drawable.ic_install);
			break;
		case DownLoadManager.STATE_DOWNLOADFAILED:// 下载失败
			mCircleProgressView.setNote("重试");
			mCircleProgressView.setIvIcon(R.drawable.ic_redownload);
			break;
		case DownLoadManager.STATE_DOWNLOADING: // 正在下载
			mCircleProgressView.setProgressEnable(true);
			mCircleProgressView.setMax(info.max);
			mCircleProgressView.setProgress(info.curProgress);
			int progress = (int) (info.curProgress * 100.f / info.max + 0.5f);
			mCircleProgressView.setNote(progress + "%");
			break;
		case DownLoadManager.STATE_INSTALLED: // 已安装
			mCircleProgressView.setNote("打开");
			mCircleProgressView.setIvIcon(R.drawable.ic_install);
			break;
		case DownLoadManager.STATE_UNDOWNLOAD: // 未下载
			mCircleProgressView.setNote("下载");
			mCircleProgressView.setIvIcon(R.drawable.ic_download);
			break;
		case DownLoadManager.STATE_WAITINGDOWNLOAD: // 等待下载
			mCircleProgressView.setNote("等待中...");
			mCircleProgressView.setIvIcon(R.drawable.ic_pause);
			break;
		case DownLoadManager.STATE_PAUSEDOWNLOAD: // 暂停下载
			mCircleProgressView.setNote("继续下载");
			mCircleProgressView.setIvIcon(R.drawable.ic_resume);
			break;
		}

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.item_appinfo_circleprogressview:
			DownLoadInfo info = DownLoadManager.getInstance().getDownLoadInfo(
					mdata);
			switch (info.state) {
			case DownLoadManager.STATE_DOWNLOADED: // 下载完成
				installApk(info);
				break;
			case DownLoadManager.STATE_DOWNLOADFAILED:// 下载失败
				doDownLoad(info);
				break;
			case DownLoadManager.STATE_DOWNLOADING: // 下载中
				pauseDownLoad(info);
				break;
			case DownLoadManager.STATE_INSTALLED: // 已安装
				openApk(info);
				break;
			case DownLoadManager.STATE_UNDOWNLOAD: // 未下载
				doDownLoad(info);
				break;
			case DownLoadManager.STATE_WAITINGDOWNLOAD: // 等待下载
				cancelDownLoad(info);
				break;
			case DownLoadManager.STATE_PAUSEDOWNLOAD: // 暂停下载
				doDownLoad(info);
				break;
			}
			break;

		default:
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

	// 下载
	private void doDownLoad(DownLoadInfo info) {
		DownLoadManager.getInstance().downLoad(info);
	}

	@Override
	public void onDownLoadInfoChange(final DownLoadInfo info) {
		// 过滤DownLoadInfo
		if (!info.packageName.equals(mdata.packageName)) {
			return;
		}

		UIUtils.postTaskSafely(new Runnable() {

			@Override
			public void run() {
				refreshCircleProgressUI(info);
			}
		});

	}

}
