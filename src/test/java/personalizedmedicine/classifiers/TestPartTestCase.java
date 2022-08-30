package personalizedmedicine.classifiers;

import com.google.common.base.Strings;
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
import java.util.StringJoiner;

@Slf4j
class TestPartTestCase extends ClassifierTest {

    private final String[] doNotMakeSplitPointActualValue = new String[]{" -doNotMakeSplitPointActualValue ", ""};

    private final String[] useMDLCorrection = new String[]{" -J ", ""};

    private final String[] unpruned = new String[]{" -U ", ""};

    private final String[] binarySplits = new String[]{" -B ", ""};

    private final String[] reducedErrorPruning = new String[]{" -R ", ""};

    @Test
    @DisplayName("Part Default")
    void testPartDefault() {
        // -M 2 -C 0.25 -Q 1
        try {
            File level2File = new File(datasetFileName);
            Instances originalDataset = WekaUtils.getOriginalData(level2File, className);
            AbstractClassifier classifier = WekaUtils.getClassifier(Constants.PART, originalDataset, new String[]{});
            WekaUtils.crossValidationAction(Constants.PART, classifier, numFolds, random);
        } catch (IOException e) {
            Assertions.fail(e.getMessage());
        }
    }

    @Test
    @DisplayName("Part All")
    void testPartAll() {
        // -M 2 -C 0.25 -Q 1
        try {
            File level2File = new File(datasetFileName);
            Instances originalDataset = WekaUtils.getOriginalData(level2File, className);
            int count = 0;
            // confidenceFactor must be greater than 0 and less than 1
            for (double confidenceFactor = 0.1; confidenceFactor <= 0.5; confidenceFactor += 0.05) { // -C
                for (int minNumObj = 1; minNumObj <= 20; minNumObj++) { // -M
                    // numFolds must be greater than 1
                    for (int numFolds = 2; numFolds <= 20; numFolds++) { // -N
                        for (String noSplitPointValue : doNotMakeSplitPointActualValue) {
                            for (String mdl : useMDLCorrection) {
                                for (String pruningIsPerformed : unpruned) {
                                    for (String bSplit : binarySplits) {
                                        for (String reducedPrune : reducedErrorPruning) {
                                            StringJoiner joiner = new StringJoiner(" ");
                                            joiner.add(reducedPrune)
                                                  .add(bSplit)
                                                  .add(pruningIsPerformed)
                                                  .add(mdl)
                                                  .add(noSplitPointValue)
                                                  .add("-M")
                                                  .add(String.valueOf(minNumObj));
                                            // -C cannot be at the same time with -R
                                            if (Strings.isNullOrEmpty(reducedPrune)) {
                                                joiner.add("-C").add(String.valueOf(confidenceFactor));
                                            }
                                            else {
                                                // Setting the number of folds does only make sense for reduced
                                                // error pruning.
                                                joiner.add("-N").add(String.valueOf(numFolds));
                                            }
                                            log.info("SAVE TEST INFO NAME: " + "Configuration_" + count);
                                            String[] options = weka.core.Utils.splitOptions(joiner.toString());
                                            AbstractClassifier classifier = WekaUtils.getClassifier(Constants.PART,
                                                                                                    originalDataset,
                                                                                                    options);
                                            WekaUtils.crossValidationAction(Constants.PART, classifier, numFolds,
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
