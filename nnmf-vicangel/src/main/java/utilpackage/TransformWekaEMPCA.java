package utilpackage;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.stream.Collectors;

import org.scify.EMPCA.Feature;

import cern.colt.matrix.tdouble.DoubleMatrix1D;
import cern.colt.matrix.tdouble.DoubleMatrix2D;
import weka.core.Attribute;
import weka.core.DenseInstance;
import weka.core.Instance;
import weka.core.Instances;

/**
 * The Class TransformWekaEMPCA.
 */
public final class TransformWekaEMPCA {

	/**
	 * Instantiates a new transform weka EMPCA.
	 */
	private TransformWekaEMPCA() {
		throw new IllegalArgumentException("utillity class");
	}

	/**
	 * Creates the EMPCA input from weka.
	 *
	 * @param originalDataset the original dataset
	 * @return the list
	 */
	public static List<ArrayList<Feature>> createEMPCAInputFromWeka(Instances originalDataset) {
		List<String> nanFeatureList = new ArrayList<>();
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
	 * Eigens to weka.
	 *
	 * @param eigenVectors   the eigen vectors
	 * @param nameNewDataset the name new dataset
	 * @return the instances
	 */
	public static Instances eigensToWeka(DoubleMatrix2D eigenVectors, String nameNewDataset) {
		// We can not set values to numeric attributes directly!!!Thus we construct new
		// data set with its attributes and no values.
		// columns represent the dimensions and rows the number of values values
		ArrayList<Attribute> attInfo = new ArrayList<>();
		// set attributes to data set
		for (int i = 0; i < eigenVectors.columns(); i++) {
			attInfo.add(new Attribute("dimension" + i));
		}
		Instances instances = new Instances(nameNewDataset, attInfo, eigenVectors.rows());
		// set instances to data set
		for (int i = 0; i < eigenVectors.rows(); i++) {
			instances.add(new DenseInstance(eigenVectors.columns()));
		}
		// fill data set with values
		for (int i = 0; i < eigenVectors.columns(); i++) {
			DoubleMatrix1D matrix1D = eigenVectors.viewColumn(i);
			for (int j = 0; j < eigenVectors.rows(); j++) {
				// number of columns = number of features
				// number of number of rows = number of instances in our case
				instances.get(j).setValue(i, matrix1D.get(j));
			}
		}
		return instances;
	}

	/**
	 * Check is null or infinity.
	 *
	 * @param value the value
	 * @param name  the name
	 * @param list  the list
	 * @return true, if successful
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
