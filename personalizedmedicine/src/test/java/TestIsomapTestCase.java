
/*
 *
 */
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;

import org.junit.Assert;
import org.junit.Test;

import dimensionality_reduction_methods.DimensionalityReductionChooser;
import helpful_classes.Constants;
import smile.graph.Graph;
import smile.manifold.IsoMap;
import utilpackage.TransformToFromWeka;
import utilpackage.Utils;
import utilpackage.WekaUtils;
import weka.api.library.WekaFileConverter;
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
		WekaFileConverter wekaFileConverterImpl = new WekaFileConverter();
		wekaFileConverterImpl.arffSaver(reData, Constants.SRC_MAIN_RESOURCES_PATH + "isomapData.arff");
		// TODO CROSS VALIDATION
//		AbstractClassifier abstractClassifier = WekaUtils.getClassifier(Constants.NAIVE_BAYES, reData, new String[] {});
//		new NaiveBayesWeka().crossValidationEvaluation(abstractClassifier, reData, 10, new Random(1));
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
		// TODO CROSS VALIDATION
//		AbstractClassifier abstractClassifier = WekaUtils.getClassifier(Constants.NAIVE_BAYES, isomapDataset,
//				new String[] {});
//		new NaiveBayesWeka().crossValidationEvaluation(abstractClassifier, isomapDataset, 10, new Random(1));
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
		// the last 2 options will be always the dataset name and the name of the class
		File level2File = new File(Constants.SRC_MAIN_RESOURCES_PATH + "PatientAndControlProcessedLevelTwo.arff");
		Instances originalDataset = WekaUtils.getOriginalData(level2File, "SampleStatus");
		String[] nDimensionsArray = { "10", "20", "50", "100", "500", "1000" };
		String className = "class";
		Double value = Math.pow(originalDataset.numAttributes() / 2, 0.5);
		String ruleOfThumb = String.valueOf(value.intValue());
		String[] kArray = { "5", "10", "25", "100", ruleOfThumb, "250" };
		DimensionalityReductionChooser dimensionalityReductionSelection = new DimensionalityReductionChooser();
		for (String nDimensions : nDimensionsArray) {
			for (String k : kArray) {
				String datasetName = nDimensions + "dimesions_" + k + " nearestNeighbours_isomapData";
				String[] options = { nDimensions, k, "false", datasetName, className };
				// Get current time
				long start = System.nanoTime();
				try {
					Instances dataset = dimensionalityReductionSelection
							.dimensionalityReductionSelector(Constants.ISOMAP, originalDataset, true, options);

					System.out.println(
							"Execution for:" + nDimensions + " dimensions with" + k + "k nearest neighbours in Isomap");
					Constants.logger.getLogger().log(Level.INFO, "Execution for:" + nDimensions + " dimensions with "
							+ k + "k nearest neighbours in Isomap\n");
					Constants.logger.getLogger().log(Level.INFO, Utils.printExecutionTime(start, System.nanoTime()));
					// here we save the new data in an arff file
					WekaFileConverter wekaFileConverterImpl = new WekaFileConverter();
					wekaFileConverterImpl.arffSaver(dataset,
							Constants.SRC_MAIN_RESOURCES_PATH + datasetName + WekaUtils.WEKA_SUFFIX);
				} catch (Exception e) {
					// nothing to do
				}
			}
		}
	}
}
