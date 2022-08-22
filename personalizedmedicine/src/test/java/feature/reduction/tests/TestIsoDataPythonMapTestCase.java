package feature.reduction.tests;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import helpful_classes.Constants;
import utilpackage.WekaUtils;
import weka.core.Instances;

/**
 * The Class TestIsoDataPythonMapTestCase. This test case class is created for
 * JUnit 5. It tests whether the files are created correctly from python and it
 * converts them in arff files for better manipulation and in weka. This does
 * not mean that weka can not manipulate with the same way csv files.
 */
public class TestIsoDataPythonMapTestCase {

	/** The class name. */
	private final String className = Constants.classRealName;

	private Map<String, Integer> fileNameNumAttributesMap = new HashMap<String, Integer>() {
		private static final long serialVersionUID = 1226004314628974778L;
		{
			put(Constants.dataset10IsomapCsvFileName, 11);
			put(Constants.dataset20IsomapCsvFileName, 21);
			put(Constants.dataset50IsomapCsvFileName, 51);
			put(Constants.dataset100IsomapCsvFileName, 101);

		}
	};;

	/**
	 * Test data created from python.
	 * 
	 * @throws IOException
	 */
	@Test
	@DisplayName("Test python files")
	public void testDataCreatedFromPython() throws IOException {
		fileNameNumAttributesMap.forEach((fileName, expectedNumAttributes) -> {
			File level2File = new File(fileName);
			Instances data = null;
			try {
				data = WekaUtils.getOriginalData(level2File, className);
			} catch (IOException e) {
				e.printStackTrace();
			}
			Assert.assertEquals("The number of attributes", expectedNumAttributes.intValue(), data.numAttributes());
			Assert.assertEquals("The number of instances", 335, data.numInstances());
			Assert.assertEquals("The number of classes(NormalTissue, PrimaryTumor)", 2, data.numClasses());
		});
	}
}
