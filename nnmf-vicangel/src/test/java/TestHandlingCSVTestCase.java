import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import utilpackage.CsvUtils;
import utilpackage.Utils;
import utilpackage.WekaUtils;

public class TestHandlingCSVTestCase {

	private static final Logger LOGGER = Logger.getLogger(TestHandlingCSVTestCase.class.getName());

	private String christinaWorkCsvFile = "src\\main\\resources\\allTogether.csv";

	/**
	 * Test read csv file.
	 */
	@Ignore
	@Test
	public void testReadCSVTestCase() {
		try {
			List<List<String>> allDataList = CsvUtils.readCSVFile(christinaWorkCsvFile);
			Assert.assertEquals("The lines of the file should be 413", 413, allDataList.size());
		} catch (IOException e) {
			Assert.fail(e.getMessage());
		}
	}

	@Ignore
	@Test
	public void testReadCSVAndGetDimensionsTestCase() {
		try {
			List<List<String>> allDataList = CsvUtils.readCSVFile(christinaWorkCsvFile);
			// get featues
			List<String> dimensions = WekaUtils.getDimensions(allDataList);
			Assert.assertEquals("All the features should be 73663", 73663, dimensions.size());
		} catch (IOException e) {
			Assert.fail(e.getMessage());
		}
	}

	@Ignore
	@Test
	public void testReadCSVColumnWIseTestCase() {
		try {
			// get all data
			List<List<String>> allDataList = CsvUtils.readCSVFile(christinaWorkCsvFile);
			// get dimension number through getDimensions
			int dimensionsSize = WekaUtils.getDimensions(allDataList).size();
			// get all data column - wised.
			long startTime = System.nanoTime();
			List<List<String>> dimensionWIseData = CsvUtils.readCSVFileColumnWise(christinaWorkCsvFile, dimensionsSize);
			long endTime = System.nanoTime();
			LOGGER.log(Level.INFO, "readCSVFileColumnWise execution time in seconds is {0}",
					(endTime - startTime) / 1_000_000_000.0);
			// assert
			Assert.assertEquals(73663, dimensionWIseData.size());
		} catch (IOException e) {
			Assert.fail(e.getMessage());
		}
	}

	@Ignore
	@Test
	public void testDataAndFeaturesHaveTheSameNumberTestCase() {
		try {
			// get all data
			List<List<String>> allDataList = CsvUtils.readCSVFile(christinaWorkCsvFile);
			// get dimension number through getDimensions
			int dimensionsSize = WekaUtils.getDimensions(allDataList).size();
			// get all data column - wised.
			List<List<String>> dimensionWIseData = CsvUtils.readCSVFileColumnWise(christinaWorkCsvFile, dimensionsSize);
			WekaUtils.checkNumberFeatureData(dimensionWIseData);
			// assert
			Assert.assertTrue("The features' number should be the same with the data",
					WekaUtils.checkNumberFeatureData(dimensionWIseData));
		} catch (IOException e) {
			Assert.fail(e.getMessage());
		}
	}

	@Ignore
	@Test
	public void testFilterDataTestCase() {
		try {
			// get all data
			List<List<String>> allDataList = CsvUtils.readCSVFile(christinaWorkCsvFile);
			// get dimension number through getDimensions
			int dimensionsSize = WekaUtils.getDimensions(allDataList).size();
			// get all data column - wised
			List<List<String>> dimensionWIseData = CsvUtils.readCSVFileColumnWise(christinaWorkCsvFile, dimensionsSize);
			Map<String, List<String>> attributes = WekaUtils.filterValidFeaturesAndData(dimensionWIseData);
			// assert
			Assert.assertEquals("The size of map should be", 69990, attributes.size());
		} catch (IOException e) {
			Assert.fail(e.getMessage());
		}
	}

	@Test
	public void testValidateWekaFileCase() {
		try {
			// get all data
			List<List<String>> allDataList = CsvUtils.readCSVFile(christinaWorkCsvFile);
			// get dimension number through getDimensions
			int dimensionsSize = WekaUtils.getDimensions(allDataList).size();
			// get all data column - wised
			List<List<String>> dimensionWIseData = CsvUtils.readCSVFileColumnWise(christinaWorkCsvFile, dimensionsSize);
			Map<String, List<String>> attributes = WekaUtils.filterValidFeaturesAndData(dimensionWIseData);
			// Prepare weka file.
			WekaUtils.createWekaFile("patient", attributes, dimensionWIseData,
					Utils.SRC_TEST_RESOURCES_PATH + "wekaFileFromChristinasWork" + WekaUtils.WEKA_SUFFIX);
			// ASSERTS
			
			Assert.assertEquals("The attributes must be 69990", 69990, attributes.size());
		} catch (IOException e) {
			Assert.fail(e.getMessage());
		}
	}
}
