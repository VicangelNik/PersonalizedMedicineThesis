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

/**
 * The Class TestPartTestCase.
 */
public class TestPartTestCase {

	/** The dataset file name. */
	// private final String datasetFileName = Constants.WEKA_FILES + "iris.arff";

	/** The num folds. */
	private int numFolds = 10;

	/** The random. */
	private int random = 1;

	/** The class name. */
	private final String className = "class";
	// private final String className = "SampleStatus";

	/** The file names. */
	// private final String[] fileNames = new String[] { completeFileName,
	// methFileName, miRNAFileName, mRNAFileName };
	private final String[] fileNames = new String[] { Constants.dataset10EMPCAFileName,
			Constants.dataset20EMPCAFileName, Constants.dataset50EMPCAFileName, Constants.dataset100EMPCAFileName };

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
	 * Test part default.
	 */
	@Test
	@DisplayName("Part Default")
	public void testPartDefault() {
		// -M 2 -C 0.25 -Q 1
		for (String datasetFileName : fileNames) {
			System.out.println(datasetFileName.toUpperCase());
			try {
				File level2File = new File(datasetFileName);
				Instances originalDataset = WekaUtils.getOriginalData(level2File, className);
				AbstractClassifier classifier = WekaUtils.getClassifier(Constants.PART, originalDataset,
						new String[] {});
				WekaUtils.crossValidationAction(Constants.PART, classifier, numFolds, random);
			} catch (IOException e) {
				Assert.assertFalse(e.getMessage(), true);
			}
		}
	}
}
