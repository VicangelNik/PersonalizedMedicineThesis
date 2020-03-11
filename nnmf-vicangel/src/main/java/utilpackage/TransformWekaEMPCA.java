package utilpackage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;
import java.util.stream.Collectors;

import org.scify.EMPCA.Feature;

import cern.colt.matrix.tdouble.DoubleMatrix1D;
import cern.colt.matrix.tdouble.DoubleMatrix2D;
import helpful_classes.Constants;
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
	 * @return the list of instances and each instance contains a list of feature
	 *         values.
	 */
	// ---------features1....N
	// --------------------------
	// instance1
	// instance2
	// .
	// .
	// instanceN|
	@Deprecated
	public static List<ArrayList<Feature>> createEMPCAInputFromWeka(Instances originalDataset) {
		List<String> nanFeatureList = new ArrayList<>();
		ArrayList<ArrayList<Feature>> empcaInput = new ArrayList<>();
		Enumeration<Instance> instEnumeration = originalDataset.enumerateInstances();
		int classIndex = originalDataset.classIndex();
		while (instEnumeration.hasMoreElements()) {
			Instance current = instEnumeration.nextElement();
			ArrayList<Feature> features = new ArrayList<>();
			// add the class feature because is not in included in attribute enumeration
			features.add(new Feature(classIndex, current.value(classIndex)));
			// enumerates all attributes except class attribute
			Enumeration<Attribute> attEnumeration = current.enumerateAttributes();
			while (attEnumeration.hasMoreElements()) {
				Attribute attribute = attEnumeration.nextElement();
				int attIndex = attribute.index();
				double value = current.value(attIndex);
				// the check was helpful because data preprocess was incomplete and it had
				// missing values and the current.value(attIndex) was returning nulls.
				// After that, ReplaceMissingValues method made data complete without missing
				// values.
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
		return empcaInput;
	}

	/**
	 * Creates the EMPCA input from weka.
	 *
	 * @param originalDataset
	 * @return the list of features and each features contains a list of its
	 *         instance values.
	 */
	// ---------Instnace1....N
	// --------------------------
	// Feature1
	// Feature2
	// .
	// .
	// featureN|
	public static List<ArrayList<Feature>> createEMPCAInputFromWekaV2(Instances originalDataset) {
		ArrayList<ArrayList<Feature>> empcaInput = new ArrayList<>();
		Enumeration<Attribute> attEnumeration = originalDataset.enumerateAttributes();
		while (attEnumeration.hasMoreElements()) {
			Attribute attribute = attEnumeration.nextElement();
			int attIndex = attribute.index();
			ArrayList<Feature> features = new ArrayList<>();
			for (int i = 0; i < originalDataset.numInstances(); i++) {
				Instance current = originalDataset.get(i);
				double value = current.value(attIndex);
				features.add(new Feature(i, value));
			}
			empcaInput.add(features);
		}
		return empcaInput;
	}

	/**
	 * Eigens to weka.
	 *
	 * @param eigenVectors   the eigen vectors
	 * @param nameNewDataset the name new dataset
	 * @return the instances
	 */
	public static Instances eigensToWeka(DoubleMatrix2D eigenVectors, String nameNewDataset, List<Double> classValues) {
		// We can not set values to numeric attributes directly!!!Thus we construct new
		// data set with its attributes and no values.
		// columns represent the dimensions and rows the number of values values
		ArrayList<Attribute> attInfo = new ArrayList<>();
		// set attributes to data set
		for (int i = 0; i < eigenVectors.columns(); i++) {
			attInfo.add(new Attribute("dimension" + i));
		}
		// add class
		attInfo.add(new Attribute("class", Arrays.asList(Constants.PRIMARY_TUMOR, Constants.MORMAL_TISSUE)));
		Instances instances = new Instances(nameNewDataset, attInfo, eigenVectors.rows());
		// set instances to data set
		for (int i = 0; i < eigenVectors.rows(); i++) {
			instances.add(new DenseInstance(eigenVectors.columns() + 1));
		}
		List<String> attributeClassValues = convertClassDoubleValuesToNominal(classValues);
		// fill data set with values
		for (int i = 0; i < eigenVectors.columns(); i++) {
			DoubleMatrix1D matrix1D = eigenVectors.viewColumn(i);
			for (int j = 0; j < eigenVectors.rows(); j++) {
				// number of columns = number of features
				// number of rows = number of instances in our case
				instances.get(j).setValue(i, matrix1D.get(j));
			}
		}
		instances.setClassIndex(eigenVectors.columns());
		// fill the class data
		for (int j = 0; j < eigenVectors.rows(); j++) {
			instances.get(j).setValue(instances.classIndex(), attributeClassValues.get(j));
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

	/**
	 * Converts class double values to Nominal. 0 maps to Primary tumor while 1 maps
	 * to Normal tissue.
	 *
	 * @param classValues
	 * @return
	 */
	private static List<String> convertClassDoubleValuesToNominal(List<Double> classValues) {
		List<String> attributeValues = new ArrayList<>();
		classValues.forEach(x -> {
			if (x.equals(0.0)) {
				attributeValues.add(helpful_classes.Constants.PRIMARY_TUMOR);
			} else {
				attributeValues.add(helpful_classes.Constants.MORMAL_TISSUE);
			}
		});
		return attributeValues;
	}

}
