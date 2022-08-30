package personalizedmedicine.classifiers;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
import personalizedmedicine.helpful_classes.Constants;
import personalizedmedicine.utilpackage.WekaUtils;
import weka.classifiers.AbstractClassifier;
import weka.core.Instances;

import java.io.File;
import java.io.IOException;
import java.util.StringJoiner;

@Slf4j
class TestIBKTestCase extends ClassifierTest {

    private final String[] crossValidate = new String[]{" -X", ""}; // creates problems when is set
    private final String[] distanceWeighting = new String[]{" -I", " -F", ""};
    private final String[] meanSquaredError = new String[]{"-E", ""};
    private final String[] nearestNeighbourSearchAlgorithm = new String[]{"weka.core.neighboursearch.LinearNNSearch "}; // -A
    /**
     * NearestNeighbourSearchAlgorithm attributes
     */
    private final String[] measurePerformance = new String[]{" -P ", ""};
    private final String[] skipIdentical = new String[]{" -S ", ""};
    private final String[] distanceFunction = new String[]{"weka.core.EuclideanDistance -R first-last ", "weka.core.ManhattanDistance -R first-last ", "weka.core.ChebyshevDistance -R first-last "}; // -A
    /**
     * distanceFunction attributes
     */
    private final String[] dontNormalize = new String[]{"-D", ""};

    @Test
    @DisplayName("IBK Default")
    void testIBKDefault() {
        // weka.classifiers.lazy.IBk -K 1 -W 0 -A
        // "weka.core.neighboursearch.LinearNNSearch -A \"weka.core.EuclideanDistance -R
        // first-last\""
        // for (String datasetFileName : fileNames) {
        try {
            File level2File = new File(datasetFileName);
            Instances originalDataset = WekaUtils.getOriginalData(level2File, className);
            AbstractClassifier classifier = WekaUtils.getClassifier(Constants.IBK, originalDataset,
                                                                    new String[]{"-output-debug-info"});
            WekaUtils.crossValidationAction(Constants.IBK, classifier, numFolds, random);
        } catch (IOException e) {
            Assertions.fail(e.getMessage());
        }
        // }
    }

    @Test
    @DisplayName("IBK ALL")
    void testIBKALL() {
        // weka.classifiers.lazy.IBk -K 1 -W 0 -A
        // "weka.core.neighboursearch.LinearNNSearch -A \"weka.core.EuclideanDistance -R
        // first-last\""
        try {
            File level2File = new File(datasetFileName);
            Instances originalDataset = WekaUtils.getOriginalData(level2File, className);
            for (String nNAlgorithm : nearestNeighbourSearchAlgorithm) {
                for (String sIdentical : skipIdentical) {
                    for (String dFunction : distanceFunction) {
                        for (String dNormalize : dontNormalize) {
                            for (int window = 0; window <= 50; window++) // -W
                            {
                                // knn must be > 0
                                for (int knn = 1; knn <= 10; knn++) // -K
                                {
                                    for (String dWeight : distanceWeighting) {
                                        // for (String cross : crossValidate) { // creates problems when is set
                                        for (String error : meanSquaredError) {
                                            StringJoiner joiner = new StringJoiner(" ");
                                            joiner.add(error)
                                                  .add(dWeight)
                                                  .add("-K") // .add(cross)
                                                  .add(String.valueOf(knn))
                                                  .add("-W")
                                                  .add(String.valueOf(window))
                                                  .add("-A")
                                                  .add("\"")
                                                  .add(nNAlgorithm)
                                                  .add(sIdentical)
                                                  .add("-A")
                                                  .add("\\\"")
                                                  .add(dFunction)
                                                  .add(dNormalize)
                                                  .add("\\\"")
                                                  .add("\"");
                                            String[] options = weka.core.Utils.splitOptions(joiner.toString());
                                            AbstractClassifier classifier = WekaUtils.getClassifier(Constants.IBK,
                                                                                                    originalDataset,
                                                                                                    options);
                                            WekaUtils.crossValidationAction(Constants.IBK, classifier, numFolds,
                                                                            random);
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            Assertions.fail(e.getMessage());
        }
    }
}
