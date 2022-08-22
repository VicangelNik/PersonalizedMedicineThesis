package personalizedmedicine.classifiers;

import java.io.File;
import java.io.IOException;

import org.junit.Assert;
import org.junit.Test;

import personalizedmedicine.dimensionality_reduction_methods.DimensionalityReductionChooser;
import personalizedmedicine.helpful_classes.Constants;
import personalizedmedicine.utilpackage.WekaUtils;
import weka.classifiers.AbstractClassifier;
import weka.core.Instances;

/**
 * The Class TestClassificationTestCase.
 */
public class TestClassificationTestCase {

	/** The dataset file name. */
	private final String datasetFileName = Constants.WEKA_FILES + "iris.arff";
//	private final String datasetFileName = Constants.SRC_MAIN_RESOURCES_PATH
//			+ "PatientAndControlProcessedLevelTwo.arff";

	/** The num folds. */
	private int numFolds = 10;

	/** The random. */
	private int random = 1;

	/** The class name. */
	private final String className = "class";
	// private final String className = "SampleStatus";

	/**
	 * Test naive bayes.
	 *
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	@Test
	public void testNaiveBayes() throws IOException {
		File level2File = new File(datasetFileName);
		Instances originalDataset = WekaUtils.getOriginalData(level2File, className);
		AbstractClassifier classifier = WekaUtils.getClassifier(Constants.NAIVE_BAYES, originalDataset,
				new String[] {});
		WekaUtils.crossValidationAction(Constants.NAIVE_BAYES, classifier, numFolds, random);
	}

	/**
	 * Test naive bayes updatable.
	 *
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	@Test
	public void testNaiveBayesUpdatable() throws IOException {
		File level2File = new File(datasetFileName);
		Instances originalDataset = WekaUtils.getOriginalData(level2File, className);
		AbstractClassifier classifier = WekaUtils.getClassifier(Constants.NAIVE_BAYES_UPDATABLE, originalDataset,
				new String[] {});
		WekaUtils.crossValidationAction(Constants.NAIVE_BAYES_UPDATABLE, classifier, numFolds, random);
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
		AbstractClassifier classifier = WekaUtils.getClassifier(Constants.ZERO_R, originalDataset, new String[] {});
		WekaUtils.crossValidationAction(Constants.ZERO_R, classifier, numFolds, random);
	}

	/**
	 * Test jrip.
	 *
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	@Test
	public void testJrip() throws IOException {
		File level2File = new File(datasetFileName);
		Instances originalDataset = WekaUtils.getOriginalData(level2File, className);
		AbstractClassifier classifier = WekaUtils.getClassifier(Constants.JRIP, originalDataset, new String[] {});
		WekaUtils.crossValidationAction(Constants.JRIP, classifier, numFolds, random);
	}

	/**
	 * Test PART.
	 *
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	@Test
	public void testPART() throws IOException {
		File level2File = new File(datasetFileName);
		Instances originalDataset = WekaUtils.getOriginalData(level2File, className);
		AbstractClassifier classifier = WekaUtils.getClassifier(Constants.PART, originalDataset, new String[] {});
		WekaUtils.crossValidationAction(Constants.PART, classifier, numFolds, random);
	}

	/**
	 * Test IBK.
	 *
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	@Test
	public void testIBK() throws IOException {
		File level2File = new File(datasetFileName);
		Instances originalDataset = WekaUtils.getOriginalData(level2File, className);
		AbstractClassifier classifier = WekaUtils.getClassifier(Constants.IBK, originalDataset, new String[] {});
		WekaUtils.crossValidationAction(Constants.IBK, classifier, numFolds, random);
	}

	/**
	 * Test deep learning 4 j.
	 *
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	@Test
	public void testDeepLearning4j() throws IOException {
		File level2File = new File(datasetFileName);
		Instances originalDataset = WekaUtils.getOriginalData(level2File, className);
		AbstractClassifier classifier = WekaUtils.getClassifier(Constants.DEEPLEARNING4J, originalDataset,
				new String[] { "1", "true" });
		WekaUtils.crossValidationAction(Constants.DEEPLEARNING4J, classifier, numFolds, random);
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
		DimensionalityReductionChooser dimensionalityReductionSelection = new DimensionalityReductionChooser();
		try {
			// http://weka.sourceforge.net/doc.dev/weka/filters/unsupervised/attribute/PrincipalComponents.html
			String[] options = weka.core.Utils.splitOptions("-R 0.95 -A 5 -M -1");
			Instances dataset = dimensionalityReductionSelection.dimensionalityReductionSelector("pca", originalDataset,
					true, options);
			// TODO CROSS VALIDATION
//			AbstractClassifier abstractClassifier = WekaUtils.getClassifier(Constants.NAIVE_BAYES, dataset,
//					new String[] {});
//			new NaiveBayesWeka().crossValidationEvaluation(abstractClassifier, originalDataset, 10,
//					new Random(1));
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail(e.getMessage());
		}
	}
}
