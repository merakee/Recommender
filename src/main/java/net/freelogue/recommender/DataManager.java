package net.freelogue.recommender;

import java.util.Date;

public class DataManager implements Runnable{
	public static boolean isActive = true; 
	
//	static void setActive(boolean _isActive){
//		isActive = _isActive; 
//	}
	static void getDataFromDatabase(){
		if (isActive){
		System.out.format("DataManger task active : Current time: %tr%n", new Date());
		}
		else{
			System.out.format("DataManger task inactive: Current time: %tr%n", new Date());
		}
		CommonUtil.listAllThreads();
	}
	
	// the run method for Runnable
	public void run(){
		DataManager.getDataFromDatabase();
	}

}
