package net.freelogue.recommender;

import java.io.File;
import java.util.List;
import java.util.Collections;
import java.util.logging.Logger;
import java.util.logging.Level;
import java.io.IOException;
import java.io.FileNotFoundException;

import org.apache.mahout.cf.taste.impl.model.file.FileDataModel;
import org.apache.mahout.cf.taste.impl.recommender.svd.SVDRecommender;
import org.apache.mahout.cf.taste.impl.recommender.svd.SVDPlusPlusFactorizer;
import org.apache.mahout.cf.taste.recommender.RecommendedItem;
import org.apache.mahout.cf.taste.common.TasteException;

public class Recommender {
	// class variable
	private static final Logger LOGGER = Logger.getLogger(Main.class.getName());
	private static Recommender recommender;

	// instance variables
	private SVDRecommender svdRecommender;

	// constructors
	// singleton implementation
	private Recommender() {
	}

	// class methods
	/**
	 * 
	 * @return the singleton instance
	 */
	public static Recommender getInstance() {
		if (recommender == null) {
			recommender = new Recommender();
		}
		return recommender;
	}

	// instance methods
	void refreshModel() {
		// LOGGER.log(Level.INFO,"Thread prority: " +
		// Thread.currentThread().getPriority());

		File dataFile = new File(AppConstants.CSV_DATA_FILE);
		if (isDataFileEmpty(dataFile)) {
			LOGGER.log(Level.INFO,
					"Data file is empty. Skipping refresh and setting recommender to null");
			swapSVDRecommender(null);
			return;
		}
		try {
			FileDataModel dataModel = getFileDataModel(dataFile);
			SVDPlusPlusFactorizer svdFactorizer = getSvdFactorizer(dataModel);

			// FilePersistenceStrategy persistenceStrategy =
			// getPersistenceStrategy(SVD_DATA_FILE);

			// recommender
			// svdRecommender = new SVDRecommender(dataModel, svdFactorizer,
			// persistenceStrategy);
			SVDRecommender svdRecommender_temp = new SVDRecommender(dataModel,
					svdFactorizer);
			swapSVDRecommender(svdRecommender_temp);
			LOGGER.log(Level.INFO, getCurrentModelInfo());
		} catch (FileNotFoundException fnf) {
			LOGGER.log(Level.SEVERE, fnf.getMessage(), fnf);
		} catch (IOException ioe) {
			LOGGER.log(Level.SEVERE, ioe.getMessage(), ioe);
		} catch (TasteException te) {
			LOGGER.log(Level.SEVERE, te.getMessage(), te);
		}
	}

	private FileDataModel getFileDataModel(File dataFile) throws IOException,
			FileNotFoundException {
		return new FileDataModel(dataFile);
	}

	private SVDPlusPlusFactorizer getSvdFactorizer(FileDataModel dataModel)
			throws TasteException {
		// factorizer
		// SVDPlusPlusFactorizer(DataModel dataModel, int numFeatures,
		// double learningRate, double preventOverfitting, double
		// randomNoise, int numIterations, double learningRateDecay)
		// SVDPlusPlusFactorizer(DataModel dataModel, int numFeatures, int
		// numIterations)

		return new SVDPlusPlusFactorizer(dataModel,
				AppConstants.SVD_NUM_FEATURES, AppConstants.SVD_NUM_ITERATIONS);
	}

	// private FilePersistenceStrategy getPersistenceStrategy(String filePath)
	// throws IOException{
	// File svdDataFile = new File(filePath);
	// return new FilePersistenceStrategy(svdDataFile);
	// }
	boolean isDataFileEmpty(File dataFile) {
		return (!dataFile.exists() || (dataFile.length() < AppConstants.DATAFILE_MIN_BYTE_COUNT_FOR_NONEMPTY));
	}

	private synchronized void swapSVDRecommender(SVDRecommender svdRecommender_) {
		svdRecommender = svdRecommender_;
		//CommonUtil.sleepThreadForNSecondsWithTag(40, "swap"); // this is just for initial thread synchronized testing - must be removed 
	}

	private String getCurrentModelInfo() {
		FileDataModel dataModel = (FileDataModel) svdRecommender.getDataModel();
		String modelInfo = "Recommender update completed with Recommender settings: ";
		try {
			modelInfo += "Number of Users: " + dataModel.getNumUsers() + ", ";
			modelInfo += "Number of Contents: " + dataModel.getNumItems() + ", ";
			modelInfo += "Max Preference: " + dataModel.getMaxPreference()
					+ ", ";
			modelInfo += "Min Preference: " + dataModel.getMinPreference();
		} catch (TasteException te) {
			LOGGER.log(Level.SEVERE, te.getMessage(), te);
			modelInfo += "Could not get info";
		}

		return modelInfo;
	}

	synchronized List<RecommendedItem> getRecommendation(long userId, int count) {
		//CommonUtil.sleepThreadForNSecondsWithTag(60, "recom"); // this is just for initial thread synchronized testing - must be removed 
		if (svdRecommender == null) {
			return Collections.<RecommendedItem> emptyList();
		}
		try {
			return svdRecommender.recommend(userId, count);
		} catch (TasteException te) {
			LOGGER.log(Level.SEVERE, te.getMessage(), te);
			return Collections.<RecommendedItem> emptyList();
		}
	}
}
