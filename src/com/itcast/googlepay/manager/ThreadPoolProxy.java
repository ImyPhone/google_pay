package com.itcast.googlepay.manager;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ThreadPoolProxy {
	ThreadPoolExecutor	mExecutor;			// ֻ�贴��һ��
	int					mCorePoolSize;
	int					mMaximumPoolSize;
	long				mKeepAliveTime;

	public ThreadPoolProxy(int corePoolSize, int maximumPoolSize, long keepAliveTime) {
		super();
		mCorePoolSize = corePoolSize;
		mMaximumPoolSize = maximumPoolSize;
		mKeepAliveTime = keepAliveTime;
	}

	private ThreadPoolExecutor initThreadPoolExecutor() {//˫�ؼ�����
		if (mExecutor == null) {
			synchronized (ThreadPoolProxy.class) {
				if (mExecutor == null) {
					TimeUnit unit = TimeUnit.MILLISECONDS;
					BlockingQueue<Runnable> workQueue = new LinkedBlockingQueue<Runnable>();// �޽����
					ThreadFactory threadFactory = Executors.defaultThreadFactory();
					RejectedExecutionHandler handler = new ThreadPoolExecutor.AbortPolicy();// ���������׳�RejectedExecutionException�쳣��
					mExecutor = new ThreadPoolExecutor(//
							mCorePoolSize,// ���ĵ��߳���
							mMaximumPoolSize,// �����߳���
							mKeepAliveTime, // ����ʱ��
							unit, // ����ʱ���Ӧ�ĵ�λ
							workQueue,// �������/��������
							threadFactory, // �̹߳���
							handler// �쳣������
					);
				}
			}
		}
		return mExecutor;
	}

	/**ִ������*/
	public void execute(Runnable task) {
		initThreadPoolExecutor();
		mExecutor.execute(task);
	}

	/**�ύ����*/
	public Future<?> submit(Runnable task) {
		initThreadPoolExecutor();
		return mExecutor.submit(task);
	}

	/**�Ƴ�����*/
	public void removeTask(Runnable task) {
		initThreadPoolExecutor();
		mExecutor.remove(task);
	}
}
