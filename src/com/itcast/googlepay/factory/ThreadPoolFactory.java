package com.itcast.googlepay.factory;

import com.itcast.googlepay.manager.ThreadPoolProxy;

public class ThreadPoolFactory {
	static ThreadPoolProxy	mNormalPool;
	static ThreadPoolProxy	mDownLoadPool;

	/**�õ�һ����ͨ���̳߳�*/
	public static ThreadPoolProxy getNormalPool() {
		if (mNormalPool == null) {
			synchronized (ThreadPoolProxy.class) {
				if (mNormalPool == null) {
					mNormalPool = new ThreadPoolProxy(5, 5, 3000);
				}
			}
		}
		return mNormalPool;
	}
	/**�õ�һ�����ص��̳߳�*/
	public static ThreadPoolProxy getDownLoadPool() {
		if (mDownLoadPool == null) {
			synchronized (ThreadPoolProxy.class) {
				if (mDownLoadPool == null) {
					mDownLoadPool = new ThreadPoolProxy(3, 3, 3000);
				}
			}
		}
		return mDownLoadPool;
	}
}
