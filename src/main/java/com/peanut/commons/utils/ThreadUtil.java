package com.peanut.commons.utils;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 线程管理类
 * @author ming.yang
 */
public class ThreadUtil {
	
	private static ExecutorService exec = Executors.newFixedThreadPool(200);
	
	static {
		Runtime.getRuntime().addShutdownHook(new Thread() {
			public void run() {
				try {
					exec.shutdown();
					System.out.println("executor service shutdown!");
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

		});
	}


	/**
	 * 提交任务到处理器
	 * 
	 * @param command
	 */
	public static final void execute(Runnable command) {
		exec.execute(command);
	}


}
