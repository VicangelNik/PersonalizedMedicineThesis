
/*
 *
 */
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.junit.Assert;
import org.junit.Test;

import helpful_classes.Constants;
import utilpackage.CsvUtils;
import utilpackage.WekaUtils;

public class TestHandlingCSVTestCase {

	private static final Logger LOGGER = Logger.getLogger(TestHandlingCSVTestCase.class.getName());

	/**
	 * Test read csv file.
	 */
	@Test
	public void testReadCSVTestCase() {
		try {
			List<List<String>> allDataList = CsvUtils.readCSVFile(Constants.C_WORK_CSV_FILE);
			Assert.assertEquals("The lines of the file should be 413", 413, allDataList.size());
		} catch (IOException e) {
			Assert.fail(e.getMessage());
		}
	}

	@Test
	public void testReadCSVColumnWIseTestCase() {
		try {
			// get all data
			List<List<String>> allDataList = CsvUtils.readCSVFile(Constants.C_WORK_CSV_FILE);
			// get dimension number through getDimensions
			int dimensionsSize = WekaUtils.getDimensions(allDataList, CsvUtils.TAB_SEPARATOR).size();
			// get all data column - wised.
			long startTime = System.nanoTime();
			List<List<String>> dimensionWIseData = CsvUtils.readCSVFileColumnWise(Constants.C_WORK_CSV_FILE,
					dimensionsSize);
			long endTime = System.nanoTime();
			LOGGER.log(Level.INFO, "readCSVFileColumnWise execution time in seconds is {0}",
					(endTime - startTime) / 1_000_000_000.0);
			// assert
			Assert.assertEquals(73663, dimensionWIseData.size());
		} catch (IOException e) {
			Assert.fail(e.getMessage());
		}
	}
}
