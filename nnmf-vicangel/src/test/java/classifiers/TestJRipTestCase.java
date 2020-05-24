package classifiers;

import java.io.File;
import java.io.IOException;

import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;

import helpful_classes.Constants;
import utilpackage.WekaUtils;
import weka.classifiers.AbstractClassifier;
import weka.core.Instances;
// TODO: Auto-generated Javadoc

/**
 * The Class TestJRipTestCase.
 */
public class TestJRipTestCase {

	/** The dataset file name. */
	// private final String datasetFileName = Constants.WEKA_FILES + "iris.arff";
	private final String completeFileName = Constants.SRC_MAIN_RESOURCES_PATH
			+ "PatientAndControlProcessedLevelTwo.arff";

	/** The meth file name. */
	private final String methFileName = Constants.SRC_MAIN_RESOURCES_PATH + "methDataset.arff";

	/** The mi RNA file name. */
	private final String miRNAFileName = Constants.SRC_MAIN_RESOURCES_PATH + "miRNADataset.arff";

	/** The m RNA file name. */
	private final String mRNAFileName = Constants.SRC_MAIN_RESOURCES_PATH + "mRNADataset.arff";

	/** The num folds. */
	private int numFolds = 10;

	/** The random. */
	private int random = 1;

	/** The class name. */
	// private final String className = "class";
	private final String className = "SampleStatus";

	/** The file names. */
	private final String[] fileNames = new String[] { completeFileName, methFileName, miRNAFileName, mRNAFileName };

	/**
	 * Inits the.
	 *
	 * @param testInfo the test info
	 */
	@BeforeEach
	void init(TestInfo testInfo) {
		System.out.println(testInfo.getDisplayName());
	}

	/**
	 * Test jrip default.
	 */
	@Test
	@DisplayName("Jrip Default")
	public void testJripDefault() {
		for (String datasetFileName : fileNames) {
			System.out.println(datasetFileName.toUpperCase());
			try {
				File level2File = new File(datasetFileName);
				Instances originalDataset = WekaUtils.getOriginalData(level2File, className);
				AbstractClassifier classifier = WekaUtils.getClassifier(Constants.JRIP, originalDataset,
						new String[] {});
				WekaUtils.crossValidationAction(Constants.JRIP, classifier, numFolds, random);
			} catch (IOException e) {
				Assert.assertFalse(e.getMessage(), true);
			}
		}
	}
}
