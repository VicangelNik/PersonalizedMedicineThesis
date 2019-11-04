package test.weka.api;

import java.io.File;
import java.io.IOException;

import org.junit.Assert;
import org.junit.Test;

import helpful_classes.Constants;
import helpful_classes.EnumSeparators;
import weka.api.library.LoadCsv;
import weka.api.library.WekaFileConverterImpl;

// TODO: Auto-generated Javadoc
/**
 * The Class TestConverterTestCase.
 */
public class TestConverterTestCase {
	
	/**
	 * Test convert csv to arff test case.
	 */
	@Test
	public void testConvertCsvToArffTestCase() {
		File file = new File(Constants.SRC_MAIN_RESOURCES_PATH + "PatientAndC�ntrolProcessed.csv");
		File arffFile = new File(Constants.SRC_MAIN_RESOURCES_PATH + "PatientAndC�ntrolProcessed.arff");
		WekaFileConverterImpl wekaFileConverterImpl = new WekaFileConverterImpl();
		try {
			LoadCsv loader = new LoadCsv(file, 73664, EnumSeparators.TAB.getSeparator());
			Assert.assertEquals("The excpected class should be: ", 73664, loader.getStructure().classIndex());
			wekaFileConverterImpl.convertCsvToArff(loader.getDataSet(), arffFile.getAbsolutePath());
			// ASSERTS
			Assert.assertTrue("The file should exists", arffFile.exists());
			Assert.assertTrue("The file should be readable.", arffFile.canRead());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
