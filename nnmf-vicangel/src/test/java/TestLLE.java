
/*
 *
 */
import java.io.File;
import java.io.IOException;
import java.util.Random;

import org.junit.Assert;
import org.junit.Test;

import dimensionality_reduction_methods.DimensionalityReductionSelection;
import helpful_classes.Constants;
import helpful_classes.NaiveBayesImplementation;
import smile.graph.Graph;
import smile.manifold.LLE;
import utilpackage.TransformToFromWeka;
import utilpackage.WekaUtils;
import weka.api.library.WekaFileConverterImpl;
import weka.classifiers.AbstractClassifier;
import weka.core.Instances;

/**
 * The Class TestLLETestCase.
 */
public class TestLLE {

	// IS NOT WORKING
	/**
	 * Test lle tet case.
	 *
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	@Test
	public void TestLLETetCase() throws IOException {
		File level2File = new File(Constants.SRC_MAIN_RESOURCES_PATH + "PatientAndControlProcessedLevelTwo.arff");
		Instances originalDataset = WekaUtils.getOriginalData(level2File, "SampleStatus");
		double data[][] = TransformToFromWeka.transformWekaToManifolds(originalDataset);
		Assert.assertTrue("Data should not be null or empty", data != null && data.length > 0);

		for (int dimensions = 2; dimensions < 201; dimensions++) {
			for (int kNearest = 1; kNearest < 500; kNearest++) {
				try {
					LLE lle = new LLE(data, dimensions, kNearest);
					System.out.println(dimensions + "   " + kNearest);

					double[][] coordinates = lle.getCoordinates();
					Assert.assertTrue("LLE results should not be null or empty",
							coordinates != null && coordinates.length > 0);
					Instances reData = TransformToFromWeka.manifoldsToWeka(coordinates, "lleDataset",
							WekaUtils.getDatasetClassValues(originalDataset), "class");
					// here we save the new data in an arff file
					WekaFileConverterImpl wekaFileConverterImpl = new WekaFileConverterImpl();
					wekaFileConverterImpl.arffSaver(reData, Constants.SRC_MAIN_RESOURCES_PATH + "lleData.arff");
					// CROSS VALIDATION
					AbstractClassifier abstractClassifier = WekaUtils.getClassifier(Constants.NAIVE_BAYES, reData);
					new NaiveBayesImplementation().crossValidationEvaluation(abstractClassifier, reData, 10,
							new Random(1));
					// new NaiveBayesImplementation().classify(abstractClassifier, originalDataset);
					Graph graph = lle.getNearestNeighborGraph();
					int[] indexes = lle.getIndex();
					Assert.assertTrue("Graph should not be empty", !graph.getEdges().isEmpty());
					Assert.assertTrue("Original indices should exist.", indexes.length > 0);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * Test lle data. TestLLETetCase should have been run first to create arff file.
	 *
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	@Test
	public void testLLEData() throws IOException {
		// GET DATA
		File lleDataFile = new File(Constants.SRC_MAIN_RESOURCES_PATH + "lleData.arff");
		Instances lleDataset = WekaUtils.getOriginalData(lleDataFile, "class");
		// CROSS VALIDATION
		AbstractClassifier abstractClassifier = WekaUtils.getClassifier(Constants.NAIVE_BAYES, lleDataset);
		new NaiveBayesImplementation().crossValidationEvaluation(abstractClassifier, lleDataset, 10, new Random(1));
	}

	/**
	 * Do lle. Same as TestLLETetCase but it is called from
	 * DimensionalityReductionSelector
	 *
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	@Test
	public void doLLE() throws IOException {
		// first option should be the number of expected dimensions
		// second option should be the k nearest neighbors
		File level2File = new File(Constants.SRC_MAIN_RESOURCES_PATH + "PatientAndControlProcessedLevelTwo.arff");
		Instances originalDataset = WekaUtils.getOriginalData(level2File, "SampleStatus");
		String[] options = { "10", "5" };
		DimensionalityReductionSelection dimensionalityReductionSelection = new DimensionalityReductionSelection();
		Instances dataset = dimensionalityReductionSelection.DimensionalityReductionSelector(Constants.LLE,
				originalDataset, true, options);
		// CROSS VALIDATION
		AbstractClassifier abstractClassifier = WekaUtils.getClassifier(Constants.NAIVE_BAYES, dataset);
		new NaiveBayesImplementation().crossValidationEvaluation(abstractClassifier, dataset, 10, new Random(1));
	}
}
