package personalizedmedicine.classifiers;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestInfo;
import personalizedmedicine.helpful_classes.Constants;

/**
 * @author Nikiforos Xylogiannopoulos
 */
@Slf4j
abstract class ClassifierTest {
    final String className = Constants.classNameForReducedData;
    final String datasetFileName = Constants.dataset20EMPCAFileName;
    final int numFolds = 10;
    final int random = 1;

    @BeforeEach
    void init(TestInfo testInfo) {
        log.info("START TEST");
//		logger.getLogger().log(Level.INFO, "SAVE FILE NAME: " + datasetFileName);
//		logger.getLogger().log(Level.INFO, "SAVE DISPLAY NAME: " + testInfo.getDisplayName());
    }

    @AfterEach
    void onEnd() {
        log.info("END TEST");
    }
}
