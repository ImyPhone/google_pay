package com.itcast.googlepay.utils;

import android.text.*;
import android.text.style.CharacterStyle;
import android.text.style.ForegroundColorSpan;

import java.text.DecimalFormat;

public class StringUtils {
	public final static String	UTF_8	= "utf-8";

	/** �ж��ַ����Ƿ���ֵ�����Ϊnull�����ǿ��ַ�������ֻ�пո����Ϊ"null"�ַ������򷵻�true�������򷵻�false */
	public static boolean isEmpty(String value) {
		if (value != null && !"".equalsIgnoreCase(value.trim()) && !"null".equalsIgnoreCase(value.trim())) {
			return false;
		} else {
			return true;
		}
	}

	/** �ж϶���ַ����Ƿ���ȣ����������һ��Ϊ���ַ�������null���򷵻�false��ֻ��ȫ��Ȳŷ���true */
	public static boolean isEquals(String... agrs) {
		String last = null;
		for (int i = 0; i < agrs.length; i++) {
			String str = agrs[i];
			if (isEmpty(str)) {
				return false;
			}
			if (last != null && !str.equalsIgnoreCase(last)) {
				return false;
			}
			last = str;
		}
		return true;
	}

	/**
	 * ����һ������spannable
	 * 
	 * @param content
	 *            �ı�����
	 * @param color
	 *            ������ɫ
	 * @param start
	 *            ��ʼλ��
	 * @param end
	 *            ����λ��
	 * @return ����spannable
	 */
	public static CharSequence getHighLightText(String content, int color, int start, int end) {
		if (TextUtils.isEmpty(content)) {
			return "";
		}
		start = start >= 0 ? start : 0;
		end = end <= content.length() ? end : content.length();
		SpannableString spannable = new SpannableString(content);
		CharacterStyle span = new ForegroundColorSpan(color);
		spannable.setSpan(span, start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		return spannable;
	}

	/**
	 * ��ȡ������ʽ���ַ��������ַ����������»���
	 * 
	 * @param resId
	 *            ������Դ
	 * @return ����������ʽ���ַ���
	 */
	public static Spanned getHtmlStyleString(int resId) {
		StringBuilder sb = new StringBuilder();
		sb.append("<a href=\"\"><u><b>").append(UIUtils.getString(resId)).append(" </b></u></a>");
		return Html.fromHtml(sb.toString());
	}

	/** ��ʽ���ļ���С��������ĩβ��0 */
	public static String formatFileSize(long len) {
		return formatFileSize(len, false);
	}

	/** ��ʽ���ļ���С������ĩβ��0���ﵽ����һ�� */
	public static String formatFileSize(long len, boolean keepZero) {
		String size;
		DecimalFormat formatKeepTwoZero = new DecimalFormat("#.00");
		DecimalFormat formatKeepOneZero = new DecimalFormat("#.0");
		if (len < 1024) {
			size = String.valueOf(len + "B");
		} else if (len < 10 * 1024) {
			// [0, 10KB)��������λС��
			size = String.valueOf(len * 100 / 1024 / (float) 100) + "KB";
		} else if (len < 100 * 1024) {
			// [10KB, 100KB)������һλС��
			size = String.valueOf(len * 10 / 1024 / (float) 10) + "KB";
		} else if (len < 1024 * 1024) {
			// [100KB, 1MB)����λ��������
			size = String.valueOf(len / 1024) + "KB";
		} else if (len < 10 * 1024 * 1024) {
			// [1MB, 10MB)��������λС��
			if (keepZero) {
				size = String.valueOf(formatKeepTwoZero.format(len * 100 / 1024 / 1024 / (float) 100)) + "MB";
			} else {
				size = String.valueOf(len * 100 / 1024 / 1024 / (float) 100) + "MB";
			}
		} else if (len < 100 * 1024 * 1024) {
			// [10MB, 100MB)������һλС��
			if (keepZero) {
				size = String.valueOf(formatKeepOneZero.format(len * 10 / 1024 / 1024 / (float) 10)) + "MB";
			} else {
				size = String.valueOf(len * 10 / 1024 / 1024 / (float) 10) + "MB";
			}
		} else if (len < 1024 * 1024 * 1024) {
			// [100MB, 1GB)����λ��������
			size = String.valueOf(len / 1024 / 1024) + "MB";
		} else {
			// [1GB, ...)��������λС��
			size = String.valueOf(len * 100 / 1024 / 1024 / 1024 / (float) 100) + "GB";
		}
		return size;
	}
}
