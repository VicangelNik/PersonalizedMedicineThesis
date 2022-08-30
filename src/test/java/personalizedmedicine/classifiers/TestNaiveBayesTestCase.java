package personalizedmedicine.classifiers;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import personalizedmedicine.helpful_classes.Constants;
import personalizedmedicine.utilpackage.WekaUtils;
import weka.classifiers.AbstractClassifier;
import weka.core.Instances;

import java.io.File;
import java.io.IOException;

@Slf4j
class TestNaiveBayesTestCase extends ClassifierTest {

    @Test
    @DisplayName("Naive Bayes Default")
    void testNBDefault() {
        try {
            File level2File = new File(datasetFileName);
            Instances originalDataset = WekaUtils.getOriginalData(level2File, className);
            String[] options = new String[]{};
            AbstractClassifier classifier = WekaUtils.getClassifier(Constants.NAIVE_BAYES, originalDataset, options);
            WekaUtils.crossValidationAction(Constants.NAIVE_BAYES, classifier, numFolds, random);
        } catch (IOException e) {
            Assertions.fail(e.getMessage());
        }
    }

    /**
     * Test NB updatable deafult.
     */
//	@Test
//	@DisplayName("Naive Bayes Updatable Default")
//	public void testNBUpdatableDeafult() {
//		try {
//			File level2File = new File(datasetFileName);
//			Instances originalDataset = WekaUtils.getOriginalData(level2File, className);
//			String[] options = new String[] {};
//			AbstractClassifier classifier = WekaUtils.getClassifier(Constants.NAIVE_BAYES_UPDATABLE, originalDataset,
//					options);
//			WekaUtils.crossValidationAction(Constants.NAIVE_BAYES_UPDATABLE, classifier, numFolds, random);
//		} catch (IOException e) {
//			Assert.assertFalse(e.getMessage(), true);
//		}
//	}
    @Test
    @DisplayName("Naive Bayes Kernel Estimator")
    void testNBKernelEstimator() {
        try {
            File level2File = new File(datasetFileName);
            Instances originalDataset = WekaUtils.getOriginalData(level2File, className);
            // Use kernel density estimator rather than normal distribution for numeric
            // attributes
            String[] options = new String[]{"-K"};
            AbstractClassifier classifier = WekaUtils.getClassifier(Constants.NAIVE_BAYES, originalDataset, options);
            WekaUtils.crossValidationAction(Constants.NAIVE_BAYES, classifier, numFolds, random);
        } catch (IOException e) {
            Assertions.fail(e.getMessage());
        }
    }

    /**
     * Test NB updatable kernel estimator.
     */
//	@Test
//	@DisplayName("Naive Bayes Updatable Kernel Estimator")
//	public void testNBUpdatableKernelEstimator() {
//		try {
//			File level2File = new File(datasetFileName);
//			Instances originalDataset = WekaUtils.getOriginalData(level2File, className);
//			// Use kernel density estimator rather than normal distribution for numeric
//			// attributes
//			String[] options = new String[] { "-K" };
//			AbstractClassifier classifier = WekaUtils.getClassifier(Constants.NAIVE_BAYES_UPDATABLE, originalDataset,
//					options);
//			WekaUtils.crossValidationAction(Constants.NAIVE_BAYES_UPDATABLE, classifier, numFolds, random);
//		} catch (IOException e) {
//			Assert.assertFalse(e.getMessage(), true);
//		}
//	}
    @Test
    @DisplayName("Naive Bayes Supervised Discretization")
    void testNBSupervisedDiscretization() {
        try {
            File level2File = new File(datasetFileName);
            Instances originalDataset = WekaUtils.getOriginalData(level2File, className);
            String[] options = new String[]{"-D"};
            AbstractClassifier classifier = WekaUtils.getClassifier(Constants.NAIVE_BAYES, originalDataset, options);
            WekaUtils.crossValidationAction(Constants.NAIVE_BAYES, classifier, numFolds, random);
        } catch (IOException e) {
            Assertions.fail(e.getMessage());
        }
    }

    @Test
    @DisplayName("Naive Bayes All")
    void testNBAll() {
        try {
            File level2File = new File(datasetFileName);
            Instances originalDataset = WekaUtils.getOriginalData(level2File, className);
            String[] configs = new String[]{"", "-K", "-D"};
            for (String option : configs) {
                String[] options = new String[]{option};
                AbstractClassifier classifier = WekaUtils.getClassifier(Constants.NAIVE_BAYES, originalDataset,
                                                                        options);
                WekaUtils.crossValidationAction(Constants.NAIVE_BAYES, classifier, numFolds, random);
            }
        } catch (IOException e) {
            Assertions.fail(e.getMessage());
        }
    }
}
