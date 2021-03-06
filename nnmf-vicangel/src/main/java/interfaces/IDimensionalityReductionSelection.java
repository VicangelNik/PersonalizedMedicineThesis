package interfaces;

import weka.core.Instances;

/**
 * The Interface IDimensionalityReductionSelection.
 */
public interface IDimensionalityReductionSelection {

	/**
	 * Dimensionality reduction selector.
	 *
	 * @param selection the selection
	 * @param dataset   the dataset
	 * @param debug     the debug
	 * @param options   the options
	 * @return the instances
	 */
	public Instances dimensionalityReductionSelector(String selection, Instances dataset, boolean debug,
			String[] options);
}
