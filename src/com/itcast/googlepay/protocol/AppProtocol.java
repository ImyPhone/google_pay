package com.itcast.googlepay.protocol;

import java.util.List;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.itcast.googlepay.base.BaseProtocol;
import com.itcast.googlepay.bean.AppInfoBean;

public class AppProtocol extends BaseProtocol<List<AppInfoBean>> {

	@Override
	public String getInterfaceKey() {
		
		return "app";
	}

	@Override
	public List<AppInfoBean> parseJson(String jsonString) {
		Gson gson = new Gson();
		return gson.fromJson(jsonString, new TypeToken<List<AppInfoBean>>(){}.getType());		
	}

}
