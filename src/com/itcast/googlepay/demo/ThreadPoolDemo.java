package com.itcast.googlepay.demo;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

public class ThreadPoolDemo {
	public void demo1() {
		ScheduledExecutorService service = Executors.newScheduledThreadPool(2);
		ExecutorService service1 = Executors.newFixedThreadPool(2);
		ExecutorService service2 = Executors.newCachedThreadPool();
		ScheduledExecutorService service3 = Executors.newScheduledThreadPool(2);
		service.execute(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				
			}
		});
		
	}
}
