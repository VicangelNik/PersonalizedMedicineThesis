package test.weka.api;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import com.opencsv.CSVReader;

import helpful_classes.Constants;
import helpful_classes.EnumSeparators;
import weka.api.library.LoadArff;
import weka.api.library.PreprocessDataImpl;
import weka.api.library.WekaFileConverterImpl;
import weka.core.Attribute;
import weka.core.Instances;

// TODO: Auto-generated Javadoc
/**
 * The Class TestPreprocess.
 */
public class TestPreprocess {

	/**
	 * Test preprocess level one.
	 */
	@Test
	public void testPreprocessLevelOne() {
		// MAKE CSV VALID
		String newCsvName = Constants.SRC_MAIN_RESOURCES_PATH + "PatientAndC�ntrolProcessedLevelOne.csv";
		try (CSVReader csvReader = new CSVReader(new FileReader(Constants.C_WORK_CSV_FILE));) {
			String[] values = null;
			try (BufferedWriter bw = new BufferedWriter(new FileWriter(newCsvName))) {
				String line1 = Arrays.toString(csvReader.readNext()).replaceAll("[\\[\\]]", "");
				bw.write(line1);
				bw.write(System.lineSeparator());
				while ((values = csvReader.readNext()) != null) {
					// the csv file had unnecessary commas, spaces, tabs and brackets and we make it
					// to be seperated only by tabs.
					String line = Arrays.toString(values).replaceAll("[\\[\\]]", "").replaceAll("na", "NA")
							.replaceAll("--", "NA");
					List<String> list = new ArrayList<>(Arrays.asList(line.split(EnumSeparators.TAB.getSeparator())));
					bw.write(modifyValues(list));
					bw.write(System.lineSeparator());
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
			Assert.fail(e.getMessage());
		}
		// ASSERT
		File file = new File(newCsvName);
		Assert.assertTrue("The file should exist", file.exists());
		Assert.assertTrue("The file should be read", file.canRead());
	}

	/**
	 * Test preprocess level 2.
	 *
	 * @throws Exception the exception
	 */
	@Test
	public void testPreprocessLevel2() throws Exception {
		// TEST CONVERTER TEST CASE MUST BE RAN BEFORE THIS TO RUN.
		// REMOVE INVALID ATTRIBUTES
		File arffFile = new File(Constants.SRC_MAIN_RESOURCES_PATH + "PatientAndC�ntrolProcessedLevelOne.arff");
		File level2File = new File(Constants.SRC_MAIN_RESOURCES_PATH + "PatientAndC�ntrolProcessedLevelTwo.arff");
		Assert.assertTrue("The file should exists", arffFile.exists());
		Assert.assertTrue("The file should be readable.", arffFile.canRead());
		LoadArff arffLoader = new LoadArff(arffFile, 73664);
		Assert.assertEquals("The excpected class should be: ", 73664, arffLoader.getStructure().classIndex());
		Assert.assertTrue("The arf file should have string attributes",
				arffLoader.getStructure().checkForStringAttributes());
		PreprocessDataImpl preprocessData = new PreprocessDataImpl();
		Instances data = null;
		// Attributes of String Type contain only NA. Also, they are not accepted by
		// NaiveBayes.
		if (arffLoader.getStructure().checkForStringAttributes()) {
			data = preprocessData.removeFeaturesByType(arffLoader.getDataSet(), Attribute.STRING);
		}
		// DATA attribute has no information and we remove it. Vital_status and
		// Days_To_Death attribute are very connected with SampleStatus attribute so are
		// no informative. We could do classification with each of the two.
		// ASSERTS
		Assert.assertTrue("Attribute DATA should exist in dataset", data.attribute("Vital_status") != null);
		Assert.assertTrue("Attribute DATA should exist in dataset", data.attribute("Days_To_Death") != null);
		Assert.assertTrue("Attribute DATA should exist in dataset", data.attribute("DATA") != null);
		// CODE
		int vitalStatusIndex = data.attribute("Vital_status").index() + 1;
		int daysToDeathIndex = data.attribute("Days_To_Death").index() + 1;
		String rangeList = "First," + vitalStatusIndex + "," + daysToDeathIndex;
		data = preprocessData.removeFeature(data, rangeList);
		// ASSERTS
		Assert.assertFalse("Attribute DATA should not exist in dataset", data.attribute("DATA") != null);
		Assert.assertFalse("Attribute Vital_status should not exist in dataset",
				data.attribute("Vital_status") != null);
		Assert.assertFalse("Attribute Days_To_Death should not exist in dataset",
				data.attribute("Days_To_Death") != null);
		WekaFileConverterImpl wekaFileConverterImpl = new WekaFileConverterImpl();
		wekaFileConverterImpl.arffSaver(data, level2File.getAbsolutePath());
	}

	/**
	 * Modify values.
	 *
	 * @param list the list
	 * @return the string
	 */
	private static String modifyValues(List<String> list) {
		StringBuilder builder = new StringBuilder();
		for (int i = 0; i < list.size() - 1; i++) {
			String value = list.get(i);
			builder.append(value.trim() + EnumSeparators.TAB.getSeparator());
		}
		String lastValue = list.get(list.size() - 1);
		builder.append(lastValue.trim());
		return builder.toString();
	}
}