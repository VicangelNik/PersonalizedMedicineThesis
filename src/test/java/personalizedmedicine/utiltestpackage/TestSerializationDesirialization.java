package personalizedmedicine.utiltestpackage;

import java.io.IOException;
import java.nio.file.Paths;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Test;

import personalizedmedicine.helpful_classes.Constants;
import personalizedmedicine.utilpackage.Utils;
import personalizedmedicine.utilpackage.WekaUtils;

/**
 * The Class TestSerializationDesirialization.
 */
public class TestSerializationDesirialization {

	/** The Constant filePath. */
	final static String filePath = Constants.SRC_TEST_RESOURCES_PATH + "test.txt";

	/**
	 * Test simple path test case.
	 */
	@Test
	public void testSimplePathTestCase() {
		try {
			String[] obj = { "test" };
			WekaUtils.serializeObject(filePath, obj, true);
			Object[] obj1 = WekaUtils.desirializeObject(filePath, true);
			Assert.assertEquals(obj[0], obj1[0]);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * clear resources.
	 */
	@AfterClass
	public static void clearResources() {
		// remove all the files of the directory but not the files in subdirectories
		Utils.removeFiles(Constants.SRC_TEST_RESOURCES_PATH);
		try {
			Assert.assertFalse(Utils.isDirEmpty(Paths.get(Constants.SRC_TEST_RESOURCES_PATH)));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
