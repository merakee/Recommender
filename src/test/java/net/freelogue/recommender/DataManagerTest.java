package net.freelogue.recommender;

//import static org.junit.Assert.*;
//import static org.hamcrest.CoreMatchers.*;

import java.sql.SQLException;

//import org.junit.Test;

public class DataManagerTest {
	public void testOpenAndCloseDb(){
		try{
	DataManager.connectToDb();
	DataManager.executeStatement("");
	DataManager.closeDbConnection();
		}
		catch(SQLException sqle){
			CommonUtil.printTextWithTimeStamp(sqle.getMessage());
		}
	}
}
