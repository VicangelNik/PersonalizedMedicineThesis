
/*
 *
 */

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.jblas.DoubleMatrix;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import utilpackage.Utils;
import utilpackage.WekaUtils;

// TODO: Auto-generated Javadoc
/**
 * The Class TestMakeFileForWekaTestCase.
 */
public class TestMakeFileForWekaTestCase {

	/** The double array. */
	private static double doubleArray[][];

	/** The matrix X. */
	private static DoubleMatrix matrixX;

	private static final String wekaFilePath = Utils.SRC_TEST_RESOURCES_PATH + "wekaFile" + WekaUtils.WEKA_SUFFIX;

	private static final String dummyDataFilePath = Utils.SRC_TEST_RESOURCES_PATH + "dummyData" + Utils.TXT_SUFFIX;

	/**
	 * Clear resources.
	 */
	@AfterClass
	public static void clearResources() {
		// remove all the files of the directory but not the files in subdirectories
		Utils.removeFiles(Utils.SRC_TEST_RESOURCES_PATH);
	}

	/**
	 * Generate random numbers to fill the matrixX.
	 */
	@Before
	public void initializeMatrix() {
		// initialize array
		doubleArray = new double[400][4];
		// set numerical parameters to the array
		Utils.setParametersToArray(doubleArray);
		// initialize matrixX
		matrixX = new DoubleMatrix(doubleArray);
		// write data with the class attribute into file
		Utils.writeMatrixToFile(dummyDataFilePath, matrixX);
		// assert
		File expected = new File(Utils.SRC_TEST_RESOURCES_PATH + "\\txtFiles\\dummyData" + Utils.TXT_SUFFIX);
		File actual = new File(dummyDataFilePath);
		try {
			Assert.assertEquals("Files should be identical",
					FileUtils.readLines(expected, StandardCharsets.UTF_8).toString().replaceAll("\\s+", ""),
					FileUtils.readLines(actual, StandardCharsets.UTF_8).toString().replaceAll("\\s+", ""));
		} catch (IOException e) {
			e.printStackTrace();
			Assert.fail(e.getMessage());
		}
	}

	/**
	 * Make attributes.
	 *
	 * @return the map
	 */
	private Map<String, List<String>> makeAttributes() {
		// LinkedHashMap is used because it gives same order for every iteration.
		Map<String, List<String>> attributes = new LinkedHashMap<String, List<String>>();
		List<String> list = new ArrayList<>();
		list.add("REAL");
		attributes.put("height", list);
		attributes.put("deathEstimation", list);
		attributes.put("ageAverage", list);
		attributes.put("weight", list);
		list = new ArrayList<>();
		list.add("greek");
		list.add("chinese");
		attributes.put("class", list);
		return attributes;
	}

	/**
	 * Test for constructing the file for weka application.
	 */
	@Test
	public void tesMakeDummyFileForWeka() {
		File fileDataName = new File(dummyDataFilePath);
		try {
			// Prepare weka file.
			WekaUtils.prepareWekaFile("nationality", makeAttributes(), fileDataName, wekaFilePath);
			// asserts
			File expectedFile = new File(
					Utils.SRC_TEST_RESOURCES_PATH + "wekaFiles\\wekaFileFromDummyData" + WekaUtils.WEKA_SUFFIX);
			File actual = new File(wekaFilePath);
			Assert.assertEquals("Files should be identical",
					FileUtils.readLines(expectedFile, StandardCharsets.UTF_8).toString().replaceAll("\\s+", ""),
					FileUtils.readLines(actual, StandardCharsets.UTF_8).toString().replaceAll("\\s+", ""));
		} catch (IOException e) {
			e.printStackTrace();
			Assert.fail(e.getMessage());
		}
	}
}