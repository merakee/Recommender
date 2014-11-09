package net.freelogue.recommender;

public class AppConstants {

	// Recommendation Engine parameters
	
	public static final int SVD_NUM_FEATURES   = 50;
	public static final int SVD_NUM_ITERATIONS = 10;
	
	public static final int CONTENT_MIN_SPREAD_COUNT = 10;
    public static final float INPUT_SPREAD_VAL = 5.0f;
    public static final float INPUT_KILL_VAL = 1.0f;
    
	public static final int DEFAULT_NUM_RECOMMENDATIONS = 20;
	
    // Timing parameters
    public static final long DATABASE_POLLING_INTERVAL = 30; // in seconds 
    public static final int API_SOCKET_TIMEOUT_INTERVAL = 60; // in seconds 
    
	
//    public static final double DOUBLE_VAL = 3.14159d;
//    public static final float FLOAT_VAL = 3.14f;
//    public static final int INT_VAL = 3;
//    public static final boolean BOOL_VAL = true;
//    public static final String STRING_VAL = "This is a string constant";   
    
//    private AppConstants() {
//        // restrict instantiation
//    	
//    }
}

/*
Usage:

import static AppConstants.STRING_VAL;

public class ClassName {
 
	public String getStringConstant() {
		return STRING_VAl;
	}
}

*/