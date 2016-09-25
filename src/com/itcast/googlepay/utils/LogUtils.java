package com.itcast.googlepay.utils;

import java.util.List;

import com.itcast.googlepay.conf.Constants;

import android.text.TextUtils;
import android.util.Log;


/**
 * @author itheima
 * @time 	2015-6-23 ����11:33:49
 * @des	   ��־������LEVEL_ALL��ʾ������Ϣ,����System.out.println��Ϣ
 * @des  ��־������LEVEL_OFF�ر�������Ϣ,����System.out.println��Ϣ
 * @updateAuthor TODO
 * @updateTime TODO
 * @updateDes TODO
 */
public class LogUtils {
	/** ��־���ʱ��TAG */
	private static String mTag = "GooglePlay";
	/** ��־�������NONE */
	public static final int LEVEL_OFF = 0;
	/** ��־�������NONE */
	public static final int LEVEL_ALL = 7;

	/** ��־�������V */
	public static final int LEVEL_VERBOSE = 1;
	/** ��־�������D */
	public static final int LEVEL_DEBUG = 2;
	/** ��־�������I */
	public static final int LEVEL_INFO = 3;
	/** ��־�������W */
	public static final int LEVEL_WARN = 4;
	/** ��־�������E */
	public static final int LEVEL_ERROR = 5;
	/** ��־�������S,�Զ��嶨���һ������ */
	public static final int LEVEL_SYSTEM = 6;

	/** �Ƿ��������log */
	private static int mDebuggable = Constants.DEBUGLEVEL;

	/** ���ڼ�ʱ�ı��� */
	private static long mTimestamp = 0;
	/** д�ļ��������� */
	private static final Object mLogLock = new Object();

	/**---------------��־���,�ѹ̶�TAG  begin---------------**/
	/** �Լ���Ϊ d ����ʽ���LOG */
	public static void v(String msg) {
		if (mDebuggable >= LEVEL_VERBOSE) {
			Log.v(mTag, msg);
		}
	}

	/** �Լ���Ϊ d ����ʽ���LOG */
	public static void d(String msg) {
		if (mDebuggable >= LEVEL_DEBUG) {
			Log.d(mTag, msg);
		}
	}

	/** �Լ���Ϊ i ����ʽ���LOG */
	public static void i(String msg) {
		if (mDebuggable >= LEVEL_INFO) {
			Log.i(mTag, msg);
		}
	}

	/** �Լ���Ϊ w ����ʽ���LOG */
	public static void w(String msg) {
		if (mDebuggable >= LEVEL_WARN) {
			Log.w(mTag, msg);
		}
	}

	/** �Լ���Ϊ w ����ʽ���Throwable */
	public static void w(Throwable tr) {
		if (mDebuggable >= LEVEL_WARN) {
			Log.w(mTag, "", tr);
		}
	}

	/** �Լ���Ϊ w ����ʽ���LOG��Ϣ��Throwable */
	public static void w(String msg, Throwable tr) {
		if (mDebuggable >= LEVEL_WARN && null != msg) {
			Log.w(mTag, msg, tr);
		}
	}

	/** �Լ���Ϊ e ����ʽ���LOG */
	public static void e(String msg) {
		if (mDebuggable >= LEVEL_ERROR) {
			Log.e(mTag, msg);
		}
	}

	/** �Լ���Ϊ s ����ʽ���LOG,��Ҫ��Ϊ��System.out.println,��΢��ʽ����һ�� */
	public static void sf(String msg) {
		if (mDebuggable >= LEVEL_ERROR) {
			System.out.println("----------" + msg + "----------");
		}
	}

	/** �Լ���Ϊ s ����ʽ���LOG,��Ҫ��Ϊ��System.out.println */
	public static void s(String msg) {
		if (mDebuggable >= LEVEL_ERROR) {
			System.out.println(msg);
		}
	}

	/** �Լ���Ϊ e ����ʽ���Throwable */
	public static void e(Throwable tr) {
		if (mDebuggable >= LEVEL_ERROR) {
			Log.e(mTag, "", tr);
		}
	}

	/** �Լ���Ϊ e ����ʽ���LOG��Ϣ��Throwable */
	public static void e(String msg, Throwable tr) {
		if (mDebuggable >= LEVEL_ERROR && null != msg) {
			Log.e(mTag, msg, tr);
		}
	}

	/**---------------��־���,�ѹ̶�TAG  end---------------**/

	/**---------------��־���,δ�̶�TAG  begin---------------**/
	/** �Լ���Ϊ d ����ʽ���LOG */
	public static void v(String tag, String msg) {
		if (mDebuggable >= LEVEL_VERBOSE) {
			Log.v(tag, msg);
		}
	}

	/** �Լ���Ϊ d ����ʽ���LOG */
	public static void d(String tag, String msg) {
		if (mDebuggable >= LEVEL_DEBUG) {
			Log.d(tag, msg);
		}
	}

	/** �Լ���Ϊ i ����ʽ���LOG */
	public static void i(String tag, String msg) {
		if (mDebuggable >= LEVEL_INFO) {
			Log.i(tag, msg);
		}
	}

	/** �Լ���Ϊ w ����ʽ���LOG */
	public static void w(String tag, String msg) {
		if (mDebuggable >= LEVEL_WARN) {
			Log.w(tag, msg);
		}
	}

	/** �Լ���Ϊ e ����ʽ���LOG */
	public static void e(String tag, String msg) {
		if (mDebuggable >= LEVEL_ERROR) {
			Log.e(tag, msg);
		}
	}

	/**---------------��־���,δ�̶�TAG  end---------------**/

	/**
	 * ��Log�洢���ļ���
	 * 
	 * @param log
	 *            ��Ҫ�洢����־
	 * @param path
	 *            �洢·��
	 */
	public static void log2File(String log, String path) {
		log2File(log, path, true);
	}

	public static void log2File(String log, String path, boolean append) {
		synchronized (mLogLock) {
			FileUtils.writeFile(log + "\r\n", path, append);
		}
	}

	/**
	 * �Լ���Ϊ e ����ʽ���msg��Ϣ,����ʱ������������һ��ʱ�����ʼ��
	 * 
	 * @param msg
	 *            ��Ҫ�����msg
	 */
	public static void msgStartTime(String msg) {
		mTimestamp = System.currentTimeMillis();
		if (!TextUtils.isEmpty(msg)) {
			e("[Started��" + mTimestamp + "]" + msg);
		}
	}

	/** �Լ���Ϊ e ����ʽ���msg��Ϣ,����ʱ������������һ��ʱ��ν�����* @param msg ��Ҫ�����msg */
	public static void elapsed(String msg) {
		long currentTime = System.currentTimeMillis();
		long elapsedTime = currentTime - mTimestamp;
		mTimestamp = currentTime;
		e("[Elapsed��" + elapsedTime + "]" + msg);
	}

	public static <T> void printList(List<T> list) {
		if (list == null || list.size() < 1) {
			return;
		}
		int size = list.size();
		i("---begin---");
		for (int i = 0; i < size; i++) {
			i(i + ":" + list.get(i).toString());
		}
		i("---end---");
	}

	public static <T> void printArray(T[] array) {
		if (array == null || array.length < 1) {
			return;
		}
		int length = array.length;
		i("---begin---");
		for (int i = 0; i < length; i++) {
			i(i + ":" + array[i].toString());
		}
		i("---end---");
	}
}
