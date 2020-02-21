package utilpackage;

import java.util.ArrayList;

import cern.colt.matrix.tdouble.DoubleMatrix1D;
import cern.colt.matrix.tdouble.DoubleMatrix2D;
import weka.core.Attribute;
import weka.core.DenseInstance;
import weka.core.Instances;

/**
 * The Class TransformToWeka.
 */
public final class TransformToWeka {

	private TransformToWeka() {
		throw new IllegalArgumentException("utillity class");
	}

	/**
	 * Eigens to weka.
	 *
	 * @param eigenVectors   the eigen vectors
	 * @param nameNewDataset the name of the new dataset
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
}
