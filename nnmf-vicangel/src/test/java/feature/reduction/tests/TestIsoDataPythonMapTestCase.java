package feature.reduction.tests;

import org.junit.jupiter.api.Test;

import weka.core.converters.CSVLoader;

/**
 * The Class TestIsoDataPythonMapTestCase. This test case class is created for
 * JUnit 5. It tests whether the files are created correctly from python and it
 * converts them in arff files for better manipulation and in weka. This does
 * not mean that weka can not manipulate with the same way csv files.
 */
public class TestIsoDataPythonMapTestCase {

	/**
	 * Test data created from python.
	 */
	@Test
	public void testDataCreatedFromPython() {
		CSVLoader loader = new CSVLoader();
        // loader.setSource(inFile);
      //  Instances data = loader.getDataSet();
	}

}
