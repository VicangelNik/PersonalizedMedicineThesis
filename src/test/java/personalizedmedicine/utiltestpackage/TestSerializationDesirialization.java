package personalizedmedicine.utiltestpackage;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import personalizedmedicine.utilpackage.Utils;
import personalizedmedicine.utilpackage.WekaUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static personalizedmedicine.helpful_classes.Constants.TESTS_RESULTS_FOLDER_PATH;

public class TestSerializationDesirialization {
    final static String filePath = TESTS_RESULTS_FOLDER_PATH + "test.txt";

    @Test
    void testSimplePathTestCase() {
        try {
            String[] obj = {"test"};
            WekaUtils.serializeObject(filePath, obj, true);
            Object[] obj1 = WekaUtils.deserializeObject(filePath, true);
            Assertions.assertEquals(obj[0], obj1[0]);
        } catch (Exception e) {
            Assertions.fail(e);
        }
    }

    @AfterAll
    public static void clearResources() {
        try {
            Files.delete(Paths.get(filePath));
            Assertions.assertTrue(Utils.isDirEmpty(Paths.get(TESTS_RESULTS_FOLDER_PATH)));
        } catch (IOException e) {
            Assertions.fail(e);
        }
    }
}
