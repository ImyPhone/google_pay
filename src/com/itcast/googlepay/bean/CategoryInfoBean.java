package com.itcast.googlepay.bean;

public class CategoryInfoBean {
	
	/*//Gson接下用的Bean
	public String title;
	public List<SpecificAppInfo> infos;
	
	public class SpecificAppInfo{
		public String  name1;
	    public String  name2;
	    public String  name3;
	    public String  url1;
	    public String  url2;
	    public String  url3;
	}*/
	
	
	//android sdk解析用的Bean  
	public String title;
	public String  name1;
    public String  name2;
    public String  name3;
    public String  url1;
    public String  url2;
    public String  url3;
    public boolean isTitle;
	@Override
	public String toString() {
		return "CategoryBean [title=" + title + ", name1=" + name1 + ", name2="
				+ name2 + ", name3=" + name3 + ", url1=" + url1 + ", url2="
				+ url2 + ", url3=" + url3 + ", isTitle=" + isTitle + "]";
	}
	
}
