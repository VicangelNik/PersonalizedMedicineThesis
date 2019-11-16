package dimensionality_reduction_methods;

import abstract_classes.DimensionalityReduction;
import helpful_classes.Constants;
import interfaces.DimensionalityReductionSelectionInterface;
import weka.core.Instances;

// TODO: Auto-generated Javadoc
/**
 * The Class DimensionalityReductionSelection.
 */
public class DimensionalityReductionSelection implements DimensionalityReductionSelectionInterface {

	/*
	 * (non-Javadoc)
	 * 
	 * @see interfaces.DimensionalityReductionSelectionInterface#
	 * DimensionalityReductionSelector(java.lang.String, weka.core.Instances,
	 * boolean, java.lang.String[])
	 */
	@Override
	public Instances DimensionalityReductionSelector(String selection, Instances dataset, boolean debug,
			String[] options) throws Exception {
		DimensionalityReduction reductionMethod;
		switch (selection) {
		case Constants.PCA: {
			reductionMethod = new PrincipalComponentAnalysisWeka();
			setValuesToDimensionalityReduction(reductionMethod, dataset, debug);
			return ((PrincipalComponentAnalysisWeka) reductionMethod).dimReductionMethod(options);
		}
		default: {
			throw new IllegalArgumentException("Invalid selection");
		}
		}
	}

	/**
	 * Sets the values to dimensionality reduction.
	 *
	 * @param reductionMethod the reduction method
	 * @param dataset         the dataset
	 * @param debug           the debug
	 */
	private void setValuesToDimensionalityReduction(DimensionalityReduction reductionMethod, Instances dataset,
			boolean debug) {
		reductionMethod.setDataset(dataset);
		reductionMethod.setDebug(debug);
	}
}
