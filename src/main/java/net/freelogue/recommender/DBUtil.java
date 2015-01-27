package net.freelogue.recommender;

import java.util.logging.Level;
import java.util.logging.Logger;

public class DBUtil {
	private static final Logger LOGGER = Logger.getLogger(Main.class.getName());
	
	private DBUtil() {
	}
	

	/* These functions read the DB credentials from the os env.*/
	
	/**
	 * Returns DB server name.
	 */
	static String getDBServerName() {
		if (System.getenv("DB_URL") != null) {
			return System.getenv("DB_URL");
		} else {
			LOGGER.log(Level.SEVERE, "DB_URL Env is not set. Exit program.");
			System.exit(1);
			return "";
		}
	}
	
	/**
	 * Returns name of database.
	 */
	static String getDbName() {
		if (System.getenv("DB_NAME") != null) {
			return System.getenv("DB_NAME");
		} else {
			LOGGER.log(Level.SEVERE, "DB_NAME Env is not set. Exit program.");
			System.exit(1);
			return "";
		}
	}
	
	/**
	 * Returns complete formatted DB URL, comprised of server and db name.
	 */
	static String getDbUrl() {
		if ((System.getenv("DB_URL") != null)
				&& (System.getenv("DB_NAME") != null)) {
			return "jdbc:postgresql://" + System.getenv("DB_URL") + "/"
					+ System.getenv("DB_NAME");
		} else {
			LOGGER.log(Level.SEVERE, "DB_URL Env is not set. Exit program.");
			System.exit(1);
			return "";
		}
	}

	/**
	 * Returns DB username.
	 */
	static String getDbUserName() {
		if (System.getenv("DB_USER_NAME") != null) {
			return System.getenv("DB_USER_NAME");
		} else {
			LOGGER.log(Level.SEVERE,
					"DB_USER_NAME Env is not set. Exit program.");
			System.exit(1);
			return "";
		}
	}

	/**
	 * Returns DB password.
	 */
	static String getDbUserPass() {
		if (System.getenv("DB_USER_PASS") != null) {
			return System.getenv("DB_USER_PASS");
		} else {
			LOGGER.log(Level.SEVERE,
					"DB_USER_PASS Env is not set. Exit program.");
			System.exit(1);
			return "";
		}
	}
	

}
