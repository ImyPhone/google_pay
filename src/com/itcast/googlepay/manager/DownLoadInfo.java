package com.itcast.googlepay.manager;



public class DownLoadInfo {
	public String	savePath;
	public String	downloadUrl;
	public int		state	= DownLoadManager.STATE_UNDOWNLOAD; // 默认状态就是未下载
	public String	packageName;								// 包名
	public long		max;
	public long		curProgress;
	public Runnable	task;
}
