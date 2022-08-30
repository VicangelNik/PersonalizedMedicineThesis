package personalizedmedicine.classifiers;

import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.junit.jupiter.api.Test;
import personalizedmedicine.helpful_classes.Constants;
import personalizedmedicine.utilpackage.WekaUtils;
import weka.classifiers.AbstractClassifier;
import weka.core.Instances;

import java.io.File;
import java.io.IOException;

@Slf4j
class TestZeroRTestCase extends ClassifierTest {

    @Test
    void testZeroR() throws IOException {
        val level2File = new File(datasetFileName);
        final Instances originalDataset = WekaUtils.getOriginalData(level2File, className);
        final AbstractClassifier classifier = WekaUtils.getClassifier(Constants.ZERO_R, originalDataset,
                                                                      new String[]{});
        WekaUtils.crossValidationAction(Constants.ZERO_R, classifier, numFolds, random);
    }
}
