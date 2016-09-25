package com.itcast.googlepay.bean;

import java.util.List;

public class AppInfoBean {
	
	public String des;	                    
	public String downloadUrl;	             
	public String iconUrl;	                 
	public long id;	 
	public String name;	 
	public String packageName; 
	public long size; 
	public float stars; 
	
	/*=============== app详情里面的一些字段 ===============*/

	public String					author;		// 应用作者
	public String					date;			// 应用更新时间
	public String					downloadNum;	// 应用下载量
	public String					version;		// 应用版本号

	public List<AppInfoSafeBean>	safe;			// 应用安全部分
	public List<String>				screen;		// 应用的截图

	public class AppInfoSafeBean {
		public String	safeDes;		// 安全描述
		public int		safeDesColor;	// 安全描述部分的文字颜色
		public String	safeDesUrl;	// 安全描述图标url
		public String	safeUrl;		// 安全图标url
	}

	@Override
	public String toString() {
		return "AppInfoBean [des=" + des + ", downloadUrl=" + downloadUrl + ", iconUrl=" + iconUrl + ", id=" + id
				+ ", name=" + name + ", packageName=" + packageName + ", size=" + size + ", stars=" + stars
				+ ", author=" + author + ", date=" + date + ", downloadNum=" + downloadNum + ", version=" + version
				+ ", safe=" + safe + ", screen=" + screen + "]";
	}
}
