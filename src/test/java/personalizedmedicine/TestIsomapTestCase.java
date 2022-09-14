package personalizedmedicine;

import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import personalizedmedicine.dimensionality_reduction_methods.DimensionalityReductionChooser;
import personalizedmedicine.utilpackage.TransformToFromWeka;
import personalizedmedicine.utilpackage.Utils;
import personalizedmedicine.utilpackage.WekaUtils;
import personalizedmedicine.weka.api.library.WekaFileConverter;
import smile.manifold.IsoMap;
import weka.core.Instances;

import java.io.File;
import java.io.IOException;

import static personalizedmedicine.helpful_classes.Constants.*;

@Slf4j
class TestIsomapTestCase {

    @Test
    void TestIsomapCase() throws IOException {
        val level2File = new File(completeFileName);
        Instances originalDataset = WekaUtils.getOriginalData(level2File, classRealName);
        final int dimensions = 10;
        final int kNearest = 5;
        final boolean cIsomap = false;
        final double[][] data = TransformToFromWeka.transformWekaToManifolds(originalDataset);
        Assertions.assertTrue(data.length > 0, "Data should not be null or empty");
        val myIsomap = IsoMap.of(data, dimensions, kNearest, cIsomap);
        Assertions.assertTrue(myIsomap.coordinates != null && myIsomap.coordinates.length > 0,
                              "Isomap results should not be null or empty");
        final Instances reData = TransformToFromWeka.manifoldsToWeka(myIsomap.coordinates, "isomapDataset",
                                                                     WekaUtils.getDatasetClassValues(originalDataset),
                                                                     classNameForReducedData);
        // here we save the new data in an arff file
        //  val wekaFileConverterImpl = new WekaFileConverter();
        //  wekaFileConverterImpl.arffSaver(reData, Constants.SRC_MAIN_RESOURCES_PATH + "isomapData.arff");
        // TODO CROSS VALIDATION
        //  AbstractClassifier abstractClassifier = WekaUtils.getClassifier(Constants.NAIVE_BAYES, reData, new String[] {});
        //  new NaiveBayesWeka().crossValidationEvaluation(abstractClassifier, reData, 10, new Random(1));
        // new NaiveBayesImplementation().classify(abstractClassifier, originalDataset);
        Assertions.assertFalse(myIsomap.graph.getEdges().isEmpty(), "Graph should not be empty");
        Assertions.assertTrue(myIsomap.index.length > 0, "Original indices should exist.");
    }

    /**
     * Do isomap. Same as TestIsomapCase but it is called from
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
        val level2File = new File(completeFileName);
        final Instances originalDataset = WekaUtils.getOriginalData(level2File, classRealName);
        final String[] nDimensionsArray = {"10", "20", "50", "100", "500", "1000"};
        double value = Math.pow(originalDataset.numAttributes() / 2, 0.5);
        final String ruleOfThumb = String.valueOf((int) value);
        final String[] kArray = {"5", "10", "25", "100", ruleOfThumb, "250"};
        val dimensionalityReductionSelection = new DimensionalityReductionChooser();
        for (String nDimensions : nDimensionsArray) {
            for (String k : kArray) {
                String datasetName = nDimensions + "dimesions_" + k + " nearestNeighbours_isomapData";
                String[] options = {nDimensions, k, "false", datasetName, classNameForReducedData};
                // Get current time
                final long start = System.nanoTime();
                try {
                    Instances dataset = dimensionalityReductionSelection.dimensionalityReductionSelector(ISOMAP,
                                                                                                         originalDataset,
                                                                                                         true, options);
                    log.info(
                            "Execution for:" + nDimensions + " dimensions with" + k + "k nearest neighbours in Isomap");
                    log.info("Execution for:" + nDimensions + " dimensions with " + k +
                             "k nearest neighbours in Isomap\n");
                    log.info(Utils.printExecutionTime(start, System.nanoTime()));
                    // here we save the new data in an arff file
                    val wekaFileConverterImpl = new WekaFileConverter();
                    wekaFileConverterImpl.arffSaver(dataset,
                                                    SRC_MAIN_RESOURCES_PATH + datasetName + WekaUtils.WEKA_SUFFIX);
                } catch (Exception e) {
                    Assertions.fail(e);
                }
            }
        }
    }
}
