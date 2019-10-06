package test.weka.api;

import java.io.File;
import java.io.IOException;

import org.junit.Assert;
import org.junit.Test;

import helpful_classes.Constants;
import weka.api.library.LoadCsv;

public class TestLoadCsvTestCase {

	private static final File csvFile = new File(Constants.SRC_MAIN_RESOURCES_PATH + "dummyCsv.csv");

	@Test
	public void testLoadCsv() {
		try {
			LoadCsv loadCsv = new LoadCsv(csvFile);
			Assert.assertEquals(csvFile, loadCsv.getFile());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Assert.fail(e.getMessage());
		}
	}

	
}
