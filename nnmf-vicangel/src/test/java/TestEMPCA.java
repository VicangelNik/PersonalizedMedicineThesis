import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;

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

	/**
	 * Test.
	 *
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	@Test
	public void test() throws IOException {

		ArrayList<ArrayList<Feature>> empcaInput = convertWekaForEMPCAInput("PatientAndControlProcessedLevelTwo.arff",
				"SampleStatus");
		EMPCA empca = new EMPCA(JavaPCAInputToScala.convert(empcaInput), 50);
		DoubleMatrix2D c = empca.performEM(20);
		Tuple2<double[], DoubleMatrix2D> eigenValueAndVectors = empca.doEig(c);

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
