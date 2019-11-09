package test.weka.api;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import org.apache.commons.lang3.math.NumberUtils;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import com.opencsv.CSVReader;

import helpful_classes.ClassifierSelectionImpl;
import helpful_classes.Constants;
import helpful_classes.EnumSeparators;
import interfaces.ClassifierSelection;
import weka.api.library.LoadArff;
import weka.classifiers.AbstractClassifier;

// TODO: Auto-generated Javadoc
/**
 * The Class TestClassificationTestCase.
 */
public class TestClassificationTestCase {

	/**
	 * Test naive bayes classification arff.
	 */
	@Ignore
	@Test
	public void testNaiveBayesClassificationArff() {
		File file = new File(Constants.WEKA_FILES + "iris.arff");
		try {
			LoadArff loader = new LoadArff(file, 4);
			Assert.assertEquals("The excpected class should be: ", 4, loader.getStructure().classIndex());
			ClassifierSelection classifierSelection = new ClassifierSelectionImpl();
			AbstractClassifier classifier = classifierSelection.selectClassifier(Constants.NAIVE_BAYES, loader);
			System.out.println(classifier.getCapabilities());
			System.out.println(classifier);
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail(e.getMessage());
		}
	}

	/**
	 * Test naive bayes classification arff real data.
	 *
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	@Test
	public void testNaiveBayesClassificationArffRealData() throws IOException {
		File level2File = new File(Constants.SRC_MAIN_RESOURCES_PATH + "PatientAndCïntrolProcessedLevelTwo.arff");
		Assert.assertTrue("The file should exists", level2File.exists());
		Assert.assertTrue("The file should be readable.", level2File.canRead());
		LoadArff arffLoader = new LoadArff(level2File);
		arffLoader.setClassIndex(arffLoader.getStructure().attribute("SampleStatus").index());
		Assert.assertEquals("The excpected class should be: ",
				arffLoader.getStructure().attribute("SampleStatus").index(), arffLoader.getStructure().classIndex());
		try {
			ClassifierSelection classifierSelection = new ClassifierSelectionImpl();
			AbstractClassifier abstractClassifier = classifierSelection.selectClassifier(Constants.NAIVE_BAYES,
					arffLoader);
			classifierSelection.classify(abstractClassifier, arffLoader);
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail(e.getMessage());
		}
	}

	/**
	 * Test naive bayes cross validation evaluation arff real data.
	 *
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	@Test
	public void testNaiveBayesCrossValidationEvaluationArffRealData() throws IOException {
		File level2File = new File(Constants.SRC_MAIN_RESOURCES_PATH + "PatientAndCïntrolProcessedLevelTwo.arff");
		LoadArff arffLoader = new LoadArff(level2File);
		arffLoader.setClassIndex(arffLoader.getStructure().attribute("SampleStatus").index());
		Assert.assertEquals("The excpected class should be: ",
				arffLoader.getStructure().attribute("SampleStatus").index(), arffLoader.getStructure().classIndex());
		try {
			ClassifierSelection classifierSelection = new ClassifierSelectionImpl();
			AbstractClassifier abstractClassifier = classifierSelection.selectClassifier(Constants.NAIVE_BAYES,
					arffLoader);
			classifierSelection.crossValidationEvaluation(abstractClassifier, arffLoader.getDataSet(), 10,
					new Random(1));
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail(e.getMessage());
		}
	}

	/**
	 * Find index.
	 */
	@Deprecated
	@Test
	public void findIndex() {
		File file = new File(Constants.SRC_MAIN_RESOURCES_PATH + "PatientAndCïntrolProcessed.csv");
		List<String> records = new ArrayList<>();
		try (CSVReader csvReader = new CSVReader(new FileReader(file));) {
			String[] values = null;
			values = csvReader.readNext();
			System.out.println(Arrays.toString(values).split(EnumSeparators.TAB.getSeparator()).length);
			// values = csvReader.readNext();
			// values = csvReader.readNext();
			values = Arrays.toString(values).replace("[", "").replace("]", "").split(EnumSeparators.TAB.getSeparator());
			records = Arrays.asList(values);
			System.out.println(records.get(73664));
			for (String record : records) {
				printNonNumericValues(record);
				if (record.equalsIgnoreCase("PrimaryTumor")) {
					int index = records.indexOf("PrimaryTumor");
					Assert.assertEquals(73664, index);
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Prints the non numeric values.
	 *
	 * @param record the record
	 */
	@Deprecated
	private static void printNonNumericValues(String record) {
		if (!NumberUtils.isCreatable(record)) {
			System.out.println(record);
		}
	}
}
