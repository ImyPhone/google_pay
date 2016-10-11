package com.itcast.googlepay.conf;

import com.itcast.googlepay.utils.LogUtils;

public class Constants {
	public static final int	DEBUGLEVEL	= LogUtils.LEVEL_ALL;//LEVEL_ALL,显示所有的日子,OFF:关闭日志的显示
	public static final int PAGESIZE = 20;	
	public static final int	PROTOCOLTIMEOUT	= 5 * 60 * 1000;		// 5分钟
	public static final class URLS {
		public static final String	BASEURL			= "http://192.168.1.16:8080/GooglePlayServer/";
		// http://localhost:8080/GooglePlayServer/image?name=
		public static final String	IMAGEBASEURL	= BASEURL + "image?name=";
		
		public static final String	DOWNLOADBASEURL	= BASEURL + "download";
		public static final String	HTTP			= "HTTP";
	}

	public static final class PAY {

	}

	public static final class REQ {

	}

	public static final class RES {

	}
}
