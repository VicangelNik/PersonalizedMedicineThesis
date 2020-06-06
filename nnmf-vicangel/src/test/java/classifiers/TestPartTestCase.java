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

import com.beust.jcommander.Strings;

import helpful_classes.AppLogger;
import helpful_classes.Constants;
import utilpackage.WekaUtils;
import weka.classifiers.AbstractClassifier;
import weka.core.Instances;

/**
 * The Class TestPartTestCase.
 */
public class TestPartTestCase {

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

	private String[] doNotMakeSplitPointActualValue = new String[] { " -doNotMakeSplitPointActualValue ", "" };

	private String[] useMDLCorrection = new String[] { " -J ", "" };

	private String[] unpruned = new String[] { " -U ", "" };

	private String[] binarySplits = new String[] { " -B ", "" };

	private String[] reducedErrorPruning = new String[] { " -R ", "" };

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
	 * Test part default.
	 */
	@Test
	@DisplayName("Part Default")
	public void testPartDefault() {
		// -M 2 -C 0.25 -Q 1
		System.out.println(datasetFileName.toUpperCase());
		try {
			File level2File = new File(datasetFileName);
			Instances originalDataset = WekaUtils.getOriginalData(level2File, className);
			AbstractClassifier classifier = WekaUtils.getClassifier(Constants.PART, originalDataset, new String[] {});
			WekaUtils.crossValidationAction(Constants.PART, classifier, numFolds, random);
		} catch (IOException e) {
			Assert.assertFalse(e.getMessage(), true);
		}
	}

	@Test
	@DisplayName("Part All")
	public void testPartAll() {
		// -M 2 -C 0.25 -Q 1
		for (String datasetFileName : Constants.datasetEmpcafileNames) {
			try {
				File level2File = new File(datasetFileName);
				Instances originalDataset = WekaUtils.getOriginalData(level2File, className);
				int count = 0;
				// confidenceFactor must be greater than 0 and less than 1
				for (double confidenceFactor = 0.5; confidenceFactor <= 1.0; confidenceFactor += 0.5) { // -C
					for (int minNumObj = 1; minNumObj <= 20; minNumObj++) { // -M
						// numFolds must be greater than 1
						for (int numFolds = 2; numFolds <= 20; numFolds++) { // -N
							for (String noSplitPointValue : doNotMakeSplitPointActualValue) {
								for (String mdl : useMDLCorrection) {
									for (String pruningIsPerformed : unpruned) {
										for (String bSplit : binarySplits) {
											for (String reducedPrune : reducedErrorPruning) {
												StringBuilder sb = new StringBuilder();
												sb.append(reducedPrune).append(bSplit).append(pruningIsPerformed)
														.append(mdl).append(noSplitPointValue).append(" -M ")
														.append(minNumObj);
												// -C cannot be at the same time with -R
												if (Strings.isStringEmpty(reducedPrune)) {
													sb.append(" -C ").append(confidenceFactor);
												} else {
													// Setting the number of folds does only make sense for reduced
													// error pruning.
													sb.append(" -N ").append(numFolds);
												}
												logger.getLogger().log(Level.INFO,
														"SAVE TEST INFO NAME: " + "Configuration_" + count);
												String[] options = weka.core.Utils.splitOptions(sb.toString());
												AbstractClassifier classifier = WekaUtils.getClassifier(Constants.PART,
														originalDataset, options);
												WekaUtils.crossValidationAction(Constants.PART, classifier, numFolds,
														random);
											}
										}
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
}
