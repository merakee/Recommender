package net.freelogue.recommender;

import static org.junit.Assert.*;

import org.junit.Test;

import java.io.FileWriter;

public class DataFileManagerTest {
@Test
public void testOpenAndCloseWriter(){
	FileWriter fw = DataFileManager.openWriter(AppConstants.CSV_DATA_FILE);
    assertNotNull(fw);
	assertTrue(DataFileManager.closeWriter(fw));
}

public void testOpenWriteLineAndCloseWriter(){
	FileWriter fw = DataFileManager.openWriter(AppConstants.CSV_DATA_FILE);
    assertNotNull(fw);
    assertTrue(DataFileManager.writeLine(fw,"This is a test line 1\n"));
    assertTrue(DataFileManager.writeLine(fw,"This is a test line 2\n"));
    assertTrue(DataFileManager.writeLine(fw,"This is a test line 3\n"));
	assertTrue(DataFileManager.closeWriter(fw));
}

}
