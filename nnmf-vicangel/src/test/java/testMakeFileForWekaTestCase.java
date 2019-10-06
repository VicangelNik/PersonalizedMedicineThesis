
/*
 *
 */
import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import helpful_classes.Constants;
import helpful_classes.MultiKey;
import utilpackage.CsvUtils;
import utilpackage.WekaUtils;
import validation.WekaValidation;

public class testMakeFileForWekaTestCase {

	@Ignore
	@Test
	public void testReadCSVAndGetDimensionsTestCase() {
		try {
			List<List<String>> allDataList = CsvUtils.readCSVFile(CsvUtils.C_WORK_CSV_FILE);
			// get featues
			List<String> dimensions = WekaUtils.getDimensions(allDataList);
			Assert.assertEquals("All the features should be 73663", 73663, dimensions.size());
		} catch (IOException e) {
			Assert.fail(e.getMessage());
		}
	}

	@Ignore
	@Test
	public void testDataAndFeaturesHaveTheSameNumberTestCase() {
		try {
			// get all data
			List<List<String>> allDataList = CsvUtils.readCSVFile(CsvUtils.C_WORK_CSV_FILE);
			// get dimension number through getDimensions
			int dimensionsSize = WekaUtils.getDimensions(allDataList).size();
			// get all data column - wised.
			List<List<String>> dimensionWIseData = CsvUtils.readCSVFileColumnWise(CsvUtils.C_WORK_CSV_FILE,
					dimensionsSize);
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
			List<List<String>> allDataList = CsvUtils.readCSVFile(CsvUtils.C_WORK_CSV_FILE);
			// get dimension number through getDimensions
			int dimensionsSize = WekaUtils.getDimensions(allDataList).size();
			// get all data column - wised
			List<List<String>> dimensionWIseData = CsvUtils.readCSVFileColumnWise(CsvUtils.C_WORK_CSV_FILE,
					dimensionsSize);
			Map<MultiKey, List<String>> attributes = WekaUtils.filterValidFeaturesAndData(dimensionWIseData);
			// assert
			Assert.assertEquals("The size of map should be", 69990, attributes.size());
		} catch (IOException e) {
			Assert.fail(e.getMessage());
		}
	}

	@Test
	public void testValidateWekaFileCase() {
		try {
			final String wekaFile = Constants.SRC_TEST_RESOURCES_PATH + "wekaFileFromChristinasWork"
					+ WekaUtils.WEKA_SUFFIX;
			// get all data
			List<List<String>> allDataList = CsvUtils.readCSVFile(CsvUtils.C_WORK_CSV_FILE);
			// get dimension number through getDimensions
			int dimensionsSize = WekaUtils.getDimensions(allDataList).size();
			// get all data column - wised
			List<List<String>> dimensionWIseData = CsvUtils.readCSVFileColumnWise(CsvUtils.C_WORK_CSV_FILE,
					dimensionsSize);
			Map<MultiKey, List<String>> attributes = WekaUtils.filterValidFeaturesAndData(dimensionWIseData);
			// Prepare weka file.
			WekaUtils.createWekaFile("patient", attributes, dimensionWIseData, wekaFile);
			// ASSERTS
			Assert.assertEquals("The attributes must be 69990", 69990, attributes.size());

			WekaValidation.wekaArfFileValidation(wekaFile, 69990, " ");
		} catch (IOException e) {
			Assert.fail(e.getMessage());
		}
	}
}
