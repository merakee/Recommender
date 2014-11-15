package net.freelogue.recommender;

import java.util.logging.Logger;
import java.util.logging.Level;
import java.io.FileWriter;
import java.io.IOException;

public class DataFileManager {
	private static final Logger LOGGER = Logger.getLogger(Main.class.getName());

	private DataFileManager(){
		
	}
	
	static FileWriter openWriter(String filePath) {
		try {
			return new FileWriter(filePath);
		} catch (IOException ioe) {
			LOGGER.log(Level.SEVERE, ioe.getMessage(), ioe);
			LOGGER.log(Level.SEVERE,
					"Cannot open CSV file for writting. Exit program.");
			return null;
		}
	}

	static boolean writeLine(FileWriter fWriter, String line) {
		try {
			fWriter.write(line);
			return true;
		} catch (IOException ioe) {
			LOGGER.log(Level.SEVERE, ioe.getMessage(), ioe);
			return false;
		}
	}

	static boolean closeWriter(FileWriter fWriter) {
		if (fWriter != null) {
			try {
				fWriter.close();
				fWriter = null;
				return true;
			} catch (IOException ioe) {
				LOGGER.log(Level.SEVERE, ioe.getMessage(), ioe);
				return false;
			}
		}
		return true;
	}

}
