package personalizedmedicine;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import personalizedmedicine.dimensionality_reduction_methods.DimensionalityReductionChooser;
import personalizedmedicine.helpful_classes.Constants;
import personalizedmedicine.utilpackage.TransformToFromWeka;
import personalizedmedicine.utilpackage.Utils;
import personalizedmedicine.utilpackage.WekaUtils;
import personalizedmedicine.weka.api.library.WekaFileConverter;
import smile.graph.Graph;
import smile.manifold.IsoMap;
import weka.core.Instances;

import java.io.File;
import java.io.IOException;

@Slf4j
class TestIsomapTestCase {

    @Test
    void TestIsomapTetCase() throws IOException {
        File level2File = new File(Constants.SRC_MAIN_RESOURCES_PATH + "PatientAndControlProcessedLevelTwo.arff");
        Instances originalDataset = WekaUtils.getOriginalData(level2File, "SampleStatus");
        int dimensions = 10;
        int kNearest = 5;
        boolean cIsomap = false;
        double data[][] = TransformToFromWeka.transformWekaToManifolds(originalDataset);
        Assertions.assertTrue(data != null && data.length > 0, "Data should not be null or empty");
        IsoMap myIsomap = new IsoMap(data, dimensions, kNearest, cIsomap);
        double[][] coordinates = myIsomap.getCoordinates();
        Assertions.assertTrue(coordinates != null && coordinates.length > 0,
                              "Isomap results should not be null or empty");
        Instances reData = TransformToFromWeka.manifoldsToWeka(coordinates, "isomapDataset",
                                                               WekaUtils.getDatasetClassValues(originalDataset),
                                                               "class");
        // here we save the new data in an arff file
        WekaFileConverter wekaFileConverterImpl = new WekaFileConverter();
        wekaFileConverterImpl.arffSaver(reData, Constants.SRC_MAIN_RESOURCES_PATH + "isomapData.arff");
        // TODO CROSS VALIDATION
//		AbstractClassifier abstractClassifier = WekaUtils.getClassifier(Constants.NAIVE_BAYES, reData, new String[] {});
//		new NaiveBayesWeka().crossValidationEvaluation(abstractClassifier, reData, 10, new Random(1));
        // new NaiveBayesImplementation().classify(abstractClassifier, originalDataset);
        Graph graph = myIsomap.getNearestNeighborGraph();
        int[] indexes = myIsomap.getIndex();
        Assertions.assertFalse(graph.getEdges().isEmpty(), "Graph should not be empty");
        Assertions.assertTrue(indexes.length > 0, "Original indices should exist.");
    }

    /**
     * Test isomap data. TestIsomapTetCase should have been run first to create arff
     * file.
     *
     * @throws IOException Signals that an I/O exception has occurred.
     */
    @Test
    void testIsomapData() throws IOException {
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
    void doIsomap() throws IOException {
        // first option should be the number of expected dimensions
        // second option should be the k nearest neighbors
        // third option should be whether C-Isomap or standard algorithm
        // the last 2 options will be always the dataset name and the name of the class
        File level2File = new File(Constants.SRC_MAIN_RESOURCES_PATH + "PatientAndControlProcessedLevelTwo.arff");
        Instances originalDataset = WekaUtils.getOriginalData(level2File, "SampleStatus");
        String[] nDimensionsArray = {"10", "20", "50", "100", "500", "1000"};
        String className = "class";
        double value = Math.pow(originalDataset.numAttributes() / 2, 0.5);
        String ruleOfThumb = String.valueOf((int) value);
        String[] kArray = {"5", "10", "25", "100", ruleOfThumb, "250"};
        DimensionalityReductionChooser dimensionalityReductionSelection = new DimensionalityReductionChooser();
        for (String nDimensions : nDimensionsArray) {
            for (String k : kArray) {
                String datasetName = nDimensions + "dimesions_" + k + " nearestNeighbours_isomapData";
                String[] options = {nDimensions, k, "false", datasetName, className};
                // Get current time
                long start = System.nanoTime();
                try {
                    Instances dataset = dimensionalityReductionSelection.dimensionalityReductionSelector(
                            Constants.ISOMAP, originalDataset, true, options);

                    log.info(
                            "Execution for:" + nDimensions + " dimensions with" + k + "k nearest neighbours in Isomap");
                    log.info("Execution for:" + nDimensions + " dimensions with " + k +
                             "k nearest neighbours in Isomap\n");
                    log.info(Utils.printExecutionTime(start, System.nanoTime()));
                    // here we save the new data in an arff file
                    WekaFileConverter wekaFileConverterImpl = new WekaFileConverter();
                    wekaFileConverterImpl.arffSaver(dataset, Constants.SRC_MAIN_RESOURCES_PATH + datasetName +
                                                             WekaUtils.WEKA_SUFFIX);
                } catch (Exception e) {
                    Assertions.fail(e);
                }
            }
        }
    }
}
