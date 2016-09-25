package com.itcast.googlepay.protocol;

import com.google.gson.Gson;
import com.itcast.googlepay.base.BaseProtocol;
import com.itcast.googlepay.bean.HomeBean;


public class HomeProtocol extends BaseProtocol<HomeBean> {

	@Override
	public String getInterfaceKey() {
		// TODO Auto-generated method stub
		return "home";
	}

	@Override
	public HomeBean parseJson(String jsonString) {
		Gson gson = new Gson();
		HomeBean homeBean = gson.fromJson(jsonString, HomeBean.class);
		return homeBean;
	}
	
//	public HomeBean loadData(int index) throws Exception{
//		HttpUtils HttpUtils = new HttpUtils();
//		String url = Constants.URLS.BASEURL+"home";
//		RequestParams parmas = new RequestParams();
//		parmas.addQueryStringParameter("index", index+"");
//		ResponseStream responseStream = HttpUtils.sendSync(HttpMethod.GET, url,parmas );		
//		String result = responseStream.readString();		
//		Gson gson = new Gson();
//		HomeBean homeBean = gson.fromJson(result, HomeBean.class);
//		return homeBean;
//	}
}
