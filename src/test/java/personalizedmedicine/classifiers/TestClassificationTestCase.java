package personalizedmedicine.classifiers;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import personalizedmedicine.dimensionality_reduction_methods.DimensionalityReductionChooser;
import personalizedmedicine.helpful_classes.Constants;
import personalizedmedicine.utilpackage.WekaUtils;
import weka.classifiers.AbstractClassifier;
import weka.core.Instances;

import java.io.File;
import java.io.IOException;

class TestClassificationTestCase extends ClassifierTest {

    @Test
    void testNaiveBayes() throws IOException {
        File level2File = new File(datasetFileName);
        Instances originalDataset = WekaUtils.getOriginalData(level2File, className);
        AbstractClassifier classifier = WekaUtils.getClassifier(Constants.NAIVE_BAYES, originalDataset, new String[]{});
        WekaUtils.crossValidationAction(Constants.NAIVE_BAYES, classifier, numFolds, random);
    }

    @Test
    void testNaiveBayesUpdatable() throws IOException {
        File level2File = new File(datasetFileName);
        Instances originalDataset = WekaUtils.getOriginalData(level2File, className);
        AbstractClassifier classifier = WekaUtils.getClassifier(Constants.NAIVE_BAYES_UPDATABLE, originalDataset,
                                                                new String[]{});
        WekaUtils.crossValidationAction(Constants.NAIVE_BAYES_UPDATABLE, classifier, numFolds, random);
    }

    @Test
    void testZeroR() throws IOException {
        File level2File = new File(datasetFileName);
        Instances originalDataset = WekaUtils.getOriginalData(level2File, className);
        AbstractClassifier classifier = WekaUtils.getClassifier(Constants.ZERO_R, originalDataset, new String[]{});
        WekaUtils.crossValidationAction(Constants.ZERO_R, classifier, numFolds, random);
    }

    @Test
    void testJrip() throws IOException {
        File level2File = new File(datasetFileName);
        Instances originalDataset = WekaUtils.getOriginalData(level2File, className);
        AbstractClassifier classifier = WekaUtils.getClassifier(Constants.JRIP, originalDataset, new String[]{});
        WekaUtils.crossValidationAction(Constants.JRIP, classifier, numFolds, random);
    }

    @Test
    void testPART() throws IOException {
        File level2File = new File(datasetFileName);
        Instances originalDataset = WekaUtils.getOriginalData(level2File, className);
        AbstractClassifier classifier = WekaUtils.getClassifier(Constants.PART, originalDataset, new String[]{});
        WekaUtils.crossValidationAction(Constants.PART, classifier, numFolds, random);
    }

    @Test
    void testIBK() throws IOException {
        File level2File = new File(datasetFileName);
        Instances originalDataset = WekaUtils.getOriginalData(level2File, className);
        AbstractClassifier classifier = WekaUtils.getClassifier(Constants.IBK, originalDataset, new String[]{});
        WekaUtils.crossValidationAction(Constants.IBK, classifier, numFolds, random);
    }

    @Test
    @Disabled("deepLearning 4j needs packages on classpath")
    void testDeepLearning4j() throws IOException {
        File level2File = new File(datasetFileName);
        Instances originalDataset = WekaUtils.getOriginalData(level2File, className);
        AbstractClassifier classifier = WekaUtils.getClassifier(Constants.DEEPLEARNING4J, originalDataset,
                                                                new String[]{"1", "true", "5"});
        WekaUtils.crossValidationAction(Constants.DEEPLEARNING4J, classifier, numFolds, random);
    }

    @Test
    void testNaiveBayesCrossValidationEvaluationWithDimensionalityReduction() throws IOException {
        File level2File = new File(datasetFileName);
        Instances originalDataset = WekaUtils.getOriginalData(level2File, className);
        // DIMENSIONALITY REDUCTION
        DimensionalityReductionChooser dimensionalityReductionSelection = new DimensionalityReductionChooser();
        try {
            // http://weka.sourceforge.net/doc.dev/weka/filters/unsupervised/attribute/PrincipalComponents.html
            String[] options = weka.core.Utils.splitOptions("-R 0.95 -A 5 -M -1");
            Instances dataset = dimensionalityReductionSelection.dimensionalityReductionSelector("pca", originalDataset,
                                                                                                 true, options);
            // TODO CROSS VALIDATION
//			AbstractClassifier abstractClassifier = WekaUtils.getClassifier(Constants.NAIVE_BAYES, dataset,
//					new String[] {});
//			new NaiveBayesWeka().crossValidationEvaluation(abstractClassifier, originalDataset, 10,
//					new Random(1));
        } catch (Exception e) {
            Assertions.fail(e.getMessage());
        }
    }
}
