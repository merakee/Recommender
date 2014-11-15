package net.freelogue.recommender;

import static org.junit.Assert.*;

import org.junit.Test;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class RecommenderTest {
	@Test
	public void testDataFileEmptyMethodForEmptyFile() {
		Recommender recom = Recommender.getInstance();
		File file = new File("./nonexistenc");
		assertTrue(recom.isDataFileEmpty(file));
		try {
			file.createNewFile();
		} catch (IOException io) {

		}
		assertTrue(recom.isDataFileEmpty(file));
		file.delete();
	}
	
	@Test
	public void testDataFileEmptyMethodForNonEmptyFile() {
		Recommender recom = Recommender.getInstance();
		try {
			FileWriter filew = new FileWriter("./temptestfile.txt");
			filew.write("a");
			filew.close();
		} catch (IOException io) {
		}

		File file = new File("./temptestfile.txt");
		assertTrue(recom.isDataFileEmpty(file));
		
		try {
			FileWriter filew = new FileWriter("./temptestfile.txt");
			filew.write("enough contents for non empty file");
			filew.close();
		} catch (IOException io) {
		}
		
		assertFalse(recom.isDataFileEmpty(file));
		
		file.delete();
		
	}

}
