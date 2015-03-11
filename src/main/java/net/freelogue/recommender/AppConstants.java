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

	/*Tables available in backend database*/
	//user responses table
	public static final String RESPONSES_TABLE_NAME = "user_responses";
	public static final String RESPONSES_USERID_COL = "user_id";
	public static final String RESPONSES_ITEMID_COL = "content_id";
	public static final String RESPONSES_PREFERENCE_COL = "response";
	
	//contents table
	public static final String CONTENTS_TABLE_NAME = "contents";
	public static final String CONTENTS_CONTENTID_COL = "id";
	public static final String CONTENTS_USERID_COL = "user_id";
	
	//comments table
	public static final String COMMENTS_TABLE_NAME = "comments";
	public static final String COMMENTS_USER_ID = "user_id";
	public static final String COMMENTS_CONTENT_ID = "content_id";
	
	//comment responses table
	public static final String COMMENT_RESPONSES_TABLE_NAME = "comment_responses";
	public static final String COMMENT_RESPONSES_USER_ID = "user_id";
	public static final String COMMENT_RESPONSES_COMMENT_ID = "comment_id";
	public static final String COMMENT_RESPONSES_RESP = "response";
	
	// user ratings table
	public static final String RATINGS_TABLE_NAME = "user_ratings";
	public static final String RATINGS_USERID_COL = "user_id";
	public static final String RATINGS_ITEMID_COL = "content_id";
	public static final String RATINGS_PREFERENCE_COL = "rating";
	
	/*Weights for user preference model equation*/
	public static final float WEIGHT_AUTHORSHIP = 50.0f;
	public static final float WEIGHT_SPREAD = 10.0f;
	public static final float WEIGHT_KILL = 10.0f;
	public static final float WEIGHT_COMMENT = 3.0f;
	public static final float WEIGHT_COMMENT_LIKE = 1.0f;
	
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