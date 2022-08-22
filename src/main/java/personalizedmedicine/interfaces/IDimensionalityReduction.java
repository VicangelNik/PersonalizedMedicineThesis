package java.personalizedmedicine.interfaces;

import weka.core.Instances;

/**
 * The Interface DimensionalityReductionInterface.
 */
public interface IDimensionalityReduction {

	/**
	 * Dim reduction method.
	 *
	 * @param options the options
	 * @return the instances
	 * @throws Exception the exception
	 */
	public Instances dimReductionMethod(String[] options) throws Exception;
}
