
import java.io.File;

import org.junit.Assert;
import org.junit.Test;

import dimensionality_reduction_methods.DimensionalityReductionSelection;
import helpful_classes.Constants;
import utilpackage.Utils;
import utilpackage.WekaUtils;
import weka.api.library.WekaFileConverterImpl;
import weka.core.Instances;

/**
 * The Class TestMultiLayerPerceptrons.
 */
public class TestMultiLayerPerceptrons {

	/**
	 * Test Autoencoder Weka case.
	 *
	 * @throws Exception the exception
	 */
	@Test
	public void TestAutoencoderWekaTetCase() throws Exception {
		File level2File = new File(Constants.SRC_MAIN_RESOURCES_PATH + "PatientAndControlProcessedLevelTwo.arff");
		Instances originalDataset = WekaUtils.getOriginalData(level2File, "SampleStatus");
		String cores = String.valueOf(Runtime.getRuntime().availableProcessors());
		// DIMENSIONALITY REDUCTION
		DimensionalityReductionSelection dimensionalityReductionSelection = new DimensionalityReductionSelection();
		String[] options = weka.core.Utils
				.splitOptions("-N 1 -L 0.01 -O 1.0E-6 -G -P " + cores + " -E " + cores + " -S 1 -seed 1");
		// Get current time
		long start = System.nanoTime();
		Instances dataset = dimensionalityReductionSelection.DimensionalityReductionSelector(Constants.AUTOENCODER_WEKA,
				originalDataset, true, options);
		Utils.printExecutionTime(start, System.nanoTime());
		// here we save the new data in an arff file
		WekaFileConverterImpl wekaFileConverterImpl = new WekaFileConverterImpl();
		wekaFileConverterImpl.arffSaver(dataset, Constants.SRC_MAIN_RESOURCES_PATH + "autoencoderData.arff");
		Assert.assertTrue(dataset != null);
	}
}
