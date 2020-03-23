package interfaces;

import weka.core.Instances;

/**
 * The Interface DimensionalityReductionSelectionInterface.
 */
public interface DimensionalityReductionSelection {

	/**
	 * Dimensionality reduction selector.
	 *
	 * @param selection the selection
	 * @param dataset   the dataset
	 * @param debug     the debug
	 * @param options   the options
	 * @return the instances
	 */
	public Instances DimensionalityReductionSelector(String selection, Instances dataset, boolean debug,
			String[] options);
}
