package com.itcast.googlepay.base;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;

import com.itcast.googlepay.conf.Constants;
import com.itcast.googlepay.utils.FileUtils;
import com.itcast.googlepay.utils.IOUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseStream;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

public abstract class BaseProtocol<T> {
	public T loadData(int index) throws Exception{
		
		//从本地获取数据
		T localBean = getDataFromLocal(index);
		if (localBean != null) {
			return localBean;
		}
		
		//本地无数据再从网络获取
		String jsonString = getDataFromNet(index);		
		T netBean = parseJson(jsonString);
		
		return netBean; 
	}

	private T getDataFromLocal(int index) {
		File cacheFile = getCacheFile(index);
		if (cacheFile.exists()) {
			BufferedReader reader = null;
			try {			
				reader = new BufferedReader(new FileReader(cacheFile));
				String outTime = reader.readLine();
				if ((System.currentTimeMillis()-(Long.parseLong(outTime)))<Constants.PROTOCOLTIMEOUT) {
					String jsonString = reader.readLine();
					T localBean = parseJson(jsonString);
					return localBean;
				}
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}finally{
				IOUtils.close(reader);
			}
		}		
		return null;
	}
	
	
	private File getCacheFile(int index){
		
		String dir = FileUtils.getDir("json");// sdcard/Android/data/包名/json文件夹
		String name = "";//文件名
		Map<String, String> extraParmas = getExtraParmas();
		if (extraParmas == null) {
			name = getInterfaceKey()+"."+index; 
		}else {//APP的详情页
			for (Map.Entry<String, String> info : extraParmas.entrySet()) {
				String key = info.getKey();
				String value = info.getValue(); //com.baiduinput...包名
				name = getInterfaceKey()+"."+value;
			}
		}
		File cacheFile = new File(dir, name);
		return cacheFile;
	}
	
	//返回额外参数  子类需要返回额外参数复写该方法
	public Map<String, String> getExtraParmas(){
		return null;
	}
	
	private String getDataFromNet(int index) throws Exception {
		
		//获取网络数据
		HttpUtils HttpUtils = new HttpUtils();
		String url = Constants.URLS.BASEURL+getInterfaceKey();
		
		RequestParams parmas = new RequestParams();		
		Map<String, String> extraParmas = getExtraParmas();
		if (extraParmas == null) {
			parmas.addQueryStringParameter("index", index+"");
		}else {//子类有额外的参数
			for (Map.Entry<String, String> info : extraParmas.entrySet()) {
				String name = info.getKey();
				String value = info.getValue();
				parmas.addQueryStringParameter(name, value);
			}
		}
		
		
		ResponseStream responseStream = HttpUtils.sendSync(HttpMethod.GET, url,parmas );		
		String jsonString = responseStream.readString();
		
		//保存一份数据到本地
		File cacheFile = getCacheFile(index);
		BufferedWriter writer = null;
		try {
			writer = new BufferedWriter(new FileWriter(cacheFile));
			writer.write(System.currentTimeMillis()+"");
			writer.write("\r\n");
			writer.write(jsonString);
		} catch (Exception e) {
			
			e.printStackTrace();
		}finally{
			IOUtils.close(writer);
		}
		return jsonString;
	}
	
	public abstract String getInterfaceKey();
	public abstract T parseJson(String jsonString);
}
