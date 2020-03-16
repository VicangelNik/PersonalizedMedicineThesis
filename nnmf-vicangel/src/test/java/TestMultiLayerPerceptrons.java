
/*
 *
 */
import java.io.File;

import org.junit.Assert;
import org.junit.Test;

import dimensionality_reduction_methods.DimensionalityReductionSelection;
import helpful_classes.Constants;
import utilpackage.WekaUtils;
import weka.core.Instances;

/**
 * The Class TestLLETestCase.
 */
public class TestMultiLayerPerceptrons {

	/**
	 * Test Autoencoder case.
	 *
	 * @throws Exception the exception
	 */
	@Test
	public void TestAutoencoderTetCase() throws Exception {
		File level2File = new File(Constants.SRC_MAIN_RESOURCES_PATH + "PatientAndControlProcessedLevelTwo.arff");
		Instances originalDataset = WekaUtils.getOriginalData(level2File, "SampleStatus");
		// DIMENSIONALITY REDUCTION
		DimensionalityReductionSelection dimensionalityReductionSelection = new DimensionalityReductionSelection();
		String[] options = weka.core.Utils.splitOptions("-N 2 -L 0.01 -O 1.0E-6 -G -P 1 -E 1 -S 1 -seed 1");
		Instances dataset = dimensionalityReductionSelection.DimensionalityReductionSelector(Constants.AUTOENCODER_WEKA,
				originalDataset, true, options);
		Assert.assertTrue(dataset != null);
	}
}
