package net.freelogue.recommender;

import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CommonUtil {
	private static final Logger LOGGER = Logger.getLogger(Main.class.getName());
	
	// prevent from creating instance
	private CommonUtil() {
	}

//	/**
//	 * Prints total number of active threads. Need to modify to list all 
//	 * @return
//	 */
//	public static String  listAllThreads() {
//		return "Total number of active threads: "
//				+ Thread.activeCount();
//		// for (Thread thread: Thread.getAllStackTraces().keySet()){
//		// System.out.format("Thread id: %d, name:%s \n", thread.getId(),
//		// thread.getName());
//		// }
//	}

	/**
	 * Returns total number of active threads in a string with header.
	 */
	public static String getThreadCountString() {
		return "Total number of active threads: "+ Thread.activeCount();
	}
	
	/**
	 * Prints total number of active threads.
	 */
	public static void printThreadCount() {
		System.out.println("Total number of active threads: "
				+ Thread.activeCount());
	}

	/**
	 * Prints current time.
	 */
	public static void printTimeStamp() {
		System.out.format("[%tr]\n", new Date());
	}

	/**
	 * Prints total number of active threads with current time.
	 */
	public static void printThreadCountWithTimeStamp() {
		System.out.format("[%tr] Total number of active threads:%d \n",
				 new Date(),Thread.activeCount());
	}

	/**
	 * Prints string given in text with current time.
	 * @param text
	 */
	public static void printTextWithTimeStamp(String text) {
		System.out.format("[%tr] %s \n", new Date(), text);
	}

	/**
	 * Trims last <code>count </code> character(s) from a string.
	 * 
	 * @param string The input string
	 * @param count The number of characters to be trimmed 
	 * @return string with last <code>count </code> character(s) trimmed. For empty string or string containing less than <code>count </code> characters, returns
	 *         empty string. For null, returns null.
	 */
	public static String stringTrimLast(String string, int count) {
		if (string == null) {
			return null;
		}
		return string.substring(0, Math.max(string.length() - count, 0));
	}

	/**
	 * Trims last character from a string.
	 * 
	 * @param string
	 *            The input string
	 * @return string with last character trimmed. For empty string, returns
	 *         empty string. For null, returns null.
	 */
	public static String stringTrimLast(String string) {
		return stringTrimLast(string, 1);
	}
	
	/**
	 * Sleeps the calling thread for delayInSec secs and displays a line on console with tag for start and stop of the sleep 
	 * @param delayInSec number of sec the thread needs to sleep
	 * @param tag a string to identify the calling thread 
	 * Example:<br>
	 *  tag = "TestMethod"<br>
	 *  delayInSec = 10<br>
	 * ************************** TestMethod Thread Sleeping for 10 sec. Start ************************<br>
	 * after 10 sec<br>
	 * <br>
	 * ************************** TestMethod Thread Sleeping End ************************<br>
	 */
	public static void sleepThreadForNSecondsWithTag(int delayInSec, String tag){
		printTextWithTimeStamp("************************** " + tag + " Thread Sleeping for "+ delayInSec + " sec. Start " + "************************");
		sleepThreadForNSeconds(delayInSec);
		printTextWithTimeStamp("************************** " + tag + " Thread Sleeping End " + "************************");
	}
	
	/**
	 * Sleeps the calling thread for delayInSec secs 
	 * @param delayInSec number of sec the thread needs to sleep
	 */
	public static void sleepThreadForNSeconds(int delayInSec){
		try {
		    Thread.sleep(delayInSec * 1000);                 //1000 milliseconds is one second.
		} catch(InterruptedException ie) {
			LOGGER.log(Level.WARNING, ie.getMessage(), ie);
		}	
	}
	
	/**
	 * Returns a random int between minVal and maxVal
	 * @param maxVal the upper limit of possible values
	 * @param minVal the lower limit of possible value 
	 * @return an random int between minVal and maxVal, if maxVal >=minVal. Otherwise, returns 0.  
	 */
	public static int getRandom(int maxVal, int minVal){
		int range = maxVal-minVal;
		if(range <0){
			return 0; 
		}
		return (int) Math.round(Math.random()*range+minVal);
	}

	/**
	 * Returns a random int between 0 and maxVal
	 * @param maxVal the upper limit of possible values 
	 * @return an random int between 0 and maxVal, if maxVal >0. Otherwise, returns 0. 
	 */
	public static int getRandom(int maxVal){
		return getRandom(maxVal,0);
	}

}
