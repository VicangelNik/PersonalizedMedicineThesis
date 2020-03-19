package test.weka.api;

import java.io.File;
import java.io.IOException;
import java.util.Random;

import org.junit.Assert;
import org.junit.Test;

import classifiers.NaiveBayesImplementation;
import classifiers.ZeroRImplementation;
import dimensionality_reduction_methods.DimensionalityReductionSelection;
import helpful_classes.Constants;
import utilpackage.WekaUtils;
import weka.classifiers.AbstractClassifier;
import weka.core.Instances;

/**
 * The Class TestClassificationTestCase.
 */
public class TestClassificationTestCase {

	/** The dataset file name. */
	// private final String datasetFileName = Constants.WEKA_FILES + "iris.arff";
	private final String datasetFileName = Constants.SRC_MAIN_RESOURCES_PATH
			+ "PatientAndControlProcessedLevelTwo.arff";

	/** The class name. */
	// private final String className = "class";
	private final String className = "SampleStatus";

	/**
	 * Test naive bayes classification.
	 *
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	@Test
	public void testNaiveBayesClassification() throws IOException {
		File level2File = new File(datasetFileName);
		Assert.assertTrue("The file should exists", level2File.exists());
		Assert.assertTrue("The file should be readable.", level2File.canRead());
		Instances originalDataset = WekaUtils.getOriginalData(level2File, className);
		AbstractClassifier abstractClassifier = WekaUtils.getClassifier(Constants.NAIVE_BAYES, originalDataset,
				new String[] {});
		new NaiveBayesImplementation().classify(abstractClassifier, originalDataset);
	}

	/**
	 * Test naive bayes cross validation evaluation.
	 *
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	@Test
	public void testNaiveBayesCrossValidationEvaluation() throws IOException {
		File level2File = new File(datasetFileName);
		Instances originalDataset = WekaUtils.getOriginalData(level2File, className);
		AbstractClassifier abstractClassifier = WekaUtils.getClassifier(Constants.NAIVE_BAYES, originalDataset,
				new String[] {});
		new NaiveBayesImplementation().crossValidationEvaluation(abstractClassifier, originalDataset, 10,
				new Random(1));
	}

	/**
	 * Test zero R.
	 *
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	@Test
	public void testZeroR() throws IOException {
		File level2File = new File(datasetFileName);
		Instances originalDataset = WekaUtils.getOriginalData(level2File, className);
		AbstractClassifier abstractClassifier = WekaUtils.getClassifier(Constants.ZERO_R, originalDataset,
				new String[] {});
		new ZeroRImplementation().crossValidationEvaluation(abstractClassifier, originalDataset, 10, new Random(1));
	}

	/**
	 * Test naive bayes cross validation evaluation with dimensionality reduction.
	 *
	 * @throws IOException Signals that an I/O exception has occurred.
	 */

	@Test
	public void testNaiveBayesCrossValidationEvaluationWithDimensionalityReduction() throws IOException {
		File level2File = new File(datasetFileName);
		Instances originalDataset = WekaUtils.getOriginalData(level2File, className);
		// DIMENSIONALITY REDUCTION
		DimensionalityReductionSelection dimensionalityReductionSelection = new DimensionalityReductionSelection();
		try {
			// http://weka.sourceforge.net/doc.dev/weka/filters/unsupervised/attribute/PrincipalComponents.html
			String[] options = weka.core.Utils.splitOptions("-R 0.95 -A 5 -M -1");
			Instances dataset = dimensionalityReductionSelection.DimensionalityReductionSelector("pca", originalDataset,
					true, options);
			// CROSS VALIDATION
			AbstractClassifier abstractClassifier = WekaUtils.getClassifier(Constants.NAIVE_BAYES, dataset,
					new String[] {});
			new NaiveBayesImplementation().crossValidationEvaluation(abstractClassifier, originalDataset, 10,
					new Random(1));
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail(e.getMessage());
		}
	}
}
