package com.itcast.googlepay.protocol;

import java.util.HashMap;
import java.util.Map;

import com.google.gson.Gson;
import com.itcast.googlepay.base.BaseProtocol;
import com.itcast.googlepay.bean.AppInfoBean;

public class AppDetailProtocol extends BaseProtocol<AppInfoBean> {
	
	private String packageName;
	public AppDetailProtocol(String packageName){
		this.packageName = packageName;
	}
	
	@Override
	public String getInterfaceKey() {
		// TODO Auto-generated method stub
		return "detail";
	}

	@Override
	public AppInfoBean parseJson(String jsonString) {
		Gson gson = new Gson();
		return gson.fromJson(jsonString, AppInfoBean.class);
	}
	
	@Override
	public Map<String, String> getExtraParmas() {
		Map<String, String> extraParmas = new HashMap<String, String>();
		extraParmas.put("packageName", packageName);
		return extraParmas;
	}

}
