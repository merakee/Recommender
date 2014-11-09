package net.freelogue.recommender;

import java.util.Date;

public class CommonUtil {

	public static void listAllThreads(){
		System.out.println("Total number of active threads: " + Thread.activeCount());
//		for (Thread thread: Thread.getAllStackTraces().keySet()){
//			System.out.format("Thread id: %d, name:%s \n", thread.getId(), thread.getName());
//		}
	}
	
	public static void printThreadCount(){
		System.out.println("Total number of active threads: " + Thread.activeCount());
	}
	
	public static void printTimeStamp(){
		System.out.format("ApiManger task: Current time: %tr%n", new Date());
	}
	public static void printThreadCountWithTimeStamp(){
		System.out.format("Total number of active threads:%d [%tr]%n", Thread.activeCount(),new Date());
	}
	
	public static void printTextWithTimeStamp(String text){
		System.out.format("%s [%tr]%n", text,new Date());
	}

	public static String stringTrimLast(String string, int count){
		if(string == null){
			return null; 
		}
		return string.substring(0,Math.max(string.length()-count,0));
	}
	public static String stringTrimLast(String string){
		return stringTrimLast(string,1);
	}
	
}
