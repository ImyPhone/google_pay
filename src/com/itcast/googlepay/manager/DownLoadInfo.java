package com.itcast.googlepay.manager;



public class DownLoadInfo {
	public String	savePath;
	public String	downloadUrl;
	public int		state	= DownLoadManager.STATE_UNDOWNLOAD; // Ĭ��״̬����δ����
	public String	packageName;								// ����
	public long		max;
	public long		curProgress;
	public Runnable	task;
}
