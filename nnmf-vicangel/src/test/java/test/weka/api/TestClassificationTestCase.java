package test.weka.api;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.util.Random;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import com.opencsv.CSVReader;

import dimensionality_reduction_methods.DimensionalityReductionSelection;
import helpful_classes.ClassifierSelectionImpl;
import helpful_classes.Constants;
import helpful_classes.NaiveBayesImplementation;
import helpful_classes.ZeroRImplementation;
import interfaces.ClassifierSelection;
import utilpackage.WekaUtils;
import weka.api.library.LoadArff;
import weka.classifiers.AbstractClassifier;
import weka.core.Instances;

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
	@Ignore
	@Test
	public void testNaiveBayesClassificationArffRealData() throws IOException {
		File level2File = new File(Constants.SRC_MAIN_RESOURCES_PATH + "PatientAndCïntrolProcessedLevelTwo.arff");
		Assert.assertTrue("The file should exists", level2File.exists());
		Assert.assertTrue("The file should be readable.", level2File.canRead());
		LoadArff arffLoader = new LoadArff(level2File);
		arffLoader.setClassIndex(arffLoader.getStructure().attribute("SampleStatus").index());
		Assert.assertEquals("The excpected class should be: ",
				arffLoader.getStructure().attribute("SampleStatus").index(), arffLoader.getStructure().classIndex());
		AbstractClassifier abstractClassifier = WekaUtils.getClassifier(Constants.NAIVE_BAYES, arffLoader);
		new NaiveBayesImplementation().classify(abstractClassifier, arffLoader);
	}

	/**
	 * Test naive bayes cross validation evaluation arff real data.
	 *
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	@Ignore
	@Test
	public void testNaiveBayesCrossValidationEvaluationArffRealData() throws IOException {
		File level2File = new File(Constants.SRC_MAIN_RESOURCES_PATH + "PatientAndCïntrolProcessedLevelTwo.arff");
		LoadArff arffLoader = new LoadArff(level2File);
		arffLoader.setClassIndex(arffLoader.getStructure().attribute("SampleStatus").index());
		Assert.assertEquals("The excpected class should be: ",
				arffLoader.getStructure().attribute("SampleStatus").index(), arffLoader.getStructure().classIndex());
		AbstractClassifier abstractClassifier = WekaUtils.getClassifier(Constants.NAIVE_BAYES, arffLoader);
		new NaiveBayesImplementation().crossValidationEvaluation(abstractClassifier, arffLoader.getDataSet(), 10,
				new Random(1));
	}

	/**
	 * Test zero R.
	 *
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	@Ignore
	@Test
	public void testZeroR() throws IOException {
		File level2File = new File(Constants.SRC_MAIN_RESOURCES_PATH + "PatientAndCïntrolProcessedLevelTwo.arff");
		LoadArff arffLoader = new LoadArff(level2File);
		arffLoader.setClassIndex(arffLoader.getStructure().attribute("SampleStatus").index());
		AbstractClassifier abstractClassifier = WekaUtils.getClassifier(Constants.ZERO_R, arffLoader);
		new ZeroRImplementation().crossValidationEvaluation(abstractClassifier, arffLoader.getDataSet(), 10,
				new Random(1));
	}

	/**
	 * Test naive bayes cross validation evaluation arff real data with
	 * dimensionality reduction.
	 *
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	@Ignore
	@Test
	public void testNaiveBayesCrossValidationEvaluationArffRealDataWithDimensionalityReduction() throws IOException {
		File level2File = new File(Constants.SRC_MAIN_RESOURCES_PATH + "PatientAndCïntrolProcessedLevelTwo.arff");
		LoadArff arffLoader = new LoadArff(level2File);
		arffLoader.setClassIndex(arffLoader.getStructure().attribute("SampleStatus").index());
		// DIMENSIONALITY REDUCTION
		DimensionalityReductionSelection dimensionalityReductionSelection = new DimensionalityReductionSelection();
		try {
			// http://weka.sourceforge.net/doc.dev/weka/filters/unsupervised/attribute/PrincipalComponents.html
			String[] options = weka.core.Utils.splitOptions("-C -R 0.95 -A 5 -M -1");
			Instances dataset = dimensionalityReductionSelection.DimensionalityReductionSelector("pca",
					arffLoader.getDataSet(), true, options);
			// CROSS VALIDATION
			AbstractClassifier abstractClassifier = WekaUtils.getClassifier(Constants.NAIVE_BAYES, arffLoader);
			new NaiveBayesImplementation().crossValidationEvaluation(abstractClassifier, dataset, 10, new Random(1));
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail(e.getMessage());
		}
	}
	
	@Test
	public void testReadCSVTestCase() throws IOException {
		String fileName1 = "C:\\Users\\vic\\Documents\\Bioscience\\patientAndControlData.csv";
		String fileName2 = "C:\\Users\\vic\\Documents\\Bioscience\\Christina's work\\allTogether.csv";
		try (CSVReader csvReader = new CSVReader(new FileReader(fileName1));) {
			csvReader.readNext();
			try (Writer writer = new BufferedWriter(
					new OutputStreamWriter(new FileOutputStream("C:\\Users\\vic\\Documents\\Bioscience\\results"), StandardCharsets.UTF_8))) {
				
			}
	}
	}
		

}
