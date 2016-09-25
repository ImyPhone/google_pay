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
	
	/*=============== app���������һЩ�ֶ� ===============*/

	public String					author;		// Ӧ������
	public String					date;			// Ӧ�ø���ʱ��
	public String					downloadNum;	// Ӧ��������
	public String					version;		// Ӧ�ð汾��

	public List<AppInfoSafeBean>	safe;			// Ӧ�ð�ȫ����
	public List<String>				screen;		// Ӧ�õĽ�ͼ

	public class AppInfoSafeBean {
		public String	safeDes;		// ��ȫ����
		public int		safeDesColor;	// ��ȫ�������ֵ�������ɫ
		public String	safeDesUrl;	// ��ȫ����ͼ��url
		public String	safeUrl;		// ��ȫͼ��url
	}

	@Override
	public String toString() {
		return "AppInfoBean [des=" + des + ", downloadUrl=" + downloadUrl + ", iconUrl=" + iconUrl + ", id=" + id
				+ ", name=" + name + ", packageName=" + packageName + ", size=" + size + ", stars=" + stars
				+ ", author=" + author + ", date=" + date + ", downloadNum=" + downloadNum + ", version=" + version
				+ ", safe=" + safe + ", screen=" + screen + "]";
	}
}
