package com.itcast.googlepay.protocol;

import java.util.List;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.itcast.googlepay.base.BaseProtocol;
import com.itcast.googlepay.bean.SubjectBean;

public class SubjectProtocol extends BaseProtocol<List<SubjectBean>> {

	@Override
	public String getInterfaceKey() {
		// TODO Auto-generated method stub
		return "subject";
	}

	@Override
	public List<SubjectBean> parseJson(String jsonString) {
		Gson gson = new Gson();
		return gson.fromJson(jsonString, new TypeToken<List<SubjectBean>>(){}.getType());
		
	}

}
