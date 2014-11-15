package net.freelogue.recommender;

import static java.util.concurrent.TimeUnit.SECONDS;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

//import java.util.concurrent.ScheduledFuture;

public class TaskManager {

	private TaskManager() {
	}
	
	static void runAllTasks() {
		TaskManager.runApiManagerTask();
		TaskManager.runDataManagerTask();
	}

	// scheduled periodic tasks
	private static final ScheduledExecutorService scheduler = Executors
			.newSingleThreadScheduledExecutor();

	static void runDataManagerTask() {
		// schedule and run data manager task
		scheduler.scheduleAtFixedRate(new DataManager(), 0,
				AppConstants.DATABASE_POLLING_INTERVAL, SECONDS);
		
		// Get a handle for finite time run
		// final ScheduledFuture<?> dataManagerTaskHandle =
		// scheduler.scheduleAtFixedRate(new DataManager(), 0,
		// AppConstants.DATABASE_POLLING_INTERVAL, SECONDS);
		// scheduler.schedule(new Runnable() {
		// public void run() {dataManagerTaskHandle.cancel(true); }
		// }, 100, SECONDS);
	}

	static void runApiManagerTask() {
		Thread apiManagerTaskthread = new Thread(new ApiManager());
		apiManagerTaskthread.setPriority(AppConstants.API_MANAGER_THREAD_PRIORITY);
		apiManagerTaskthread.start();
	}
}
