package net.freelogue.recommender;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.mahout.cf.taste.recommender.RecommendedItem;

public class RecommendationManager {

	// class variable
	private static final Logger LOGGER = Logger.getLogger(Main.class.getName());
	private static final Recommender recommender = Recommender.getInstance();
	public static boolean isRecommenderUpdateActive = false;
	
	// instance variables

	// constructors
	private RecommendationManager() {

	}

	// class methods
	static List<RecommendedItem> getRecommendationForUser(long userId) {
		return getRecommendationForUser(userId,
				AppConstants.DEFAULT_NUM_RECOMMENDATIONS);
	}

	static List<RecommendedItem> getRecommendationForUser(long userId, int count) {
		return recommender.getRecommendation(userId, count);
	}

	static void updateRecommender() {
		Thread refreshThread = new Thread(new Runnable() {
			public void run() {
				isRecommenderUpdateActive = true;
				recommender.refreshModel();
				isRecommenderUpdateActive = false;
			}
		});
		refreshThread.setPriority(AppConstants.RECOMMENDER_REFRESH_THREAD_PRIORITY);
		refreshThread.start();
	}

	// instance methods
	/**
	 * Sets up the recommender and all related tasks by calling TaskManager. <br> <br>
	 * Required ENV variables: the program will exit if any of these are not set. <br>
	 * ENV["DB_URL"]: the url of postgres database  with port. Example: xyz.abc.us-west-2.rds.amazonaws.com:5432 <br>
	 * ENV["DB_NAME"]: the name of postgres database  <br>
	 * ENV["DB_USER_NAME"]: the user name for database <br>
	 * ENV["DB_USER_PASS"]: the password for database <br>
	 * <br>
	 * The full database URL is internally converted to the following: jdbc:postgresql://DB_URL/DB_NAME<br>
	 * Example: jdbc:postgresql://xyz.abc.us-west-2.rds.amazonaws.com:5432/ebdb<br>
	 * <br> <br>
	 * Optional ENV variables:<br>
	 * ENV["RECOM_DEFAULT_PORT"]: the server socket port. Default 2014. <br>
	 */
	public static void setupAndRun() {
		//LOGGER.log(Level.INFO,"Thread prority: " + Thread.currentThread().getPriority());
		LOGGER.log(Level.INFO, "Set up and run all recommender tasks.");
		TaskManager.runAllTasks();
	}
}
