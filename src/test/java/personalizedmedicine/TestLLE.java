package personalizedmedicine;

import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import personalizedmedicine.dimensionality_reduction_methods.DimensionalityReductionChooser;
import personalizedmedicine.helpful_classes.Constants;
import personalizedmedicine.utilpackage.TransformToFromWeka;
import personalizedmedicine.utilpackage.WekaUtils;
import personalizedmedicine.weka.api.library.WekaFileConverter;
import smile.graph.Graph;
import smile.manifold.LLE;
import weka.core.Instances;

import java.io.File;
import java.io.IOException;

import static personalizedmedicine.helpful_classes.Constants.*;

@Slf4j
class TestLLE {

    // IS NOT WORKING

    /**
     * Test lle tet case.
     *
     * @throws IOException Signals that an I/O exception has occurred.
     */
    @Test
    void TestLLECase() throws IOException {
        val level2File = new File(completeFileName);
        final Instances originalDataset = WekaUtils.getOriginalData(level2File, classRealName);
        final double[][] data = TransformToFromWeka.transformWekaToManifolds(originalDataset);
        Assertions.assertTrue(data.length > 0, "Data should not be null or empty");
        for (int dimensions = 10; dimensions < 201; dimensions += 10) {
            for (int kNearest = 5; kNearest < 500; kNearest += 5) {
                try {
                    val lle = new LLE(data, dimensions, kNearest);
                    final double[][] coordinates = lle.getCoordinates();
                    Assertions.assertTrue(coordinates != null && coordinates.length > 0,
                                          "LLE results should not be null or empty");
                    final Instances reData = TransformToFromWeka.manifoldsToWeka(coordinates, "lleDataset",
                                                                                 WekaUtils.getDatasetClassValues(
                                                                                         originalDataset),
                                                                                 classNameForReducedData);
                    // here we save the new data in an arff file
                    val wekaFileConverterImpl = new WekaFileConverter();
                    wekaFileConverterImpl.arffSaver(reData,
                                                    SRC_MAIN_RESOURCES_PATH + LLE + "\\" + "k_" + kNearest + "d_" +
                                                    dimensions + "lleData.arff");
                    // CROSS VALIDATION
                    //	AbstractClassifier abstractClassifier = WekaUtils.getClassifier(Constants.NAIVE_BAYES, reData);
                    //	new NaiveBayesImplementation().crossValidationEvaluation(abstractClassifier, reData, 10, new Random(1));
                    // new NaiveBayesImplementation().classify(abstractClassifier, originalDataset);
                    final Graph graph = lle.getNearestNeighborGraph();
                    final int[] indexes = lle.getIndex();
                    Assertions.assertFalse(graph.getEdges().isEmpty(), "Graph should not be empty");
                    Assertions.assertTrue(indexes.length > 0, "Original indices should exist.");
                } catch (Exception e) {
                    log.error(e.getMessage());
                }
            }
        }
    }

    /**
     * Do lle. Same as TestLLECase but it is called from
     * DimensionalityReductionSelector
     *
     * @throws IOException Signals that an I/O exception has occurred.
     */
    @Test
    void doLLE() throws IOException {
        // first option should be the number of expected dimensions
        // second option should be the k nearest neighbors
        val level2File = new File(completeFileName);
        final Instances originalDataset = WekaUtils.getOriginalData(level2File, classRealName);
        final String[] options = {"10", "5"};
        val dimensionalityReductionSelection = new DimensionalityReductionChooser();
        final Instances dataset = dimensionalityReductionSelection.dimensionalityReductionSelector(Constants.LLE,
                                                                                                   originalDataset,
                                                                                                   true, options);
        // TODO CROSS VALIDATION
        //	AbstractClassifier abstractClassifier = WekaUtils.getClassifier(Constants.NAIVE_BAYES, dataset, new String[] {});
        //	new NaiveBayesWeka().crossValidationEvaluation(abstractClassifier, dataset, 10, new Random(1));
    }
}
