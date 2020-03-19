
import java.io.File;

import org.junit.Assert;
import org.junit.Test;

import dimensionality_reduction_methods.DimensionalityReductionSelection;
import helpful_classes.Constants;
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
		// DIMENSIONALITY REDUCTION
		DimensionalityReductionSelection dimensionalityReductionSelection = new DimensionalityReductionSelection();
		String[] options = weka.core.Utils.splitOptions("-N 20 -L 0.01 -O 1.0E-6 -G -P 1 -E 1 -S 1 -seed 1");
		// Get current time
		long start = System.currentTimeMillis();
		Instances dataset = dimensionalityReductionSelection.DimensionalityReductionSelector(Constants.AUTOENCODER_WEKA,
				originalDataset, true, options);
		// Get elapsed time in milliseconds
		long elapsedTimeMillis = System.currentTimeMillis() - start;
		// Get elapsed time in hours
		System.out.println(elapsedTimeMillis / (60 * 60 * 1000F));
		// here we save the new data in an arff file
		WekaFileConverterImpl wekaFileConverterImpl = new WekaFileConverterImpl();
		wekaFileConverterImpl.arffSaver(dataset, Constants.SRC_MAIN_RESOURCES_PATH + "autoencoderData.arff");
		Assert.assertTrue(dataset != null);
	}
}
