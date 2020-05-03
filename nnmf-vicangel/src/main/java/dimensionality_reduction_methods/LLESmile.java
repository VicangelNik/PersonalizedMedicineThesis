package dimensionality_reduction_methods;

import abstract_classes.DimensionalityReduction;
import interfaces.IDimensionalityReduction;
import smile.manifold.LLE;
import utilpackage.TransformToFromWeka;
import utilpackage.WekaUtils;
import weka.core.Instances;

/**
 * The Class LLESmile.
 */
public class LLESmile extends DimensionalityReduction implements IDimensionalityReduction {

	/* (non-Javadoc)
	 * @see interfaces.IDimensionalityReduction#dimReductionMethod(java.lang.String[])
	 */
	@Override
	public Instances dimReductionMethod(String[] options) throws Exception {
		// first option should be the number of expected dimensions
		// second option should be the k nearest neighbors
		// the last 2 options will be always the dataset name and the name of the class
		double data[][] = TransformToFromWeka.transformWekaToManifolds(this.getDataset());
		LLE lle = new LLE(data, Integer.parseInt(options[0]), Integer.parseInt(options[1]));
		double[][] coordinates = lle.getCoordinates();
		return TransformToFromWeka.manifoldsToWeka(coordinates, options[2],
				WekaUtils.getDatasetClassValues(this.getDataset()), options[3]);
	}
}
