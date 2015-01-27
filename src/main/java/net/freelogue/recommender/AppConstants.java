package net.freelogue.recommender;

public class AppConstants {

	// prevent from creating instance
	private AppConstants() {
	}

	// Recommendation Engine parameters
	public static final int SVD_NUM_FEATURES = 50;
	public static final int SVD_NUM_ITERATIONS = 10;

	public static final int RESPONSE_MIN_COUNT_FOR_UPDATE = 100;
	public static final int CONTENT_MIN_SPREAD_COUNT = 10;
	public static final int DATAFILE_MIN_BYTE_COUNT_FOR_NONEMPTY = 5;
	public static final float INPUT_SPREAD_VAL = 5.0f;
	public static final float INPUT_KILL_VAL = 1.0f;

	public static final int DEFAULT_NUM_RECOMMENDATIONS = 20;

	public static final String SVD_DATA_FILE = "./svd_data.data";
	public static final String CSV_DATA_FILE = "./ratings_data.csv";

	public static final String BE_DB_TABLE_NAME = "user_responses";
	public static final String BE_DB_USERID_COL = "user_id";
	public static final String BE_DB_ITEMID_COL = "content_id";
	public static final String BE_DB_PREFERENCE_COL = "response";
	
	public static final String RATINGS_DB_TABLE_NAME = "user_ratings";
	public static final String RATINGS_DB_USERID_COL = "user_id";
	public static final String RATINGS_DB_ITEMID_COL = "content_id";
	public static final String RATINGS_DB_PREFERENCE_COL = "rating";
	
	// Timing parameters
	public static final long DATABASE_POLLING_INTERVAL = 300; // in seconds
	public static final int API_SOCKET_TIMEOUT_INTERVAL = 60; // in seconds // Currently not used:
	
	// Thread Parameters
	public static final int API_MANAGER_THREAD_PRIORITY = Thread.MAX_PRIORITY;
	public static final int DATA_MANAGER_THREAD_PRIORITY = Thread.NORM_PRIORITY; // Currently not used: set to NORM priority
	public static final int RECOMMENDER_REFRESH_THREAD_PRIORITY = Thread.NORM_PRIORITY;
	public static final int APP_DEFAULT_THREAD_PRIORITY = Thread.NORM_PRIORITY;

	// public static final double DOUBLE_VAL = 3.14159d;
	// public static final float FLOAT_VAL = 3.14f;
	// public static final int INT_VAL = 3;
	// public static final boolean BOOL_VAL = true;
	// public static final String STRING_VAL = "This is a string constant";

	// private AppConstants() {
	// // restrict instantiation
	//
	// }
}

/*
 * Usage:
 * 
 * import static AppConstants.STRING_VAL;
 * 
 * public class ClassName {
 * 
 * public String getStringConstant() { return STRING_VAl; } }
 */