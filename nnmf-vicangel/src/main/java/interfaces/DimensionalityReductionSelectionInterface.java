package interfaces;

import weka.core.Instances;

// TODO: Auto-generated Javadoc
/**
 * The Interface DimensionalityReductionSelectionInterface.
 */
public interface DimensionalityReductionSelectionInterface {

	/**
	 * Dimensionality reduction selector.
	 *
	 * @param selection the selection
	 * @param dataset   the dataset
	 * @param debug     the debug
	 * @param options   the options
	 * @return the instances
	 * @throws Exception the exception
	 */
	public Instances DimensionalityReductionSelector(String selection, Instances dataset, boolean debug,
			String[] options) throws Exception;
}
