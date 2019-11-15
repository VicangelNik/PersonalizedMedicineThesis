package interfaces;

import weka.core.Instances;

// TODO: Auto-generated Javadoc
/**
 * The Interface DimensionalityReductionInterface.
 */
public interface DimensionalityReductionInterface {

	/**
	 * Dim reduction method.
	 *
	 * @param options the options
	 * @return the instances
	 * @throws Exception the exception
	 */
	public Instances dimReductionMethod(String[] options) throws Exception;
}
