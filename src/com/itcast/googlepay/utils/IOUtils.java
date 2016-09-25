package com.itcast.googlepay.utils;

import java.io.Closeable;
import java.io.IOException;

public class IOUtils {
	/** ¹Ø±ÕÁ÷ */
	public static boolean close(Closeable io) {
		if (io != null) {
			try {
				io.close();
			} catch (IOException e) {
				LogUtils.e(e);
			}
		}
		return true;
	}
}

