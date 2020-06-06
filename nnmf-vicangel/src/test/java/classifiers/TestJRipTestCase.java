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

import helpful_classes.AppLogger;
import helpful_classes.Constants;
import utilpackage.WekaUtils;
import weka.classifiers.AbstractClassifier;
import weka.core.Instances;

/**
 * The Class TestJRipTestCase.
 */
public class TestJRipTestCase {

	/** The logger. */
	private static AppLogger logger = AppLogger.getInstance();

	/** The class name. */
	private final String className = Constants.classNameForReducedData;

	/** The dataset file name. */
	private final String datasetFileName = Constants.dataset20EMPCAFileName;

	/** The num folds. */
	private int numFolds = 10;

	/** The random. */
	private int random = 1;

	private String[] usePruning = new String[] { " -P ", "" };

	private String[] checkErrorRate = new String[] { " -E ", "" };

	/**
	 * Inits the.
	 *
	 * @param testInfo the test info
	 */
	@BeforeEach
	void init(TestInfo testInfo) {
		logger.getLogger().log(Level.INFO, "START TEST");
		// logger.getLogger().log(Level.INFO, "SAVE FILE NAME: " + datasetFileName);
		// logger.getLogger().log(Level.INFO, "SAVE DISPLAY NAME: " +
		// testInfo.getDisplayName());
	}

	/**
	 * On End.
	 */
	@AfterEach
	void onEnd() {
		logger.getLogger().log(Level.INFO, "END TEST");
	}

	/**
	 * Test jrip default.
	 */
	@Test
	@DisplayName("Jrip Default")
	public void testJripDefault() {
		// "-F 3 -N 2.0 -O 2 -S 1"
		System.out.println(datasetFileName.toUpperCase());
		try {
			File level2File = new File(datasetFileName);
			Instances originalDataset = WekaUtils.getOriginalData(level2File, className);
			AbstractClassifier classifier = WekaUtils.getClassifier(Constants.JRIP, originalDataset, new String[] {});
			WekaUtils.crossValidationAction(Constants.JRIP, classifier, numFolds, random);
		} catch (IOException e) {
			Assert.assertFalse(e.getMessage(), true);
		}
	}

	/**
	 * Test jrip all.
	 */
	@Test
	@DisplayName("Jrip All")
	public void testJripAll() {
		// Default: "-F 3 -N 2.0 -O 2 -S 1"
		try {
			for (String datasetFileName : Constants.datasetEmpcafileNames) {
				int count = 0;
				File level2File = new File(datasetFileName);
				Instances originalDataset = WekaUtils.getOriginalData(level2File, className);
				for (int optimization = 1; optimization <= 10;) {
					for (int folds = 1; folds <= 20; folds++) {
						for (double mWeight = 0.5; mWeight <= 10; mWeight += 0.5) {
							for (String prune : usePruning) {
								for (String errorRate : checkErrorRate) {
									String options = " -O " + optimization + " -F " + folds + " -N " + mWeight + prune
											+ errorRate;
									count++;
									logger.getLogger().log(Level.INFO,
											"SAVE TEST INFO NAME: " + "Configuration_" + count);
									AbstractClassifier classifier = WekaUtils.getClassifier(Constants.JRIP,
											originalDataset, weka.core.Utils.splitOptions(options));
									WekaUtils.crossValidationAction(Constants.JRIP, classifier, numFolds, random);
								}
							}
						}
					}
				}
			}
		} catch (Exception e) {
			Assert.assertFalse(e.getMessage(), true);
		}
	}
}
