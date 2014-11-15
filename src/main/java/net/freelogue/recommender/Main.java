package net.freelogue.recommender;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;
import java.io.File;

public class Main {
	/**
	 * The Entry point for the code
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		RecommendationManager.setupAndRun();
	}

	/*
	 * Logger set up
	 */
	private static final LogManager logManager = LogManager.getLogManager();
	private static final Logger LOGGER = Logger.getLogger(Main.class.getName());

	static {
		try {
			// create log dir if it does not exist
			File directory = new File("./log");
			if (!directory.exists()) {
				directory.mkdirs();
			}
			FileInputStream fileInputStream = new FileInputStream(
					"./logging.properties");
			logManager.readConfiguration(fileInputStream);
			fileInputStream.close();
		} catch (IOException exception) {
			LOGGER.log(Level.SEVERE, "Error in loading logging configuration",
					exception);
		}
	}
}
