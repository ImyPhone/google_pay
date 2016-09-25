package com.itcast.googlepay.protocol;

import java.util.List;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.itcast.googlepay.base.BaseProtocol;
import com.itcast.googlepay.bean.AppInfoBean;


public class GameProtocol extends BaseProtocol<List<AppInfoBean>> {

	@Override
	public String getInterfaceKey() {
		// TODO Auto-generated method stub
		return "game";
	}

	@Override
	public List<AppInfoBean> parseJson(String jsonString) {
		Gson gson = new Gson();
		return gson.fromJson(jsonString, new TypeToken<List<AppInfoBean>>(){}.getType());

	}
	
}
