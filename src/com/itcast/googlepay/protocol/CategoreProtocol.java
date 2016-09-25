package com.itcast.googlepay.protocol;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import com.itcast.googlepay.base.BaseProtocol;
import com.itcast.googlepay.bean.CategoryInfoBean;

public class CategoreProtocol extends BaseProtocol<List<CategoryInfoBean>> {

	@Override
	public String getInterfaceKey() {
		// TODO Auto-generated method stub
		return "category";
	}

	@Override
	public List<CategoryInfoBean> parseJson(String jsonString) {
		
//		Gson解析--->Bean得根据服务器返回的json格式设置
//		Gson gson = new Gson();
//  	return gson.fromJson(jsonString, new TypeToken<List<CategoryBean>>(){}.getType());
		
		
//      android sdk解析  Bean的设置稍微灵活，可以不用根据服务器返回的json格式设置		
		List<CategoryInfoBean> categoryBeans = new ArrayList<CategoryInfoBean>();
		try {
			JSONArray rootJsonArray = new JSONArray(jsonString);
			for (int i = 0; i < rootJsonArray.length(); i++) {
				
				JSONObject itemJsonObject = rootJsonArray.getJSONObject(i);
				String title = itemJsonObject.getString("title");
				CategoryInfoBean cBean = new CategoryInfoBean();
				cBean.title = title;
				cBean.isTitle = true; 
				
				categoryBeans.add(cBean);
				
				JSONArray itemJsonArray = itemJsonObject.getJSONArray("infos");
				for (int j = 0; j < itemJsonArray.length(); j++) {
					JSONObject infoJsonObject = itemJsonArray.getJSONObject(j);
					CategoryInfoBean cBean2 = new CategoryInfoBean();
					cBean2.name1 = infoJsonObject.getString("name1");
					cBean2.name2 = infoJsonObject.getString("name2");
					cBean2.name3 = infoJsonObject.getString("name3");
					cBean2.url1 = infoJsonObject.getString("url1");
					cBean2.url2 = infoJsonObject.getString("url2");
					cBean2.url3 = infoJsonObject.getString("url3");
					
					categoryBeans.add(cBean2);
				}			
			}
			return categoryBeans;
		} catch (JSONException e) {
			
			e.printStackTrace();
		}
		return null;
	}

}
