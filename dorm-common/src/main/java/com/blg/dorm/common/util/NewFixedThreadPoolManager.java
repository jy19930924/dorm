package com.blg.dorm.common.util;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 线程池管理 固定线程池
 * 
 *
 */
public class NewFixedThreadPoolManager {
	private static int fetchThreadPoolSize = 5;
	private static NewFixedThreadPoolManager manager = null;
	private ExecutorService es = null;

	private NewFixedThreadPoolManager(int fetchThreadPoolSize) {
		es = Executors.newFixedThreadPool(fetchThreadPoolSize);
	}

	public static synchronized NewFixedThreadPoolManager getInstance() {
		if (manager == null) {
			manager = new NewFixedThreadPoolManager(fetchThreadPoolSize);
		}
		return manager;
	}

	public static synchronized NewFixedThreadPoolManager getInstance(int poolSize) {
		if (manager == null) {
			manager = new NewFixedThreadPoolManager(poolSize);
		}
		return manager;
	}

	public ExecutorService getExecutorService() {
		return es;
	}

}
