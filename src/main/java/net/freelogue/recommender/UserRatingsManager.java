package net.freelogue.recommender;

import java.util.ArrayList;
import java.util.logging.Logger;
import java.util.logging.Level;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserRatingsManager {
	/*class variables*/
	private static final int UPDATE_PERIOD_MILLISEC = 86400000; /*1 day*/
	private static final Logger LOGGER = Logger.getLogger(Main.class.getName());
	
	/*instance variables*/
	private Connection dbConnection = null;
	private PreparedStatement sqlStatement = null;
	private ArrayList<int[]> records = new ArrayList<int[]>();
	
	/*Constructor*/
	public UserRatingsManager(Connection conn) {
		dbConnection = conn;
	}
	
	/*Instance methods*/
	
	void storeTableRecord(int userId, int contentId, boolean responseBool) {
		int response = (responseBool) ? 1 : 0;
		records.add(new int[]{userId, contentId, response});
	}
	
	/* Write each record in user_responses table to user_ratings table, if not already present. */
	void writeToRatingsTable() {
		String sqlInsertCmd = "INSERT INTO " + AppConstants.RATINGS_DB_TABLE_NAME + " (user_id, content_id, rating) VALUES(?, ?, ?)";
		try {
			sqlStatement = dbConnection.prepareStatement(sqlInsertCmd);
			System.out.println(sqlStatement);
			for (int i = 0; i < records.size(); i++) {
				int[] row = records.get(i);
				int userId = row[0];
				int contentId = row[1];
				double rating = convertResponseToRating(row[2]);
				if (!recordAlreadyExists(userId, contentId, rating)) {
					sqlStatement.setInt(1, userId);
					sqlStatement.setInt(2, contentId);
					sqlStatement.setDouble(3, rating);
					sqlStatement.executeUpdate();
				} else {
					LOGGER.log(Level.INFO, "Record already exists in user_ratings table.");
				}
			}
			sqlStatement.close();
		} catch (SQLException sqle) {
			LOGGER.log(Level.WARNING, sqle.getMessage(), sqle);
		}
	}
	
	/*void writeToRatingsTable(int userId, int contentId, boolean response) {
		String sqlInsertCmd = "INSERT INTO " + AppConstants.RATINGS_DB_TABLE_NAME + " (user_id, content_id, rating) VALUES(?, ?, ?)";
		System.out.println(sqlInsertCmd);
		try {
			if (!recordAlreadyExists(userId, contentId, response)) {
				sqlStatement.setInt(1,userId);
				sqlStatement.setInt(2,contentId);
				sqlStatement.setDouble(3,convertResponseToRating(response));
				sqlStatement = dbConnection.prepareStatement(sqlInsertCmd);
				sqlStatement.executeUpdate();
			}
		} catch (SQLException sqle) {
			LOGGER.log(Level.WARNING, sqle.getMessage(), sqle);
		}
	}*/
	
	
	boolean recordAlreadyExists(int userId, int contentId, double rating) {
		String sqlSelectCmd = "SELECT user_id,content_id,rating FROM " + AppConstants.RATINGS_DB_TABLE_NAME + " WHERE user_id=? AND content_id=? AND rating=?";
		try {
			sqlStatement = dbConnection.prepareStatement(sqlSelectCmd);
			sqlStatement.setInt(1, userId);
			sqlStatement.setInt(2, contentId);
			sqlStatement.setDouble(3, rating);
			ResultSet resultSet = sqlStatement.executeQuery();
			if (resultSet.next()) {
				return true;
			}
		} catch (SQLException sqle) {
			LOGGER.log(Level.WARNING, sqle.getMessage(), sqle);
		}
		return false;
	}
	
	/*class methods*/
	
	static double convertResponseToRating(int response) {
		if (response == 1) {
			return AppConstants.INPUT_SPREAD_VAL;
		}
		return AppConstants.INPUT_KILL_VAL;
	}
	
	/*static double convertResponseToRating(boolean response) {
		return response ? AppConstants.INPUT_SPREAD_VAL : AppConstants.INPUT_KILL_VAL;
	}*/
	
}
