import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Random;

import org.junit.Test;
import org.scify.EMPCA.EMPCA;
import org.scify.EMPCA.Feature;
import org.scify.EMPCA.JavaPCAInputToScala;

import cern.colt.matrix.tdouble.DoubleMatrix2D;
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

	/**
	 * Test.
	 *
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	@Test
	public void test() throws IOException {

//		ArrayList<ArrayList<Feature>> empcaInput = convertWekaForEMPCAInput("PatientAndControlProcessedLevelTwo.arff",
//				"SampleStatus");
		EMPCA empca = new EMPCA(JavaPCAInputToScala.convert(testData()), 50);
		DoubleMatrix2D c = empca.performEM(20);
		System.out.println("finish perform em");
		Tuple2<double[], DoubleMatrix2D> eigenValueAndVectors = empca.doEig(c);
		System.out.println("finish");
	}

	/**
	 * Convert weka for EMPCA input.
	 *
	 * @param fileName  the file name
	 * @param className the class name
	 * @return the array list
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	private ArrayList<ArrayList<Feature>> convertWekaForEMPCAInput(String fileName, String className)
			throws IOException {
		File level2File = new File(Constants.SRC_MAIN_RESOURCES_PATH + fileName);
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
				features.add(new Feature(attIndex, current.value(attIndex)));
			}
			empcaInput.add(features);
		}
		return empcaInput;
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

}
