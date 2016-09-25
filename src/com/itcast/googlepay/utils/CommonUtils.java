package com.itcast.googlepay.utils;

import java.io.File;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;

/**
 * @��Ŀ��: GooglePlay56
 * @����: org.itheima56.googleplay.utils
 * @����: CommonUtils
 * @������: Ф��
 * @����ʱ��: 2015-5-9 ����6:00:40
 * @����: TODO
 * 
 * @svn�汾: $Rev: 48 $
 * @������: $Author: admin $
 * @����ʱ��: $Date: 2015-07-20 10:09:09 +0800 (����һ, 20 ���� 2015) $
 * @��������: TODO
 */
public class CommonUtils
{

	/**
	 * �жϰ��Ƿ�װ
	 * 
	 * @param context
	 * @param packageName
	 * @return
	 */
	public static boolean isInstalled(Context context, String packageName)
	{
		PackageManager manager = context.getPackageManager();
		try
		{
			manager.getPackageInfo(packageName, PackageManager.GET_ACTIVITIES);

			return true;
		}
		catch (NameNotFoundException e)
		{
			return false;
		}
	}

	/**
	 * ��װӦ�ó���
	 * 
	 * @param context
	 * @param apkFile
	 */
	public static void installApp(Context context, File apkFile)
	{
		Intent intent = new Intent(Intent.ACTION_VIEW);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.setDataAndType(Uri.fromFile(apkFile), "application/vnd.android.package-archive");
		context.startActivity(intent);
	}

	/**
	 * ��Ӧ�ó���
	 * 
	 * @param context
	 * @param packageName
	 */
	public static void openApp(Context context, String packageName)
	{
		Intent intent = context.getPackageManager().getLaunchIntentForPackage(packageName);
		context.startActivity(intent);
	}
}
