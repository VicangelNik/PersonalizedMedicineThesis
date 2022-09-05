package personalizedmedicine.tests;

import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import personalizedmedicine.utilpackage.WekaUtils;
import weka.core.Instances;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static personalizedmedicine.helpful_classes.Constants.*;

/**
 * The Class TestIsoDataPythonMapTestCase. This test case class is created for
 * JUnit 5. It tests whether the files are created correctly from python and it
 * converts them in arff files for better manipulation and in weka. This does
 * not mean that weka can not manipulate with the same way csv files.
 */
@Slf4j
class TestIsoDataPythonMapTestCase {

    private final Map<String, Integer> fileNameNumAttributesMap = new HashMap<String, Integer>() {
        {
            put(dataset10IsomapCsvFileName, 11);
            put(dataset20IsomapCsvFileName, 21);
            put(dataset50IsomapCsvFileName, 51);
            put(dataset100IsomapCsvFileName, 101);

        }
    };

    @Test
    @DisplayName("Test isomap files which created by python code")
    void testDataCreatedFromPython() {
        fileNameNumAttributesMap.forEach((fileName, expectedNumAttributes) -> {
            val level2File = new File(fileName);
            Instances data = null;
            try {
                data = WekaUtils.getOriginalData(level2File, classRealName);
            } catch (IOException e) {
                log.error(e.getMessage());
            }
            Assertions.assertNotNull(data);
            Assertions.assertEquals(expectedNumAttributes.intValue(), data.numAttributes(), "The number of attributes");
            Assertions.assertEquals(335, data.numInstances(), "The number of instances");
            Assertions.assertEquals(2, data.numClasses(), "The number of classes(NormalTissue, PrimaryTumor)");
        });
    }
}
