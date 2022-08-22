package dimensionality_reduction_methods;

import interfaces.IDimensionalityReduction;
import smile.manifold.IsoMap;
import utilpackage.TransformToFromWeka;
import utilpackage.WekaUtils;
import weka.core.Instances;

/**
 * The Class IsomapSmile.
 */
public class IsomapSmile extends DimensionalityReduction implements IDimensionalityReduction {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * interfaces.IDimensionalityReduction#dimReductionMethod(java.lang.String[])
	 */
	@Override
	public Instances dimReductionMethod(String[] options) throws Exception {
		// first option should be the number of expected dimensions
		// second option should be the k nearest neighbors
		// third option should be whether C-Isomap or standard algorithm
		// the last 2 options will be always the dataset name and the name of the class
		double data[][] = TransformToFromWeka.transformWekaToManifolds(this.getDataset());
		IsoMap myIsomap = new IsoMap(data, Integer.parseInt(options[0]), Integer.parseInt(options[1]),
				Boolean.parseBoolean(options[2]));
		double[][] coordinates = myIsomap.getCoordinates();
		return TransformToFromWeka.manifoldsToWeka(coordinates, options[3],
				WekaUtils.getDatasetClassValues(this.getDataset()), options[4]);
	}
}
