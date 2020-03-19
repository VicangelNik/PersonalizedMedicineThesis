
/*
 *
 */
import java.io.File;
import java.io.IOException;
import java.util.Random;

import org.junit.Assert;
import org.junit.Test;

import classifiers.NaiveBayesImplementation;
import dimensionality_reduction_methods.DimensionalityReductionSelection;
import helpful_classes.Constants;
import smile.graph.Graph;
import smile.manifold.IsoMap;
import utilpackage.TransformToFromWeka;
import utilpackage.WekaUtils;
import weka.api.library.WekaFileConverterImpl;
import weka.classifiers.AbstractClassifier;
import weka.core.Instances;

/**
 * The Class TestIsomapTestCase.
 */
public class TestIsomapTestCase {

	/**
	 * Test isomap tet case.
	 *
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	@Test
	public void TestIsomapTetCase() throws IOException {
		File level2File = new File(Constants.SRC_MAIN_RESOURCES_PATH + "PatientAndControlProcessedLevelTwo.arff");
		Instances originalDataset = WekaUtils.getOriginalData(level2File, "SampleStatus");
		int dimensions = 10;
		int kNearest = 5;
		boolean cIsomap = false;
		double data[][] = TransformToFromWeka.transformWekaToManifolds(originalDataset);
		Assert.assertTrue("Data should not be null or empty", data != null && data.length > 0);
		IsoMap myIsomap = new IsoMap(data, dimensions, kNearest, cIsomap);
		double[][] coordinates = myIsomap.getCoordinates();
		Assert.assertTrue("Isomap results should not be null or empty", coordinates != null && coordinates.length > 0);
		Instances reData = TransformToFromWeka.manifoldsToWeka(coordinates, "isomapDataset",
				WekaUtils.getDatasetClassValues(originalDataset), "class");
		// here we save the new data in an arff file
		WekaFileConverterImpl wekaFileConverterImpl = new WekaFileConverterImpl();
		wekaFileConverterImpl.arffSaver(reData, Constants.SRC_MAIN_RESOURCES_PATH + "isomapData.arff");
		// CROSS VALIDATION
		AbstractClassifier abstractClassifier = WekaUtils.getClassifier(Constants.NAIVE_BAYES, reData, new String[] {});
		new NaiveBayesImplementation().crossValidationEvaluation(abstractClassifier, reData, 10, new Random(1));
		// new NaiveBayesImplementation().classify(abstractClassifier, originalDataset);
		Graph graph = myIsomap.getNearestNeighborGraph();
		int[] indexes = myIsomap.getIndex();
		Assert.assertTrue("Graph should not be empty", !graph.getEdges().isEmpty());
		Assert.assertTrue("Original indices should exist.", indexes.length > 0);
	}

	/**
	 * Test isomap data. TestIsomapTetCase should have been run first to create arff
	 * file.
	 *
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	@Test
	public void testIsomapData() throws IOException {
		// GET DATA
		File isomapDataFile = new File(Constants.SRC_MAIN_RESOURCES_PATH + "isomapData.arff");
		Instances isomapDataset = WekaUtils.getOriginalData(isomapDataFile, "class");
		// CROSS VALIDATION
		AbstractClassifier abstractClassifier = WekaUtils.getClassifier(Constants.NAIVE_BAYES, isomapDataset,
				new String[] {});
		new NaiveBayesImplementation().crossValidationEvaluation(abstractClassifier, isomapDataset, 10, new Random(1));
	}

	/**
	 * Do isomap. Same as TestIsomapTetCase but it is called from
	 * DimensionalityReductionSelector
	 *
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	@Test
	public void doIsomap() throws IOException {
		// first option should be the number of expected dimensions
		// second option should be the k nearest neighbors
		// third option should be whether C-Isomap or standard algorithm
		File level2File = new File(Constants.SRC_MAIN_RESOURCES_PATH + "PatientAndControlProcessedLevelTwo.arff");
		Instances originalDataset = WekaUtils.getOriginalData(level2File, "SampleStatus");
		String[] options = { "10", "5", "false" };
		DimensionalityReductionSelection dimensionalityReductionSelection = new DimensionalityReductionSelection();
		Instances dataset = dimensionalityReductionSelection.DimensionalityReductionSelector(Constants.ISOMAP,
				originalDataset, true, options);
		// CROSS VALIDATION
		AbstractClassifier abstractClassifier = WekaUtils.getClassifier(Constants.NAIVE_BAYES, dataset,
				new String[] {});
		new NaiveBayesImplementation().crossValidationEvaluation(abstractClassifier, dataset, 10, new Random(1));
	}
}
