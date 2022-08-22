package personalizedmedicine;

import java.io.File;

import org.junit.Assert;
import org.junit.Test;

import personalizedmedicine.dimensionality_reduction_methods.DimensionalityReductionChooser;
import personalizedmedicine.helpful_classes.Constants;
import personalizedmedicine.utilpackage.Utils;
import personalizedmedicine.utilpackage.WekaUtils;
import personalizedmedicine.weka.api.library.WekaFileConverter;
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
		// this command return the logical processors (threads)
		String cores = String.valueOf(Runtime.getRuntime().availableProcessors());
		// DIMENSIONALITY REDUCTION
		DimensionalityReductionChooser dimensionalityReductionSelection = new DimensionalityReductionChooser();
		String[] options = weka.core.Utils
				.splitOptions("-N 1 -L 0.01 -O 1.0E-6 -G -P " + cores + " -E " + cores + " -S 1 -seed 1");
		// Get current time
		long start = System.nanoTime();
		Instances dataset = dimensionalityReductionSelection.dimensionalityReductionSelector(Constants.AUTOENCODER_WEKA,
				originalDataset, true, options);
		Utils.printExecutionTime(start, System.nanoTime());
		// here we save the new data in an arff file
		WekaFileConverter wekaFileConverterImpl = new WekaFileConverter();
		wekaFileConverterImpl.arffSaver(dataset, Constants.SRC_MAIN_RESOURCES_PATH + "autoencoderData.arff");
		Assert.assertTrue(dataset != null);
	}
}
