import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.stream.Collectors;

import org.junit.Assert;
import org.junit.Test;
import org.scify.EMPCA.EMPCA;
import org.scify.EMPCA.Feature;
import org.scify.EMPCA.JavaPCAInputToScala;

import cern.colt.matrix.tdouble.DoubleMatrix2D;
import helpful_classes.AppLogger;
import helpful_classes.Constants;
import helpful_classes.NaiveBayesImplementation;
import scala.Tuple2;
import utilpackage.TransformWekaEMPCA;
import utilpackage.WekaUtils;
import weka.classifiers.AbstractClassifier;
import weka.core.Attribute;
import weka.core.Instances;

// TODO: Auto-generated Javadoc
/*
 * The standard Scala backend is a Java VM. Scala classes are Java classes, and vice versa.
 * You can call the methods of either language from methods in the other one.
 * You can extend Java classes in Scala, and vice versa.
 * The main limitation is that some Scala features do not have equivalents in Java, for example traits.
 */

/**
 * The Class TestEMPCA.
 */
public class TestEMPCA {

	/** The logger. */
	private static AppLogger logger = AppLogger.getInstance();

	/**
	 * Test.
	 *
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	@Test
	public void test() throws IOException {
		File level2File = new File(Constants.SRC_MAIN_RESOURCES_PATH + "PatientAndControlProcessedLevelTwo.arff");
		Instances originalDataset = WekaUtils.getOriginalData(level2File, "SampleStatus");
		// INPUT TO EMPCA PART
		List<ArrayList<Feature>> empcaInput = TransformWekaEMPCA.createEMPCAInputFromWeka(originalDataset);
		Assert.assertTrue("The number of lists (instances) should be 335", 335 == empcaInput.size());
		// the position 3 is chosen arbitarily.
		Assert.assertTrue("The number of features of each instance should be 72122", 72122 == empcaInput.get(3).size());
		scala.collection.immutable.List<Tuple2<Object, Object>>[] convertedToScalaList = JavaPCAInputToScala
				.convert((ArrayList<ArrayList<Feature>>) empcaInput);
		Assert.assertEquals("The sizes of the 2 arrays should be the same", empcaInput.size(),
				convertedToScalaList.length);
		Assert.assertEquals("The sizes of the 2 arrays should be the same", empcaInput.get(3).size(),
				convertedToScalaList[3].length());
		// DO EMPCA PART
		EMPCA empca = new EMPCA(convertedToScalaList, 20);
		DoubleMatrix2D c = empca.performEM(20);
		System.out.println("finish perform em");
		Tuple2<double[], DoubleMatrix2D> eigenValueAndVectors = empca.doEig(c);
		writeEigensToFile(Constants.loggerPath + "output.log", eigenValueAndVectors);
		System.out.println("finish doEig");
		// OUTPUT TO WEKA PART
		// zero position of each instance's feature list contains the class value. Class
		// values are PrimaryTumor, NormalTissue. PrimaryTumor -> 0, NormalTissue-> 1
		List<Double> classValues = empcaInput.stream().map(x -> x.get(0).getValue()).collect(Collectors.toList());
		Instances reData = TransformWekaEMPCA.eigensToWeka(eigenValueAndVectors._2, "empcaDataset", classValues);
		Assert.assertTrue(reData.numAttributes() == eigenValueAndVectors._2.columns());
		Assert.assertTrue(reData.numInstances() == eigenValueAndVectors._2.rows());
		// CROSS VALIDATION
		AbstractClassifier abstractClassifier = WekaUtils.getClassifier(Constants.NAIVE_BAYES, reData);
		new NaiveBayesImplementation().crossValidationEvaluation(abstractClassifier, originalDataset, 10,
				new Random(1));
	}

	/**
	 * Prints the eigen values vectors.
	 *
	 * @param eigenValueAndVectors the eigen value and vectors
	 */
	@SuppressWarnings("unused")
	private static void printEigenValuesVectors(Tuple2<double[], DoubleMatrix2D> eigenValueAndVectors) {
		System.out.println("start printing values");
		logger.getLogger().log(Level.INFO, "EigenValues" + System.lineSeparator());
		for (int i = 0; i < eigenValueAndVectors._1.length; i++) {
			double value = eigenValueAndVectors._1[i];
			logger.getLogger().log(Level.INFO, String.valueOf(value) + "\t");
		}
		logger.getLogger().log(Level.INFO, "EigenVectors" + System.lineSeparator());
		for (int i = 0; i < eigenValueAndVectors._2.columns(); i++) {
			logger.getLogger().log(Level.INFO, String.valueOf(eigenValueAndVectors._2.viewColumn(i)));
		}
	}

	/**
	 * Write eigens to file.
	 *
	 * @param fileName             the file name
	 * @param eigenValueAndVectors the eigen value and vectors
	 */
	private static void writeEigensToFile(String fileName, Tuple2<double[], DoubleMatrix2D> eigenValueAndVectors) {
		try (BufferedWriter bw = new BufferedWriter(new FileWriter(fileName))) {
			bw.write("Number of eigenValues: " + eigenValueAndVectors._1.length + System.lineSeparator());
			bw.write("Number of eigenVectors (rows): " + eigenValueAndVectors._2.rows() + System.lineSeparator());
			bw.write("Number of eigenVectors (columns): " + eigenValueAndVectors._2.columns() + System.lineSeparator());
			bw.newLine();
			for (int i = 0; i < eigenValueAndVectors._1.length; i++) {
				bw.write(String.valueOf(eigenValueAndVectors._1[i]) + System.lineSeparator());
			}
			for (int i = 0; i < eigenValueAndVectors._2.columns(); i++) {
				bw.write("The number of size for the " + i + "th is: " + eigenValueAndVectors._2.viewColumn(i).size()
						+ System.lineSeparator());
				bw.write(String.valueOf(eigenValueAndVectors._2.viewColumn(i)));
				bw.newLine();
			}
			bw.flush();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			// nothing to do
		}

	}

	/**
	 * Test random data set.
	 */
	@Test
	public void testRandomDataSet() {
		ArrayList<ArrayList<Feature>> dataset = testData();
		scala.collection.immutable.List<Tuple2<Object, Object>>[] convertedToScalaList = JavaPCAInputToScala
				.convert(dataset);
		// DO EMPCA PART
		EMPCA empca = new EMPCA(convertedToScalaList, 20);
		DoubleMatrix2D c = empca.performEM(20);
		System.out.println("finish perform em");
		Tuple2<double[], DoubleMatrix2D> eigenValueAndVectors = empca.doEig(c);
		System.out.println("finish doEig");
		System.out.println("Number of eigenValues: " + eigenValueAndVectors._1.length + System.lineSeparator());
		System.out.println("Number of eigenVectors (rows): " + eigenValueAndVectors._2.rows() + System.lineSeparator());
		System.out.println(
				"Number of eigenVectors (columns): " + eigenValueAndVectors._2.columns() + System.lineSeparator());
	}

	/**
	 * Test data.
	 *
	 * @return the array list
	 */
	private static ArrayList<ArrayList<Feature>> testData() {
		Random rand = new Random();
		DecimalFormat df = new DecimalFormat("#.###");
		ArrayList<ArrayList<Feature>> empcaInput = new ArrayList<>();
		for (int i = 0; i < 45; i++) {
			ArrayList<Feature> features = new ArrayList<>();
			for (int j = 0; j < 750; j++) {
				double random = rand.nextDouble();
				features.add(new Feature(j, Double.parseDouble(df.format(random))));
			}
			empcaInput.add(features);
		}
		return empcaInput;
	}

	/**
	 * Example test.
	 *
	 * @return the array list
	 */
	public static ArrayList<ArrayList<Feature>> exampleTest() {
		ArrayList<ArrayList<Feature>> data = new ArrayList<>();
		ArrayList<Feature> instance1 = new ArrayList<>();
		instance1.add(new Feature(1, 0.1));
		instance1.add(new Feature(2, 0.2));
		instance1.add(new Feature(3, 0.245));
		instance1.add(new Feature(4, 0.34));
		instance1.add(new Feature(5, 0.2));
		instance1.add(new Feature(6, 0.3));
		instance1.add(new Feature(7, 0.5));
		ArrayList<Feature> instance2 = new ArrayList<>();
		instance2.add(new Feature(1, 0.543));
		instance2.add(new Feature(2, 0.54));
		instance2.add(new Feature(3, 0.245));
		instance2.add(new Feature(4, 0.3234));
		instance2.add(new Feature(5, 0.24));
		instance2.add(new Feature(6, 0.24));
		instance2.add(new Feature(7, 0.43));

		data.add(instance1);
		data.add(instance2);
		return data;
	}

	/**
	 * Check is nominal.
	 *
	 * @param attribute the attribute
	 */
	@SuppressWarnings("unused")
	private void checkIsNominal(Attribute attribute) {
		if (attribute.isNominal()) {
			System.out.println(attribute.name());
		}
	}
}
