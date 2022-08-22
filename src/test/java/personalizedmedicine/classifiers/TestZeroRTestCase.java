package java.personalizedmedicine.classifiers;

import java.io.File;
import java.io.IOException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;

import java.personalizedmedicine.helpful_classes.Constants;
import java.personalizedmedicine.utilpackage.WekaUtils;
import weka.classifiers.AbstractClassifier;
import weka.core.Instances;

/**
 * The Class TestZeroRTestCase.
 */
public class TestZeroRTestCase {

	/** The num folds. */
	private int numFolds = 10;

	/** The random. */
	private int random = 1;

	/** The class name. */
	private final String className = "SampleStatus";

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
	 * Test zero R original.
	 *
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	@Test
	@DisplayName("ZeroR in PatientAndControlProcessedLevelTwo.arff")
	public void testZeroROriginal() throws IOException {
		final String datasetFileName = Constants.SRC_MAIN_RESOURCES_PATH + "PatientAndControlProcessedLevelTwo.arff";
		File level2File = new File(datasetFileName);
		Instances originalDataset = WekaUtils.getOriginalData(level2File, className);
		AbstractClassifier classifier = WekaUtils.getClassifier(Constants.ZERO_R, originalDataset, new String[] {});
		WekaUtils.crossValidationAction(Constants.ZERO_R, classifier, numFolds, random);
	}

	/**
	 * Test zero R methylation.
	 *
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	@Test
	@DisplayName("ZeroR in methDataset.arff")
	public void testZeroRMethylation() throws IOException {
		final String datasetFileName = Constants.SRC_MAIN_RESOURCES_PATH + "methDataset.arff";
		File level2File = new File(datasetFileName);
		Instances originalDataset = WekaUtils.getOriginalData(level2File, className);
		AbstractClassifier classifier = WekaUtils.getClassifier(Constants.ZERO_R, originalDataset, new String[] {});
		WekaUtils.crossValidationAction(Constants.ZERO_R, classifier, numFolds, random);
	}

	/**
	 * Test zero R mi RNA.
	 *
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	@Test
	@DisplayName("ZeroR in miRNADataset.arff")
	public void testZeroRMiRNA() throws IOException {
		final String datasetFileName = Constants.SRC_MAIN_RESOURCES_PATH + "miRNADataset.arff";
		File level2File = new File(datasetFileName);
		Instances originalDataset = WekaUtils.getOriginalData(level2File, className);
		AbstractClassifier classifier = WekaUtils.getClassifier(Constants.ZERO_R, originalDataset, new String[] {});
		WekaUtils.crossValidationAction(Constants.ZERO_R, classifier, numFolds, random);
	}

	/**
	 * Test zero rm RNA.
	 *
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	@Test
	@DisplayName("ZeroR in mRNADataset.arff")
	public void testZeroRmRNA() throws IOException {
		final String datasetFileName = Constants.SRC_MAIN_RESOURCES_PATH + "mRNADataset.arff";
		File level2File = new File(datasetFileName);
		Instances originalDataset = WekaUtils.getOriginalData(level2File, className);
		AbstractClassifier classifier = WekaUtils.getClassifier(Constants.ZERO_R, originalDataset, new String[] {});
		WekaUtils.crossValidationAction(Constants.ZERO_R, classifier, numFolds, random);
	}
}
