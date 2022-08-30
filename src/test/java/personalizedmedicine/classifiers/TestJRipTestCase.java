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
class TestJRipTestCase extends ClassifierTest {
    private final String[] usePruning = new String[]{" -P ", ""};
    private final String[] checkErrorRate = new String[]{" -E ", ""};

    @Test
    @DisplayName("Jrip Default")
    void testJripDefault() {
        // "-F 3 -N 2.0 -O 2 -S 1"
        try {
            File level2File = new File(datasetFileName);
            Instances originalDataset = WekaUtils.getOriginalData(level2File, className);
            AbstractClassifier classifier = WekaUtils.getClassifier(Constants.JRIP, originalDataset, new String[]{});
            WekaUtils.crossValidationAction(Constants.JRIP, classifier, numFolds, random);
        } catch (IOException e) {
            Assertions.fail(e.getMessage());
        }
    }

    @Test
    @DisplayName("Jrip All")
    void testJripAll() {
        // Default: "-F 3 -N 2.0 -O 2 -S 1"
        try {
            int count = 0;
            File level2File = new File(datasetFileName);
            Instances originalDataset = WekaUtils.getOriginalData(level2File, className);
            for (int optimization = 1; optimization <= 10; optimization++) {
                for (int folds = 1; folds <= 20; folds++) {
                    for (double mWeight = 0.5; mWeight <= 10; mWeight += 0.5) {
                        for (String prune : usePruning) {
                            for (String errorRate : checkErrorRate) {
                                StringJoiner joiner = new StringJoiner(" ");
                                String options = joiner.add("-O")
                                                       .add(String.valueOf(optimization))
                                                       .add("-F")
                                                       .add(String.valueOf(folds))
                                                       .add("-N")
                                                       .add(String.valueOf(mWeight))
                                                       .add(String.valueOf(prune))
                                                       .add(String.valueOf(errorRate))
                                                       .toString();
                                count++;
                                log.info("SAVE TEST INFO NAME: " + "Configuration_" + count);
                                AbstractClassifier classifier = WekaUtils.getClassifier(Constants.JRIP, originalDataset,
                                                                                        weka.core.Utils.splitOptions(
                                                                                                options));
                                WekaUtils.crossValidationAction(Constants.JRIP, classifier, numFolds, random);
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
