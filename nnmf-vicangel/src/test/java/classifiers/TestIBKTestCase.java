package classifiers;

import java.io.File;
import java.io.IOException;
import java.util.StringJoiner;
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
 * The Class TestIBKTestCase.
 */
public class TestIBKTestCase {

	/** The class name. */
	private final String className = Constants.classNameForReducedData;

	/** The dataset file name. */
	private final String datasetFileName = Constants.dataset20EMPCAFileName;

	/** The num folds. */
	private int numFolds = 10;

	/** The random. */
	private int random = 1;

	private String[] crossValidate = new String[] { " -X", "" }; // creates problems when is set

	private String[] distanceWeighting = new String[] { " -I", " -F", "" };

	private String[] meanSquaredError = new String[] { "-E", "" };

	private String[] nearestNeighbourSearchAlgorithm = new String[] { "weka.core.neighboursearch.LinearNNSearch " }; // -A

	// NearestNeighbourSearchAlgorithm attributes

	private String[] measurePerformance = new String[] { " -P ", "" };

	private String[] skipIdentical = new String[] { " -S ", "" };

	private String[] distanceFunction = new String[] { "weka.core.EuclideanDistance -R first-last ",
			"weka.core.ManhattanDistance -R first-last ", "weka.core.ChebyshevDistance -R first-last " }; // -A
	// distanceFunction attributes
	private String[] dontNormalize = new String[] { "-D", "" };

	/**
	 * Inits the.
	 *
	 * @param testInfo the test info
	 */
	@BeforeEach
	void init(TestInfo testInfo) {
		Constants.logger.getLogger().log(Level.INFO, "START TEST");
//		logger.getLogger().log(Level.INFO, "SAVE FILE NAME: " + datasetFileName);
//		logger.getLogger().log(Level.INFO, "SAVE DISPLAY NAME: " + testInfo.getDisplayName());
	}

	/**
	 * On End.
	 */
	@AfterEach
	void onEnd() {
		Constants.logger.getLogger().log(Level.INFO, "END TEST");
	}

	/**
	 * Test ibk default.
	 */
	@Test
	@DisplayName("IBK Default")
	public void testIBKDefault() {
		// weka.classifiers.lazy.IBk -K 1 -W 0 -A
		// "weka.core.neighboursearch.LinearNNSearch -A \"weka.core.EuclideanDistance -R
		// first-last\""
		// for (String datasetFileName : fileNames) {
		System.out.println(datasetFileName.toUpperCase());
		try {
			File level2File = new File(datasetFileName);
			Instances originalDataset = WekaUtils.getOriginalData(level2File, className);
			AbstractClassifier classifier = WekaUtils.getClassifier(Constants.IBK, originalDataset,
					new String[] { "-output-debug-info" });
			WekaUtils.crossValidationAction(Constants.IBK, classifier, numFolds, random);
		} catch (IOException e) {
			Assert.assertFalse(e.getMessage(), true);
		}
		// }
	}

	/**
	 * Test IBKALL.
	 */
	@Test
	@DisplayName("IBK ALL")
	public void testIBKALL() {
		// weka.classifiers.lazy.IBk -K 1 -W 0 -A
		// "weka.core.neighboursearch.LinearNNSearch -A \"weka.core.EuclideanDistance -R
		// first-last\""
		String datasetFileName = Constants.dataset20EMPCAFileName;
		try {
			File level2File = new File(datasetFileName);
			Instances originalDataset = WekaUtils.getOriginalData(level2File, className);
			for (String nNAlgorithm : nearestNeighbourSearchAlgorithm) {
				for (String sIdentical : skipIdentical) {
					for (String dFunction : distanceFunction) {
						for (String dNormalize : dontNormalize) {
							for (int window = 0; window <= 50; window++) // -W
							{
								// knn must be > 0
								for (int knn = 1; knn <= 10; knn++) // -K
								{
									for (String dWeight : distanceWeighting) {
										// for (String cross : crossValidate) { // creates problems when is set
										for (String error : meanSquaredError) {
											StringJoiner joiner = new StringJoiner(" ");
											joiner.add(error).add(dWeight).add("-K") // .add(cross)
													.add(String.valueOf(knn)).add("-W").add(String.valueOf(window))
													.add("-A").add("\"").add(nNAlgorithm).add(sIdentical).add("-A")
													.add("\\\"").add(dFunction).add(dNormalize).add("\\\"").add("\"");
											String[] options = weka.core.Utils.splitOptions(joiner.toString());
											AbstractClassifier classifier = WekaUtils.getClassifier(Constants.IBK,
													originalDataset, options);
											WekaUtils.crossValidationAction(Constants.IBK, classifier, numFolds,
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
