package net.freelogue.recommender;

import java.util.logging.Logger;
import java.util.logging.Level;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.io.FileWriter;

public class DataManager implements Runnable {
	// class variables
	private static final Logger LOGGER = Logger.getLogger(Main.class.getName());
	private static Connection dbConnection = null;
	private static Statement sqlStatement = null;
	private static ResultSet dbResultsSet = null;
	private static FileWriter fileWriter;

	public static long maxUserResponseId = 0;
	public static long lastActiveResponseCount = 0;
	
	static void getDataFromDatabase() {
		if (RecommendationManager.isRecommenderUpdateActive) {
			LOGGER.log(Level.INFO,
					"Skipped fetching data from database since recommender computation is active");
		} else {
			connectToDbAndWriteDataToFile();
		}

		LOGGER.log(Level.INFO, CommonUtil.getThreadCountString());
	}

	static void connectToDbAndWriteDataToFile() {
		try {
			connectToDb();
			if ( !isUpdateNeededCountBased()) {
				closeDbConnection();
				LOGGER.log(Level.INFO,
						"Skipped updating data due to lack of enough new responses");
				return;
			}
			//int limit = CommonUtil.getRandom(50000)+7400;
			String sql_command = "SELECT user_id,content_id,response FROM user_responses WHERE content_id IN (SELECT id FROM contents WHERE spread_count >"
					+ AppConstants.CONTENT_MIN_SPREAD_COUNT + ")";
			// String sql_command = "SELECT count(*) FROM user_responses ";

			executeStatement(sql_command);
			// open file to write
			fileWriter = DataFileManager.openWriter(AppConstants.CSV_DATA_FILE);
			while (dbResultsSet.next()) {
				// white to csv file
				DataFileManager.writeLine(
						fileWriter,
						dbResultsSet.getInt("user_id")
								+ ","
								+ dbResultsSet.getInt("content_id")
								+ ","
								+ convertResponseToRating(dbResultsSet
										.getBoolean("response")) + "\n");
			}
			// need to close the file before recommender update to avoid file access conflict
			DataFileManager.closeWriter(fileWriter);
			closeDbConnection();
			LOGGER.log(Level.INFO, "Fetch data from Database and write to file...completed.");
			// update model
			RecommendationManager.updateRecommender();
		} catch (SQLException sqle) {
			LOGGER.log(Level.WARNING, sqle.getMessage(), sqle);
		} finally {
			// need to close in case of exception - in addition to closing it in the try block 
			DataFileManager.closeWriter(fileWriter);
		}
	}

	static double convertResponseToRating(boolean response) {
		return response ? AppConstants.INPUT_SPREAD_VAL
				: AppConstants.INPUT_KILL_VAL;
	}

	static boolean isUpdateNeededCountBased() throws SQLException {
		//String sql_command = "SELECT max(id) FROM user_responses";
		String sql_command = "SELECT count(*) FROM user_responses WHERE content_id IN (SELECT id FROM contents WHERE spread_count >"
		+ AppConstants.CONTENT_MIN_SPREAD_COUNT + ")";
		executeStatement(sql_command);
		if (dbResultsSet.next()) {
			int totalCount = dbResultsSet.getInt(1);
			if (totalCount - lastActiveResponseCount > AppConstants.RESPONSE_MIN_COUNT_FOR_UPDATE) {
				lastActiveResponseCount = totalCount;
				return true;
			}
			return false;
		} else {
			return false;
		}
	}
	static boolean isUpdateNeeded() throws SQLException {
		String sql_command = "SELECT max(id) FROM user_responses";
		executeStatement(sql_command);
		if (dbResultsSet.next()) {
			int maxId = dbResultsSet.getInt(1);
			if (maxId - maxUserResponseId > AppConstants.RESPONSE_MIN_COUNT_FOR_UPDATE) {
				maxUserResponseId = maxId;
				return true;
			}
			return false;
		} else {
			return false;
		}
	}
	
	static void connectToDb() throws SQLException {
		if (dbConnection == null) {
			dbConnection = DriverManager.getConnection(getDbUrl(),
					getDbUserName(), getDbUserPass());
		}
		if (sqlStatement == null) {
			sqlStatement = dbConnection.createStatement();
		}
	}

	static void executeStatement(String sql_command) throws SQLException {
		dbResultsSet = sqlStatement.executeQuery(sql_command);
	}

	static void closeDbConnection() throws SQLException {
		if (dbResultsSet != null) {
			dbResultsSet.close();
			dbResultsSet = null;
		}
		if (sqlStatement != null) {
			sqlStatement.close();
			sqlStatement = null;
		}
		if (dbConnection != null) {
			dbConnection.close();
			dbConnection = null;
		}
	}

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

	// 
	/**
	 * The run method for Runnable. Calls getDataFromDatabase. 
	 */
	public void run() {
		//Thread.currentThread().setPriority(AppConstants.DATA_MANAGER_THREAD_PRIORITY);
		//LOGGER.log(Level.INFO,"Thread prority: " + Thread.currentThread().getPriority());
		getDataFromDatabase();
	}

}
