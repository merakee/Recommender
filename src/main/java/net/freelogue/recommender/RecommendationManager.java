package net.freelogue.recommender;

public class RecommendationManager {

	// class variable

	// instance variables

	// constructors
	public RecommendationManager() {

	}
 
	// class methods
	static double[][] getRecommendationForUser(int userId){
		double[][] recommendations={{101, 0.3},{203, 0.89}};
		return recommendations;
	}
	// instance methods
	void setupAndRun() {
		System.out.println("****Running recommendation manager ...");
		TaskManager.runAllTasks();
	}
}
