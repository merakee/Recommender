package net.freelogue.recommender;

import java.util.ArrayList;
import java.util.logging.Logger;
import java.util.logging.Level;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/*UserRatingsManager is the preference manager. It converts a user's response to a rating -- a float value representing her likelihood of
 * liking a particular content. The rating values are written to the user rating table in the database. */

public class UserRatingsManager {
	/*class variables*/
	//private static final int UPDATE_PERIOD_MILLISEC = 86400000; /*1 day*/
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
		String sqlInsertCmd = "INSERT INTO " + AppConstants.RATINGS_TABLE_NAME + " (user_id, content_id, rating) VALUES(?, ?, ?)";
		try {
			sqlStatement = dbConnection.prepareStatement(sqlInsertCmd);
			System.out.println(sqlStatement);
			for (int i = 0; i < records.size(); i++) {
				int[] row = records.get(i);
				int userId = row[0];
				int contentId = row[1];
				int contentAuthorUserId = getAuthorForContent(contentId);
				ArrayList<Integer> commentIds = getUserCommentIdsForContent(userId, contentId);
				int numCommentLikes = getUserNumCommentLikes(userId, contentId, commentIds);
				double rating = convertResponseToRatingMultiCriterion(row[2], (contentId == contentAuthorUserId), commentIds.size(), numCommentLikes);
				if (!recordAlreadyExists(userId, contentId, rating)) {
					sqlStatement.setInt(1, userId);
					sqlStatement.setInt(2, contentId);
					sqlStatement.setDouble(3, rating);
					sqlStatement.execute(); //use execute() instead of executeUpdate(), if exception raised
				} else {
					LOGGER.log(Level.INFO, "Record already exists in user_ratings table.");
				}
			}
			sqlStatement.close();
		} catch (SQLException sqle) {
			LOGGER.log(Level.WARNING, sqle.getMessage(), sqle);
		}
	}
	
	/*Checks if record already exists in User Ratings Table.*/
	boolean recordAlreadyExists(int userId, int contentId, double rating) {
		String sqlSelectCmd = "SELECT user_id,content_id,rating FROM " + AppConstants.RATINGS_TABLE_NAME + " WHERE user_id=? AND content_id=? AND rating=?";
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
	
	/*Returns userId for the author of the passed-in contentId.*/
	int getAuthorForContent(int contentId) {
		int userId = 0;
		String sqlSelectCmd = "SELECT user_id FROM " + AppConstants.CONTENTS_TABLE_NAME + " WHERE id=?";
		try {
			sqlStatement = dbConnection.prepareStatement(sqlSelectCmd);
			sqlStatement.setInt(1, contentId);
			ResultSet resultSet = sqlStatement.executeQuery();
			if (resultSet.next()) {
				userId = resultSet.getInt("user_id");
			}
		} catch (SQLException sqle) {
			LOGGER.log(Level.WARNING, sqle.getMessage(), sqle);
		}
		return userId;
	}
	
	/*Returns an arraylist of comment ids, of the comments made by the user and for the content, referenced in parameters.*/
	ArrayList<Integer> getUserCommentIdsForContent(int userId, int contentId) {
		String sqlSelectCmd = "SELECT id FROM " + AppConstants.COMMENTS_TABLE_NAME + " WHERE user_id=? AND content_id=?";
		ArrayList<Integer> commentIds = new ArrayList<Integer>();
		try {
			sqlStatement = dbConnection.prepareStatement(sqlSelectCmd);
			sqlStatement.setInt(1, userId);
			sqlStatement.setInt(2, contentId);
			ResultSet resultSet = sqlStatement.executeQuery();
			while (resultSet.next()) {
				commentIds.add(resultSet.getInt("id"));
				System.out.println("id " + commentIds.get(commentIds.size()-1));
			}
		} catch (SQLException sqle) {
			LOGGER.log(Level.WARNING, sqle.getMessage(), sqle);
		}
		return commentIds;
	}
	
	/*Returns number of comments for content contentId liked by user userId.*/
	int getUserNumCommentLikes(int userId, int contentId, ArrayList<Integer> commentIds) {
		int numLikes = 0;
		String sqlSelectCmd = "SELECT response FROM " + AppConstants.COMMENT_RESPONSES_TABLE_NAME + " WHERE comment_id=? AND user_id=?";
		for (int i = 0; i < commentIds.size(); i++) {
			try {
				sqlStatement = dbConnection.prepareStatement(sqlSelectCmd);
				sqlStatement.setInt(1, commentIds.get(i));
				sqlStatement.setInt(2, userId);
				ResultSet resultSet = sqlStatement.executeQuery();
				if (resultSet.next()) {
					boolean liked = resultSet.getBoolean("response");
					System.out.println("liked: " + liked);
					if (liked) {
						numLikes++;
					}
				}
			} catch (SQLException sqle) {
				LOGGER.log(Level.WARNING, sqle.getMessage(), sqle);
			}
		}
		return numLikes;
	}
	
	/*class methods*/
	
	/* Computes the rating for a particular user given a particular content. */
	static double convertResponseToRatingMultiCriterion(int response, boolean isAuthorBool, int numComments, int numCommentLikes) { 
		int isAuthor = (isAuthorBool) ? 1 : 0;
		if (response == 1) {
			response = 1;
		} else {
			response = -1;
		}
		double rating = isAuthor*AppConstants.WEIGHT_AUTHORSHIP + response*(AppConstants.WEIGHT_SPREAD + AppConstants.WEIGHT_COMMENT*numComments + AppConstants.WEIGHT_COMMENT_LIKE*numCommentLikes);
		return rating;
	}
	
	/*static double convertResponseToRating(boolean response) {
		return response ? AppConstants.INPUT_SPREAD_VAL : AppConstants.INPUT_KILL_VAL;
	}*/
	
}
