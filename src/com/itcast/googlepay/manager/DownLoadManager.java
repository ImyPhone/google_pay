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
	
	public static final int	STATE_UNDOWNLOAD		= 0;// δ����
	public static final int	STATE_DOWNLOADING		= 1;// ������
	public static final int	STATE_PAUSEDOWNLOAD		= 2;// ��ͣ����
	public static final int	STATE_WAITINGDOWNLOAD	= 3;// �ȴ�����
	public static final int	STATE_DOWNLOADFAILED	= 4;// ����ʧ��
	public static final int	STATE_DOWNLOADED		= 5;// �������
	public static final int	STATE_INSTALLED			= 6;// �Ѱ�װ
	
	//��¼�������ص�һЩdownloadInfo
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

	
	//�û�������ذ�ť�������˷���  �û���֮ǰ���ȡ��ǰ��ӦDownLoadInfo����Ϣ������Ϊ�������뵽�˷���
	public void downLoad(DownLoadInfo info){
		
		mDownLoadInfoMaps.put(info.packageName, info);
		
		/*............δ����............*/
		info.state = STATE_UNDOWNLOAD; //ÿ��DownLoadInfo��Ĭ��״̬
		notifyObservers(info);
		
		/*............�ȴ�����............*/
		info.state = STATE_WAITINGDOWNLOAD;//ÿ������һ��ʼ����Ϊ���ڵȴ�״̬(�ڽ��뻺�������֮ǰ��Ԥ�ȸı�ɵȴ�״̬),Ȼ�������ڹ����߳�run()�иı���״̬
		notifyObservers(info);
		
		//�õ��̳߳� ִ������
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
				mInfo.curProgress = initRange; //ע�� ***��  ��ʼ����
				
				
				/*............������...........*/
				mInfo.state = STATE_DOWNLOADING;
				notifyObservers(mInfo);
				
				String url = URLS.DOWNLOADBASEURL;
				HttpUtils httpUtils = new HttpUtils();
				
				RequestParams params = new RequestParams();
				params.addQueryStringParameter("name", mInfo.downloadUrl);
				params.addQueryStringParameter("range", initRange+"");  //ע�� ***��  ��������
				ResponseStream responseStream = httpUtils.sendSync(HttpMethod.GET, url, params);
				if (responseStream.getStatusCode() == 200) {
					InputStream is = null;
					FileOutputStream os = null;	
					boolean isPause = false;
					try {	
						is = responseStream.getBaseStream();
						File saveFile = new File(mInfo.savePath);
						os = new FileOutputStream(saveFile, true); //ע�� ***��  ׷�� 
						byte[] buffer = new byte[1024];
						int len = -2;
/*						
						while(len != -1){
							len = is.read(buffer);
							os.write(buffer, 0, len);
							............������............
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
							/*............������............*/
							mInfo.state = STATE_DOWNLOADING;
							mInfo.curProgress += len;
							notifyObservers(mInfo);						
						}
						
						if (isPause) {							
							/*.........��ͣ����...............*/
							mInfo.state = STATE_PAUSEDOWNLOAD;
							notifyObservers(mInfo);
							
						}else {
							/*............�������............*/
							mInfo.state = STATE_DOWNLOADED;
							notifyObservers(mInfo);
						}
								
					} finally{
						com.itcast.googlepay.utils.IOUtils.close(os);
						com.itcast.googlepay.utils.IOUtils.close(is);
					}
					
				}else {
					/*............����ʧ��............*/
					mInfo.state = STATE_DOWNLOADFAILED;
					notifyObservers(mInfo);
					
				}
			} catch (Exception e) {
				
				/*............����ʧ��............*/
				System.out.println("��������"+e);
				mInfo.state = STATE_DOWNLOADFAILED;
				notifyObservers(mInfo);
				
			}
		}	
	}
	
	//��ͣ����
	public void pause(DownLoadInfo info) {
		
		/*............��ͣ����............*/
		info.state = STATE_PAUSEDOWNLOAD;
		notifyObservers(info);	
	}
	
	
	//ȡ������
	public void cancel(DownLoadInfo info) {
		Runnable task = info.task;
		//�ҵ��̳߳� �Ƴ�����
		ThreadPoolFactory.getDownLoadPool().removeTask(task);

		/*............�Ƴ�����   ״̬<=>δ����............*/  
		info.state = STATE_PAUSEDOWNLOAD;
		notifyObservers(info);	
		
	}
	
	//��¶app��ǰ״̬  �����Ҫ���µ�״̬ʱ����
	public DownLoadInfo getDownLoadInfo(AppInfoBean data) {
				
		//�Ѱ�װ״̬
		if (CommonUtils.isInstalled(UIUtils.getContext(), data.packageName)) {
			DownLoadInfo info = generateDownLoadInfo(data);
			info.state = STATE_INSTALLED;		
			return info;
		}
		
		//�������
		DownLoadInfo info = generateDownLoadInfo(data);
		File saveApk = new File(info.savePath);
		if (saveApk.exists()) {		
			if (saveApk.length() == data.size) {
				info.state = STATE_DOWNLOADED;			
				return info;
			}
		}
		
		//������  ֹͣ����  �ȴ�����  ��������   -->run()����
		DownLoadInfo downLoadInfo = mDownLoadInfoMaps.get(data.packageName);
		if (downLoadInfo != null) {
			return downLoadInfo;
		}
		
		//δ����
		DownLoadInfo tempInfo = generateDownLoadInfo(data);
		tempInfo.state = STATE_UNDOWNLOAD;		
		return tempInfo;
	}


	//����AppInfoBean����һ��DownLoadinfo,�����г������Եĸ�ֵ   state�ǳ���
	private DownLoadInfo generateDownLoadInfo(AppInfoBean data) {
		
		String dir = FileUtils.getDir("download"); 
		File file = new File(dir, data.packageName+".apk"); //���ݰ�����ȡ����·��
		String savePath = file.getAbsolutePath(); //sdcard/android/data/����/download�ļ���/com.baiduinput.xxx.apk
		
		DownLoadInfo info = new DownLoadInfo();	
		info.savePath = savePath;
		info.downloadUrl = data.downloadUrl;
		info.packageName = data.packageName;
		info.max = data.size;
		info.curProgress = 0;
		return info;
	}
	
	
	//�Զ���۲������ģʽ
	List<DownLoadObserver> downLoadObservers = new LinkedList<DownLoadObserver>();
	public interface DownLoadObserver{
		void onDownLoadInfoChange(DownLoadInfo info);
	}
	
	
	//��ӹ۲���
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
	
	//ɾ���۲���
	public synchronized void deleteObserver(DownLoadObserver observer) {
		downLoadObservers.remove(observer);
	}
	
	//֪ͨ�۲������ݸı�
	public void notifyObservers(DownLoadInfo info) {
		for (DownLoadObserver observer : downLoadObservers) {
			observer.onDownLoadInfoChange(info);
		}
	}


	

}
