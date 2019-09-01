
/*
 *
 */

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.jblas.DoubleMatrix;
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
	public static double doubleArray[][];

	/** The matrix X. */
	public static DoubleMatrix matrixX;

	/** The matrix W. */
	public static DoubleMatrix matrixW;

	/** The matrix H. */
	public static DoubleMatrix matrixH;

	/**
	 * Clear resources.
	 */
	// @AfterClass
	public static void clearResources() {
		// remove all the contents of the directory
		File file = new File(Utils.RELATIVE_PATH);
		try {
			FileUtils.cleanDirectory(file);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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
		Utils.writeMatrixToFile(Utils.FILE_DATA_PATH, matrixX);
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
	 * Test test make file for weka.
	 */
// Test for constructing the file for weka application.
	@Test
	public void testTestMakeFileForWeka() {
		File fileData = new File(Utils.FILE_DATA_PATH);
		try {
			// Prepare weka file.
			WekaUtils.prepareWekaFile("nationality", makeAttributes(), fileData,
					Utils.RELATIVE_PATH + "wekaFile" + WekaUtils.WEKA_SUFFIX);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}