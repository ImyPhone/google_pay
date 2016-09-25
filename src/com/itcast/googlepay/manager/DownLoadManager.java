package com.itcast.googlepay.manager;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.itcast.googlepay.bean.AppInfoBean;
import com.itcast.googlepay.conf.Constants.URLS;
import com.itcast.googlepay.factory.ThreadPoolFactory;
import com.itcast.googlepay.utils.CommonUtils;
import com.itcast.googlepay.utils.FileUtils;
import com.itcast.googlepay.utils.UIUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseStream;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

public class DownLoadManager {
	
	public static final int	STATE_UNDOWNLOAD		= 0;// 未下载
	public static final int	STATE_DOWNLOADING		= 1;// 下载中
	public static final int	STATE_PAUSEDOWNLOAD		= 2;// 暂停下载
	public static final int	STATE_WAITINGDOWNLOAD	= 3;// 等待下载
	public static final int	STATE_DOWNLOADFAILED	= 4;// 下载失败
	public static final int	STATE_DOWNLOADED		= 5;// 下载完成
	public static final int	STATE_INSTALLED			= 6;// 已安装
	
	//记录正在下载的一些downloadInfo
	public Map<String , DownLoadInfo> mDownLoadInfoMaps = new HashMap<String, DownLoadInfo>();
	
	private DownLoadManager(){}
	
	public static DownLoadManager manager;
	public static DownLoadManager getInstance(){
		if (manager == null) {
			synchronized (DownLoadManager.class) {
				if (manager == null) {
					manager = new DownLoadManager();
				}
			}
		}
		return manager;
	}

	
	//用户点击下载按钮后跳到此方法  用户点之前会获取当前对应DownLoadInfo的信息，并作为参数传入到此方法
	public void downLoad(DownLoadInfo info){
		
		mDownLoadInfoMaps.put(info.packageName, info);
		
		/*............未下载............*/
		info.state = STATE_UNDOWNLOAD; //每个DownLoadInfo的默认状态
		notifyObservers(info);
		
		/*............等待下载............*/
		info.state = STATE_WAITINGDOWNLOAD;//每个任务一开始都认为处于等待状态(在进入缓存队列中之前就预先改变成等待状态),然后我们在工作线程run()中改变其状态
		notifyObservers(info);
		
		//得到线程池 执行任务
		DownLoadTask task = new DownLoadTask(info);
		info.task = task;
		ThreadPoolFactory.getDownLoadPool().execute(task);	
	}
	
	class DownLoadTask implements Runnable{
		DownLoadInfo mInfo;
		
		public DownLoadTask(DownLoadInfo info) {
			super();
			mInfo = info;
		}

		@Override
		public void run() {
			try {	
				long initRange = 0;
				File saveApk = new File(mInfo.savePath);
				if (saveApk.exists()) {
					initRange = saveApk.length();
				}
				mInfo.curProgress = initRange; //注意 ***③  初始进度
				
				
				/*............下载中...........*/
				mInfo.state = STATE_DOWNLOADING;
				notifyObservers(mInfo);
				
				String url = URLS.DOWNLOADBASEURL;
				HttpUtils httpUtils = new HttpUtils();
				
				RequestParams params = new RequestParams();
				params.addQueryStringParameter("name", mInfo.downloadUrl);
				params.addQueryStringParameter("range", initRange+"");  //注意 ***①  处理请求
				ResponseStream responseStream = httpUtils.sendSync(HttpMethod.GET, url, params);
				if (responseStream.getStatusCode() == 200) {
					InputStream is = null;
					FileOutputStream os = null;	
					boolean isPause = false;
					try {	
						is = responseStream.getBaseStream();
						File saveFile = new File(mInfo.savePath);
						os = new FileOutputStream(saveFile, true); //注意 ***②  追加 
						byte[] buffer = new byte[1024];
						int len = -2;
/*						
						while(len != -1){
							len = is.read(buffer);
							os.write(buffer, 0, len);
							............下载中............
							mInfo.state = STATE_DOWNLOADING;
							mInfo.curProgress += len;
							notifyObservers(mInfo);						
						}*/
						
						while((len = is.read(buffer)) !=-1 ){
							
							if (mInfo.state == STATE_PAUSEDOWNLOAD) {
								isPause = true;
								break;
							}
							
							os.write(buffer, 0, len);
							/*............下载中............*/
							mInfo.state = STATE_DOWNLOADING;
							mInfo.curProgress += len;
							notifyObservers(mInfo);						
						}
						
						if (isPause) {							
							/*.........暂停下载...............*/
							mInfo.state = STATE_PAUSEDOWNLOAD;
							notifyObservers(mInfo);
							
						}else {
							/*............下载完成............*/
							mInfo.state = STATE_DOWNLOADED;
							notifyObservers(mInfo);
						}
								
					} finally{
						com.itcast.googlepay.utils.IOUtils.close(os);
						com.itcast.googlepay.utils.IOUtils.close(is);
					}
					
				}else {
					/*............下载失败............*/
					mInfo.state = STATE_DOWNLOADFAILED;
					notifyObservers(mInfo);
					
				}
			} catch (Exception e) {
				
				/*............下载失败............*/
				System.out.println("气死我了"+e);
				mInfo.state = STATE_DOWNLOADFAILED;
				notifyObservers(mInfo);
				
			}
		}	
	}
	
	//暂停下载
	public void pause(DownLoadInfo info) {
		
		/*............暂停下载............*/
		info.state = STATE_PAUSEDOWNLOAD;
		notifyObservers(info);	
	}
	
	
	//取消下载
	public void cancel(DownLoadInfo info) {
		Runnable task = info.task;
		//找到线程池 移除任务
		ThreadPoolFactory.getDownLoadPool().removeTask(task);

		/*............移除任务   状态<=>未下载............*/  
		info.state = STATE_PAUSEDOWNLOAD;
		notifyObservers(info);	
		
	}
	
	//暴露app当前状态  外界需要最新的状态时调用
	public DownLoadInfo getDownLoadInfo(AppInfoBean data) {
				
		//已安装状态
		if (CommonUtils.isInstalled(UIUtils.getContext(), data.packageName)) {
			DownLoadInfo info = generateDownLoadInfo(data);
			info.state = STATE_INSTALLED;		
			return info;
		}
		
		//下载完成
		DownLoadInfo info = generateDownLoadInfo(data);
		File saveApk = new File(info.savePath);
		if (saveApk.exists()) {		
			if (saveApk.length() == data.size) {
				info.state = STATE_DOWNLOADED;			
				return info;
			}
		}
		
		//下载中  停止下载  等待下载  正在下载   -->run()里面
		DownLoadInfo downLoadInfo = mDownLoadInfoMaps.get(data.packageName);
		if (downLoadInfo != null) {
			return downLoadInfo;
		}
		
		//未下载
		DownLoadInfo tempInfo = generateDownLoadInfo(data);
		tempInfo.state = STATE_UNDOWNLOAD;		
		return tempInfo;
	}


	//根据AppInfoBean生成一个DownLoadinfo,并进行常规属性的赋值   state非常规
	private DownLoadInfo generateDownLoadInfo(AppInfoBean data) {
		
		String dir = FileUtils.getDir("download"); 
		File file = new File(dir, data.packageName+".apk"); //根据包名获取保存路径
		String savePath = file.getAbsolutePath(); //sdcard/android/data/包名/download文件夹/com.baiduinput.xxx.apk
		
		DownLoadInfo info = new DownLoadInfo();	
		info.savePath = savePath;
		info.downloadUrl = data.downloadUrl;
		info.packageName = data.packageName;
		info.max = data.size;
		info.curProgress = 0;
		return info;
	}
	
	
	//自定义观察者设计模式
	List<DownLoadObserver> downLoadObservers = new LinkedList<DownLoadObserver>();
	public interface DownLoadObserver{
		void onDownLoadInfoChange(DownLoadInfo info);
	}
	
	
	//添加观察者
	public void addObserver(DownLoadObserver observer) {
		if (observer == null) {
			throw new NullPointerException("observer == null");
		}
		synchronized (this) {
			if (!downLoadObservers.contains(observer)) {
				downLoadObservers.add(observer);
			}
		}
	}
	
	//删除观察者
	public synchronized void deleteObserver(DownLoadObserver observer) {
		downLoadObservers.remove(observer);
	}
	
	//通知观察者数据改变
	public void notifyObservers(DownLoadInfo info) {
		for (DownLoadObserver observer : downLoadObservers) {
			observer.onDownLoadInfoChange(info);
		}
	}


	

}
