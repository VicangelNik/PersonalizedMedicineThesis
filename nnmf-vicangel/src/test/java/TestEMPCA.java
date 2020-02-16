import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.stream.Collectors;

import org.junit.Test;
import org.scify.EMPCA.EMPCA;
import org.scify.EMPCA.Feature;
import org.scify.EMPCA.JavaPCAInputToScala;

import cern.colt.matrix.tdouble.DoubleMatrix2D;
import helpful_classes.AppLogger;
import helpful_classes.Constants;
import scala.Tuple2;
import utilpackage.WekaUtils;
import weka.core.Attribute;
import weka.core.Instance;
import weka.core.Instances;

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
	private static AppLogger logger = AppLogger.getInstance();

	/**
	 * Test.
	 *
	 * @throws IOException
	 */
	@Test
	public void test() throws IOException {
		// PrintStream out = new PrintStream(new FileOutputStream(Constants.loggerPath +
		// "project.log", true), true);
		// System.setOut(out);
		ArrayList<ArrayList<Feature>> empcaInput = convertWekaForEMPCAInput("PatientAndControlProcessedLevelTwo.arff",
				"SampleStatus");
		EMPCA empca = new EMPCA(JavaPCAInputToScala.convert(empcaInput), 20);
		DoubleMatrix2D c = empca.performEM(20);
		System.out.println("finish perform em");
		Tuple2<double[], DoubleMatrix2D> eigenValueAndVectors = empca.doEig(c);
		writeEigensToFile(Constants.loggerPath + "output.log", eigenValueAndVectors);
		System.out.println("finish doEig");
	}

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
	 * Convert weka for EMPCA input.
	 *
	 * @param fileName  the file name
	 * @param className the class name
	 * @return the array list
	 * @throws Exception
	 */
	private ArrayList<ArrayList<Feature>> convertWekaForEMPCAInput(String fileName, String className)
			throws IOException {
		File level2File = new File(Constants.SRC_MAIN_RESOURCES_PATH + fileName);
		List<String> nanFeatureList = new ArrayList<>();
		Instances originalDataset = WekaUtils.getOriginalData(level2File, className);
		ArrayList<ArrayList<Feature>> empcaInput = new ArrayList<>();
		Enumeration<Instance> instEnumeration = originalDataset.enumerateInstances();
		while (instEnumeration.hasMoreElements()) {
			Instance current = instEnumeration.nextElement();
			ArrayList<Feature> features = new ArrayList<>();
			Enumeration<Attribute> attEnumeration = current.enumerateAttributes();
			while (attEnumeration.hasMoreElements()) {
				Attribute attribute = attEnumeration.nextElement();
				int attIndex = attribute.index();
				double value = current.value(attIndex);
				if (checkIsNullOrInfinity(value, attribute.name(), nanFeatureList)) {
					throw new IllegalArgumentException(
							"At this point data set should not have any NaN or infinity value");
				}
				features.add(new Feature(attIndex, value));
			}
			empcaInput.add(features);
		}
		List<String> distinctElements = nanFeatureList.stream().distinct().collect(Collectors.toList());
		System.out.println("Number of distinct features with NaN values: " + distinctElements.size());
		System.out.println("Number of NaN value occurencies: " + nanFeatureList.size());
		// distinctElements.forEach(System.out::println);
		return empcaInput;
	}

	/**
	 * Write eigen values and eigen vectors to file.
	 *
	 * @param fileName
	 * @param eigenValueAndVectors
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
	 * Test data.
	 *
	 * @return the array list
	 */
	@SuppressWarnings("unused")
	private static ArrayList<ArrayList<Feature>> testData() {
		Random rand = new Random();
		DecimalFormat df = new DecimalFormat("#.###");
		ArrayList<ArrayList<Feature>> empcaInput = new ArrayList<>();
		for (int i = 0; i < 450; i++) {
			ArrayList<Feature> features = new ArrayList<>();
			for (int j = 0; j < 75000; j++) {
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

	/**
	 * checkIsNullOrInfinity
	 *
	 * @param value
	 * @param name
	 * @return
	 */
	private static boolean checkIsNullOrInfinity(double value, String name, List<String> list) {
		if (Double.isInfinite(value) || Double.isNaN(value)) {
			// System.out.println("value: " + value + " Feature: " + name);
			list.add(name);
			return true;
		}
		return false;
	}
}
