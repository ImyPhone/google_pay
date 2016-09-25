package com.itcast.googlepay.protocol;

import java.util.List;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.itcast.googlepay.base.BaseProtocol;

public class RecommendProtocol extends BaseProtocol<List<String>> {

	@Override
	public String getInterfaceKey() {
		// TODO Auto-generated method stub
		return "recommend";
	}

	@Override
	public List<String> parseJson(String jsonString) {
		Gson gson = new Gson();
		return gson.fromJson(jsonString, new TypeToken<List<String>>(){}.getType());
	}

}
