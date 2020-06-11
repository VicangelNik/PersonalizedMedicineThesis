package classifiers;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;

import org.junit.Assert;
import org.junit.jupiter.api.AfterEach;
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
public class TestMLPDeepLearning4jTestCase {

	/** The class name. */
	private final String className = Constants.classNameForReducedData;

	/** The dataset file name. */
	private final String datasetFileName = Constants.dataset20EMPCAFileName;

	/** The num folds. */
	private int numFolds = 10;

	/** The random. */
	private int random = 1;

	/**
	 * Inits the.
	 *
	 * @param testInfo the test info
	 */
	@BeforeEach
	void init(TestInfo testInfo) {
		Constants.logger.getLogger().log(Level.INFO, "START TEST");
		Constants.logger.getLogger().log(Level.INFO, "SAVE FILE NAME: " + datasetFileName);
		Constants.logger.getLogger().log(Level.INFO, "SAVE DISPLAY NAME: " + testInfo.getDisplayName());
	}

	/**
	 * On End.
	 */
	@AfterEach
	void onEnd() {
		Constants.logger.getLogger().log(Level.INFO, "END TEST");
	}

	/**
	 * Test part default.
	 */
	@Test
	@DisplayName("Multilayer Perceptron Default")
	public void testPartDefault() {
		// for (String datasetFileName : fileNames) {
		System.out.println(datasetFileName.toUpperCase());
		try {
			File level2File = new File(datasetFileName);
			Instances originalDataset = WekaUtils.getOriginalData(level2File, className);
			AbstractClassifier classifier = WekaUtils.getClassifier(Constants.DEEPLEARNING4J, originalDataset,
					new String[] { "1", "true", "10" });
			WekaUtils.crossValidationAction(Constants.DEEPLEARNING4J, classifier, numFolds, random);
		} catch (IOException e) {
			Assert.assertFalse(e.getMessage(), true);
		}
		// }
	}
}
