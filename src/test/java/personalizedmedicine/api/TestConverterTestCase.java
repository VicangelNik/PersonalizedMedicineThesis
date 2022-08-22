package java.personalizedmedicine.api;

import java.io.File;
import java.io.IOException;

import org.junit.Assert;
import org.junit.Test;

import java.personalizedmedicine.helpful_classes.Constants;
import java.personalizedmedicine.helpful_classes.EnumSeparators;
import java.personalizedmedicine.utilpackage.WekaUtils;
import java.personalizedmedicine.weka.api.library.LoadCsv;
import java.personalizedmedicine.weka.api.library.WekaFileConverter;
import weka.core.Instances;
import weka.core.converters.CSVLoader;

/**
 * The Class TestConverterTestCase.
 */
public class TestConverterTestCase {

	/**
	 * Test convert csv to arff test case.
	 */
	@Test
	public void testConvertCsvToArffTestCase() {
		File file = new File(Constants.SRC_MAIN_RESOURCES_PATH + "PatientAndControlProcessedLevelOne.csv");
		File arffFile = new File(Constants.SRC_MAIN_RESOURCES_PATH + "PatientAndControlProcessedLevelOne.arff");
		WekaFileConverter wekaFileConverterImpl = new WekaFileConverter();
		try {
			LoadCsv loader = new LoadCsv(file, 73664, EnumSeparators.TAB.getSeparator(), "NA");
			Assert.assertEquals("The excpected class should be: ", 73664, loader.getStructure().classIndex());
			wekaFileConverterImpl.arffSaver(loader.getDataSet(), arffFile.getAbsolutePath());
			// ASSERTS
			Assert.assertTrue("The file should exists", arffFile.exists());
			Assert.assertTrue("The file should be readable.", arffFile.canRead());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Saves the processed data set as csv
	 *
	 * @throws IOException
	 *
	 */
	@Test
	public void testConvertArffToCSVTestCase() throws IOException {
		File arffFile = new File(Constants.completeFileName);
		File csvFile = new File(Constants.SRC_MAIN_RESOURCES_PATH + "finalDataset.csv");
		Instances originalDataset = WekaUtils.getOriginalData(arffFile, Constants.classRealName);
		WekaFileConverter wekaFileConverterImpl = new WekaFileConverter();
		Assert.assertTrue("The excpected class should be in position: ", 72121 == originalDataset.classIndex());
		wekaFileConverterImpl.csvSaver(originalDataset, csvFile.getAbsolutePath());
		// Check CSV file
		Assert.assertTrue("The file should exists", csvFile.exists());
		Assert.assertTrue("The file should be readable.", csvFile.canRead());
		// load csv
		CSVLoader csvLoader = new CSVLoader();
		csvLoader.setFile(csvFile.getAbsoluteFile());
		Instances csvData = csvLoader.getDataSet();
		csvData.setClass(csvData.attribute(Constants.classRealName));
		// ASSERTS
		Assert.assertTrue("The number of instances should be the same",
				csvData.numInstances() == originalDataset.numInstances());
		Assert.assertTrue("The number of attributes should be the same",
				csvData.numAttributes() == originalDataset.numAttributes());
		Assert.assertTrue("The class index should be the same", csvData.classIndex() == originalDataset.classIndex());
	}
}
