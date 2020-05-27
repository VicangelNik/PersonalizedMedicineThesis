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
 * The Class TestNaiveBayesTestCase.
 */
public class TestNaiveBayesTestCase {

	/** The dataset file name. */
	// private final String datasetFileName = Constants.WEKA_FILES + "iris.arff";
//	private final String datasetFileName = Constants.SRC_MAIN_RESOURCES_PATH
//			+ "PatientAndControlProcessedLevelTwo.arff";
//	private final String datasetFileName = Constants.SRC_MAIN_RESOURCES_PATH + "methDataset.arff";
//	private final String datasetFileName = Constants.SRC_MAIN_RESOURCES_PATH + "miRNADataset.arff";
//	private final String datasetFileName = Constants.SRC_MAIN_RESOURCES_PATH + "mRNADataset.arff";
//	private final String datasetFileName = Constants.SRC_MAIN_RESOURCES_PATH + "10empcaData.arff";
//	private final String datasetFileName = Constants.SRC_MAIN_RESOURCES_PATH + "20empcaData.arff";
//	private final String datasetFileName = Constants.SRC_MAIN_RESOURCES_PATH + "50empcaData.arff";
	private final String datasetFileName = Constants.SRC_MAIN_RESOURCES_PATH + "100empcaData.arff";

	/** The num folds. */
	private int numFolds = 10;

	/** The random. */
	private int random = 1;

	/** The class name. */
	private final String className = "class";
	// private final String className = "SampleStatus";

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
	 * Test NB default.
	 */
	@Test
	@DisplayName("Naive Bayes Default")
	public void testNBDefault() {
		try {
			File level2File = new File(datasetFileName);
			Instances originalDataset = WekaUtils.getOriginalData(level2File, className);
			String[] options = new String[] {};
			AbstractClassifier classifier = WekaUtils.getClassifier(Constants.NAIVE_BAYES, originalDataset, options);
			WekaUtils.crossValidationAction(Constants.NAIVE_BAYES, classifier, numFolds, random);
		} catch (IOException e) {
			Assert.assertFalse(e.getMessage(), true);
		}
	}

	/**
	 * Test NB updatable deafult.
	 */
	@Test
	@DisplayName("Naive Bayes Updatable Default")
	public void testNBUpdatableDeafult() {
		try {
			File level2File = new File(datasetFileName);
			Instances originalDataset = WekaUtils.getOriginalData(level2File, className);
			String[] options = new String[] {};
			AbstractClassifier classifier = WekaUtils.getClassifier(Constants.NAIVE_BAYES_UPDATABLE, originalDataset,
					options);
			WekaUtils.crossValidationAction(Constants.NAIVE_BAYES_UPDATABLE, classifier, numFolds, random);
		} catch (IOException e) {
			Assert.assertFalse(e.getMessage(), true);
		}
	}

	/**
	 * Test NB kernel estimator.
	 */
	@Test
	@DisplayName("Naive Bayes Kernel Estimator")
	public void testNBKernelEstimator() {
		try {
			File level2File = new File(datasetFileName);
			Instances originalDataset = WekaUtils.getOriginalData(level2File, className);
			// Use kernel density estimator rather than normal distribution for numeric
			// attributes
			String[] options = new String[] { "-K" };
			AbstractClassifier classifier = WekaUtils.getClassifier(Constants.NAIVE_BAYES, originalDataset, options);
			WekaUtils.crossValidationAction(Constants.NAIVE_BAYES, classifier, numFolds, random);
		} catch (IOException e) {
			Assert.assertFalse(e.getMessage(), true);
		}
	}

	/**
	 * Test NB updatable kernel estimator.
	 */
	@Test
	@DisplayName("Naive Bayes Updatable Kernel Estimator")
	public void testNBUpdatableKernelEstimator() {
		try {
			File level2File = new File(datasetFileName);
			Instances originalDataset = WekaUtils.getOriginalData(level2File, className);
			// Use kernel density estimator rather than normal distribution for numeric
			// attributes
			String[] options = new String[] { "-K" };
			AbstractClassifier classifier = WekaUtils.getClassifier(Constants.NAIVE_BAYES_UPDATABLE, originalDataset,
					options);
			WekaUtils.crossValidationAction(Constants.NAIVE_BAYES_UPDATABLE, classifier, numFolds, random);
		} catch (IOException e) {
			Assert.assertFalse(e.getMessage(), true);
		}
	}

	/**
	 * Test NB supervised discretization.
	 */
	@Test
	@DisplayName("Naive Bayes Supervised Discretization")
	public void testNBSupervisedDiscretization() {
		try {
			File level2File = new File(datasetFileName);
			Instances originalDataset = WekaUtils.getOriginalData(level2File, className);
			String[] options = new String[] { "-D" };
			AbstractClassifier classifier = WekaUtils.getClassifier(Constants.NAIVE_BAYES, originalDataset, options);
			WekaUtils.crossValidationAction(Constants.NAIVE_BAYES, classifier, numFolds, random);
		} catch (IOException e) {
			Assert.assertFalse(e.getMessage(), true);
		}
	}
}
